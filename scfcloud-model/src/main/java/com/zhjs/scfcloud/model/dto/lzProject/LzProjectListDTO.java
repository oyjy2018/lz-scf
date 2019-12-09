package com.zhjs.scfcloud.model.dto.lzProject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 深华项目列表 入参
 */

@Data
public class LzProjectListDTO extends Page {

    //项目合同编号
    private String contractNo;

    //项目名称
    private String projectName;

    //项目经理
    private String businessManager;

    //项目状态
    private String projectStatus;

}
