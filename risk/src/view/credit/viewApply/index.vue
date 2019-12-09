<style lang="less">
@import "../../../index.less";
.content-div {
  // margin-bottom: 80px;
  .buttons {
    margin-left: 70px;
    margin-bottom: 20px;
  }
}
.title {
  border-left: 3px solid @primary-color;
  padding-left: 10px;
  margin-bottom: 20px;
}
.comment {
  width: 800px;
  padding: 20px;
  margin-left: 70px;
  background: #ecf0f6;
  &-title {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    &-text {
      color: #8791a0;
      margin-right: 20px;
    }
  }
  &-content {
    font-size: 14px;
    color: @title-color;
  }
}
.form-label {
  width: 180px;
  color: rgba(102, 102, 102, 1);
  text-align: right;
  font-size: 14px;
}
.form-value {
  margin-left: 20px;
  color: rgba(34, 34, 34, 1);
  font-size: 14px;
}
</style>

<template>
  <div class="apply">
    <div class="title-nav">
      <div class="title-nav-text">申请授信详情</div>
      <FlowIndicator :forwardFlow="forwardFlow"
                     :flowCode="flowCode" />
    </div>
    <div class="form-steps"
         style="margin-bottom: 20px">
      <!-- 详情 -->
      <Tabs :value="accordion"
            @on-click="onClickTab">
        <TabPane v-for="(item, index) in detail"
                 :key="index"
                 :label="item.classifyName"
                 v-if="item.isHide !== true"
                 :name="index+1+''">
        </TabPane>
        <!-- <Button @click="handleTabsEdit"
                size="small"
                slot="extra">编辑</Button> -->
      </Tabs>
      <div v-for="(item, index) in detail"
           :key="index"
           :label="item.classifyName"
           :name="index+1+''">
        <template v-if="accordion === index+1+''">
          <div style="margin-top: 0px">
            <template v-for="(subItem, subIndex) in item.attrList">
              <div :key="subItem.columnName+subIndex"
                   style="display:flex;line-height: 34px;">
                <div class="form-label">{{subItem.columnCh}}</div>
                <font v-if="subItem.required"
                      color="red">*</font>
                <div class="form-value"
                     v-if="subItem.attrValObj">
                  {{subItem.attrValObj['attrValMap'][subItem.columnVal] && subItem.attrValObj['attrValMap'][subItem.columnVal]['valueCh'] || subItem.columnVa}} {{subItem.columnUnit}}
                </div>
                <div class="form-value"
                     v-else>
                  <span v-if="subItem.columnName === 'creditApply_familyAddressProvinceCode'">{{subItem.columnVal | province}}</span>
                  <span v-else-if="subItem.columnName === 'creditApply_familyAddressCityCode'">{{subItem.columnVal | city}}</span>
                  <span v-else-if="subItem.columnName === 'creditApply_familyAddressRegionCode'">{{subItem.columnVal | region}}</span>
                  <span v-else>{{subItem.columnVal}} {{subItem.columnUnit}}</span>
                </div>
              </div>
            </template>
          </div>
        </template>
      </div>
      <div v-if="accordion === detail.length-3+''">
        <Table border
               style="width: 792px"
               :columns="filesColumns"
               :data="formFileCfgList"></Table>
