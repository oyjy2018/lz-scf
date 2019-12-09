package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.CreditItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 授信项目信息 Mapper 接口
 * @author:liuchanghai
 * @date:2019-07-06 11:43
 */
public interface CreditItemMapper extends BaseMapper<CreditItem> {
    /**
     * 根据授信业务ID查询项目信息
     * @param creditApplyId
     * @return
     */
    List<Map<String, Object>> selectByCreditApplyId(@Param("creditApplyId") Long creditApplyId);

    List<CreditItem> selectListByApplyIds(@Param("applyIds") String applyIds);
}