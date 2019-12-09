package com.zhjs.scfcloud.task.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.entity.Dictionary;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.BusinessAttrTransfer;
import com.zhjs.scfcloud.model.transfer.RoleTransfer;
import com.zhjs.scfcloud.model.transfer.UserTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.util.RedisInitCfgUtil;
import com.zhjs.scfcloud.model.vo.RoleVO;
import com.zhjs.scfcloud.model.vo.UserVO;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowAttrExtendVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;
import com.zhjs.scfcloud.util.constant.RedisConstant;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.RedisUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 初始化redis
 */
@Component
public class InitRedis {
    private static final Logger logger = LoggerFactory.getLogger(InitRedis.class);
    @Resource
    private DictionaryMapper dictionaryMapper;
    @Resource
    private DictionaryDetailsMapper dictionaryDetailsMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private BusinessAttrValMapper businessAttrValMapper;
    @Resource
    private BusinessFeeFormulaMapper businessFeeFormulaMapper;
    @Resource
    private BusinessFlowMapper businessFlowMapper;
    @Resource
    private BusinessFlowInterfaceMapper businessFlowInterfaceMapper;
    @Resource
    private BusinessFlowPermitMapper businessFlowPermitMapper;
    @Resource
    private BusinessTypeMapper businessTypeMapper;
    @Resource
    private BusinessWorkFlowMapper businessWorkFlowMapper;
    @Resource
    private BusinessWorkFlowAttrMapper businessWorkFlowAttrMapper;
    @Resource
    private BusinessAttrMapper businessAttrMapper;
    @Resource
    private BusinessAttrTransfer businessAttrTransfer;
    @Resource
    private BusinessFileCfgMapper businessFileCfgMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BusinessWorkFlowFileMapper businessWorkFlowFileMapper;
    @Resource
    private RoleTransfer roleTransfer;
    @Resource
    private UserTransfer userTransfer;

    public void initRedis() {
        logger.info("初始化redis");
        //初始化字典表
        initDictionary();
        //初始化业务属性字段归类表
        //initBusinessAttrClassify();
        //初始化业务属性字段值配置
        initBusinessAttrVal();
        //初始化费用公式
        initBusinessFeeFormula();
        //初始化业务流程状态
        initBusinessFlow();
        // 初始化业务流程对接接口
//        initBusinessFlowInterface();
        // 初始化业务流程权限配置
        initBusinessFlowPermit();
        // 初始化业务类型
        initBusinessType();
        // 初始化业务流转流程
        initBusinessWorkFlow();
        // 初始化业务流程属性配置
        initBusinessWorkFlowAttr();
        // 初始化读取业务属性配置到redis
        initBusinessAttr();
        // 初始化业务流转流程以及流转条件和流转字段
        initBusinessWorkFlowExtend();
        // 初始化业务附件配置
        initBusinessFileCfg();
        // 初始化流程扭转文件配置
        initBusinessWorkFlowFileCfg();
    }