<!--        v-if="fileList.length"-->
        <!-- <FilesTable v-if="fileList.length"
                    :list="formFileCfgList"
                    :fileList="fileList" /> -->
      </div>
      <div v-if="accordion === detail.length-2+''">
        <div class="sention-context">
          <div class="form-steps"
               style="width: 700px;">
            <div v-if="approvalCourseList.length === 0">暂无信息</div>
            <div class="form-steps-item approval-sub"
                 style="margin-bottom: 0"
                 v-for="(item, index) in approvalCourseList"
                 :key="'approval'+index">
              <!-- <Affixs :offset-top="index * 44 + 151"
                      v-show="scrollTop < (sention2 + (index * 50) - 600)"
                      container=".content-wrapper"> -->
              <div class="form-steps-title"
                   @click="approvalIndex = index">
                <div class="form-steps-title-num">
                  <div><span class="span-active">{{index+1}}</span></div>
                  <!-- <div><span :class="{'span-active': index === subStep[1]}">{{index+1}}</span></div> -->
                </div>
                <div class="form-steps-title-text">
                  <div style="line-height: 30px"><span>{{item.auditBeforeFlowCH}}</span>
                    <span style="line-height: 14px;color:#8791A0;font-size: 12px;margin-left:8px">{{item.auditTime}}</span></div>
                </div>
              </div>
              <!-- </Affixs> -->
              <div class="form-steps-context"
                   style="padding-bottom: 30px;"
                   v-show="approvalIndex === index">
                <div v-for="(subItem, subIndex) in item.applyColumnList"
                     :key="'approvall'+subIndex"
                     style="padding-left: 80px;display:flex;line-height: 34px;">
                  <div class="form-label">{{subItem.columnCh}}</div>
                  <font v-if="subItem.required"
                        color="red">*</font>
                  <div class="form-value">
                    <span>{{subItem.columnVal}} {{subItem.columnUnit}}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="accordion === detail.length-1+''">
        <div style="margin-top: 20px">
          <Table border
                 :columns="auditColumns"
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
      <div v-if="accordion === detail.length+''">
        <div style="margin-top: 20px">
          <Table border
                 :columns="operateColumns"
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
    </div>
    <!-- 详情 -->

    <Drawer :title="drawerTitle"
            :closable="false"
            width="720"
            v-model="isDrawer">
      <Table border
             style="width: 602px"
             :columns="fileColumns"
             :data="DrawerData"></Table>
    </Drawer>
    <Modal v-model="modal"
           width="800px"
           :footer-hide="true"
           title="查看文件">
      <iframe :src="src"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
    </Modal>
  </div>
</template>

<script>

