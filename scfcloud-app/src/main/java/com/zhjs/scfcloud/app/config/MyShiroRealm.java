package com.zhjs.scfcloud.app.config;

import com.zhjs.scfcloud.app.feign.RoleFeignService;
import com.zhjs.scfcloud.app.feign.UserFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.UserIdAndCompanyIdDTO;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.CompanySelectVO;
import com.zhjs.scfcloud.model.vo.RoleVO;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private RoleFeignService roleFeignService;

    /**
     * 认证.登录
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //添加用户角色与权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
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
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }

        //获取登录用户
        String loginAccount = authenticationToken.getPrincipal().toString();
        User user = null;
        if(StringUtil.isEmail(loginAccount)){
            user = userFeignService.getUserByEmail(loginAccount).getData();
        }else{
            if(!StringUtil.isPhone(loginAccount)){
                return null;
            }
            user = userFeignService.getUserByPhone(loginAccount).getData();
        }
        if(user == null){
            return null;
        }else{
            MySimpleAuthenticationInfo simpleAuthenticationInfo = new MySimpleAuthenticationInfo(loginAccount, user.getPassword(), getName());
            String companyName = null;
            Long companyId = null;
            UserIdAndCompanyIdDTO dto = new UserIdAndCompanyIdDTO();
            dto.setUserId(user.getId());
            List<CompanySelectVO> list = JsonUtil.jsonToBeanList(JsonUtil.toJSON(userFeignService.findCompanyListByUserId(dto).getData()),CompanySelectVO.class);

            for (int i = 0; i < list.size(); i++){
                if(list.get(i).getId().equals(user.getLastCompanyId())){
                    companyId = user.getLastCompanyId();
                    companyName = list.get(i).getCompanyName();
                }
            }
            if (companyId == null){
                if (list.size() > 0){
                    companyId = list.get(0).getId();
                    companyName = list.get(0).getCompanyName();
                }
            }
            simpleAuthenticationInfo.setUser(user);
            simpleAuthenticationInfo.setCompanyId(companyId);
            simpleAuthenticationInfo.setCompanyName(companyName);

            Result<List<Role>> result = roleFeignService.findRoleListByCompanyIdAndUserId(companyId,user.getId());
            if(result.getCode() == Result.RESULT_CODE_SUCCESS){
                simpleAuthenticationInfo.setRoleList(result.getData());
            }

            //更新最后一次登录公司
            user.setLastCompanyId(companyId);
            userFeignService.updateUser(user);
            return simpleAuthenticationInfo;
        }
    }

    //清空权限缓存
    public void clearCachedAuthorizationInfo(PrincipalCollection principals){
        super.clearCachedAuthorizationInfo(principals);
    }
}
