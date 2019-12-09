package com.zhjs.scfcloud.app.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.QueryBusinessAttrDTO;
import com.zhjs.scfcloud.model.dto.business.QueryWorkFlowCfgDTO;

/**
 * 业务相关配置逻辑处理类
 * @author: dailongting
 * @date:2019/6/13 10:39
 */
public interface BusinessCfgService {

    /**
     * 获取业务表单配置
     * @param dto
     * @return
     */
    Result getBusinessFormCfg(QueryBusinessAttrDTO dto);

    /**
     * 获取后续流程配置
     * @param dto
     * @return
     */
    Result getTrailingWorkFlowCfg(QueryWorkFlowCfgDTO dto);

    String getBusinessTypeList(Long companyId);
}
