package com.zhjs.scfcloud.task.job;

import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.billpay.response.HandleOrderRespBody;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.BusinessTicketOrder;
import com.zhjs.scfcloud.model.mapper.BusinessTicketOrderMapper;
import com.zhjs.scfcloud.model.util.JdzpRespUtil;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 自动撤销支付过期的订单在京东智票平台的支付订单
 * @author: dailongting
 * @date:2019/7/19 11:48
 */
public class AutoRevokeOrderPayJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(AutoRevokeOrderPayJob.class);

    @Resource
    private BusinessTicketOrderMapper orderMapper;
    @Resource
    private JDZPUtil jdzpUtil;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("===========>任务开始执行-自动撤销支付过期或支付撤销失败的订单在京东智票平台的支付订单");
        Date sysDate = new Date();
        List<BusinessTicketOrder> orderList = orderMapper.findPayOverdueOrderList(sysDate);
        if(orderList == null || orderList.isEmpty()){
            logger.info("===========>无需要撤销的过期订单（支付过期）");
            return;
        }

        orderList.forEach(order -> {
            //京东智票订单操作接口，2代表撤单操作
            CommonResponse<HandleOrderRespBody> resp = jdzpUtil.handleOrder(order.getPlatformReqNo(),2);
            Result result = JdzpRespUtil.analyzeResponseJDZP(resp);
            if(result.getCode() != Result.RESULT_CODE_SUCCESS){
                logger.error("撤销支付超时订单错误，订单ID：{}，订单号：{}，平台请求流水号：{}，错误原因：{}",order.getId(),order.getOrderNo(),order.getPlatformReqNo(),result.getMessage());
                return;
            }

            order.setPayStatus(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status97.getStatus());
            order.setUpdateTime(sysDate);
            order.setUpdateBy(1L);
            orderMapper.updateById(order);
        });
        logger.info("===========>任务执行结束-自动撤销支付过期的订单在京东智票平台的支付订单");
    }
}
