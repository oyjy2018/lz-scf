<style lang="less">
@import "../../../index.less";
.form-steps-context {
  margin-top: 30px;
}
.form-steps-item {
  margin-bottom: 10px;
}
</style>

<template>
  <div ref="container"
       class="container">
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
          <div class="form-steps-title">
            <div class="form-steps-title-num"
                 @click="collapse(index)">
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
            <div class="form-steps-title-text"
                 @click="collapse(index)">
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
                             :step="step"
                             :personList="formAttrList[0].attrList"
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
                           :step="step"
                           :personList="formAttrList[0].attrList"
                           @on-save="onSaveDraft" />
              </div>
            </div>
            <!-- 切换步骤按钮 -->
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
      form: {},
      rules: {},
      businessId: '',
      businessTypeId: '1',
      businessType: '申请授信',
      attrValList: [],
      formAttrList: [1],
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
    const data = {
      businessTypeId: this.businessTypeId,
      businessType: this.businessType,
      queryType: 'create'
    }
    this.findBusinessAttrCfg(data).then(res => {
      if (res && res.status === 200 && res.data && res.data.code === 10000) {
        const data1 = res.data.data
        console.log(data1)

        const formAttrList = []
        // from
        const form = {}

        data1.formAttrList.forEach(item => {
          let obj = {}
          obj.classifyName = item.classifyName
          obj.attrList = []
          let subObj = {
            columnGroup: '',
            attrList: []
          }
          // from
          let fromArr = {}
          item.attrList.forEach(subItem => {
            if (subItem.columnType === 'radio' || subItem.columnType === 'select') {
              subItem.list = data1.attrValList.filter(sitem => {
                if (subItem.businessAttrId === sitem.businessAttrId) {
                  return sitem
                }
              })
            }
            if (subItem.columnGroup) {
              subObj.columnGroup = subItem.columnGroup
              subObj.attrList.push(subItem)
              // from
              form[subItem.columnGroup] = []
              fromArr = Object.assign(fromArr, { [subItem.columnName]: subItem.columnName.indexOf('Province') === -1 ? '' : [] })
              form[subItem.columnGroup] = [...[fromArr]]
            } else {
              obj.attrList.push(subItem)
              // from
              form[subItem.columnName] = subItem.columnName.indexOf('Province') === -1 ? '' : []
            }
          })
          if (subObj.columnGroup) {
            obj.attrList.push(subObj)
          }
          formAttrList.push(obj)
        })

        this.form = form
        this.formAttrList = formAttrList
        this.data = data1
      }
    })
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
      console.log(subItem)
      console.log(index)
      console.log(subIndex)
      console.log(this.form)
      const len = this.form[subItem.columnGroup].length
      const obj = this.form[subItem.columnGroup][len - 1]
      console.log(obj)
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
