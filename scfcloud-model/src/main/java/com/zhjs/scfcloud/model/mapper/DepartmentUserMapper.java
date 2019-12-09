package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.DepartmentUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门与用户关联关系信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 08:59
 *
 * @author liuchanghai
 * @create 2019-05-22 08:59
 * @since
 */
public interface DepartmentUserMapper extends BaseMapper<DepartmentUser> {

    /**
     * 查询用户加入的部门
     * @param userId 入参 用户ID
     * @param companyId 入参 公司ID
     * @return
     */
    List<Long> findDeptIdList(Long userId, Long companyId);

    /**
     * 部门删除用户
     * @param id 入参 用户ID
     * @param companyId 入参 公司ID
     * @return
     */
    boolean deleteDepartmentUserByUserIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

}
