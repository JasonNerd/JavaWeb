package com.rainbow.mapper;

import com.rainbow.pojo.Emp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpMapper {
    // #{id}: 预编译sql, 防止SQL注入
    @Delete("delete from tb_emp where id=#{id}")
    void delete(Integer id);    // 点击删除按钮时依据主键id删除一条记录

    // 新增一个员工
    void insert(Emp emp);
}
