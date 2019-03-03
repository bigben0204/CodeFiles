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
package com.imooc.data;

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

import com.imooc.data.GirlProperties;

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