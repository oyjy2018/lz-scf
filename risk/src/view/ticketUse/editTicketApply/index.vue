<style lang="less">
@import "../../../index.less";
.requiredClass .ivu-form-item-label::after {
  content: '*';
  display: inline-block;
  margin-left: 5px;
  line-height: 1;
  // font-family: SimSun;
  font-size: 6px;
  color: #f67373;
}
</style>

<template>
  <div class="container">
    <div class="title-nav">
      <div class="title-text">编辑申请开商票</div>
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
              <FormItem label="关联授信"
                        prop="creditIndex">
                <Select v-model="creditIndex"
                        placeholder="选择关联授信"
                        name="creditIndex"
                        @on-change="(value) => handleChangeCreditList(value)">
                  <Option v-for="(credit, cIndex) in creditList"
                          :key="credit.itemName"
                          :value="cIndex">{{credit.itemName}}</Option>
                </Select>
              </FormItem>
              <FormItem label="申请人"
                        prop="creditIndex">
                <Input :value="creditList[creditIndex] && creditList[creditIndex].customerName"
                       placeholder="申请人"
                       disabled></Input>
              </FormItem>
              <FormItem label="身份证号"
                        prop="creditIndex">
                <Input :value="creditList[creditIndex] && creditList[creditIndex].idCard"
                       placeholder="身份证号"
                       disabled></Input>
              </FormItem>
              <FormItem label="授信工程项目"
                        prop="creditIndex">
                <Input :value="creditList[creditIndex] && creditList[creditIndex].itemName"
                       placeholder="授信工程项目"
                       disabled></Input>
              </FormItem>
              <FormItem label="申请业务类型"
                        prop="creditIndex">
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
                           :item="subItem"
                           :citysNum="2"
                           :form="form"
                           :attrList="item.attrList"
                           :useAbleMoney = "useAbleMoney"
                           @on-save="onSaveDraft" />
              </div>
            </div>
            <div v-if="index === (formAttrList.length - 1) && data && data.trailingWorkFlowCfg[0]">
              <FormItem v-for="sitem in data.trailingWorkFlowCfg[0].workFlowFileVOList"
                        :class="{requiredClass: sitem.required}"
                        :key="sitem.fileName"
                        :label="sitem.fileName">
                <span style="color: #999">最多上传{{sitem.uploadLimit}}张，每个文件大小不超过{{sitem.onceUploadSize}}M</span>
                <Upload multiple
                        :max-size="sitem.onceUploadSize * 1024"
                        accept=".doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.bmp,.txt,.rar,.zip,.pdf"
                        :format="['doc','docx','xls','xlsx','ppt','pptx','png','jpg','jpeg','gif','bmp','txt','rar','zip','pdf']"
                        :default-file-list="form.fileList"
                        :on-success="(res, file, fileList) => handleSuccess(res, file, fileList, sitem)"
                        :on-preview="handlePreview"
                        :on-remove="(file, fileList) => handleRemove(file, fileList, sitem)"
                        :on-format-error="handleFormatError"
                        :on-exceeded-size="(file) => handleMaxSize(file, sitem.onceUploadSize)"
                        :before-upload="() => handleBeforeUpload(sitem)"
                        type="drag"
                        :action="upUrl">
                  <div style="padding: 20px 0">
                    <Icon type="ios-cloud-upload"
                          size="52"
                          style="color: #3399ff"></Icon>
                    <p>添加 | 拖曳上传 </p>
                  </div>
                </Upload>
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
    <Modal v-model="visible"
           width="800px"
           :footer-hide="true"
           title="查看文件">
      <iframe :src="viewUrl"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
    </Modal>
  </div>
</template>

