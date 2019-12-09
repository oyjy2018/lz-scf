package com.zhjs.scfcloud.model.vo.businessTicket;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhjs.scfcloud.model.entity.BusinessTicketFile;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.FileUtil;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 我的发布vo
 */
@Data
public class BusinessTicketInquireDetailVO {

    //主键id
    private Long id;


    //询价时间 create_time
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //发布截止时间 expiration_date
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;

    //报价剩余天数
//    private Integer remainQuotationDays;
//
//    public Integer getRemainQuotationDays() {
//        return DateUtil.getPoorDays(new Date(),expirationDate);
//    }

    //报价数量
    private Integer quotationCount;

    /**
     * 询价状态码(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
     * status
     */
    private Integer status;

    /**
     * 询价状态名称(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
     * status
     */
    private String statusCH;

    public String getStatusCH() {
        return BusinessTicketEnum.InquireStatus.getStatusCH(this.status);
    }


    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 票据号码
     * bill_no
     */
    private String billNo;

    /**
     * 票面金额
     * bill_amt
     */
    private BigDecimal billAmt;

    /**
     * 到期日
     * maturity_date
     */
    private Date maturityDate;

    public String getMaturityDate() {
        return DateUtil.format(maturityDate,"yyyy-MM-dd");
    }

    /**
     * 剩余天数（实时计算）
     */
    private Integer surplusValidDays;

    /**
     * 背书次数
     * endorsed_count
     */
    private Integer endorsedCount;

    /**
     * 票据瑕疵
     * flaws
     */
    private String flaws;

    //商票正面图片url
    private String ticketFrontUrl;

    public String getTicketFrontUrl() {
        return FileUtil.getViewFileUrl(ticketFrontUrl);
    }

    /**
     * 公司名称
     * company_name
     */
    private String companyName;

    //联系人
    private String contactPerson;

    //联系电话
    private String contactPhone;

    //指定资方姓名
    private String assignBuyerCompanyName;

    //文件列表
    List<BusinessTicketFile> fileList;

}