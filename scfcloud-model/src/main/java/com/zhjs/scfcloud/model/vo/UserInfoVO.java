package com.zhjs.scfcloud.model.vo;

import com.zhjs.scfcloud.model.entity.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: dailongting
 * @date:2019/5/21 16:37
 */
@Data
public class UserInfoVO extends User implements Serializable {

    private static final long serialVersionUID = 2427389723552147596L;

    /**
     * 当前登录公司ID
     */
    private Long companyId;

    /**
     * 当前登录公司名称
     */
    private String companyName;
    ///当前公司状态
    private Integer companyStatus;
    ///当前公司是否已删除
    private Integer companyIsDelete;
    /**
     * 角色
     */
    private List<Role> roleList;

    /**
     * 功能权限
     */
    private List<SystemFunction> systemFunctionList;

    /**
     * 公司
     */
    private List<Company> companyList;

    /**
     * 可访问菜单
     */
    private List<String> menuList;

    public String getRoleNames(){
        if(roleList == null || roleList.isEmpty()) return null;
        return StringUtils.join(roleList.stream().map(role -> { return role.getRoleName();}).collect(Collectors.toList()),",");
    }

}
