package com.zhjs.scfcloud.system.controller;

/**
 * 文件管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-14 15:01
 *
 * @author liuchanghai
 * @create 2019-06-14 15:01
 * @since
 */

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.entity.File;
import com.zhjs.scfcloud.model.entity.OperateLog;
import com.zhjs.scfcloud.model.enums.OperatObjectEnum;
import com.zhjs.scfcloud.model.enums.OperatTypeEnum;
import com.zhjs.scfcloud.model.transfer.FileTransfer;
import com.zhjs.scfcloud.system.service.FileService;
import com.zhjs.scfcloud.system.service.OperateLogService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {

    private Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;
    @Autowired
    private FileTransfer fileTransfer;
    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation("文件批量保存")
    @PostMapping("/addBatch")
    public Result addBatch(@RequestBody FileDTO dto){
        log.info("文件批量保存：{}" + dto.toString());
        List<File> files = new ArrayList<>();
        if (fileService.addBatch(files)){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("根据条件选择查询文件")
    @PostMapping("/findSelective")
    public Result<List<File>> findSelective(@RequestBody FileDTO dto){
        log.info("文件查询：{}" + dto.toString());
        return fileService.findSelective(dto);
    }

    @ApiOperation("文件删除")
    @PostMapping("/deleteById")
    public Result deleteById(@RequestBody BaseIdDTO dto){
        log.info("文件删除：{}" + dto.toString());
        if(fileService.removeById(dto.getId())){
            return Result.success("删除成功");
        }
        log.info("文件删除异常：{}" + dto.toString());
        return Result.failure("删除失败");
    }

    @ApiOperation("根据文件CODE集合查询")
    @PostMapping("/findByFileCodes")
    public Result findByFileCodes(@RequestParam String fileCodes){
        log.info("根据文件CODE集合查询:{}",fileCodes);
        return Result.success(fileService.findByFileCodes(fileCodes));
    }

    @ApiOperation("添加文件")
    @PostMapping("/addFile")
    public Result addFile(@RequestBody FileDTO dto){
        log.info("添加文件:{}", JsonUtil.toJSON(dto));
        File file = fileTransfer.toFile(dto);
        file.setCreateTime(new Date());
        fileService.add(file);
        if(file.getId() == 0){
            return Result.failure("保存文件信息失败");
        }
        return Result.success(file);
    }
}
