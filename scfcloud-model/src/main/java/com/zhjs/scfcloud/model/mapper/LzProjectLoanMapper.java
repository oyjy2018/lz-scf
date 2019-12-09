package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectLoan;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectLoanListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-04 16:05
 */
public interface LzProjectLoanMapper extends BaseMapper<LzProjectLoan> {
    //借款列表
    List<LzProjectLoanListVO> getLoanList(@Param("contractNo") String contractNo, @Param("isPersonal") String isPersonal);
}