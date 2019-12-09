package com.zhjs.scfcloud.ticket.service;


import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.esign.*;
import com.zhjs.scfcloud.model.vo.UserInfoVO;

/**
 * 电签相关
 */

public interface SignService  {

    //创建个人用户
    Result createPerson(EsignPersonCreateDTO esignPersonCreateDTO);

    //创建企业用户
    Result createOrganize(EsignOrganizeCreateDTO esignOrganizeCreateDTO);

    //添加企业印章
    Result addOrganizeSeal(String accountId);

    //电签前发短信验证码
    Result sendSmsCode(EsignSendCodeDTO esignSendCodeDTO, UserInfoVO user);

    //根据合同模板文件生成合同，并上传到领筑互联oss
    Result createContract(EsignContractContentDTO esignContractContentDTO);

    //合同电签(加验证码)
    Result signContract(EsignContractSignDTO esignContractSignDTO, UserInfoVO user);

    //获取合同内容
    Result getContractContent(Long orderId);

    //更换合同
    Result changeContract(EsignContractChangeDTO dto);

    //校验关键字是否唯一
    Result isOnlyKeyWord(EsignKeyWordOnlyValidDTO dto);

    //获取初始合同地址
    Result getInitContractViewUrl(Long orderId);
}