    //初始化业务流转流程以及流转条件和流转字段
    private void initBusinessWorkFlowExtend() {
        logger.info("初始化业务流转流程以及流转条件和流转字段");

        //先查询流转流程以及流转条件（两者一对一关系）
        List<BusinessWorkFlowExtendVO> businessWorkFlowExtendVOS = businessWorkFlowMapper.getBusinessWorkFlowAndPermitList();
        if (businessWorkFlowExtendVOS == null || businessWorkFlowExtendVOS.isEmpty())
            return;
        //获取下一步处理人(即状态流转后的处理人)
        businessWorkFlowExtendVOS.stream().forEach(businessWorkFlowExtendVO -> {
            //流转后流程code
            String afterFlowCode = businessWorkFlowExtendVO.getAfterFlow();
            //以 流转后流程code为当前流程的 流转
            List<BusinessWorkFlowExtendVO> nextList =businessWorkFlowExtendVOS.stream().filter(vo ->
                    vo.getBeforeFlow().equals(afterFlowCode) && businessWorkFlowExtendVO.getCompanyId().longValue() == vo.getCompanyId().longValue()).collect(Collectors.toList());
            businessWorkFlowExtendVO.setNextDisposeRoleIds(StringUtil.stringDistinct(nextList.stream().filter(vo -> !StringUtil.isEmpty(vo.getRoleIds())).map(vo -> vo.getRoleIds()).collect(Collectors.joining(",")), ","));
            businessWorkFlowExtendVO.setNextDisposeUserIds(StringUtil.stringDistinct(nextList.stream().filter(vo -> !StringUtil.isEmpty(vo.getUserIds())).map(vo -> vo.getUserIds()).collect(Collectors.joining(",")), ","));
        });

        //查询所有角色
        List<Role> roleList = roleMapper.selectList(new QueryWrapper<>());
        if (roleList != null && !roleList.isEmpty()) {
            List<RoleVO> roleVOList = roleTransfer.roles2RoleVO(roleList);
            //通过id分组 （实际每组的list只有一个role）
            Map<String,List<RoleVO>> roleMapByRoleId = roleVOList.stream().collect(Collectors.groupingBy(roleVO -> roleVO.getId().toString()));//stream().collect(Collectors.groupingBy().groupingBy(roleVo -> roleVo.get.getId().toString()))
            businessWorkFlowExtendVOS.stream().forEach(businessWorkFlowExtendVO -> {
                String roleIds = businessWorkFlowExtendVO.getRoleIds();
                if (!StringUtil.isEmpty(roleIds)) {
                    String[] roleArray = roleIds.split("\\,");
                    List<RoleVO> roles = new ArrayList<>();
                    for (String roleId:roleArray) {
                        //实际一个list只有一个role
                        List<RoleVO> singleRoleList = roleMapByRoleId.get(roleId);
                        if (singleRoleList != null && !singleRoleList.isEmpty())
                        roles.addAll(singleRoleList);
                    }
                    //存入vo
                    businessWorkFlowExtendVO.setRoleList(roles);
                }
                String nextDisposeRoleIds = businessWorkFlowExtendVO.getNextDisposeRoleIds();
                if (!StringUtil.isEmpty(nextDisposeRoleIds)) {
                    String[] roleArray = nextDisposeRoleIds.split("\\,");
                    List<RoleVO> roles = new ArrayList<>();
                    for (String roleId:roleArray) {
                        //实际一个list只有一个role
                        List<RoleVO> singleRoleList = roleMapByRoleId.get(roleId);
                        if (singleRoleList != null && !singleRoleList.isEmpty())
                            roles.addAll(singleRoleList);
                    }
                    //存入vo
                    businessWorkFlowExtendVO.setNextDisposeRoleList(roles);
                }
            });
        }

        //查询所有用户
        List<User> userList = userMapper.selectList(new QueryWrapper<>());
        if (userList != null && !userList.isEmpty()) {
            List<UserVO> userVOList = userTransfer.users2UserVo(userList);
            //通过id分组 （实际每组的list只有一个user）
            Map<String,List<UserVO>> userMapByUserId = userVOList.stream().collect(Collectors.groupingBy(userVO -> userVO.getId().toString()));
            //collect(Collectors.groupingBy( -> user.getId().toString()));
            businessWorkFlowExtendVOS.stream().forEach(businessWorkFlowExtendVO -> {
                String userIds = businessWorkFlowExtendVO.getUserIds();
                if (!StringUtil.isEmpty(userIds)) {
                    String[] userArray = userIds.split("\\,");
                    List<UserVO> users = new ArrayList<>();
                    for (String userId:userArray) {
                        //实际一个list只有一个user
                        List<UserVO> singleUserList = userMapByUserId.get(userId);
                        if (singleUserList != null && !singleUserList.isEmpty())
                        users.addAll(singleUserList);
                    }
                    //存入vo
                    businessWorkFlowExtendVO.setUserList(users);
                }

                String nextDisposeUserIds = businessWorkFlowExtendVO.getNextDisposeUserIds();
                if (!StringUtil.isEmpty(nextDisposeUserIds)) {
                    String[] userArray = nextDisposeUserIds.split("\\,");
                    List<UserVO> users = new ArrayList<>();
                    for (String userId:userArray) {
                        //实际一个list只有一个user
                        List<UserVO> singleUserList = userMapByUserId.get(userId);
                        if (singleUserList != null && !singleUserList.isEmpty())
                            users.addAll(singleUserList);
                    }
                    //存入vo
                    businessWorkFlowExtendVO.setNextDisposeUserList(users);
                }
            });
        }

        //在查询所有流转字段
        List<BusinessWorkFlowAttrExtendVO> businessWorkFlowAttrExtendVOList = businessWorkFlowAttrMapper.getBusinessWorkFlowAttrExtendList();
        //流转字段不为空时候塞进businessWorkFlowExtendVOS中
        if (businessWorkFlowAttrExtendVOList != null && !businessWorkFlowAttrExtendVOList.isEmpty()) {
            //根据流转id分组
            Map<String,List<BusinessWorkFlowAttrExtendVO>> mapByWorkFlowId = businessWorkFlowAttrExtendVOList.stream().collect(Collectors.groupingBy(x -> x.getWorkFlowId().toString()));
            businessWorkFlowExtendVOS.stream().forEach(businessWorkFlowExtendVO -> {
                List<BusinessWorkFlowAttrExtendVO> businessWorkFlowAttrExtendVOS = mapByWorkFlowId.get(businessWorkFlowExtendVO.getId().toString());
                //转为BusinessAttrCfgVO
                List<BusinessAttrCfgVO> businessAttrCfgVOList = businessAttrTransfer.toBusinessAttrCfgVOList(businessWorkFlowAttrExtendVOS);
                businessWorkFlowExtendVO.setBusinessAttrCfgVOList(businessAttrCfgVOList);
            });
        }


        //流程扭转文件配置塞入businessWorkFlowExtendVOS中
        List<BusinessWorkFlowFileVO> workFlowFileVOList =
                businessWorkFlowFileMapper.findWorkFlowFileCfg();
        if(workFlowFileVOList != null && !workFlowFileVOList.isEmpty()){
            businessWorkFlowExtendVOS.forEach(businessWorkFlowExtendVO -> {
                //将与流程扭转相关的附件配置注入到流程扭转配置对象
                List<BusinessWorkFlowFileVO> workFlowFileCfgList = workFlowFileVOList.stream().filter(vo -> {
                    //流程扭转配置ID为空则适用与所有流程扭转配置
                    if(vo.getWorkFlowId() == null || businessWorkFlowExtendVO.getId().longValue() == vo.getWorkFlowId().longValue() ) return true;
                    return false;
                }).collect(Collectors.toList());
                businessWorkFlowExtendVO.setWorkFlowFileVOList(workFlowFileCfgList);
            });
        }

        //根据公司id分组转为键值map
        Map<String,List<BusinessWorkFlowExtendVO>> mapByCompanyId = businessWorkFlowExtendVOS.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //设置存入缓存的结果集（公司id为空key值）
        Map<String,Object> mapByCompanyIdResult = new HashMap<>();
        for (String companyId:mapByCompanyId.keySet()) {
            List<BusinessWorkFlowExtendVO> ListByCompanyId= mapByCompanyId.get(companyId);
            //将公司id分组的键值map的值 再根据业务类型id分组转为键值map
            Map<String,List<BusinessWorkFlowExtendVO>> mapByTypeId = ListByCompanyId.stream().collect(Collectors.groupingBy(x -> x.getBusinessTypeId().toString()));
            //将公司id对应的  业务类型id分组的键值map
            mapByCompanyIdResult.put(companyId,mapByTypeId);
        }
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW_EXTEND,mapByCompanyIdResult);
    }

    //初始化业务流程属性配置
    private void initBusinessWorkFlowAttr() {
        logger.info("初始化业务流程属性配置");
        List<BusinessWorkFlowAttr> businessWorkFlowAttrList = businessWorkFlowAttrMapper.selectList(new QueryWrapper<BusinessWorkFlowAttr>());
        if (businessWorkFlowAttrList == null || businessWorkFlowAttrList.isEmpty())
            return;
        //根据公司id分组转为键值map
        Map<String,List<BusinessWorkFlowAttr>> mapByCompanyId = businessWorkFlowAttrList.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //设置存入缓存的结果集（公司id为空key值）
        Map<String,Object> mapByCompanyIdResult = new HashMap<>();
        for (String companyId:mapByCompanyId.keySet()) {
            List<BusinessWorkFlowAttr> ListByCompanyId= mapByCompanyId.get(companyId);
            //将公司id分组的键值map的值 再根据业务类型id分组转为键值map
            Map<String,List<BusinessWorkFlowAttr>> mapByTypeId = ListByCompanyId.stream().collect(Collectors.groupingBy(x -> x.getBusinessTypeId().toString()));
            //设置每个公司的结果集（业务类型id为key值）
            Map<String,Map> mapByTypeIdResult = new HashMap<>();
            for (String typeId:mapByTypeId.keySet()) {
                List<BusinessWorkFlowAttr> listByAttrId= mapByTypeId.get(typeId);
                //将公司业务类型id分组的键值map的值 再根据流程id分组转为键值map
                Map<String,List<BusinessWorkFlowAttr>> mapByFlowId = listByAttrId.stream().collect(Collectors.groupingBy(x -> x.getWorkFlowId().toString()));
                //将业务类型id对应的 流程id分组的键值map收拢
                mapByTypeIdResult.put(typeId,mapByFlowId);
            }
            //将公司id对应的  业务类型id分组的键值map
            mapByCompanyIdResult.put(companyId,mapByTypeIdResult);
        }
        //存入redis
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW_ATTR,mapByCompanyIdResult);
    }

    //初始化业务流转流程
    private void initBusinessWorkFlow() {
        logger.info("初始化业务流转流程");
        List<BusinessWorkFlow> businessWorkFlowList = businessWorkFlowMapper.selectList(new QueryWrapper<BusinessWorkFlow>());
        if (businessWorkFlowList == null || businessWorkFlowList.isEmpty())
            return;
        //根据公司id分组转为键值map
        Map<String,List<BusinessWorkFlow>> mapByCompanyId = businessWorkFlowList.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //设置存入缓存的结果集（公司id为空key值）
        Map<String,Object> mapByCompanyIdResult = new HashMap<>();
        for (String companyId:mapByCompanyId.keySet()) {
            List<BusinessWorkFlow> ListByCompanyId= mapByCompanyId.get(companyId);
            //将公司id分组的键值map的值 再根据业务类型id分组转为键值map
            Map<String,List<BusinessWorkFlow>> mapByTypeId = ListByCompanyId.stream().collect(Collectors.groupingBy(x -> x.getBusinessTypeId().toString()));
            //将公司id对应的  业务类型id分组的键值map
            mapByCompanyIdResult.put(companyId,mapByTypeId);
        }
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW,mapByCompanyIdResult);
    }

    //初始化业务类型
    private void initBusinessType() {
        logger.info("初始化业务类型");
        List<BusinessType> businessTypeList = businessTypeMapper.selectList(new QueryWrapper<BusinessType>());
        if (businessTypeList == null || businessTypeList.isEmpty())
            return;
        //根据公司id分组转为键值map
        Map<String,List<BusinessType>> mapByCompanyId = businessTypeList.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //存入redis
        for (String companyId:mapByCompanyId.keySet()) {
            redisUtil.hset(RedisConstant.REDIS_KEY_BUSINESS_TYPE,companyId,mapByCompanyId.get(companyId));
        }
    }

    //初始化业务流程权限配置
    private void initBusinessFlowPermit() {
        logger.info("初始化业务流程权限配置");
        List<BusinessFlowPermit> businessFlowPermitList = businessFlowPermitMapper.selectList(new QueryWrapper<BusinessFlowPermit>());
        if (businessFlowPermitList == null || businessFlowPermitList.isEmpty())
            return;
        //根据公司id分组转为键值map
        Map<String,List<BusinessFlowPermit>> mapByCompanyId = businessFlowPermitList.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //设置存入缓存的结果集（公司id为空key值）
        Map<String,Object> mapByCompanyIdResult = new HashMap<>();
        for (String companyId:mapByCompanyId.keySet()) {
            List<BusinessFlowPermit> ListByCompanyId= mapByCompanyId.get(companyId);
            //将公司id分组的键值map的值 再根据业务类型id分组转为键值map
            Map<String,List<BusinessFlowPermit>> mapByTypeId = ListByCompanyId.stream().collect(Collectors.groupingBy(x -> x.getBusinessTypeId().toString()));
            //设置每个公司的结果集（业务类型id为key值）
            Map<String,Map> mapByTypeIdResult = new HashMap<>();
            for (String typeId:mapByTypeId.keySet()) {
                List<BusinessFlowPermit> listByAttrId= mapByTypeId.get(typeId);
                //将公司业务类型id分组的键值map的值 再根据流程id分组转为键值map
                Map<String,List<BusinessFlowPermit>> mapByFlowId = listByAttrId.stream().collect(Collectors.groupingBy(x -> x.getWorkFlowId().toString()));
                //将业务类型id对应的 流程id分组的键值map收拢
                mapByTypeIdResult.put(typeId,mapByFlowId);
            }
            //将公司id对应的  业务类型id分组的键值map
            mapByCompanyIdResult.put(companyId,mapByTypeIdResult);
        }
        //存入redis
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_FLOW_PERMIT,mapByCompanyIdResult);
    }

    //初始化业务流程对接接口
    private void initBusinessFlowInterface() {
        logger.info("初始化业务流程对接接口");
        List<BusinessFlowInterface> businessFlowInterfaceList = businessFlowInterfaceMapper.selectList(new QueryWrapper<BusinessFlowInterface>());
        if (businessFlowInterfaceList == null || businessFlowInterfaceList.isEmpty())
            return;
        Map<String, Object> businessFlowInterfaceMap = new HashMap<>();
        businessFlowInterfaceList.stream().forEach(businessFlowInterface -> {
            businessFlowInterfaceMap.put(
                    businessFlowInterface.getCompanyId()+":"+businessFlowInterface.getBusinessTypeId()+":"+businessFlowInterface.getId(),
                    businessFlowInterface);
        });
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_FLOW_INTERFACE,businessFlowInterfaceMap);
    }

    //初始化业务流程状态
    private void initBusinessFlow() {
        logger.info("初始化业务流程状态");
        List<BusinessFlow> businessFlowList = businessFlowMapper.selectList(new QueryWrapper<BusinessFlow>().orderByAsc("company_id","business_type_id","sort"));
        if (businessFlowList == null || businessFlowList.isEmpty())
            return;
        //根据公司id分组转为键值map
        Map<String,List<BusinessFlow>> mapByCompanyId = businessFlowList.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //设置存入缓存的结果集（公司id为空key值）
        Map<String,Object> mapByCompanyIdResult = new HashMap<>();
        for (String companyId:mapByCompanyId.keySet()) {
            List<BusinessFlow> ListByCompanyId= mapByCompanyId.get(companyId);
            //将公司id分组的键值map的值 再根据业务类型id分组转为键值map
            Map<String,List<BusinessFlow>> mapByTypeId = ListByCompanyId.stream().collect(Collectors.groupingBy(x -> x.getBusinessTypeId().toString()));
            //将公司id对应的  业务类型id分组的键值map
            mapByCompanyIdResult.put(companyId,mapByTypeId);
        }
        //存入redis
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_FLOW,mapByCompanyIdResult);
    }

    //初始化费用公式
    private void initBusinessFeeFormula() {
        logger.info("初始化费用公式");
        List<BusinessFeeFormula> businessFeeFormulaList = businessFeeFormulaMapper.selectList(new QueryWrapper<BusinessFeeFormula>());
        if (businessFeeFormulaList == null || businessFeeFormulaList.isEmpty())
            return;
        //根据公司id分组转为键值map
        Map<String,List<BusinessFeeFormula>> mapByCompanyId = businessFeeFormulaList.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //设置存入缓存的结果集（公司id为空key值）
        Map<String,Object> mapByCompanyIdResult = new HashMap<>();
        for (String companyId:mapByCompanyId.keySet()) {
            List<BusinessFeeFormula> ListByCompanyId= mapByCompanyId.get(companyId);
            //将公司id分组的键值map的值 再根据业务类型id分组转为键值map
            Map<String,List<BusinessFeeFormula>> mapByTypeId = ListByCompanyId.stream().collect(Collectors.groupingBy(x -> x.getBusinessTypeId().toString()));
            //设置每个公司的结果集（业务类型id为key值）
            Map<String,Map> mapByTypeIdResult = new HashMap<>();
            for (String typeId:mapByTypeId.keySet()) {
                List<BusinessFeeFormula> listByAttrId= mapByCompanyId.get(typeId);
                //将公司业务类型id分组的键值map的值 再根据字段id分组转为键值map
                Map<String,List<BusinessFeeFormula>> mapByAttrId = listByAttrId.stream().collect(Collectors.groupingBy(x -> x.getBusinessAttrId().toString()));
                //将业务类型id对应的 字段id分组的键值map收拢
                mapByTypeIdResult.put(typeId,mapByAttrId);
            }
            //将公司id对应的  业务类型id分组的键值map
            mapByCompanyIdResult.put(companyId,mapByTypeIdResult);
        }
        //存入redis
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_FEE_FORMULA,mapByCompanyIdResult);
    }

    //初始化业务属性字段归类表
    private void initBusinessAttrVal() {
        logger.info("初始化业务属性字段值配置");
        List<BusinessAttrVal> businessAttrValList = businessAttrValMapper.selectList(new QueryWrapper<BusinessAttrVal>());
        if (businessAttrValList == null || businessAttrValList.isEmpty())
            return;
        //根据公司id分组转为键值map
        Map<String,List<BusinessAttrVal>> mapByCompanyId = businessAttrValList.stream().collect(Collectors.groupingBy(x -> x.getCompanyId().toString()));
        //设置存入缓存的结果集（公司id为空key值）
        Map<String,Object> mapByCompanyIdResult = new HashMap<>();
        for (String companyId:mapByCompanyId.keySet()) {
            List<BusinessAttrVal> ListByCompanyId= mapByCompanyId.get(companyId);
            //将公司id分组的键值map的值 再根据业务类型id分组转为键值map
            Map<String,List<BusinessAttrVal>> mapByTypeId = ListByCompanyId.stream().collect(Collectors.groupingBy(x -> x.getBusinessTypeId().toString()));
            //设置每个公司的结果集（业务类型id为key值）
            Map<String,Map> mapByTypeIdResult = new HashMap<>();
            for (String typeId:mapByTypeId.keySet()) {
                List<BusinessAttrVal> listByAttrId= mapByTypeId.get(typeId);
                //将公司业务类型id分组的键值map的值 再根据字段id分组转为键值map
                Map<String,List<BusinessAttrVal>> mapByAttrId = listByAttrId.stream().collect(Collectors.groupingBy(x -> x.getBusinessAttrId().toString()));
                //将业务类型id对应的 字段id分组的键值map收拢
                mapByTypeIdResult.put(typeId,mapByAttrId);
            }
            //将公司id对应的  业务类型id分组的键值map
            mapByCompanyIdResult.put(companyId,mapByTypeIdResult);
        }
        //存入redis
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL,mapByCompanyIdResult);
    }

    //初始化业务属性字段归类表
