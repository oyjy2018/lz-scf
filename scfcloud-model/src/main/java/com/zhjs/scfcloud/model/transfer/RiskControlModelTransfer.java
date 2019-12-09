package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.risk.RiskControlModelInsertDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelUpdateDTO;
import com.zhjs.scfcloud.model.entity.RiskControlModel;
import org.mapstruct.Mapper;

/**
 * 风控模型转换
 */

@Mapper(componentModel = "spring")
public interface RiskControlModelTransfer {

   RiskControlModel toRiskControlModel(RiskControlModelInsertDTO dto);

   RiskControlModel toRiskControlModel(RiskControlModelUpdateDTO dto);



}
