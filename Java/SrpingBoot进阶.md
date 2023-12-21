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

import java.util.Optional;

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

    public void getAge(Integer id) throws Exception {
        Optional<Girl> girl = girlRepository.findById(id);
        Integer age = girl.map(Girl::getAge).orElse(null);
        if (age < 10) {
            //返回“你还在上小学吧”
            throw new Exception("你还在上小学吧");
        } else if (age >= 10 && age < 16) {
            //返回“你可能在上初中”
            throw new Exception("你可能在上初中");
        }
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

在成员变量前使用注解@Min定义校验范围，在入参数前增加@Valid，即可获得校验失败时的提示信息

```java
//GirlController.java
package com.imooc.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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
     * @param girl 可以自动将参数值生成在Girl对象中
     * @return
     */
    @PostMapping(value = "/girls")
    public Girl addGirl(@Valid Girl girl, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return null;
        }

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

//Girl.java
package com.imooc.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name="t_girls")
public class Girl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cupSize;

    @Min(value = 18, message = "未成年少女禁止入内")
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

    @Override
    public String toString() {
        return "Girl{" +
            "id=" + id +
            ", cupSize='" + cupSize + '\'' +
            ", age=" + age +
            '}';
    }
}
```

### 1.2.2. 使用AOP处理请求 <https://www.imooc.com/video/14343>

也谈AOP

* AOP是一种编程范式

与语言无关，是一种程序设计思想

面向切面（AOP）Aspect Oriented Programming

面向对象（OOP）Object Oriented Programming

面向过程（POP）Procedure Oriented Programming

* 面向过程到面向对象

面向过程：假如下雨了，我打开了雨伞

面向对象：
> 天气->下雨
>
> 我->打伞

* 换个角度看世界，换个姿势处理问题

* 将通用逻辑从业务逻辑中分离出来

具体实例：

* 记录每一个http请求

```java
//pom.xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>

//HttpAspect.java
package com.imooc.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HttpAspect {
    //如果方法被@Valid拦截住，也会先走到Aspect切面方法中，再打印拦截日志:1111111111\n未成年少女禁止入内
    //@Before在方法执行之前执行Aspect切面。com.imooc.controller.GirlController.getGirlList 指定方法
    @Before("execution(public * com.imooc.controller.GirlController.*(..))")
    public void log() {
        System.out.println(1111111111);
    }

    //@After在方法执行之后执行Aspect切面
    @After("execution(public * com.imooc.controller.GirlController.*(..))")
    public void doAfter() {
        System.out.println(222222222);
    }
}
//输出
1111111111
getGirlList
2019-03-15 22:57:17.733  INFO 6792 --- [nio-8080-exec-1] o.h.h.i.QueryTranslatorFactoryInitiator  : HHH000397: Using ASTQueryTranslatorFactory
Hibernate: select girl0_.id as id1_0_, girl0_.age as age2_0_, girl0_.cup_size as cup_size3_0_ from t_girls girl0_
222222222
```

重复Aspect方法抽成PointCut：

```java
//HttpAspect.java
package com.imooc.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HttpAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAspect.class);

    //定义切点
    @Pointcut("execution(public * com.imooc.controller.GirlController.*(..))")
    public void log() {
    }

    @Before("log()")
    public void deBefore() {
        LOGGER.info("1111111111111");
    }

    @After("log()")
    public void doAfter() {
        LOGGER.info("2222222222222");
    }
}

//GirlController.java
package com.imooc.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(GirlController.class); //这里使用哪个类，日志打印时就显示该的完整包路径

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
        LOGGER.info("getGirlList");
        return girlRepository.findAll();
    }

    /**
     * 添加一个女生 POST http://localhost:8080/girls?cupSize=B&age=17
     *
     * @param girl 可以自动将参数值生成在Girl对象中
     * @return
     */
    @PostMapping(value = "/girls")
    public Girl addGirl(@Valid Girl girl, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return null;
        }

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

//输出：GET http://localhost:8080/girls/
2019-03-15 23:05:34.417  INFO 4696 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : 1111111111111
2019-03-15 23:05:34.420  INFO 4696 --- [nio-8080-exec-1] com.imooc.controller.GirlController      : getGirlList
2019-03-15 23:05:34.444  INFO 4696 --- [nio-8080-exec-1] o.h.h.i.QueryTranslatorFactoryInitiator  : HHH000397: Using ASTQueryTranslatorFactory
Hibernate: select girl0_.id as id1_0_, girl0_.age as age2_0_, girl0_.cup_size as cup_size3_0_ from t_girls girl0_
2019-03-15 23:05:34.513  INFO 4696 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : 2222222222222
```

修改Aspect来记录日志：

```java
//HttpAspect.java
package com.imooc.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class HttpAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAspect.class);

    //定义切点
    @Pointcut("execution(public * com.imooc.controller.GirlController.*(..))")
    public void log() {
    }

    @Before("log()")
    public void deBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        LOGGER.info("url={}", request.getRequestURL());

        //method
        LOGGER.info("method={}", request.getMethod());

        //ip
        LOGGER.info("ip={}", request.getRemoteAddr());

        //类方法
        LOGGER.info("class_method={}.{}", joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName());

        //参数
        LOGGER.info("args={}", joinPoint.getArgs());
    }

    @After("log()")
    public void doAfter() {
        LOGGER.info("2222222222222");
    }

    @AfterReturning(returning ="object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        LOGGER.info("response={}", object);
    }
}

//输出 POST http://localhost:8080/girls/
2019-03-15 23:17:29.522  INFO 11768 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : url=http://localhost:8080/girls/
2019-03-15 23:17:29.522  INFO 11768 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : method=POST
2019-03-15 23:17:29.522  INFO 11768 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : ip=0:0:0:0:0:0:0:1
2019-03-15 23:17:29.523  INFO 11768 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : class_method=com.imooc.controller.GirlController.addGirl
2019-03-15 23:17:29.523  INFO 11768 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : args=Girl{id=null, cupSize='D', age=21}

//GET http://localhost:8080/girls/10
2019-03-15 23:25:02.635  INFO 8600 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : url=http://localhost:8080/girls/10
2019-03-15 23:25:02.635  INFO 8600 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : method=GET
2019-03-15 23:25:02.635  INFO 8600 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : ip=0:0:0:0:0:0:0:1
2019-03-15 23:25:02.636  INFO 8600 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : class_method=com.imooc.controller.GirlController.findOneGirl
2019-03-15 23:25:02.636  INFO 8600 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : args=10
Hibernate: select girl0_.id as id1_0_0_, girl0_.age as age2_0_0_, girl0_.cup_size as cup_size3_0_0_ from t_girls girl0_ where girl0_.id=?
2019-03-15 23:25:02.676  INFO 8600 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : 2222222222222
2019-03-15 23:25:02.676  INFO 8600 --- [nio-8080-exec-1] com.imooc.aspect.HttpAspect              : response=Girl{id=10, cupSize='F', age=35}
```

#### 本地试验：

记日志：<https://www.cnblogs.com/wangshen31/p/9379197.html>

鉴权：<https://blog.csdn.net/luckykapok918/article/details/79287889>

加入依赖：

```xml
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

```java
// WebLogAspect.java
package com.example.restproject.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebLogAspect {
    private final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.example.restproject.controller..*.*(..))")
    public void controllerLog() {
    }

    @Before("controllerLog()")
    public void logBeforeController(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();  //这个RequestContextHolder是Springmvc提供来获得请求的东西
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("THE ARGS OF THE CONTROLLER : " + Arrays.toString(joinPoint.getArgs()));

        // 下面这个getSignature().getDeclaringTypeName()是获取包+类名的，然后后面的joinPoint.getSignature.getName()获取了方法名
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        logger.info("TARGET: " + joinPoint.getTarget());  //返回的是需要加强的目标类的对象
//        logger.info("THIS: " + joinPoint.getThis());  //返回的是经过加强后的代理类的对象
    }
}

