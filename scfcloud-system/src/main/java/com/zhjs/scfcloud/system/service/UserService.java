package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.UserEditInfoVO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.model.vo.UserListVO;

/**
 * 用户账号信息管理业务逻辑接口
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 14:01
 * @since
 */

public interface UserService extends IService<User> {

    /**
     * 查询成员管理列表 分页
     * @param dto 入参
     * @return
     */
    Page<UserListVO> findUserlist(FindUserListDTO dto);

    User selectOne(Object[] ...params);

    /**
     * 获取用户详细信息
     * @param userId
     * @param companyId
     * @return
     */
    UserInfoVO getUserInfo(Long userId, Long companyId);

    /**
     * 邀请添加多个用户
     * @param dto 入参
     * @return
     */
    Result addUserList(AddUserList dto);

    /**
     * 查询编辑用户的信息
     * @param userId 入参 用户ID
     * @param companyId 入参 公司ID
     * @return
     */
    UserEditInfoVO findEditUserInfo(Long userId, Long companyId);

    /**
     * 保存编辑成员的信息
     * @param dto 入参
     * @return
     */
    boolean saveEditUserInfo(SaveEditUserInfoDTO dto);

    /**
     * 更新公司用户状态
     * @param companyUserId
     * @param status
     * @return
     */
    boolean updateCompanyUserByCompanyUserId(Long companyUserId, Integer status);

    /**
     * 删除公司用户(逻辑删除)
     * @param companyUserId
     * @return
     */
    Result deleteCompanyUserByCompanyUserId(Long companyUserId);

    /**
     * 用户接受邀请并注册（新用户）
     * @param dto
     * @return
     */
    Result acceptInviteAndRegister(UserAcceptInviteAndRegisterDTO dto);

    /**
     * 用户接受邀请并注册（新用户）
     * @param dto
     * @return
     */
    Result acceptInvite(UserAcceptInviteDTO dto);

    /**
     * 修改密码
     * @param phone
     * @param newPassword
     * @return
     */
    Result updatePassword(String phone, String newPassword);

    /**
     * 检查手机号是否存在
     * @param phone 入参
     * @return
     */
    boolean phoneCheck(String phone);

    /**
     * 检查邮箱是否存在
     * @param email 入参
     * @return
     */
    boolean emailCheck(String email);

    //检测是为新用户
    Result checkIsNewUser(String email);

    /**
     * 再次发送邀请邮件
     * @param userReInviteDTO
     * @return
     */
    Result reSendInvite(UserReInviteDTO userReInviteDTO);

    /**
     * 获取公司成员
     * @param companyId 入参 公司ID
     * @return
     */
    Result findByCompanyId(Long companyId);

    /**
     * 查询我的详情
     * @param id 用户ID
     * @return
     */
    Result myDetailsByUserId(Long id);

    /**
     * 根据用户ID修改邮箱
     * @param userId 用户ID
     * @param email 邮箱
     * @return
     */
    Result updateEmailByUserId(Long userId, String email);

    /**
     * 根据用户ID修改手机号
     * @param userId 用户ID
     * @param phone 手机号
     * @return
     */
    Result updatePhoneByUserId(Long userId, String phone);

    /**
     * 检查密码是否正确
     * @param userId 用户ID
     * @param password 密码
     * @return
     */
    boolean isPassword(Long userId, String password);


    /**
     * 查询用户的数据权限
     * @param userId 入参 用户ID
     * @return
     */
    Result findUserDataPermissionList(Long userId);
}
