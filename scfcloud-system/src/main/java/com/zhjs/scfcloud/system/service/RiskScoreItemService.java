package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.RiskScoreItems;
import com.zhjs.scfcloud.model.vo.ScoreItemEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RiskScoreItemService {
    /**
     * 获取评分项变量
     * @param controlModelId
     * @return
     */
    public Map<String, Object> getScoreItemVariable(Long controlModelId);

    /**
     * 评分项列表
     * @return
     */
    public List<RiskScoreItems> findList(Long controlModelId);

    /**
     * 评分项详情
     * @param riskScoreItemId
     * @return
     */
    public Result findDetail(Long riskScoreItemId);

    /**
     * 添加评分项
     * @param scoreItemVO
     */
    public Result addScoreItem(ScoreItemVO scoreItemVO);

    /**
     * 修改评分项
     * @param editVO
     */
    public Result updateScoreItem(ScoreItemEditVO editVO);

    /**
     * 修改满分值
     * @param riskScoreItemId
     */
    public Result updateFullMarkById(BigDecimal fullMark, Long riskScoreItemId);

    /**
     * 逻辑删除评分项
     * @param riskScoreItemId
     * @return
     */
    public Result delelteScoreById(Long riskScoreItemId);
}
