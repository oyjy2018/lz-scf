package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketAllInquireDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketInquireListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketInquireMyAssignListDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import com.zhjs.scfcloud.model.vo.businessTicket.BusinessTicketInquireDetailVO;
import com.zhjs.scfcloud.model.vo.businessTicket.BusinessTicketInquireListVO;
import com.zhjs.scfcloud.model.vo.businessTicket.BusinessTicketInquireMyAssignListVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商票询价信息表 Mapper 接口
 * @author:weijie.chen
 * @date:2019-07-15 15:14
 */
public interface BusinessTicketInquireMapper extends BaseMapper<BusinessTicketInquire> {

    List<BusinessTicketInquireListVO> getInquireList(BusinessTicketInquireListDTO dto);

    BusinessTicketInquireDetailVO getInquireDetail(Long id);

    int updateOverdueInquireStatus(@Param("now") String now);

    int updateSurplusValidDaysBatch(List<BusinessTicketInquire> businessTicketInquireList);

    /**
     * 根据京东智票请求流水号查询询价（票据）信息
     * @param platformReqNo
     * @param orderNo
     * @return
     */
    BusinessTicketInquire findInquireByPlatformReqNo(@Param("platformReqNo")String platformReqNo, @Param("orderNo")String orderNo);

    int getInquireInActiveOrderCount(@Param("billNo") String billNo);

    List<BusinessTicketInquireMyAssignListVO> myAssignInquireList(BusinessTicketInquireMyAssignListDTO dto);

    //所有询价
    List<BusinessTicketInquireListVO> allInquire(BusinessTicketAllInquireDTO dto);
}