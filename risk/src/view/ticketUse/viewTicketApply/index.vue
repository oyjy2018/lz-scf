<style lang="less">
@import "../../../index.less";
.content-div {
  margin-bottom: 80px;
  .buttons {
    margin-left: 70px;
    margin-bottom: 20px;
  }
}
.comment {
  width: 800px;
  padding: 20px;
  margin-left: 70px;
  background: #f9f9f9;
  &-title {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    &-name {
      color: @title-color;
    }
    &-status {
      flex: 1;
      text-align: center;
    }
    &-time {
      color: #999;
    }
  }
  &-content {
    font-size: 16px;
    color: @title-color;
  }
}
</style>

<template>
  <div class="apply">
    <Card title=""
          style="margin-bottom: 40px">
      <h3 style="margin-bottom: 20px">用信申请详情
        <FlowIndicator :forwardFlow="forwardFlow"
                       :flowCode="flowCode" />
      </h3>
      <!-- 详情 -->
      <Tabs :value="accordion"
            @on-click="onClickTab">
        <TabPane v-for="(item, index) in formAttrList"
                 :key="index"
                 :label="item.classifyName"
                 v-if="item.isHide !== true"
                 :name="index+1+''">
        </TabPane>
      </Tabs>
      <div v-for="(item, index) in formAttrList"
           :key="index"
           :label="item.classifyName"
           :name="index+1+''">
        <template v-if="accordion === index+1+''">
          <div style="margin-top: 20px">
            <Form style="width: 550px"
                  :model="form"
                  :label-width="250">
              <div v-if="index === 0">
                <FormItem label="关联授信">
                  {{form.creditItemName}}
                </FormItem>
                <FormItem label="申请人">
                  {{form.applyUserName}}
                </FormItem>
                <FormItem label="身份证号">
                  {{form.applyUserIdCard}}
                </FormItem>
                <FormItem label="授信工程项目">
                  {{form.creditItemName}}
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
                               :group="subItem.columnGroup"
                               :form="form"
                               :citysNum="2"
                               :attrList="subItem.attrList"
                               :isRules="false"
                               disabled />
                    <!-- 项目分割线 -->
                    <Divider v-if="subIndexSub === subItem.attrList.length -1 && !(subIndex === item.attrList.length - 1)"
                             dashed />
                  </div>

                </div>
                <div v-else>
                  <ApplyForm :model="form[subItem.columnName]"
                             :item="subItem"
                             :form="form"
                             :citysNum="2"
                             :attrList="item.attrList"
                             :isRules="false"
                             disabled />
                </div>
              </div>
            </Form>
          </div>
        </template>
      </div>
      <div v-if="accordion === formAttrList.length-2+''">
        <FilesTable v-if="fileList.length"
                    :list="formFileCfgList"
                    :fileList="fileList" />
      </div>
      <div v-if="accordion === formAttrList.length-1+''">
        <div style="margin-top: 20px">
          <Table :columns="auditColumns"
                 :data="auditLogData.list"></Table>
          <div class="pagination">
            <Page :total="auditLogData.total"
                  :current="auditLogData.current"
                  :page-size="auditLogData.size"
                  @on-change="changePageAuditLog"
                  show-elevator
                  show-total></Page>
          </div>
        </div>
      </div>
      <div v-if="accordion === formAttrList.length+''">
        <div style="margin-top: 20px">
          <Table :columns="operateColumns"
                 :data="operateLogData.list"></Table>
          <div class="pagination">
            <Page :total="operateLogData.total"
                  :current="operateLogData.current"
                  :page-size="operateLogData.size"
                  @on-change="changePageOperateLog"
                  show-elevator
                  show-total></Page>
          </div>
        </div>
      </div>
    </Card>
    <Card title=""
          style="margin-bottom: 40px">
      <!-- 详情 -->
      <!-- 评论 -->
      <Divider orientation="left">
        <Icon type="ios-chatbubbles"
              size="20"
              color="#2C8EF3"
              style="margin-right: 15px;" /><strong>评论</strong></Divider>
      <div class="content-div">
        <div class="comment"
             v-for="item in commentList"
             :key="item.id+'comment'">
          <div class="comment-title">
            <div class="comment-title-name">{{item.createBy}}</div>
            <div class="comment-title-status">{{item.statusDesc}}</div>
            <div class="comment-title-time">{{item.createTime}}</div>
          </div>
          <Divider dashed />
          <div class="comment-content">
            {{item.content}}
          </div>
        </div>
      </div>
    </Card>
    <!-- 评论 -->
  </div>
