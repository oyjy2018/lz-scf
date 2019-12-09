package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.feign.CommentFeignService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommentDTO;
import com.zhjs.scfcloud.model.entity.Comment;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-22 14:13
 *
 * @author liuchanghai
 * @create 2019-06-22 14:13
 * @since
 */

@Api(tags = "评论管理")
@RestController
@RequestMapping("/comment/")
public class CommentController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommentFeignService commentFeignService;

    @ApiOperation("查询评论列表")
    @PostMapping("findList")
    public String findList(@RequestBody CommentDTO dto) {
        logger.info("查询评论列表");
        return commentFeignService.findList(dto).toJSON();
    }

    @ApiOperation("新建评论")
    @PostMapping("add")
    public String add(@RequestBody Comment dto){
        logger.info("新增评论：{}", JsonUtil.toJSON(dto));
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setCreateBy(user.getId());
        dto.setCreateTime(new Date());
        return commentFeignService.add(dto).toJSON();
    }
}
