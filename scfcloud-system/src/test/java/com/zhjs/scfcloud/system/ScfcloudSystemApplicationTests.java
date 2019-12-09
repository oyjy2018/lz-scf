package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.mapper.DepartmentMapper;
import com.zhjs.scfcloud.model.vo.DeptTreeVOO;
import com.zhjs.scfcloud.system.service.BusinessAttrValService;
import com.zhjs.scfcloud.system.service.impl.CreditApplyServiceImpl;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.RedisUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScfcloudSystemApplicationTests {

    @Autowired
    private EmailTool emailTool;

    @Test
    public void contextLoads() {
        // 测试邮件
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("to", new String[]{"942059473@qq.com"});
        valueMap.put("title", "【领筑金融云】");
        valueMap.put("company", "领筑数字(深圳)有限公司");
        valueMap.put("name", "领筑");
        valueMap.put("email", "942059473@qq.com");
        valueMap.put("id","10000");
        emailTool.sendMailTemplate(valueMap);
    }

    @Resource
    private DepartmentMapper departmentMapper;
    @Test
    public void tree() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<DeptTreeVOO> deptTreeVO = departmentMapper.selectTreeList(ids);
        System.out.println(deptTreeVO.toString());
    }

    @Test
    public void resultMap() {
        List<Map> depts =  departmentMapper.selectByCompanyId(1L);
        List<Map> users =  departmentMapper.selectDepartmentUserByCompanyId(1L);
        System.out.println(depts.toString());
        System.out.println(users.toString());
    }


    @Autowired
    private BusinessAttrValService businessAttrValService;
    @Test
    public void findBusinessAttrVal() {
        Result attrId = businessAttrValService.findByTypeIdOrAttrId(null);
        System.out.println(attrId.toString());
    }

    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void getRedisDict() {

        System.out.println(redisUtil.hget("redis:data:dictionary_details:","concrete"));
    }

    /**
     * 测试时间差计算
     */
    @Test
    public void getDatePoor() {
        System.out.println(DateUtil.getDatePoor(new Date(),new Date()));
    }

    /**
     * 测试时间差计算
     */
    @Test
    public void getDatePoor2() {
        String aaa = "1,23,34";
        String[] permissionOrgIds = aaa.split(",");
        List<Long> ids = new ArrayList<>();
        for (String id: permissionOrgIds){
            if(!StringUtil.isEmpty(id)){
                ids.add(Long.parseLong(id));
            }
        }
        System.out.println(ids.toString());
    }


    @Autowired
    private CreditApplyServiceImpl creditApplyServiceImpl;
    /**
     * 测试时间差计算
     */
    @Test
    public void getCreditApplyService() {
        Long businessId = 1L;
        Long businessTypeId = 1L;
        Long companyId = 1L;
        Long projectId = 1L;
        String columnName = "creditItem_auditCreditMoney";
        String columnValue = "4006666";
        String result = creditApplyServiceImpl.updateCreditApplyFormData(businessId, companyId, businessTypeId, projectId,
                columnName, columnValue);
        System.out.println(result);
    }


}
