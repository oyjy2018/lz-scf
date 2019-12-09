package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.SystemVersion;
import com.zhjs.scfcloud.model.mapper.SystemVersionMapper;
import com.zhjs.scfcloud.model.dto.AddSystemVDTO;
import com.zhjs.scfcloud.model.dto.EditSystemVDTO;
import com.zhjs.scfcloud.system.service.SystemVersionService;
import com.zhjs.scfcloud.model.transfer.SystemVersionTransfer;
import com.zhjs.scfcloud.model.vo.SystemVVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统版本管理业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 11:06
 *
 * @author liuchanghai
 * @create 2019-05-23 11:06
 * @since
 */

@Service
public class SystemVersionServiceImpl extends ServiceImpl<SystemVersionMapper, SystemVersion> implements SystemVersionService {

    @Autowired
    private SystemVersionTransfer systemVersionTransfer;

    @Override
    public boolean add(AddSystemVDTO dto) {
        SystemVersion systemVersion = systemVersionTransfer.addSystemVDto2Po(dto);
        return save(systemVersion);
    }

    @Override
    public boolean edit(EditSystemVDTO dto) {
        SystemVersion systemVersion = systemVersionTransfer.editSystemVDto2Po(dto);
        return updateById(systemVersion);
    }

    @Override
    public List<SystemVVO> findList() {
        List<SystemVersion> list = list(new QueryWrapper<SystemVersion>());
        List<SystemVVO> results = systemVersionTransfer.systemVersions2SystemVVoList(list);
        return results;
    }
}
