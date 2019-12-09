package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.dto.SystemFunctionListDTO;
import com.zhjs.scfcloud.model.entity.SystemFunction;
import com.zhjs.scfcloud.model.vo.MenuVO;
import com.zhjs.scfcloud.model.vo.SystemFunctionVO;

import java.util.List;

/**
 * 系统功能业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 11:05
 *
 * @author liuchanghai
 * @create 2019-05-20 11:05
 * @since
 */
public interface SystemFunctionService extends IService<SystemFunction> {

    /**
     * 查询系统版本-功能列表 (树)
     * @param companyId
     * @return
     */
    List<MenuVO> findSystemFunctions(Long systemId,Long companyId);

    List<SystemFunctionVO> findList(SystemFunctionListDTO dto);
}
