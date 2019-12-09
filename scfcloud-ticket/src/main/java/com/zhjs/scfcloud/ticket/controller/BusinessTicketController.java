package com.zhjs.scfcloud.ticket.controller;

import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.ocr.response.OcrRespBody;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.SingleParamDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.*;
import com.zhjs.scfcloud.model.util.JdzpRespUtil;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderDetailsVO;
import com.zhjs.scfcloud.ticket.service.BusinessTicketInquireService;
import com.zhjs.scfcloud.ticket.service.BusinessTicketOrderService;
import com.zhjs.scfcloud.ticket.service.BusinessTicketQuotationService;
import com.zhjs.scfcloud.ticket.service.UserService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.*;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

import static com.zhjs.scfcloud.util.util.RedisUtil.REDIS_KEY_DEFEND_REPEAT;

/**
 * @author: dailongting
 * @date:2019/7/9 15:57
 */
@RestController
@RequestMapping("/businessTicket")
public class BusinessTicketController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JDZPUtil jdzpUtil;
    @Autowired
    private BusinessTicketInquireService businessTicketInquireService;
    @Autowired
    private BusinessTicketQuotationService businessTicketQuotationService;
    @Autowired
    private BusinessTicketOrderService businessTicketOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;


    @ApiOperation("获取公司京东实名属性")
    @GetMapping("/getCompanyJdProperty")
    public Result getCompanyJdProperty(){
        logger.info("subject:{}","获取公司京东实名属性");
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("请先登录");
        return userService.getCompanyJdProperty(user.getCompanyId());
    }


    //商票ocr识别
    @PostMapping("/ocrRecognize")
    public Result ocrRecognize(@RequestBody SingleParamDTO dto){
        logger.info("subject:{},dto:{}","商票ocr识别",dto.toString());
        CommonResponse<OcrRespBody> respBody = jdzpUtil.ocrRecognize(dto.getValue(),"1");//固定传1 识别正面
        //分析结果
        Result analyzeResult = JdzpRespUtil.analyzeResponseJDZP(respBody);
        logger.info("subject:{},ocr:{}","商票OCR识别结果",JsonUtil.toJSON(respBody));
        if (analyzeResult.getCode() == Result.RESULT_CODE_FAILURE) return analyzeResult;

        return Result.success(respBody.getData());
    }

    //发布票据
    @PostMapping("/inquire")
    @RequiresPermissions("ticket:inquire:issueTicket")
    public Result inquire(@RequestBody @Valid BusinessTicketInquireAddDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","发布票据",dto.toString());

        if (result.hasErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        if (dto.getHasSignProtocols() == 0) Result.failure("未同意签署《票据收益权转让协议》");
        if (ListUtil.isEmpty(dto.getFileList())) Result.failure("票据图片为空");

        //票据发布时 剩余有效时间不能低于五天
        if (System.currentTimeMillis() > DateUtil.getLastTimeForDate(dto.getMaturityDate())) return Result.failure("您发布的票据已经到期，不能发布");
        if (System.currentTimeMillis() > dto.getExpirationDate().getTime()) return Result.failure("询价截止日期不能小于当前时间");
        if (DateUtil.getTimeDistance(new Date(),DateUtil.getLastDateTimeForDate(dto.getMaturityDate())) < 5) return Result.failure("您发布的票据快要到期，不能发布");

        if (dto.getExpirationDate().getTime() > DateUtil.getLastTimeForDate(dto.getMaturityDate())) return Result.failure("询价截止日期不能大于票据到期日");

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        dto.setCompanyId(user.getCompanyId());
        dto.setCompanyName(user.getCompanyName());
        dto.setPublishPersonId(user.getId());
        dto.setPublishPerson(user.getUserName());

        return businessTicketInquireService.inquire(dto);
    }

    //发布列表
    @PostMapping("/myInquireList")
    @RequiresPermissions("ticket:inquire:myList")
    public Result myInquireList(@RequestBody BusinessTicketInquireListDTO dto){
        logger.info("subject:{},dto:{}","发布列表",dto.toString());

        //根据登录人查询
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");
        dto.setCompanyId(user.getCompanyId());

        return businessTicketInquireService.inquireList(dto);
    }

    //交易大厅询价列表
    @PostMapping("/inquireList")
    public Result inquireList(@Valid @RequestBody BusinessTicketInquireListDTO dto,BindingResult result){
        logger.info("subject:{},dto:{}","交易大厅询价列表",dto.toString());

        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        //只查待报价和竞价中的
        dto.setMultiStatus("('"+BusinessTicketEnum.InquireStatus.inquire_status_1.getStatus()+"','"+BusinessTicketEnum.InquireStatus.inquire_status_2.getStatus()+"')");
        //过滤掉指定卖家的
        dto.setIsAssignBuyer("1");

        return businessTicketInquireService.inquireList(dto);
    }

    //给我的询价
    @PostMapping("/myAssignInquireList")
    @RequiresPermissions("ticket:inquire:myAssign")
    public Result myAssignInquireList(@Valid @RequestBody BusinessTicketInquireMyAssignListDTO dto,BindingResult result){
        logger.info("subject:{},dto:{}","交易大厅询价列表",dto.toString());

        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        //根据登录人查询
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setCompanyId(user.getCompanyId());

        return businessTicketInquireService.myAssignInquireList(dto);
    }

    //撤销发布
    @PostMapping("/cancelInquire")
    @RequiresPermissions("ticket:inquire:cancel")
    public Result cancelInquire(@RequestBody @Valid BusinessTicketInquireCancelDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","撤销发布",dto.toString());

        if (result.hasErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");
        dto.setUserId(user.getId());

        return businessTicketInquireService.cancelInquire(dto);
    }

    //询价详情
    @PostMapping("/inquireDetail")
    @RequiresPermissions(value = {"ticket:inquire:detail","ticket:inquire:detail:quotation"},logical= Logical.OR)
    public Result inquireDetail(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","询价详情",dto.toString());

        if (StringUtil.isEmpty(dto.getId())) return Result.failure("询价单id为空");

        return businessTicketInquireService.inquireDetail(dto.getId());
    }


    //报价
    @PostMapping("/quote")
    @RequiresPermissions("ticket:quotation:quotation")
    public Result quote(@RequestBody @Valid BusinessTicketQuoteAddDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","报价",dto.toString());

        if (result.hasErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        if (dto.getHasSignProtocols() == 0) Result.failure("未同意签署《票据收益权转让协议》");

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        String redisQuoteKey = REDIS_KEY_DEFEND_REPEAT +"quote:"+dto.getInquireId()+ "&" + user.getCompanyId();
        if (!StringUtil.isEmpty(redisUtil.get(redisQuoteKey))) {
            return Result.failure("请勿重复提交");
        }
        //设置五秒 防重复提交
        redisUtil.set(redisQuoteKey,user.getUserName(),5);

        dto.setCompanyId(user.getCompanyId());
        dto.setCompanyName(user.getCompanyName());
        dto.setQuotationPersonId(user.getId());
        dto.setQuotationPerson(user.getUserName());

        return businessTicketQuotationService.quote(dto);
    }

    /**
     * 接受报价
     * @param dto
     * @param result
     * @return
     */
    @PostMapping("/acceptedQuotation")
    @RequiresPermissions("ticket:inquire:acceptedQuotation")
    public String acceptedQuotation(@RequestBody @Valid QuotationDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","接受报价",JsonUtil.toJSON(dto));
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) {
            return Result.failure("没有登录信息").toJSON();
        }
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return businessTicketQuotationService.acceptedQuotation(dto).toJSON();
    }

    /**
     * 拒绝报价
     * @param dto
     * @param result
     * @return
     */
    @PostMapping("/rejectQuotation")
    @RequiresPermissions("ticket:inquire:rejectQuotation")
    public String rejectQuotation(@RequestBody QuotationDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","拒绝报价",JsonUtil.toJSON(dto));
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) {
            return Result.failure("没有登录信息").toJSON();
        }
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return businessTicketQuotationService.rejectQuotation(dto).toJSON();
    }

    /**
     * 查询订单列表
     * @param dto
     * @return
     */
    @PostMapping("/findOrderList")
    @RequiresPermissions(value = {"ticket:order:list","ticket:order:list:quotation"},logical = Logical.OR)
    public String findOrderList(@RequestBody @Valid QueryOrderListDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","查询订单列表",JsonUtil.toJSON(dto));
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }

        //根据用户类型确定查询票方订单列表，还是资方订单列表
        if(BusinessTicketEnum.UserType.USER_TYPE_1.getStatus().intValue() == dto.getUserType().intValue()){
            dto.setPublishCompanyId(MySubjectUtil.getUser().getCompanyId());
        }else{
            dto.setQuotationCompanyId(MySubjectUtil.getUser().getCompanyId());
        }
        return businessTicketOrderService.findOrderList(dto).toJSON();
    }

    /**
     * 查询订单详情
     * @param dto
     * @param result
     * @return
     */
    @PostMapping("/findOrderDetails")
    @RequiresPermissions(value = {"ticket:order:detail","ticket:order:detail:quotation"},logical= Logical.OR)
    public String findOrderDetails(@RequestBody @Valid OrderBaseDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","查询订单详情",JsonUtil.toJSON(dto));
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }
        OrderDetailsVO details = businessTicketOrderService.findOrderDetails(dto);
        if(details == null){
            return Result.failure("订单数据丢失").toJSON();
        }
        return Result.success(details).toJSON();
    }

    /**
     * 发起订单支付
     * @param dto
     * @param result
     * @return
     */
    @PostMapping("/payOrder")
    @RequiresPermissions("ticket:order:pay")
    public String payOrder(@RequestBody @Valid OrderBaseDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","发起订单支付",JsonUtil.toJSON(dto));
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) {
            return Result.failure("没有登录信息").toJSON();
        }
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return businessTicketOrderService.payOrder(dto).toJSON();
    }

    /**
     * 撤销订单
     * @param dto
     * @param result
     * @return
     */
    @PostMapping("/revokeOrder")
    @RequiresPermissions(value = {"ticket:order:revoke","ticket:order:revoke:quotation"},logical = Logical.OR)
    public String revokeOrder(@RequestBody @Valid RevokeOrderDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","撤销订单", JsonUtil.toJSON(dto));
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) {
            return Result.failure("没有登录信息").toJSON();
        }
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return businessTicketOrderService.revokeOrder(dto).toJSON();
    }

    /**
     * 询价时指定买家列表
     */
    @PostMapping("/quotationCompanyList")
    public Result quotationCompanyList(){
        logger.info("subject:{}","询价时指定买家列表");
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        return userService.quotationCompanyList(user.getCompanyId());
    }

}
