# 1. Spring Boot进阶

## 1.1. 2小时学会Spring Boot

### 1.1.1. 2-1 第一个SpringBoot应用

使用IDEA的Spring Initializer直接创建出Web工程，增加@RestController即可直接URL访问。

遇到的问题：

界面显示异常:This application has no explicit mapping for /error, so you are seeing this as a fallback.

参考<https://www.cnblogs.com/lilinzhiyu/p/7921890.html>

出现这个异常说明了跳转页面的url无对应的值.

原因1:

Application启动类的位置不对。**要将Application类放在最外侧,即包含所有子包**

原因:spring-boot会自动加载启动类所在包下及其子包下的所有组件。

```java
//HelloController.java
package com.imooc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say() {
        return "Hello, Spring Boot!";
    }
}

//GirlApplication.java
package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GirlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GirlApplication.class, args);
    }

}

//第一种启动方式：使用IDEA启动GirlApplication类，访问URL：http://localhost:8080/hello，可以看到页面显示：
Hello, Spring Boot!

//第二种启动方式：进入项目根目录E:\Program Files\JetBrains\JavaProject\girl，cmd命令：mvn spring-boot:run，同样可以启动工程。

//第三种启动方式：进入项目根目录E:\Program Files\JetBrains\JavaProject\girl，cmd命令：mvn install，进入到target目录，可以看到多出个girl-0.0.1-SNAPSHOT.jar文件，使用命令：java -jar girl-0.0.1-SNAPSHOT.jar启动工程。
```

### 1.1.2. 3-1 项目属性配置

#### 1.1.2.1. 配置端口和路径：

```java
//application.properties
server.port=8081
#server.context-path=/girl
server.servlet.context-path=/girl

//增加如上配置后，使用URL：http://localhost:8081/girl/hello访问，可以正常看到页面显示

//application.yml 推荐使用，不用写重复的路径，必须在冒号后加空格，使用URL：http://localhost:8082/girl/hello访问
server:
  port: 8082
  servlet:
    context-path: /girl
```

#### 1.1.2.2. 配置参数和变量注入

如下三个注解使用及不同配置文件：

* @Value
* @Component
* @ConfigurationProperties
* 多环境配置

如下对每个配置文件里的参数进行成员变量的注入：

```java
//application.yml
server:
  port: 8080
cupSize: B
age: 18
content: "cupSize: ${cupSize}, age: ${age}" #配置里使用配置参数

//HelloController.java
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //使用配置文件中的参数注入到成员变量中
    @Value("${cupSize}")
    private String cupSize;

    @Value("${age}")
    private Integer age;

    @Value("${content}")
    private String content;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say() {
        return String.format("CupSize in config file is: %s, age: %d, content: %s", cupSize, age, content);
    }
}


//使用http://localhost:8080/hello访问，显示：
CupSize in config file is: B, age: 18, content: cupSize: B, age: 18
```

一个一个写参数的注入太麻烦了，如下一次完成Girl属性的注入：

```java
//application.yml
server:
  port: 8080
girl:
  cupSize: B
  age: 18

//GirlProperties.java
package com.imooc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "girl")
public class GirlProperties {

    private String cupSize;

    private Integer age;

    public String getCupSize() {
        return cupSize;
    }

    public void setCupSize(String cupSize) {
        this.cupSize = cupSize;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

//HelloController.java
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.properties.GirlProperties;

@RestController
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)

    public String say() {
        return String.format("CupSize in config file is: %s, age: %d", girlProperties.getCupSize(),
            girlProperties.getAge());
    }
}

//pom.xml 增加
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>

//http://localhost:8080/hello 页面显示：
CupSize in config file is: B, age: 18
```

如果有不同的配置场景需求，可以修改配置文件如下：

```java
//application.yml 通过修改active里为dev或prod来运行不同的配置文件
spring:
  profiles:
    active: dev

//application-dev.yml
server:
  port: 8080
girl:
  cupSize: B
  age: 18

//application-prod.yml
server:
  port: 8081
girl:
  cupSize: F
  age: 18

//通过cmd命令，增加参数来启动不同的配置文件：
mvn install
java -jar target\girl-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
//或者
java -jar target\girl-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### 1.1.3. 4-1 Controller使用

Controller的使用

* @Controller: 处理http请求
* @RestController：Spring4之后新加的注解，如果返回json格式对象需要@ResponseBody来配合@Controller使用
* @RequestMapping：配置url映射

@Controller使用样例如下：

（这个代码类似原来的JSP，只不过模板的引擎使用的是spring官方的thymeleaf。
现在的开发方式都是前后端分离的，前端只使用后端Rest接口提供的json格式数据，而不是原来的模板方式，性能上有较大的损耗。）

```java
//pom.xml中增加
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

