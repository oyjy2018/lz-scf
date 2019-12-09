package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordBalanceQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordImportDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordMyListQueryDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.exception.ScfRuntimeException;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.CreditRecordTransfer;
import com.zhjs.scfcloud.model.vo.credit.*;
import com.zhjs.scfcloud.system.service.CreditRecordService;
import com.zhjs.scfcloud.system.service.CreditTicketApplyService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.zhjs.scfcloud.util.util.ExcelUtil.readExcel;

/**
 * 授信申请的业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 16:00
 *
 * @author liuchanghai
 * @create 2019-06-11 16:00
 * @since
 */

@Service
public class CreditRecordServiceImpl extends ServiceImpl<CreditRecordMapper, CreditRecord> implements CreditRecordService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CreditRecordMapper creditRecordMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private CreditRecordTransfer creditRecordTransfer;
    @Value("${creditRecord.errorTemplate}")
    private String errorTemplate;
    @Value("${creditRecord.temporaryTemplatePath}")
    private String temporaryTemplatePath;
    @Value("${file-upload-url}")
    private String fileUploadUrl; //文件服务器上传地址
    @Resource
    private CreditTicketApplyMapper creditTicketApplyMapper;
    @Resource
    private CreditUseMapper creditUseMapper;
    @Resource
    private OperateLogMapper operateLogMapper;
    @Autowired
    private CreditTicketApplyService creditTicketApplyService;
    @Value("${api.partnerIds}")
    private String partnerIds; //文件服务器上传地址


    //我的授信列表
    @Override
    public String myList(CreditRecordMyListQueryDTO creditRecordMyListQueryDTO) {
        Map retMap = new HashMap();
        retMap.put("list",new ArrayList<>());
        retMap.put("total",0);
        //查询列表
        if ("web".equals(creditRecordMyListQueryDTO.getClient())) {
            List<CreditRecordMyListWebVO> creditRecordMyListWebVOList = creditRecordMapper.getMyListWebVOList(creditRecordMyListQueryDTO);
            if (creditRecordMyListWebVOList == null || creditRecordMyListWebVOList.isEmpty())
                return Result.success(retMap).toJSON();
            retMap.put("list",creditRecordMyListWebVOList);
            retMap.put("total",creditRecordMyListQueryDTO.getTotal());
        }else {
            List<CreditRecordMyListAppVO> creditRecordMyListAppVOList = creditRecordMapper.getMyListAppVOList(creditRecordMyListQueryDTO);
            if (creditRecordMyListAppVOList == null || creditRecordMyListAppVOList.isEmpty())
                return Result.success(retMap).toJSON();
            retMap.put("list",creditRecordMyListAppVOList);
            retMap.put("total",creditRecordMyListQueryDTO.getTotal());
        }
        return Result.success(retMap).toSerializerJSON();
    }

    //所有授信
    @Override
    public String allList(CreditRecordAllListQueryDTO creditRecordAllListQueryDTO) {
        Map retMap = new HashMap();
        retMap.put("list",new ArrayList<>());
        retMap.put("total",0);
        //查询列表
        List<CreditRecordAllListWebVO> creditRecordAllListWebVOList = creditRecordMapper.getAllListVOList(creditRecordAllListQueryDTO);
        if (creditRecordAllListWebVOList == null || creditRecordAllListWebVOList.isEmpty())
            return Result.success(retMap).toJSON();
        retMap.put("list",creditRecordAllListWebVOList);
        retMap.put("total",creditRecordAllListQueryDTO.getTotal());
        return Result.success(retMap).toSerializerJSON();
    }

    @Override
    public List<PersonalCreditVO> findPersonalCreditList(String idCard, Long companyId) {
        return creditRecordMapper.findPersonalCreditList(idCard,companyId);
    }

    @Override
    public PersonalCreditVO findPersonalCredit(Long creditRecordId) {
        return creditRecordMapper.findPersonalCredit(creditRecordId);
    }

    /**
     * 导入授信
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Result importRecord(CreditRecordImportDTO dto) {
        //读取文件内容
        List<Map<Integer,String>> mapList = null;
        try {
            mapList = readExcel(dto.getFileUrl());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("解析文件异常");
        }
        if (ListUtil.isEmpty(mapList)||mapList.size() <= 1) {
            return Result.failure("导入数据为空");
        }
        if (mapList.size() > 101) {
            return Result.failure("导入数据不能超过100条");
        }

        List<CreditRecordImportVO> correctList = new ArrayList(); //正确的部分
        List<CreditRecordImportVO> errorList = new ArrayList(); //错误的部分

        //导入数据校验
        importDataValid(mapList,correctList,errorList,dto.getCompanyId());
        for (CreditRecordImportVO creditRecordImportVO : correctList) {
            //客户是否存在
            Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("id_card",creditRecordImportVO.getIdCard()).last("limit 1"));
            if (customer == null) { //不存在时插入
                customer = new Customer();
                customer.setIdCard(creditRecordImportVO.getIdCard());
                customer.setCustomerName(creditRecordImportVO.getCustomerName());
                //获取其他信息
                Map<String,String> map = StringUtil.getBirAgeSex(creditRecordImportVO.getIdCard());
                customer.setAge(Integer.parseInt(map.get("age")));
                customer.setGender(map.get("sex"));
                if (customerMapper.insert(customer) != 1){
                    throw new ScfRuntimeException("添加客户失败");
                }
            }
            //插入授信
            CreditRecord record = creditRecordTransfer.toCreditRecord(creditRecordImportVO);
            record.setCreditStart(DateUtil.parse(creditRecordImportVO.getCreditStartDate(),"yyyy-MM-dd"));
            record.setCreditEnd(DateUtil.parse(creditRecordImportVO.getCreditEndDate(),"yyyy-MM-dd"));
            record.setCompanyId(dto.getCompanyId());
            record.setIfImport(CommonEnum.WhetherEnum.YES.getStatus());
            record.setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus());
            record.setCreateBy(dto.getUserId()).setCreateTime(new Date());
            if (creditRecordMapper.insert(record) != 1){
                throw new ScfRuntimeException("添加授信失败");
            };
        }

        String errorMsgDownloadUrl = null;
        //导出错误部分
        if (!ListUtil.isEmpty(errorList)) {
            Workbook workbook = ExcelUtil.getWorkbook(errorTemplate);
            // 获得工作表
            Sheet sheet = workbook.getSheetAt(0);
            //填充内容
            fillErrorExcel(errorList,sheet);
            FileOutputStream out = null;
            try {
                File file = new File(temporaryTemplatePath+"导入授信错误记录_"+DateUtil.getTodayDateStr("yyyy-MM-dd")+".xls");
                out = new FileOutputStream(file);
                workbook.write(out);
                out.close();
                out.flush();

                //将文件上传到文件服务
                String result = HttpUtil.uploadFile(file,fileUploadUrl+"?directory=creditRecord/error","file");
                Result res = JsonUtil.jsonToBean(result,Result.class);
                if (res.getCode() == Result.RESULT_CODE_FAILURE) return res;
                Map data = (Map) res.getData();
                errorMsgDownloadUrl = FileUtil.getDownloadFileUrl((String) data.get("fileUrl"));
                //删除文件
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Map retMap = new HashMap();
        retMap.put("errorMsgDownloadUrl",errorMsgDownloadUrl);
        retMap.put("errorCount",errorList.size());
        return Result.success(retMap);
    }

    /**
     * 将错误信息写到excel中
     * @param errorList
     * @param sheet
     */
    private void fillErrorExcel(List<CreditRecordImportVO> errorList, Sheet sheet) {
        for (int i = 0; i < errorList.size() ; i++) {
            CreditRecordImportVO vo = errorList.get(i);
            Row row = sheet.getRow(i+1);
            if (row == null) {
                row = sheet.createRow(i+1);
            }
            Cell cell = row.getCell(0);
            if (cell == null) {
                cell =  row.createCell(0);
            }
            cell.setCellValue(vo.getCustomerName());

            cell = row.getCell(1);
            if (cell == null) {
                cell =  row.createCell(1);
            }
            cell.setCellValue(vo.getIdCard());

            cell = row.getCell(2);
            if (cell == null) {
                cell =  row.createCell(2);
            }
            cell.setCellValue(vo.getProjectName());

            cell = row.getCell(3);
            if (cell == null) {
                cell =  row.createCell(3);
            }
            cell.setCellValue(vo.getRelateProjectId());

            cell = row.getCell(4);
            if (cell == null) {
                cell =  row.createCell(4);
            }
            cell.setCellValue(vo.getCreditAmount());

            cell = row.getCell(5);
            if (cell == null) {
                cell =  row.createCell(5);
            }
            cell.setCellValue(vo.getCreditStartDate());

            cell = row.getCell(6);
            if (cell == null) {
                cell =  row.createCell(6);
            }
            cell.setCellValue(vo.getCreditEndDate());

            cell = row.getCell(7);
            if (cell == null) {
                cell =  row.createCell(7);
            }
            cell.setCellValue(vo.getErrorMessage());
        }
    }

    /**
     * 校验导入的数据
     * @param mapList
     * @param correctList
     * @param errorList
     * @param companyId
     */
    private void importDataValid(List<Map<Integer, String>> mapList, List<CreditRecordImportVO> correctList, List<CreditRecordImportVO> errorList, Long companyId) {
        //循环判断  因为需要逐个判断 所以有点耗时
        for (int i = 1; i < mapList.size(); i++) {
            Map<Integer,String> map = mapList.get(i);
            CreditRecordImportVO vo = toCreditRecordImportVO(map);
            //空数据不处理
            if (vo.equals(new CreditRecordImportVO())) continue;
            //必填校验
            if (StringUtil.isEmpty(vo.getCustomerName())) {
                vo.setErrorMessage("被授信人姓名必填");
                errorList.add(vo);
                continue;
            }
            if (StringUtil.isEmpty(vo.getIdCard())) {
                vo.setErrorMessage("被授信人身份证号必填");
                errorList.add(vo);
                continue;
            }
            if (StringUtil.isEmpty(vo.getProjectName())) {
                vo.setErrorMessage("授信项目名称必填");
                errorList.add(vo);
                continue;
            }
            if (StringUtil.isEmpty(vo.getCreditAmount())) {
                vo.setErrorMessage("授信金额必填");
                errorList.add(vo);
                continue;
            }
            if (StringUtil.isEmpty(vo.getCreditStartDate())) {
                vo.setErrorMessage("授信开始日必填");
                errorList.add(vo);
                continue;
            }
            if (StringUtil.isEmpty(vo.getCreditEndDate())) {
                vo.setErrorMessage("授信截止日必填");
                errorList.add(vo);
                continue;
            }
            //金额格式校验
            if (!StringUtil.isDigit(vo.getCreditAmount())) {
                vo.setErrorMessage("授信金额 格式错误，需输入数字");
                errorList.add(vo);
                continue;
            }
            //身份证格式校验
            if (!StringUtil.identityCodeValid(vo.getIdCard())){
                vo.setErrorMessage("被授信人身份证号格式错误");
                errorList.add(vo);
                continue;
            }
            //日期格式校验
            if (DateUtil.parse(vo.getCreditStartDate(),"yyyy-MM-dd") == null) {
                vo.setErrorMessage("授信开始日格式错误，需输入日期格式");
                errorList.add(vo);
                continue;
            }
            if (DateUtil.parse(vo.getCreditEndDate(),"yyyy-MM-dd") == null) {
                vo.setErrorMessage("授信截止日格式错误，需输入日期格式");
                errorList.add(vo);
                continue;
            }
            //截止日大于开始日
            long creditEndTime = DateUtil.parse(vo.getCreditEndDate(),"yyyy-MM-dd").getTime();
            long creditStartTime = DateUtil.parse(vo.getCreditStartDate(),"yyyy-MM-dd").getTime();
            if ( creditStartTime > creditEndTime) {
                vo.setErrorMessage("授信截止日必须大于授信开始日");
                errorList.add(vo);
                continue;
            }

            //是否与导入文件中已验证通过的授信有冲突
            if (correctList.stream().filter(
                    importRecordVO->
                            (vo.getProjectName().equals(importRecordVO.getProjectName())  //项目名重复
                                    || (!StringUtil.isEmpty(vo.getRelateProjectId()) && vo.getRelateProjectId().equals(importRecordVO.getRelateProjectId()))) //或者关联项目id重复
                                    && ((creditEndTime >DateUtil.parse(importRecordVO.getCreditEndDate(),"yyyy-MM-dd").getTime()  //时间冲突
                                    && creditStartTime <= DateUtil.parse(importRecordVO.getCreditEndDate(),"yyyy-MM-dd").getTime())
                                    ||(creditEndTime >DateUtil.parse(importRecordVO.getCreditStartDate(),"yyyy-MM-dd").getTime()
                                    && creditStartTime <= DateUtil.parse(importRecordVO.getCreditStartDate(),"yyyy-MM-dd").getTime()))
            ).collect(Collectors.toList()).size() > 0 ) {
                vo.setErrorMessage("导入文件已存在同样的项目且授信时间区间重合的授信记录");
                errorList.add(vo);
                continue;
            }
            //数据库是否存在同样的项目且授信时间区间重合的授信记录
            if (creditRecordMapper.getMixedRecordCount(vo.getProjectName(),vo.getRelateProjectId(),vo.getCreditStartDate(),vo.getCreditEndDate(),companyId) > 0) {
                vo.setErrorMessage("系统已存在同样的项目且授信时间区间重合的授信记录");
                errorList.add(vo);
                continue;
            }

            //通过所有检验
            correctList.add(vo);
        }
    }

    //将文件读取map转为vo
    private CreditRecordImportVO toCreditRecordImportVO(Map<Integer, String> map) {
        CreditRecordImportVO vo = new CreditRecordImportVO();
        vo.setCustomerName(map.get(0));
        vo.setIdCard(map.get(1));
        vo.setProjectName(map.get(2));
        vo.setRelateProjectId(map.get(3));
        vo.setCreditAmount(map.get(4));
        vo.setCreditStartDate(map.get(5));
        vo.setCreditEndDate(map.get(6));
        return vo;
    }

    /**
     * 删除授信
     * @param dto
     * @return
     */
    @Override
    public Result deleteRecord(BaseDeleteDTO dto) {
        //获取授信
        CreditRecord record = creditRecordMapper.selectById(dto.getId());
        if (record == null) return Result.failure("授信数据不存在");

        if (record.getDeleteStatus().intValue() == CommonEnum.WhetherEnum.YES.getStatus().intValue()) {
            return Result.failure("授信数据已经删除");
        }
        if (record.getIfImport().intValue() != CommonEnum.WhetherEnum.YES.getStatus().intValue()) {
            return Result.failure("只有人工导入的授信可用删除");
        }
        //查询是否关联用信申请
        LambdaQueryWrapper<CreditTicketApply> ticketApplyWrapper = new QueryWrapper<CreditTicketApply>().lambda();
        ticketApplyWrapper.eq(CreditTicketApply::getCreditId,dto.getId());
        List<CreditTicketApply>  creditTicketApplyList = creditTicketApplyMapper.selectList(ticketApplyWrapper);
        if ( !ListUtil.isEmpty(creditTicketApplyList) ) {
            String ticketApplyIds = creditTicketApplyList.stream().map(creditTicketApply -> creditTicketApply.getId().toString()).collect(Collectors.joining("、"));
            return Result.failure("该授信已关联用信申请，用信申请id："+ticketApplyIds);
        }
        //查询是否关联用信记录
        LambdaQueryWrapper<CreditUse> useWrapper = new QueryWrapper<CreditUse>().lambda();
        useWrapper.eq(CreditUse::getCreditId,dto.getId());
        List<CreditUse> creditUseList = creditUseMapper.selectList(useWrapper);
        if (!ListUtil.isEmpty(creditUseList)) {
            String useIds = creditUseList.stream().map(creditUse -> creditUse.getId().toString()).collect(Collectors.joining("、"));
            return Result.failure("该授信已关联用信记录，用信记录id："+useIds);
        }
        //修改状态
        record.setDeleteStatus(CommonEnum.WhetherEnum.YES.getStatus());
        if (creditRecordMapper.updateById(record) != 1) {
            return Result.failure("删除失败");
        }
        //记录操作日志
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateTime(new Date())
                .setCompanyId(dto.getCompanyId())
                .setOperatObject("record")
                .setBusinessId(record.getId())
                .setOperatType("delete")
                .setOperateContent("删除授信")
                .setOperateUserId(dto.getUserId())
                .setOperateUserName(dto.getUserName());
        operateLogMapper.insert(operateLog);

        return Result.success();
    }

    /**
     * 获取项目授信余额
     * @param dto
     * @return
     */
    @Override
    public Result findCreditBalance(CreditRecordBalanceQueryDTO dto) {
        //检验是否为合作伙伴
        Optional optional = Arrays.asList(partnerIds.split(",")).stream().filter(s -> dto.getPartnerId().equals(s)).findAny();
        if (!optional.isPresent()) return Result.failure("非合作方");

        //根据工程云项目查询授信信息
        LambdaQueryWrapper<CreditRecord> recordWrapper = new QueryWrapper<CreditRecord>().lambda();
        recordWrapper.eq(CreditRecord::getRelateProjectId,dto.getProjectId());
        recordWrapper.le(CreditRecord::getCreditStart,DateUtil.getTodayDateStr("yyyy-MM-dd"));
        recordWrapper.ge(CreditRecord::getCreditEnd,DateUtil.getTodayDateStr("yyyy-MM-dd"));
        recordWrapper.last("limit 1");
        CreditRecord record = creditRecordMapper.selectOne(recordWrapper);
        if (record == null) return Result.failure("此项目无授信");
        //判断项目商务经理是否匹配
        if (!dto.getManagerName().equals(record.getCustomerName())) return Result.failure("项目和商务经理不匹配");
        //计算剩余额度
        Result result = creditTicketApplyService.getRemainCreditAmount(record);
        Map retMap = (Map) result.getData(); //已返回剩余额度
        retMap.put("creditStart",DateUtil.format(record.getCreditStart()));
        retMap.put("creditEnd",DateUtil.format(record.getCreditEnd()));
        retMap.put("projectId",dto.getProjectId());
        retMap.put("partnerId",dto.getPartnerId());

        return Result.success(retMap);
    }

    @Override
    public Result findBalance(Long id) {
        CreditRecord record = creditRecordMapper.selectById(id);
        if (record == null) return Result.failure("授信数据不存在");

        //计算剩余额度
        Result result = creditTicketApplyService.getRemainCreditAmount(record);
        Map retMap = (Map) result.getData(); //已返回剩余额度

        return Result.success(retMap);
    }

}
