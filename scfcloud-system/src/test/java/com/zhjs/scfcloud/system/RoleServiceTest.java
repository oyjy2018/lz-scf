package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AddRoleDTO;
import com.zhjs.scfcloud.model.dto.EditRoleDTO;
import com.zhjs.scfcloud.model.vo.CompanyRoleListVO;
import com.zhjs.scfcloud.system.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTest {
    @Autowired
    RoleService roleService;

    @Test
    public void findCompanyRoleListByUserId(){
        List<CompanyRoleListVO> list =  roleService.findCompanyRoleListByUserId(52L, false);
        System.out.println(list);
    }

    @Test
    public void addCompanyRole(){
        AddRoleDTO dto = new AddRoleDTO();
        dto.setRoleName("测试部门");
        Result result =  roleService.addCompanyRole(61L,dto);
        System.out.println(result);
    }

    @Test
    public void edit(){
        EditRoleDTO dto = new EditRoleDTO();
        dto.setRoleName("商务经理组");
        dto.setId(188L);
        Result result =  roleService.edit(dto);
        System.out.println(result);
    }

    @Test
    public void getRoleFilePermissionsByUserId(){
        System.out.println(roleService.getRoleFilePermissionsByUserId(1L));
    }
}
