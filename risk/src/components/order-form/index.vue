<template>
  <div>
    <Form ref="formData"
          :label-width="100"
          :model="formAddScore">
      <Row style="margin-bottom: 24px;">
        <Col span=3>
        <span style="font-size: 12px; float: right;margin-right: 12px;">评分项公式</span>
        </Col>
        <Col span="20">
        <Row>
          <Col span="20">
          <Button icon="md-add"
                  type="primary"
                  ghost
                  style='margin-bottom:16px;'
                  @click="addScoreBtn('formData')">添加评分公式</Button>
          </Col>
        </Row>
        <Row>
          <div v-for="(item,index) in formAddScore.items"
               :key='index'
               ref='formList'>
            <Col span="20"
                 style="margin: 20px 0;">
            <div class="box"
                 title="评分项公式">
              <div class="box"
                   title="满足如下条件时"
                   style="margin-bottom: 25px;">
                <Row>
                  <Col span="20">
                  <FormItem :prop="'items.'+index+'.criteriaType'"
                            :rules="{required: true, message: '请选择条件',  trigger: 'change'}"
                            :label-width="0">
                    <Select placeholder="选择条件"
                            v-model="item.criteriaType"
                            @on-change="handleCriteriaTypeChange(item.criteriaType,index)">
                      <Option :value="1+''">符合如下任何条件</Option>
                      <Option :value="2+''">符合如下全部条件</Option>
                      <Option :value="3+''">无条件</Option>
                    </Select>
                  </FormItem>
                  </Col>
                </Row>
                <Button icon="md-add"
                        type="primary"
                        ghost
                        style='margin-bottom:16px;'
                        @click='addRuleBtn(index,null)' :disabled="item.criteriaType === '3'">添加条件</Button>
                <div class="box"
                     title="使用方法"
                     style="margin-top: 20px; padding-bottom:0px;">
                  <Row v-for="(citem,n) in item.criteriaArray"
                       :key='n'>
                    <Col span="8">
                    <FormItem :label-width="0"
                              :prop="'items.'+index+'.criteriaArray.'+n+'.variable'"
                              :rules="{required: true, message: '请选择条件',  trigger: 'change'}">
                      <Select placeholder=""
                              filterable
                              clearable
                              v-model="formAddScore.items[index].criteriaArray[n].variable">
                        <OptionGroup v-for="(item,index) in selecVariable"
                                     :key='index'
                                     :label="item.title">
                          <Option v-for="(list,index) in item[item.name]"
                                  :key="index"
                                  :value="list.columnName">{{ list.columnCh }}</Option>
                        </OptionGroup>

                      </Select>
                    </FormItem>
                    </Col>
                    <Col span="5"
                         offset="1">
                    <FormItem :label-width="0"
                              :prop="'items.'+index+'.criteriaArray.'+n+'.operator'"
                              :rules="{required: true, message: '请选择',  trigger: 'change'}">

                      <Select placeholder=""
                              v-model="formAddScore.items[index].criteriaArray[n].operator">
                        <Option v-for="(item,index) in optionOperator"
                                :key="index"
                                :value="item">{{item}}</Option>
                      </Select>
                    </FormItem>
                    </Col>
                    <Col span="5"
                         offset="1">
                    <FormItem :label-width="0"
                              :prop="'items.'+index+'.criteriaArray.'+n+'.value'"
                              :rules="{required: true, message: '请输入分值',  trigger: 'change'}">
                      <Input placeholder=""
                             type="text"
                             v-model="formAddScore.items[index].criteriaArray[n].value"></Input>
                    </FormItem>
                    </Col>
                    <Icon type="md-add"
                          color="#19be6b"
                          size='16'
                          style="margin:0 6px;"
                          @click='addRuleBtn(index,n)' />
                    <Icon type="md-close"
                          color="#ed4014"
                          size='16'
                          v-if="formAddScore.items[index].criteriaArray.length > 1"
                          @click='removeRuleBtn(index,n)'/>
                    <Col span="4">
                    </Col>
                  </Row>
                </div>
              </div>
              <div class="box"
                   title="执行：该评分项得分">
                <!-- <p slot="title">执行：该评分项得分</p> -->
                <Button type="primary"
                        @click="showDrawer(index)"
                        style='margin-bottom:16px;'>选择评分项公式变量</Button>
                <p class="ruleType">请选择模型变量来编辑该项得分的计算公式，请用数字符号完成公式，数学符号包括：+-*\（），比如（a-(b-c)*d）</p>
                <FormItem :label-width="0"
                          :prop="'items.'+index+'.formulaDesc'"
                          :rules="validFormulaDesc">
                  <Input v-model="item.formulaDesc"
                         @compositionend.native="disZW($event,index)"
                         @mousemove.native="disTextSelect"
                         @mousedown.native="hasMousePress = true"
                         @mouseup.native="disCursorInVar"
                         :idx="index"
                         type="textarea"
                         :rows="4"
                         @on-keydown="enter($event,index)"
                         placeholder="请填写公式"></Input>
                </FormItem>
                <p style="margin-top: -21px;font-size: 12px; color: #f67373;">{{tips}}</p>
              </div>
            </div>
            </Col>
            <Col span="4"
                 style="margin: 20px 0;">
            <Icon type="md-add"
                  color="#19be6b"
                  size='20'
                  style="margin:0 16px;"
                  @click="addScoreBtn('formData',index)" />
            <Icon type="md-close"
                  color="#ed4014"
                  size='20'
                  @click='removeScoreBtn(index)' v-if="formAddScore.items.length > 1"/>
            </Col>
          </div>
        </Row>
        </Col>
        <Col span="20">
        <FormItem label="说明">
          <TextAreaBottom :value="formAddScore.itemExplain">
            <Input v-model="formAddScore.itemExplain"
                type="textarea"
                :maxlength="maxlength"
                @on-keyup="updateWords"
                :rows="4"
                placeholder="请填写说明"></Input>
          </TextAreaBottom>
        </FormItem>
        <FormItem v-if="this.$route.name === 'addScore'">
          <Button @click="Submit('formData')"
                  type="primary"
                  style="width: 60px;margin-right:20px;">保存</Button>
          <Button style="width: 60px"
                  @click="cancel()">取消</Button>
        </FormItem>
        </Col>
      </Row>
    </Form>
    <Drawer title="选择评分项公式变量"
            v-model="value3"
            width="800"
            :mask="false"
            :mask-closable="false">
      <div>
        <Select placeholder="支持模糊查询：最大长度为20个中英文字符"
                filterable
                clearable
                remote
                :remote-method="remoteSearchMethod"
                @on-change="handleSearchFiledChange"
                @on-clear="handleSearchFiledClear"
                class="search-input"
                style="width: 300px;margin-right:20px">
          <OptionGroup v-for="(item,index) in searchVariableList"
                       :key='index'
                       :label="item.title">
            <Option v-for="(list,index) in item[item.name]"
                    :key="index"
                    :value="list.columnCh">{{ list.columnCh }}</Option>
          </OptionGroup>
        </Select>
        <Button class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <div class="orderFormChoose">
        <ul>
          <li>
            <h4>模型基础字段:</h4>
            <span v-for="(item,i) in this.basicFiled"
                  :key='i'
                  :title='item.columnCh'
                  @click="hideDrawer(item.columnCh)">{{item.columnCh}}</span>
          </li>
          <li>
            <h4>业务流程中，用户在申请单填写的字段:</h4>
            <span v-for="(item,i) in this.applyFiled"
                  :key='i'
                  :title='item.columnCh'
                  @click="hideDrawer(item.columnCh)">{{item.columnCh}}</span>
          </li>
          <li>
            <h4>业务流程中，审核过程填写的字段:</h4>
            <span v-for="(item,i) in this.reviewFiled"
                  :key='i'
                  :title='item.columnCh'
                  @click="hideDrawer(item.columnCh)">{{item.columnCh}}</span>
          </li>
          <li>
            <h4>深华工程系统-项目数据:</h4>
          </li>
          <li>
            <h4>平台获取-个人数据:</h4>
          </li>
        </ul>
      </div>
    </Drawer>
  </div>
