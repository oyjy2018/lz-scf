package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.esign.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.exception.ScfRuntimeException;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.SignService;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 电签相关
 */

@Service
public class SignServiceImpl implements SignService {

    private Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    @Value("${file-upload-url}")
    private String fileUploadUrl; //文件服务器上传地址
    @Value("${hulian-esign.appId}")
    private String signAppId; //领筑电签appId
    @Value("${hulian-esign.contract-template-path}")
    private String contractTemplatePath; //商票贴现合同模板路径
    @Value("${hulian-esign.url}")
    private String signUrl; //领筑电签服务地址
    @Value("${hulian-esign.sign.create-person}")
    private String createPersonPath; //电签创建个人接口
    @Value("${hulian-esign.sign.create-organize}")
    private String createOrganizePath; //电签创建企业接口
    @Value("${hulian-esign.sign.add-person-seal}")
    private String addPersonSeal; //电签添加个人印章接口
    @Value("${hulian-esign.sign.add-organize-seal}")
    private String addOrganizeSeal; //电签添加企业印章接口
    @Value("${hulian-esign.sign.send-sms-code}")
    private String sendSmsCode; //电签签章前发送短信验证接口
    @Value("${hulian-esign.file.upload}")
    private String fileUpload; //电签文件上传接口
    @Value("${hulian-esign.file.download}")
    private String fileDownload; //电签文件下载接口
    @Value("${hulian-esign.file.tmp-pdf-path}")
    private String tmpPdfPath; //电签合同本地临时文件路径
    @Value("${hulian-esign.sign.sign-mobile-a1}")
    private String signMobileA1; //电签甲方签章接口
    @Value("${hulian-esign.sign.sign-mobile-a2}")
    private String signMobileA2; //电签甲方签章接口
    @Value("${hulian-esign.sign.sign-mobile-b1}")
    private String signMobileB1;//电签乙方签章接口
    @Value("${hulian-esign.sign.sign-mobile-b2}")
    private String signMobileB2;//电签乙方签章接口

    @Resource
    private ESignAccountMapper eSignAccountMapper;
    @Resource
    private BusinessTicketOrderMapper businessTicketOrderMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private BusinessTicketOrderContractSignMapper orderContractSignMapper;
    @Resource
    private BusinessTicketOrderContractMapper orderContractMapper;
    @Resource
    private BusinessTicketInquireMapper inquireMapper;
    @Resource
    private BusinessTicketQuotationMapper quotationMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private EmailTool emailTool;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;
    @Autowired
    private SmsUtil smsUtil;



    //创建个人账户
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
            eSignAccount.setScfAccountType(1).setScfAccountId(dto.getUserId()).setESignAccountId(accountId).setSealData(sealData).setCreateTime(new Date());
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
            eSignAccount.setScfAccountType(0).setScfAccountId(dto.getCompanyId()).setESignAccountId(accountId).setSealData(sealData).setCreateTime(new Date());
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
    @Override
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

