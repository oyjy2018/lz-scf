package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditTicketListWebDTO;
import com.zhjs.scfcloud.model.entity.CreditTicket;
import com.zhjs.scfcloud.model.vo.credit.CreditTicketDetailWebVO;
import com.zhjs.scfcloud.model.vo.credit.CreditTicketListWebVO;

import java.util.List;

/**
 * 商票表 Mapper 接口
 * @author:liuchanghai
 * @date:2019-07-01 15:09
 */
public interface CreditTicketMapper extends BaseMapper<CreditTicket> {

    List<CreditTicketListWebVO> getTicketWebList(CreditTicketListWebDTO dto);

    CreditTicketDetailWebVO getTicketDetail(BaseIdDTO dto);
}