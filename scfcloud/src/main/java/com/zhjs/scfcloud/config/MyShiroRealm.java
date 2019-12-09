package com.zhjs.scfcloud.config;

import com.zhjs.scfcloud.feign.UserFeignService;
import com.zhjs.scfcloud.model.dto.UserLoginDTO;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.enums.CompanyStatusEnum;
import com.zhjs.scfcloud.util.util.StringUtil;
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
import org.springframework.beans.factory.annotation.Autowired;

public class MyShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String AUTHORIZATION_CACHE_KEY = "authorization_cache_key:";
    private static final String AUTHORIZATION_CACHE_NAME = "authorization_cache_name";

    @Autowired
    private UserFeignService userFeignService;

    @Override
    protected void afterCacheManagerSet() {
        getAvailableAuthorizationCache();
    }

    /**
     * 认证.登录
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


    /**
     * 授权
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        MyUsernamePasswordToken myToken = (MyUsernamePasswordToken) authenticationToken;
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (myToken.getPrincipal() == null) {
            return null;
        }
        //获取登录用户
        String loginAccount = myToken.getPrincipal().toString();
        User user = null;
        if(StringUtil.isPhone(loginAccount)){
            user = userFeignService.getUserByPhone(loginAccount).getData();
        }else{
            user = userFeignService.getUserByEmail(loginAccount).getData();
        }

        if(user == null) throw new AuthenticationException("邮箱或手机不存在");
        if(user.getIsDel() == 1) throw new AuthenticationException("您尚未加入任何公司，请联系管理员邀请您加入");

        //获取用户详情
        UserInfoVO userInfoVO = userFeignService.getUserInfo(new UserLoginDTO(user.getId(),myToken.getCompanyId())).getData();

        //登录的公司为无效公司（员工账户 已删除 或者 不处于激活状态）
        if(userInfoVO.getCompanyId() == null) throw new AuthenticationException("您尚未加入任何公司，请联系管理员邀请您加入");
        if(!user.getPassword().equals(String.valueOf(myToken.getPassword()))) throw new AuthenticationException("密码错误");
        //公司是否已删除
        if(userInfoVO.getCompanyIsDelete() == 1) throw new AuthenticationException("您的公司已被删除");
        //公司状态
        if(userInfoVO.getCompanyStatus() == CompanyStatusEnum.status0.getValue()) throw new AuthenticationException("您的公司未激活，系统已向您的邮箱发送了激活邮件，请前往激活");
        if(userInfoVO.getCompanyStatus() == CompanyStatusEnum.status2.getValue()) throw new AuthenticationException("您的公司已被禁用");

        MyUsernamePasswordToken token = (MyUsernamePasswordToken)authenticationToken;
        token.setUserId(user.getId());
        MySimpleAuthenticationInfo simpleAuthenticationInfo = new MySimpleAuthenticationInfo(loginAccount, user.getPassword(), getName());
        simpleAuthenticationInfo.setUserId(user.getId());
        simpleAuthenticationInfo.setCompanyId(userInfoVO.getCompanyId());
        simpleAuthenticationInfo.setUserInfoVO(userInfoVO);

        //更新最后一次登录公司
        user.setLastCompanyId(userInfoVO.getCompanyId());
        userFeignService.updateUser(user);

        //登录时清除上次登录的权限缓存
        clearCachedAuthorizationInfo(loginAccount);
        return simpleAuthenticationInfo;
    }

    //清空权限缓存
    public void clearCachedAuthorizationInfo(String primaryPrincipal){
        if (primaryPrincipal == null) {
            return;
        }

        Cache<Object, AuthorizationInfo> cache = getAvailableAuthorizationCache();
        //cache instance will be non-null if caching is enabled:
        if (cache != null) {
//            Object key = getAuthorizationCacheKey(principals);
            Object key = AUTHORIZATION_CACHE_KEY + primaryPrincipal;
            cache.remove(key);
        }
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
