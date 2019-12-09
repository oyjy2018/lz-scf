package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.FileUploadLog;

import java.util.List;

/**
 * 文件上传日记信息表 Mapper 接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-13 13:44
 *
 * @author liuchanghai
 * @create 2019-06-13 13:44
 * @since
 */

public interface FileUploadLogMapper extends BaseMapper<FileUploadLog> {

    List<FileUploadLog> findRubbishFileList();
}
