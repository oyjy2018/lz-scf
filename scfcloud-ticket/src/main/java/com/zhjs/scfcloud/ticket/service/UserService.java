package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.User;

/**
 * 用户账号信息管理业务逻辑接口
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 14:01
 * @since
 */

public interface UserService extends IService<User> {

    //获取公司京东实名属性
    Result getCompanyJdProperty(Long companyId);

    //获取报价公司列表，公司类型∈（保理公司，工程公司，银行）
    Result quotationCompanyList(Long companyId);
}
