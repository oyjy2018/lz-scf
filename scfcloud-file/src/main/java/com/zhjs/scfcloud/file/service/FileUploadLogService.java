package com.zhjs.scfcloud.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.entity.FileUploadLog;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-14 16:16
 *
 * @author liuchanghai
 * @create 2019-06-14 16:16
 * @since
 */
public interface FileUploadLogService extends IService<FileUploadLog> {

    FileUploadLog findByFileUrl(String fileUrl);

}
