package com.zhjs.scfcloud.model.vo.TicketPlatform;

import lombok.Data;

import java.util.List;

/**
 *  首页VO
 * @author weijie.chen
 */
@Data
public class IndexVO {

    /**
     * 成交统计
     */
    private DealCountVO dealCount;
    /**
     * 询价单列表
     */
    private List<IndexInquireVO> inquireList;

}