// HelloCheckAspect.java
package com.example.restproject.utils;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class HelloCheckAspect {
    @Pointcut("execution(public * com.example.restproject.controller.HelloController.*(..))")
    public void checkId() {
    }

    @Around("checkId()")
    public String check(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();  //这个RequestContextHolder是Springmvc提供来获得请求的东西
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String id = request.getParameter("id");
        if (Integer.valueOf(id) > 100) {
            return String.format("Id: %s should be less than 100", id);
        }

        Object proceed = joinPoint.proceed();
        return (String) proceed;
    }
}

```

 注意，由于是动态代理的实现方法，所以不是所有的方法都能拦截得下来，对于JDK代理只有public的方法才能拦截得下来，对于CGLIB只有public和protected的方法才能拦截。

这里我们主要介绍execution的匹配方法，因为大多数时候都会用这个来定义pointcut：

```
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)

execution(方法修饰符(可选)  返回类型  类路径 方法名  参数  异常模式(可选)) 
```

 除了返回类型，方法名还有参数之外，其他都是可选的

 ret-type-pattern：可以为*表示任何返回值,全路径的类名等.

name-pattern：指定方法名,\*代表所以,set\*,代表以set开头的所有方法.

parameters pattern:指定方法参数(声明的类型),     ()匹配没有参数； (..)代表任意多个参数；  (\*)代表一个参数，但可以是任意型；  (\*,String)代表第一个参数为任何值,第二个为String类型。

下面给几个例子：

```
1）execution(public * *(..))——表示匹配所有public方法
2）execution(* set*(..))——表示所有以“set”开头的方法
3）execution(* com.xyz.service.AccountService.*(..))——表示匹配所有AccountService接口的方法
4）execution(* com.xyz.service.*.*(..))——表示匹配service包下所有的方法
5）execution(* com.xyz.service..*.*(..))——表示匹配service包和它的子包下的方法
```



另一种通过Validated的方式进行校验：

加入依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

```java
// Student.java
package com.example.restproject.data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.stereotype.Component;

