package com.rainbow.mavenspringboot.paramPasser;
import com.rainbow.mavenspringboot.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class RequestController {
    public static void main(String[] args) {
        SpringApplication.run(RequestController.class, args);
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
    // 1. 原始方式, get, 通过 HttpServletRequest 实现
    @RequestMapping("/greet")
    public String greet(HttpServletRequest request){
        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        int age = Integer.parseInt(ageStr);
        // do something with the args
        System.out.println(name+": "+age);
        return "Hello from greet to " + name+", your age is "+ageStr;
    }

    // 2. 基于 Springboot 方式, get, 无需数据转换
    @RequestMapping("/greet2")
    public String greet2(String name, Integer age){
        System.out.println(name+": "+age);
        return "Hello from greet2 to " + name+", your age is "+age;
    }

    // 3. 将众多参数封装到实体类中 -- 简单实体
    @RequestMapping("/simplePojo")
    public String greet3(User user){
        System.out.println(user);   // user 包含 name 和 age 属性
        return "Hello from simplePojo to " + user.getName()+", your age is "+user.getAge();
    }

    // 4. 复杂实体, 例如一个类中的字段是另一个类的实体
    @RequestMapping("/complexPojo")
    public String complexPojo(User user){
        System.out.println(user);   // user 包含 name 和 age 属性
        return user.toString();
    }

    // 5. 数组参数
    @RequestMapping("/arrayParam")
    public String arrayParam(String[] hobby){
        return Arrays.toString(hobby);
    }

    // 6. 集合参数
    @RequestMapping("/listParam")
    public String arrayParam(@RequestParam List<String> hobby){
        return hobby.toString();
    }

    // 7. 日期时间, 注意格式化
    @RequestMapping("/datetime")
    public String dateParam(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime now){
        return now.toString();
    }

    // 8. json 字符串, 使用实体对象接受
    @RequestMapping("/jsonParam")
    public String jsonParam(@RequestBody User user){
        return user.toString();
    }

    // 9. 路径参数, 动态路径绑定
    @RequestMapping("/pathParam/{id}/{name}")
    public String jsonParam(@PathVariable Integer id, @PathVariable String name){
        return id.toString()+", "+name;
    }
}