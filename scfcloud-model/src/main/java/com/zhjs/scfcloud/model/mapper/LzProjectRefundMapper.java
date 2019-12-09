package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectRefund;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectRefundListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-01 16:46
 */
public interface LzProjectRefundMapper extends BaseMapper<LzProjectRefund> {
    //还款信息
    List<LzProjectRefundListVO> getRefundList(@Param("loanNo") String loanNo);
}