package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.system.service.DepartmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentServiceTest {

    @Autowired
    DepartmentService departmentService;

    @Test
    public void findDeptUserTree() {
        Result result = departmentService.findDeptUserTree(1L);
        System.out.println(result);
    }
}
