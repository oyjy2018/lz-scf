package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectInsurance;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectInsuranceListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-13 14:08
 */
public interface LzProjectInsuranceMapper extends BaseMapper<LzProjectInsurance> {

    List<LzProjectInsuranceListVO> getList(@Param("contractNo") String contractNo);
}