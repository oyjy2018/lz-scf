package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.service.FileService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.file.AddFileDTO;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: dailongting
 * @date:2019/6/25 17:48
 */
@RestController
@RequestMapping("/file")
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;

    @ApiOperation("新增附件信息")
    @PostMapping("/addFile")
    public String addFile(@RequestBody @Valid AddFileDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","新增附件信息", JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return fileService.addFile(dto).toJSON();
    }
}
