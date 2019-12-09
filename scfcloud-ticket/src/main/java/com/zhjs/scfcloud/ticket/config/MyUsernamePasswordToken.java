package com.zhjs.scfcloud.ticket.config;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author: dailongting
 * @date:2019/5/22 17:23
 */
public class MyUsernamePasswordToken extends UsernamePasswordToken {
    private Long companyId;

    private Long userId;

    public MyUsernamePasswordToken(){

    }

    public MyUsernamePasswordToken(String username, String password){
        super(username, password);
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
