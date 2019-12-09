package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.system.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {
    @Autowired
    FileService fileService;

    @Test
    public void testGetFile(){
        System.out.println(fileService.findByFileUrl("/app/file/upload/4ed0e084-f8fc-4daf-9136-57648b1dd936.gif").getOriginalFileName());
    }


}
