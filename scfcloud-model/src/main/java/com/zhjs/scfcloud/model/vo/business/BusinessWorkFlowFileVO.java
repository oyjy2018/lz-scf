package com.zhjs.scfcloud.model.vo.business;

import com.zhjs.scfcloud.model.dto.business.AddWorkFlowFileDTO;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/21 15:43
 */
@Data
public class BusinessWorkFlowFileVO {
    /**
     * 流程扭转附件配置ID
     */
    private Long workFlowFileId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 业务类型ID
     */
    private Long businessTypeId;

    /**
     * 业务文件配置ID
     */
    private Long businessFileId;

    /**
     * 流程扭转ID
     */
    private Long workFlowId;

    /**
     * 文件CODE
     */
    private String fileCode;

    /**
     * 文件中文名称
     */
    private String fileName;

    /**
     * 单次上传文件大小
     */
    private Integer onceUploadSize;

    /**
     * 上传文件数量限制
     */
    private Integer uploadLimit;

    /**
     * 有权限查看的用户组
     */
    private String roleIds;

    private List<Role> roleList;

    /**
     * 有权限查看的用户
     */
    private String userIds;

    private List<User> userList;

    /**
     * 是否必填
     */
    private Integer required;

    /**
     * 排序
     */
    private Integer sort;

    private Integer status;

    /**
     * 比对配置是否一致
     * @param add
     * @return
     */
    public boolean settingEqual(AddWorkFlowFileDTO add){
        if(add == null){
            return false;
        }

        if(this.businessFileId == null || add.getBusinessFileId() == null){
            return false;
        }

        if(this.businessFileId.longValue() != add.getBusinessFileId().longValue()){
            return false;
        }

        if(this.required == null || add.getRequired() == null){
            return false;
        }

        if(this.required.intValue() != add.getRequired().intValue()){
            return false;
        }

        return true;
    }
}