@Component
public class Student {
    @Min(value = 1, message = "id必须为正整数")
    private int id;
    private String name;
    @Max(value = 60, message = "age不能大于60")
    @Min(value = 18, message = "age不能小于18")
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", age=" + age +
            '}';
    }
}

// StudentController.java
package com.example.restproject.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restproject.data.Student;

@RestController
@RequestMapping("/student")
public class StudentController {
    @PostMapping(value = "/add")
    public String add(@RequestBody String jsonStr) {
        return String.format("Add jsonStr: %s", jsonStr);
    }

    @PostMapping(value = "/addStudent")
    public String addStudent(@Validated @RequestBody Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> fieldErrors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
            return String.format("Errors: %s", fieldErrors);
        }

        return String.format("Add student: %s", student);
    }
}

// StudentCheckAspect.java 这个类这里没啥用
package com.example.restproject.utils;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StudentCheckAspect {
    @Pointcut("execution(public * com.example.restproject.controller.StudentController.*(..))")
    public void checkAge() {
    }

//    @Around("checkAge()")
//    public String check(ProceedingJoinPoint joinPoint) throws Throwable {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        Object proceed = joinPoint.proceed();
//        return (String) proceed;
//    }
}

// POST调用：http://localhost:8080/student/addStudent
// body：{"id":"0","name":"LiLei","age":"61"}
// 界面回显：
Errors: [id必须为正整数, age不能大于60]
```



### 1.2.3. 统一异常处理 <https://www.imooc.com/video/14339>

* 什么是异常处理？

*为什么要统一异常处理？

如下例子：

```java
//在调用POST http://localhost:8080/girls?cupSize=B&age=17

@AfterReturning(returning ="object", pointcut = "log()")
public void doAfterReturning(Object object) {
    LOGGER.info("response={}", object.toString());//将object修改为object.toString()
}

//由于校验age失败，return null，又在doAfterReturning函数中调用了.toString方法，Console将显示：
java.lang.NullPointerException: null

