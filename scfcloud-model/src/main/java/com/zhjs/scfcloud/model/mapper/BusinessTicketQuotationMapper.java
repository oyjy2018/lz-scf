package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.entity.BusinessTicketQuotation;
import com.zhjs.scfcloud.model.vo.QuotationOwnerVO;
import com.zhjs.scfcloud.model.vo.QuotationTicketVO;
import com.zhjs.scfcloud.model.vo.businessTicket.AllQuotationTicketVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商票报价 Mapper 接口
 * @author:weijie.chen
 * @date:2019-07-15 19:06
 */
public interface BusinessTicketQuotationMapper extends BaseMapper<BusinessTicketQuotation> {
    /**
     * 票方-报价单列表
     * @param page
     * @param quotationStatus
     * @param quotationCompanyName
     * @param quotationCreateStartTime
     * @param quotationCreateEndTime
     * @param quotationId
     * @return
     */
    List<QuotationTicketVO> selectQuotationTicketList(Page<QuotationTicketVO> page,
                                                      @Param("inquireCompanyId") Long inquireCompanyId,
                                                      @Param("quotationStatus") Integer quotationStatus,
                                                      @Param("quotationCompanyName") String quotationCompanyName,
                                                      @Param("quotationCreateStartTime") Date quotationCreateStartTime,
                                                      @Param("quotationCreateEndTime") Date quotationCreateEndTime,
                                                      @Param("quotationId") Long quotationId,
                                                      @Param("inquireId") Long inquireId);

    /**
     * 票方-报价单列表
     * @param page
     * @param quotationCompanyId
     * @param quotationStatus
     * @param inquireCompanyName
     * @param quotationCreateStartTime
     * @param quotationCreateEndTime
     * @param quotationId
     * @return
     */
    List<QuotationOwnerVO> selectQuotationOwnerList(Page<QuotationOwnerVO> page,
                                                    @Param("quotationCompanyId") Long quotationCompanyId,
                                                    @Param("quotationStatus") Integer quotationStatus,
                                                    @Param("inquireCompanyName") String inquireCompanyName,
                                                    @Param("quotationCreateStartTime") Date quotationCreateStartTime,
                                                    @Param("quotationCreateEndTime") Date quotationCreateEndTime,
                                                    @Param("quotationId") Long quotationId);

    /**
     * 所有报价单列表
     * @param page
     * @param quotationStatus
     * @param quotationCompanyName
     * @param quotationCreateStartTime
     * @param quotationCreateEndTime
     * @param quotationId
     * @return
     */
    List<AllQuotationTicketVO> selectAllQuotationsList(Page<AllQuotationTicketVO> page,
                                                       @Param("quotationStatus") Integer quotationStatus,
                                                       @Param("quotationCompanyName") String quotationCompanyName,
                                                       @Param("quotationCreateStartTime") Date quotationCreateStartTime,
                                                       @Param("quotationCreateEndTime") Date quotationCreateEndTime,
                                                       @Param("quotationId") Long quotationId,
                                                       @Param("companyIds") String companyIds);
}
