package com.zhjs.scfcloud.model.dto.business;

import lombok.Data;

@Data
public class BusinessFlowPermitInitDTO {
    //用户组id
    private String roleIds;

    //用户组名称
    private String roleNames;

    //当前流程码
    private String flowCode;

    //当前流程名称
    private String flowName;

    //流转后流程码
    private String afterFlowCode;

    //流转后流程名称
    private String afterFlowName;

    //流转id
    private String workFlowId;
}
