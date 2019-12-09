package com.zhjs.scfcloud.app.util;

import com.zhjs.scfcloud.app.config.MySimplePrincipalCollection;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.RoleVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/5/23 11:14
 */
public class MySubjectUtil {

    private static MySimplePrincipalCollection getPrincipal(){
        return (MySimplePrincipalCollection)SecurityUtils.getSubject().getSession().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    }

    public static User getUser(){
        return getPrincipal().getUser();
    }

    public static Long getUserId(){
        return getUser().getId();
    }

    public static Long getCompanyId(){
        return getPrincipal().getCompanyId();
    }

    public static String getCompanyName(){
        return getPrincipal().getCompanyName();
    }

    /**
     * 获取登录人在当前登录公司下的用户组集合
     * @return
     */
    public static List<Role> getRoleList() {
        return getPrincipal().getRoleList();
    }
}
