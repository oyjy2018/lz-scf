package com.zhjs.scfcloud.util.util;

import com.jd.jr.cbp.sdk.client.DefaultCbpClient;
import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.basic.request.QueryCompanyInfoReqBody;
import com.jd.jr.cbp.sdk.entity.basic.request.QueryUserCenterUrlReqBody;
import com.jd.jr.cbp.sdk.entity.basic.response.QueryCompanyInfoRespBody;
import com.jd.jr.cbp.sdk.entity.basic.response.QueryUserCenterUrlRespBody;
import com.jd.jr.cbp.sdk.entity.billpay.request.*;
import com.jd.jr.cbp.sdk.entity.billpay.response.*;
import com.jd.jr.cbp.sdk.entity.ocr.request.OcrReqBody;
import com.jd.jr.cbp.sdk.entity.ocr.response.OcrRespBody;
import com.jd.jr.cbp.sdk.services.basic.BasicInfoService;
import com.jd.jr.cbp.sdk.services.billpay.BankCardService;
import com.jd.jr.cbp.sdk.services.billpay.EcdsOrderService;
import com.jd.jr.cbp.sdk.services.billpay.OrderService;
import com.jd.jr.cbp.sdk.services.ocr.RecognizeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 京东金融对接工具类
 * @author: dailongting
 * @date:2019/6/26 16:49
 */
@Component
public class JDZPUtil {
    private Logger log = LoggerFactory.getLogger(JDZPUtil.class);

    @Resource
    private BankCardService bankCardService;
    @Resource
    private OrderService orderService;
    @Resource
    private RecognizeService recognizeService;
    @Resource
    private EcdsOrderService ecdsOrderService;
    @Resource
    private BasicInfoService basicInfoService;

    public static String getPlatformReqNo(Long orderId){
        return DateUtil.format(new Date(),"ssmmHHddMMyyy")+orderId+RandomNumUtil.createRandomNum(3);
    }

