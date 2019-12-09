package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckCompanyNameDTO;
import com.zhjs.scfcloud.model.dto.CheckDeptNameDTO;
import com.zhjs.scfcloud.model.dto.NatureSelectListDTO;
import com.zhjs.scfcloud.model.vo.DictKeyValueListVO;
import com.zhjs.scfcloud.model.vo.SystemVVO;
import com.zhjs.scfcloud.system.service.DictionaryService;
import com.zhjs.scfcloud.system.service.SystemVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Api接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:51
 *
 * @author liuchanghai
 * @create 2019-05-17 14:51
 * @since
 */

@Api(tags = "Api接口")
@RestController
@RequestMapping("api")
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private SystemVersionService systemVersionService;

    @ApiOperation("检查公司公司名是否存在")
    @PostMapping("checkCompanyName")
    public Result checkCompanyName(@RequestBody CheckCompanyNameDTO dto) {
        return Result.success();
    }

    @ApiOperation("检查部门名称是否存在")
    @PostMapping("checkDeptName")
    public Result checkDeptName(@RequestBody CheckDeptNameDTO dto) {
        return Result.success();
    }

    @ApiOperation("选择行类")
    @PostMapping("nature/select")
    public Result natureSelectList(@RequestBody NatureSelectListDTO dto) {
        List<DictKeyValueListVO> result = dictionaryService.natureSelectList(dto);
        return Result.success(result);
    }

    @ApiOperation("选择具体行业")
    @PostMapping("nature/d/list")
    public Result natureConcrete(@RequestBody CheckDeptNameDTO dto) {
        return Result.success();
    }

    @ApiOperation("公司人员规模")
    @PostMapping("staffsize/select")
    public Result<List<DictKeyValueListVO>> staffSizeSelectList() {
        List<DictKeyValueListVO> result = dictionaryService.staffSizeSelectList(null);
        return Result.success(result);
    }

    @ApiOperation("查询系统版本")
    @PostMapping("getSystemVersionList")
    public Result<List<SystemVVO>> getSystemVersionList() {
        List<SystemVVO> systemVVOList = systemVersionService.findList();
        return Result.success(systemVVOList);
    }
}
