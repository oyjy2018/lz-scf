package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyAuditDTO;
import com.zhjs.scfcloud.model.dto.CompanyAuditDetailDTO;
import com.zhjs.scfcloud.model.dto.CompanyRegDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyAuditListDTO;
import com.zhjs.scfcloud.model.dto.esign.EsignOrganizeCreateDTO;
import com.zhjs.scfcloud.model.dto.esign.EsignPersonCreateDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.CompanyAuditMapper;
import com.zhjs.scfcloud.model.mapper.ESignAccountMapper;
import com.zhjs.scfcloud.model.vo.CompanyAuditListVO;
import com.zhjs.scfcloud.system.service.*;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.*;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.System;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyAuditServiceImpl extends ServiceImpl<CompanyAuditMapper, CompanyAudit> implements CompanyAuditService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CompanyService companyService;
    @Autowired
    UserService userService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentUserService departmentUserService;
    @Autowired
    CompanyUserService companyUserService;
    @Autowired
    RoleService roleService;
    @Autowired
    RoleFunctionService roleFunctionService;
    @Autowired
    RoleUserService roleUserService;
    @Autowired
    CompanySystemVersionService companySystemVersionService;
    @Autowired
    PlatformRoleFunctionConfigService platformRoleFunctionConfigService;
    @Autowired
    CompanyJdVerifiedService companyJdVerifiedService;
    @Autowired
    CompanyCommonService companyCommonService;
    @Autowired
    SignService signService;
    @Resource
    private ESignAccountMapper eSignAccountMapper;
    @Autowired
    InitFlowRedisService initFlowRedisService;
    @Autowired
    FileService fileService;

    @Autowired
    private EmailTool emailTool;
    @Autowired
    private SmsUtil smsUtil;
    @Value("${mail.username}")
    private String senderMailAddress;
    @Value("${frontEndUrl.risk}")
    private String riskFrontEndUrl;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;
    @Value("${confirmUrlExpired}")
    private Long confirmUrlExpired;

    /**
     * 分页查询 公司审核列表数据
     * @param dto
     * @return
     */
    @Override
    public Page<CompanyAuditListVO> selectCompanyAuditList(FindCompanyAuditListDTO dto) {
        Page<CompanyAuditListVO> page= new Page<>(dto.getCurrent(),dto.getSize());
        List<CompanyAuditListVO> auditList = this.baseMapper.selectCompanyAuditList(page, dto.getStatus());
        for(CompanyAuditListVO vo : auditList){
            //转换Id集合
            List<Long> ids = new ArrayList<>();
            if(!StringUtils.isEmpty(vo.getSystemIdList())){
                for(String element : vo.getSystemIdList().split(",")){
                    ids.add(Long.parseLong(element));
                }
            }

            BeanUtils.copyProperties(companyCommonService.selectCompanySystemVersion(ids),vo);
        }
        return (Page<CompanyAuditListVO>) page.setRecords(auditList);
    }

    @Override
    public CompanyAuditDetailDTO selectCompanyAuditDetail(Long companyAuditId) {
        CompanyAuditDetailDTO result = new CompanyAuditDetailDTO();

        CompanyAudit companyAudit = this.getById(companyAuditId);
        BeanUtils.copyProperties(companyAudit,result);
        if(companyAudit.getStatus() == CompanyAuditEnum.Status.status1.getStatus()){
            result.setCompanyCategory(companyService.getById(companyAudit.getCompanyId()).getCompanyCategory());
            if(result.getSystemIdList().contains("1")){
                QueryWrapper<CompanySystemVersion> riskQueryWrapper = new QueryWrapper<>();
                riskQueryWrapper.eq("company_id", companyAudit.getCompanyId());
                riskQueryWrapper.eq("system_version_id",1);
                result.setRiskControlSystemVersionName(companySystemVersionService.count(riskQueryWrapper) > 0 ? "标准版" : "不绑定");
            }
            if(result.getSystemIdList().contains("2")){
                QueryWrapper<CompanySystemVersion> billQueryWrapper = new QueryWrapper<>();
                billQueryWrapper.eq("company_id", companyAudit.getCompanyId());
                billQueryWrapper.eq("system_version_id",2);
                result.setTicketSystemVersionName(companySystemVersionService.count(billQueryWrapper) > 0 ? "标准版" : "不绑定");
            }
        }
        //设置文件路径
        result.setBlicUrlFileName(fileService.findLogByFileUrl(result.getBlicUrl()).getOriginalFileName());
        result.setBlicUrl(FileUtil.getViewFileUrl(result.getBlicUrl()));

        result.setLegalPersonCertFrontFileName(fileService.findLogByFileUrl(result.getLegalPersonCertFrontUrl()).getOriginalFileName());
        result.setLegalPersonCertFrontUrl(FileUtil.getViewFileUrl(result.getLegalPersonCertFrontUrl()));

        result.setLegalPersonCertBackFileName(fileService.findLogByFileUrl(result.getLegalPersonCertBackUrl()).getOriginalFileName());
        result.setLegalPersonCertBackUrl(FileUtil.getViewFileUrl(result.getLegalPersonCertBackUrl()));

        result.setPorxyPersonCertFrontFileName(fileService.findLogByFileUrl(result.getPorxyPersonCertFrontUrl()).getOriginalFileName());
        result.setPorxyPersonCertFrontUrl(FileUtil.getViewFileUrl(result.getPorxyPersonCertFrontUrl()));

        result.setPorxyPersonCertBackFileName(fileService.findLogByFileUrl(result.getPorxyPersonCertBackUrl()).getOriginalFileName());
        result.setPorxyPersonCertBackUrl(FileUtil.getViewFileUrl(result.getPorxyPersonCertBackUrl()));

        if(!StringUtils.isEmpty(result.getPorxyCommissionUrl())){
            result.setPorxyCommissionFileName(fileService.findLogByFileUrl(result.getPorxyCommissionUrl()).getOriginalFileName());
            result.setPorxyCommissionUrl(FileUtil.getViewFileUrl(result.getPorxyCommissionUrl()));
        }
        return result;
    }

    @Override
    @Transactional
    public Result companyRegister(CompanyRegDTO dto) {
        //检查邮箱
        QueryWrapper<User> emailCheckQuery = new QueryWrapper<>();
        emailCheckQuery.eq("email", dto.getPorxyPersonEmail());
        if(userService.count(emailCheckQuery) > 0) return Result.failure("邮箱已被注册");
        //检查公司名称
        QueryWrapper<Company> companyNameQuery = new QueryWrapper<>();
        companyNameQuery.eq("company_name",dto.getCompanyName());
        if(companyService.count(companyNameQuery) > 0) return Result.failure("公司名称已被注册");
        //检查联系电话
        QueryWrapper<User> phoneCheckQuery = new QueryWrapper<>();
        phoneCheckQuery.eq("phone",dto.getPorxyPersonPhone());
        if(userService.count(phoneCheckQuery) > 0) return Result.failure("手机已被注册");

        //添加管理员账户
        User user = new User();
        user.setPhone(dto.getPorxyPersonPhone());
        user.setEmail(dto.getPorxyPersonEmail());
        //MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getPorxyPersonPassword().getBytes()));
        user.setIdCard(dto.getPorxyPersonCertNo());
        user.setUserName(dto.getPorxyPersonName());
        user.setIsEmailValid(CommonEnum.WhetherEnum.NO.getStatus());
        user.setPermissionType(UserEnum.PermissionType.PermissionType2.getStatus());
        user.setCreateTime(LocalDateTime.now());
        user.setIsDel(CommonEnum.WhetherEnum.NO.getStatus());
        userService.save(user);
        //添加待审核记录
        CompanyAudit companyAudit = new CompanyAudit();
        BeanUtils.copyProperties(dto,companyAudit);
        companyAudit.setUserId(user.getId());
        companyAudit.setStatus(CompanyAuditEnum.Status.status0.getStatus());
        companyAudit.setCreateTime(LocalDateTime.now());
        this.save(companyAudit);

        //发送通知邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑金融云】有新的公司注册");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","平台管理员");
        variables.put("content","您好，有新的公司提交了注册申请，请到平台审核。");
        variables.put("url", riskFrontEndUrl);
        variables.put("linkContent","点此进入领筑风控平台");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);

        return Result.success();
    }

    @Override
    @Transactional
    public Result companyAudit(Long companyAuditId,Long auditUserId,CompanyAuditDTO dto) {

        CompanyAudit audit = this.getById(companyAuditId);
        //校验
        if(dto.getStatus() == CompanyAuditEnum.Status.status1.getStatus()){
            if(dto.getTicketSystemVersionId() == null && dto.getRiskControlSystemVersionId() == null) return Result.failure("至少选择一个系统版本");
            if(dto.getRiskControlSystemVersionId() != null && dto.getBusinessFlowList().isEmpty()){
                return Result.failure("至少选择一个业务流程");
            }
        }
        if(audit == null) return Result.failure("不存在的待审核记录");
        if(audit.getStatus() != CompanyAuditEnum.Status.status0.getStatus()) return Result.failure("已审核过，不需要再进行审核");

        //审核拒绝
        if(dto.getStatus() == CompanyAuditEnum.Status.status2.getStatus()){
            //删除用户记录
            userService.removeById(audit.getUserId());
            //修改审核记录
            audit.setStatus(dto.getStatus())
                    .setAuditUserId(auditUserId)
                    .setAuditTime(LocalDateTime.now())
                    .setUpdateById(auditUserId)
                    .setUserId(null)
                    .setUpdateTime(LocalDateTime.now());
            this.updateById(audit);

            return Result.success("审核成功");
        }
        //审核通过

        //初始化公司
        Company company = new Company();
        BeanUtils.copyProperties(audit,company);
        company.setCompanyCategory(dto.getCompanyCategory()).setRegUserId(audit.getUserId());
        company.setIsDelete(0)
                .setStatus(CompanyStatusEnum.status0.getValue())
                .setCreateById(auditUserId)
                .setCreateTime(LocalDateTime.now())
                .setUpdateById(auditUserId)
                .setUpdateTime(LocalDateTime.now());
        companyService.save(company);
        //初始化管理员账户-权限相关
        initCompany(company.getId(),audit,dto.getRiskControlSystemVersionId(),dto.getTicketSystemVersionId());

        //业务流程
        if(!dto.getBusinessFlowList().isEmpty()){
            List<String> flowIds = new ArrayList<>();
            dto.getBusinessFlowList().forEach(element -> {
                if("授信审批流程".equals(element)){
                    flowIds.add("1");
                }
                if("用信审批流程".equals(element)){
                    flowIds.add("2");
                }
            });
            audit.setFlowList(flowIds.stream().collect(Collectors.joining(",")));
        }

        // 更新审核记录表
        audit.setCompanyId(company.getId())
                .setStatus(dto.getStatus())
                .setAuditTime(LocalDateTime.now())
                .setAuditUserId(auditUserId)
                .setUpdateById(auditUserId)
                .setUpdateTime(LocalDateTime.now());
        this.updateById(audit);

        //发送通知
        sendActiveEmail(audit.getPorxyPersonEmail(),companyAuditId);
        sendActiveSms(audit.getCompanyName(),audit.getPorxyPersonEmail(),audit.getPorxyPersonPhone());

        return Result.success("审核成功");
    }
    public void sendActiveEmail(String porxyPersonEmail,Long companyAuditId){
//        EmailVO emailVO = new EmailVO();
//        emailVO.setTo(new String[]{audit.getPorxyPersonEmail()});
//        emailVO.setTemplate("NoticeTemplate.html");
//        emailVO.setTitle("【领筑金融云】公司注册成功提醒");
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
//        variables.put("firstH","【"+company.getCompanyName()+"】");
//        variables.put("content","您好，平台已审核通过您的注册申请，您可以到平台【设置-成员管理】、【设置-权限管理】页面新增公司各个业务的操作人员。");
//        variables.put("url", riskFrontEndUrl);
//        variables.put("linkContent","点此进入领筑风控平台");
//        variables.put("secondUrl", billFrontEndUrl+"/login");
//        variables.put("secondlinkContent", "点此进入领筑票据融资平台");
//        emailVO.setVariables(variables);
//        emailTool.commonSendMail(emailVO);
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{porxyPersonEmail});
        emailVO.setTitle("【领筑金融云】公司激活");
        emailVO.setTemplate("CompanyActive.html");
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("email", porxyPersonEmail);
        valueMap.put("url", riskFrontEndUrl+"/company/active");
        valueMap.put("param", URLEncoder.encode(companyAuditId+","+(System.currentTimeMillis()+confirmUrlExpired*60*1000))
                .replaceAll("%40","@")
                .replaceAll("%2C",","));
        valueMap.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        emailVO.setVariables(valueMap);

        emailTool.commonSendMail(emailVO);

    }

    public void sendActiveSms(String companyName,String email,String phone){
        Map<String, String> params = new HashMap<>();
        params.put("companyName",companyName);
        params.put("email",email);
        smsUtil.lzySmsBsend(phone, SmsUtil.lzy_sms_type_S510,params);
    }

    @Override
    @Transactional
    public Result companyActive(Long companyAuditId) {
        CompanyAudit audit = this.getById(companyAuditId);
        if(audit == null) return Result.failure("审核记录不存在");
        if(audit.getStatus() != CompanyAuditEnum.Status.status1.getStatus()) return Result.failure("尚未审核通过，不可激活。");
        Company company = companyService.getById(audit.getCompanyId());
        if(company.getIsDelete() == CommonEnum.WhetherEnum.YES.getStatus()) return Result.failure("当前公司不存在");
        if(company.getStatus() != CompanyStatusEnum.status0.getValue()) return Result.failure("当前公司状态不是待激活状态，无需激活。");

        //发起京东注册-异步
        QueryWrapper<CompanyJdVerified> jdVerifiedQueryWrapper = new QueryWrapper<>();
        jdVerifiedQueryWrapper.eq("company_id", audit.getCompanyId()).eq("is_register",CommonEnum.WhetherEnum.YES.getStatus());
        if(companyJdVerifiedService.count(jdVerifiedQueryWrapper) == 0) {
            companyJdVerifiedService.companyRegister(company.getId(),audit.getPorxyPersonPhone());
        }
        //E签宝注册-异步
        eSginRegister(audit.getCompanyId(),audit);
        //复制业务流程
        if(!StringUtils.isEmpty(audit.getFlowList())){
            this.baseMapper.initBusinessFlow(company.getId(),audit.getFlowList());
            //业务流程初始化-异步发起
            initFlowRedisService.initFlowRedis();
        }
        //激活公司
        company.setStatus(CompanyStatusEnum.status1.getValue());
        company.setUpdateTime(LocalDateTime.now());
        companyService.updateById(company);
        //激活公司用户
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        companyUserQueryWrapper.eq("company_id",audit.getCompanyId()).eq("user_id",audit.getUserId());
        CompanyUser companyUser = companyUserService.getOne(companyUserQueryWrapper);
        companyUser.setStatus(CompanyUserEnum.status_1.getValue());
        companyUser.setUpdateTime(LocalDateTime.now());
        companyUserService.updateById(companyUser);
        // 更新用户的数据权限
        User user = userService.getById(audit.getUserId());
        user.setPermissionOrgIds(audit.getCompanyId().toString());
        userService.updateById(user);
        //赋予管理员，新公司权限
        User adminUser = userService.getById(1L);
        if(adminUser != null){
            adminUser.setPermissionOrgIds(adminUser.getPermissionOrgIds()+","+audit.getCompanyId());
            userService.updateById(adminUser);
        }

        return Result.success();
    }

    @Override
    public Result sendCompanyActiveEmail(Long companyAuditId) {
        CompanyAudit audit = this.getById(companyAuditId);
        if(audit == null) return Result.failure("审核记录不存在");
        if(audit.getStatus() != CompanyAuditEnum.Status.status1.getStatus()) return Result.failure("尚未审核通过。");
        sendActiveEmail(audit.getPorxyPersonEmail(),audit.getId());
        return Result.success();
    }

    /**
     *
     * @param companyId (公司id)
     * @param audit (审核信息)
     * @param riskControlSystemVersionId（风控平台,版本Id）
     * @param ticketSystemVersionId (商票交易平台,版本Id)
     */
    private void initCompany(Long companyId,CompanyAudit audit,Long riskControlSystemVersionId,Long ticketSystemVersionId){
        //创建部门
        Department department = new Department();
        department.setDeptName(audit.getCompanyName()).setParentId(0L)
                .setCompanyId(companyId).setDeptLevel(0)
                .setCreateTime(LocalDateTime.now());
        departmentService.save(department);

        //创建部门与用户的关系
        DepartmentUser departmentUser = new DepartmentUser();
        departmentUser.setDeptId(department.getId()).setUserId(audit.getUserId()).setStatus(1);
        departmentUserService.save(departmentUser);

        // 建立用户与公司的关系
        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyId(companyId)
                .setUserId(audit.getUserId())
                .setStatus(CompanyUserEnum.status_0.getValue())
                .setCreateTime(LocalDateTime.now())
                .setIsDelete(CommonEnum.WhetherEnum.NO.getStatus());
        companyUserService.save(companyUser);

        // 创建（公司管理员,公司普通员工）角色
        Role manageRole = new Role();
        manageRole.setRoleName("公司管理员")
                .setCompanyId(companyId)
                .setIsEditPrivilege(CommonEnum.WhetherEnum.NO.getStatus())
                .setIsDelete(CommonEnum.WhetherEnum.NO.getStatus())
                .setStatus(CommonEnum.StatusEnnum.status1.getStatus());

        Role staffRole = new Role();
        staffRole.setRoleName("公司普通员工")
                .setCompanyId(companyId)
                .setIsEditPrivilege(CommonEnum.WhetherEnum.YES.getStatus())
                .setIsDelete(CommonEnum.WhetherEnum.NO.getStatus())
                .setStatus(CommonEnum.StatusEnnum.status1.getStatus());

        List<Role> roles = new ArrayList<>();
        Collections.addAll(roles,manageRole,staffRole);
        roleService.saveBatch(roles);
        // 建立 用户-角色 关系
        RoleUser roleUser = new RoleUser();
        roleUser.setUserId(audit.getUserId())
                .setRoleId(manageRole.getId())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        roleUserService.save(roleUser);
        //
        if(riskControlSystemVersionId != null){
            //建立 公司-系统版本 关系
            initCompanyVersion(companyId,riskControlSystemVersionId);
            //初始化 公司-角色 权限
            initCompanyManagerPermission(riskControlSystemVersionId,manageRole.getId(),staffRole.getId());
        }
        if(ticketSystemVersionId != null){
            //建立 公司-系统版本 关系
            initCompanyVersion(companyId,ticketSystemVersionId);
            //初始化 公司-角色 权限
            initCompanyManagerPermission(ticketSystemVersionId,manageRole.getId(),staffRole.getId());
        }
        //默认设置基础功能版本
        initCompanyVersion(companyId,3L);
        initCompanyManagerPermission(3L,manageRole.getId(),staffRole.getId());

    }

    /**
     * 初始化公司系统版本
     * @param companyId
     * @param versionId
     */
    private void initCompanyVersion(Long companyId,Long versionId){
        CompanySystemVersion companySystemVersion = new CompanySystemVersion();
        companySystemVersion.setCompanyId(companyId);
        companySystemVersion.setSystemVersionId(versionId);
        companySystemVersion.setCreateTime(LocalDateTime.now());
        companySystemVersion.setUpdateTime(LocalDateTime.now());
        companySystemVersionService.save(companySystemVersion);
    }

    /**
     * 初始化 公司-角色 权限
     * @param systemVersionId
     * @param manageRoleId
     * @param staffRoleId
     */
    private void initCompanyManagerPermission(Long systemVersionId,Long manageRoleId,Long staffRoleId){

        QueryWrapper<PlatformRoleFunctionConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("system_version_id",systemVersionId);
        List<PlatformRoleFunctionConfig> functionConfigs = platformRoleFunctionConfigService.list(queryWrapper);
        if(functionConfigs.isEmpty()) return;
        List<RoleFunction> roleFunctions = new ArrayList<>();

        functionConfigs.forEach( platformRoleFunctionConfig -> {
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setFunctCode(platformRoleFunctionConfig.getFunctCode());
            roleFunction.setSystemId(platformRoleFunctionConfig.getSystemVersionId());
            roleFunction.setRoleId("公司管理员".equals(platformRoleFunctionConfig.getRoleName()) ? manageRoleId : staffRoleId);
            roleFunctions.add(roleFunction);
        });

        roleFunctionService.saveBatch(roleFunctions);
    }

    /**
     * E签宝注册
     * @param companyId
     * @param audit
     */
    @Override
    public void eSginRegister(Long companyId,CompanyAudit audit){
        //个人账号
        personEsignRegister(companyId,audit);
        // 企业账号
        companyEsignRegister(companyId,audit);
    }

    /**
     * 个人账号E签宝注册
     * @param audit
     */
    @Async
    public void personEsignRegister(Long companyId,CompanyAudit audit){
        EsignPersonCreateDTO personCreateDTO = new EsignPersonCreateDTO();
        personCreateDTO.setUserId(audit.getUserId());
        personCreateDTO.setIdNo(audit.getPorxyPersonCertNo());
        personCreateDTO.setMobile(audit.getPorxyPersonPhone());
        personCreateDTO.setName(audit.getPorxyPersonName());
        Result personResult = signService.createPerson(personCreateDTO);
        if(personResult.getCode() == Result.RESULT_CODE_FAILURE){
            sendEsignEmail("E签宝个人注册失败","公司ID为："+companyId,"失败原因:"+personResult.getMessage());
        }
    }

    /**
     * 企业账号E签宝注册
     * @param companyId
     * @param audit
     */
    @Async
    public void companyEsignRegister(Long companyId,CompanyAudit audit){
        // 企业账号
        EsignOrganizeCreateDTO esignOrganizeCreateDTO = new EsignOrganizeCreateDTO();
        esignOrganizeCreateDTO.setCompanyId(companyId);
        esignOrganizeCreateDTO.setName(audit.getCompanyName());
        esignOrganizeCreateDTO.setRegType("MERGE");
        esignOrganizeCreateDTO.setOrganCode(audit.getCreditCode());
        esignOrganizeCreateDTO.setUserType("2");
        esignOrganizeCreateDTO.setLegalIdNo(audit.getLegalPersonCertNo());
        esignOrganizeCreateDTO.setLegalName(audit.getLegalPersonName());
        esignOrganizeCreateDTO.setAddress(audit.getDetailAddr());
        Result companyResult = signService.createOrganize(esignOrganizeCreateDTO);
        if(companyResult.getCode() == Result.RESULT_CODE_FAILURE){
            sendEsignEmail("E签宝企业注册失败","公司ID为："+companyId,"失败原因:"+companyResult.getMessage());
        }
    }

    @Override
    public Result companyJdAsyncRegister(Long companyId) {
        QueryWrapper<CompanyAudit> companyAuditQueryWrapper = new QueryWrapper<>();
        companyAuditQueryWrapper.eq("company_id",companyId);
        companyAuditQueryWrapper.eq("status",CompanyAuditEnum.Status.status1.getStatus());
        CompanyAudit companyAudit = getOne(companyAuditQueryWrapper);
        if (companyAudit == null){
            return Result.failure("此公司尚未审核通过或不存在");
        }
        //京东注册
        QueryWrapper<CompanyJdVerified> companyJdVerifiedQueryWrapper = new QueryWrapper<>();
        companyJdVerifiedQueryWrapper.eq("company_id",companyId);
        CompanyJdVerified checkJdVerified = companyJdVerifiedService.getOne(companyJdVerifiedQueryWrapper);
        if (checkJdVerified != null && checkJdVerified.getIsRegister() == CommonEnum.WhetherEnum.YES.getStatus()){
            return Result.failure("此公司已成功注册京东账户，无需重复注册");
        }
        companyJdVerifiedService.companyRegister(companyId,companyAudit.getPorxyPersonPhone());
        return Result.success();
    }

    @Override
    public Result personEsginAsyncRegister(Long companyId) {
        QueryWrapper<CompanyAudit> companyAuditQueryWrapper = new QueryWrapper<>();
        companyAuditQueryWrapper.eq("status",CompanyAuditEnum.Status.status1.getStatus());
        companyAuditQueryWrapper.eq("company_id",companyId);
        CompanyAudit companyAudit = getOne(companyAuditQueryWrapper);
        if (companyAudit == null){
            return Result.failure("此公司尚未审核通过或不存在");
        }
        QueryWrapper<ESignAccount> eSignAccountQueryWrapper = new QueryWrapper<>();
        eSignAccountQueryWrapper.eq("scf_account_type",1);
        eSignAccountQueryWrapper.eq("scf_account_id",companyAudit.getUserId());
        if(eSignAccountMapper.selectCount(eSignAccountQueryWrapper) > 0){
            return Result.failure("已注册E签宝个人账号，无需重复注册");
        }
        personEsignRegister(companyAudit.getCompanyId(),companyAudit);
        return Result.success();
    }

    @Override
    public Result companyEsignAsyncRegister(Long companyId) {
        QueryWrapper<CompanyAudit> companyAuditQueryWrapper = new QueryWrapper<>();
        companyAuditQueryWrapper.eq("company_id",companyId)
            .eq("status",CompanyAuditEnum.Status.status1.getStatus());
        CompanyAudit companyAudit = getOne(companyAuditQueryWrapper);
        if (companyAudit == null){
            return Result.failure("此公司尚未审核通过或不存在");
        }
        QueryWrapper<ESignAccount> eSignAccountQueryWrapper = new QueryWrapper<>();
        eSignAccountQueryWrapper.eq("scf_account_id",companyId);
        eSignAccountQueryWrapper.eq("scf_account_type",0);
        if(eSignAccountMapper.selectCount(eSignAccountQueryWrapper) > 0){
            return Result.failure("已注册E签宝企业账号，无需重复注册");
        }
        companyEsignRegister(companyId,companyAudit);
        return Result.success();
    }

    private void sendEsignEmail(String Title,String companyInfo,String reason){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("JdRegisterTemplate.html");
        emailVO.setTitle(Title);
        Map<String,Object> variables = new HashMap<>();
        variables.put("subject",Title);
        variables.put("companyInfo",companyInfo);
        variables.put("reason",reason);
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
    }
}