//resources/templates/index.html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Spring Boot</title>
</head>
<body>
<h1>hello Spring Boot!</h1>

</body>
</html>

//HelloController.java
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.imooc.data.GirlProperties;

@Controller
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)

    public String say() {
        return "index.html"; //返回模板
    }
}

//访问url：http://localhost:8080/hello，将看到hello Spring Boot!
```

@Controller+@ResponseBody组合使用赞同于@RestController：

```java
//HelloController.java
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.data.GirlProperties;

@Controller
@ResponseBody
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)

    public String say() {
        return String.format("CupSize in config file is: %s, age: %d", girlProperties.getCupSize(),
            girlProperties.getAge());
    }
}

//不需要提供resources/templates/index.html，也不需要在pom中增加spring-boot-starter-thymeleaf

//访问url：http://localhost:8080/hello，可以看到CupSize in config file is: B, age: 18
```

@RequestMapping使用：

```java
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.data.GirlProperties;

@RestController
@RequestMapping("/good")//给整个类增加地址，也可以写成good不带斜杠
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    //如果不加method参数，则POST和GET方法都能访问到，但是不推荐这么做
    @RequestMapping(value = {"/hello", "/hi"}, method = RequestMethod.POST)
    public String say() {
        return String.format("CupSize in config file is: %s, age: %d", girlProperties.getCupSize(),
            girlProperties.getAge());
    }
}

//则使用http://localhost:8080/good/hello或http://localhost:8080/good/hi都可以访问到
//修改为POST方式，则需要使用POSTMAN或Restlet来访问
```

获取url中参数：

* PathVariable：获取url中的数据
* RequestParam：获取请求参数的值
* GetMapping：组合注解

```java
//HelloController.java
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.data.GirlProperties;

@RestController
@RequestMapping("/good")
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello/{id}", method = RequestMethod.GET)
    public String say(@PathVariable("id") Integer id) {
        return String.format("id: %d", id);
    }
}

//通过url访问：http://localhost:8080/good/hello/100，则页面显示：id: 100

//如果写成value = "/hello{id}，则通过url访问：http://localhost:8080/good/hello1100，页面显示id: 1100


//----------------------使用RequestParam获取参数
//HelloController.java
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.data.GirlProperties;

@RestController
@RequestMapping("/good")
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello", method = RequestMethod.GET) //RequestMethod.POST也适用
    public String say(@RequestParam("id") Integer id) {
        return String.format("RequestParam id: %d", id);
    }
}

//使用url访问：http://localhost:8080/good/hello?id=2100，页面显示：RequestParam id: 2100；使用url访问：http://localhost:8080/good/hello?id=，页面显示RequestParam id: null

//----------------------如果使用http://localhost:8080/good/hello访问，则页面显示异常，此时可以给参数默认值
//修改为如下方式：
package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.data.GirlProperties;

@RestController
@RequestMapping("/good")
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        return String.format("RequestParam id: %d", id);
    }
}
//访问：http://localhost:8080/good/hello，页面显示RequestParam id: 0
```

@RequestMapping需要指定method，较长，推荐使用：

```java
@GetMapping(value = "/hello")
@PostMapping(value = "/hello")
@DeleteMapping(value = "/hello")
@PutMapping(value = "/hello")
```

### 1.1.4. 数据库操作

#### 1.1.4.1. Spring-Data-Jpa

JPA(Java Persistence API)定义了一系列对象持久化的标准，目前实现这一规范的产品有Hiberante、TopLink等。

请求类型|请求路径|功能
-|-|-
GET|/girls|获取女生列表
POST|/girls|创建一个女生
GET|/girls/id|通过id查询一个女生
PUT|/girls/id|通过id更新一个女生
DELETE|/girls/id|通过id删除一个女生

问题1：

总创建一个hibernate_sequence表，参考<https://blog.csdn.net/danchaofan0534/article/details/53608832>

解决：将@GeneratedValue修改为@GeneratedValue(strategy = GenerationType.IDENTITY)

问题2：

没有自动创建girl表

解决：
明确指定dependency版本号version，而不要不带版本号：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.15</version>
</dependency>
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>javax.persistence-api</artifactId>
    <version>2.2</version>
</dependency>
```

样例代码：

