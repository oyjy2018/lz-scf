package com.zhjs.scfcloud.ticket.util;

import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.config.MySimplePrincipalCollection;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.support.DefaultSubjectContext;

/**
 * @author: dailongting
 * @date:2019/5/23 11:14
 */
public class MySubjectUtil {

    private static MySimplePrincipalCollection getPrincipal(){
        return (MySimplePrincipalCollection)SecurityUtils.getSubject().getSession().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    }

    public static UserInfoVO getUser(){
        return getPrincipal().getUserInfoVO();
    }

    public static Long getUserId(){
        return getUser().getId();
    }
}
