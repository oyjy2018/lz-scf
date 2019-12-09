package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.entity.File;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/20 16:53
 */
@FeignClient(value = "scfcloud-system", contextId = "file")
public interface FileFeignService {
    @ApiOperation("根据文件CODE集合查询")
    @PostMapping("/file/findByFileCodes")
    Result findByFileCodes(@RequestParam String fileCodes);

    @ApiOperation("添加文件")
    @PostMapping("/file/addFile")
    Result addFile(@RequestBody FileDTO dto);

    @ApiOperation("根据条件选择查询文件")
    @PostMapping("/file/findSelective")
    Result<List<File>> findSelective(@RequestBody FileDTO dto);

}
