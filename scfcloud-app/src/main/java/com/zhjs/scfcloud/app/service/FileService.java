package com.zhjs.scfcloud.app.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.file.AddFileDTO;

/**
 * @author: dailongting
 * @date:2019/6/25 17:56
 */
public interface FileService {

    /**
     * 添加附件
     * @param dto
     * @return
     */
    Result addFile(AddFileDTO dto);

}
