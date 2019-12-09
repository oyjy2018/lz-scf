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
      <div class="title-nav-text">授信审批</div>
      <FlowIndicator :forwardFlow="forwardFlow"
                     :flowCode="flowCode" />
    </div>
    <div class="form-steps"
         style="margin-bottom: 20px">
      <!-- 详情 -->
      <Affixs :offset-top="104"
              container=".content-wrapper">
        <div style="background:#fff;">
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
        </div>
      </Affixs>
      <div v-for="(item, index) in detail"
           :key="index"
           :label="item.classifyName"
           :name="index+1+''">
        <template v-if="accordion === index+1+''">
          <div style="margin-top: 0px">
            <template v-for="(subItem, subIndex) in item.attrList">
              <div v-if="subItem.columnVal"
                   :key="subItem.columnName+subIndex"
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
    <!-- 评论 -->
    <div class="form-steps"
         style="margin-bottom: 20px">
      <h3 class="title">评论</h3>
      <div class="content-div">
        <div class="comment"
             style="padding: 0; padding-bottom: 20px; background: transparent"
             v-for="item in commentList"
             :key="item.id+'comment'">
          <div class="comment-title">
            <div class="comment-title-text">{{item.createBy}}</div>
            <div class="comment-title-text">{{item.statusDesc}}</div>
            <div class="comment-title-text">{{item.createTime}}</div>
          </div>
          <div class="comment-content">
            {{item.content}}
          </div>
        </div>
      </div>
    </div>
    <!-- 评论 -->
    <!-- 审批 -->
    <div class="form-steps"
         style="margin-bottom: 20px">
      <h3 class="title">请选择下一个状态</h3>
      <div class="content-div">
        <div class="buttons">
          <Button v-for="(item, index) in trailingWorkFlowCfg"
                  :key="item.afterFlowName"
                  @click="handleChangeType(index)"
                  style="margin-right: 15px;"
                  size="large"
                  :type="trailingActive === index ? 'primary' : 'dashed'">
            <span v-if="trailingActive === index">{{item.afterFlowName}}</span>
            <span v-else>{{item.afterFlowName}}</span>
            <Icon v-if="trailingActive === index"
                  type="md-checkmark" />
          </Button>
        </div>
        <div class="comment">
          <Form :label-width="150">
            <FormItem label="下一处理人">
              <div v-for="(item, index) in trailingWorkFlowCfg"
                   :key="item.afterFlowName+index">
                <div v-show="trailingActive === index">
                  <Tag v-for="user in item.nextDisposeRoleList"
                       :key="user.roleName+user.id"
                       type="dot"
                       color="primary">{{user.roleName}}</Tag>
                  <Tag v-for="user in item.nextDisposeUserList"
                       :key="user.userName+user.id"
                       type="dot"
                       color="primary">{{user.userName}}</Tag>
                </div>
              </div>
            </FormItem>
          </Form>
        </div>
      </div>
    </div>

    <div class="form-steps"
         style="margin-bottom: 20px">
      <h3 class="title">流转到下一步填写<span style="font-size: 12px;color:#999">（您填写的信息将会对申请人保密）</span></h3>
      <div class="content-div">
        <div class="comment">
          <Form :label-width="150"
                ref="form">
            <div v-for="(item, index) in trailingWorkFlowCfg"
                 :key="item.afterFlowName+index">
              <div v-show="trailingActive === index">
                <div v-for="(subItem, subIndex) in item.businessAttrCfgVOList"
                     :key="subItem.columnName+subIndex">
                  <template v-if="subItem.businessAttrId <= 0">
                    {{subItem.defaultValue}}
                  </template>
                  <ApplyFormFlow v-if="subItem.businessAttrId > 0"
                                 :model="subItem.columnValue"
                                 :item="subItem"
                                 :attrList="item.businessAttrCfgVOList"
                                 :index="subIndex"
                                 @on-save="onSaveDraft" />
                </div>
                <FormItem v-for="sitem in item.workFlowFileVOList"
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
            <FormItem label="评论"
                      prop="commentContent">
              <Input v-model="formData.commentContent"
                     type="textarea"
                     :maxlength="500"
                     :autosize="{minRows: 2,maxRows: 5}"
                     placeholder="@通知他人，增加处理意见，此信息可被其他用户查看"></Input>
            </FormItem>
          </Form>
        </div>
      </div>
      <div style="text-align: center; margin-top: 20px;">
        <Button @click="handleSubmit"
                type="primary"
                size="large">流转到【{{trailingWorkFlowCfg.length && trailingWorkFlowCfg[trailingActive].afterFlowName}}】</Button>
      </div>
      <br><br>
    </div>
    <!-- 审批 -->
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
import Affixs from '_c/affixs'
import FlowIndicator from '../../business/flowIndicator'
import ApplyForm from '_c/apply-form'
import ApplyFormFlow from '_c/apply-form-flow'
import FilesTable from '_c/files-table'
import UploadFile from '_c/upload-file'
import { mapActions, mapMutations, mapState } from 'vuex'
import { CITYS } from '@/libs/citys.js'
import { deepCopy } from '@/libs/util.js'
import config from '@/config'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'applyAudit',
  components: {
    ApplyForm,
    ApplyFormFlow,
    FilesTable,
    UploadFile,
    FlowIndicator,
    Affixs
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
      this.getDetail()
      this.getData()
      this.getComment()
    }
  },
  methods: {
    ...mapActions([
      'getCreditApplyDetails',
      'getTrailingWorkFlowCfg',
      'fileCommit',
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
    getDetail () {
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
      this.getTrailingWorkFlowCfg(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          const data1 = res.data.data
          // const formAttrList = []
          // // from
          // const form = {}

          // data1.formAttrList = data1.formAttrList.forEach(item => {
          //   let obj = {}
          //   obj.classifyName = item.classifyName
          //   obj.attrList = []
          //   let subObj = {
          //     columnGroup: '',
          //     attrList: []
          //   }
          //   // from
          //   let fromArr = {}
          //   item.attrList.forEach(subItem => {
          //     if (subItem.columnType === 'radio' || subItem.columnType === 'select') {
          //       subItem.list = data1.attrValList.filter(sitem => {
          //         if (subItem.businessAttrId === sitem.businessAttrId) {
          //           return sitem
          //         }
          //       })
          //     }
          //     if (subItem.columnGroup) {
          //       subObj.columnGroup = subItem.columnGroup
          //       subObj.attrList.push(subItem)
          //       // from
          //       form[subItem.columnGroup] = []
          //       fromArr = Object.assign(fromArr, { [subItem.columnName]: subItem.columnName.indexOf('Province') === -1 ? '' : [] })
          //       form[subItem.columnGroup] = [...[fromArr]]
          //     } else {
          //       obj.attrList.push(subItem)
          //       // from
          //       form[subItem.columnName] = subItem.columnName.indexOf('Province') === -1 ? '' : []
          //     }
          //   })
          //   if (subObj.columnGroup) {
          //     obj.attrList.push(subObj)
          //   }
          //   formAttrList.push(obj)
          // })
          this.trailingWorkFlowCfg = data1.trailingWorkFlowCfg.map((item, index) => {
            if (item.businessAttrCfgVOList) {
              item.businessAttrCfgVOList.map(subItem => {
                if (subItem.columnType === 'radio' || subItem.columnType === 'select') {
                  subItem.list = data1.attrValList.filter(sitem => {
                    if (subItem.businessAttrId === sitem.businessAttrId) {
                      return sitem
                    }
                  })
                }
                subItem.columnValue = ''
                if (subItem.businessAttrId > 0 && subItem.defaultValue) {
                  subItem.columnValue = subItem.defaultValue
                }
                subItem.hasViewVisible = 1
                return subItem
              })
            }

            return item
          })
          // this.form = toObjValue(data1.creditApplyDetails, form)
          // this.formAttrList = formAttrList.concat(this.formAttrList)
          this.formFileCfgList = data1.formFileCfgList
          // this.checkDetailsArr(data1.creditApplyDetails)
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
    handleAdd (subItem, index, subIndex) {
      this.form[subItem.columnGroup].push(deepCopy(this.form[subItem.columnGroup][0], true))
      this.formAttrList[index]['attrList'].push(subItem)
    },
    onSaveDraft ({ value, index }) {
      this.trailingWorkFlowCfg[this.trailingActive].businessAttrCfgVOList[index].columnValue = value
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
    },
    handleTabsEdit () {
      const route = {
        name: 'editapply',
        query: {
          creditApplyId: this.creditApplyId,
          flowCode: this.flowCode
        }
      }
      this.$router.push(route)
    },
    handleSuccess ({ res, sitem }) {
      if (res && res.code === 10000) {
        res.data.name = res.data.originalFileName
        res.data.fileCode = sitem.fileCode
        this.formData.fileList.push(res.data)
        this.formData.uploadDeleteFileDesc = this.formData.uploadDeleteFileDesc === ''
          ? `${sitem.fileName}: ${res.data.originalFileName} 上传`
          : `${this.formData.uploadDeleteFileDesc};${sitem.fileName}: ${res.data.originalFileName} 上传`
      }
    },
    handleRemove ({ file, sitem }) {
      this.formData.fileList = this.formData.fileList.filter(item => {
        if (file.response.data.newFileName !== item.newFileName) {
          return item
        }
      })
      this.formData.uploadDeleteFileDesc = this.formData.uploadDeleteFileDesc === ''
        ? `${sitem.fileName}: ${file.response.data.originalFileName} 删除`
        : `${this.formData.uploadDeleteFileDesc};${sitem.fileName}: ${file.response.data.originalFileName} 删除`
    },
    handleSubmit () {
      if (this.creditApplyId) {
        let formDataList = deepCopy(this.trailingWorkFlowCfg[this.trailingActive].businessAttrCfgVOList)
        if (formDataList.length) {
          for (let i = 0, len = formDataList.length; i < len; i++) {
            if (formDataList[i].required && formDataList[i].columnValue === '') {
              if (formDataList[i].preColumnId) {
                for (let j = 0, len2 = formDataList.length; j < len2; j++) {
                  if (formDataList[j].businessAttrId === formDataList[i].preColumnId && formDataList[j].projectId === formDataList[i].projectId) {
                    if (formDataList[j].columnValue === formDataList[i].preColumnValue) {
                      this.$Message.warning({
                        content: `请输入或选择${formDataList[i].columnCh}`,
                        duration: 5,
                        closable: true
                      })
                      return false
                    }
                  }
                }
              } else {
                this.$Message.warning({
                  content: `请输入或选择${formDataList[i].columnCh}`,
                  duration: 5,
                  closable: true
                })
                return false
              }
            } else if (formDataList[i].required && formDataList[i].verifyFormula) {
              const exp = new RegExp(formDataList[i].verifyFormula)
              if (!exp.test(formDataList[i].columnValue)) {
                this.$Message.warning({
                  content: `${formDataList[i].columnCh}：${formDataList[i].columnErrMsg}`,
                  duration: 5,
                  closable: true
                })
                return false
              }
            }
          }
          formDataList = formDataList.filter(item => {
            if (item.businessAttrId > 0) {
              return item
            }
          })
        } else {
          formDataList = []
        }
        this.formData.formDataList = formDataList || []
        this.formData.companyId = this.companyId
        this.formData.businessTypeId = this.businessTypeId
        this.formData.creditApplyId = this.creditApplyId
        this.formData.businessId = this.creditApplyId
        this.formData.beforeFlow = this.trailingWorkFlowCfg[this.trailingActive].beforeFlow
        this.formData.afterFlow = this.trailingWorkFlowCfg[this.trailingActive].afterFlow
        this.formData.disposeRoleIds = this.trailingWorkFlowCfg[this.trailingActive].nextDisposeRoleIds
        this.formData.disposeUserIds = this.trailingWorkFlowCfg[this.trailingActive].nextDisposeUserIds
        this.formData.flowDesc = `状态: 由“${this.trailingWorkFlowCfg[this.trailingActive].beforeFlowName}”修改为“${this.trailingWorkFlowCfg[this.trailingActive].afterFlowName}”`
        this.formData.statusDesc = `在状态[${this.trailingWorkFlowCfg[this.trailingActive].beforeFlowName}]`
        this.formData.flowId = this.trailingWorkFlowCfg[this.trailingActive].id
        this.fileCommit(this.formData).then(res => {
          if (res && res.status === 200 && res.data && res.data.code === 10000) {
            this.closeTag({
              name: 'myapplyaudit',
              query: {
                companyId: this.$route.query.companyId,
                businessTypeId: this.$route.query.businessTypeId,
                creditApplyId: this.$route.query.creditApplyId,
                flowCode: this.$route.query.flowCode
              }
            })
            this.$router.push({
              name: 'myapply'
            })
            this.$Modal.success({
              title: '提示',
              content: '流转成功'
            })
          }
        })
      }
    }
  }
}
</script>
