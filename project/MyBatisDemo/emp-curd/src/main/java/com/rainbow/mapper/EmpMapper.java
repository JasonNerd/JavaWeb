package com.rainbow.mapper;

import com.rainbow.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {
    // #{id}: 预编译sql, 防止SQL注入
    @Delete("delete from tb_emp where id=#{id}")
    void delete(Integer id);    // 点击删除按钮时依据主键id删除一条记录

    // 新增一个员工
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into tb_emp(username, password, name, gender, image, job, entrydate,"+
                          " dept_id, create_time, update_time)"+
        "VALUES (#{username}, #{password}, #{name}, #{gender}, #{image}, #{job}, #{entrydate},"+
                "#{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    // 静态的更新数据
    @Update("update tb_emp "+
    "set username=#{username}, password=#{password}, name=#{name},"+
        "gender=#{gender}, image=#{image}, job=#{job}, entrydate=#{entrydate},"+
        "dept_id=#{deptId}, update_time=#{updateTime} "+
    "where id=#{id}")
    void updStatic(Emp emp);
    // 更新员工信息, 若仅更新部分字段值(此时其他字段为NULL), 则使用动态SQL
    void update(Emp emp);

    // 查询 select
    @Select("select id, username, password, name, gender, image, job, " +
            "entrydate, dept_id, create_time, update_time " +
            "from tb_emp where id=#{id}")
    Emp getByID(Integer id);

    List<Emp> select(String name, Short gender, LocalDate begin, LocalDate end);

}
