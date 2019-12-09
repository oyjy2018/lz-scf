package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessTicketOrderContractSign;

/**
 * 订单合同电签表 Mapper 接口
 * @author:dailongting
 * @date:2019-07-27 17:25
 */
public interface BusinessTicketOrderContractSignMapper extends BaseMapper<BusinessTicketOrderContractSign> {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessTicketOrderContractSign record);

    int insertSelective(BusinessTicketOrderContractSign record);

    BusinessTicketOrderContractSign selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusinessTicketOrderContractSign record);

    int updateByPrimaryKey(BusinessTicketOrderContractSign record);
}