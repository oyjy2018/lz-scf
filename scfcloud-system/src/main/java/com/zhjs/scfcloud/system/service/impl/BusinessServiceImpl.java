package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;
import com.zhjs.scfcloud.system.service.BusinessService;
import com.zhjs.scfcloud.system.service.InitFlowRedisService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 16:00
 *
 * @author liuchanghai
 * @create 2019-06-11 16:00
 * @since
 */

@Service
public class BusinessServiceImpl implements BusinessService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private BusinessFlowMapper flowMapper;
    @Resource
    private BusinessWorkFlowMapper workFlowMapper;
    @Resource
    private BusinessFlowPermitMapper flowPermitMapper;
    @Resource
    private BusinessWorkFlowAttrMapper workFlowAttrMapper;
    @Autowired
    InitFlowRedisService initFlowRedisService;
    @Resource
    private BusinessWorkFlowFileMapper workFlowFileMapper;
    /**
     * 事务管理器
     */
    @Autowired
    private PlatformTransactionManager txManager;

    //初始公司流程权限-标准化配置
    @Override
    public Result initCompanyFlowPermit(Long companyId) {
        //查询公司所有用户组
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new QueryWrapper<Role>().lambda();
        roleLambdaQueryWrapper.eq(Role::getCompanyId, companyId);
        List<Role> roleList = roleMapper.selectList(roleLambdaQueryWrapper);
        if (ListUtil.isEmpty(roleList)) return Result.failure("公司无用户组数据");

        //查询公司所有流程状态
        LambdaQueryWrapper<BusinessFlow> flowLambdaQueryWrapper = new QueryWrapper<BusinessFlow>().lambda();
        flowLambdaQueryWrapper.eq(BusinessFlow::getCompanyId, companyId);
        List<BusinessFlow> flowList = flowMapper.selectList(flowLambdaQueryWrapper);
        if (ListUtil.isEmpty(flowList)) return Result.failure("公司无业务流程状态数据");

        //查询公司所有流转配置
        LambdaQueryWrapper<BusinessWorkFlow> workFlowLambdaQueryWrapper = new QueryWrapper<BusinessWorkFlow>().lambda();
        workFlowLambdaQueryWrapper.eq(BusinessWorkFlow::getCompanyId, companyId);
        List<BusinessWorkFlow> workFlowList = workFlowMapper.selectList(workFlowLambdaQueryWrapper);
        if (ListUtil.isEmpty(flowList)) return Result.failure("公司无业务流转配置数据");

        //获取默认权限配置
        List<BusinessFlowPermitInitDTO> flowPermitList = defaultFlowPermit();

        //设置配置用户组id
        setRoleIds(flowPermitList,roleList);
        //设置配置流程码
        setFlowCode(flowPermitList,flowList);
        //设置流转id
        setWorkFlowId(flowPermitList,workFlowList);

        //过滤没有流转id的
        flowPermitList = flowPermitList.stream().filter(flowPermit-> !StringUtil.isEmpty(flowPermit.getWorkFlowId())).collect(Collectors.toList());
        if (ListUtil.isEmpty(flowPermitList)) return Result.failure("无可配置处理权限的流转");

        //批量更新处理用户组
        int effect = flowPermitMapper.updateDisposeRoleIdsBatch(flowPermitList);

        return Result.success();
    }

    @Override
    public Result editWorkFlow(EditWorkFlowDTO dto) {
        List<BusinessWorkFlow> workFlowList = BusinessCfgUtil.getBusinessWorkFlowList(dto.getCompanyId(),dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(workFlowList == null || workFlowList.isEmpty()){
            return Result.failure("获取当前流程流转失败");
        }

        // 开启事务管理
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        List<AddWorkFlowDTO> addWorkFlowList = dto.getAddWorkFlowList();
        List<BusinessWorkFlow> delWorkFlowList = new ArrayList<>();
        List<BusinessWorkFlow> newWorkFlowList = new ArrayList<>();
        try{

            //过滤没有变更的流程扭转配置,当前流程扭转配置无法匹配，而本次提交配置内存在的，则是新增配置
            workFlowList.forEach(workFlow -> {
                addWorkFlowList.stream().filter(awf -> workFlow.getBeforeFlow().equals(awf.getFlowCode())).forEach(awf -> {

                    String flowExtend =
                            awf.getFlowExtendList().stream()
                                    .filter(fe -> workFlow.getBeforeFlow().equals(awf.getFlowCode()) && workFlow.getAfterFlow().equals(fe))
                                    .findAny().orElse(null);
                    //在缓存中存在的流程扭转配置，如果在本次提交的流程流转配置中不存在，则证明此流程扭转配置已被取消
                    if(StringUtil.isEmpty(flowExtend)){
                        delWorkFlowList.add(workFlow);
                    }else{ //反之如果存在，则证明此流程扭转配置没有变更，将其剔除新增配置队列
                        awf.getFlowExtendList().remove(flowExtend);
                    }

                });
            });

            // 删除已被取消的配置
            if(delWorkFlowList.size() > 0){
                List<Long> workFlowIds =  delWorkFlowList.stream().map(dwf -> dwf.getId()).collect(Collectors.toList());

                // 删除流程扭转配置关联的流程扭转字段配置
                LambdaQueryWrapper<BusinessWorkFlowAttr> wrapper = new QueryWrapper<BusinessWorkFlowAttr>().lambda();
                wrapper.in(BusinessWorkFlowAttr::getWorkFlowId, workFlowIds);
                int count = workFlowAttrMapper.delete(wrapper);

                // 删除流程扭转配置关联的流程扭转附件配置配置
                LambdaQueryWrapper<BusinessWorkFlowFile> wrapperFile = new QueryWrapper<BusinessWorkFlowFile>().lambda();
                wrapperFile.in(BusinessWorkFlowFile::getWorkFlowId, workFlowIds);
                count = workFlowFileMapper.delete(wrapperFile);

                // 删除流程扭转配置
                count = workFlowMapper.deleteBatchIds(workFlowIds);
            }

            // 新增流程扭转配置
            Date sysdate = new Date();
            addWorkFlowList.forEach(awf -> {
                awf.getFlowExtendList().forEach(flowExtend -> {
                    BusinessWorkFlow newWorkFlow = new BusinessWorkFlow();
                    newWorkFlow.setCompanyId(dto.getCompanyId());
                    newWorkFlow.setBusinessTypeId(dto.getBusinessTypeId());
                    newWorkFlow.setBeforeFlow(awf.getFlowCode());
                    newWorkFlow.setAfterFlow(flowExtend);
                    newWorkFlow.setCreateBy(dto.getUserId());
                    newWorkFlow.setCreateTime(sysdate);
                    newWorkFlow.setHasEdit(CommonEnum.WhetherEnum.YES.getStatus());
                    newWorkFlow.setStatus(CommonEnum.StatusEnnum.status1.getStatus().byteValue());
                    newWorkFlowList.add(newWorkFlow);
                });
            });

            if(newWorkFlowList.size() > 0){
                workFlowMapper.batchInsert(newWorkFlowList);
            }
            // 提交事务
            txManager.commit(status);
        }catch (Exception e){
            txManager.rollback(status);
            return Result.failure("保存流程扭转配置异常："+e.getMessage());
        }


        //如果存在新增或取消流程扭转配置，则更新缓存
        if(newWorkFlowList.size() > 0 || delWorkFlowList.size() > 0){
            initFlowRedisService.initFlowRedisNoAsync();
        }

        return Result.success();
    }

    @Override
    public Result savePower(SavePowerDTO dto) {
        CommonEnum.StatusEnnum status1 = CommonEnum.StatusEnnum.status1;
        Date sysdate = new Date();

        BusinessWorkFlow workFlow = BusinessCfgUtil.getBusinessWorkFlow(dto.getCompanyId(), dto.getBusinessTypeId(), status1.getStatus(), dto.getBeforeFlow(), dto.getAfterFlow());

        // 获取流程已存在的字段配置
        List<BusinessWorkFlowAttr> oldWorkFlowAttr =
                BusinessCfgUtil.getBusinessWorkFlowAttrList(dto.getCompanyId(), dto.getBusinessTypeId(), workFlow.getId(), status1.getStatus());
        if(oldWorkFlowAttr == null){
            oldWorkFlowAttr = new ArrayList<>();
        }

        List<AddWorkFlowAttrDTO> newWorkFlowAttr = dto.getWorkFlowAttrList();
        List<Long> delOldAttrIdList = new ArrayList<>();
        List<AddWorkFlowAttrDTO> noChangeAttr = new ArrayList<>();
        List<AddWorkFlowAttrDTO> updateAttr = new ArrayList<>();
        // 区分出为变更的字段配置，和被取消（删除）的字段配置
        oldWorkFlowAttr.forEach( old -> {
            AddWorkFlowAttrDTO newAttr =
                    newWorkFlowAttr.stream()
                            .filter(attr -> attr.getBusinessAttrId().longValue() == old.getBusinessAttrId().longValue()).findAny().orElse(null);
            if(newAttr == null){
                delOldAttrIdList.add(old.getId());
            }else{// 无论是修改还是不修改，都要从新增集合里剔除
                noChangeAttr.add(newAttr);
                if(!old.settingEqual(newAttr)){
                    newAttr.setWorkFlowAttrId(old.getId());
                    updateAttr.add(newAttr);
                }
            }
        });

        // 剔除不需要变更的字段配置
        newWorkFlowAttr.removeAll(noChangeAttr);

        // 获取流程已存在的附件配置
        List<BusinessWorkFlowFileVO> oldWorkFlowFile =
                BusinessCfgUtil.getBusinessWorkFlowFileVOList(dto.getCompanyId(), dto.getBusinessTypeId(), workFlow.getId(), status1.getStatus());
        if(oldWorkFlowFile == null){
            oldWorkFlowFile = new ArrayList<>();
        }

        List<AddWorkFlowFileDTO> newWorkFlowFile = dto.getWorkFlowFileList();
        List<Long> delOldFileIdList = new ArrayList<>();
        List<AddWorkFlowFileDTO> noChangeFile = new ArrayList<>();
        List<AddWorkFlowFileDTO> updateFile = new ArrayList<>();
        // 区分出为变更的附件配置，和被取消（删除）的附件配置
        oldWorkFlowFile.forEach( old -> {
            AddWorkFlowFileDTO newFile =
                    newWorkFlowFile.stream()
                            .filter( file -> file.getBusinessFileId().longValue() == old.getBusinessFileId()).findAny().orElse(null);
            if(newFile == null){
                delOldFileIdList.add(old.getWorkFlowFileId());
            }else{// 无论是修改还是不修改，都要从新增集合里剔除
                noChangeFile.add(newFile);
                if(!old.settingEqual(newFile)){
                    newFile.setWorkFlowFileId(old.getWorkFlowFileId());
                    updateFile.add(newFile);
                }
            }
        });
        // 剔除不需要变更的附件配置
        newWorkFlowFile.removeAll(noChangeFile);

        // 获取流程已存在的权限配置
        List<BusinessFlowPermit> oldFlowPermitList =
                BusinessCfgUtil.getBusinessFlowPermitList(dto.getCompanyId(), dto.getBusinessTypeId(), workFlow.getId(), status1.getStatus());

        BusinessFlowPermit flowPermit = null;
        // 不存在就新增
        if(oldFlowPermitList == null || oldFlowPermitList.isEmpty()){
            flowPermit = new BusinessFlowPermit();
            flowPermit.setCompanyId(dto.getCompanyId());
            flowPermit.setBusinessTypeId(dto.getBusinessTypeId());
            flowPermit.setWorkFlowId(workFlow.getId());
            flowPermit.setStatus(status1.getStatus().byteValue());
            flowPermit.setCreateTime(sysdate);
            flowPermit.setCreateBy(dto.getUserId());
            flowPermit.setUpdateTime(sysdate);
            flowPermit.setUpdateBy(dto.getUserId());
        }else{
            // 一个流程只有一个权限配置
            flowPermit = oldFlowPermitList.get(0);
        }
        flowPermit.setRoleIds(String.join(",",dto.getRoleIds()));
        flowPermit.setUserIds(String.join(",",dto.getUserIds()));

        // 开启事务管理
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        try{
            // 删除被取消的字段配置
            if(delOldAttrIdList.size() > 0){
                int count = workFlowAttrMapper.deleteBatchIds(delOldAttrIdList);
            }
            // 删除被取消的附件配置
            if(delOldFileIdList.size() > 0){
                int count = workFlowFileMapper.deleteBatchIds(delOldFileIdList);
            }

            // 更新字段配置
            if(updateAttr.size() > 0){
                int count = workFlowAttrMapper.batchUpdateAttr(updateAttr);
            }

            // 新增字段配置
            if(newWorkFlowAttr.size() > 0){
                List<BusinessWorkFlowAttr> newAttrCfgList = new ArrayList<>();
                BusinessWorkFlowAttr newAttrCfg = null;
                for(AddWorkFlowAttrDTO newAttr: newWorkFlowAttr){
                    newAttrCfg = new BusinessWorkFlowAttr();
                    newAttrCfg.setCompanyId(dto.getCompanyId());
                    newAttrCfg.setBusinessTypeId(dto.getBusinessTypeId());
                    newAttrCfg.setBusinessAttrId(newAttr.getBusinessAttrId());
                    newAttrCfg.setWorkFlowId(workFlow.getId());
                    newAttrCfg.setRequired(newAttr.getRequired());
                    newAttrCfg.setDefaultValueType(newAttr.getDefaultValueType());
                    newAttrCfg.setDefaultValue(newAttr.getDefaultValue());
                    newAttrCfg.setSort(newAttr.getSort());
                    newAttrCfg.setStatus(status1.getStatus().byteValue());
                    newAttrCfg.setCreateTime(sysdate);
                    newAttrCfg.setCreateBy(dto.getUserId());
                    newAttrCfg.setUpdateTime(sysdate);
                    newAttrCfg.setUpdateBy(dto.getUserId());
                    newAttrCfgList.add(newAttrCfg);
                }
                workFlowAttrMapper.batchInsert(newAttrCfgList);
            }

            // 更新附件配置
            if(updateFile.size() > 0){
                workFlowFileMapper.batchUpdateFile(updateFile);
            }

            // 新增附件配置
            if(newWorkFlowFile.size() > 0){
                List<BusinessWorkFlowFile> newFileCfgList = new ArrayList<>();
                BusinessWorkFlowFile newFileCfg = null;
                for(AddWorkFlowFileDTO newFile:newWorkFlowFile){
                    newFileCfg = new BusinessWorkFlowFile();
                    newFileCfg.setCompanyId(dto.getCompanyId());
                    newFileCfg.setBusinessTypeId(dto.getBusinessTypeId());
                    newFileCfg.setBusinessFileId(newFile.getBusinessFileId());
                    newFileCfg.setWorkFlowId(workFlow.getId());
                    newFileCfg.setRequired(newFile.getRequired());
                    newFileCfg.setSort(newFile.getSort());
                    newFileCfg.setStatus(status1.getStatus());
                    newFileCfg.setCreateTime(sysdate);
                    newFileCfg.setCreateBy(dto.getUserId());
                    newFileCfg.setUpdateTime(sysdate);
                    newFileCfg.setUpdateBy(dto.getUserId());
                    newFileCfgList.add(newFileCfg);
                }
                workFlowFileMapper.batchInsert(newFileCfgList);
            }

            // 保存权限信息（PS: 没有主键则为新增)
            if(flowPermit.getId() == null || flowPermit.getId() == 0){
                flowPermitMapper.insert(flowPermit);
            }else{
                flowPermitMapper.updateById(flowPermit);
            }
            // 提交事务
            txManager.commit(status);
        } catch (Exception e){
            // 回滚事务
            txManager.rollback(status);
            return Result.failure("保存流程扭转权限字段配置异常："+e.getMessage());
        }


        // 更新缓存
        initFlowRedisService.initFlowRedisNoAsync();

        return Result.success();
    }

    //设置流转id
    private void setWorkFlowId(List<BusinessFlowPermitInitDTO> flowPermitList, List<BusinessWorkFlow> workFlowList) {
        flowPermitList.stream().forEach(permitInitDTO -> {
            for (BusinessWorkFlow workFlow:workFlowList) {
                if (workFlow.getBeforeFlow().equals(permitInitDTO.getFlowCode()) && workFlow.getAfterFlow().equals(permitInitDTO.getAfterFlowCode())) {
                    permitInitDTO.setWorkFlowId(workFlow.getId().toString());
                    break;
                }
            }
        });
    }

    //设置配置流程码
    private void setFlowCode(List<BusinessFlowPermitInitDTO> flowPermitList, List<BusinessFlow> flowList) {
        Map nameCodeFlowMap = new HashMap();//名称为键 码为值
        flowList.stream().forEach(flow -> {
            nameCodeFlowMap.put(flow.getFlowName(),flow.getFlowCode());
        });

        flowPermitList.stream().forEach(permitInitDTO -> {
            permitInitDTO.setFlowCode(StringUtil.isEmpty(nameCodeFlowMap.get(permitInitDTO.getFlowName()))?null:nameCodeFlowMap.get(permitInitDTO.getFlowName()).toString());
            permitInitDTO.setAfterFlowCode(StringUtil.isEmpty(nameCodeFlowMap.get(permitInitDTO.getAfterFlowName()))?null:nameCodeFlowMap.get(permitInitDTO.getAfterFlowName()).toString());
        });
    }

    //设置配置用户组id
    private void setRoleIds(List<BusinessFlowPermitInitDTO> flowPermitList, List<Role> roleList) {
        Map nameIdRoleMap = new HashMap();//名称为键 id为值
        roleList.stream().forEach(role -> {
            nameIdRoleMap.put(role.getRoleName().trim(),role.getId());
        });
        flowPermitList.stream().forEach(permitInitDTO -> {
           String roleNames = permitInitDTO.getRoleNames();
           String[] roleNameArray = roleNames.split("、");
           String roleIds = Arrays.stream(roleNameArray).filter(name -> !StringUtil.isEmpty(nameIdRoleMap.get(name))).map(name->nameIdRoleMap.get(name).toString()).collect(Collectors.joining(","));
           permitInitDTO.setRoleIds(StringUtil.isEmpty(roleIds)?null:roleIds);
        });
    }

    //默认流转权限配置
    private List<BusinessFlowPermitInitDTO> defaultFlowPermit(){
        List<BusinessFlowPermitInitDTO> flowPermitList = new ArrayList<>();
        BusinessFlowPermitInitDTO dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("商务经理组、客户经理组");dto.setFlowName("草稿");dto.setAfterFlowName("客户经理初审");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("客户经理初审");dto.setAfterFlowName("商务经理资信评估");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("客户经理初审");dto.setAfterFlowName("草稿");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("客户经理初审");dto.setAfterFlowName("已拒绝");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("大区经理组、大区运营专员组、客户经理组、风控组、风控总经理组");dto.setFlowName("商务经理资信评估");dto.setAfterFlowName("项目风险评估");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("大区经理组、大区运营专员组、客户经理组、风控组、风控总经理组");dto.setFlowName("商务经理资信评估");dto.setAfterFlowName("客户经理初审");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("工程主管组、客户经理组、风控组、风控总经理组");dto.setFlowName("项目风险评估");dto.setAfterFlowName("确定初步方案");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("工程主管组、客户经理组、风控组、风控总经理组");dto.setFlowName("项目风险评估");dto.setAfterFlowName("商务经理资信评估");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("确定初步方案");dto.setAfterFlowName("准备资料");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("确定初步方案");dto.setAfterFlowName("项目风险评估");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("确定初步方案");dto.setAfterFlowName("已拒绝");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("商务经理组、客户经理组");dto.setFlowName("准备资料");dto.setAfterFlowName("客户经理审核");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("商务经理组、客户经理组");dto.setFlowName("准备资料");dto.setAfterFlowName("确定初步方案");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("客户经理审核");dto.setAfterFlowName("风控经理审核");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("客户经理审核");dto.setAfterFlowName("准备资料");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组");dto.setFlowName("客户经理审核");dto.setAfterFlowName("已拒绝");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("风控组");dto.setFlowName("风控经理审核");dto.setAfterFlowName("财务经理审核");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("风控组");dto.setFlowName("风控经理审核");dto.setAfterFlowName("客户经理审核");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("风控组");dto.setFlowName("风控经理审核");dto.setAfterFlowName("待补充资料");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("风控组");dto.setFlowName("风控经理审核");dto.setAfterFlowName("已拒绝");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("商务经理组、客户经理组");dto.setFlowName("待补充资料");dto.setAfterFlowName("风控经理审核");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("财务组");dto.setFlowName("财务经理审核");dto.setAfterFlowName("风控总经理审批");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("财务组");dto.setFlowName("财务经理审核");dto.setAfterFlowName("风控经理审核");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("风控总经理组");dto.setFlowName("风控总经理审批");dto.setAfterFlowName("授信完成");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("风控总经理组");dto.setFlowName("风控总经理审批");dto.setAfterFlowName("财务经理审核");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("风控总经理组");dto.setFlowName("风控总经理审批");dto.setAfterFlowName("已拒绝");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("商务经理组、客户经理组");dto.setFlowName("草稿");dto.setAfterFlowName("待审批");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组、风控组、风控总经理组");dto.setFlowName("待审批");dto.setAfterFlowName("审批通过");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组、风控组、风控总经理组");dto.setFlowName("待审批");dto.setAfterFlowName("草稿");
        flowPermitList.add(dto);dto = new BusinessFlowPermitInitDTO();
        dto.setRoleNames("客户经理组、风控组、风控总经理组");dto.setFlowName("待审批");dto.setAfterFlowName("审批拒绝");
        flowPermitList.add(dto);
        return flowPermitList;
    }
}
