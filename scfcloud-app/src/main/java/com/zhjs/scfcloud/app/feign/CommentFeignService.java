package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommentDTO;
import com.zhjs.scfcloud.model.entity.Comment;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-22 14:15
 *
 * @author liuchanghai
 * @create 2019-06-22 14:15
 * @since
 */


@FeignClient(value = "scfcloud-system", contextId = "comment")
public interface CommentFeignService {

    @ApiOperation("查询评论列表")
    @PostMapping("/comment/findList")
    Result findList(@RequestBody CommentDTO dto);

    @ApiOperation("新建评论")
    @PostMapping("/comment/add")
    Result add(@RequestBody Comment dto);
}
