package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.dto.SystemFunctionListDTO;
import com.zhjs.scfcloud.model.entity.CompanySystemVersion;
import com.zhjs.scfcloud.model.entity.SystemFunction;
import com.zhjs.scfcloud.model.mapper.CompanySystemVersionMapper;
import com.zhjs.scfcloud.model.mapper.SystemFunctionMapper;
import com.zhjs.scfcloud.model.mapper.SystemVersionFunctionMapper;
import com.zhjs.scfcloud.model.transfer.SystemFunctionTransfer;
import com.zhjs.scfcloud.model.vo.MenuVO;
import com.zhjs.scfcloud.model.vo.SystemFunctionVO;
import com.zhjs.scfcloud.system.service.SystemFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统功能业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 11:06
 *
 * @author liuchanghai
 * @create 2019-05-20 11:06
 * @since
 */

@Service
public class SystemFunctionServiceImpl extends ServiceImpl<SystemFunctionMapper, SystemFunction> implements SystemFunctionService {

    @Autowired
    private SystemFunctionTransfer systemFunctionTransfer;
    @Autowired
    private SystemVersionFunctionMapper systemVersionFunctionMapper;
    @Autowired
    private CompanySystemVersionMapper companySystemVersionMapper;

    @Override
    public List<MenuVO> findSystemFunctions(Long systemId,Long companyId) {
        //获取公司购买的系统版本
        QueryWrapper<CompanySystemVersion> companySystemVersionQueryWrapper = new QueryWrapper<>();
        companySystemVersionQueryWrapper.eq("company_id",companyId);
        if(systemId == 2){
            companySystemVersionQueryWrapper.in("system_version_id",2,4);
        }else{
            companySystemVersionQueryWrapper.notIn("system_version_id",2,4);
        }
        List<CompanySystemVersion> companySystemVersions = companySystemVersionMapper.selectList(companySystemVersionQueryWrapper);
        if(companySystemVersions.isEmpty()) return null;

        List<Long> versionIds = new ArrayList<>(companySystemVersions.size()+1);
        companySystemVersions.forEach(companySystemVersion -> {
            versionIds.add(companySystemVersion.getSystemVersionId());
        });

        //获取一级菜单列表
        List<MenuVO> firstMenus = systemVersionFunctionMapper.selectMenuList(0L,versionIds);

        if (firstMenus.size() == 0){
            return null;
        }
        //设置菜单列表及功能
        firstMenus.forEach(menuVO -> {
            //补充-公司管理二级菜单
            versionIds.add(4L);
            //查询二级菜单
            List<MenuVO> secondMenus = systemVersionFunctionMapper.selectMenuList(menuVO.getMenuId(),versionIds);
            //
            secondMenus.forEach(menuVO1 -> {
                //设置二级菜单下的功能
                menuVO1.setFunctionList(systemVersionFunctionMapper.selectFunctionList(menuVO1.getMenuId(),versionIds));
            });
            //设置二级菜单
            menuVO.setMenuList(secondMenus);
        });

        return firstMenus;
    }

    @Override
    public List<SystemFunctionVO> findList(SystemFunctionListDTO dto) {
        QueryWrapper<SystemFunction> where = new QueryWrapper<SystemFunction>();
        where.eq("system_version_id", dto.getId());
        List<SystemFunction> list = list(where);
        List<SystemFunctionVO> result = systemFunctionTransfer.systemFunctions2VoList(list);
        return result;
    }

}