<script>
import MyForm from '_c/my-form'
import ApplyForm from '_c/apply-form'
import { mapState, mapActions, mapMutations } from 'vuex'
import { CITYS } from '@/libs/citys.js'
import { deepCopy, toObjValue } from '@/libs/util.js'
import config from '@/config'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'editticketapply',
  components: {
    MyForm,
    ApplyForm
  },
  data () {
    return {
      useAbleMoney: 0,
      step: 0,
      visible: false,
      viewUrl: '',
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
      creditIndex: 0,
      data: null,
      primaryId: ''
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    const { companyId, creditApplyId } = this.$route.query
    if (creditApplyId) {
      this.companyId = companyId
      // this.businessTypeId = businessTypeId
      this.businessId = creditApplyId
      this.creditApplyId = creditApplyId
      // this.flowCode = flowCode
      this.getData()
    }
  },
  methods: {
    ...mapActions([
      'findCreditTicketCfg',
      'findCreditUseApplyDetails',
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
          this.creditRecordId = this.creditList[0].id
          this.data = data1
          this.getDetails()
          this.handleChangeCreditUseAbleMoney()
        }
      })
    },
    getDetails () {
      const data = {
        id: this.creditApplyId
        // companyId: this.companyId,
        // businessTypeId: this.businessTypeId,
        // creditApplyId: this.creditApplyId,
        // flowCode: this.flowCode,
        // queryType: 'view'
      }
      this.findCreditUseApplyDetails(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          const data1 = res.data.data
          this.creditList.forEach((item, index) => {
            if (item.itemId === res.data.data.formData.creditItemId) {
              this.creditIndex = index
            }
          })
          data1.formData.fileList.map(item => {
            item.name = item.originalFileName
            item.url = item.viewFileUrl
            return item
          })
          this.form = toObjValue(data1.formData, this.form)
        }
      })
    },
    handleChangeCreditList (value) {
      const credit = this.creditList[value]
      this.creditRecordId = credit.id
      this.creditIndex = value
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
    handleSuccess (res, file, fileList, sitem) {
      if (res && res.code === 10000) {
        res.data.fileCode = sitem.fileCode
        // this.formData.fileList.push(res.data)
        this.onTicketUsesaveFile(res.data)
        // this.formData.uploadDeleteFileDesc = this.formData.uploadDeleteFileDesc === ''
        //   ? `${sitem.fileName}: ${res.data.originalFileName} 上传`
        //   : `${this.formData.uploadDeleteFileDesc};${sitem.fileName}: ${res.data.originalFileName} 上传`
      }
    },
    handlePreview (file) {
      if (file && file.viewFileUrl) {
        if (file.fileType === 'image' || file.fileType === 'pdf') {
          this.visible = true
          this.viewUrl = file.viewFileUrl
        } else {
          this.$Notice.warning({
            title: '提示',
            desc: '该文件类型暂不能预览'
          })
        }
      } else {
        if (file && file.percentage === 100 && (file.response.data.fileType === 'image' || file.response.data.fileType === 'pdf')) {
          this.visible = true
          this.viewUrl = file.response.data.viewFileUrl
        } else {
          this.$Notice.warning({
            title: '提示',
            desc: '该文件类型暂不能预览'
          })
        }
      }
    },
    handleRemove (file) {
      this.form.fileList = this.form.fileList.filter(item => {
        if (file.newFileName !== item.newFileName) {
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
    handleFormatError (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '文件类型错误'
      })
    },
    handleMaxSize (file, onceUploadSize) {
      this.$Notice.warning({
        title: '提示',
        desc: `文件大于${onceUploadSize}M`
      })
    },
    handleBeforeUpload (sitem) {
      const arr = this.form.fileList.filter(item => {
        if (sitem.fileCode === item.fileCode) {
          return item
        }
      })
      const check = arr.length < sitem.uploadLimit
      if (!check) {
        this.$Notice.warning({
          title: `最多上传${sitem.uploadLimit}个文件`
        })
      }
      return check
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
          this.form.fileList.push(res.data.data.file)
          this.businessId = res.data.data.businessId
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
      this.$refs['form1'].validate((valid) => {
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
                this.$Modal.success({
                  title: '提示',
                  content: '提交申请成功'
                })
                this.closeTag({
                  name: 'editticketapply',
                  query: {
                    companyId: this.$route.query.companyId,
                    businessTypeId: this.$route.query.businessTypeId,
                    creditApplyId: this.$route.query.creditApplyId,
                    flowCode: this.$route.query.flowCode
                  }
                })
              }
            })
          }
        } else {
          this.$Message.error({
            content: '请填写完整信息',
            duration: 5,
            closable: true
          })
        }
      })
    }
  }
}
</script>
