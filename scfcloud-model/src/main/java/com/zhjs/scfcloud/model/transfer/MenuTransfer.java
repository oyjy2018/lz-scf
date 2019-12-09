package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddMenuDTO;
import com.zhjs.scfcloud.model.dto.EditMenuDTO;
import com.zhjs.scfcloud.model.entity.Menu;
import org.mapstruct.Mapper;

/**
 * 菜单数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 14:18
 *
 * @author liuchanghai
 * @create 2019-05-20 14:18
 * @since
 */

@Mapper(componentModel = "spring")
public interface MenuTransfer {

    Menu addMenuDto2Po(AddMenuDTO dto);

    Menu editDto2Po(EditMenuDTO dto);
}
