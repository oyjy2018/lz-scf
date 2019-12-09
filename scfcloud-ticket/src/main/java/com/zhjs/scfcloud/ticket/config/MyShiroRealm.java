package com.zhjs.scfcloud.ticket.config;

import com.zhjs.scfcloud.model.vo.UserInfoVO;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String AUTHORIZATION_CACHE_KEY = "authorization_cache_key:";
    private static final String AUTHORIZATION_CACHE_NAME = "authorization_cache_name";

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        MySimplePrincipalCollection myPrincipal = (MySimplePrincipalCollection) principalCollection;
        //获取登录用户
        UserInfoVO userInfoVO = myPrincipal.getUserInfoVO();
        //添加用户角色与权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        userInfoVO.getRoleList().forEach(role -> {
            simpleAuthorizationInfo.addRole(role.getId()+":"+role.getRoleName());
        });
        userInfoVO.getSystemFunctionList().forEach(func -> {
            simpleAuthorizationInfo.addStringPermission(func.getFuncCode());
        });
        //将用户信息保存到subject对象里
        myPrincipal.setUserInfoVO(userInfoVO);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return  new MySimpleAuthenticationInfo();
    }

    @Override
    protected void afterCacheManagerSet() {
        getAvailableAuthorizationCache();
    }

    @Override
    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            return null;
        }

        AuthorizationInfo info = null;

        if (logger.isTraceEnabled()) {
            logger.trace("Retrieving AuthorizationInfo for principals [" + principals + "]");
        }

        Cache<Object, AuthorizationInfo> cache = this.getAvailableAuthorizationCache();
        if (cache != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to retrieve the AuthorizationInfo from cache.");
            }
//            Object key = getAuthorizationCacheKey(principals);
            Object key = AUTHORIZATION_CACHE_KEY + principals.getPrimaryPrincipal();
            info = cache.get(key);
            if (logger.isTraceEnabled()) {
                if (info == null) {
                    logger.trace("No AuthorizationInfo found in cache for principals [" + principals + "]");
                } else {
                    logger.trace("AuthorizationInfo found in cache for principals [" + principals + "]");
                }
            }
        }


        if (info == null) {
            // Call template method if the info was not found in a cache
            info = doGetAuthorizationInfo(principals);
            // If the info is not null and the cache has been created, then cache the authorization info.
            if (info != null && cache != null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Caching authorization info for principals: [" + principals + "].");
                }
//                Object key = getAuthorizationCacheKey(principals);
                Object key = AUTHORIZATION_CACHE_KEY + principals.getPrimaryPrincipal();
                cache.put(key, info);
            }
        }

        return info;
    }

    private Cache<Object, AuthorizationInfo> getAuthorizationCacheLazy() {

        if (getAuthorizationCache() == null) {

            if (logger.isDebugEnabled()) {
                logger.debug("No authorizationCache instance set.  Checking for a cacheManager...");
            }

            CacheManager cacheManager = getCacheManager();

            if (cacheManager != null) {
                String cacheName = MyShiroRealm.AUTHORIZATION_CACHE_NAME;
                if (logger.isDebugEnabled()) {
                    logger.debug("CacheManager [" + cacheManager + "] has been configured.  Building " +
                            "authorization cache named [" + cacheName + "]");
                }
                setAuthorizationCache(cacheManager.getCache(cacheName));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No cache or cacheManager properties have been set.  Authorization cache cannot " +
                            "be obtained.");
                }
            }
        }

        return getAuthorizationCache();
    }

    private Cache<Object, AuthorizationInfo> getAvailableAuthorizationCache() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache == null && isAuthorizationCachingEnabled()) {
            cache = getAuthorizationCacheLazy();
        }
        return cache;
    }
}
