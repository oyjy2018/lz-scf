package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.entity.OperateLog;

/**
 * 操作日记业务管理接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-20 11:34
 *
 * @author liuchanghai
 * @create 2019-06-20 11:34
 * @since
 */
public interface OperateLogService extends IService<OperateLog> {

    /**
     * 查询操作日志
     * @param dto
     * @return
     */
    String findList(OperateLogDTO dto);
}
