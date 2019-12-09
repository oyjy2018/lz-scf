package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.PhoneVaildCodeDTO;
import com.zhjs.scfcloud.model.entity.VCodeValid;
import com.zhjs.scfcloud.model.mapper.VCodeValidMapper;
import com.zhjs.scfcloud.system.service.SmsCodeService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-31 18:30
 *
 * @author liuchanghai
 * @create 2019-05-31 18:30
 * @since
 */
@Service
public class SmsCodeServiceImpl extends ServiceImpl<VCodeValidMapper, VCodeValid> implements SmsCodeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private EmailTool emailTool;
    @Value("${smsSendSwitch}")
    private Integer smsSendSwitch;
    /**
     * 发送验证码
     * @param dto
     * @return
     */
    @Override
    public Result sendSms(PhoneVaildCodeDTO dto) {
        String code = RandomNumUtil.createRandomNum(6);
        String token = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + dto.getPhone() + code).getBytes());
        //发送手机验证码
        boolean sendSms = false;
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        CommonEnum.SmsType smsType = CommonEnum.SmsType.getEnumByStatus(dto.getSmsType());
        if(smsType == null){
            return Result.failure("发送短信失败：无效的短信类型！");
        }
        String res = smsUtil.lzySmsBsend(dto.getPhone(), smsType.getLzySmsType(), params);
        if(StringUtil.isEmpty(res)){
            return Result.failure("发送短信失败：访问短信服务失败！");
        }

        if(res.indexOf("{") == -1){
            return Result.failure("发送短信失败："+res);
        }

        Map<String, Object> resMap = JsonUtil.jsonToMap(res);
        if(!"200".equals(resMap.get("code").toString())){
            return Result.failure("发送短信失败："+resMap.get("message").toString());
        }

        Map<String, Object> responseBody = (Map<String, Object>)resMap.get("responseBody");
        if(!"1".equals(responseBody.get("code"))){
            return Result.failure("发送短信失败："+responseBody.get("message").toString());
        }

        logger.info("短信发送成功，response：{}", res);
        //保存数据
        VCodeValid vCodeValid = new VCodeValid();
        vCodeValid.setPhone(dto.getPhone())
                .setVcode(code)
                .setToken(token)
                .setCreateTime(LocalDateTime.now())
                .setEffectiveTime(System.currentTimeMillis()+5*60*1000);
        save(vCodeValid);
        Map<String, Object> jMap = new HashMap<>();
        jMap.put("token",token);
        return Result.success(jMap);
    }

    @Override
    public boolean checkVCode(CheckPhoneVCodeDTO dto) {
        //发送短信开关控制
        if(0 == smsSendSwitch) return true;

        LambdaQueryWrapper<VCodeValid> lambda = new QueryWrapper<VCodeValid>().lambda();
        lambda.eq(VCodeValid::getPhone,dto.getPhone()).orderByDesc(VCodeValid::getId);
        VCodeValid codeValid = getOne(lambda);
        if(codeValid == null){
            return  false;
        }
        if(codeValid.getVcode().equals(dto.getVcode())){
            Long valid = System.currentTimeMillis();
            if(valid <= codeValid.getEffectiveTime()){
                codeValid.setValidCode(dto.getVcode()).setValidTime(valid);
                updateById(codeValid);
                return true;
            }else {
                codeValid.setValidCode(dto.getVcode()).setValidTime(valid);
                updateById(codeValid);
                return false;
            }
        }
        return false;
    }

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return
     */
    @Override
    public Result sendEmailVCode(String email,String userName) {
        String code = RandomNumUtil.createRandomNum(6);
        // 设置发送邮件参数
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("to", new String[]{email});
        valueMap.put("title", "【领筑金融云】更改邮箱验证码");
        valueMap.put("validTime",30);
        valueMap.put("code",code);
        valueMap.put("userName", StringUtils.isEmpty(userName) ? "用户" : userName);
        valueMap.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        valueMap.put("template","sendEmailVCodeTemplate.html");
        emailTool.sendMailVCodeTemplate(valueMap);
        String token = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + email + code).getBytes());
        //保存数据
        VCodeValid vCodeValid = new VCodeValid();
        vCodeValid.setEmail(email).setVcode(code).setToken(token).setCreateTime(LocalDateTime.now()).setEffectiveTime(System.currentTimeMillis()+30*60*1000);
        save(vCodeValid);
        return Result.success("发送成功");
    }

    /**
     * 邮箱验证码校验
     * @param email 邮箱
     * @param vcode 验证码
     * @return
     */
    @Override
    public Result emailVCodeCheck(String email, String vcode) {
        LambdaQueryWrapper<VCodeValid> lambda = new QueryWrapper<VCodeValid>().lambda();
        lambda.eq(VCodeValid::getEmail,email).orderByDesc(VCodeValid::getId);
        VCodeValid codeValid = getOne(lambda);
        if(codeValid == null){
            return  Result.failure("验证码错误");
        }
        if(codeValid.getVcode().equals(vcode)){
            Long validTime = System.currentTimeMillis();
            if(validTime <= codeValid.getEffectiveTime()){
                codeValid.setValidCode(vcode).setValidTime(validTime);
                updateById(codeValid);
                return Result.success("验证码成功");
            }else {
                codeValid.setValidCode(vcode).setValidTime(validTime);
                updateById(codeValid);
                return Result.failure("验证码已失效");
            }
        }
        return Result.failure("验证码错误");
    }

}