//Web界面显示：
{
"timestamp": "2019-03-16T14:58:04.919+0000",
"status": 500,
"error": "Internal Server Error",
"message": "No message available",
"path": "/girls"
}
```

为了统一显示返回数据，增加如下Result类，并将数据封闭在Result对象中返回显示：

```java
//Result.java
package com.imooc.domain;

/**
 * http请求返回的最外层对象
 */
public class Result<T> {
    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体的内容.
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

//ResultUtil.java
package com.imooc.utils;

import com.imooc.domain.Result;

public class ResultUtil {
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}

//GirlController.java
@PostMapping(value = "/girls")
public Result<Girl> addGirl(@Valid Girl girl, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
    }

    return ResultUtil.success(girlRepository.save(girl));
}

//调用POST http://localhost:8080/girls?cupSize=A&age=19
{
"code": 0,
"msg": "成功",
"data":{
"id": 18,
"cupSize": "A",
"age": 19
}
}
//调用POST http://localhost:8080/girls?cupSize=A&age=17
{
"code": 1,
"msg": "未成年少女禁止入内",
"data": null
}
```

有时候如果对每一种场景都做判断根据返回值做分支处理，会比较麻烦，所以统一使用异常处理。

```java

//GirlController.java 中增加：
@GetMapping(value = "/girls/getAge/{id}")
public void getAge(@PathVariable("id") Integer id) throws Exception {
    girlService.getAge(id);
}

//调用 GET http://localhost:8080/girls/getAge/2，Web显示
{
"timestamp": "2019-03-16T15:54:31.779+0000",
"status": 500,
"error": "Internal Server Error",
"message": "你还在上小学吧",
"path": "/girls/getAge/2"
}
```

如上显示方式，可能不是我们想要的统一显示方式，所以增加如下异常处理类：

```java
//GirlExceptionHandler.java
package com.imooc.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.imooc.domain.Result;
import com.imooc.utils.ResultUtil;

@RestControllerAdvice //= @ControllerAdvice + @ResponseBody
public class GirlExceptionHandler {
    @ExceptionHandler(value = Exception.class)
//    @ResponseBody //由于要返回给Json格式给浏览器，类前如果又没有@RestController注解，所以这里要增加@ResponseBody
    public Result handle(Exception e) {
        return ResultUtil.error(100, e.getMessage());
    }
}

//调用 GET http://localhost:8080/girls/getAge/2
{
"code": 100,
"msg": "你还在上小学吧",
"data": null
}
```

想对不同的错误类型进行不同的Code定义，增加自定义Exception类：

```java
//GirlException.java
package com.imooc.exception;

//SpringBoot只会对RuntimeException进行事务回滚，不会对Exception进行回滚，这里要定义为RuntimeException
public class GirlException extends RuntimeException {
    private Integer code;

    public GirlException(int code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

//GirlService.java
package com.imooc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.domain.Girl;
import com.imooc.exception.GirlException;
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

    public void getAge(Integer id) throws Exception {
        Optional<Girl> girl = girlRepository.findById(id);
        Integer age = girl.map(Girl::getAge).orElse(null);
        if (age < 10) {
            //返回“你还在上小学吧” code=100
            throw new GirlException(100, "你还在上小学吧");
        } else if (age >= 10 && age < 16) {
            //返回“你可能在上初中” code=101
            throw new GirlException(101, "你可能在上初中");
        }
    }
}

//GirlExceptionHandler.java
package com.imooc.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.imooc.domain.Result;
import com.imooc.exception.GirlException;
import com.imooc.utils.ResultUtil;

@RestControllerAdvice //= @ControllerAdvice + @ResponseBody
public class GirlExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GirlExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
//    @ResponseBody //由于要返回给Json格式给浏览器，类前又没有@RestController注解，所以这里要增加@ResponseBody
    public Result handle(Exception e) {
        if (e instanceof GirlException) {
            GirlException girlException = (GirlException) e;
            return ResultUtil.error(girlException.getCode(), girlException.getMessage());
        }
        LOGGER.info("【系统异常】：", e);
        return ResultUtil.error(-1, "未知错误");
    }
}


