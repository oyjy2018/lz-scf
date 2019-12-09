package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.exception.ScfRuntimeException;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.UserTransfer;
import com.zhjs.scfcloud.model.vo.RoleUserVO;
import com.zhjs.scfcloud.model.vo.UserEditInfoVO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.model.vo.UserListVO;
import com.zhjs.scfcloud.system.service.*;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.*;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.lang.System;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户账号信息管理业务逻辑实现类
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 14:01
 * @since
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserTransfer userTransfer;

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private SystemFunctionMapper functionMapper;
    @Resource
    private CompanyUserMapper companyUserMapper;
    @Autowired
    private CompanyUserService companyUserService;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private CompanyAuditMapper companyAuditMapper;
    @Resource
    private RoleUserMapper roleUserMapper;
    @Autowired
    private RoleUserService roleUserService;
    @Resource
    private DepartmentUserMapper departmentUserMapper;
    @Autowired
    private DepartmentUserService departmentUserService;
    @Autowired
    private DepartmentService departmentService;
    @Resource
    private MenuMapper menuMapper;
    @Autowired
    private EmailTool emailTool;
    @Value("${frontConfirmUrl}")
    private String frontConfirmUrl;
    @Value("${confirmUrlExpired}")
    private Long confirmUrlExpired;
    @Autowired
    private SmsCodeService smsCodeService;

    /**
     * 查询成员管理列表 分页
     * @param dto 入参
     * @return
     */
    @Override
    public Page<UserListVO> findUserlist(FindUserListDTO dto) {
        Page<UserListVO> page = new Page<>(1,20);
        if(dto != null){
            if(dto.getSize() != null){
                page.setSize(dto.getSize());
            }
            if(dto.getCurrent() != null){
                page.setCurrent(dto.getCurrent());
            }

        }
        //用户组查询条件
        List<Long> userIDs = null;
        if(dto.getRoleId() != null){
            QueryWrapper<RoleUser> roleUserQueryWrapper = new QueryWrapper<>();
            roleUserQueryWrapper.eq("role_id",dto.getRoleId());
            List<RoleUser> roleUserList =  roleUserService.list(roleUserQueryWrapper);
            if(roleUserList.isEmpty()) return page;
            userIDs = roleUserList.stream().map(roleUser -> roleUser.getUserId()).sorted().collect(Collectors.toList());
        }
        return (Page<UserListVO>) page.setRecords(
                userMapper.querySelective(page,
                        dto.getCompanyId(),
                        getDepartmentIds(dto.getDeptId()),
                        userIDs,
                        dto.getStatus(),
                        dto.getUserName()));
    }

    private List<Long> getDepartmentIds(Long deptId){
        List<Long> ids = new ArrayList<>();
        ids.add(deptId);
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq("parent_id",deptId);
        List<Department> deptList = departmentService.list(departmentQueryWrapper);
        if(deptList.isEmpty()){
            return ids;
        }
        deptList.forEach(department -> {
            ids.add(department.getId());
            ids.addAll(getDepartmentIds(department.getId()));
        });
        return ids;
    }

    @Override
    public User selectOne(Object[]... params) {
        if(params == null || params.length == 0) return null;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for(Object[] param:params){
            if(param[0] == null) return null;
            queryWrapper.eq(param[0].toString(),param[1]);
        }
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public UserInfoVO getUserInfo(Long userId, Long companyId) {
        //查询用户
        User user = selectOne(new Object[]{"id", userId});
        //封装用户信息
        UserInfoVO userInfoVO = userTransfer.toUserInfoVO(user);

        //查询加入的公司列表
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        companyUserQueryWrapper.eq("user_id",user.getId());
        //未删除
        companyUserQueryWrapper.eq("is_delete",0);
        //在职
        companyUserQueryWrapper.eq("status",CompanyUserEnum.status_1.getValue());
        List<CompanyUser> companyUserList = companyUserService.list(companyUserQueryWrapper);
        //无有效公司，返回
        if(companyUserList.isEmpty()) return userInfoVO;
        //选登录公司
        CompanyUser loginCompanyUser = null;
        //指定登录公司
        if(companyId != null){
            loginCompanyUser = companyUserList.stream().filter(companyUser -> companyUser.getCompanyId() == companyId).findFirst().orElse(null);
            //指定登录公司无效，返回
            if(loginCompanyUser == null) return userInfoVO;
        //最近登录公司
        }else if (user.getLastCompanyId() != null){
            loginCompanyUser = companyUserList.stream().filter(companyUser -> companyUser.getCompanyId() == user.getLastCompanyId().longValue()).findFirst().orElse(null);
            //最近登录公司无效，返回
            if(loginCompanyUser == null) return userInfoVO;
        //有效公司
        }else{
            loginCompanyUser = companyUserList.get(0);
        }
        //登录公司
        Company loginCompany = companyMapper.selectById(loginCompanyUser.getCompanyId());
        userInfoVO.setCompanyId(loginCompany.getId());
        userInfoVO.setCompanyName(loginCompany.getCompanyName());
        userInfoVO.setCompanyStatus(loginCompany.getStatus());
        userInfoVO.setCompanyIsDelete(loginCompany.getIsDelete());

        List<Role> roleList = roleMapper.findUserRolesByCompanyId(user.getId(),loginCompany.getId());
        if(!roleList.isEmpty()){
            //设置用户组
            userInfoVO.setRoleList(roleList);

            String roles = StringUtils.join(roleList.stream().map(role -> {return role.getId();}).collect(Collectors.toList()),",");
            //判定授信用信流程
            String parentIds = null;
            QueryWrapper<CompanyAudit> auditQueryWrapper = new QueryWrapper<>();
            auditQueryWrapper.eq("company_id",loginCompany.getId()).eq("status", CompanyAuditEnum.Status.status1.getStatus());
            CompanyAudit companyAudit = companyAuditMapper.selectOne(auditQueryWrapper);
            if(companyAudit != null){
                if(StringUtils.isNotBlank(companyAudit.getFlowList())){
                    if(!companyAudit.getFlowList().contains(",")){
                       parentIds = companyAudit.getFlowList().contains("1") ? "2" : "1";
                    }
                }else{
                    parentIds = "1,2";
                }
            }
            //设置菜单权限
            userInfoVO.setMenuList(menuMapper.findHaveMenu(roles,parentIds));
            //设置功能权限
            userInfoVO.setSystemFunctionList(functionMapper.findFuncListByRoleIds(roles));
        }
        //设置公司集合
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.in("id",companyUserList.stream().map(companyUser -> companyUser.getCompanyId()).collect(Collectors.toList()));
        companyQueryWrapper.eq("is_delete",0);
        companyQueryWrapper.eq("status", CompanyStatusEnum.status1.getValue());
        userInfoVO.setCompanyList(companyMapper.selectList(companyQueryWrapper));

        return userInfoVO;
    }

    /**
     * 查询用户的数据权限
     * @param userId 入参 用户ID
     * @return
     */
    @Override
    public Result findUserDataPermissionList(Long userId) {
        if (userId == null){
            Result.failure("userId不能为空");
        }
        User user = getById(userId);
        //用户数据权限类型为个人的，返回空数组
        if(user.getPermissionType() == null || user.getPermissionType().intValue() == UserEnum.PermissionType.PermissionType1.getStatus()){
            return Result.success(new ArrayList<>());
        }
        String[] permissionOrgIds = user.getPermissionOrgIds().split(",");
        List<Long> ids = new ArrayList<>();
        for (String id: permissionOrgIds){
            if(!StringUtil.isEmpty(id)){
                ids.add(Long.parseLong(id));
            }
        }
        if (ids.size() == 0){
            Result.success("暂无数据");
        }
        List<Company> companyList = companyMapper.selectList(new QueryWrapper<Company>().in("id",ids));
        if(companyList == null || companyList.isEmpty()) {
            return Result.failure("机构权限数据丢失");
        }
        List<Map<String, Object>> list = companyList.stream().map(company -> new HashMap<String, Object>(){{
            put("id",company.getId());
            put("companyName",company.getCompanyName());
        }}).collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 检查密码是否正确
     * @param userId 用户ID
     * @param password 密码
     * @return
     */
    @Override
    public boolean isPassword(Long userId, String password) {
        User user = getById(userId);
        if(user == null) return false;
        return user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()));
    }

    /**
     * 邀请添加多个用户
     * @param dto 入参
     * @return
     */
    @Transactional
    @Override
    public Result addUserList(AddUserList dto) {
        if(dto.getEmails().size() == 0){
            return Result.failure("无邮箱");
        }
        //查询公司
        Company company = companyMapper.selectById(dto.getCompanyId());
        if (company == null){
            return Result.failure("公司不存在");
        }

        List<String> useEmailList = new ArrayList<>();
        for (String email: dto.getEmails()){
            boolean isNewUser = true; //是否为新用户
            //检查已被使用的邮箱
            QueryWrapper<User> userEmailQueryWrapper = new QueryWrapper<>();
            userEmailQueryWrapper.eq("email", email);
            userEmailQueryWrapper.eq("is_del",CommonEnum.WhetherEnum.NO.getStatus());
            userEmailQueryWrapper.last("limit 1");
            User user = userMapper.selectOne(userEmailQueryWrapper);
            if ( user != null){
                //用户存在  且已绑定了手机为老用户
                if (!StringUtil.isEmpty(user.getPhone())) {
                    isNewUser = false;
                }
                //判断用户是否和公司有关联
                LambdaQueryWrapper<CompanyUser> companyUserWrapper = new QueryWrapper<CompanyUser>().lambda();
                companyUserWrapper.eq(CompanyUser::getCompanyId,dto.getCompanyId());
                companyUserWrapper.eq(CompanyUser::getUserId,user.getId());
                companyUserWrapper.eq(CompanyUser::getIsDelete,CommonEnum.WhetherEnum.NO.getStatus());
                //已存在于当前公司
                if (companyUserMapper.selectCount(companyUserWrapper) > 0) {
                    useEmailList.add(email);
                    continue;
                }
            } else {
                // 新增用户
                user = new User();
                user.setEmail(email)
                        .setPermissionType(dto.getPermissionType())
                        .setPermissionOrgIds(dto.getPermissionOrgIds() == null ? null : String.join(",", new HashSet<>(dto.getPermissionOrgIds())))
                        .setIsEmailValid(CommonEnum.WhetherEnum.NO.getStatus())
                        .setCreateTime(LocalDateTime.now())
                        .setIsDel(0);
                save(user);
            }

            //不管是否为新用户，只要和公司不存在有效关联，都重新设置默认的关联
            // 加入角色组
            for (Long roleId: dto.getRoleIds()){
                RoleUser roleUser = new RoleUser();
                roleUser.setRoleId(roleId).setUserId(user.getId());
                roleUserMapper.insert(roleUser);
            }
            // 加入部门
            if(dto.getDeptIds().isEmpty()){
                //默认加入顶级部门
                QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
                departmentQueryWrapper.eq("company_id", dto.getCompanyId()).eq("parent_id", 0);
                Department department = departmentService.getOne(departmentQueryWrapper);
                if(department != null){
                    DepartmentUser departmentUser = new DepartmentUser();
                    departmentUser.setDeptId(department.getId()).setUserId(user.getId()).setStatus(1);
                    departmentUserMapper.insert(departmentUser);
                }
            }else{
                for (Long deptId: dto.getDeptIds()){
                    DepartmentUser departmentUser = new DepartmentUser();
                    departmentUser.setDeptId(deptId).setUserId(user.getId()).setStatus(1);
                    departmentUserMapper.insert(departmentUser);
                }
            }
            // 加入公司
            CompanyUser companyUser = new CompanyUser();
            companyUser.setCompanyId(dto.getCompanyId())
                    .setUserId(user.getId())
                    .setStatus(CompanyUserEnum.status_0.getValue())
                    .setCreateTime(LocalDateTime.now())
                    .setIsDelete(0);
            companyUserMapper.insert(companyUser);

            //抛线程异步发邮件  避免超时
            sendEmail(email,company,dto.getUserName(),isNewUser);
        }
        //全部邮箱都被使用了
        if(useEmailList.size() == dto.getEmails().size()){
            return Result.failure("邀请邮箱的所属用户已"+(dto.getEmails().size()==1?"":"全")+"是您公司的员工");
        }
        //部分邮箱被使用，返回提示被使用的部分
        if (!useEmailList.isEmpty()){
            String msg = useEmailList.stream().collect(Collectors.joining(","));
            return Result.success("以下邮箱的所属用户已是您公司的员工:"+msg+";已给其余邮箱发送邀请邮件");
        }
        return Result.success();
    }

    //发送邀请邮件
    private void sendEmail(String email, Company company, String userName, boolean isNewUser) {
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{email});
        emailVO.setTitle("【领筑金融云】邀请成员加入公司");
        if (isNewUser) {
            emailVO.setTemplate("inviteNewUser.html");
        }else {
            emailVO.setTemplate("inviteExistUser.html");
        }
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("company", company.getCompanyName());
        valueMap.put("name", userName);
        valueMap.put("email", email);
        valueMap.put("url", frontConfirmUrl);
        long expireTime = System.currentTimeMillis()+confirmUrlExpired*60*1000;
        //logger.info("expireTime:{}",expireTime);
        //处理网址参数
        valueMap.put("param", URLEncoder.encode(email+","+ company.getCompanyName()+","+company.getId()+","+expireTime+","+isNewUser)
                .replaceAll("%40","@")
                .replaceAll("%2C",","));
        valueMap.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        emailVO.setVariables(valueMap);

        emailTool.commonSendMail(emailVO);
    }

    /**
     * 查询编辑用户的信息
     * @param userId 入参 用户ID
     * @param companyId 入参 公司ID
     * @return
     */
    @Override
    public UserEditInfoVO findEditUserInfo(Long userId, Long companyId) {
        User user = getById(userId);
        if(user == null){
            return null;
        }

        // 用户信息转换
        UserEditInfoVO info = new UserEditInfoVO();
        info.setId(user.getId());
        info.setEmail(user.getEmail());
        info.setGender(user.getGender());
        info.setUserName(user.getUserName());
        info.setPermissionType(user.getPermissionType());
        info.setPhone(user.getPhone());
        if(!StringUtil.isEmpty(user.getPermissionOrgIds())){
            // permissionOrgIds 字符串转数组
            List<Long> permissionOrgIds = new ArrayList<>();
            String[] splits = user.getPermissionOrgIds().split(",");
            for (String s: splits){
                permissionOrgIds.add(Long.parseLong(s));
            }
            info.setPermissionOrgIds(permissionOrgIds);
        }
        // 查询用户在公司拥有的角色
        List<Long> deptIds = departmentUserMapper.findDeptIdList(userId, companyId);
        info.setDeptIds(deptIds);
        // 查询用户在公司拥有的角色
        List<Long> roleIds = roleUserMapper.findRoleIdList(userId, companyId);
        info.setRoleIds(roleIds);
        return info;
    }

    /**
     * 保存编辑用户的信息
     * @param dto 入参
     * @return
     */
    @Override
    @Transactional
    public boolean saveEditUserInfo(SaveEditUserInfoDTO dto) {
        // 判断用户ID是否为空
        if(dto.getId() == null){
            return false;
        }
        // 更新用户信息

        User user = getById(dto.getId());
        user.setUserName(dto.getUserName())
                .setPermissionType(dto.getPermissionType());
        if(!StringUtil.isEmpty(dto.getPermissionOrgIds())){
            user.setPermissionOrgIds(String.join(",",new HashSet<>(dto.getPermissionOrgIds())));
        }
        updateById(user);
        // 更新部门
        departmentUserMapper.deleteDepartmentUserByUserIdAndCompanyId(dto.getId(), dto.getCompanyId());
        List<DepartmentUser> deptUsers = new ArrayList<>();
        for(Long deptId: dto.getDeptIds()){
            DepartmentUser departmentUser = new DepartmentUser();
            departmentUser.setDeptId(deptId).setUserId(dto.getId()).setStatus(1);
            deptUsers.add(departmentUser);
        }
        departmentUserService.saveBatch(deptUsers);
        // 更新角色
        boolean result1 = roleUserMapper.deleteByUserIdAndCompanyId(dto.getId(), dto.getCompanyId());
        List<RoleUser> roleUsers = new ArrayList<>();
        for(Long roleId: dto.getRoleIds()){
            RoleUser roleUser = new RoleUser();
            roleUser.setRoleId(roleId).setUserId(dto.getId());
            roleUsers.add(roleUser);
        }
        roleUserService.saveBatch(roleUsers);
        // 更新用户的数据权限
//        QueryWrapper<CompanyUser> where = new QueryWrapper<>();
//        where.eq("user_id", dto.getId());
//        boolean result2 = companyUserService.remove(where);
//        List<CompanyUser> companyUsers = new ArrayList<>();
//        for(Long comapnyId: dto.getCompanyIds()){
//            CompanyUser companyUser = new CompanyUser();
//            companyUser.setCompanyId(comapnyId).setUserId(dto.getId()).setStatus(1);
//            companyUsers.add(companyUser);
//        }
//        companyUserService.saveBatch(companyUsers);

        return true;
    }

    /**
     * 编辑用户的状态
     * @param companyUserId 公司用户ID
     * @param status 状态
     * @return
     */
    @Override
    public boolean updateCompanyUserByCompanyUserId(Long companyUserId, Integer status) {
        CompanyUser companyUser = companyUserService.getById(companyUserId);
        companyUser.setStatus(status);
        companyUser.setUpdateTime(LocalDateTime.now());
        return companyUserService.updateById(companyUser);
    }

    /**
     * 删除公司用户
     * @param companyUserId 公司用户ID
     * @return
     */
    @Override
    @Transactional
    public Result deleteCompanyUserByCompanyUserId(Long companyUserId) {
        //删除公司用户关系
        CompanyUser companyUser = companyUserService.getById(companyUserId);
        if (companyUser == null) return Result.success("公司用户关系不存在");
        if (!companyUserService.remove(new QueryWrapper<CompanyUser>().lambda()
                .eq(CompanyUser::getUserId,companyUser.getUserId())
                .eq(CompanyUser::getCompanyId,companyUser.getCompanyId()))) {
            throw new ScfRuntimeException("删除公司用户关系失败");
        }
        //删除用户与公司所有角色的关联
        if (!roleUserMapper.deleteByUserIdAndCompanyId(companyUser.getUserId(),companyUser.getCompanyId())){
            throw new ScfRuntimeException("删除角色用户关系失败");
        }

        //删除用户与公司所有部门的关联
        if (!departmentUserMapper.deleteDepartmentUserByUserIdAndCompanyId(companyUser.getUserId(),companyUser.getCompanyId())) {
            throw new ScfRuntimeException("删除部门用户关系失败");
        }
        return Result.success();
    }

    /**
     * 用户接受邀请并注册（新用户）
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Result acceptInviteAndRegister(UserAcceptInviteAndRegisterDTO dto) {
        //短信验证码校验
        CheckPhoneVCodeDTO checkPhoneVCodeDTO = new CheckPhoneVCodeDTO();
        checkPhoneVCodeDTO.setPhone(dto.getPhone());
        checkPhoneVCodeDTO.setVcode(dto.getCode());
        if (!smsCodeService.checkVCode(checkPhoneVCodeDTO)){
            return Result.failure("验证码不正确");
        }
        //验证是否为待激活邮箱
        User user = selectOne(new Object[]{"email", dto.getEmail()});
        if (user == null) {
            return Result.failure("暂未收到公司邀请，无需激活");
        }
        //判断激活状态
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        companyUserQueryWrapper.eq("company_id", dto.getCompanyId());
        companyUserQueryWrapper.eq("user_id",user.getId());
        CompanyUser companyUser = companyUserService.getOne(companyUserQueryWrapper);
        if(companyUser == null){
            return Result.failure("暂未收到公司邀请，无需激活");
        }
        if(companyUser.getStatus() != CompanyStatusEnum.status0.getValue()){
            return Result.failure("您已激活过账户，无需重复激活");
        }
        //校验手机号是否被使用
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", dto.getPhone());
        if (count(userQueryWrapper) > 0) {
            return Result.failure("手机号已被使用");
        }

        //进行激活注册
        user.setUserName(dto.getUserName())
                .setIsEmailValid(CommonEnum.WhetherEnum.YES.getStatus())
                .setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes()))
                .setPhone(dto.getPhone());
        //更新
        if (userMapper.updateById(user) != 1){
            throw new RuntimeException("注册失败");
        }

        //更新company_user表
        companyUserMapper.updateStatusByUserIdAndCompanyId(CompanyUserEnum.status_1.getValue(),user.getId(), dto.getCompanyId());

        return Result.success();
    }

    /**
     * 用户接受邀请并注册（老用户）
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Result acceptInvite(UserAcceptInviteDTO dto) {

        //查询邮箱对应用户
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda()
                .eq(User::getEmail,dto.getEmail())
                .eq(User::getIsDel,CommonEnum.WhetherEnum.NO.getStatus())
                .last("limit 1"));
        if (user == null) {
            return Result.failure("用户不存在,无法加入公司");
        }
        //判断激活状态
        LambdaQueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<CompanyUser>().lambda();
        companyUserQueryWrapper.eq(CompanyUser::getCompanyId, dto.getCompanyId());
        companyUserQueryWrapper.eq(CompanyUser::getUserId,user.getId());
        companyUserQueryWrapper.ne(CompanyUser::getIsDelete,CommonEnum.WhetherEnum.YES.getStatus()); //不是已删除的
        companyUserQueryWrapper.last("limit 1");
        CompanyUser companyUser = companyUserMapper.selectOne(companyUserQueryWrapper);
        if(companyUser == null) return Result.failure("暂未收到公司邀请，无需加入");

        if(companyUser.getStatus() != CompanyStatusEnum.status0.getValue()){
            return Result.failure("您已加入该公司，无需重复加入");
        }
        //更新company_user表
        companyUserMapper.updateStatusByUserIdAndCompanyId(CompanyUserEnum.status_1.getValue(),user.getId(), dto.getCompanyId());

        return Result.success();
    }

    /**
     * 修改密码
     * @param phone
     * @param newPassword
     * @return
     */
    @Override
    @Transactional
    public Result updatePassword(String phone, String newPassword) {
        User user = selectOne(new Object[]{"phone", phone});
        if (user == null) {
            return Result.failure("账号不存在");
        }
        //更新密码
        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        if (userMapper.updateById(user) != 1) {
            throw new RuntimeException("修改失败");
        }

        return Result.success();
    }

    /**
     * 检查手机号是否存在
     * @param phone 入参
     * @return
     */
    @Override
    public boolean phoneCheck(String phone) {
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        lambda.eq(User::getPhone, phone);
        List<User> users = list(lambda);
        return users.size() > 0 ? true : false;
    }

    @Override
    public boolean emailCheck(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email",email).eq("is_del",CommonEnum.WhetherEnum.NO.getStatus());
        return this.count(userQueryWrapper) > 0;
    }

    //检测是为新用户
    @Override
    public Result checkIsNewUser(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email",email).eq("is_del",CommonEnum.WhetherEnum.NO.getStatus()).last("limit 1");
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            return Result.failure("非法邮箱");
        }

        return Result.successData(StringUtil.isEmpty(user.getPhone()+""));
    }

    /**
     * 再次发送邀请邮件
     * @param userReInviteDTO
     * @return
     */
    @Override
    public Result reSendInvite(UserReInviteDTO userReInviteDTO) {
        //检查邮箱账号是否已激活
        User user = selectOne(new Object[]{"email", userReInviteDTO.getEmail()});
        if (user == null) {
            return Result.failure("成员信息丢失");
        }
        //查询公司
        Company company = companyMapper.selectById(userReInviteDTO.getCompanyId());
        if (company == null)
            return Result.failure("公司不存在");

        boolean isNewUser = true; //存在时不为新用户
        //判断用户是否和公司有关联
        LambdaQueryWrapper<CompanyUser> companyUserWrapper = new QueryWrapper<CompanyUser>().lambda();
        companyUserWrapper.eq(CompanyUser::getUserId,user.getId());
        companyUserWrapper.eq(CompanyUser::getCompanyId,userReInviteDTO.getCompanyId());
        companyUserWrapper.eq(CompanyUser::getIsDelete,CommonEnum.WhetherEnum.NO.getStatus());
        //已存在于当前公司
        if (companyUserMapper.selectCount(companyUserWrapper) > 0) {
            isNewUser = false;
        }

        //抛线程异步发邮件  避免超时
        sendEmail(userReInviteDTO.getEmail(),company,userReInviteDTO.getUserName(), isNewUser);

        return Result.success();
    }

    @Override
    public Result findByCompanyId(Long companyId) {
        List<RoleUserVO> users = userMapper.findByCompanyId(companyId);
        return Result.success(users);
    }

    /**
     * 根据用户ID获取我的详情信息
     * @param id 用户ID
     * @return
     */
    @Override
    public Result myDetailsByUserId(Long id) {
        User user = getById(id);
        if(user == null){
            return Result.failure("用户不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("userId",user.getId());
        result.put("phone",user.getPhone());
        result.put("email",user.getEmail());
        if (!StringUtil.isEmpty(user.getPassword())){
            result.put("loginPassword", "已设置");
        }else {
            result.put("loginPassword", "未设置");
        }
        return Result.success(result);
    }

    /**
     * 根据用户ID修改邮箱
     * @param userId 用户ID
     * @param email 邮箱
     * @return
     */
    @Override
    public Result updateEmailByUserId(Long userId, String email) {
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda().eq(User::getEmail, email);
        User user = getOne(lambda);
        if (user != null){
            Result.failure("此邮箱已被使用");
        }
        user = getById(userId);
        user.setEmail(email);
        boolean result = updateById(user);
        if(!result){
            Result.failure("修改失败");
        }
        return Result.success("修改成功");
    }

    /**
     * 根据用户ID修改手机号
     * @param userId 用户ID
     * @param phone 手机号
     * @return
     */
    @Override
    public Result updatePhoneByUserId(Long userId, String phone) {
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda().eq(User::getPhone, phone);
        User user = getOne(lambda);
        if (user != null){
            Result.failure("此手机号已被使用");
        }
        user = getById(userId);
        user.setPhone(phone);
        boolean result = updateById(user);
        if(!result){
            Result.failure("修改失败");
        }
        return Result.success("修改成功");
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

}
