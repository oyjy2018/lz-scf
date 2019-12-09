package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelListDTO;
import com.zhjs.scfcloud.model.entity.RiskControlModel;
import com.zhjs.scfcloud.model.vo.risk.RiskControlModelListVO;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-10-15 16:02
 */
public interface RiskControlModelMapper extends BaseMapper<RiskControlModel> {

    //查询模型列表
    List<RiskControlModelListVO> list(RiskControlModelListDTO dto);
}