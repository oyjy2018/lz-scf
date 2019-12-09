package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.vo.DeptTreeVOO;
import com.zhjs.scfcloud.model.vo.DeptVO;
import com.zhjs.scfcloud.system.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门(组织架构)管理控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:29
 *
 * @author liuchanghai
 * @create 2019-05-17 16:29
 * @since
 */

@Api(tags = "部门(组织架构)管理")
@RestController
public class DepartmentController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("新建部门")
    @PostMapping("/dept/add")
    public Result add(@RequestBody AddDeptDTO dto) {
        logger.info("新建部门 " + dto.toString());
        boolean result = departmentService.add(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("是否存在部门")
    @PostMapping("/dept/isExist")
    public Result isExist(@Valid @RequestBody IsExistDTO dto) {
        logger.info("是否存在部门" + dto.toString());
        boolean result = departmentService.isExist(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("编辑部门")
    @PostMapping("/dept/edit")
    public Result edit(@RequestBody EditDeptDTO dto) {
        logger.info(dto.toString());
        boolean result = departmentService.edit(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除部门")
    @PostMapping("/dept/delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        logger.info(dto.toString());
        boolean result = departmentService.removeById(dto.getId());
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("查询部门管理列表")
    @PostMapping("/dept/find/list")
    public Result findList(@RequestBody DeptListDTO dto) {
        logger.info(dto.toString());
        List<DeptVO> list = departmentService.findList(dto.getCompanyId());
        return Result.success(list);
    }

    @ApiOperation("查询部门下拉列表")
    @PostMapping("/dept/find/select/list")
    public Result findSelectList(@RequestBody CompanyIdDTO dto) {
        logger.info(dto.toString());
        List<DeptTreeVOO> list = departmentService.findSelectListByCompanyId(dto.getCompanyId());
        return Result.success(list);
    }

    @ApiOperation("查询用户所加入公司的部门")
    @PostMapping("/dept/find/user/list")
    public Result findUserDeptList(@RequestBody UserIdAndCompanyIdDTO dto) {
        logger.info(dto.toString());
        List<DeptVO> list = departmentService.findUserDeptList(dto.getUserId(), dto.getCompanyId());
        return Result.success(list);
    }

    @ApiOperation("根据用户查询查询部门树")
    @PostMapping("/dept/find/tree")
    public Result findTree(@RequestParam Long userId) {
        logger.info(userId.toString());
        List<DeptTreeVOO> list = departmentService.findtree(userId);
        return Result.success(list);
    }

    @ApiOperation("查询公司部门成员结构树")
    @GetMapping("/dept/user")
    public Result findDeptUserTree(@RequestParam(required = true) Long companyId) {
        return departmentService.findDeptUserTree(companyId);
    }

}
