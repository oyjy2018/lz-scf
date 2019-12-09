package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectFundChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-04 15:56
 */
public interface LzProjectFundChangeMapper extends BaseMapper<LzProjectFundChange> {
    List<LzProjectFundChange> findProjectAsset(@Param("contractNo")String contractNo, @Param("days")List<String> list);
}
