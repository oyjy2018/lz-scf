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
  <div class="container">
    <div class="title-nav">
      <div class="title-text">申请开商票</div>
      <div class="nav-text">
        <!-- <span class="nav-text-active">草稿</span>
        <span> > 客户经理初审</span> -->
      </div>
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
            <div v-if="index === 0">
              <MyFormItem label="关联授信"
                          :rules="{required: true, message: '请选择关联授信', trigger: 'change'}"
                          prop="creditIndex">
                <Select v-model="creditIndex"
                        placeholder="选择关联授信"
                        name="creditIndex"
                        @on-change="(value) => handleChangeCreditList(value)">
                  <Option v-for="(credit, cIndex) in creditList"
                          :key="credit.itemName"
                          :value="cIndex">{{credit.itemName}}</Option>
                </Select>
              </MyFormItem>
              <FormItem label="申请人">
                <Input :value="creditList[creditIndex] && creditList[creditIndex].customerName"
                       placeholder="申请人"
                       disabled></Input>
              </FormItem>
              <FormItem label="身份证号">
                <Input :value="creditList[creditIndex] && creditList[creditIndex].idCard"
                       placeholder="身份证号"
                       disabled></Input>
              </FormItem>
              <FormItem label="授信工程项目">
                <Input :value="creditList[creditIndex] && creditList[creditIndex].itemName"
                       placeholder="授信工程项目"
                       disabled></Input>
              </FormItem>
              <FormItem label="申请业务类型">
                <Input value="申请开商票"
                       placeholder="申请业务类型"
                       disabled></Input>
              </FormItem>
            </div>
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
                           :useAbleMoney = "useAbleMoney"
                           @on-save="onSaveDraft"/>
              </div>
            </div>
            <div v-if="index === (formAttrList.length - 1) && data && data.trailingWorkFlowCfg[0]">
              <FormItem v-for="sitem in data.trailingWorkFlowCfg[0].workFlowFileVOList"
                        :key="sitem.fileName"
                        :label="sitem.fileName">
                <UploadFile multiple
                            :sitem="sitem"
                            accept=".doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.bmp,.txt,.rar,.zip,.pdf"
                            :format="['doc','docx','xls','xlsx','ppt','pptx','png','jpg','jpeg','gif','bmp','txt','rar','zip','pdf']"
                            @on-success="handleSuccess"
                            @on-remove="handleRemove"
                            :action="upUrl" />
              </FormItem>
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
import MyFormItem from '_c/my-form-item'
import ApplyForm from '_c/apply-form'
import UploadFile from '_c/upload-file'
import { mapState, mapActions, mapMutations } from 'vuex'
import { CITYS } from '@/libs/citys.js'
import { deepCopy } from '@/libs/util.js'
import config from '@/config'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'ticketapply',
  components: {
    MyForm,
    MyFormItem,
    ApplyForm,
    UploadFile
  },
  data () {
    return {
      useAbleMoney: 0,
      step: 0,
      upUrl: upUrl + 'file/upload',
      citys: CITYS,
      accordion: '1',
      form: {},
      companyId: '',
      businessId: '',
      businessTypeId: '2',
      attrValList: [],
      formAttrList: [1],
      creditRecordId: '',
      creditTicketApplyId: '',
      flowCode: 'ticket1',
      queryType: 'create',
      creditList: [],
      creditIndex: -1,
      data: null,
      primaryId: '',
      fileList: []
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    const { companyId, creditRecordId, creditTicketApplyId } = this.$route.query
    if (creditRecordId) {
      this.companyId = companyId
      this.creditRecordId = creditRecordId
      this.creditTicketApplyId = creditTicketApplyId
    }
    this.getData()
  },
  methods: {
    ...mapActions([
      'findCreditTicketCfg',
      'saveDraftTicket',
      'applyCommitTicket',
      'ticketUsesaveFile',
      'deleteFileById',
      'getCreditUseAbleMoney'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getData () {
      const data = {
        businessTypeId: this.businessTypeId,
        queryType: this.queryType,
        creditRecordId: this.creditRecordId,
        creditTicketApplyId: this.creditTicketApplyId,
        flowCode: this.flowCode
      }
      this.findCreditTicketCfg(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          const data1 = res.data.data
          const formAttrList = []
          // from
          const form = {
            creditIndex: ''
          }

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
          this.form = form
          this.formAttrList = formAttrList
          this.creditList = data1.creditList
          this.companyId = data1.creditList[0].companyId
          this.data = data1
          if (this.creditRecordId) {
            this.handleChangeCreditList(0)
          }
        }
      })
    },
    handleChangeCreditList (value) {
      const credit = this.creditList[value]
      this.creditIndex = value
      this.creditRecordId = credit.id
      this.form.creditIndex = value + ''
      let name = 'creditTicketApply_creditId creditTicketApply_creditApplyId creditTicketApply_applyUserName creditTicketApply_applyUserIdCard creditTicketApply_creditItemId creditTicketApply_creditItemName'
      let values = `${credit.id} ${credit.creditApplyId} ${credit.customerName} ${credit.idCard} ${credit.itemId} ${credit.itemName}`
      this.onSaveDraft({
        tableName: name,
        value: values,
        index: -1
      })
      this.handleChangeCreditUseAbleMoney()
    },

    // 更新项目可用余额
    handleChangeCreditUseAbleMoney () {
      const data = {
        id: this.creditRecordId
      }
      this.getCreditUseAbleMoney(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.useAbleMoney = res.data.data.remainCreditAmount
        }
      })
    },

    collapse (index) {
      this.step = index === this.step ? -1 : index
    },
    handleAdd (subItem, index, subIndex) {
      this.form[subItem.columnGroup].push(deepCopy(this.form[subItem.columnGroup][0], true))
      this.formAttrList[index]['attrList'].push(subItem)
    },
    handleSuccess ({ res, sitem }) {
      if (res && res.code === 10000) {
        res.data.name = res.data.originalFileName
        res.data.fileCode = sitem.fileCode
        this.onTicketUsesaveFile(res.data)
      }
    },
    // handleSuccess (res, file, fileList, sitem) {
    //   if (res && res.code === 10000) {
    //     res.data.fileCode = sitem.fileCode
    //     this.fileList.push(res.data)
    //     this.onTicketUsesaveFile(res.data)
    //     // this.formData.uploadDeleteFileDesc = this.formData.uploadDeleteFileDesc === ''
    //     //   ? `${sitem.fileName}: ${res.data.originalFileName} 上传`
    //     //   : `${this.formData.uploadDeleteFileDesc};${sitem.fileName}: ${res.data.originalFileName} 上传`
    //   }
    // },
    handleRemove ({ file }) {
      this.fileList = this.fileList.filter(item => {
        if (file.response.data.newFileName !== item.newFileName) {
          return item
        } else {
          this.deleteFileById({ id: item.id }).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.$Message.success('删除文件成功')
            }
          })
        }
      })
    },
    onTicketUsesaveFile (file) {
      const data = {
        companyId: this.companyId,
        businessTypeId: this.businessTypeId,
        businessId: this.businessId,
        workFlowId: this.data.trailingWorkFlowCfg[0].id,
        flowCode: this.data.trailingWorkFlowCfg[0].beforeFlow,
        file
      }
      this.ticketUsesaveFile(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.businessId = res.data.data.businessId
          this.fileList.push(res.data.data.file)
        }
      })
    },
    onSaveDraft ({ group, tableName, value, index }) {
      const data = {
        companyId: this.companyId,
        businessTypeId: this.businessTypeId,
        businessId: this.businessId,
        columnName: tableName,
        value,
        index,
        flowCode: this.flowCode,
        primaryId: this.primaryId
      }
      if (group) {
        this.form[group][index][tableName] = value
      } else {
        this.form[tableName] = value
      }
      if (tableName === 'creditTicketApply_ticketGetBankProvince') {
        data.columnName = 'creditTicketApply_ticketGetBankProvince creditTicketApply_ticketGetBankCity'
        data.value = value.join(' ')
      }
      this.saveDraftTicket(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.businessId = res.data.data.businessId
          this.primaryId = res.data.data.primaryId
        }
      })
    },
    handleSubmit () {
      this.$refs['form1'].validate((valid, errors) => {
        if (valid) {
          if (this.businessId) {
            const data = {
              businessId: this.businessId,
              workFlowId: this.data.trailingWorkFlowCfg[0].id
            }
            this.applyCommitTicket(data).then(res => {
              if (res && res.status === 200 && res.data && res.data.code === 10000) {
                this.businessId = ''
                this.from = deepCopy(this.from, true)
                this.closeTag({
                  name: 'ticketapply',
                  query: {
                    companyId: this.companyId,
                    creditRecordId: this.$route.query.id,
                    businessTypeId: this.$route.query.businessTypeId,
                    creditTicketApplyId: '',
                    flowCode: this.$route.query.flowCode
                  }
                })
                this.$router.push({
                  name: 'myticketapply'
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
