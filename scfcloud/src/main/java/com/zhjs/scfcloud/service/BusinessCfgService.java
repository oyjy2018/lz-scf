package com.zhjs.scfcloud.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.*;

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
     * 查询流程扭转附件配置
     * @return
     */
    Result findAllBusinessFileCfg();

    /**
     * 查询指定流程扭转附件配置
     * @param dto
     * @return
     */
    Result findFileCfgByWorkFlowIds(QueryBusinessFileCfgDTO dto);

    //获取业务的流程状态集合
    String getFlowCodeList(Long businessTypeId);

    //获取的业务类型集合
    String getBusinessTypeList(Long companyId);

    //获取业务类型集合（转为专业术语）
    Result getStandardBusinessTypeList(Long companyId);

    /**
     * 查询正向流程列表
     * @param dto
     * @return
     */
    String getBusinessFlowForward(QueryBusinessCfgDTO dto);

    /**
     * 查询业务流程扭转配置
     * @param dto
     * @return
     */
    Result getBusinessFlowExtendAll(BusinessTypeBaseDTO dto);

    /**
     * 编辑业务流程扭转配置
     * @param dto
     * @return
     */
    Result editWorkFlow(EditWorkFlowDTO dto);

    /**
     * 校验流程扭转配置是否存在
     * @param dto
     * @return
     */
    Result workFlowExists(WorkFlowExistsDTO dto);

    /**
     * 查询流程扭转配置信息
     * @param dto
     * @return
     */
    Result findFlowExtendSettingInfo(WorkFlowExistsDTO dto);

    /**
     * 编辑业务流程扭转权限、字段与附件配置
     * @param dto
     * @return
     */
    Result savePower(SavePowerDTO dto);
}
