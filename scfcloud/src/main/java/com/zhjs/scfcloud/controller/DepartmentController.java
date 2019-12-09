package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.DepartmentFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.util.MySubjectUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 部门管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-28 10:57
 *
 * @author liuchanghai
 * @create 2019-05-28 10:57
 * @since
 */
@RestController
public class DepartmentController {

    private Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentFeignService departmentFeignService;

    @ApiOperation("新建部门")
    @PostMapping("/dept/add")
    @RequiresPermissions("common:department:add")
    public Result add(@RequestBody AddDeptDTO dto) {
        return departmentFeignService.addDept(dto);
    }

    @ApiOperation("部门是否存在")
    @PostMapping("/dept/isExist")
    public Result isExist(@RequestBody IsExistDTO dto) {
        return departmentFeignService.isExist(dto);
    }

    @ApiOperation("编辑部门")
    @PostMapping("/dept/edit")
    @RequiresPermissions("common:department:edit")
    public Result edit(@RequestBody EditDeptDTO dto) {
        return departmentFeignService.editDept(dto);
    }

    @ApiOperation("删除部门")
    @PostMapping("/dept/delete")
    @RequiresPermissions("common:department:delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        return departmentFeignService.deleteDept(dto);
    }

    @ApiOperation("查询部门树")
    @PostMapping("/dept/find/tree")
    @RequiresPermissions("common:department:tree")
    public Result findTree() {
        // 获取userId
        Long userId = MySubjectUtil.getUserId();
        logger.info(userId.toString());
        return departmentFeignService.findTreeByUserId(userId);
    }

    @ApiOperation("查询用户加入的公司部门")
    @PostMapping("/dept/find/user/list")
    public Result findUserDeptList(@RequestBody UserIdAndCompanyIdDTO dto) {
        // 获取userId
        dto.setUserId(MySubjectUtil.getUserId());
        return departmentFeignService.findUserDeptList(dto);
    }

    @ApiOperation("查询部门下拉列表")
    @PostMapping("/dept/find/select/list")
    public Result findSelectList(@RequestBody CompanyIdDTO dto) {
        return departmentFeignService.findSelectList(dto);
    }

    @ApiOperation("查询公司部门成员结构树")
    @GetMapping("/dept/user")
    @RequiresPermissions("common:department:tree")
    public Result findDeptUserTree(@RequestParam(required = true) Long companyId) {
        return departmentFeignService.findDeptUserTree(companyId);
    }
}
