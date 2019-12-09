package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorAddDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorListDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorUpdateDTO;
import com.zhjs.scfcloud.model.entity.Acceptor;

/**
 * 承兑方
 */
public interface AcceptorService extends IService<Acceptor> {

    //查询承兑方列表
    Result findList(AcceptorListDTO dto);

    //新增承兑方
    Result add(AcceptorAddDTO dto);

    //删除承兑方
    Result delete(BaseDeleteDTO dto);

    //修改承兑方
    Result update(AcceptorUpdateDTO dto);

    //承兑方详情
    Result detail(Long id, Long companyId);

    //修改承兑方限制
    Result updateLimit(BaseSingleUpdateDTO dto);

    //查询当前是否限制承兑方
    Result isLimit(Long companyId);
}
