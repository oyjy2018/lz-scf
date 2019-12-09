<style lang="less">
@import "../../../index.less";
</style>

<template>
  <div class="container">
    <div class="title-nav">
      <div class="title-text">申请授信</div>
      <!-- <div class="nav-text">
        <span v-for="item in flowCodeList"
              :key="item.flowCode"
              :class="{'nav-text-active': item.flowName === '草稿'}">{{item.flowName}} > </span>
      </div> -->
    </div>
    <div class="form-steps">
      <MyForm style="width: 700px"
              ref="form1"
              :model="form"
              :label-width="250">
        <div class="form-steps-item"
             v-for="(item, index) in formAttrList"
             :key="index">
          <div class="form-steps-title"
               @click="collapse(index)">
            <div class="form-steps-title-num">
              <div><span :class="{'span-active': index === step}">{{index+1}}</span></div>
              <div style="height: 30px;">
                <Icon size="18"
                      :class="{'i-active': index === step}"
                      :type="index === step ? 'ios-arrow-down' : 'ios-arrow-up'" /><br>
                <Icon :class="{'i-active': index === step}"
                      style="margin-top: -30px;"
                      size="18"
                      :type="index === step ? 'ios-arrow-down' : 'ios-arrow-up'" />
              </div>
            </div>
            <div class="form-steps-title-text">
              <div><span>{{item.classifyName}}</span></div>
            </div>
          </div>
          <div class="form-steps-context"
               v-show="index === step">
            <div v-for="(subItem, subIndex) in item.attrList"
                 :key="subItem.columnName">
              <div v-if="subItem.columnGroup">
                <div v-for="(subItemSub, subIndexSub) in subItem.attrList"
                     :key="subItemSub.columnCh">
                  <ApplyForm :model="form[subItem.columnGroup][subIndex][subItemSub.columnName]"
                             :form="form"
                             :attrList="subItem.attrList"
                             :item="subItemSub"
                             :index="subIndex"
                             :group="subItem.columnGroup"
                             @on-save="onSaveDraft" />
                  <!-- 删除项目按钮 -->
                  <FormItem v-if="subIndexSub === subItem.attrList.length -1 && subIndex !== 0">
                    <Row>
                      <Col span="12">
                      <Button type="text"
                              @click="handledel(subItem, index, subIndex, subIndexSub)">删除项目</Button>
                      </Col>
                    </Row>
                  </FormItem>
                  <!-- 添加项目按钮 -->
                  <FormItem v-if="subIndexSub === subItem.attrList.length -1 && subIndex === item.attrList.length - 1">
                    <Row>
                      <Col span="12">
                      <Button type="dashed"
                              @click="handleAdd(subItem, index, subIndex)"
                              icon="md-add">添加项目</Button>
                      </Col>
                    </Row>
                  </FormItem>
                  <!-- 项目分割线 -->
                  <Divider v-if="subIndexSub === subItem.attrList.length -1 && !(subIndex === item.attrList.length - 1)"
                           dashed />
                </div>

              </div>
              <div v-else>
                <ApplyForm :model="form[subItem.columnName]"
                           :form="form"
                           :attrList="item.attrList"
                           :item="subItem"
                           @on-save="onSaveDraft" />
              </div>
            </div>
            <div style="text-align: center;"> <Button v-if="index !== formAttrList.length - 1"
                      @click="collapse(index + 1)"
                      type="primary"
                      style="width: 150px">下一步</Button>
              <Button v-else
                      @click="collapse(index - 1)"
                      type="primary"
                      style="width: 150px">上一步</Button>
            </div>

          </div>
          <div v-show="index !== step && index !== formAttrList.length - 1"
               class="form-steps-context"
               style="height: 30px"></div>
        </div>
      </MyForm>
      <br><br>
      <Button @click="handleSubmit"
              type="primary"
              style="width: 150px">提交申请</Button>
      <br><br>
    </div>
  </div>
</template>

<script>
import MyForm from '_c/my-form'
import ApplyForm from '_c/apply-form'
import { mapState, mapActions, mapMutations } from 'vuex'
import { CITYS } from '@/libs/citys.js'
import { deepCopy } from '@/libs/util.js'

