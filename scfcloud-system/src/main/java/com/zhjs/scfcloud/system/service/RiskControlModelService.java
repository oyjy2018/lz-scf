package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelInsertDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelListDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelUpdateDTO;
import com.zhjs.scfcloud.model.entity.RiskControlModel;

/**
 * 风控模型
 */

public interface RiskControlModelService extends IService<RiskControlModel> {

    Result list(RiskControlModelListDTO dto);

    Result updateHasEnabled(BaseSingleUpdateDTO dto);

    Result delete(BaseDeleteDTO dto);

    Result insert(RiskControlModelInsertDTO dto);

    Result update(RiskControlModelUpdateDTO dto);

    Result updateModelFormula(BaseSingleUpdateDTO dto);

    Result detail(Long id);

    Result getModelFormula(Long id);
}
