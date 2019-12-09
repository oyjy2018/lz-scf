package com.zhjs.scfcloud.app.config;

import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.RoleVO;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

import java.util.List;

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

    public void setUser(User user) {
        this.myPrincipals.setUser(user);
    }

    public void setCompanyId(Long companyId) {
        this.myPrincipals.setCompanyId(companyId);
    }

    public void setCompanyName(String companyName) {
        this.myPrincipals.setCompanyName(companyName);
    }

    public void setRoleList(List<Role> roleList) {
        this.myPrincipals.setRoleList(roleList);
    }

}