import FlowIndicator from '../../business/flowIndicator'
import ApplyForm from '_c/apply-form'
import ApplyFormFlow from '_c/apply-form-flow'
import FilesTable from '_c/files-table'
import UploadFile from '_c/upload-file'
import { mapActions, mapMutations, mapState } from 'vuex'
import { CITYS } from '@/libs/citys.js'
import config from '@/config'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'applyAudit',
  components: {
    ApplyForm,
    ApplyFormFlow,
    FilesTable,
    UploadFile,
    FlowIndicator
  },
  data () {
    return {
      approvalIndex: 0,
      modal: false,
      src: '',
      citys: CITYS,
      accordion: '1',
      trailingActive: 0,
      upUrl: upUrl + 'file/upload',
      forwardFlow: null,
      form: {},
      fileList: [],
      creditApplyId: '',
      businessTypeId: '1',
      companyId: '',
      flowCode: '',
      detail: [],
      attrValList: [],
      formAttrList: [{ classifyName: '文件资料', attrList: [], isHide: true }, { classifyName: '审核过程', attrList: [], isHide: true }, { classifyName: '审核日志', attrList: [], isHide: true }, { classifyName: '操作日志', attrList: [], isHide: true }],
      trailingWorkFlowCfg: [],
      formFileCfgList: [],
      approvalCourseList: [],
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
      filesColumns: [
        {
          type: 'index',
          title: '序号',
          width: 80,
          align: 'center'
        },
        {
          title: '文件分类',
          width: 560,
          key: 'fileName',
          render: (h, params) => {
            let html = params.row.fileName
            if (params.row.list.length) {
              html = `<a style="text-decoration: underline;" title="${params.row.fileName}">${params.row.fileName}</a>`
            }
            return h('div', {
              on: {
                click: () => {
                  this.drawerTitle = params.row.fileName
                  this.DrawerData = params.row.list
                  this.isDrawer = true
                }
              },
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '数量',
          width: 150,
          align: 'center',
          key: 'list',
          render: (h, params) => {
            return h('div', params.row.list.length)
          }
        }
      ],
      isDrawer: false,
      drawerTitle: '',
      DrawerData: [],
      fileColumns: [
        {
          title: '文件',
          width: 450,
          key: 'originalFileName',
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  if (params.row.fileType === 'image' || params.row.fileType === 'pdf') {
                    this.modal = true
                    this.src = params.row.viewFileUrl
                  } else {
                    window.location.href = params.row.downloadFileUrl
                  }
                }
              }
            }, params.row.originalFileName)
          }
        },
        {
          title: '上传时间',
          width: 150,
          align: 'center',
          key: 'updateTime'
        }
      ],
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
      }
    }
  },
  filters: {
    province (value) {
      for (let i = 0, len = CITYS.length; i < len; i++) {
        if (CITYS[i].value === value) {
          return CITYS[i].label
        }
      }
    },
    city (value) {
      for (let i = 0, len = CITYS.length; i < len; i++) {
        for (let j = 0, lenj = CITYS[i].children.length; j < lenj; j++) {
          if (CITYS[i].children[j].value === value) {
            return CITYS[i].children[j].label
          }
        }
      }
    },
    region (value) {
      if (value) {
        for (let i = 0, len = CITYS.length; i < len; i++) {
          for (let j = 0, lenj = CITYS[i].children.length; j < lenj; j++) {
            for (let k = 0, lenk = CITYS[i].children[j].children.length; k < lenk; k++) {
              if (CITYS[i].children[j].children[k].value === value) {
                return CITYS[i].children[j].children[k].label
              }
            }
          }
        }
      } else {
        return ''
      }
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
      this.getDetails()
      this.getData()
    }
  },
  methods: {
    ...mapActions([
      'getCreditApplyDetails',
      'getDetail',
      'auditCommit',
      'getCommentList',
      'findAuditLogList',
      'findOperateLogList',
      'getCreditDocument',
      'findCreditApprovalCourse',
      'getBusinessFlowForward'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    // 根据权限隐藏tab
    handleHideTabControl () {
      // 有文件资料权限
      if (this.permission.indexOf('risk:credit:apply:document') !== -1) {
        this.formAttrList[0].isHide = false
      }
      // 有审核过程权限
      if (this.permission.indexOf('risk:credit:audit:course') !== -1) {
        this.formAttrList[1].isHide = false
      }
      // 有审核权限
      if (this.permission.indexOf('risk:credit:apply:audit:log') !== -1) {
        this.formAttrList[2].isHide = false
      }
      // 有查看资料权限
      if (this.permission.indexOf('risk:credit:apply:log') !== -1) {
        this.formAttrList[3].isHide = false
      }
    },
    getDetails () {
      const data = {
        creditApplyId: this.creditApplyId,
        queryType: 'view'
      }
      this.getCreditApplyDetails(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.detail = res.data.data.concat(this.formAttrList)
        }
      })

      const queryFlowForward = {
        companyId: this.companyId,
        businessTypeId: this.businessTypeId
      }
      this.getBusinessFlowForward(queryFlowForward).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.forwardFlow = res.data.data
        }
      })
    },
    getData () {
      const data = {
        companyId: this.companyId,
        businessTypeId: this.businessTypeId,
        creditApplyId: this.creditApplyId,
        flowCode: this.flowCode,
        queryType: 'audit'
      }
      this.getDetail(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          const data1 = res.data.data
          this.formFileCfgList = data1.formFileCfgList
        }
      })
    },
    getApprovalCourse () {
      const data = {
        creditApplyId: this.creditApplyId
      }
      this.findCreditApprovalCourse(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.approvalCourseList = res.data.data
        }
      })
    },
    getComment () {
      const data = {
        companyId: this.companyId,
        businessId: this.creditApplyId,
        businessTypeId: this.businessTypeId
      }
      this.getCommentList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.commentList = res.data.data
        }
      })
    },
    getCreditDocumentList () {
      const data = {
        companyId: this.companyId,
        businessTypeId: this.businessTypeId,
        creditApplyId: this.creditApplyId,
        flowCode: this.flowCode,
        queryType: 'audit'
      }
      this.getCreditDocument(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.fileList = res.data.data
          this.formFileCfgList = this.formFileCfgList.map(item => {
            item.list = []
            this.fileList.forEach(file => {
              if (file.fileCode === item.fileCode) {
                item.list.push(file)
              }
            })
            return item
          })
        }
      })
    },
    getAuditLogList () {
      const data = {
        companyId: this.companyId,
        businessId: this.creditApplyId,
        businessTypeId: this.businessTypeId,
        size: this.auditLogData.size,
        current: this.auditLogData.current
      }
      this.findAuditLogList(data).then(res => {
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
        businessTypeId: this.businessTypeId,
        size: this.operateLogData.size,
        current: this.operateLogData.current
      }
      this.findOperateLogList(data).then(res => {
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
    handleChangeType (value) {
      this.trailingActive = value
    },
    onClickTab (value) {
      this.accordion = value
      if ((this.detail.length - 3) + '' === value && this.fileList.length === 0) {
        this.getCreditDocumentList()
      }
      if ((this.detail.length - 2) + '' === value && this.approvalCourseList.length === 0) {
        this.getApprovalCourse()
      }
      if ((this.detail.length - 1) + '' === value && this.auditLogData.list.length === 0) {
        this.getAuditLogList()
      }
      if (this.detail.length + '' === value && this.operateLogData.list.length === 0) {
        this.getOperateLogList()
      }
    }
  }
}
</script>