</template>
<script>
import { mapActions, mapMutations } from 'vuex'
import TextAreaBottom from '_c/common/text-area-bottom'

export default {
  components: {
    TextAreaBottom
  },
  data () {
    return {
      // 判断鼠标是否按下（按下时文本框不能进行退格操作）
      hasMousePress: false,
      maxlength: 1000,
      words: 0,
      drawerIndex: '',
      modelId: '',
      scoreId: '',
      value3: false,
      basicFiled: [
        {
          columnName: 'basic-score',
          columnCh: '该评分项得分'
        },
        {
          columnName: 'basic-fullMark',
          columnCh: '该项满分分值'
        }
      ],
      applyFiled: [],
      projectData: [],
      reviewFiled: [],
      searchVariableList: [],
      remoteSearchLoading: false,
      selecVariable: [],
      tips: '',
      optionOperator: [
        '==', '!=', '<', '>', '<=', '>='
      ],
      formAddScore: {
        itemExplain: '',
        items: [
          {
            criteriaType: '',
            formula: '',
            formulaDesc: '',
            criteriaJsons: '',
            criteria: '',
            criteriaDesc: '',
            criteriaArray: [
              {
                variable: '',
                operator: '',
                value: ''
              }
            ]
          }
        ]
      },
      validFormulaDesc: [
        {
          validator: (rule, value, callback) => {
            if (value === '') {
              callback(new Error('公式不能为空'))
            } else if (value.indexOf('[') === -1) {
              callback(new Error('至少选择一个变量'))
            } else {
              callback()
            }
          }
        }
      ]
    }
  },
  watch: {
    formAddScore: {
      handler (newValue, oldValue) {
        if (newValue.items.length > 0) {
          newValue.items.forEach((item) => {
            // 处理公式
            item.formula = this.handleCalculateFormula(item.formulaDesc)
          })
        }
      },
      deep: true
    }
  },
  mounted () {
    let id = this.$route.query.id
    this.scoreId = this.$route.query.scoreId
    this.modelId = id
    this.getVariableData()
    if (this.$route.name !== 'addScore') {
      this.getScoreDetail()
    }
  },
  methods: {
    ...mapActions([
      'scoreVariable', 'scoreItem', 'scoreDetail'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    handleCalculateFormula (formulaDesc) {
      let formula = formulaDesc
      for (let i = 0; i < formulaDesc.length; i++) {
        if (formulaDesc[i] === '[') {
          let right = 0
          for (let j = i + 1; j < formulaDesc.length; j++) {
            if (formulaDesc[j] === ']') {
              right = j
              break
            }
          }
          let columnCh = formulaDesc.substring(i + 1, right)
          let filedList = this.basicFiled.concat(this.applyFiled, this.reviewFiled)
          let columnName = filedList.find(item => item.columnCh === columnCh).columnName
          formula = formula.replace(columnCh, columnName)
        }
      }
      return formula
    },
    getScoreDetail () { // 编辑填充页面
      this.scoreDetail(this.scoreId).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.formAddScore.itemExplain = res.data.data.itemExplain
          this.words = this.formAddScore.itemExplain.length
          this.formAddScore.items = []
          res.data.data.formulaList.forEach((list, i) => {
            this.formAddScore.items.push({
              criteriaType: '',
              formula: '',
              formulaDesc: '',
              criteriaJsons: '',
              criteria: '',
              criteriaDesc: '',
              criteriaArray: [
                {
                  variable: '',
                  operator: '',
                  value: ''
                }
              ]
            })
            this.formAddScore.items[i].formula = list.formula
            this.formAddScore.items[i].formulaDesc = list.formulaDesc
            this.formAddScore.items[i].criteriaType = list.criteriaType + ''
            this.formAddScore.items[i].criteriaJsons = list.criteriaJsons.slice(0, list.criteriaJsons.length - 1)
            let criteriaD
            if (!this.formAddScore.items[i].criteriaJsons || this.formAddScore.items[i].criteriaJsons === '') {
              criteriaD = []
            } else {
              criteriaD = this.formAddScore.items[i].criteriaJsons.split(';')
            }
            var detailList = []
            criteriaD.forEach((criteria, n) => {
              var Ncriteria = JSON.parse(criteria)
              detailList.push(Ncriteria)
            })

            this.formAddScore.items[i].criteriaArray = detailList
          })
        }
      })
    },
    getVariableData () {
      this.scoreVariable(this.modelId).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.projectData = res.data.data.projectData
          this.reviewFiled = res.data.data.reviewFiled
          this.applyFiled = res.data.data.applyFiled
          // 用户申请字段
          var selecVariableList = {}
          selecVariableList.title = '业务申请字段'
          selecVariableList.name = 'applyFiled'
          var tempApplyList = []
          res.data.data.applyFiled.forEach((item, index) => {
            if (item.columnType === 'radio' || item.columnType === 'number') {
              item.fatherName = 'applyFiled'
              tempApplyList.push(item)
            }
          })
          selecVariableList.applyFiled = tempApplyList
          this.selecVariable.push(selecVariableList)
          // 业务审核字段
          var reviewFiledList = {}
          reviewFiledList.title = '业务审核字段'
          reviewFiledList.name = 'reviewFiled'
          var tempReviewList = []
          res.data.data.reviewFiled.forEach((item, index) => {
            if (item.columnType === 'radio' || item.columnType === 'number') {
              item.fatherName = 'reviewFiled'
              tempReviewList.push(item)
            }
          })
          reviewFiledList.reviewFiled = tempReviewList
          this.selecVariable.push(reviewFiledList)
        }
      })
    },
    handleCriteriaTypeChange (val, index) {
      if (parseInt(val) === 3) {
        this.formAddScore.items[index].criteriaJsons = ''
        this.formAddScore.items[index].criteriaArray = []
      } else {
        if (this.formAddScore.items[index].criteriaArray.length === 0) {
          this.addRuleBtn(index, null)
        }
      }
    },
    addScoreBtn (name, index) {
      if (this.formAddScore.items.length) {
        if (index || index === 0) {
          this.formAddScore.items.splice(index + 1, 0, {
            criteriaType: '',
            formula: '',
            formulaDesc: '',
            criteriaArray: [
              {
                variable: '',
                operator: '',
                value: ''
              }
            ]
          })
        } else {
          this.formAddScore.items.push({
            criteriaType: '',
            formula: '',
            formulaDesc: '',
            criteriaArray: [
              {
                variable: '',
                operator: '',
                value: ''
              }
            ]
          })
        }
      } else {
        this.formAddScore.items.push({
          criteriaType: '',
          formula: '',
          formulaDesc: '',
          criteriaArray: [
            {
              variable: '',
              operator: '',
              value: ''
            }
          ]
        })
      }
    },
    removeScoreBtn (index) {
      this.formAddScore.items.splice(index, 1)
    },
    addRuleBtn (index, n) {
      if (n || n === 0) {
        this.formAddScore.items[index].criteriaArray.splice(n + 1, 0, {
          variable: '',
          operator: '',
          value: ''
        })
      } else {
        this.formAddScore.items[index].criteriaArray.push({
          variable: '',
          operator: '',
          value: ''
        })
      }
    },
    removeRuleBtn (index, n) {
      this.formAddScore.items[index].criteriaArray.splice(n, 1)
    },
    showDrawer (index) {
      this.value3 = true
      this.drawerIndex = index
    },
    hideDrawer (columnCh) {
      this.formAddScore.items[this.drawerIndex].formulaDesc += `[` + columnCh + ']'
    },
    handleConcatCriteria () {
      this.formAddScore.items.forEach(item => {
        // 处理条件
        if (item.criteriaType === '' || item.criteriaType === '3') {
          item.criteriaDesc = 'if(true)'
          item.criteria = 'if(true)'
        } else {
          if (item.criteriaArray.length > 0) {
            let criteriaJson = ''
            let criteria = ''
            let criteriaDesc = ''
            item.criteriaArray.forEach((e, index) => {
              if (item.criteriaType === '1' && e.variable !== '') {
                let fatFiled = e.variable.split('-')[0]
                let varCh = this.selecVariable.find(s => s.name === fatFiled)[fatFiled].find(v => v.columnName === e.variable).columnCh

                let type = index === item.criteriaArray.length - 1 ? '' : ' || '
                criteriaDesc += '[' + varCh + ']' + e.operator + e.value + type
                criteria += '[' + e.variable + ']' + e.operator + e.value + type
              }
              if (item.criteriaType === '2' && e.variable !== '') {
                let fatFiled = e.variable.split('-')[0]
                let varCh = this.selecVariable.find(s => s.name === fatFiled)[fatFiled].find(v => v.columnName === e.variable).columnCh

                let type = index === item.criteriaArray.length - 1 ? '' : ' && '
                criteriaDesc += '[' + varCh + ']' + e.operator + e.value + type
                criteria += '[' + e.variable + ']' + e.operator + e.value + type
              }
              criteriaJson += JSON.stringify(e) + ';'
            })
            item.criteriaDesc = 'if(' + criteriaDesc + ')'
            item.criteria = 'if(' + criteria + ')'
            item.criteriaJsons = criteriaJson
          }
        }
      })
    },
    handleSubmit (name) { // 编辑
      // 拼接条件
      this.handleConcatCriteria()
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.$emit('subData', this.formAddScore, this.scoreId, 'form')
        } else {
          this.$Message.error('请完善信息再提交!')
        }
      })
    },
    Submit (name) { // 添加
      // 拼接条件
      this.handleConcatCriteria()
      if (this.$refs.formList.length) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.$emit('subData', this.formAddScore, this.modelId, 'form')
          } else {
            this.$Message.error('请完善信息再提交!')
          }
        })
      } else {
        this.$Message.error('请至少添加一条评分项!')
      }
    },
    enter (e, idx) {
      if (!e.key.match('^(\\d|Backspace|Enter|[(]|[)]|[+]|[-]|[*]|[/]|[{]|[}]|[.]|ArrowUp|ArrowDown|ArrowLeft|ArrowRight)$')) {
        // 阻止当前按键事件的发生
        e.preventDefault()
      }

      if (e.key.match('^Backspace$') && this.hasMousePress) {
        e.preventDefault()
        return
      }

      // 删除变量时整个删除
      if (e.key.match('^Backspace$') && e.target.selectionEnd > 0) {
        let value = e.target.value
        if (value.substring(e.target.selectionEnd - 1, e.target.selectionEnd) === ']') {
          e.preventDefault()

          let index = value.lastIndexOf('[')
          value = value.substring(0, index) + value.substring(e.target.selectionEnd, value.length)
          e.target.value = value
          this.formAddScore.items[idx].formulaDesc = value
          e.target.selectionEnd = index
        }
      }
    },
    // 禁止输入法
    disZW (e, idx) {
      let descPre = this.formAddScore.items[idx].formulaDesc
      let newDescPre = descPre.replace(new RegExp(e.data + '$'), '')
      e.target.value = newDescPre
      this.formAddScore.items[idx].formulaDesc = newDescPre
    },
    // 禁止鼠标选中文字（防止选中多文字删除）
    disTextSelect (e) {
      e.preventDefault()
    },
    // 禁止光标进入变量内
    disCursorInVar (e) {
      this.hasMousePress = false

      let selectionEnd = e.target.selectionEnd
      if (selectionEnd === 0) {
        return
      }

      let value = e.target.value
      let frontVal = value.substring(0, selectionEnd)
      let rearVal = value.substring(selectionEnd, value.length)
      if (frontVal.lastIndexOf('[') === -1) {
        return
      }

      if (frontVal.lastIndexOf('[') > frontVal.lastIndexOf(']')) {
        e.target.selectionEnd = selectionEnd + rearVal.indexOf(']') + 1
        e.target.selectionStart = selectionEnd + rearVal.indexOf(']') + 1
      }
    },
    cancel () {
      this.closeTag({
        name: this.$route.name,
        query: {
          id: this.modelId,
          type: this.$route.query.type
        }
      })
    },
    remoteSearchMethod (query) {
      this.searchVariableList = []
      if (query !== '') {
        this.remoteSearchLoading = true
        setTimeout(() => {
          this.remoteSearchLoading = false
          let list1 = this.basicFiled.filter(item => item.columnCh.toLowerCase().indexOf(query.toLowerCase()) > -1)
          if (list1.length > 0) {
            var basicObject = {}
            basicObject.title = '模型基础字段'
            basicObject.name = 'basicFiled'
            basicObject.basicFiled = list1
            this.searchVariableList.push(basicObject)
          }
          let list2 = this.applyFiled.filter(item => item.columnCh.toLowerCase().indexOf(query.toLowerCase()) > -1)
          if (list2.length > 0) {
            var applyObject = {}
            applyObject.title = '业务申请字段'
            applyObject.name = 'applyFiled'
            applyObject.applyFiled = list2
            this.searchVariableList.push(applyObject)
          }
          let list3 = this.reviewFiled.filter(item => item.columnCh.toLowerCase().indexOf(query.toLowerCase()) > -1)
          if (list3.length > 0) {
            var reviewObject = {}
            reviewObject.title = '业务审核字段'
            reviewObject.name = 'reviewFiled'
            reviewObject.reviewFiled = list3
            this.searchVariableList.push(reviewObject)
          }
        }, 200)
      } else {
        this.searchVariableList = []
      }
    },
    handleSearchFiledClear () {
      this.searchVariableList = []
    },
    handleSearchFiledChange (v) {
      if (v !== undefined) {
        this.hideDrawer(v)
      }
    },
    updateWords (e) {
      this.words = e.target.value.length
    }
  }
}
</script>
<style lang="less">
ul {
  list-style: none;
}
.orderFormChoose ul li h4 {
  line-height: 40px;
  font-size: 15px;
}
.orderFormChoose ul li {
  padding: 10px;
  margin: 20px 0;
  line-height: 30px;
  background-color: #ecf0f6;
}
.orderFormChoose ul li span {
  width: 13.9%;
  word-spacing: 2px;
  padding: 3px;
  border: 1px solid #cccccc;
  padding: 0px 10px;
  border-radius: 4px;
  margin: 0 10px;
  display: inline-block;
  text-align: center;
  color: #555555;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.orderFormChoose ul li span:hover {
  border: 1px solid #2c8ef3;
  color: #2c8ef3;
}
.ivu-icon {
  cursor: pointer;
}
.box {
  padding: 40px 20px 25px;
  position: relative;
  border: 1px solid #ecf0f6;
  border-radius: 4px;
}
.box::before {
  content: attr(title);
  position: absolute;
  left: 15%;
  transform: translateX(-50%);
  -webkit-transform: translate(-50%, -57px);
  padding: 4px 13px;
  background-color: #fff;
  border: 1px solid #ecf0f6;
  color: #8791a0;
  font-size: 12px;
  border-radius: 4px;
}
.ruleType {
  font-size: 12px;
  color: #8791a0;
  margin-bottom: 10px;
}

.ivu-input-wrapper > .ivu-input {
  user-select: none;
  -ms-user-select: none;
  -moz-user-select: none;
  -webkit-user-select: none;
}
</style>
