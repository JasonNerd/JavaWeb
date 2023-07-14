package com.rainbow;

import com.rainbow.mapper.EmpMapper;
import com.rainbow.pojo.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class EmpCurdApplicationTests {
    @Autowired
    private EmpMapper empMapper;
    @Test
    void testDelete() {
        empMapper.delete(1);
    }

    @Test
    void testInsert(){
        Emp emp = new Emp();
        emp.setUsername("Julie5");
        emp.setName("朱莉儿5");
        emp.setGender((short) 2);
        emp.setImage("july.jpg");
        emp.setJob((short) 3);
        emp.setEntrydate(LocalDate.of(2012, 2, 11));
        emp.setDeptId(2);
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        System.out.println(emp.getId());    // 主键为空
        empMapper.insert(emp);
        System.out.println(emp.getId());    // 主键回显
    }

    @Test
    public void testUpdate(){
        Emp emp = new Emp();
        emp.setId(21);      // 更新 21 号数据
        emp.setUsername("Amel21");
        emp.setName("阿迈尔21");
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
    }

    @Test
    public void testSelect(){
        // 1. 通过 id 查找
//        System.out.println(empMapper.getByID(10));
        // 可以看到, 结果查询到了, 并且 deptId, createTime 等字段也不为空
        // 这就说明 properties 文件的驼峰开启有效(仅在Select注解里)

        // 2. 条件查询
        System.out.println(empMapper.select(null, (short) 1,
                LocalDate.of(2009, 10, 3),
                LocalDate.of(2019, 5, 20))
        );
    }
}
