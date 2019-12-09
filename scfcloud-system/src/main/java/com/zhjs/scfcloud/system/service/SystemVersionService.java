package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.entity.SystemVersion;
import com.zhjs.scfcloud.model.dto.AddSystemVDTO;
import com.zhjs.scfcloud.model.dto.EditSystemVDTO;
import com.zhjs.scfcloud.model.vo.SystemVVO;

import java.util.List;

/**
 * 系统版本管理业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 11:05
 *
 * @author liuchanghai
 * @create 2019-05-23 11:05
 * @since
 */

public interface SystemVersionService extends IService<SystemVersion> {

    boolean add(AddSystemVDTO dto);

    boolean edit(EditSystemVDTO dto);

    List<SystemVVO> findList();
}
