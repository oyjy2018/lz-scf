package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectBudget;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectCostDetailsVO;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-10-31 11:20
 */
public interface LzProjectBudgetMapper extends BaseMapper<LzProjectBudget> {

    /**
     * 查询项目成本费用明细
     * @param contractNo
     * @return
     */
    List<LzProjectCostDetailsVO> findCostDetails(String contractNo);
}