    //电签前发短信验证码
    @Override
    public Result sendSmsCode(EsignSendCodeDTO dto, UserInfoVO user) {

        //判断登录用户有无签署权限
        Company company = companyMapper.selectById(user.getCompanyId());
        if (company == null) return Result.failure("所属公司不存在");
        //公司的授权人id等于登录用户的id
        if (company.getRegUserId().intValue() != user.getId().intValue()) return Result.failure("您没有签署合同的权限");

        //查询订单获取合同key
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(dto.getOrderId());
        if (order == null) return Result.failure("订单不存在");
        int orderStatus = order.getOrderStatus().intValue();
        int contractSignStatus = order.getContractSignStatus().intValue();

        String signParty = dto.getSignParty();
        ESignAccount eSignAccount = null; //e签宝账号信息
        String noESignMessage = "贵公司还未通过E签宝企业注册，请联系管理员处理 。咨询电话：0755-86571036";
        //根据不同签署方
        switch (signParty) {
            case "a1" :
                if (orderStatus != BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()){
                    return Result.failure("不处于待卖方签合同状态");
                }
                //千位为1 卖方授权人已签署
                if (contractSignStatus >= 1000) return Result.failure("卖方授权人已签署合同");
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getId()).eq("scf_account_type",1));
                noESignMessage = "您还未通过E签宝个人注册，请联系管理员处理。咨询电话：0755-86571036";
                break;
            case "a2" :
                if (orderStatus != BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()){
                    return Result.failure("不处于待卖方签合同状态");
                }
                //百位为1 卖方公司已签署
                if (contractSignStatus%1000 >= 100) return Result.failure("卖方公司已签署合同");
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getCompanyId()).eq("scf_account_type",0));break;
            case "b1" :
                if (orderStatus != BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()){
                    return Result.failure("不处于待买方签合同状态");
                }
                //十位为1 买方授权人已签署
                if (contractSignStatus%100 >= 10) return Result.failure("买方授权人已签署合同");

                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getId()).eq("scf_account_type",1));
                noESignMessage = "您还未通过E签宝个人注册，请联系管理员处理。咨询电话：0755-86571036";
                break;
            case "b2" :
                if (orderStatus != BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()){
                    return Result.failure("不处于待买方签合同状态");
                }
                //个位为1 买方公司已签署
                if (contractSignStatus%10 >= 1) return Result.failure("买方公司已签署合同");

                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getCompanyId()).eq("scf_account_type",0));break;
            default:return Result.failure("不支持该签署方");
        }
        if (eSignAccount == null) return Result.failure(noESignMessage);

        String url = signUrl+sendSmsCode+"?accountId="+eSignAccount.getESignAccountId()+"&mobile="+user.getPhone()+"&appId="+signAppId;
        //调用领筑互联对接的e签宝发短信
        String result = HttpUtil.get(url);
        Result validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        return Result.success();
    }

    //根据合同模板文件生成合同，并上传到领筑互联oss
    @Override
    @Transactional
    public Result createContract(EsignContractContentDTO dto) {
        //查询订单
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(dto.getOrderId());
        if (order == null) return Result.failure("订单不存在");

        //生成合同号
        String contractNo = generateContractNo();
        logger.info("contractNo={}",contractNo);
        //dto.setContractNo(contractNo);

        ByteArrayOutputStream bos = null;
        try {
            //根据模板生成合同 并获取输出流
            bos = PDFUtil.createPDF(contractTemplatePath, JsonUtil.beanToMap(dto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将输出流写入临时文件
        File file = new File(tmpPdfPath);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //关闭流
            out.write(bos.toByteArray());
            out.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file == null) return Result.failure("文件不存在");
        String result = null;
        //将文件上传到领筑互联电签服务
        result = HttpUtil.uploadFile(file,signUrl+fileUpload+"?appId="+signAppId,"fileName");
        Result validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        Map data = (Map) validResult.getData();
        if (StringUtil.isEmpty(data.get("url"))) return Result.failure("未得到远端返回的合同文件key");
        //获取合同文件key
        String fileKey = (String) data.get("url");

        //将文件上传到文件服务
        result = HttpUtil.uploadFile(file,fileUploadUrl,"file");
        Result res = JsonUtil.jsonToBean(result,Result.class);
        if (res.getCode() == Result.RESULT_CODE_FAILURE) return res;
        data = (Map) res.getData();
        String fileUrl = (String) data.get("fileUrl");

        //删除文件
        file.delete();

        //存入订单表
        order.setContractNo(contractNo);
        if (businessTicketOrderMapper.updateById(order) != 1){
            return Result.failure("文件关联订单失败");
        }

        //存入合同表
        BusinessTicketOrderContract contract = new BusinessTicketOrderContract();
        contract.setOrderId(order.getId());
        contract.setContractType(0);//系统配置
        contract.setContractFileKey(fileKey);
        contract.setContractFileUrl(fileUrl);
        contract.setIsUse(CommonEnum.WhetherEnum.YES.getStatus());
        contract.setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus());
        contract.setCreateBy(order.getCreateBy()).setCreateTime(new Date());
        if (orderContractMapper.insert(contract) != 1) {
            throw new ScfRuntimeException("生成合同失败");
        }

        return Result.success();
    }

    /**
     * 生成合同
     * 规则：SPJY+年+月+日+当天4位流水号，比如：SPJY201908180001
     */
    private String generateContractNo() {
        StringBuffer contractNo = new StringBuffer();
        contractNo.append("SPJY");
        contractNo.append(DateUtil.getTodayDateStr("yyyyMMdd")); //年月日
        //查询今天生成的订单
        int count = businessTicketOrderMapper.selectCount(new QueryWrapper<BusinessTicketOrder>().le("create_time",new Date()).ge("create_time",DateUtil.getTodayDateStr("yyyy-MM-dd")));
        String no = StringUtil.changeLength(4,"0",count+1);
        contractNo.append(no);
        return contractNo.toString();
    }

    //合同电签(加验证码)
    @Override
    @Transactional
    public Result signContract(EsignContractSignDTO dto, UserInfoVO user) {
        //判断登录用户有无签署权限
        Company company = companyMapper.selectById(user.getCompanyId());
        if (company == null) return Result.failure("所属公司不存在");
        //公司的授权人id等于登录用户的id
        if (company.getRegUserId().intValue() != user.getId().intValue()) return Result.failure("您没有签署合同的权限");

        //查询订单
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(dto.getOrderId());
        if (order == null) return Result.failure("订单不存在");
        //查询合同
        BusinessTicketOrderContract contract = orderContractMapper.selectOne(
                new QueryWrapper<BusinessTicketOrderContract>().lambda()
                        .eq(BusinessTicketOrderContract::getOrderId,dto.getOrderId())
                        .eq(BusinessTicketOrderContract::getIsUse,CommonEnum.WhetherEnum.YES.getStatus()));
        if (contract == null) return Result.failure("合同不存在");
        //获取合同key
        if (StringUtil.isEmpty(contract.getContractFileKey())) return Result.failure("无合同文件key");
        String fileKey = contract.getContractFileKey();

        ESignAccount eSignAccount = null; //e签宝账号信息
        BusinessTicketOrderContractSign orderContractSign = new BusinessTicketOrderContractSign(); //签署记录

        int contractType = contract.getContractType().intValue();//合同类型
        Result signResult = null;
        if (0 == contractType) { //系统生成的合同  通过坐标签章
            signResult = signByPosition(orderContractSign,order,eSignAccount,dto,user,fileKey);
        }else { //用户上传的合同  通过关键字签章
            signResult = signByKeyword(orderContractSign,order,eSignAccount,dto,user,fileKey);
        }

        if (signResult.getCode() == Result.RESULT_CODE_FAILURE) return signResult;
        String signServiceId = null; //签署id
        String signDetailUrl = null; //签署详情链接
        if (signResult.getData() != null) {
            Map map = (Map) signResult.getData();
            signServiceId = (String) map.get("signServiceId");
            signDetailUrl = (String) map.get("signDetailUrl");
            orderContractSign = (BusinessTicketOrderContractSign) map.get("orderContractSign");
            order = (BusinessTicketOrder) map.get("order");
            eSignAccount = (ESignAccount) map.get("eSignAccount");
        }

        //保存签署记录
        orderContractSign.setOrderId(dto.getOrderId())
                .setESignAccountId(eSignAccount.getESignAccountId())
                .setMobile(user.getPhone())
                .setSignServiceId(signServiceId)
                .setSignDetailUrl(signDetailUrl)
                .setCreateTime(new Date())
                .setCreateBy(user.getId());
        orderContractSignMapper.insert(orderContractSign);

        Result res = new Result();
        try {
            //下载最新签章信息的合同文件
            File file = HttpUtil.downloadFile(signUrl + fileDownload + "?fileKey=" + fileKey + "&appId=" + signAppId, tmpPdfPath);
            //将文件上传到供应链文件服务器
            String result = HttpUtil.uploadFile(file,fileUploadUrl,"file");
            res = JsonUtil.jsonToBean(result,Result.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (res.getCode() == Result.RESULT_CODE_FAILURE) return res;
        Map data = (Map) res.getData();
        String fileUrl = (String) data.get("fileUrl");

        //根据合同签署状态判断订单状态
        int contractSignStatus = order.getContractSignStatus().intValue(); //取更新过后的状态
        if (contractSignStatus%100 == 11){ //买方签署完毕
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus());
        }
        if (contractSignStatus/100 == 11) {//卖方签署完毕
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_3.getStatus());
        }
        order.setContractSignStatus(contractSignStatus)
                .setUpdateBy(company.getRegUserId()).setCreateTime(new Date());
        if (businessTicketOrderMapper.updateById(order) != 1) {
            return Result.failure("更新合同状态失败");
        }
        //更新合同地址
        contract.setContractFileUrl(fileUrl)//将文件地址添加到
                .setUpdateBy(company.getRegUserId()).setUpdateTime(new Date());
        if (orderContractMapper.updateById(contract) != 1) {
            throw new ScfRuntimeException("更新合同地址失败");
        }

        Result result = Result.success();
        result.setData(FileUtil.getViewFileUrl(fileUrl));
        //发送邮件通知
        if((order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus() && contractSignStatus/100 == 0)
            || order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_3.getStatus()){
            sendSignEmail(order);
        }

        return result;
    }

    private void sendSignEmail(BusinessTicketOrder order){
        //
        BusinessTicketInquire inquire = inquireMapper.selectById(order.getInquireId());
        User inquireUser = userMapper.selectById(inquire.getPublishPersonId());
        BusinessTicketQuotation quotation = quotationMapper.selectById(order.getQuotationId());
        User quotationUser = userMapper.selectById(quotation.getQuotationPersonId());
        //给卖家发送邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{inquireUser.getEmail()});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】订单状态更新提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+inquire.getCompanyName()+"】");
        if(order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus()){
            //买家签署合同,订单状态 = 待卖家签合同
            variables.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus())+"】，请前往平台签署交易合同。");
        }else{
            //卖家签署合同,订单状态 = 双方已签合同-待支付
            variables.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus())+"】。");
        }
        variables.put("url", billFrontEndUrl+"/sell/orderdetail?orderId="+order.getId());
        variables.put("linkContent","点此查看订单详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //给买家发送邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{quotationUser.getEmail()});
        quotationEmailVO.setTemplate("NoticeTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】订单状态更新提醒");
        Map<String,Object> mapCopy = variables.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapCopy.put("firstH","【"+quotation.getCompanyName()+"】");
        if(order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus()){
            //买家签署合同,订单状态 = 待卖家签合同
            mapCopy.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus())+"】。");
            //发送短信
            Map<String, String> params = new HashMap<>();
            params.put("orderNo",order.getId().toString());
            params.put("orderStatus",BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus()));
            smsUtil.lzySmsBsend(inquireUser.getPhone(), SmsUtil.lzy_sms_type_S502,params);

        }else{
            //卖家签署合同,订单状态 = 双方已签合同-待支付
            mapCopy.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus())+"】，请前往平台支付票价。");
            //发送短信
            Map<String, String> params = new HashMap<>();
            params.put("orderNo",order.getId().toString());
            params.put("orderStatus",BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus()));
            smsUtil.lzySmsBsend(quotationUser.getPhone(), SmsUtil.lzy_sms_type_S503,params);
        }
        quotationEmailVO.setVariables(mapCopy);
        emailTool.commonSendMail(quotationEmailVO);
    }

    /**
     * 通过关键字签署
     * @param orderContractSign
     * @param order
     * @param eSignAccount
     * @param dto
     * @param user
     * @param fileKey
     * @return
     */
    private Result signByKeyword(BusinessTicketOrderContractSign orderContractSign, BusinessTicketOrder order, ESignAccount eSignAccount, EsignContractSignDTO dto, UserInfoVO user, String fileKey) {
        //获取签署方
        String signParty = dto.getSignParty();
        String requestPath = null;
        String keyWord = dto.getKeyWord();
        int contractSignStatus = order.getContractSignStatus().intValue();
        //根据不同签署方
        switch (signParty) {
            case "a1" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()){
                    return Result.failure("不处于待卖方签合同状态");
                }
                //千位为1 卖方授权人已签署
                if (contractSignStatus >= 1000) return Result.failure("卖方授权人已签署合同");

                orderContractSign.setScfAccountType(1).setScfAccountId(user.getId()).setSignParty(0);;
                contractSignStatus += 1000;
                requestPath = signMobileA1;
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getId()).eq("scf_account_type",1)); break;
            case "a2" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()){
                    return Result.failure("不处于待卖方签合同状态");
                }
                //百位为1 卖方公司已签署
                if (contractSignStatus%1000 >= 100) return Result.failure("卖方公司已签署合同");

                orderContractSign.setScfAccountType(0).setScfAccountId(user.getCompanyId()).setSignParty(0);
                contractSignStatus += 100;
                requestPath = signMobileA2;
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getCompanyId()).eq("scf_account_type",0));break;
            case "b1" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()){
                    return Result.failure("不处于待买方签合同状态");
                }
                //十位为1 买方授权人已签署
                if (contractSignStatus%100 >= 10) return Result.failure("买方授权人已签署合同");

                orderContractSign.setScfAccountType(1).setScfAccountId(user.getId()).setSignParty(1);;
                contractSignStatus += 10;
                requestPath = signMobileB1;
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getId()).eq("scf_account_type",1));break;
            case "b2" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()){
                    return Result.failure("不处于待买方签合同状态");
                }
                //个位为1 买方公司已签署
                if (contractSignStatus%10 >= 1) return Result.failure("买方公司已签署合同");

                orderContractSign.setScfAccountType(0).setScfAccountId(user.getCompanyId()).setSignParty(1);;
                contractSignStatus += 1;
                requestPath = signMobileB2;
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getCompanyId()).eq("scf_account_type",0));break;
            default:return Result.failure("不支持该签署方");
        }
        if (eSignAccount == null) return Result.failure("账户无签章配置信息");
        //更新合同签署状态
        order.setContractSignStatus(contractSignStatus);
        //编码
        try {
            keyWord = URLEncoder.encode(keyWord,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //处理url
        String url = signUrl+requestPath+"?keyWord="+ keyWord +"&fileKey="+fileKey+"&appId="+signAppId;
        //处理json参数
        Map param = new HashMap();
        param.put("mobile",user.getPhone()); //接受验证码的手机号即为登录人手机号
        param.put("code",dto.getCode());
        param.put("accountId",eSignAccount.getESignAccountId());
        param.put("sealData",eSignAccount.getSealData());

        //请求电签接口
        String result = HttpUtil.post(url,JsonUtil.toJSON(param));

        Result validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        Map map = (Map) validResult.getData();
        map.put("orderContractSign",orderContractSign);
        map.put("order",order);
        map.put("eSignAccount",eSignAccount);

        return validResult;
    }

    /**
     * 坐标签章
     * @param orderContractSign
     * @param order
     * @param eSignAccount
     * @param dto
     * @param user
     * @param fileKey
     * @return
     */
    private Result signByPosition(BusinessTicketOrderContractSign orderContractSign, BusinessTicketOrder order, ESignAccount eSignAccount, EsignContractSignDTO dto, UserInfoVO user, String fileKey) {
        String requestPath = null;
        int contractSignStatus = order.getContractSignStatus().intValue();
        Map<String,String> signPos = new HashMap<>(); //电子章坐标配置
        //获取签署方
        String signParty = dto.getSignParty();
        //根据不同签署方
        switch (signParty) {
            case "a1" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()){
                    return Result.failure("不处于待卖方签合同状态");
                }
                //千位为1 卖方授权人已签署
                if (contractSignStatus >= 1000) return Result.failure("卖方授权人已签署合同");

                orderContractSign.setScfAccountType(1).setScfAccountId(user.getId()).setSignParty(0);;
                contractSignStatus += 1000;
                requestPath = signMobileA1;
                signPos.put("posX","197");
                signPos.put("posY","198"); //signPos.put("posY","217");
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getId()).eq("scf_account_type",1)); break;
            case "a2" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()){
                    return Result.failure("不处于待卖方签合同状态");
                }
                //百位为1 卖方公司已签署
                if (contractSignStatus%1000 >= 100) return Result.failure("卖方公司已签署合同");

                orderContractSign.setScfAccountType(0).setScfAccountId(user.getCompanyId()).setSignParty(0);
                contractSignStatus += 100;
                requestPath = signMobileA2;
                signPos.put("posX","200");
                signPos.put("posY","285");
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getCompanyId()).eq("scf_account_type",0));break;
            case "b1" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()){
                    return Result.failure("不处于待买方签合同状态");
                }
                //十位为1 买方授权人已签署
                if (contractSignStatus%100 >= 10) return Result.failure("买方授权人已签署合同");

                orderContractSign.setScfAccountType(1).setScfAccountId(user.getId()).setSignParty(1);;
                contractSignStatus += 10;
                requestPath = signMobileB1;
                signPos.put("posX","468");
                signPos.put("posY","197"); //signPos.put("posY","217");
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getId()).eq("scf_account_type",1));break;
            case "b2" :
                if (order.getOrderStatus().intValue() != BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()){
                    return Result.failure("不处于待买方签合同状态");
                }
                //个位为1 买方公司已签署
                if (contractSignStatus%10 >= 1) return Result.failure("买方公司已签署合同");

                orderContractSign.setScfAccountType(0).setScfAccountId(user.getCompanyId()).setSignParty(1);;
                contractSignStatus += 1;
                requestPath = signMobileB2;
                signPos.put("posX","468");
                signPos.put("posY","285");
                eSignAccount = eSignAccountMapper.selectOne(new QueryWrapper<ESignAccount>().eq("scf_account_id",user.getCompanyId()).eq("scf_account_type",0));break;
            default:return Result.failure("不支持该签署方");
        }
        if (eSignAccount == null) return Result.failure("账户无签章配置信息");

        //更新合同的签署状态
        order.setContractSignStatus(contractSignStatus);

        //处理url
        String url = signUrl+requestPath+"?fileKey="+fileKey+"&appId="+signAppId;
        //处理json参数
        Map param = new HashMap();
        param.put("mobile",user.getPhone()); //接受验证码的手机号即为登录人手机号
        param.put("code",dto.getCode());
        param.put("accountId",eSignAccount.getESignAccountId());
        param.put("sealData",eSignAccount.getSealData());
        signPos.put("posPage","2");
        signPos.put("width","135");
        param.put("signPos",signPos);

        //签署请求
        String result = HttpUtil.post(url,JsonUtil.toJSON(param));

        Result validResult = validSignResult(result);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE) return validResult;
        Map map = (Map) validResult.getData();
        map.put("order",order);
        map.put("orderContractSign",orderContractSign);
        map.put("eSignAccount",eSignAccount);

        return validResult;
    }

    //获取合同内容
    @Override
    public Result getContractContent(Long orderId) {
        return Result.success();
    }

    //更换合同
    @Override
    @Transactional
    public Result changeContract(EsignContractChangeDTO dto) {
        //查询订单
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(dto.getOrderId());
        if (order == null) return Result.failure("订单数据不存在");
        //如果已经发生签署  则不能修改
        if (order.getContractSignStatus() != 0) return Result.failure("合同已经签署，不能修改");

        //查询使用中的合同
        BusinessTicketOrderContract useContract = orderContractMapper.selectOne(
                new QueryWrapper<BusinessTicketOrderContract>().lambda()
                        .eq(BusinessTicketOrderContract::getOrderId,dto.getOrderId())
                        .eq(BusinessTicketOrderContract::getIsUse,CommonEnum.WhetherEnum.YES.getStatus()));
        if (useContract == null) return Result.failure("合同不存在");

        Result result = Result.success();
        if ("0".equals(dto.getContractType())) { //选择的是系统生成
            //如果和原有合同类型相同 直接返回成功
            if (dto.getContractType().equals(useContract.getContractType().toString())) {
                result.setData(FileUtil.getViewFileUrl(useContract.getContractFileUrl()));
                return result;
            }
            //查询系统生成的合同
            BusinessTicketOrderContract defaultContract = orderContractMapper.selectOne(
                    new QueryWrapper<BusinessTicketOrderContract>().lambda()
                            .eq(BusinessTicketOrderContract::getOrderId,dto.getOrderId())
                            .eq(BusinessTicketOrderContract::getContractType,0));
            if (defaultContract == null) return Result.failure("合同不存在");
            //再次激活系统生成的合同
            defaultContract.setIsUse(CommonEnum.WhetherEnum.YES.getStatus());
            if (orderContractMapper.updateById(defaultContract) != 1) {
                return Result.failure("启用系统默认合同失败");
            }
            result.setData(FileUtil.getViewFileUrl(defaultContract.getContractFileUrl()));
        } else {//选择的是用户上传
            String fileUrl = dto.getFileUrl();
            //获取文件
            File file = new File(fileUrl);
            //如果是word类型  转为pdf
            if (dto.getFileUrl().endsWith(".doc")||dto.getFileUrl().endsWith(".docx")) {
                file = Word2Pdf.word2pdf(dto.getFileUrl(),tmpPdfPath);
                //将文件上传到供应链文件服务器
                String s = HttpUtil.uploadFile(file,fileUploadUrl,"file");
                Result res = JsonUtil.jsonToBean(s,Result.class);
                if (res.getCode() == Result.RESULT_CODE_FAILURE) return res;
                Map data = (Map) res.getData();
                fileUrl = (String) data.get("fileUrl");
            }

            //将文件上传到领筑互联电签服务
            String uploadResult = HttpUtil.uploadFile(file,signUrl+fileUpload+"?appId="+signAppId,"fileName");
            Result validResult = validSignResult(uploadResult);
            if (validResult.getCode() != Result.RESULT_CODE_SUCCESS) return validResult;
            Map data = (Map) validResult.getData();
            if (StringUtil.isEmpty(data.get("url"))) return Result.failure("未得到远端返回的合同文件key");
            //获取合同文件key
            String fileKey = (String) data.get("url");

            //创建一个新的合同
            BusinessTicketOrderContract newContract = new BusinessTicketOrderContract();
            newContract.setOrderId(dto.getOrderId());
            newContract.setIsUse(CommonEnum.WhetherEnum.YES.getStatus());
            newContract.setContractType(1);
            newContract.setContractFileKey(fileKey);
            newContract.setContractFileUrl(fileUrl);
            newContract.setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus());
            newContract.setCreateBy(dto.getUserId()).setCreateTime(new Date());
            if (orderContractMapper.insert(newContract) != 1){
                return Result.failure("保存新合同数据失败");
            }
            result.setData(FileUtil.getViewFileUrl(fileUrl));
        }

        //将原使用的合同改为未使用
        useContract.setIsUse(CommonEnum.WhetherEnum.NO.getStatus())
                .setUpdateTime(new Date()).setUpdateBy(dto.userId);
        if (orderContractMapper.updateById(useContract) != 1) {
            throw new ScfRuntimeException("更新原有合同使用状态失败");
        }

        return result;
    }

    //校验关键字是否唯一
    @Override
    public Result isOnlyKeyWord(EsignKeyWordOnlyValidDTO dto) {
        //获取合同
        BusinessTicketOrderContract contract = orderContractMapper.selectOne(
                new QueryWrapper<BusinessTicketOrderContract>().lambda()
                .eq(BusinessTicketOrderContract::getIsUse,"1")
                .eq(BusinessTicketOrderContract::getOrderId,dto.getOrderId()));
        if (contract == null) return Result.failure("合同信息不存在");
        //获取合同文件
        String content = PDFUtil.getCharacterContent(contract.getContractFileUrl());
        if (StringUtil.isEmpty(content)) return Result.failure("获取文件内容失败");

        //判断关键字
        int count = StringUtil.getContainCount(content,dto.getKeyWord());
        if (count == 0) return Result.failure("您输入的关键字在合同中不存在，请重新输入。");
        if (count > 1) return Result.failure("您输入的关键字在合同中存在"+ count +"个，请重新输入。");

        return Result.success();
    }

    @Override
    public Result getInitContractViewUrl(Long orderId) {
        //获取合同
        BusinessTicketOrderContract contract = orderContractMapper.selectOne(
                new QueryWrapper<BusinessTicketOrderContract>().lambda()
                        .eq(BusinessTicketOrderContract::getIsUse,"1")
                        .eq(BusinessTicketOrderContract::getOrderId,orderId));
        if (contract == null) return Result.failure("合同信息不存在");

        Result result = Result.success();
        result.setData(FileUtil.getViewFileUrl(contract.getContractFileUrl()));

        return result;
    }

    //校验 调用领筑互联电签服务返回结果
    private Result validSignResult(String result) {
        if (result == null) {
            return Result.failure("请求失败");
        }
        Map resultMap = JsonUtil.jsonToMap(result);
        //没有data返回
        if (resultMap.get("data") == null){
            if (resultMap.get("retCode") == null || !resultMap.get("retCode").toString().equals("0000")) {
                return Result.failure(resultMap.get("rtnMsg").toString());
            }
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

    public static void main(String[] args) {
        try {
            File file = HttpUtil.downloadFile("http://120.79.31.91:19201/esign-service/file/fileDownload?fileKey=20190802094120476_714647.pdf", "F://this.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
