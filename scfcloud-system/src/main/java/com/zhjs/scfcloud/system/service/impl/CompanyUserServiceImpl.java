package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.entity.CompanyUser;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.system.service.CompanyUserService;
import com.zhjs.scfcloud.system.service.RoleService;
import com.zhjs.scfcloud.system.service.SmsCodeService;
import com.zhjs.scfcloud.system.service.UserService;
import com.zhjs.scfcloud.util.enums.CompanyUserEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-30 14:36
 *
 * @author liuchanghai
 * @create 2019-05-30 14:36
 * @since
 */

@Service
public class CompanyUserServiceImpl extends ServiceImpl<CompanyUserMapper, CompanyUser> implements CompanyUserService {
    @Resource
    private CompanyUserMapper companyUserMapper;
    @Resource
    UserService userService;
    @Resource
    CompanyMapper companyMapper;
    @Resource
    RoleService roleService;
    @Resource
    SmsCodeService smsCodeService;


    //是否弹出邀请成员引导(只有一个成员时弹出)
    @Override
    public Result isInvite(Long companyId) {
        List list = companyUserMapper.findCompanyUserListById(companyId);
        Map retMap = new HashMap();
        retMap.put("isInvite",false);
        if (list.size() == 1) {
            retMap.put("isInvite",true);
        }
        return Result.success(retMap);
    }

    @Override
    public List<Long> findCompanyIdsByUserId(Long userId) {
        //查询用户所属公司
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        companyUserQueryWrapper.eq("status", CompanyUserEnum.status_1.getValue());
        companyUserQueryWrapper.eq("user_id", userId);
        List<CompanyUser> companyUserList = companyUserMapper.selectList(companyUserQueryWrapper);
        return companyUserList.stream().map(companyUser -> companyUser.getCompanyId()).sorted().collect(Collectors.toList());
    }

    @Override
    public CompanyUserInfoDTO findCompanyUserInfo(Long userId) {
        CompanyUserInfoDTO companyUserInfoDTO = new CompanyUserInfoDTO();
        //用户基本信息
        User user = userService.getById(userId);
        companyUserInfoDTO.setGender(user.getGender());
        companyUserInfoDTO.setUserName(user.getUserName());
        companyUserInfoDTO.setEmail(user.getEmail());
        companyUserInfoDTO.setPhone(user.getPhone());
        companyUserInfoDTO.setCreateTime(user.getCreateTime());
        //公司信息
        List<Long> companyIds = this.findCompanyIdsByUserId(userId);
        if(!companyIds.isEmpty()){
            Company company = companyMapper.selectById(companyIds.get(0));
            companyUserInfoDTO.setCompanyName(company.getCompanyName());
            //用户组信息
            QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.eq("company_id",company.getId());
            List<Role> roleList = roleService.findRoleListByCompanyIdAndUserId(company.getId(),userId);
            if(!roleList.isEmpty()){
                companyUserInfoDTO.setRoleName(roleList.stream().map(Role::getRoleName).collect(Collectors.joining(",")));
            }
        }
        return companyUserInfoDTO;
    }

    @Override
    public void updateCompanyUserInfo(Long userId, CompanyUserInfoEditDTO companyUserInfoEditDTO) {
        User user = userService.getById(userId);
        if(!StringUtils.isEmpty(companyUserInfoEditDTO.getUserName())){
            user.setUserName(companyUserInfoEditDTO.getUserName());
        }
        if(companyUserInfoEditDTO.getGender() != null){
            user.setGender(companyUserInfoEditDTO.getGender());
        }
        userService.updateById(user);
    }

    @Override
    public void leave(Long userId) {
        //查询用户所属公司
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        companyUserQueryWrapper.eq("status", CompanyUserEnum.status_1.getValue());
        companyUserQueryWrapper.eq("user_id", userId);
        List<CompanyUser> companyUserList = companyUserMapper.selectList(companyUserQueryWrapper);
        if(!companyUserList.isEmpty()){
            //离职
            userService.updateCompanyUserByCompanyUserId(companyUserList.get(0).getId(),CompanyUserEnum.status_2.getValue());
        }
    }

    @Override
    public boolean checkCompanyUserPassword(Long userId, String password) {
        User user = userService.getById(userId);
        return DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword());
    }

    @Override
    public Result updateUserEmail(Long userId, CompanyUserEmailEditDTO emailEditDTO) {
        User user = userService.getById(userId);
        //校验验证码
        Result emailCheckResult = smsCodeService.emailVCodeCheck(emailEditDTO.getEmail(),emailEditDTO.getCode());
        if(emailCheckResult.getCode() == Result.RESULT_CODE_FAILURE) return emailCheckResult;

        user.setEmail(emailEditDTO.getEmail());
        userService.updateById(user);
        return Result.success();
    }

    @Override
    public Result updateUserPhone(Long userId, CompanyUserPhoneEditDTO phoneEditDTO) {
        User user = userService.getById(userId);
        //校验验证码
        CheckPhoneVCodeDTO checkPhoneVCodeDTO = new CheckPhoneVCodeDTO();
        checkPhoneVCodeDTO.setPhone(phoneEditDTO.getPhone());
        checkPhoneVCodeDTO.setVcode(phoneEditDTO.getCode());
        if(!smsCodeService.checkVCode(checkPhoneVCodeDTO)) return Result.failure("验证码错误");

        user.setPhone(phoneEditDTO.getPhone());
        userService.updateById(user);
        return Result.success();
    }

    @Override
    public Result updatePassword(Long userId, CompanyUserPasswordEditDTO passwordEditDTO) {
        User user = userService.getById(userId);
        if(!DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()).equals(user.getPassword())){
            return Result.failure("原密码错误");
        }

        user.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
        userService.updateById(user);
        return Result.success();
    }
}
