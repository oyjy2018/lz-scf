package com.zhjs.scfcloud.config;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class MySessionManager extends DefaultWebSessionManager {

    public static final String AUTHENTICATIONTOKEN = "AuthenticationToken";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    private Logger logger = LoggerFactory.getLogger(MySessionManager.class);

    public MySessionManager(){
        super();
    }

    @Override
    protected void onStart(Session session, SessionContext context) {
        if (!WebUtils.isHttp(context)) {
            //log.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response pair. No session ID cookie will be set.");
        } else {
            //session管理开始时，会把sessionID放到cookie上，这里重写，让sessionID只在登录时返回给前端，不放在response上
            HttpServletRequest request = WebUtils.getHttpRequest(context);
            //HttpServletResponse response = WebUtils.getHttpResponse(context);
            //Serializable sessionId = session.getId();
            request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
        }
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String id = req.getHeader(AUTHENTICATIONTOKEN);
        logger.info("id:{}",id);
        if(id != null){
            //暂时不知道这些是做什么用的
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        return id;
    }

    @Override
    public void touch(SessionKey key) throws InvalidSessionException {
        Session s = lookupRequiredSession(key);
        s.touch();
        onChange(s);
    }

    private Session lookupSession(SessionKey key) throws SessionException {
        if (key == null) {
            throw new NullPointerException("SessionKey argument cannot be null.");
        }
        return doGetSession(key);
    }

    private Session lookupRequiredSession(SessionKey key) throws SessionException {
        Session session = lookupSession(key);
        if (session == null) {
            String msg = "Unable to locate required Session instance based on SessionKey [" + key + "].";
            throw new UnknownSessionException(msg);
        }
        return session;
    }
}
