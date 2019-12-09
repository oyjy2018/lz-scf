package com.zhjs.scfcloud.util;

import com.zhjs.scfcloud.config.MyRedisSessionDAO;
import com.zhjs.scfcloud.config.MySimplePrincipalCollection;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author: dailongting
 * @date:2019/8/23 14:25
 */
@Component
public class ShiroSessionUtil {

    private static MyRedisSessionDAO myRedisSessionDAO;

    @Autowired
    public void setMyRedisSessionDAO(MyRedisSessionDAO myRedisSessionDAO){
        ShiroSessionUtil.myRedisSessionDAO = myRedisSessionDAO;
    }

    /**
     * 根据userID获取登录用户
     * @param userId
     * @return
     */
    public static UserInfoVO getLoginUserByUserId(Long userId){
        if(userId == null) {
            return null;
        }

        Collection<Session> sessions = myRedisSessionDAO.getActiveSessions();
        UserInfoVO user = null;
        MySimplePrincipalCollection principal = null;
        for(Session session : sessions){
            principal = (MySimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            user = principal.getUserInfoVO();
            if(user.getId().longValue() == userId.longValue()){
                return user;
            }
        }
        return null;
    }

    /**
     * 根据userId更新登录用户身份证
     * @param userId
     * @param idCard
     */
    public static void updateLoginUserIdCardByUserId(Long userId, String idCard){
        System.out.println("--------需要更新身份证的用户ID："+userId);
        if(userId == null){
            return;
        }

        Collection<Session> sessions = myRedisSessionDAO.getActiveSessions();
        UserInfoVO user = null;
        MySimplePrincipalCollection principal = null;
        for(Session session : sessions){
            principal = (MySimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            user = principal.getUserInfoVO();
            System.out.println("------------判断是否是需要被更新的登录用户：userId:"+user.getId()+";username:"+user.getUserName()+";idCard:"+user.getIdCard());
            if(user.getId().longValue() == userId.longValue()){
                System.out.println("------------将要被更新的登录用户：username:"+user.getUserName()+";idCard:"+user.getIdCard());
                user.setIdCard(idCard);
                session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY,principal);
                myRedisSessionDAO.update(session);
                System.out.println("------------更新登录用户身份证成功：新idCard:"+user.getIdCard());
            }
        }

    }

    public static void testAAA(){
        Collection<Session> sessions = myRedisSessionDAO.getActiveSessions();
        UserInfoVO user = null;
        MySimplePrincipalCollection principal = null;
        for(Session session : sessions){
            principal = (MySimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            user = principal.getUserInfoVO();
            if(208 == user.getId().intValue()){
                user.setIdCard("修改BUG专用idCard");
                session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY,principal);
                myRedisSessionDAO.update(session);
            }
        }
    }
}
