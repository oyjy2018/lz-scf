package com.zhjs.scfcloud.model.vo.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhjs.scfcloud.model.entity.BusinessFileCfg;
import com.zhjs.scfcloud.model.entity.BusinessWorkFlowAttr;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.RoleVO;
import com.zhjs.scfcloud.model.vo.UserVO;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

/**
 * 查询工作流以及流转条件和流转字段
 * @author:oyjy
 * @date:2019-06-13 11:19
 */
@Data
public class BusinessWorkFlowExtendVO {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 扭转前流程CODE
     * before_flow
     */
    private String beforeFlow;

    private String beforeFlowName;

    /**
     * 扭转后流程CODE
     * after_flow
     */
    private String afterFlow;

    private String afterFlowName;

    /**
     * 是否可编辑
     * has_edit
     */
    private Integer hasEdit;


    /**
     * 用户组ID集合
     * role_ids
     */
    private String roleIds;

    private List<RoleVO> roleList;

    /**
     * 用户ID集合
     * user_ids
     */
    private String userIds;

    private List<UserVO> userList;

    /**
     * 下一流程处理人
     */
    private String nextDisposeRoleIds;

    private List<RoleVO> nextDisposeRoleList;

    /**
     * 下一流程处理角色
     */
    private String nextDisposeUserIds;

    private List<UserVO> nextDisposeUserList;

    //业务流程属性集合
    List<BusinessAttrCfgVO> businessAttrCfgVOList;

    /**
     * 业务流程附件配置集合
     */
    private List<BusinessWorkFlowFileVO> workFlowFileVOList;
}