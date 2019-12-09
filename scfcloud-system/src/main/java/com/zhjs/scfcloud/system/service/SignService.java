package com.zhjs.scfcloud.system.service;


import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.esign.*;

/**
 * 电签相关
 */

public interface SignService {

    //创建个人用户并创建印章
    Result createPerson(EsignPersonCreateDTO esignPersonCreateDTO);

    //创建企业用户并创建印章
    Result createOrganize(EsignOrganizeCreateDTO esignOrganizeCreateDTO);
}
