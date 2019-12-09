package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyDetailDTO;
import com.zhjs.scfcloud.model.dto.EditStatusDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyListDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.CompanyTransfer;
import com.zhjs.scfcloud.model.vo.*;
import com.zhjs.scfcloud.system.service.CompanyCommonService;
import com.zhjs.scfcloud.system.service.CompanyService;
import com.zhjs.scfcloud.system.service.CompanySystemVersionService;
import com.zhjs.scfcloud.system.service.FileService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyAuditEnum;
import com.zhjs.scfcloud.util.enums.CompanyUserEnum;
import com.zhjs.scfcloud.util.util.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 公司管理业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 17:02
 *
 * @author liuchanghai
 * @create 2019-05-21 17:02
 * @since
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Autowired
    private CompanyCommonService companyCommonService;
    @Autowired
    private CompanyTransfer companyTransfer;
    @Autowired
    private CompanyUserMapper companyUserMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CompanyAuditMapper companyAuditMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private CompanySystemVersionService companySystemVersionService;
    @Resource
    private CompanyJdVerifiedMapper companyJdVerifiedMapper;



    /**
     * 分页获取公司管理列表数据
     * @param dto
     * @return
     */
    @Override
    public Page<CompanyListVO> selectCompanyListPage(FindCompanyListDTO dto) {
        Page<CompanyListVO> page = new Page<>(dto.getCurrent(),dto.getSize());

        List<CompanyListVO> companyList = companyMapper.selectCompanyListPage(page,dto.getCompanyName(), dto.getStatus());
        //设置额外属性
        for(CompanyListVO vo : companyList){
            //系统版本
            BeanUtils.copyProperties(companyCommonService.selectCompanySystemVersion(vo.getCompanyId()),vo);
            //成员数
            vo.setUserCount(companyCommonService.userCountByCompanyId(vo.getCompanyId(),CompanyUserEnum.status_1.getValue()));
            //京东注册
            vo.setIsJdRegister(companyCommonService.isJdRegister(vo.getCompanyId()));
            //京东实名
            vo.setJdVerified(companyCommonService.isJdVerified(vo.getCompanyId()));
            //收票收款账户
            BeanUtils.copyProperties(companyCommonService.isDefaultBankAccount(vo.getCompanyId()),vo);
            //E签宝注册
            vo.setEsignVerified(companyCommonService.esignVerified(vo.getCompanyId()));
        }
        return (Page<CompanyListVO>) page.setRecords(companyList);
    }

    @Override
    public Result selectCompanyDetail(Long companyId) {
        Company company = getById(companyId);
        if (company == null) return Result.failure("公司不存在或未审核通过.");

        CompanyDetailDTO result = new CompanyDetailDTO();
        //公司信息
        BeanUtils.copyProperties(company,result);
        //被授权人账户信息
        User user = userMapper.selectById(company.getRegUserId());
        if(user != null){
            result.setPorxyPersonEmail(user.getEmail());
            result.setPorxyPersonPhone(user.getPhone());
        }

        QueryWrapper<CompanyAudit> auditQueryWrapper = new QueryWrapper<>();
        auditQueryWrapper.eq("company_id",companyId);
        auditQueryWrapper.eq("status", CompanyAuditEnum.Status.status1.getStatus());
        CompanyAudit source = companyAuditMapper.selectOne(auditQueryWrapper);
        //审核信息
        if(source != null){
            result.setSystemIdList(source.getSystemIdList());
            result.setFlowList(source.getFlowList());

            if(result.getSystemIdList().contains("1")){
                QueryWrapper<CompanySystemVersion> riskQueryWrapper = new QueryWrapper<>();
                riskQueryWrapper.eq("company_id", company.getId());
                riskQueryWrapper.eq("system_version_id",1);
                result.setRiskControlSystemVersionName(companySystemVersionService.count(riskQueryWrapper) > 0? "标准版" : "不绑定");
            }
            if(result.getSystemIdList().contains("2")){
                QueryWrapper<CompanySystemVersion> billQueryWrapper = new QueryWrapper<>();
                billQueryWrapper.eq("company_id", company.getId());
                billQueryWrapper.eq("system_version_id",2);
                result.setTicketSystemVersionName(companySystemVersionService.count(billQueryWrapper) > 0? "标准版" : "不绑定");
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

        return Result.success(result);
    }

    /**
     * 公司管理列表数据 不分页
     * @param dto
     * @return
     */
    @Override
    public List<CompanyListVO> findList(FindCompanyListDTO dto) {
        List<CompanyListVO> result =  companyMapper.findCompanyList();
        return result;
    }

    /**
     * 编辑公司状态
     * @param dto 入参
     * @return
     */
    @Override
    public boolean editComapnyStatusById(EditStatusDTO dto) {
        Company company = getById(dto.getId());
        company.setStatus(dto.getStatus());
        return updateById(company);
    }

    /**
     * 根据公司ID查询公司的所有成员
     * @param companyId 入参
     * @return
     */
    @Override
    public List<UserVO> findCompanyUserListById(Long companyId) {
        return companyUserMapper.findCompanyUserListById(companyId);
    }

    /**
     * 根据用户ID查询用户拥有数据权限
     * @param userId 入参 用户ID
     * @return
     */
    @Override
    public List<CompanySelectVO> findUserDataPermissionList(Long userId) {
        List<Company> companies = companyCommonService.selectCompanyListByUserId(userId);
        return companyTransfer.companyList2CompanySelectVO(companies);
    }

    /**
     * 删除公司(逻辑删除)
     * @param id 主键ID
     * @return
     */
    @Override
    public boolean deleteById(Long id) {
        Company company = getById(id);
        company.setIsDelete(CommonEnum.WhetherEnum.YES.getStatus());
        company.setUpdateTime(LocalDateTime.now());
        return updateById(company);
    }

    /**
     * 公司是否存在
     * @param comapnyName 公司名称
     * @return
     */
    @Override
    public boolean isExist(String comapnyName) {
        LambdaQueryWrapper<Company> lambda = new QueryWrapper<Company>().lambda();
        lambda.eq(Company::getCompanyName, comapnyName);
        List<Company> list = list(lambda);
        if(list.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public CompanyBasicInfoVO findCompanyBasicInformation(Long companyId) {
        CompanyBasicInfoVO basicInfoVO = new CompanyBasicInfoVO();
        basicInfoVO.setCompanyId(companyId);
        //公司成员数
        basicInfoVO.setMemberCount(companyCommonService.userCountByCompanyId(companyId,null));
        //在职人数
        basicInfoVO.setMemberWorkCount(companyCommonService.userCountByCompanyId(companyId, CompanyUserEnum.status_1.getValue()));
        //jd企业认证
        basicInfoVO.setJdVerified(companyCommonService.isJdVerified(companyId));
        //e签宝认证
        basicInfoVO.setEsignVerified(companyCommonService.esignVerified(companyId));
        //收款账户 收票账户
        BeanUtils.copyProperties(companyCommonService.isDefaultBankAccount(companyId),basicInfoVO);
        //系统版本信息
        List<String> versions = new ArrayList<>(2);
        CompanySystemVersionNameVO source = companyCommonService.selectCompanySystemVersion(companyId);
        if(!StringUtils.isEmpty(source.getRiskSystemVersionName())){
            versions.add(source.getRiskSystemName()+"-"+source.getRiskSystemVersionName());
        }
        if(!StringUtils.isEmpty(source.getTicketSystemVersionName())){
            versions.add(source.getTicketSystemName()+"-"+source.getTicketSystemVersionName());
        }
        basicInfoVO.setSystemVersionList(versions);
        return basicInfoVO;
    }

    /**
     * 是否京东实名认证
     * @param companyId
     * @return
     */
    @Override
    public Result isJdVerified(Long companyId) {
        LambdaQueryWrapper<CompanyJdVerified> wrapper = new QueryWrapper<CompanyJdVerified>().lambda();
        wrapper.eq(CompanyJdVerified::getCompanyId,companyId);
        CompanyJdVerified companyJdVerified = companyJdVerifiedMapper.selectOne(wrapper);
        Boolean isJdVerified = false;
        if (companyJdVerified == null) return Result.successData(isJdVerified);
        if (companyJdVerified.getMerRealStatus().intValue() == 4) {
            isJdVerified = true;
        }
        return Result.successData(isJdVerified);
    }

}
