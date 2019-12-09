package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 部门管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-28 10:59
 *
 * @author liuchanghai
 * @create 2019-05-28 10:59
 * @since
 */

@FeignClient(value = "scfcloud-system", contextId = "dept")
public interface DepartmentFeignService {

    @ApiOperation("查询部门树")
    @PostMapping("/dept/find/tree")
    Result findTreeByUserId(@RequestParam Long userId);

    @ApiOperation("新建部门")
    @PostMapping("/dept/add")
    Result addDept(@RequestBody AddDeptDTO dto);

    @ApiOperation("新建部门")
    @PostMapping("/dept/isExist")
    Result isExist(@RequestBody IsExistDTO dto);

    @ApiOperation("删除部门")
    @PostMapping("/dept/delete")
    Result deleteDept(@RequestBody BaseIdDTO dto);

    @ApiOperation("编辑部门")
    @PostMapping("/dept/edit")
    Result editDept(@RequestBody EditDeptDTO dto);

    @ApiOperation("查询用户加入的公司部门")
    @PostMapping("/dept/find/user/list")
    Result findUserDeptList(@RequestBody UserIdAndCompanyIdDTO dto);

    @ApiOperation("查询部门下拉列表")
    @PostMapping("/dept/find/select/list")
    Result findSelectList(@RequestBody CompanyIdDTO dto);

    @ApiOperation("查询公司部门成员结构树")
    @GetMapping("/dept/user")
    Result findDeptUserTree(@RequestParam(required = true) Long companyId);

}
