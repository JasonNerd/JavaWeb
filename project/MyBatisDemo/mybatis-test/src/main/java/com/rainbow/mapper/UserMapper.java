package com.rainbow.mapper;

import com.rainbow.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    // 在运行时，会自动生成资接口的实现类对象(代理对象)，并且将该对象交给TOC容器营理
    @Select("select * from user")
    List<User> listAll();
}
