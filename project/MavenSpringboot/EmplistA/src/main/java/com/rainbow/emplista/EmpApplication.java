package com.rainbow.emplista;

import com.rainbow.emplista.pojo.Emp;
import com.rainbow.emplista.pojo.Result;
import com.rainbow.emplista.utils.XMLParseUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class EmpApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmpApplication.class, args);
    }

    // 基本信息:
    //      请求路径: /listEmp
    //      返回值:  统一为 Result实体对象
    //      请求参数: 无
    @RequestMapping("/listEmp")
    public Result listEmp(){
        // 1. 获取原始数据
        String file = this.getClass().getClassLoader().getResource("emp.xml").getFile();
        System.out.println(file);
        List<Emp> empList = XMLParseUtils.parse(file, Emp.class);
        // 2. 处理得到响应数据
        empList.forEach(emp -> {
            String gender = emp.getGender();
            emp.setGender(gender.equals("1")? "男": "女");
            String job = emp.getJob();
            if ("1".equals(job)){
                emp.setJob("讲师");
            } else if ("2".equals(job)) {
                emp.setJob("班主任");
            } else if ("3".equals(job)) {
                emp.setJob("就业指导");
            }
        });
        // 3. 返回响应数据
        return Result.success(empList);
    }
}
