package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.CommentDTO;
import com.zhjs.scfcloud.model.entity.Comment;
import com.zhjs.scfcloud.model.vo.CommentVO;
import com.zhjs.scfcloud.system.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 评论管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-21 16:04
 *
 * @author liuchanghai
 * @create 2019-06-21 16:04
 * @since
 */

@Api(tags = "评论管理")
@RestController
@RequestMapping("/comment/")
public class CommentController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommentService commentService;

    @ApiOperation("查询评论列表")
    @PostMapping("findList")
    public Result findList(@RequestBody CommentDTO dto) {
        logger.info("查询评论列表");
        List<CommentVO> list = commentService.findList(dto.getCompanyId(), dto.getBusinessId(),
                dto.getBusinessTypeId(), dto.getFlowCode());
        return Result.success(list);
    }

    @ApiOperation("新建评论")
    @PostMapping("add")
    public Result add(@RequestBody Comment dto) {
        logger.info("新建评论");
        if(commentService.save(dto)){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除评论")
    @PostMapping("delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        logger.info("删除评论");
        if(commentService.removeById(dto.getId())){
            return Result.success();
        }
        return Result.failure();
    }

}
