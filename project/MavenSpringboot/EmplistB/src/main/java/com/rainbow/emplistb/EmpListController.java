package com.rainbow.emplistb;

import com.rainbow.emplistb.pojo.Emp;
import com.rainbow.emplistb.pojo.Result;
import com.rainbow.emplistb.service.EmpService;
import com.rainbow.emplistb.service.impl.EmpServiceA;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class EmpListController {
    @Resource(name = "empServiceA")     // 依赖注入
    private EmpService empService;
    public static void main(String[] args) {
        SpringApplication.run(EmpListController.class, args);
    }
    @RequestMapping("/listEmp")
    public Result listEmp() {
        // 调用服务, 获取响应数据
        List<Emp> empList = empService.listEmp();
        return Result.success(empList);
    }
}
