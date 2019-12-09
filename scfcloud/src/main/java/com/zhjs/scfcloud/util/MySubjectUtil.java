package com.zhjs.scfcloud.util;

import com.zhjs.scfcloud.config.MyRedisSessionDAO;
import com.zhjs.scfcloud.config.MySimplePrincipalCollection;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author: dailongting
 * @date:2019/5/23 11:14
 */
@Component
public class MySubjectUtil {

    private static MyRedisSessionDAO myRedisSessionDAO;

    @Autowired
    public void setMyRedisSessionDAO(MyRedisSessionDAO myRedisSessionDAO){
        MySubjectUtil.myRedisSessionDAO = myRedisSessionDAO;
    }

    private static MySimplePrincipalCollection getPrincipal(){
        return (MySimplePrincipalCollection)SecurityUtils.getSubject().getSession().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    }

    public static UserInfoVO getUser(){
        return getPrincipal().getUserInfoVO();
    }

    public static Long getUserId(){
        return getUser().getId();
    }

    public static void setUserInfoVO(UserInfoVO user){
        Session session = myRedisSessionDAO.doReadSession(SecurityUtils.getSubject().getSession().getId());
        MySimplePrincipalCollection principal = getPrincipal();
        principal.setUserInfoVO(user);
        session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY,principal);
        myRedisSessionDAO.update(session);
    }

}