</template>

<script>
import ApplyForm from '_c/apply-form'
import { mapActions, mapMutations, mapState } from 'vuex'
import FilesTable from '_c/files-table'
import { CITYS } from '@/libs/citys.js'
import { deepCopy, toObjValue } from '@/libs/util.js'
import FlowIndicator from '../../business/flowIndicator'

export default {
  name: 'viewticketapply',
  components: {
    FilesTable,
    ApplyForm,
    FlowIndicator
  },
  data () {
    return {
      citys: CITYS,
      accordion: '1',
      trailingActive: 0,
      form: {},
      fileList: [],
      creditApplyId: '',
      companyId: '',
      flowCode: '',
      queryType: 'view',
      businessTypeId: '2',
      creditRecordId: '',
      attrValList: [],
      creditIndex: -1,
      creditList: [],
      formAttrList: [{ classifyName: '文件资料', attrList: [], isHide: true }, { classifyName: '审核日志', attrList: [], isHide: true }, { classifyName: '操作日志', attrList: [], isHide: true }],
      trailingWorkFlowCfg: [],
      formFileCfgList: [],
      commentList: [],
      operateLogData: {
        list: [],
        size: 10,
        current: 1,
        total: 0
      },
      auditLogData: {
        list: [],
        size: 10,
        current: 1,
        total: 0
      },
      operateColumns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '操作时间',
          width: 260,
          key: 'operateTime'
        },
        {
          title: '操作人',
          width: 260,
          key: 'operateUserName'
        },
        {
          title: '操作内容',
          key: 'operateContent',
          render: (h, params) => {
            let html = ''
            if (params.row.operateContent) {
              params.row.operateContent.split('/r/n').forEach(item => {
                html += `<p style="padding: 10px 0;">${item}</p>`
              })
            }
            return h('div', {
              domProps: {
                innerHTML: html
              }
            })
          }
        }
      ],
      auditColumns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '审核时间',
          width: 160,
          key: 'auditTime'
        },
        {
          title: '审核时长',
          width: 100,
          key: 'totalTimeConsuming'
        },
        {
          title: '审核人',
          width: 160,
          key: 'auditPerson'
        },
        {
          title: '审核前',
          width: 160,
          key: 'auditBeforeFlow'
        },
        {
          title: '审核后',
          width: 160,
          key: 'auditAfterFlow'
        },
        {
          title: '审核内容',
          key: 'auditOpinion',
          render: (h, params) => {
            let html = ''
            if (params.row.auditOpinion) {
              params.row.auditOpinion.split('/r/n').forEach(item => {
                html += `<p style="padding: 10px 0;">${item}</p>`
              })
            }
            return h('div', {
              domProps: {
                innerHTML: html
              }
            })
          }
        }
      ],
      formData: {
        creditApplyId: '',
        beforeFlow: '',
        afterFlow: '',
        disposeRoleIds: '',
        disposeUserIds: '',
        uploadDeleteFileDesc: '',
        flowDesc: '',
        statusDesc: '',
        flowId: '',
        commentContent: '',
        fileList: [],
        formDataList: []
      },
      forwardFlow: []
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    this.handleHideTabControl()
    const { companyId, businessTypeId, creditApplyId, flowCode } = this.$route.query
    if (creditApplyId) {
      this.companyId = companyId
      this.businessTypeId = businessTypeId
      this.creditApplyId = creditApplyId
      this.flowCode = flowCode
      this.getData()
    }
  },
  methods: {
    ...mapActions([
      'findCreditTicketCfg',
      'findCreditUseApplyDetails',
      'getCommentList',
      'getTicketAuditLogList',
      'getTicketOperateLogList',
      'getTicketDocument',
      'getBusinessFlowForward'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    // 根据权限隐藏tab
    handleHideTabControl () {
      // 有文件资料权限
      if (this.permission.indexOf('risk:credit:use:document') !== -1) {
        this.formAttrList[0].isHide = false
      }
      // 有审核权限
      if (this.permission.indexOf('risk:credit:use:audit:log') !== -1) {
        this.formAttrList[1].isHide = false
      }
      // 有查看资料权限
      if (this.permission.indexOf('risk:credit:use:log') !== -1) {
        this.formAttrList[2].isHide = false
      }
    },
    getData () {
      const data = {
        businessTypeId: this.businessTypeId,
        queryType: this.queryType,
        creditRecordId: this.creditRecordId,
        creditTicketApplyId: this.creditApplyId,
        flowCode: this.flowCode
      }
      this.findCreditTicketCfg(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          const data1 = res.data.data
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
          this.formAttrList = formAttrList.concat(this.formAttrList)
          this.creditList = data1.creditList
          // this.formFileCfgList = (data1.trailingWorkFlowCfg && data1.trailingWorkFlowCfg[0].workFlowFileVOList) || []
          this.formFileCfgList = data1.formFileCfgList
          this.getDetails()
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
          // data1.formData.fileList.map(item => {
          //   item.name = item.originalFileName
          //   return item
          // })
          this.form = toObjValue(data1.formData, this.form)
          this.getComment()

          const queryFlowForward = {
            companyId: this.companyId,
            businessTypeId: this.form.businessTypeId
          }
          this.getBusinessFlowForward(queryFlowForward).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.forwardFlow = res.data.data
            }
          })
        }
      })
    },
    getComment () {
      const data = {
        companyId: this.companyId,
        businessId: this.creditApplyId,
        businessTypeId: this.form.businessTypeId
      }
      this.getCommentList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.commentList = res.data.data
        }
      })
    },
    getTicketDocumentList () {
      const data = {
        id: this.creditApplyId
      }
      this.getTicketDocument(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.fileList = res.data.data
        }
      })
    },
    getAuditLogList () {
      const data = {
        companyId: this.companyId,
        businessId: this.creditApplyId,
        businessTypeId: this.form.businessTypeId,
        size: this.auditLogData.size,
        current: this.auditLogData.current
      }
      this.getTicketAuditLogList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.auditLogData.list = res.data.data.list
          this.auditLogData.total = res.data.data.total
        }
      })
    },
    changePageAuditLog (current) {
      this.auditLogData.current = current
      this.getAuditLogList()
    },
    getOperateLogList () {
      const data = {
        companyId: this.companyId,
        businessId: this.creditApplyId,
        businessTypeId: this.form.businessTypeId,
        size: this.operateLogData.size,
        current: this.operateLogData.current
      }
      this.getTicketOperateLogList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.operateLogData.list = res.data.data.list
          this.operateLogData.total = res.data.data.total
        }
      })
    },
    changePageOperateLog (current) {
      this.operateLogData.current = current
      this.getOperateLogList()
    },
    // 检查creditApplyDetails数据表单的类似item为数组的是否有大于2，大于2则渲染表单要添加数组对应进行渲染
    checkDetailsArr (form) {
      for (let k in form) {
        if (form[k] instanceof Array && form[k].length) {
          this.formAttrList.map((item, index) => {
            item.attrList.forEach(subItem => {
              if (subItem.columnGroup === k) {
                const numbers = [...Array(form[k].length - 1).keys()]
                numbers.forEach(() => {
                  this.handleAdd(subItem, index)
                })
              }
            })
          })
        }
      }
    },
    handleChangeType (value) {
      this.trailingActive = value
    },
    onClickTab (value) {
      this.accordion = value
      if ((this.formAttrList.length - 2) + '' === value && this.fileList.length === 0) {
        this.getTicketDocumentList()
      }
      if ((this.formAttrList.length - 1) + '' === value && this.auditLogData.list.length === 0) {
        this.getAuditLogList()
      }
      if (this.formAttrList.length + '' === value && this.operateLogData.list.length === 0) {
        this.getOperateLogList()
      }
    },
    handleTabsEdit () {
      const route = {
        name: 'editticketapply',
        query: {
          companyId: this.companyId,
          businessTypeId: this.businessTypeId,
          creditApplyId: this.creditApplyId
        }
      }
      this.$router.push(route)
    },
    handleAdd (subItem, index, subIndex) {
      this.form[subItem.columnGroup].push(deepCopy(this.form[subItem.columnGroup][0], true))
      this.formAttrList[index]['attrList'].push(subItem)
    }
  }
}
</script>
