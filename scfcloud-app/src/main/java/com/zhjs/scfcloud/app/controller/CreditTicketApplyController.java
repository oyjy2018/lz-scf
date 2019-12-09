package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.service.CreditTicketApplyService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.ticket.CreditTicketApplyCommitDTO;
import com.zhjs.scfcloud.model.dto.ticket.CreditTicketDraftSaveDTO;
import com.zhjs.scfcloud.model.dto.ticket.CreditTicketFileSaveDTO;
import com.zhjs.scfcloud.model.dto.ticket.QueryCreditTicketCfgDTO;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: dailongting
 * @date:2019/6/28 17:01
 */
@RestController
@RequestMapping("/creditTicketApply")
public class CreditTicketApplyController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreditTicketApplyService creditTicketApplyService;

    @ApiOperation("查询开票申请流程相关信息")
    @PostMapping("/findCreditTicketCfg")
    public String findCreditTicketCfg(@RequestBody QueryCreditTicketCfgDTO dto){
        logger.info("查询开票申请流程相关信息:{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.findCreditTicketCfg(dto).toJSON();
    }

    @ApiOperation("保存开票申请字段值")
    @PostMapping("/saveDraft")
    public String saveDraft(@RequestBody CreditTicketDraftSaveDTO dto){
        logger.info("保存开票申请字段值:{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.saveDraft(dto).toJSON();
    }

    @ApiOperation("开票申请存文件")
    @PostMapping("/saveFile")
    public String saveFile(@RequestBody @Valid CreditTicketFileSaveDTO dto, BindingResult bindingResult){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","授信申请存文件", JsonUtil.toJSON(dto));
        if (dto == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditTicketApplyService.saveFile(dto).toJSON();
    }

    /**
     * 开票申请提交
     * @param dto
     * @param bindingResult
     * @return
     */
    @PostMapping("/applyCommit")
    public String applyCommit(@RequestBody CreditTicketApplyCommitDTO dto, BindingResult bindingResult){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","授信申请存文件", JsonUtil.toJSON(dto));
        if (dto == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditTicketApplyService.applyCommit(dto).toJSON();
    }
}
