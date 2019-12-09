package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.entity.PlatformServiceRate;

/**
 * 平台服务费率
 */
public interface PlatformServiceRateService extends IService<PlatformServiceRate> {

    Result save(BaseSingleUpdateDTO dto);

    Result query();
}
