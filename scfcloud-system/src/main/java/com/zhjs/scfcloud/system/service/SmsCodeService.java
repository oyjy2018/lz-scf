package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.PhoneVaildCodeDTO;
import com.zhjs.scfcloud.model.entity.VCodeValid;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-31 18:20
 *
 * @author liuchanghai
 * @create 2019-05-31 18:20
 * @since
 */
public interface SmsCodeService extends IService<VCodeValid> {

    Result sendSms(PhoneVaildCodeDTO dto);

    boolean checkVCode(CheckPhoneVCodeDTO dto);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return
     */
    Result sendEmailVCode(String email,String userName);

    /**
     * 邮箱验证码校验
     * @param email 邮箱
     * @param vCode 验证码
     * @return
     */
    Result emailVCodeCheck(String email, String vCode);
}
