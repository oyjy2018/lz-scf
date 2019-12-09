<style lang="less">
@import "../../../index.less";
</style>

<template>
  <div class="apply">
    <Card title="">
      <h3 style="margin-bottom: 20px">申请开商票</h3>
      <MyForm style="width: 550px"
              :model="form"
              ref="form1"
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
                               :item="subItemSub"
                               :index="subIndex"
                               :citysNum="2"
                               :form="form"
                               :attrList="subItem.attrList"
                               :group="subItem.columnGroup"
                               @on-save="onSaveDraft" />
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
                             :citysNum="2"
                             @on-save="onSaveDraft" />
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
      'deleteFileById'
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
      this.form.creditIndex = value + ''
      let name = 'creditTicketApply_creditId creditTicketApply_creditApplyId creditTicketApply_applyUserName creditTicketApply_applyUserIdCard creditTicketApply_creditItemId creditTicketApply_creditItemName'
      let values = `${credit.id} ${credit.creditApplyId} ${credit.customerName} ${credit.idCard} ${credit.itemId} ${credit.itemName}`
      this.onSaveDraft({
        tableName: name,
        value: values,
        index: -1
      })
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
