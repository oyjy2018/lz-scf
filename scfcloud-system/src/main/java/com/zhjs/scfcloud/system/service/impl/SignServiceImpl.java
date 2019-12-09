package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.esign.EsignOrganizeCreateDTO;
import com.zhjs.scfcloud.model.dto.esign.EsignPersonCreateDTO;
import com.zhjs.scfcloud.model.entity.ESignAccount;
import com.zhjs.scfcloud.model.mapper.ESignAccountMapper;
import com.zhjs.scfcloud.system.service.SignService;
import com.zhjs.scfcloud.util.util.HttpUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 电签相关
 */

@Service
public class SignServiceImpl implements SignService {

    private Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    @Value("${hulian-esign.url}")
    private String signUrl; //领筑电签服务地址
    @Value("${hulian-esign.appId}")
    private String signAppId; //领筑电签appId
    @Value("${hulian-esign.sign.create-person}")
    private String createPersonPath; //电签创建个人接口
    @Value("${hulian-esign.sign.create-organize}")
    private String createOrganizePath; //电签创建企业接口
    @Value("${hulian-esign.sign.add-person-seal}")
    private String addPersonSeal; //电签添加个人印章接口
    @Value("${hulian-esign.sign.add-organize-seal}")
    private String addOrganizeSeal; //电签添加企业印章接口


    @Resource
    private ESignAccountMapper eSignAccountMapper;

    /**
     * 创建企业账户
     */
    @Override
    @Transactional
    public Result createOrganize(EsignOrganizeCreateDTO dto) {
        if (dto == null) return Result.failure("参数对象缺失");
        if (StringUtil.isEmpty(dto.getCompanyId())) return Result.failure("公司id缺失");
        if (StringUtil.isEmpty(dto.getName()) || StringUtil.isEmpty(dto.getOrganCode())) {
            return Result.failure("必填参数缺失");
        }
        if (StringUtil.isEmpty(dto.getUserType())) dto.setUserType("0"); //注册类型为空时设为缺省注册
        switch (dto.getUserType()) {
            //缺省注册
            case "0": break;
            //代理人注册
            case "1": if (StringUtil.isEmpty(dto.getAgentName()) || StringUtil.isEmpty(dto.getAgentIdNo())) return Result.failure("代理人注册时缺少代理人信息");break;
            //法人注册
            case "2": if (StringUtil.isEmpty(dto.getLegalName()) || StringUtil.isEmpty(dto.getLegalIdNo())) return Result.failure("法人注册时缺少代理人信息");break;
            default: return Result.failure("不支持的注册类型");
        }
        //企业注册类型 默认为MERGE
        if (StringUtil.isEmpty(dto.getRegType())) dto.setRegType("MERGE");

        //调用领筑互联对接的e签宝企业注册
        String result = HttpUtil.post(signUrl+createOrganizePath+"?appId="+signAppId,JsonUtil.toJSON(dto));
        Result validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        Map data = (Map) validResult.getData();
        if (StringUtil.isEmpty(data.get("accountId"))) return Result.failure("未返回企业账户id");
        //e签宝账户id
        String accountId = (String) data.get("accountId");
        //创建企业印章
        Result addOrgSealResult = addOrganizeSeal(accountId);
        if (addOrgSealResult.getCode() == Result.RESULT_CODE_FAILURE) return addOrgSealResult;
        String sealData = (String) addOrgSealResult.getData();

        //入本系统库
        ESignAccount eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_type",0).eq("scf_account_id",dto.getCompanyId()));
        if (eSignAccount == null) {
            eSignAccount = new ESignAccount();
            eSignAccount.setScfAccountType(0).setScfAccountId(dto.getCompanyId()).setESignAccountId(accountId).setSealData(sealData).setCreateTime(new Date()).setUpdateTime(new Date());
            if (eSignAccountMapper.insertSelective(eSignAccount) != 1)
                return Result.failure("e签宝账户和印章信息保存到本地失败");
        }else {
            eSignAccount.setESignAccountId(accountId).setSealData(sealData).setUpdateTime(new Date());
            if (eSignAccountMapper.updateByPrimaryKeySelective(eSignAccount) != 1)
                return Result.failure("e签宝账户和印章信息更新到本地失败");
        }

        return Result.success();
    }

    /**
     * 创建企业印章
     * @param accountId e签宝账户id
     * @return
     */
    public Result addOrganizeSeal(String accountId) {
        if (StringUtil.isEmpty(accountId)) return Result.failure("e签宝账号id不能为空");
        String url = signUrl+addOrganizeSeal+"?accountId="+accountId+"&appId="+signAppId;
        //调用领筑互联对接的e签宝创建企业印章
        String result = HttpUtil.post(url,null);
        Result validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        Map data = (Map) validResult.getData();
        if (StringUtil.isEmpty(data.get("sealData"))) return Result.failure("未返回企业印章base64码");
        //电子印章图片base64数据
        String sealData = (String) data.get("sealData");
        Result res = new Result();
        res.setData(sealData);
        return res;
    }

    //创建个人账号
    @Override
    public Result createPerson(EsignPersonCreateDTO dto) {
        if (dto == null){
            return Result.failure("参数缺失");
        }
        String result = HttpUtil.post(signUrl+createPersonPath+"?appId="+signAppId,JsonUtil.toJSON(dto));
        Result validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        Map data = (Map) validResult.getData();
        if (StringUtil.isEmpty(data.get("accountId"))) return Result.failure("未返回个人账户id");
        String accountId = (String) data.get("accountId");

        //调用领筑互联对接的e签宝创建个人印章
        String url = signUrl+addPersonSeal+"?accountId="+accountId+"&appId="+signAppId;
        result = HttpUtil.post(url,null);
        validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        data = (Map) validResult.getData();
        if (StringUtil.isEmpty(data.get("sealData"))) return Result.failure("未返回个人印章base64码");
        //电子印章图片base64数据
        String sealData = (String) data.get("sealData");

        //入本系统库
        ESignAccount eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_type",1).eq("scf_account_id",dto.getUserId()));
        if (eSignAccount == null) {
            eSignAccount = new ESignAccount();
            eSignAccount.setScfAccountType(1).setScfAccountId(dto.getUserId()).setESignAccountId(accountId).setSealData(sealData).setCreateTime(new Date()).setUpdateTime(new Date());
            if (eSignAccountMapper.insertSelective(eSignAccount) != 1)
                return Result.failure("e签宝账户和印章信息保存到本地失败");
        }else {
            eSignAccount.setESignAccountId(accountId).setSealData(sealData).setUpdateTime(new Date());
            if (eSignAccountMapper.updateByPrimaryKeySelective(eSignAccount) != 1)
                return Result.failure("e签宝账户和印章信息更新到本地失败");
        }
        return Result.success();
    }


    //校验 调用领筑互联电签服务返回结果
    private Result validSignResult(String result) {
        if (result == null) {
            return Result.failure("请求失败");
        }
        Map resultMap = JsonUtil.jsonToMap(result);
        if (resultMap.get("retCode") == null || !resultMap.get("retCode").toString().equals("0000")) {
            return Result.failure(resultMap.get("rtnMsg").toString());
        }
        //没有data返回
        if (resultMap.get("data") == null){
            return Result.success();
        }
        //data是string类型
        if (resultMap.get("data") instanceof String) {
           resultMap.put("data",JsonUtil.jsonToMap(resultMap.get("data").toString()));
        }
        Map data = (Map) resultMap.get("data");
        if (data.get("errCode") != null && !data.get("errCode").toString().equals("0")){
            return Result.failure(data.get("msg").toString());
        }
        return Result.success(data);
    }

}
