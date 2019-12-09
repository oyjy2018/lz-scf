package com.zhjs.scfcloud.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.dto.CompanyAuditDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyAuditListDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.vo.CompanyAuditListVO;
import com.zhjs.scfcloud.system.service.*;
import com.zhjs.scfcloud.system.service.impl.CompanyAuditServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.System;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyAuditServiceTest {

    @Autowired
    CompanyAuditService companyAuditService;
    @Autowired
    CompanyAuditServiceImpl companyAuditServiceImpl;
    @Autowired
    SignService signService;
    @Autowired
    PlatformRoleFunctionConfigService platformRoleFunctionConfigService;
    @Autowired
    RoleFunctionService roleFunctionService;
    @Autowired
    CompanySystemVersionService companySystemVersionService;
    @Autowired
    RoleService roleService;

    @Test
    public void selectCompanyAuditList() {
        //分页查询
        FindCompanyAuditListDTO auditListDTO = new FindCompanyAuditListDTO();
        auditListDTO.setCurrent(1L);
        auditListDTO.setSize(10L);
        auditListDTO.setStatus(0);
       Page<CompanyAuditListVO> page = companyAuditService.selectCompanyAuditList(auditListDTO);
        System.out.println(page);
    }

    @Test
    public void eSginRegister() {
        CompanyAudit audit = companyAuditService.getById(58L);
        companyAuditService.eSginRegister(audit.getCompanyId(),audit);
    }

    @Test
    public void companyAudit(){
        CompanyAuditDTO dto = new CompanyAuditDTO();
        dto.setStatus(2);
        System.out.println(companyAuditService.companyAudit(49L,1L,dto));
    }

    @Test
    public void init() {
        Long companyId = 82L;
        //公司
        List<Long> versions = new ArrayList<>();
        Collections.addAll(versions,1L,2L,3L);
        //删除公司版本
        QueryWrapper<CompanySystemVersion> systemVersionQueryWrapper = new QueryWrapper<>();
        systemVersionQueryWrapper.eq("company_id",companyId);
        companySystemVersionService.remove(systemVersionQueryWrapper);
        //添加新版本
        versions.forEach(element -> {
            initCompanyVersion(companyId,element);

        });
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("company_id",companyId);
        List<Role> roles = roleService.list(roleQueryWrapper);
        roles.forEach(role -> {
            //角色权限
            initCompanyManagerPermission(role.getId(),role.getRoleName());
        });
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
     * @param roleId
     */
    private void initCompanyManagerPermission(Long roleId,String RoleName){
        //删除旧权限
        QueryWrapper<RoleFunction> roleFunctionQueryWrapper = new QueryWrapper<>();
        roleFunctionQueryWrapper.eq("role_id",roleId);
        roleFunctionService.remove(roleFunctionQueryWrapper);
        //添加新权限
        QueryWrapper<PlatformRoleFunctionConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name",RoleName);
        List<PlatformRoleFunctionConfig> functionConfigs = platformRoleFunctionConfigService.list(queryWrapper);
        if(functionConfigs.isEmpty()) return;
        List<String> functionList = functionConfigs.stream().map(platformRoleFunctionConfig -> platformRoleFunctionConfig.getFunctCode()).collect(Collectors.toList());
        List<RoleFunction> roleFunctions = new ArrayList<>();
        for (String code: functionList){
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setFunctCode(code).setRoleId(roleId);
            roleFunctions.add(roleFunction);
        }
        roleFunctionService.saveBatch(roleFunctions);
    }

    @Test
    public void email(){
        companyAuditServiceImpl.sendActiveEmail("chenweijie@zhihuikg.com",115L);
    }

    @Test
    public void sms(){
        companyAuditServiceImpl.sendActiveSms("陈伟杰测试公司101","chenweijie@zhihuikg.com","13246689983");
    }

}
