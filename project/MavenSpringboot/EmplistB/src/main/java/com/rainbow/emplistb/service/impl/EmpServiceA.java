package com.rainbow.emplistb.service.impl;

import com.rainbow.emplistb.dao.EmpDao;
import com.rainbow.emplistb.dao.impl.EmpDaoA;
import com.rainbow.emplistb.pojo.Emp;
import com.rainbow.emplistb.service.EmpService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmpServiceA implements EmpService {
//    private EmpDao empDao = new EmpDaoA();      // 这里直接实例化了一个具体的 EmpDaoA 对象
    @Resource(name = "empDaoA")     // 依赖注入(指定了哪一类依赖, 防止多个同类型的依赖造成冲突)
    private EmpDao empDao;
    @Override
    public List<Emp> listEmp() {
        List<Emp> empList = empDao.listEmp();
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
        return empList;
    }
}
