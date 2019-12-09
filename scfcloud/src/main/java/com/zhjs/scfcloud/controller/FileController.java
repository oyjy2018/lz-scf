package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.FileFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-04 10:48
 *
 * @author liuchanghai
 * @create 2019-06-04 10:48
 * @since
 */

@RestController
@RequestMapping("/file")
public class FileController {

    private Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileFeignService fileFeignService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public String uploadFile(@RequestParam MultipartFile file){
        return fileFeignService.uploadFile(file);
    }

    @ApiOperation("文件下载")
    @PostMapping("/downloadFile")
    public ResponseEntity<Object> downloadFile(@RequestParam String fileUrl, @RequestParam String suffix, HttpServletResponse response){
        return fileFeignService.downloadFile(fileUrl, suffix, response);
    }

    @ApiOperation("文件删除")
    @PostMapping("/deleteById")
    public Result deleteById(@RequestBody BaseIdDTO dto){
        return fileFeignService.deleteById(dto);
    }
}