```java
//Girl.java
package com.imooc.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_girls")
public class Girl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cupSize;

    private Integer age;

    public Girl() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCupSize() {
        return cupSize;
    }

    public void setCupSize(String cupSize) {
        this.cupSize = cupSize;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

//application.yml
spring:
  profiles:
    active: dev
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/hibernate?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&useSSL=false
    username: root
    password: root123
  jpa:
    hibernate:
      ddl-auto: update
      format_sql: true
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL55Dialect
    open-in-view: false
//其中ddl-auto: update，没有表时就自动创建，有表就不创建；create：每次都会创建，如果有表则删除；create-drop：启动时创建，结束时删除；validate：验证表和Entity是否一样，不一样则报错
//open-in-view：解决一个警告 spring.jpa.open-in-view is enabled by default

//pom.xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>2.1.3.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.15</version>
    </dependency>
    <dependency>
        <groupId>javax.persistence</groupId>
        <artifactId>javax.persistence-api</artifactId>
        <version>2.2</version>
    </dependency>
</dependencies>

//GirlRepository.java
package com.imooc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imooc.domain.Girl;

public interface GirlRepository extends JpaRepository<Girl, Integer> {

    //通过age来查询，方法名ByXXX必须是真实的参数
    List<Girl> findByAge(Integer age);
}


//GirlController.java
package com.imooc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.domain.Girl;
import com.imooc.repository.GirlRepository;
import com.imooc.service.GirlService;

@RestController
public class GirlController {

    @Autowired
    private GirlRepository girlRepository;

    @Autowired
    private GirlService girlService;

    /**
     * 查询所有女生列表 GET http://localhost:8080/girls
     *
     * @return
     */
    @GetMapping(value = "/girls")
    public List<Girl> getGirlList() {
        return girlRepository.findAll();
    }

    /**
     * 添加一个女生 POST http://localhost:8080/girls?cupSize=B&age=17
     *
     * @param cupSize
     * @param age
     * @return
     */
    @PostMapping(value = "/girls")
    public Girl addGirl(@RequestParam("cupSize") String cupSize, @RequestParam("age") Integer age) {
        Girl girl = new Girl();
        girl.setCupSize(cupSize);
        girl.setAge(age);

        return girlRepository.save(girl);
    }

    /**
     * 查询一个女生 GET http://localhost:8080/girls/3
     */
    @GetMapping(value = "/girls/{id}")
    public Girl findOneGirl(@PathVariable("id") Integer id) {
        Optional<Girl> girl = girlRepository.findById(id);
        return girl.orElse(null);
    }

    /**
     * 更新一个女生 PUT http://localhost:8080/girls/3?cupSize=A&age=19 或在Body里以Form格式添加参数
     */
    @PutMapping(value = "/girls/{id}")
    public Girl updateGirl(@PathVariable("id") Integer id, @RequestParam("cupSize") String cupSize, @RequestParam("age") Integer age) {
        Girl girl = new Girl();
        girl.setId(id);
        girl.setCupSize(cupSize);
        girl.setAge(age);

        return girlRepository.save(girl);
    }

    /**
     * 删除一个女生 DELETE http://localhost:8080/girls/3
     */
    @DeleteMapping(value = "/girls/{id}")
    public void deleteGirl(@PathVariable("id") Integer id) {
        girlRepository.deleteById(id);
    }

    //通过age查询女生列表 GET http://localhost:8080/girls/age/35
    @GetMapping(value = "/girls/age/{age}")
    public List<Girl> findGirlsByAge(@PathVariable("age") Integer age) {
        return girlRepository.findByAge(age);
    }

    //增加两个女生，需要做事务管理 POST http://localhost:8080/girls/two 执行会抛异常
    @PostMapping(value = "/girls/two")
    public void insertTwoGirls() {
        girlService.insertTwo();
    }
}
```

### 1.1.5. 事务管理

插入两条数据，要么一起成功，要么一起失败：

```java
//增加一个Service层用来做事务管理：
//GirlService.java
package com.imooc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.domain.Girl;
import com.imooc.repository.GirlRepository;

@Service
public class GirlService {
    @Autowired
    private GirlRepository girlRepository;

    @Transactional //增加了@Transactional注解后，尽管会抛异常，但是数据要么一起入库，要么一起不入库
    public void insertTwo() {
        Girl girlA = new Girl();
        girlA.setCupSize("A");
        girlA.setAge(18);
        girlRepository.save(girlA);

        Girl girlB = new Girl();
        girlB.setCupSize("BBBB"); //把数据库表cupSize字段长度修改为1
        girlB.setAge(19);
        girlRepository.save(girlB);
    }
}
```

### 1.1.6. 总结

1. SpringBoot介绍
2. 安装，使用Idea的Spring Initializer
3. 配置，推荐使用配置分组
4. Controller的使用
5. 数据库操作
6. 事务管理

## 1.2. SprintBoot进阶

### 1.2.1. 使用@Valid表单验证

### 1.2.2. 使用AOP处理请求

### 1.2.3. 统一异常处理

### 1.2.4. 单元测试