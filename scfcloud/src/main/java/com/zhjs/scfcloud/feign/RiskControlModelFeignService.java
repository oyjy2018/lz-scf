package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelInsertDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelListDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelUpdateDTO;
import com.zhjs.scfcloud.model.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dailongting
 * @date:2019/5/21 16:24
 */
@FeignClient(value = "scfcloud-system", contextId = "risk/model")
public interface RiskControlModelFeignService {

    @PostMapping("/risk/model/list")
    Result list(@RequestBody RiskControlModelListDTO dto);

    @PostMapping("/risk/model/updateHasEnabled")
    Result updateHasEnabled(@RequestBody BaseSingleUpdateDTO dto);

    @PostMapping("/risk/model/delete")
    Result delete(@RequestBody BaseDeleteDTO dto);

    @PostMapping("/risk/model/insert")
    Result insert(@RequestBody RiskControlModelInsertDTO dto);

    @PostMapping("/risk/model/update")
    Result update(@RequestBody RiskControlModelUpdateDTO dto);

    @PostMapping("/risk/model/updateModelFormula")
    Result updateModelFormula(@RequestBody BaseSingleUpdateDTO dto);

    @PostMapping("/risk/model/detail")
    Result detail(@RequestParam("id") Long id);

    @PostMapping("/risk/model/getModelFormula")
    Result getModelFormula(@RequestParam("id") Long id);
}
