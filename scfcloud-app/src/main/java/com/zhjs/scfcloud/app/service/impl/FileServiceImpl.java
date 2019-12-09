package com.zhjs.scfcloud.app.service.impl;

import com.zhjs.scfcloud.app.feign.FileFeignService;
import com.zhjs.scfcloud.app.feign.OperateLogFeignService;
import com.zhjs.scfcloud.app.service.FileService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.dto.file.AddFileDTO;
import com.zhjs.scfcloud.model.entity.BusinessFlow;
import com.zhjs.scfcloud.model.entity.File;
import com.zhjs.scfcloud.model.entity.OperateLog;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.enums.OperatObjectEnum;
import com.zhjs.scfcloud.model.enums.OperatTypeEnum;
import com.zhjs.scfcloud.model.transfer.FileTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/25 17:57
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileFeignService fileFeignService;
    @Autowired
    FileTransfer fileTransfer;
    @Autowired
    OperateLogFeignService operateLogFeignService;

    @Override
    public Result addFile(AddFileDTO dto) {
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息");
        dto.setCompanyId(MySubjectUtil.getCompanyId());
        dto.getFile().setCompanyId(dto.getCompanyId());

        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        BusinessFlow beforeFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getBeforeFlow(), businessFlowList);
        BusinessFlow afterFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getAfterFlow(), businessFlowList);

        //获取审批流程相关配置
        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(),dto.getBusinessTypeId());
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()) return Result.failure("流程【"+beforeFlow.getFlowName()+"】获取后续流程失败");

        BusinessWorkFlowExtendVO workFlowExtendVO = BusinessCfgUtil.getBusinessWorkFlowExtend(dto.getBeforeFlow(),dto.getAfterFlow(),workFlowCfgList);
        if(workFlowExtendVO == null) return Result.failure("流程【"+beforeFlow.getFlowName()+"】未配置指向【"+afterFlow.getFlowName()+"】流程");

        //查询历史附件进行附件校验
        Result<List<File>> result =
                fileFeignService.findSelective(
                        new FileDTO().setCompanyId(dto.getCompanyId())
                                    .setBusinessTypeId(dto.getBusinessTypeId())
                                    .setBusinessId(dto.getBusinessId())
                                    .setFileCode(dto.getFileCode())
                                    .setStatus(CommonEnum.StatusEnnum.status1.getStatus())
                );
        if(result.getCode() != Result.RESULT_CODE_SUCCESS){
            return Result.failure("查询历史文件失败");
        }

        List<File> fileList = null;
        if(result.getData() == null){
            fileList = new ArrayList<>();
        }else{
            fileList = result.getData();
        }
        fileList.add(fileTransfer.toFile(dto.getFile()));
        BusinessWorkFlowFileVO workFlowFileCfg = workFlowExtendVO.getWorkFlowFileVOList()
                .stream().filter(workFlowFile -> workFlowFile.getFileCode().equals(dto.getFileCode())).findAny().orElse(null);
        //校验附件
        Result valid = BusinessCfgUtil.validFile(fileList,workFlowFileCfg);
        if(valid.getCode() != Result.RESULT_CODE_SUCCESS){
            return valid;
        }

        //保存文件
        result = fileFeignService.addFile(dto.getFile());
        if(result.getCode() != Result.RESULT_CODE_SUCCESS){
            return result;
        }

        //添加操作日志
        operateLogFeignService.add(
                new OperateLog().setCompanyId(dto.getCompanyId())
                .setBusinessTypeId(dto.getBusinessTypeId())
                .setBusinessId(dto.getBusinessId())
                .setOperateContent("添加了附件【"+workFlowFileCfg.getFileName()+"】")
                .setOperateTime(new Date())
                .setOperateUserId(user.getId())
                .setOperateUserName(user.getUserName())
                .setOperatObject(OperatObjectEnum.credit.getValue())
                .setOperatType(OperatTypeEnum.add.getValue()));
        return Result.success(result.getData());
    }
}
