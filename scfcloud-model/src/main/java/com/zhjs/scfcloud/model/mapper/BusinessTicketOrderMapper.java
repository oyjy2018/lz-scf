package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.QueryOrderListDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketOrder;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderAllListVO;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderDetailsVO;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderListVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商票订单表 Mapper 接口
 * @author:weijie.chen
 * @date:2019-07-15 10:03
 */
public interface BusinessTicketOrderMapper extends BaseMapper<BusinessTicketOrder> {
    /**
     * 查询订单列表
     * @param dto
     * @return
     */
    List<OrderListVO> findOrderList(QueryOrderListDTO dto);

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    OrderDetailsVO findOrderDetails(Long orderId);

    /**
     * 查询支付过期的订单
     * @return
     */
    List<BusinessTicketOrder> findPayOverdueOrderList(@Param("sysDate") Date sysDate);

    /**
     * 获取所有商票订单
     * @param dto
     * @return
     */
    List<OrderAllListVO> getAllOrderList(BusinessTicketOrderAllListDTO dto);
}