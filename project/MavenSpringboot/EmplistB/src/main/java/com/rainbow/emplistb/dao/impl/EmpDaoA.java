package com.rainbow.emplistb.dao.impl;
// 数据访问的一个实现
import com.rainbow.emplistb.dao.EmpDao;
import com.rainbow.emplistb.pojo.Emp;
import com.rainbow.emplistb.utils.XMLParseUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class EmpDaoA implements EmpDao {
    @Override
    public List<Emp> listEmp() {
        String file = this.getClass().getClassLoader().getResource("emp.xml").getFile();
        System.out.println(file);
        return XMLParseUtils.parse(file, Emp.class);
    }
}
