package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AddDeptDTO;
import com.zhjs.scfcloud.model.dto.EditDeptDTO;
import com.zhjs.scfcloud.model.dto.IsExistDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.CompanyMapper;
import com.zhjs.scfcloud.model.mapper.DepartmentMapper;
import com.zhjs.scfcloud.model.mapper.UserMapper;
import com.zhjs.scfcloud.model.transfer.DeptTransfer;
import com.zhjs.scfcloud.model.vo.DepaetmentUserVO;
import com.zhjs.scfcloud.model.vo.DeptTreeVOO;
import com.zhjs.scfcloud.model.vo.DeptUserTreeVO;
import com.zhjs.scfcloud.model.vo.DeptVO;
import com.zhjs.scfcloud.system.service.CompanyCommonService;
import com.zhjs.scfcloud.system.service.CompanyUserService;
import com.zhjs.scfcloud.system.service.DepartmentService;
import com.zhjs.scfcloud.system.service.DepartmentUserService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyStatusEnum;
import com.zhjs.scfcloud.util.enums.CompanyUserEnum;
import com.zhjs.scfcloud.util.enums.UserEnum;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门(组织架构)管理的业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:35
 *
 * @author liuchanghai
 * @create 2019-05-17 16:35
 * @since
 */

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DeptTransfer deptTransfer;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CompanyCommonService companyCommonService;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private DepartmentUserService departmentUserService;

    /**
     * 新建部门
     * @param dto 入参
     * @return
     */
    @Override
    public boolean add(AddDeptDTO dto) {
        Department dept = deptTransfer.addDTO2Po(dto);
        if(dept.getParentId() == null){
            dept.setParentId(0L);
        }
        return save(dept);
    }

    /**
     * 查询部门列表
     * @param companyId 入参
     * @return
     */
    @Override
    public List<DeptVO> findList(Long companyId) {
        QueryWrapper<Department> where = new QueryWrapper<Department>().eq("company_id", companyId);
        List<Department> list = list(where);
        List<DeptVO> result = deptTransfer.depts2DeptVoList(list);
        return result;
    }

    /** 获取用户加入的部门 */
    @Override
    public List<DeptVO> findUserDeptList(Long userId, Long companyId) {
        List<Department> depts =  departmentMapper.findDepartmentListByUserIdAndCompanyId(userId, companyId);
        return deptTransfer.depts2DeptVoList(depts);
    }

    /**
     * 编辑部门
     * @param dto 入参
     * @return
     */
    @Override
    public boolean edit(EditDeptDTO dto) {
        Department dept = deptTransfer.editDeptDto2Po(dto);
        return updateById(dept);
    }

    /**
     * 部门是否存在
     * @param dto
     * @return
     */
    @Override
    public boolean isExist(IsExistDTO dto) {
        LambdaQueryWrapper<Department> lambda = new QueryWrapper<Department>().lambda();
        lambda.eq(Department::getCompanyId, dto.getId());
        lambda.eq(Department::getDeptName, dto.getName());
        List<Department> list = list(lambda);
        return list.size() > 0 ? true : false;
    }

    /**
     * 根据用户ID 查询公司部门结构树
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<DeptTreeVOO> findtree(Long userId) {
        User user = userMapper.selectById(userId);
        if(user == null){
            return null;
        }
        Long[] companyIds = null;
        if(user.getPermissionType() == null
                || user.getPermissionType() == UserEnum.PermissionType.PermissionType1.getStatus()
                || StringUtil.isEmpty(user.getPermissionOrgIds())){
            List<Company> list = companyCommonService.selectCompanyListByUserId(userId);
            companyIds = list.stream().map(company -> company.getId()).collect(Collectors.toList()).toArray(new Long[list.size()]);
        }else{
            List<String> list = Arrays.asList(user.getPermissionOrgIds().split(","));
            companyIds = list.stream().map(s -> Long.parseLong(s)).collect(Collectors.toList()).toArray(new Long[list.size()]);
        }
        //排除无效公司
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.ne("status", CompanyStatusEnum.status2.getValue());
        companyQueryWrapper.eq("is_delete", CommonEnum.WhetherEnum.NO.getStatus());
        companyQueryWrapper.in("id",companyIds);
        List<Company> list = companyMapper.selectList(companyQueryWrapper);
        List<Long> ids = new ArrayList<>();
        for (Company item: list){
            ids.add(item.getId());
        }
        if(ids.size() > 0){
            return departmentMapper.selectTreeList(ids);
        }
        return null;
    }

    /**
     * 查询部门下拉列表
     * @param companyId 入参 公司ID
     * @return
     */
    @Override
    public List<DeptTreeVOO> findSelectListByCompanyId(Long companyId) {
        List<DeptTreeVOO> deptTree = departmentMapper.selectTree(companyId);
        return deptTree;
    }

    /**
     * 根据公司ID查询公司部门成员树
     * @param companyId 入参 公司ID
     * @return
     */
    @Override
    public Result findDeptUserTree(Long companyId) {
        DeptUserTreeVO firstTree = new DeptUserTreeVO();
        //获取所有公司成员
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        //在职 或 待激活
        List<Integer> companyUserStatusList = new ArrayList<>(2);
        Collections.addAll(companyUserStatusList, CompanyUserEnum.status_0.getValue(),CompanyUserEnum.status_1.getValue());

        companyUserQueryWrapper.eq("company_id",companyId)
                .eq("is_delete", CommonEnum.WhetherEnum.NO.getStatus())
                .in("status",companyUserStatusList);
        List<CompanyUser> companyUserList = companyUserService.list(companyUserQueryWrapper);
        //符合条件的用户ID集合
        List<Long> userIds = companyUserList.stream().map(companyUser -> companyUser.getUserId()).collect(Collectors.toList());

        QueryWrapper<Department> firstDepartmentQueryWrapper = new QueryWrapper<>();
        firstDepartmentQueryWrapper.eq("company_id",companyId)
                .eq("dept_level",0);
        Department department = getOne(firstDepartmentQueryWrapper);
        //初始化顶层部门树
        firstTree.setDeptId(department.getId());
        firstTree.setDeptName(department.getDeptName());
        //初始化顶层部门树 - 用户
        List<DepaetmentUserVO> departmentUsers = departmentMapper.findUserListByDeptId(department.getId(),userIds);
        //补充未加入部门的员工
        userIds.forEach(userId -> {
            QueryWrapper<DepartmentUser> departmentUserQueryWrapper = new QueryWrapper<>();
            departmentUserQueryWrapper.eq("user_id",userId);
            if(departmentUserService.count(departmentUserQueryWrapper) == 0){
                User user = userMapper.selectById(userId);
                DepaetmentUserVO depaetmentUserVO = new DepaetmentUserVO();
                depaetmentUserVO.setUserId(user.getId());
                depaetmentUserVO.setUserName(user.getUserName());
                depaetmentUserVO.setUserEmail(user.getEmail());
                departmentUsers.add(depaetmentUserVO);
            }
        });
        firstTree.setUserList(departmentUsers);
        //

        //判断是否还有下层部门
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq("parent_id",department.getId());
        List<Department> departmentList = list(departmentQueryWrapper);
        if(!departmentList.isEmpty()){
            //递归 添加 所有层级
            List<DeptUserTreeVO> childrenList = new ArrayList<>(departmentList.size());
            departmentList.forEach(department1 -> {
                DeptUserTreeVO children = recursiveMethod(userIds,department1.getId());
                childrenList.add(children);
            });
            firstTree.setChildrenList(childrenList);
        }

        return Result.success(firstTree);

    }

    /**
     * 递归方法
     * @param userIds
     * @param departmentId
     * @return
     */
    private DeptUserTreeVO recursiveMethod(List<Long> userIds,Long departmentId){
        DeptUserTreeVO result = new DeptUserTreeVO();

        Department department = departmentMapper.selectById(departmentId);
        result.setDeptName(department.getDeptName());
        result.setDeptId(department.getId());
        //初始化用户
        result.setUserList(departmentMapper.findUserListByDeptId(department.getId(),userIds));
        //判断是否还有下层部门
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq("parent_id",department.getId());
        List<Department> departmentList = list(departmentQueryWrapper);
        if(departmentList.isEmpty()){
            return result;
        }else{
            List<DeptUserTreeVO> childrenList = new ArrayList<>(departmentList.size());
            departmentList.forEach(department1 -> {
                DeptUserTreeVO children = recursiveMethod(userIds,department1.getId());
                childrenList.add(children);
            });
            result.setChildrenList(childrenList);
        }
        return result;
    }
}
