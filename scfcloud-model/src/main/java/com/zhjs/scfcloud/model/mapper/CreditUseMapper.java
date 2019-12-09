package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.credit.CreditUseDetailWebDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseMyListAppDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseMyListWebDTO;
import com.zhjs.scfcloud.model.entity.CreditUse;
import com.zhjs.scfcloud.model.vo.credit.CreditUseDetail4TicketWebVO;
import com.zhjs.scfcloud.model.vo.credit.CreditUseMyListAppVO;
import com.zhjs.scfcloud.model.vo.credit.CreditUseMyListWebVO;

import java.util.List;

/**
 * 用信表 Mapper 接口
 * @author:liuchanghai
 * @date:2019-07-01 10:09
 */
public interface CreditUseMapper extends BaseMapper<CreditUse> {
    int deleteByPrimaryKey(Long id);

    int insert(CreditUse record);

    int insertSelective(CreditUse record);

    CreditUse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CreditUse record);

    int updateByPrimaryKey(CreditUse record);

    List<CreditUseMyListAppVO> getMyListAppVOList(CreditUseMyListAppDTO creditUseMyListAppDTO);

    List<CreditUseMyListWebVO> getMyListWebVOList(CreditUseMyListWebDTO dto);

    List<CreditUseMyListWebVO> getAllListWebVOList(CreditUseMyListWebDTO dto);

    CreditUseDetail4TicketWebVO getWebDetail(CreditUseDetailWebDTO dto);
}