export default {
  name: 'apply',
  components: {
    MyForm,
    ApplyForm
  },
  data () {
    return {
      step: 0,
      citys: CITYS,
      accordion: '1',
      form: { 'creditApply_customerName': '', 'creditApply_idCard': '', 'creditApply_gender': '', 'creditApply_age': '', 'creditApply_mobile': '', 'creditApply_maritalStatus': '', 'creditApply_familyAddressProvinceCode': [], 'creditApply_familyAddressCityCode': '', 'creditApply_familyAddressRegionCode': '', 'creditApply_familyAddress': '', 'creditApply_housingOwnership': '', 'creditApply_spouseName': '', 'creditApply_spouseIdCard': '', 'creditApply_spouseMobile': '', 'item': [{ 'creditItem_itemName': '', 'creditItem_constructionSchedule': '', 'creditItem_planFinishTime': '', 'creditItem_itemCollectionDays': '', 'creditItem_applyCreditBusiness': '', 'creditItem_applyPurpose': '', 'creditItem_applyPurposeOther': '', 'creditItem_applyCreditMoney': '', 'creditItem_hasDivideUse': '' }], 'creditApply_ecCooperationTime': '', 'creditApply_ecCooperationYear': '', 'creditApply_belongsRegion': '', 'creditApply_regionManager': '', 'creditRisk_riskCredit1': '', 'creditRisk_riskCredit2': '', 'creditRisk_riskCredit3': '', 'creditRisk_riskCredit4': '', 'creditRisk_riskCredit5': '', 'creditRisk_riskCredit6': '', 'creditRisk_riskCredit7': '', 'creditRisk_riskCredit8': '', 'creditRisk_riskCredit9': '', 'creditRisk_riskCredit10': '', 'creditRisk_riskCredit11': '', 'creditRisk_riskCredit12': '', 'creditRisk_riskCredit13': '' },
      rules: {},
      businessId: '',
      businessTypeId: '1',
      businessType: '申请授信',
      attrValList: [],
      formAttrList: [
        {
          'classifyName': '个人信息',
          'attrList': [
            {
              'classifyName': '个人信息',
              'columnCh': '姓名',
              'columnErrMsg': '请输入1-10位字符',
              'columnName': 'creditApply_customerName',
              'columnTip': '',
              'columnType': 'text',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '^.{1,10}$'
            },
            {
              'classifyName': '个人信息',
              'columnCh': '身份证号',
              'columnErrMsg': '请输入正确的身份证',
              'columnName': 'creditApply_idCard',
              'columnTip': '',
              'columnType': 'id-card',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': ''
            },
            {
              'classifyName': '个人信息',
              'columnCh': '性别',
              'columnName': 'creditApply_gender',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 1,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 1,
                  'valueCh': '男',
                  'valueKey': 'M'
                },
                {
                  'id': 2,
                  'valueCh': '女',
                  'valueKey': 'F'
                }
              ]
            },
            {
              'classifyName': '个人信息',
              'columnCh': '年龄',
              'columnErrMsg': '请输入1-3位大于0的数字',
              'columnName': 'creditApply_age',
              'columnTip': '',
              'columnType': 'text',
              'hasReadOnly': 1,
              'required': 1,
              'verifyFormula': '^[1-9]\\d{0,2}$'
            },
            {
              'classifyName': '个人信息',
              'columnCh': '手机号',
              'columnErrMsg': '请输入正确的手机号码',
              'columnName': 'creditApply_mobile',
              'columnTip': '',
              'columnType': 'text',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '^1\\d{10}$'
            },
            {
              'classifyName': '个人信息',
              'columnCh': '婚姻状况',
              'columnName': 'creditApply_maritalStatus',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 3,
                  'valueCh': '已婚',
                  'valueKey': '1'
                },
                {
                  'id': 4,
                  'valueCh': '未婚 ',
                  'valueKey': '2'
                },
                {
                  'id': 5,
                  'valueCh': '离异',
                  'valueKey': '3'
                }
              ]
            },
            {
              'classifyName': '个人信息',
              'columnCh': '家庭住址-省',
              'columnName': 'creditApply_familyAddressProvinceCode',
              'columnTip': '',
              'columnType': 'select-province',
              'hasReadOnly': 0,
              'juniorColumnId': 8,
              'required': 1,
              'verifyFormula': ''
            },
            {
              'classifyName': '个人信息',
              'columnCh': '家庭住址-市',
              'columnName': 'creditApply_familyAddressCityCode',
              'columnTip': '',
              'columnType': 'select-city',
              'hasReadOnly': 0,
              'juniorColumnId': 9,
              'required': 1,
              'verifyFormula': ''
            },
            {
              'classifyName': '个人信息',
              'columnCh': '家庭住址-区',
              'columnName': 'creditApply_familyAddressRegionCode',
              'columnTip': '',
              'columnType': 'select-region',
              'hasReadOnly': 0,
              'required': 0,
              'verifyFormula': ''
            },
            {
              'classifyName': '个人信息',
              'columnCh': '家庭住址-详细地址',
              'columnErrMsg': '请输入1-60位字符',
              'columnName': 'creditApply_familyAddress',
              'columnTip': '',
              'columnType': 'text',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '^.{1,60}$'
            },
            {
              'classifyName': '个人信息',
              'columnCh': '家庭住址房产状态',
              'columnName': 'creditApply_housingOwnership',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 6,
                  'valueCh': '购置',
                  'valueKey': '1'
                },
                {
                  'id': 7,
                  'valueCh': '按揭中',
                  'valueKey': '2'
                },
                {
                  'id': 8,
                  'valueCh': '租住',
                  'valueKey': '3'
                }
              ]
            },
            {
              'classifyName': '个人信息',
              'columnCh': '配偶姓名',
              'columnErrMsg': '请输入1-10位字符',
              'columnName': 'creditApply_spouseName',
              'columnTip': '',
              'columnType': 'text',
              'hasReadOnly': 0,
              'preColumnId': 6,
              'preColumnValue': '3',
              'required': 1,
              'verifyFormula': '^.{1,10}$'
            },
            {
              'classifyName': '个人信息',
              'columnCh': '配偶身份证号',
              'columnErrMsg': '请输入正确的身份证',
              'columnName': 'creditApply_spouseIdCard',
              'columnTip': '',
              'columnType': 'id-card',
              'hasReadOnly': 0,
              'preColumnId': 6,
              'preColumnValue': '3',
              'required': 1,
              'verifyFormula': ''
            },
            {
              'classifyName': '个人信息',
              'columnCh': '配偶手机号',
              'columnErrMsg': '请输入正确的手机号码',
              'columnName': 'creditApply_spouseMobile',
              'columnTip': '',
              'columnType': 'text',
              'hasReadOnly': 0,
              'preColumnId': 6,
              'preColumnValue': '3',
              'required': 1,
              'verifyFormula': '^1\\d{10}$'
            }
          ]
        },
        {
          'classifyName': '项目授信额度申请',
          'attrList': [
            {
              'columnGroup': 'item',
              'attrList': [
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '授信工程项目',
                  'columnErrMsg': '请输入1-100位字符',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_itemName',
                  'columnTip': '',
                  'columnType': 'text',
                  'hasReadOnly': 0,
                  'required': 1,
                  'verifyFormula': '^.{1,100}$'
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '施工进度',
                  'columnErrMsg': '请输入0.00~100.00的双小数数字',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_constructionSchedule',
                  'columnTip': '',
                  'columnType': 'two-decimal',
                  'columnUnit': '%',
                  'hasReadOnly': 0,
                  'required': 1,
                  'verifyFormula': '^(100\\.\\d{2}|\\d{1,2}\\.\\d{2})$'
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '预计完工时间',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_planFinishTime',
                  'columnTip': '',
                  'columnType': 'date',
                  'hasReadOnly': 0,
                  'required': 1,
                  'verifyFormula': ''
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '项目收款账期',
                  'columnErrMsg': '请输入1~11位大于0的整数',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_itemCollectionDays',
                  'columnTip': '必须为整数',
                  'columnType': 'number',
                  'columnUnit': '天',
                  'defaultValue': '',
                  'defaultValueType': '',
                  'hasReadOnly': 0,
                  'preColumnValue': '',
                  'required': 1,
                  'verifyFormula': '^[1-9]\\d{0,10}$'
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '申请业务种类（授信成功后）',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_applyCreditBusiness',
                  'columnTip': '',
                  'columnType': 'select',
                  'hasReadOnly': 0,
                  'required': 1,
                  'verifyFormula': '',
                  'list': [
                    {
                      'id': 45,
                      'valueCh': '',
                      'valueKey': ''
                    },
                    {
                      'id': 46,
                      'valueCh': '开商票',
                      'valueKey': 'open-business-ticket'
                    }
                  ]
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '申请用途',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_applyPurpose',
                  'columnTip': '',
                  'columnType': 'select',
                  'hasReadOnly': 0,
                  'required': 1,
                  'verifyFormula': '',
                  'list': [
                    {
                      'id': 53,
                      'valueCh': '支付人工费',
                      'valueKey': 'pay-artificial-fee'
                    },
                    {
                      'id': 54,
                      'valueCh': '支付材料款',
                      'valueKey': 'pay-material-money'
                    },
                    {
                      'id': 57,
                      'valueCh': '其他',
                      'valueKey': 'other'
                    }
                  ]
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '申请用途-其他',
                  'columnErrMsg': '请输入1~18位字符',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_applyPurposeOther',
                  'columnTip': '',
                  'columnType': 'text',
                  'hasReadOnly': 0,
                  'preColumnId': 20,
                  'preColumnValue': '57',
                  'required': 1,
                  'verifyFormula': '^.{1,18}$'
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '申请授信额度',
                  'columnErrMsg': '请输入1~11位大于0的整数',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_applyCreditMoney',
                  'columnTip': '必须为整数',
                  'columnType': 'number',
                  'columnUnit': '元',
                  'hasReadOnly': 0,
                  'required': 1,
                  'verifyFormula': '^[1-9]\\d{0,10}$'
                },
                {
                  'classifyName': '项目授信额度申请',
                  'columnCh': '是否分次支用',
                  'columnGroup': 'item',
                  'columnName': 'creditItem_hasDivideUse',
                  'columnTip': '',
                  'columnType': 'radio',
                  'columnUnit': '',
                  'defaultValue': '',
                  'defaultValueType': '',
                  'hasReadOnly': 0,
                  'preColumnValue': '',
                  'required': 1,
                  'verifyFormula': '',
                  'list': [
                    {
                      'createBy': 1,
                      'createTime': 1565662601000,
                      'id': 3148,
                      'superiorAttrKey': '',
                      'updateBy': 1,
                      'valueCh': '是',
                      'valueKey': '1'
                    },
                    {
                      'createBy': 1,
                      'createTime': 1565662758000,
                      'id': 3149,
                      'superiorAttrKey': '',
                      'updateBy': 1,
                      'valueCh': '否',
                      'valueKey': '0'
                    }
                  ]
                }
              ]
            }
          ]
        },
        {
          'classifyName': '申请人与工程公司关系',
          'attrList': [
            {
              'classifyName': '申请人与工程公司关系',
              'columnCh': '跟工程公司开始合作时间',
              'columnName': 'creditApply_ecCooperationTime',
              'columnTip': '',
              'columnType': 'date',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': ''
            },
            {
              'classifyName': '申请人与工程公司关系',
              'columnCh': '跟工程公司合作年限',
              'columnErrMsg': '请输入小于100.0的单小数数字',
              'columnName': 'creditApply_ecCooperationYear',
              'columnTip': '可输入小数',
              'columnType': 'one-decimal',
              'columnUnit': '年',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '^[1-9]?\\d\\.\\d$'
            },
            {
              'classifyName': '申请人与工程公司关系',
              'columnCh': '所属大区',
              'columnName': 'creditApply_belongsRegion',
              'columnTip': '',
              'columnType': 'select',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 30,
                  'valueCh': '广东大区',
                  'valueKey': 'guangdong'
                },
                {
                  'id': 32,
                  'valueCh': '南方大区',
                  'valueKey': 'nanfang'
                },
                {
                  'id': 33,
                  'valueCh': '深圳大区',
                  'valueKey': 'shenzhen'
                },
                {
                  'id': 35,
                  'valueCh': '广州大区',
                  'valueKey': 'guangzhou'
                },
                {
                  'id': 36,
                  'valueCh': '青岛分公司',
                  'valueKey': 'qingdao'
                },
                {
                  'id': 37,
                  'valueCh': '济南分公司',
                  'valueKey': 'jinan'
                },
                {
                  'id': 38,
                  'superiorAttrKey': 'guangdong',
                  'valueCh': '东莞分公司',
                  'valueKey': 'dongguan'
                },
                {
                  'id': 39,
                  'superiorAttrKey': 'guangdong',
                  'valueCh': '佛山分公司',
                  'valueKey': 'foshan'
                },
                {
                  'id': 40,
                  'superiorAttrKey': 'guangdong',
                  'valueCh': '惠州分公司',
                  'valueKey': 'huizhou'
                },
                {
                  'id': 41,
                  'superiorAttrKey': 'guangdong',
                  'valueCh': '珠海分公司',
                  'valueKey': 'zhuhai'
                },
                {
                  'id': 42,
                  'superiorAttrKey': 'guangdong',
                  'valueCh': '海南分公司',
                  'valueKey': 'hainan'
                },
                {
                  'id': 44,
                  'superiorAttrKey': 'nanfang',
                  'valueCh': '重庆分公司',
                  'valueKey': 'chongqing'
                }
              ]
            },
            {
              'classifyName': '申请人与工程公司关系',
              'columnCh': '大区经理',
              'columnErrMsg': '请输入1-10位字符',
              'columnName': 'creditApply_regionManager',
              'columnTip': '',
              'columnType': 'text',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '^.{1,10}$'
            }
          ]
        },
        {
          'classifyName': '情况说明',
          'attrList': [
            {
              'classifyName': '情况说明',
              'columnCh': '是否项目实际控制人',
              'columnName': 'creditRisk_riskCredit1',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 11,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 12,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '是否签劳动合同',
              'columnName': 'creditRisk_riskCredit2',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 13,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 14,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '是否购买社保',
              'columnName': 'creditRisk_riskCredit3',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 15,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 16,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '社保是否50万以上（含）',
              'columnName': 'creditRisk_riskCredit4',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'preColumnId': 28,
              'preColumnValue': '15',
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 17,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 18,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '是否已签订《商务经理承包责任书》',
              'columnName': 'creditRisk_riskCredit5',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 19,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 20,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '是否已签订《项目内部承包经营责任书》',
              'columnName': 'creditRisk_riskCredit6',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 21,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 22,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '个人/配偶人行征信是否有不良记录',
              'columnName': 'creditRisk_riskCredit7',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 23,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 24,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '个人/配偶人行征信不良记录说明',
              'columnErrMsg': '请输入长度小于等于500的字符',
              'columnName': 'creditRisk_riskCredit8',
              'columnTip': '',
              'columnType': 'textarea',
              'hasReadOnly': 0,
              'preColumnId': 32,
              'preColumnValue': '23',
              'required': 0,
              'verifyFormula': '^[\\s\\S]{1,500}$'
            },
            {
              'classifyName': '情况说明',
              'columnCh': '个人/配偶是否有涉案、涉诉',
              'columnName': 'creditRisk_riskCredit9',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 25,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 26,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '个人/配偶涉案、涉诉说明',
              'columnErrMsg': '请输入长度小于等于500的字符',
              'columnName': 'creditRisk_riskCredit10',
              'columnTip': '',
              'columnType': 'textarea',
              'hasReadOnly': 0,
              'preColumnId': 34,
              'preColumnValue': '25',
              'required': 0,
              'verifyFormula': '^[\\s\\S]{1,500}$'
            },
            {
              'classifyName': '情况说明',
              'columnCh': '在集团有无欠款情况',
              'columnName': 'creditRisk_riskCredit11',
              'columnTip': '',
              'columnType': 'radio',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '',
              'list': [
                {
                  'id': 27,
                  'valueCh': '是',
                  'valueKey': '1'
                },
                {
                  'id': 28,
                  'valueCh': '否',
                  'valueKey': '0'
                }
              ]
            },
            {
              'classifyName': '情况说明',
              'columnCh': '在集团欠款情况说明',
              'columnErrMsg': '请输入长度小于等于500的字符',
              'columnName': 'creditRisk_riskCredit12',
              'columnTip': '',
              'columnType': 'textarea',
              'hasReadOnly': 0,
              'preColumnId': 36,
              'preColumnValue': '27',
              'required': 0,
              'verifyFormula': '^[\\s\\S]{1,500}$'
            },
            {
              'classifyName': '情况说明',
              'columnCh': '用信计划',
              'columnErrMsg': '请输入长度小于等于500的字符',
              'columnName': 'creditRisk_riskCredit13',
              'columnTip': '',
              'columnType': 'textarea',
              'hasReadOnly': 0,
              'required': 1,
              'verifyFormula': '^[\\s\\S]{1,500}$'
            }
          ]
        }
      ],
      data: {},
      flowCodeList: []
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    // const data = {
    //   businessTypeId: this.businessTypeId,
    //   businessType: this.businessType,
    //   queryType: 'create'
    // }
    // this.findBusinessAttrCfg(data).then(res => {
    //   if (res && res.status === 200 && res.data && res.data.code === 10000) {
    //     const data1 = res.data.data
    //     const formAttrList = []
    //     // from
    //     const form = {}

    //     data1.formAttrList.forEach(item => {
    //       let obj = {}
    //       obj.classifyName = item.classifyName
    //       obj.attrList = []
    //       let subObj = {
    //         columnGroup: '',
    //         attrList: []
    //       }
    //       // from
    //       let fromArr = {}
    //       item.attrList.forEach(subItem => {
    //         if (subItem.columnType === 'radio' || subItem.columnType === 'select') {
    //           subItem.list = data1.attrValList.filter(sitem => {
    //             if (subItem.businessAttrId === sitem.businessAttrId) {
    //               return sitem
    //             }
    //           })
    //         }
    //         if (subItem.columnGroup) {
    //           subObj.columnGroup = subItem.columnGroup
    //           subObj.attrList.push(subItem)
    //           // from
    //           form[subItem.columnGroup] = []
    //           fromArr = Object.assign(fromArr, { [subItem.columnName]: subItem.columnName.indexOf('Province') === -1 ? '' : [] })
    //           form[subItem.columnGroup] = [...[fromArr]]
    //         } else {
    //           obj.attrList.push(subItem)
    //           // from
    //           form[subItem.columnName] = subItem.columnName.indexOf('Province') === -1 ? '' : []
    //         }
    //       })
    //       if (subObj.columnGroup) {
    //         obj.attrList.push(subObj)
    //       }
    //       formAttrList.push(obj)
    //     })
    //     // this.form = form
    //     // console.log(JSON.stringify(form))
    //     // this.formAttrList = formAttrList
    //     this.data = data1
    //   }
    // })
    // this.getFlowCodeList1()
  },
  methods: {
    ...mapActions([
      'findBusinessAttrCfg',
      'saveDraft',
      'commitApply',
      'deleteCreditItem',
      'getFlowCodeList'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    collapse (index) {
      this.step = index === this.step ? -1 : index
    },
    getFlowCodeList1 () {
      const data = {
        param: '申请授信'
      }
      this.getFlowCodeList(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.flowCodeList = res.data.data
        }
      })
    },
    onSaveDraft ({ group, tableName, value, index, valid }) {
      const data = {
        businessTypeId: this.businessTypeId,
        businessId: this.businessId,
        tableName,
        value,
        index
      }
      if (group) {
        this.form[group][index][tableName] = value
      } else {
        this.form[tableName] = value
      }
      if (tableName === 'creditApply_familyAddressProvinceCode') {
        data.value = value.join(' ')
      }
      const name = index === -1 ? tableName : `${group}[${index}][${tableName}]`
      this.$refs['form1'].validateField(name, error => {
        if (!error && valid) {
          this.saveDraft(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.businessId = res.data.data.businessId
              if (tableName === 'creditApply_idCard') {
                this.discriCard(value)
              }
              // 与工程公司合作年限根据合作开始时间算出
              if (tableName === 'creditApply_ecCooperationTime') {
                this.computedYear(value, index)
              }
            }
          })
        }
      })
    },
    // 身份证识别性别和年龄
    discriCard (idCard) {
      // 获取性别 creditApply_gender 1 2
      const list = this.formAttrList[0]['attrList'][2].list
      if (parseInt(idCard.substr(16, 1)) % 2 === 1) {
        // alert('男')
        for (let i = 0; i < 2; i++) {
          if (list[i].valueCh === '男') {
            let value = list[i].id + ''
            this.onSaveDraft({ group: '', tableName: 'creditApply_gender', value, index: -1, valid: true })
          }
        }
      } else {
        // alert('女')
        for (let i = 0; i < 2; i++) {
          if (list[i].valueCh === '女') {
            let value = list[i].id + ''
            this.onSaveDraft({ group: '', tableName: 'creditApply_gender', value, index: -1, valid: true })
          }
        }
      }
      // 获取年龄 creditApply_age
      let myDate = new Date()
      let month = myDate.getMonth() + 1
      let day = myDate.getDate()
      let age = myDate.getFullYear() - idCard.substring(6, 10) - 1
      if (idCard.substring(10, 12) < month || (idCard.substring(10, 12) === month && idCard.substring(12, 14) <= day)) {
        age++
      }
      // 年龄 age
      this.onSaveDraft({ group: '', tableName: 'creditApply_age', value: age + '', index: -1, valid: true })
    },
    // 与工程公司合作年限根据合作开始时间算出 "creditApply_ecCooperationYear"
    computedYear (value, index) {
      const year = ((new Date().getTime() - new Date(value.replace(new RegExp(/-/gm), '/')).getTime()) / (365 * 24 * 60 * 60 * 1000)).toFixed(1)
      if (year >= 0) {
        this.onSaveDraft({ group: '', tableName: 'creditApply_ecCooperationYear', value: year + '', index, valid: true })
      }
    },
    handleAdd (subItem, index, subIndex) {
      const len = this.form[subItem.columnGroup].length
      const obj = this.form[subItem.columnGroup][len - 1]
      let isAdd = true
      for (let i in obj) {
        if (obj[i] !== '') {
          this.form[subItem.columnGroup].push(deepCopy(this.form[subItem.columnGroup][0], true))
          this.formAttrList[index]['attrList'].push(subItem)
          isAdd = false
          break
        }
      }
      if (isAdd) {
        this.$Message.warning({
          content: '请先填写上一个授信工程项目信息',
          duration: 5,
          closable: true
        })
      }
    },
    handledel (subItem, index, subIndex, subIndexSub) {
      if (this.businessId) {
        const item = this.form[subItem.columnGroup][subIndex]
        for (let key in item) {
          if (item[key] !== '') {
            this.onDeleteCreditItem(subItem, index, subIndex)
            return false
          }
        }
      } else {
        this.delItem(subItem, index, subIndex)
      }
    },
    delItem (subItem, index, subIndex) {
      this.form[subItem.columnGroup].splice(subIndex, 1)
      this.formAttrList[index]['attrList'].pop()
    },
    onDeleteCreditItem (subItem, index, subIndex) {
      const data = {
        creditApplyId: this.businessId,
        itemIndex: subIndex
      }
      this.deleteCreditItem(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.delItem(subItem, index, subIndex)
          this.$Modal.success({
            title: '提示',
            content: '删除成功'
          })
        }
      })
    },
    handleSubmit () {
      this.$refs['form1'].validate((valid, errors) => {
        if (valid) {
          if (this.businessId) {
            const data = {
              businessTypeId: this.businessTypeId,
              businessId: this.businessId
            }
            this.commitApply(data).then(res => {
              if (res && res.status === 200 && res.data && res.data.code === 10000) {
                this.businessId = ''
                this.from = deepCopy(this.from, true)
                this.closeTag({
                  name: 'apply'
                })
                this.$router.push({
                  name: 'myapply'
                })
                this.$Modal.success({
                  title: '提示',
                  content: '提交申请成功'
                })
              }
            })
          } else {
            this.$Message.warning({
              content: '请先填写信息',
              duration: 5,
              closable: true
            })
          }
        } else {
          this.$Message.error({
            content: errors[0],
            duration: 5,
            closable: true
          })
        }
      })
    }
  }
}
</script>
