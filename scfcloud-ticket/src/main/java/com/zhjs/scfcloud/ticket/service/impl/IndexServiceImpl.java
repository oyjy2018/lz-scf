package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import com.zhjs.scfcloud.model.entity.BusinessTicketOrder;
import com.zhjs.scfcloud.model.mapper.BusinessTicketInquireMapper;
import com.zhjs.scfcloud.model.mapper.BusinessTicketOrderMapper;
import com.zhjs.scfcloud.model.vo.TicketPlatform.DealCountVO;
import com.zhjs.scfcloud.model.vo.TicketPlatform.IndexInquireVO;
import com.zhjs.scfcloud.model.vo.TicketPlatform.IndexVO;
import com.zhjs.scfcloud.ticket.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    BusinessTicketOrderMapper businessTicketOrderMapper;
    @Autowired
    BusinessTicketInquireMapper businessTicketInquireMapper;

    @Override
    public IndexVO index() {
        IndexVO indexVO = new IndexVO();

        //成交信息统计
        DealCountVO dealCountVO = new DealCountVO();

        QueryWrapper<BusinessTicketOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_status",99);
        //条件待补充
        List<BusinessTicketOrder> orderList = businessTicketOrderMapper.selectList(orderQueryWrapper);
        //所有成交信息统计
        BigDecimal countAmount = new BigDecimal(0);
        for(BusinessTicketOrder element : orderList){
            countAmount = countAmount.add(element.getAmounts());
        }
        dealCountVO.setDealAmount(countAmount.divide(new BigDecimal(10000),2, BigDecimal.ROUND_HALF_UP));
        dealCountVO.setDealCount(orderList.size());
        //本月成交信息统计
        Map<String,Object> monthMap = filterToList(orderList,
                LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(),
                LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1).atStartOfDay());
        dealCountVO.setDealCountMonth((Integer) monthMap.get("count"));
        dealCountVO.setDealAmountMonth(((BigDecimal)monthMap.get("amount")).divide(new BigDecimal(10000),2, BigDecimal.ROUND_HALF_UP));
        //今日成交信息统计
        Map<String,Object> dayMap = filterToList(orderList,LocalDate.now().atStartOfDay(),LocalDate.now().plusDays(1).atStartOfDay());
        dealCountVO.setDealCountDay((Integer) dayMap.get("count"));
        dealCountVO.setDealAmountDay(((BigDecimal)dayMap.get("amount")).divide(new BigDecimal(10000),2, BigDecimal.ROUND_HALF_UP));

        indexVO.setDealCount(dealCountVO);

        //商票询价单列表
        QueryWrapper<BusinessTicketInquire> inquireQueryWrapper = new QueryWrapper<>();
        List<Integer> statusList = new ArrayList<>(2);
        Collections.addAll(statusList,1,2);

        inquireQueryWrapper.in("status", statusList);
        inquireQueryWrapper.isNull("assign_buyer_company_id"); //没有指定买家的
        inquireQueryWrapper.orderByDesc("create_time");
        IPage<BusinessTicketInquire> page = businessTicketInquireMapper.selectPage(new Page<>(1,3),inquireQueryWrapper);
        if(page.getSize() > 0){
            List<IndexInquireVO> inquireList = new ArrayList<>(3);
            for(BusinessTicketInquire source : page.getRecords()){
                IndexInquireVO target = new IndexInquireVO();
                BeanUtils.copyProperties(source,target);
                target.setInquireId(source.getId());

                inquireList.add(target);
            }

            indexVO.setInquireList(inquireList);
        }

        return indexVO;
    }
    private Map filterToList(List<BusinessTicketOrder> target, LocalDateTime starDate,LocalDateTime endDate){
        Map<String,Object> map = new HashMap<>();
        List<BusinessTicketOrder> list = target.stream().filter(
                businessTicketOrder -> businessTicketOrder.getDealTime().isAfter(starDate) &&
                        businessTicketOrder.getDealTime().isBefore(endDate)).collect(Collectors.toList());
        BigDecimal amount = new BigDecimal(0);
        for(BusinessTicketOrder element : list){
            amount = amount.add(element.getAmounts());
        }
        map.put("count",list.size());
        map.put("amount",amount);
        return map;
    }
}
