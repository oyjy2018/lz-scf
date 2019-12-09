package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectLiabilities;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-06 16:23
 */
public interface LzProjectLiabilitiesMapper extends BaseMapper<LzProjectLiabilities> {

    List<LzProjectLiabilities> findLiabilities(@Param("contractNo")String contractNo, @Param("days")List<String> list);
}
