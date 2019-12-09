<style lang="less">
@import "../../../index.less";
</style>

<template>
  <div class="apply">
    <Card title="">
      <h3 style="margin-bottom: 20px">修改申请授信</h3>
      <MyForm style="width: 700px"
              ref="form1"
              :model="form"
              :label-width="250">
        <Collapse accordion
                  simple
                  v-model="accordion">
          <Panel v-for="(item, index) in formAttrList"
                 :key="index"
                 :name="index+1+''">
            <span style="font-size: 16px">
              <Badge :count="index+1"
                     type="primary"></Badge>
            </span><span style="font-size: 16px; margin-left: 10px;">{{item.classifyName}} </span>
            <div slot="content"
                 style="margin-top: 20px">
              <div v-for="(subItem, subIndex) in item.attrList"
                   :key="subItem.columnName">
                <div v-if="subItem.columnGroup">
                  <div v-for="(subItemSub, subIndexSub) in subItem.attrList"
                       :key="subItemSub.columnCh">
                    <ApplyForm :model="form[subItem.columnGroup][subIndex][subItemSub.columnName]"
                               :item="subItemSub"
                               :index="subIndex"
                               :form="form"
                               :attrList="subItem.attrList"
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
                                long
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
            </div>
          </Panel>
        </Collapse>
      </MyForm>

      <br><br>
      <Button @click="handleSubmit"
              type="primary"
              style="width: 150px">提交申请</Button>
      <br><br>
    </Card>
  </div>
</template>

<script>
import MyForm from '_c/my-form'
import ApplyForm from '_c/apply-form'
import { mapActions, mapMutations } from 'vuex'
import { CITYS } from '@/libs/citys.js'
import { deepCopy, toObjValue } from '@/libs/util.js'

export default {
  name: 'edit',
  components: {
    MyForm,
    ApplyForm
  },
  data () {
    return {
      citys: CITYS,
      accordion: '1',
      form: {},
      rules: {},
      companyId: '',
      businessId: '',
      businessTypeId: '1',
      businessType: '申请授信',
      creditApplyId: '',
      flowCode: '',
      attrValList: [],
      formAttrList: [1]
    }
  },
  mounted () {
    const { companyId, businessTypeId, creditApplyId, flowCode } = this.$route.query
    if (creditApplyId) {
      this.companyId = companyId
      this.businessTypeId = businessTypeId
      this.businessId = creditApplyId
      this.flowCode = flowCode
      this.getData()
    }
  },
  methods: {
    ...mapActions([
      'getDetail',
      'saveDraft',
      'commitApply',
      'deleteCreditItem'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getData () {
      const data = {
        companyId: this.companyId,
        businessTypeId: this.businessTypeId,
        creditApplyId: this.businessId,
        flowCode: this.flowCode,
        queryType: 'edit'
      }
      this.getDetail(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          const data1 = res.data.data
          const formAttrList = []
          // from
          const form = {}

          data1.formAttrList = data1.formAttrList.forEach(item => {
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
          this.form = toObjValue(data1.creditApplyDetails, form)
          this.formAttrList = formAttrList
          this.checkDetailsArr(data1.creditApplyDetails)
        }
      })
    },
    // 检查creditApplyDetails数据表单的类似item为数组的是否有大于2，大于2则渲染表单要添加数组对应进行渲染
    checkDetailsArr (form) {
      for (let k in form) {
        if (form[k] instanceof Array && form[k].length) {
          this.formAttrList.map((item, index) => {
            item.attrList.forEach(subItem => {
              if (subItem.columnGroup === k) {
                for (let i = 0; i < form[k].length - 1; i++) {
                  this.formAttrList[index]['attrList'].push(subItem)
                }
              }
            })
          })
        }
      }
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
      if (valid) {
        this.saveDraft(data).then(res => {
          if (res && res.status === 200 && res.data && res.data.code === 10000) {
            this.businessId = res.data.data.businessId
            if (tableName === 'creditApply_idCard') {
              this.discriCard(value)
            }
          }
        })
      }
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
                  name: 'editapply',
                  query: {
                    companyId: this.$route.query.companyId,
                    businessTypeId: this.$route.query.businessTypeId,
                    creditApplyId: this.$route.query.creditApplyId,
                    flowCode: this.$route.query.flowCode
                  }
                })
                this.$Modal.success({
                  title: '提示',
                  content: '提交申请成功'
                })
              }
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
