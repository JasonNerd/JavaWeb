package com.rainbow.emplistb.dao;

import com.rainbow.emplistb.pojo.Emp;

import java.util.List;

// 创建一个数据访问接口
public interface EmpDao {
    // 获取员工列表数据
    List<Emp> listEmp();
}
