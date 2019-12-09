package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorListDTO;
import com.zhjs.scfcloud.model.entity.Acceptor;
import com.zhjs.scfcloud.model.vo.credit.acceptor.AcceptorListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-09-18 13:51
 */
public interface AcceptorMapper extends BaseMapper<Acceptor> {
    //查询承兑方列表
    List<AcceptorListVO> findList(AcceptorListDTO dto);

    //承兑方详情
    AcceptorListVO getDetail(@Param("id") Long id, @Param("companyId")Long companyId);

    //查询承兑方余额
    AcceptorListVO getRemainLimitMoney(@Param("companyId") Long companyId, @Param("acceptorName") String acceptorName);
}