package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.dto.AddMenuDTO;
import com.zhjs.scfcloud.model.dto.EditMenuDTO;
import com.zhjs.scfcloud.model.entity.Menu;

/**
 * 菜单管理的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:14
 *
 * @author liuchanghai
 * @create 2019-05-17 16:14
 * @since
 */
public interface MenuService extends IService<Menu> {

    boolean add(AddMenuDTO dto);

    boolean edit(EditMenuDTO dto);
}
