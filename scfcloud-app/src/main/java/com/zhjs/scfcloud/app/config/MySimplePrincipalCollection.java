package com.zhjs.scfcloud.app.config;

import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.RoleVO;
import lombok.Data;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/5/23 10:40
 */

@Data
public class MySimplePrincipalCollection extends SimplePrincipalCollection {

    private Long userId;

    private String userName;

    private String accountNo;

    private Long companyId;

    private String companyName;

    private Long roleId;

    private User user;

    private List<Role> roleList;

    public MySimplePrincipalCollection(){

    }

    public MySimplePrincipalCollection(Object principal, String realmName){
        super(principal, realmName);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
