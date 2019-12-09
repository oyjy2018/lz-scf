package com.zhjs.scfcloud.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.file.service.FileUploadLogService;
import com.zhjs.scfcloud.model.entity.FileUploadLog;
import com.zhjs.scfcloud.model.mapper.FileUploadLogMapper;
import org.springframework.stereotype.Service;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-14 16:17
 *
 * @author liuchanghai
 * @create 2019-06-14 16:17
 * @since
 */

@Service
public class FileUploadLogServiceImpl extends ServiceImpl<FileUploadLogMapper, FileUploadLog> implements FileUploadLogService {

    @Override
    public FileUploadLog findByFileUrl(String fileUrl) {
        LambdaQueryWrapper<FileUploadLog> lambda = new QueryWrapper<FileUploadLog>().lambda();
        lambda.eq(FileUploadLog::getFileUrl,fileUrl);
        return getOne(lambda);
    }
}
