package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.Department;
import com.zhjs.scfcloud.model.vo.DepaetmentUserVO;
import com.zhjs.scfcloud.model.vo.DeptTreeVOO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公司部门(组织架构)信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:16
 *
 * @author liuchanghai
 * @create 2019-05-17 14:16
 * @since
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 查询用户加入的部门
     * @param userId 入参 用户ID
     * @param companyId 入参 公司ID
     * @return
     */
    List<Department> findDepartmentListByUserIdAndCompanyId(Long userId, Long companyId);

    /**
     * 查询部门成员信息
     * @param deptId 部门ID
     * @return
     */
    List<DepaetmentUserVO> findUserListByDeptId(Long deptId,List<Long> userIds);

    /**
     *
     * @param companyId
     * @return
     */
    List<DeptTreeVOO> selectTree(@Param("companyId") Long companyId);

    /**
     * 多公司部门树
     * @param companyIds 公司ID
     * @return
     */
    List<DeptTreeVOO> selectTreeList(@Param("companyIds") List<Long> companyIds);

    /**
     * 查询公司部门
     * @param companyId 公司ID
     * @return
     */
    List<Map> selectByCompanyId(@Param("companyId") Long companyId);

    /**
     * 查询部门成员
     * @param companyId 公司ID
     * @return
     */
    List<Map> selectDepartmentUserByCompanyId(@Param("companyId") Long companyId);

}
