package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddSystemVDTO;
import com.zhjs.scfcloud.model.entity.SystemVersion;
import com.zhjs.scfcloud.model.dto.EditSystemVDTO;
import com.zhjs.scfcloud.model.vo.SystemVVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 系统版本数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 11:31
 *
 * @author liuchanghai
 * @create 2019-05-23 11:31
 * @since
 */

@Mapper(componentModel = "spring")
public interface SystemVersionTransfer {
    
    List<SystemVVO> systemVersions2SystemVVoList(List<SystemVersion> list);

    SystemVersion addSystemVDto2Po(AddSystemVDTO dto);

    SystemVersion editSystemVDto2Po(EditSystemVDTO dto);
}
