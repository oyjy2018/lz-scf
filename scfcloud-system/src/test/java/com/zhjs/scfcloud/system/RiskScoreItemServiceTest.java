package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.vo.ScoreItemEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemFormulaEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemFormulaVO;
import com.zhjs.scfcloud.model.vo.ScoreItemVO;
import com.zhjs.scfcloud.system.service.RiskScoreItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RiskScoreItemServiceTest {
    @Autowired
    RiskScoreItemService riskScoreItemService;
    @Test
    public void getScoreItemVariable(){
        Map<String,Object> map =  riskScoreItemService.getScoreItemVariable(1L);
        System.out.println(map);
    }

    @Test
    public void items(){
        System.out.println(riskScoreItemService.findList(1L));
    }

    @Test
    public void detail(){
        System.out.println(riskScoreItemService.findDetail(1L));
    }

    @Test
    public void add(){
        ScoreItemVO scoreItemVO = new ScoreItemVO();

        scoreItemVO.setRiskControlModelId(1L);
        scoreItemVO.setItemName("测试评分项名称");
        scoreItemVO.setFullMark(new BigDecimal(20));
//        scoreItemVO.setFormula("测试公式");
        scoreItemVO.setItemExplain("测试评分项说明");

        ScoreItemFormulaVO f1 = new ScoreItemFormulaVO();
        f1.setCriteriaType(1);
        f1.setCriteriaJsons("测试条件1");
        f1.setFormula("测试公式1");
        ScoreItemFormulaVO f2 = new ScoreItemFormulaVO();
        f2.setCriteriaType(2);
        f2.setCriteriaJsons("测试条件2");
        f2.setFormula("测试公式2");
        List<ScoreItemFormulaVO> formulaVOS = new ArrayList<>();
        Collections.addAll(formulaVOS, f1, f2);

        scoreItemVO.setFormulaList(formulaVOS);
        riskScoreItemService.addScoreItem(scoreItemVO);
    }

    @Test
    public void update() {
        ScoreItemEditVO editVO = new ScoreItemEditVO();
        editVO.setScoreItemId(1L);
        editVO.setItemName("测试修改评分项名称");
        editVO.setFullMark(new BigDecimal(10));
//        editVO.setFormula("测试修改公式");
        editVO.setItemExplain("测试修改评分项说明");

        ScoreItemFormulaEditVO formulaVO1 = new ScoreItemFormulaEditVO();
        formulaVO1.setCriteriaType(3);
        formulaVO1.setCriteriaJsons("测试修改条件1");
        formulaVO1.setFormula("测试修改公式1");

        ScoreItemFormulaEditVO formulaVO2 = new ScoreItemFormulaEditVO();
        formulaVO2.setCriteriaType(2);
        formulaVO2.setCriteriaJsons("测试修改条件2");
        formulaVO2.setFormula("测试修改公式2");

        ScoreItemFormulaEditVO formulaVO3 = new ScoreItemFormulaEditVO();
        formulaVO3.setCriteriaType(3);
        formulaVO3.setCriteriaJsons("测试修改条件3");
        formulaVO3.setFormula("测试修改公式3");

        List<ScoreItemFormulaEditVO> formulaEditVOList = new ArrayList<>();
        Collections.addAll(formulaEditVOList, formulaVO1, formulaVO2, formulaVO3);

        editVO.setFormulaList(formulaEditVOList);
        riskScoreItemService.updateScoreItem(editVO);
    }

    @Test
    public void updateFullMark(){
        riskScoreItemService.updateFullMarkById(new BigDecimal(15), 1L);
    }

    @Test
    public void delelteScore(){
        riskScoreItemService.delelteScoreById(1L);
    }

}