//    private void initBusinessAttrClassify() {
//        logger.info("初始化业务属性字段归类");
//        List<BusinessAttrClassify> businessAttrClassifyList = businessAttrClassifyMapper.selectList(new QueryWrapper<BusinessAttrClassify>());
//        if (businessAttrClassifyList == null || businessAttrClassifyList.isEmpty())
//            return;
//        Map<String, Object> businessAttrClassifyMap = new HashMap<>();
//        businessAttrClassifyList.stream().forEach(businessAttrClassify -> {
//            businessAttrClassifyMap.put(
//                    businessAttrClassify.getCompanyId()+":"+businessAttrClassify.getBusinessTypeId()+":"+businessAttrClassify.getId(),
//                    businessAttrClassify);
//        });
//        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_ATTR_CLASSIFY,businessAttrClassifyMap);
//    }

    //初始化数据字典到redis
    private void initDictionary() {
        logger.info("初始化数据字典");
        List<Dictionary> dictionaryList = dictionaryMapper.selectList(new QueryWrapper<Dictionary>().eq("status", 1));
        if(dictionaryList != null && !dictionaryList.isEmpty()){
            Map<String, Object> dictionaryMap = dictionaryList.stream().collect(Collectors.toMap(Dictionary::getDictKey, dictionary -> dictionary));
            redisUtil.hmset(RedisConstant.REDIS_KEY_DICTIONARY,dictionaryMap);
        }
        List<DictionaryDetails> detailsList = dictionaryDetailsMapper.selectList(new QueryWrapper<DictionaryDetails>().eq("status", 1));
        if(detailsList != null && !detailsList.isEmpty()){
            Map<String, Object> detailsMap = new HashMap<>();
            dictionaryList.forEach(dictionary -> {
                detailsMap.put(dictionary.getDictKey(),
                        detailsList.stream()
                                .filter(details -> { return details.getParentKey().equals(dictionary.getDictKey());})
                                .collect(Collectors.toList()));
            });
            redisUtil.hmset(RedisConstant.REDIS_KEY_DICTIONARY_DETAILS,detailsMap);
        }
    }

    /**
     * 初始化读取业务属性配置到redis
     */
    private void initBusinessAttr(){
        logger.info("-------------------初始化业务属性配置-----------------");
        List<BusinessAttrCfgVO> businessAttrList = businessAttrMapper.findBusinessAttrCfg();
        if(businessAttrList == null || businessAttrList.isEmpty()) {
            logger.info("-------------------业务属性配置数据为空-----------------");
            logger.info("-------------------结束业务属性配置-----------------");
            return;
        }

        Map<String, Object> businessAttrMap = BusinessCfgUtil.createBusinessAttrCfgMap(businessAttrList);
        //将配置存入缓存
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_ATTR,businessAttrMap);

        logger.info("-------------------结束业务属性配置-----------------");
    }

    /**
     * 初始化业务附件配置
     */
    private void initBusinessFileCfg(){
        logger.info("-------------------初始化业务附件配置-----------------");
        List<BusinessFileCfg> businessFileCfgList =
                businessFileCfgMapper.selectList(
                        new QueryWrapper<BusinessFileCfg>().lambda().orderByAsc(BusinessFileCfg::getSort)
                );
        if(businessFileCfgList == null || businessFileCfgList.isEmpty()) {
            logger.info("-------------------业务附件配置数据为空-----------------");
            logger.info("-------------------结束业务附件配置-----------------");
            return;
        }
        Map<String, Object> businessFileCfgMap = BusinessCfgUtil.createBusinessFileCfgMap(businessFileCfgList);
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_FILE_CFG, businessFileCfgMap);

        logger.info("-------------------结束业务附件配置-----------------");
    }

    /**
     * 初始化流程扭转文件配置
     */
    private void initBusinessWorkFlowFileCfg(){
        logger.info("-------------------初始化流程扭转文件配置-----------------");
        List<BusinessWorkFlowFileVO> workFlowFileVOList = businessWorkFlowFileMapper.findWorkFlowFileCfg();
        if(workFlowFileVOList == null || workFlowFileVOList.isEmpty()){
            logger.info("-------------------流程扭转文件配置数据为空-----------------");
            logger.info("-------------------结束流程扭转文件配置-----------------");
            return;
        }

        Map<String, Object> cfgMap = RedisInitCfgUtil.createBusinessWorkFlowFileCfgMap(workFlowFileVOList);
        redisUtil.hmset(RedisConstant.REDIS_KEY_BUSINESS_WORKFLOW_FILE_CFG,cfgMap);

        logger.info("-------------------结束流程扭转文件配置-----------------");
    }
}
