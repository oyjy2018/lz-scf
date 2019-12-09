package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.dto.AddMenuDTO;
import com.zhjs.scfcloud.model.dto.EditMenuDTO;
import com.zhjs.scfcloud.model.entity.Menu;
import com.zhjs.scfcloud.model.mapper.MenuMapper;
import com.zhjs.scfcloud.system.service.MenuService;
import com.zhjs.scfcloud.model.transfer.MenuTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单管理的业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:14
 *
 * @author liuchanghai
 * @create 2019-05-17 16:14
 * @since
 */

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuTransfer menuTransfer;

    /**
     * 新建菜单
     * @param dto 入参
     * @return
     */
    @Override
    public boolean add(AddMenuDTO dto) {
        Menu menu = menuTransfer.addMenuDto2Po(dto);
        return save(menu);
    }

    /**
     * 编辑菜单
     * @param dto
     * @return
     */
    @Override
    public boolean edit(EditMenuDTO dto) {
        Menu menu = menuTransfer.editDto2Po(dto);
        System.out.println(menu);
        return updateById(menu);
    }
}