// 调用 GET http://localhost:8080/girls/getAge/2
{
"code": 100,
"msg": "你还在上小学吧",
"data": null
}
// 调用 GET http://localhost:8080/girls/getAge/10
{
"code": 101,
"msg": "你可能在上初中",
"data": null
}

//如果发生非GirException异常，则显示：
{
"code": -1,
"msg": "未知错误",
"data": null
}
```

建议对给Web界面显示的都是自定义异常内容，未知异常则用日志打印在后台

如果每次都写着魔鬼数字100，101，可能会写重，也不方便管理，定义一个ResultEnum类：

```java
//ResultEnum.java
package com.imooc.enums;

public enum ResultEnum {
    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    PRIMARY_SCHOOL(100, "你可能还在上小学"),
    MIDDLE_SCHOOL(101, "你可能在上初中");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

//GirlException.java
package com.imooc.exception;

import com.imooc.enums.ResultEnum;

//SpringBoot只会对RuntimeException进行事务回滚，不会对Exception进行回滚，这里要定义为RuntimeException
public class GirlException extends RuntimeException {
    private Integer code;

    public GirlException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

//GirlService.java
package com.imooc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.domain.Girl;
import com.imooc.exception.GirlException;
import com.imooc.repository.GirlRepository;

import static com.imooc.enums.ResultEnum.MIDDLE_SCHOOL;
import static com.imooc.enums.ResultEnum.PRIMARY_SCHOOL;

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

    public void getAge(Integer id) throws Exception {
        Optional<Girl> girl = girlRepository.findById(id);
        Integer age = girl.map(Girl::getAge).orElse(null);
        if (age < 10) {
            //返回“你还在上小学吧” code=100
            throw new GirlException(PRIMARY_SCHOOL);
        } else if (age >= 10 && age < 16) {
            //返回“你可能在上初中” code=101
            throw new GirlException(MIDDLE_SCHOOL);
        }
    }
}

```

### 1.2.4. 单元测试

* 测试Service

使用

@RunWith(SpringRunner.class)
@SpringBootTest

```java
//GirlServiceTest.java
package com.imooc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.domain.Girl;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GirlServiceTest {
    @Autowired
    private GirlService girlService;

    @Test
    public void testFindOne() {
        Girl girl = girlService.findOne(10);
        assertEquals(new Integer(13), girl.getAge());
    }
}
```

* 测试API

使用

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

```java
//GirlControllerTest.java
package com.imooc.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GirlControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getGirlListStatusIsOk() throws Exception {
        //MockMvcResultMatchers.content().json(returnContent) 如果比较失败，则显示失败的节点值
        //MockMvcResultMatchers.content().string(returnContent) 如果比较失败，则显示全部的字符串值
        String returnContent = "[{\"id\":1,\"cupSize\":\"1\",\"age\":1},{\"id\":2,\"cupSize\":\"2\",\"age\":2}," +
            "{\"id\":10,\"cupSize\":\"F\",\"age\":13},{\"id\":11,\"cupSize\":\"C\",\"age\":20}]";
        mvc.perform(MockMvcRequestBuilders.get("/girls"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(returnContent));
    }

    @Test
    public void getGirlListStatusIsNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/girlsabc"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
```

在进行maven项目打包时，会自动执行全部测试用例：

1. 在项目根目录执行: mvn clean package
2. Idea -> Maven -> LifeCycle -> package

如果不想执行测试用例，则如下操作：(耗时会少很多)

1. 在项目根目录执行：mvn clean package -Dmaven.test.skip=true
2. Idea -> Maven -> Toggle 'Skip Tests' Mode -> LifeCycle -> package

### 1.2.5. 课程总结 <https://www.imooc.com/video/14337>

web进阶:

1. 使用@Valid表单验证
2. 使用AOP处理请求
3. 统一异常处理
4. 单元测试