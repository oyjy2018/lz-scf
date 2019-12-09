package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.EditWorkFlowDTO;
import com.zhjs.scfcloud.model.dto.business.SavePowerDTO;

/**
 * 业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 15:46
 *
 * @author liuchanghai
 * @create 2019-06-11 15:46
 * @since
 */
public interface BusinessService {

    Result initCompanyFlowPermit(Long companyId);

    Result editWorkFlow(EditWorkFlowDTO dto);

    Result savePower(SavePowerDTO dto);
}
