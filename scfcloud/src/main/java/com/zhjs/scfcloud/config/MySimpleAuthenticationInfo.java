package com.zhjs.scfcloud.config;

import com.zhjs.scfcloud.model.vo.UserInfoVO;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

/**
 * @author: dailongting
 * @date:2019/5/23 10:48
 */
public class MySimpleAuthenticationInfo extends SimpleAuthenticationInfo {

    private MySimplePrincipalCollection myPrincipals;

    public MySimpleAuthenticationInfo(){

    }

    public MySimpleAuthenticationInfo(Object principal, Object credentials, String realmName){
        this.myPrincipals = new MySimplePrincipalCollection(principal, realmName);
        this.principals = this.myPrincipals;
        this.credentials = credentials;
    }

    public void setCompanyId(Long companyId) {
        this.myPrincipals.setCompanyId(companyId);
    }

    public void setUserId(Long userId) {
        this.myPrincipals.setUserId(userId);
    }

    public void setUserInfoVO(UserInfoVO userInfoVO) {
        this.myPrincipals.setUserInfoVO(userInfoVO);
    }

}