    /**
     * 平台获取处理异常单URL
     * QueryUserCenterUrlReqBody
     * 属性名	类型	空值	备注
     * platformId	String	否	平台ID，由京东智票分配
     * companyName	String	否	企业名称
     * platformUserId	String	否	平台用户ID，平台需要保证不同用户的platformUserId不同。
     */
    public CommonResponse<QueryUserCenterUrlRespBody> queryUserCenterUrl(String companyName,String platformUserId) {
        QueryUserCenterUrlReqBody reqBody = new QueryUserCenterUrlReqBody();
        reqBody.setCompanyName(companyName);
        reqBody.setPlatformUserId(platformUserId);
        CommonResponse<QueryUserCenterUrlRespBody> resp = basicInfoService.queryUserCenterUrl(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 企业实名信息查询
     * QueryCompanyInfoReqBody
     * 属性名	类型	空值	备注
     * platformId	String	否	平台ID，由京东智票分配
     * companyName	String	否	企业名称
     * creditCode	Strinng	是	统一社会信用代码或者工商注册号
     */
    public CommonResponse<QueryCompanyInfoRespBody> queryCompanyInfo(String companyName, String creditCode) {
        QueryCompanyInfoReqBody reqBody = new QueryCompanyInfoReqBody();
        reqBody.setCompanyName(companyName);
        reqBody.setCreditCode(creditCode);
        CommonResponse<QueryCompanyInfoRespBody> resp = basicInfoService.queryCompanyInfo(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 查询是否授权京东查询ECDS 银行账户（查询后，修改收票账户的ECDS授权状态）
     * QueryEcdsAuthReqDto
     * 属性名	类型	空值	备注
     * platformId	String	否	平台ID，由京东智票分配
     * buyerAccount	AccountDetail	否	买方收票账户（ECDS验证）
     *
     * AccountDetail
     * 属性名	类型	空值	备注
     * accountNo	String	否	账号
     * bankCode			买方收票账户银行编码
     */
    public CommonResponse<QueryEcdsAuthRespDto> queryEcdsAccountAuth(QueryEcdsAuthReqDto reqBody) {
        CommonResponse<QueryEcdsAuthRespDto> resp = ecdsOrderService.queryEcdsAccountAuth(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 【银行卡管理】验证打款金额并绑卡
     * VerifyAgencyDefpayReqBody `
     * 属性名	类型	空值	备注
     * platformId	String	否	平台ID，由京东智票分配
     * platformUserId	String	否	平台用户ID，平台需要保证不同用户的platformUserId不同。
     * payOrderId	Long	否	代付单id
     * amount	Long	否	打款金额，单位：分
     */
    public CommonResponse<VerifyAgencyDefpayRespBody> verifyAgencyDefpay(VerifyAgencyDefpayReqBody reqBody) {
        CommonResponse<VerifyAgencyDefpayRespBody> resp = bankCardService.verifyAgencyDefpay(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 【银行卡管理】打款申请
     * ApplyAgencyDefpayReqBody `
     * 属性名	类型	空值	备注
     * platformId	String	否	平台ID，由京东智票分配
     * platformUserId	String	否	平台用户ID，平台需要保证不同用户的platformUserId不同。
     * bankCode	String	否	银行编码
     * bankName	String	否	银行名称
     * bankCnaps	String	否	银行支行银联号
     * bankChildName	String	否	支行银行全称
     * bankAccountName	String	否	账号名称
     * bankAccountNo	String	否	银行账号
     * provinceId	String	否	省ID
     * provinceName	String	否	省名称
     * cityId	String	否	城市ID
     * cityName	String	否	城市名称
     */
    public CommonResponse<ApplyAgencyDefpayRespBody> applyAgencyDefpay(ApplyAgencyDefpayReqBody reqBody) {
        CommonResponse<ApplyAgencyDefpayRespBody> resp = bankCardService.applyAgencyDefpay(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 查询开户行支行列表
     * @param cnapBankCode 联行号银行编码
     * @param cityId  支行城市id
     * @return
     */
    public CommonResponse<QueryBankCnapsListRespBody> queryBankCnapsList(Integer cnapBankCode,String cityId) {
        QueryBankCnapsListReqBody reqBody = new QueryBankCnapsListReqBody();
        reqBody.setCnapBankCode(cnapBankCode);
        reqBody.setCityId(cityId);
        CommonResponse<QueryBankCnapsListRespBody> resp = bankCardService.queryBankCnapsList(reqBody, UUID.randomUUID().toString());
        return resp;
    }

   /**
     * 查询支持小额打款银行列表
     * @return
     */
    public CommonResponse<QueryBankListRespBody> queryBankList() {
        QueryBankListReqBody reqBody = new QueryBankListReqBody();
        CommonResponse<QueryBankListRespBody> resp = bankCardService.queryBankList(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 获取收银台地址
     * @param platformReqNo 平台请求流水号
     * @return
     */
    public CommonResponse<GetCashierUrlRespBody> getCashierUrl(String platformReqNo){
        GetCashierUrlReqBody reqBody = new GetCashierUrlReqBody();
        reqBody.setPlatformReqNo(platformReqNo);
        CommonResponse<GetCashierUrlRespBody> resp = orderService.getCashierUrl(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 查询用户绑定银行卡账户接口
     * @param platformUserId
     * @param type
     * @return
     */
    public CommonResponse<QueryUserBindAccountsRespBody> queryUserBindAccounts(String platformUserId, Integer type){
        QueryUserBindAccountsReqBody reqBody = new QueryUserBindAccountsReqBody();
        reqBody.setPlatformUserId(platformUserId);
        reqBody.setType(type);
        CommonResponse<QueryUserBindAccountsRespBody> resp = bankCardService.queryUserBindAccounts(reqBody, UUID.randomUUID().toString());
        return resp;
    }

    /**
     * 创建订单接口
     * @param body
     * platformReqNo	String	否	平台请求流水号
     * platformOrderNo	String	否	平台订单号
     * platformOrderDate	String	否	平台下单时间yyyyMMddHHmmss
     * platformContractNo	String	否	平台合同编号。
     * expireDate	String	否	订单支付过期时间 yyyyMMddHHmmss。
     * 订单支付成功后，可继续流转，不受此时间限制。
     * transRate	String(15,2)	是	年化利率
     * transAmt	Long	否	交易金额（不含手续费），单位：分
     * transCharge	Long	否	手续费金额，单位：分
     * buyerUserId	String	否	买方用户平台方ID
     * buyerAccount	AccountDetail	否	买方收票账户（必须授权京东查询票据状态，授权问题咨询我方业务或者产品经理）
     * sellerUserId	String	否	卖方用户平台方ID
     * sellerAccountA	AccountDetail	否	卖方收款账户（放款）
     * sellerAccountB	AccountDetail	否	卖方持票账户，预留参数，与sellerAccountA保持一致即可
     * billDetail	BillDetail	否	票据信息
     *
     *
     * AccountDetail
     * 属性名	类型	空值	备注
     * bindId	String	否	绑定ID，通过2#接口可查询到
     * bankCnaps	String	否	开户行联行号
     * accountNo	String	否	银行账号
     *
     * BillDetail
     * 属性名	类型	空值	备注
     * billNo	String	否	票据号码
     * billAmt	Long	否	票面金额，单位：分
     * drawerName	String	是	出票人名称
     * drawerBank	String	是	出票人开户行行号
     * drawerAccount	String	是	出票人开户账号
     * accepterName	String	是	承兑人名称
     * accepterBank	String	是	承兑人开户行行号
     * accepterAccount	String	是	承兑人开户账号
     * payeeName	String	是	收款人名称
     * payeeBank	String	是	收款人开户行行号
     * payeeAccount	String	是	收款人开户账号
     * @return
     */
    public CommonResponse<CreateOrderRespBody> createOrder(CreateOrderReqBody body){
        return orderService.createOrder(body, UUID.randomUUID().toString());
    }

    /**
     * 查询订单详情
     * @param platformReqNo 平台请求流水号
     * @return
     */
    public CommonResponse<QueryOrderRespBody> queryOrder(String platformReqNo){
        QueryOrderReqBody body = new QueryOrderReqBody();
        body.setPlatformReqNo(platformReqNo);
        return orderService.queryOrder(body, UUID.randomUUID().toString());
    }

    /**
     * 【银行卡管理】解绑银行账户
     * @param platformUserId 平台用户ID，平台需要保证不同用户的platformUserId不同。
     * @param bindId 绑定ID
     * @return
     */
    public CommonResponse<UnBindAccountRespBody> disableAccount(String platformUserId, String bindId){
        UnBindAccountReqBody body = new UnBindAccountReqBody();
        body.setPlatformUserId(platformUserId);
        body.setBindId(bindId);
        return bankCardService.disableAccount(body, UUID.randomUUID().toString());
    }

    /**
     * 查询开户行省市
     * @param provinceId 省ID
     * @return
     */
    public CommonResponse<QueryProvincesAndCitysRespBody> queryProvincesAndCitys(String provinceId){
        QueryProvincesAndCitysReqBody body = new QueryProvincesAndCitysReqBody();
        body.setProvinceId(provinceId);
        return bankCardService.queryProvincesAndCitys(body, UUID.randomUUID().toString());
    }

    /**
     * 商票OCR识别
     * @param base64Str 图片base64，字符串最大不超过5.3MB
     * @param imgType 1=票据正面图片，目前仅支持正面。
     * @return
     */
    public CommonResponse<OcrRespBody> ocrRecognize(String base64Str,String imgType){
        OcrReqBody body = new OcrReqBody();
        body.setBase64Str(base64Str);
        body.setImgType(imgType);
        return recognizeService.ocrRecognize(body, UUID.randomUUID().toString());
    }

    /**
     * 订单操作接口
     * platformId	String	否	平台ID，由京东智票分配
     * platformReqNo	String	否	平台请求流水号
     * handleType	Integer	否	操作类型：
     * 1继续查询票据状态，同步返回成功则表示已受理成功，如果票据状态发生变化会发送通知。结合异步通知中的票据签收结果通知一起使用
     * 2申请撤单。订单最终结果以订单异步通知为准
     * @param platformReqNo
     * @param handleType
     * @return
     */
    public CommonResponse<HandleOrderRespBody> handleOrder(String platformReqNo, Integer handleType){
        HandleOrderReqBody body = new HandleOrderReqBody();
        body.setPlatformReqNo(platformReqNo);
        body.setHandleType(handleType);
        return orderService.handleOrder(body, UUID.randomUUID().toString());
    }



    public static void main(String[] args) {
        QueryProvincesAndCitysReqBody body = new QueryProvincesAndCitysReqBody();
        body.setPlatformId("111754934");
        DefaultCbpClient client = new DefaultCbpClient();
        BankCardService bankCardService = new BankCardService();
        CommonResponse<QueryProvincesAndCitysRespBody> res = bankCardService.queryProvincesAndCitys(body, UUID.randomUUID().toString());
        System.out.println(res.toString());
    }
}
