package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.entity.File;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-04 10:52
 *
 * @author liuchanghai
 * @create 2019-06-04 10:52
 * @since
 */

@FeignClient(value = "scfcloud-system", contextId = "file")
public interface FileFeignService {

    @ApiOperation("文件上传")
    @PostMapping("/file/upload")
    String uploadFile(@RequestParam MultipartFile file);

    @ApiOperation("文件下载")
    @PostMapping("/file/downloadFile")
    ResponseEntity<Object> downloadFile(@RequestParam String fileUrl, @RequestParam String suffix, HttpServletResponse response);

    /**
     * 删除文件
     * @param dto
     * @return
     */
    @ApiOperation("文件删除")
    @PostMapping("/file/deleteById")
    Result deleteById(@RequestBody BaseIdDTO dto);

    @ApiOperation("根据条件选择查询文件")
    @PostMapping("/file/findSelective")
    Result<List<File>> findSelective(@RequestBody FileDTO dto);
}
