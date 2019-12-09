package com.zhjs.scfcloud.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.system.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MD5Test {
    @Autowired
    UserService userService;

    @Test
    public void md5() {
        String str = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(str.length());
        String str2 = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(str2);
        System.out.println(str.equals(str2));
    }

    @Test
    public void udpatePassword(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        List<User> users = userService.list(userQueryWrapper);
        users.forEach(user -> {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userService.updateById(user);
        });
    }
}
