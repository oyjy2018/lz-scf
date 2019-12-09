package com.zhjs.scfcloud.ticket.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketAllInquireDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.entity.PlatformServiceRate;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.BusinessTicketInquireService;
import com.zhjs.scfcloud.ticket.service.BusinessTicketOrderService;
import com.zhjs.scfcloud.ticket.service.PlatformServiceRateService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 运营管理
 */
@RestController
@RequestMapping("/operation/")
public class OperationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusinessTicketInquireService businessTicketInquireService;
    @Autowired
    private BusinessTicketOrderService businessTicketOrderService;
    @Resource
    private PlatformServiceRateService platformServiceRateService;

    //所有询价
    @PostMapping("/allInquire")
    @RequiresPermissions("operationsMgt:allInquire:view")
    public Result allInquire(@Valid @RequestBody BusinessTicketAllInquireDTO dto){
        logger.info("subject:{},dto:{}","所有询价",dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");

        //下列用于数据权限控制
        dto.setUserId(user.getId());
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());

        return businessTicketInquireService.allInquire(dto);
    }

    //所有订单
    @PostMapping("allOrder")
    @RequiresPermissions("operationsMgt:allQuotation:view")
    public String allOrder(@RequestBody BusinessTicketOrderAllListDTO dto){
        logger.info("subject:{},dto:{}","获取所有商票订单",dto);

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息").toJSON();
        //下列用于数据权限控制
        dto.setUserId(user.getId());
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());

        return businessTicketOrderService.allOrder(dto);
    }

    //重新发起商票状态轮询
    @PostMapping("reStartTicketStatusQuery")
    public Result reStartTicketStatusQuery(@RequestBody BaseIdDTO dto) {
        logger.info("subject:{},dto:{}","重新发起商票状态轮询",dto);
        if (StringUtil.isEmpty(dto.getId())) return Result.failure("订单id不能为空");
        return businessTicketOrderService.reStartTicketStatusQuery(dto.getId());
    }

    //保存票据平台服务费率
    @PostMapping("platformServiceRateSave")
    @RequiresPermissions("operationsMgt:platformServiceRate:save")
    public Result platformServiceRateSave(@RequestBody BaseSingleUpdateDTO dto) {
        logger.info("subject:{},dto:{}","保存票据平台服务费率",dto);
        if (StringUtil.isEmpty(dto.getValue())) return Result.failure("收取服务费率不能为空");
        if (!StringUtil.isDigit(dto.getValue())) return Result.failure("收取服务费率必须为数字");
        if (new BigDecimal(dto.getValue()).compareTo(BigDecimal.ZERO) < 0 || new BigDecimal(dto.getValue()).compareTo(new BigDecimal(100)) > 0) {
            return Result.failure("收取服务费率必须为0到100的数字");
        }
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        //下列用于数据权限控制
        dto.setUpdateBy(user.getId());
        return platformServiceRateService.save(dto);
    }

    //查询票据平台服务费率
    @PostMapping("platformServiceRateQuery")
    public Result platformServiceRateQuery() {
        logger.info("subject:{}","查询票据平台服务费率");
        return platformServiceRateService.query();
    }
}
