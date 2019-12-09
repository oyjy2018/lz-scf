package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddRoleDTO;
import com.zhjs.scfcloud.model.dto.EditRoleDTO;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.vo.RoleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 角色数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 14:18
 *
 * @author liuchanghai
 * @create 2019-05-20 14:18
 * @since
 */

@Mapper(componentModel = "spring")
public interface RoleTransfer {
    
    Role addDto2Po(AddRoleDTO dto);

    Role editDto2Po(EditRoleDTO dto);

    List<RoleVO> roles2RoleVO(List<Role> list);

    RoleVO toRoleVO(Role role);
}
