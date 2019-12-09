package com.zhjs.scfcloud.config;

import com.zhjs.scfcloud.model.vo.UserInfoVO;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * @author: dailongting
 * @date:2019/5/23 10:40
 */
public class MySimplePrincipalCollection extends SimplePrincipalCollection {

    private static final long serialVersionUID = 9898989898L;

    private Long companyId;

    private Long userId;

    private UserInfoVO userInfoVO;

    public MySimplePrincipalCollection(){

    }

    public MySimplePrincipalCollection(Object principal, String realmName){
        super(principal, realmName);
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserInfoVO getUserInfoVO() {
        return userInfoVO;
    }

    public void setUserInfoVO(UserInfoVO userInfoVO) {
        this.userInfoVO = userInfoVO;
    }
}
