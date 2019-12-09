<style lang="less" scoped>
@import "../../../index.less";
.container {
  height: ~"calc(100% - 60px)";
}
.apply-main {
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-top: 1px solid #ccc;
  background: #fff;
}
.tabs {
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding: 10px 20px 0 20px;
  &-items {
    display: flex;
    padding-left: 20px;
    height: 47px;
    border-bottom: 1px solid #ccc;
  }
  &-item {
    margin-right: 30px;
    line-height: 48px;
    cursor: pointer;
  }
  &-item-active {
    height: 46px;
    border-bottom: 2px solid @primary-color;
  }
  &-context {
    width: 100%;
    height: ~"calc(100% - 48px)";
    overflow: hidden;
    &-item {
      display: flex;
      flex-direction: row;
      width: 100%;
      height: 100%;
      overflow: hidden;
    }
  }
}
.tabs-main {
  padding: 0 20px;
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.nav {
  width: 198px;
  height: 100%;
  border-right: 2px solid #ccc;
}
.nav-items {
  position: relative;
}
.nav-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 198px;
  padding-left: 20px;
  line-height: 50px;
  cursor: pointer;
  i {
    padding-right: 25px;
  }
}
.nav-item-sub {
  padding-left: 40px;
}
.active {
  width: 198px;
  background: #f0faff;
  border-right: 2px solid @primary-color;
}
.content {
  flex: 1;
  height: 100%;
  overflow-y: scroll;
}
.content-div {
  margin-bottom: 80px;
  .buttons {
    margin-left: 70px;
    margin-bottom: 20px;
  }
}
.sention-title {
  line-height: 46px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  span {
    padding-left: 10px;
    border-left: 3px solid @primary-color;
  }
}
.sention {
  padding-left: 20px;
  padding-bottom: 40px;
  border-bottom: 8px solid #f3f3f3;
  &:last-child {
    border-bottom: none;
  }
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
.pagination {
  padding-top: 10px;
}
.form-steps-item {
  margin-bottom: -23px;
}
.form-steps-context {
  margin-left: 15px;
  border-left: 1px dashed #ccc;
}
.exp-step-nav {
  position: fixed;
  top: 230px;
  left: 532px;
}
</style>

<template>
  <div class="container">
    <div class="title-nav">
      <div class="title-text">申请授信{{scrollTop}}</div>
      <!-- <div class="nav-text">
        <span v-for="item in flowCodeList"
              :key="item.flowCode"
              :class="{'nav-text-active': item.flowName === '草稿'}">{{item.flowName}} > </span>
      </div> -->
    </div>
    <div class="apply-main">
      <!-- tabs -->
      <div class="tabs">
        <div class="tabs-items">
          <div class="tabs-item"
               :class="{'tabs-item-active': accordion === '1'}"
               @click="onClickTab('1')">申请详情、审核详情{{sention1}}</div>
          <div class="tabs-item"
               :class="{'tabs-item-active': accordion === '2'}"
               @click="onClickTab('2')">审核日志{{sention0}}</div>
          <div class="tabs-item"
               :class="{'tabs-item-active': accordion === '3'}"
               @click="onClickTab('3')">变更日志</div>
        </div>
        <div class="tabs-context">
          <!-- tab-context -->
          <div v-show="accordion === '1'"
               class="tabs-context-item">
            <div class="nav">
              <div class="nav-items">
                <div v-for="(item, index) in menus"
                     :key="item.label"
                     @click="scrollTopEl(index)">
                  <div class="nav-item"
                       :class="{'active': activeStep === index && (activeStep !== 0 && activeStep !== 1)}">
                    <span :title="item.label">{{item.label}}</span>
                    <Icon v-show="item.menus && item.menus.length"
                          :type="activeStep === index ? 'ios-arrow-up' : 'ios-arrow-down'" />
                  </div>
                  <div class="nav-item nav-item-sub"
                       :title="subItem.label"
                       v-show="activeStep === index"
                       :class="{'active': activeStep === index && subStep[index] === subIndex}"
                       v-for="(subItem, subIndex) in item.menus"
                       :key="subItem.label"
                       @click.stop="collapse(index, subIndex)">
                    {{subItem.label}}
                  </div>
                </div>
              </div>
            </div>
            <div ref="content"
                 class="content"
                 @scroll="handleScroll">
              <div class="sention">
                <div class="sention-title"><span>申请详情</span></div>
                <div class="sention-context">
                  <div class="form-steps">
                    <Form style="width: 650px;"
                          :model="form"
                          :label-width="250">
                      <div class="form-steps-item sention-sub"
                           v-for="(item, index) in formAttrList"
                           :key="index">
                        <div class="form-steps-title"
                             :class="{'exp-step-nav': scrollTop >= (sentionSub0 - sention0) && scrollTop < (sention1 + (index * 50) - 600) && subStep[0] >= index && activeStep === 0}"
                             :style="{top: index * 30 + 230 + 'px'}"
                             @click="collapse(0, index)">
                          <div class="form-steps-title-num">
                            <div><span :class="{'span-active': index === subStep[0]}">{{index+1}}</span></div>
                          </div>
                          <div class="form-steps-title-text">
                            <div style="line-height: 30px;"><span>{{item.classifyName}}</span></div>
                          </div>
                        </div>
                        <div class="form-steps-context">
                          <div v-for="(subItem, subIndex) in item.attrList"
                               :key="subItem.columnName"
                               style="padding-left: 150px">
                            <div v-if="subItem.columnGroup">
                              <div v-for="(subItemSub, subIndexSub) in subItem.attrList"
                                   :key="subItemSub.columnCh">
                                <ApplyForm :model="form[subItem.columnGroup][subIndex][subItemSub.columnName]"
                                           :item="subItemSub"
                                           :index="subIndex"
                                           :group="subItem.columnGroup"
                                           :form="form"
                                           :attrList="subItem.attrList"
                                           :isRules="false"
                                           disabled />
                                <Divider style="margin-left:30px"
                                         v-if="subIndexSub === subItem.attrList.length -1 && !(subIndex === item.attrList.length - 1)"
                                         dashed />
                              </div>

                            </div>
                            <div v-else>
                              <ApplyForm :model="form[subItem.columnName]"
                                         :item="subItem"
                                         :isRules="false"
                                         :form="form"
                                         :attrList="item.attrList"
                                         disabled />
                            </div>
                          </div>
                        </div>
                      </div>
                    </Form>
                  </div>
                </div>
              </div>
              <div class="sention">
                <div class="sention-title"><span>审核过程</span></div>
                <div class="sention-context">
                  <div class="form-steps">
                    <Form style="width: 650px;"
                          :model="form"
                          :label-width="250">
                      <div class="form-steps-item approval-sub"
                           style="margin-bottom: 0"
                           v-for="(item, index) in approvalCourseList"
                           :key="'approval'+index">
                        <div class="form-steps-title"
                             :class="{'exp-step-nav': scrollTop >= (approvalSub0 - sention0) && scrollTop < (sention2 + (index * 50) - 700) && subStep[1] >= index && activeStep === 1}"
                             :style="{top: index * 30 + 230 + 'px'}"
                             @click="collapse(index)">
                          <div class="form-steps-title-num">
                            <div><span :class="{'span-active': index === subStep[1]}">{{index+1}}</span></div>
                          </div>
                          <div class="form-steps-title-text">
                            <div style="line-height: 16px"><span>{{item.auditBeforeFlowCH}}{{sention2 + (index * 50) - 700}}</span></div>
                            <div style="line-height: 14px;color:#8791A0;font-size: 12px;"><span>{{item.auditTime}}</span></div>
                          </div>
                        </div>
                        <div class="form-steps-context"
                             style="padding-bottom: 30px;">
                          <div v-for="(subItem, subIndex) in item.applyColumnList"
                               :key="'approvall'+subIndex"
                               style="padding-left: 180px; line-height: 30px;">
                            {{subItem.columnCh}}: {{subItem.columnVal}}
                          </div>
                        </div>
                      </div>
                    </Form>
                  </div>
                </div>
              </div>
              <div class="sention">
                <div class="sention-title"><span>附件</span></div>
                <div class="sention-context"
                     v-if="fileList.length">
                  <FilesTable :list="formFileCfgList"
                              :fileList="fileList" />
                </div>
              </div>
              <div class="sention">
                <div class="sention-title"><span>请选择下一个状态</span></div>
                <div class="sention-context">
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
              </div>
              <div class="sention">
                <div class="sention-title"><span>流转到下一步填写</span>
                  <font style="font-size: 12px;color:#999">（您填写的信息将会对申请人保密）</font>
                </div>
                <div class="sention-context">
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
                </div>
              </div>
              <div class="sention">
                <div class="sention-title"><span>评论</span></div>
                <div class="sention-context">
                  <div class="content-div"
                       v-if="commentList.length">
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
                  <div v-else
                       style="color:#999">暂无评论</div>
                </div>
                <br><br>
                <div>
                  <Button @click="handleSubmit"
                          type="primary"
                          size="large">流转到【{{trailingWorkFlowCfg.length && trailingWorkFlowCfg[trailingActive].afterFlowName}}】</Button>
                </div>
              </div>

            </div>
          </div>
          <div v-show="accordion === '2'"
               class="tabs-context-item">
            <div style="margin-top: 20px;padding-bottom: 40px;height: 100%;overflow-y: auto;">
              <Table border
                     stripe
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
          <div v-show="accordion === '3'"
               class="tabs-context-item">
            <div style="margin-top: 20px; padding-bottom: 40px;height: 100%;overflow-y: auto;">
              <Table border
                     stripe
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
      </div>
    </div>
  </div>
</template>

<script>
import ApplyForm from '_c/apply-form'
import ApplyFormFlow from '_c/apply-form-flow'
import FilesTable from '_c/files-table'
import UploadFile from '_c/upload-file'
import { mapActions, mapMutations } from 'vuex'
import { CITYS } from '@/libs/citys.js'
import { deepCopy, toObjValue, scrollTop } from '@/libs/util.js'
import config from '@/config'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'applyAudit',
  components: {
    ApplyForm,
    ApplyFormFlow,
    FilesTable,
    UploadFile
  },
  data () {
    return {
      activeStep: 0,
      subStep: [0, 0],
      scrollTop: 0,
      sention: null,
      sention0: 0,
      sention1: 0,
      sention2: 0,
      sentionSub: null,
      sentionSub0: 0,
      approvalSub: [],
      approvalSub0: 0,
      menus: [
        { label: '申请详情', menus: [] },
        { label: '审核过程', menus: [] },
        { label: '附件' },
        { label: '请选择下一个状态' },
        { label: '流转到下一步填写' },
        { label: '评论' }
      ],
      citys: CITYS,
      accordion: '1',
      trailingActive: 0,
      upUrl: upUrl + 'file/upload',
      detail: null,
      form: {},
      fileList: [],
      creditApplyId: '',
      businessTypeId: '1',
      companyId: '',
      flowCode: '',
      attrValList: [],
      formAttrList: [],
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
      approvalCourseList: []
    }
  },
  watch: {
    activeStep (value) {
      if (value === 1 && this.approvalCourseList.length === 0) {
        this.getApprovalCourse()
      }
      if (value === 2 && this.fileList.length === 0) {
        this.getCreditDocumentList()
      }
      if (value === 5 && this.commentList.length === 0) {
        this.getComment()
      }
    }
  },
  computed: {
    top () {
      if (this.scrollTop > 10 && this.activeStep === 0 && this.sentionSub && this.scrollTop > this.sentionSub[0].offsetTop) {
        return this.subStep[0]
      } else {
        return 0
      }
    }
  },
  created () {
    const { companyId, businessTypeId, creditApplyId, flowCode } = this.$route.query
    if (creditApplyId) {
      this.companyId = companyId
      this.businessTypeId = businessTypeId
      this.creditApplyId = creditApplyId
      this.flowCode = flowCode
      this.getData()
      this.getDetail()
    }
  },
  mounted () {

  },
  methods: {
    ...mapActions([
      'getCreditApplyDetails',
      'getTrailingWorkFlowCfg',
      'auditCommit',
      'getCommentList',
      'findAuditLogList',
      'findOperateLogList',
      'getCreditDocument',
      'findCreditApprovalCourse'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getDetail () {
      const data = {
        creditApplyId: this.creditApplyId,
        queryType: 'view'
      }
      this.getCreditApplyDetails(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.detail = res.data.data
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
          const formAttrList = []
          // from
          const form = {}

          data1.formAttrList = data1.formAttrList.forEach(item => {
            this.menus[0].menus.push({ label: item.classifyName })
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
          this.form = toObjValue(data1.creditApplyDetails, form)
          this.formAttrList = formAttrList.concat(this.formAttrList)
          this.formFileCfgList = data1.formFileCfgList
          this.checkDetailsArr(data1.creditApplyDetails)
          this.$nextTick(function () {
            this.sention = document.querySelectorAll('.sention')
            this.sentionSub = document.querySelectorAll('.sention-sub')
            this.sention0 = this.sention[0].offsetTop
            this.sention1 = this.sention[1].offsetTop
            this.sentionSub0 = this.sentionSub[0].offsetTop
          })
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
    getApprovalCourse () {
      const data = {
        creditApplyId: this.creditApplyId
      }
      this.findCreditApprovalCourse(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.approvalCourseList = res.data.data
          res.data.data.forEach(item => {
            this.menus[1].menus.push({ label: item.auditBeforeFlowCH })
          })
          this.scrollTopEl(1)
          this.$nextTick(function () {
            this.approvalSub = document.querySelectorAll('.approval-sub')
            this.approvalSub0 = this.approvalSub[0].offsetTop
            this.sention2 = this.sention[2].offsetTop
          })
        }
      })
    },
    collapse (type, index) {
      this.subStep[type] = index
      if (type === 0) {
        const to = this.sentionSub[index].offsetTop - this.sention[0].offsetTop
        scrollTop(document.querySelector('.content'), this.scrollTop, to, 1000)
      } else {
        const to = this.approvalSub[index].offsetTop - this.sention[0].offsetTop
        scrollTop(document.querySelector('.content'), this.scrollTop, to, 1000)
      }
    },
    handleScroll () {
      this.scrollTop = this.$refs.content.scrollTop
      this.sention.forEach((item, index) => {
        if (this.scrollTop + this.sention[0].offsetTop >= item.offsetTop) {
          this.activeStep = index
        }
      })
      this.sentionSub.forEach((item, index) => {
        if (this.scrollTop + this.sention[0].offsetTop >= item.offsetTop) {
          this.subStep[0] = index
        }
      })
      this.approvalSub.forEach((item, index) => {
        if (this.scrollTop + this.sention[0].offsetTop >= item.offsetTop) {
          this.subStep[1] = index
        }
      })
      // 判断是否滚到底部 element.scrollHeight - element.scrollTop === element.clientHeight
      if ((this.scrollTop + this.$refs.content.clientHeight) === this.$refs.content.scrollHeight) {
        this.activeStep = this.menus.length - 1
      }
    },
    scrollTopEl (index) {
      const to = this.sention[index].offsetTop - this.sention[0].offsetTop
      scrollTop(document.querySelector('.content'), this.scrollTop, to, 1000, () => {
        this.activeStep = index
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
          this.scrollTopEl(2)
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
      if (value === '2' && this.auditLogData.list.length === 0) {
        this.getAuditLogList()
      }
      if (value === '3' && this.operateLogData.list.length === 0) {
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
        this.auditCommit(this.formData).then(res => {
          if (res && res.status === 200 && res.data && res.data.code === 10000) {
            this.closeTag({
              name: 'applyaudit',
              query: {
                companyId: this.$route.query.companyId,
                businessTypeId: this.$route.query.businessTypeId,
                creditApplyId: this.$route.query.creditApplyId,
                flowCode: this.$route.query.flowCode
              }
            })
            this.$router.push({
              name: 'auditlist'
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
