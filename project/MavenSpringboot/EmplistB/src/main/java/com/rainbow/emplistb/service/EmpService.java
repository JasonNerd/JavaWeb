package com.rainbow.emplistb.service;

import com.rainbow.emplistb.pojo.Emp;

import java.util.List;

public interface EmpService {
    // 将数据访问层的数据进行处理, 得到响应数据
    List<Emp> listEmp();
}
