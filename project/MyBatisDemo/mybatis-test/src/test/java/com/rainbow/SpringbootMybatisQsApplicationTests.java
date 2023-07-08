package com.rainbow;

import com.rainbow.mapper.UserMapper;
import com.rainbow.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootMybatisQsApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testListAll(){
        List<User> users = userMapper.listAll();
        users.forEach(System.out::println);
    }
}
