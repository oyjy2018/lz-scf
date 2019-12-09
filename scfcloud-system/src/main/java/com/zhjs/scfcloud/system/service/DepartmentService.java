package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AddDeptDTO;
import com.zhjs.scfcloud.model.dto.EditDeptDTO;
import com.zhjs.scfcloud.model.dto.IsExistDTO;
import com.zhjs.scfcloud.model.entity.Department;
import com.zhjs.scfcloud.model.vo.DeptTreeVOO;
import com.zhjs.scfcloud.model.vo.DeptVO;

import java.util.List;

/**
 * 部门(组织架构)管理的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:34
 *
 * @author liuchanghai
 * @create 2019-05-17 16:34
 * @since
 */
public interface DepartmentService extends IService<Department> {

    boolean add(AddDeptDTO dto);

    List<DeptVO> findList(Long companyId);

    List<DeptVO> findUserDeptList(Long userId, Long companyId);

    boolean edit(EditDeptDTO dto);

    boolean isExist(IsExistDTO dto);

    List<DeptTreeVOO> findtree(Long userId);

    /**
     * 查询部门下拉列表
     * @param companyId 入参 公司ID
     * @return
     */
    List<DeptTreeVOO> findSelectListByCompanyId(Long companyId);

    /**
     * 根据公司ID查询公司部门成员树
     * @param companyId 入参 公司ID
     * @return
     */
    Result findDeptUserTree(Long companyId);
}
