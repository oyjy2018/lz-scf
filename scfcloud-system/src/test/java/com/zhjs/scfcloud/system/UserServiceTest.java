package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.dto.UserAcceptInviteAndRegisterDTO;
import com.zhjs.scfcloud.system.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void confirmUser() {
        UserAcceptInviteAndRegisterDTO dto = new UserAcceptInviteAndRegisterDTO();
        dto.setCode("367861");
        dto.setEmail("asjd@fljdf.codd");
        dto.setPhone("17676163221");
        dto.setCompanyId(56L);
        dto.setPassword("123456");
        System.out.println(userService.acceptInviteAndRegister(dto));
    }
}
