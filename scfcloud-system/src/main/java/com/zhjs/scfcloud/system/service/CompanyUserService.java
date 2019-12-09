package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.CompanyUser;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-30 14:36
 *
 * @author liuchanghai
 * @create 2019-05-30 14:36
 * @since
 */
public interface CompanyUserService extends IService<CompanyUser> {
    //是否弹出邀请成员引导(只有一个成员时弹出)
    Result isInvite(Long companyId);

    /**
     * 查询用户有效的公司ID集合
     * @param userId 用户ID 入参
     * @return
     */
    List<Long> findCompanyIdsByUserId(Long userId);

    /**
     * 公司用户信息
     * @param userId
     * @return
     */
    CompanyUserInfoDTO findCompanyUserInfo(Long userId);

    /**
     * 修改基本信息
     * @param userId
     * @param companyUserInfoEditDTO
     */
    void updateCompanyUserInfo(Long userId, CompanyUserInfoEditDTO companyUserInfoEditDTO);

    /**
     * 离职
     * @param userId
     */
    void leave(Long userId);

    /**
     * 校验用户密码
     * @param userId
     * @param password
     * @return
     */
    boolean checkCompanyUserPassword(Long userId,String password);

    /**
     * 修改用户邮箱
     * @param userId
     * @param emailEditDTO
     * @return
     */
    Result updateUserEmail(Long userId, CompanyUserEmailEditDTO emailEditDTO);

    /**
     * 修改用户手机
     * @param userId
     * @param phoneEditDTO
     * @return
     */
    Result updateUserPhone(Long userId, CompanyUserPhoneEditDTO phoneEditDTO);

    /**
     * 修改账户密码
     * @param userId
     * @param passwordEditDTO
     * @return
     */
    Result updatePassword(Long userId, CompanyUserPasswordEditDTO passwordEditDTO);
}
