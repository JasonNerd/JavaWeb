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
        emp.setUsername("Taylor");
        emp.setName("泰勒尔");
        emp.setGender((short) 2);
        emp.setImage("taylor.jpg");
        emp.setJob((short) 2);
        emp.setEntrydate(LocalDate.of(2009, 7, 22));
        emp.setDeptId(2);
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);
    }
}
