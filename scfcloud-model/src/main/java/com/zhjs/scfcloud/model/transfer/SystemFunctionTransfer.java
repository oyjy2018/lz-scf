package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddSystemFunctionDTO;
import com.zhjs.scfcloud.model.dto.EditSystemFunctionDTO;
import com.zhjs.scfcloud.model.entity.SystemFunction;
import com.zhjs.scfcloud.model.vo.SystemFunctionVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统功能数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 16:49
 *
 * @author liuchanghai
 * @create 2019-05-20 16:49
 * @since
 */

@Mapper(componentModel = "spring")
public interface SystemFunctionTransfer {
    
    SystemFunction addDto2Po(AddSystemFunctionDTO dto);

    SystemFunction editDto2Po(EditSystemFunctionDTO dto);

    List<SystemFunctionVO> systemFunctions2VoList(List<SystemFunction> list);
}
