<style lang="less" scoped>
@import "../../../index.less";

.edit-top {
  width: 100%;
  height: 34px;
  .my-card {
    width: 150px;
    float: left;
    text-align: center;
  }
  .edit-icon {
    float: left;
  }
  .edit-span {
    float: left;
    line-height: 34px;
  }
}
.edit-attr {
  width: 100%;
  margin-top: 20px;
}
.edit-file {
  width: 100%;
  margin-top: 20px;
}
.p-title {
    text-align: left;
    font-weight: bold;
  }
.my-model {
  height: 660px;
  position: relative;
  overflow: hidden;
}
.my-model-overflow-y {
  overflow-y: auto;
}
</style>
<template>
  <div>
    <Card>
      <i-col span="25">
          <div class="layout-content-main">流转设置</div>
          <span>工作流流转设置，是设置工作流各状态间的先后流转关系。如果需要设置该流转，请在两个状态间的复选框内打勾。</span>
          <i-table stripe :columns="flowList" :data="flowCfgList" width="100%" :loading="loading"></i-table>
          <span>注： 点击<Icon type="md-build" size="14" color="#3f4a56"></Icon>图标，可以设置流转的附加字段及授权用户。</span>
          <div>
            <Button type="primary" style="width: 100px;margin-right: 100px;margin-left: 30px;" :loading="saveing" @click="save()">
              <span v-if="!saveing">保存</span>
              <span v-else>保存中...</span>
            </Button>
            <Button style="width: 100px;" @click="cancel">取消</Button>
          </div>

      </i-col>
    </Card>
    <maskLayer v-if="showPermissionEdit">
      <Modal v-model="showPermissionEdit" title="配置流程扭转的附加字段、附件与用户权限" width="800px" draggable
        :mask-closable="maskClosable" ok-text="提交" @on-cancel="showPermissionEdit = false;editPowerClose()">
        <!-- <Icon type="ios-close" slot="close" @click="showPermissionEdit = false;editPowerClose()" /> -->
        <div class="my-model" :class="{'my-model-overflow-y':scrollable}">
          <div class="edit-top">
            <span class="edit-span">当前扭转 :&nbsp;</span>
            <Card class="my-card" :padding="cardPadding">{{flowExtend.beforeFlow}}</Card>
            <Icon class="edit-icon" type="md-arrow-round-forward" size="30" color="#3582fb" />
            <Card class="my-card" :padding="cardPadding">{{flowExtend.afterFlow}}</Card>
          </div>
          <div class="edit-attr">
            <p class="p-title">
              附加字段配置
            </p>
            <p>
              配置状态流转过程中需要额外填写的字段，可以设置是否必填和默认值。
            </p>
            <br />
            <Button type="text" icon="md-add" @click="addEditAttrCfgRow()">添加字段</Button><br/>
            <i-table :columns="editAttrCfgHead" :data="editAttrCfgData"></i-table>
          </div>
          <div class="edit-file">
            <p class="p-title">
              附加附件配置
            </p>
            <p>
              配置状态流转过程中需要额外上传的附件，可以设置是否必填。
            </p>
            <br />
            <Button type="text" icon="md-add" @click="addEditFileCfgRow()">添加附件</Button><br/>
            <i-table :columns="editFileCfgHead" :data="editFileCfgData"></i-table>
            <br />
            <div class="edit-power">
              <p class="p-title">
                用户权限配置
              </p>
              <p>
                配置状态流转用户权限，只有有权限的用户才允许做此流转。如果为空则默认所有人有权限。
              </p>
              <i-table :columns="editPowerHead" :data="editPowerData" :show-header="showHeader"></i-table>
            </div>
          </div>
          <Spin size="large" fix v-if="modelLoading">提交中...</Spin>
        </div>

        <div slot="footer">
            <Button type="primary" :loading="modelLoading" @click="savePowerEdit()" style="width: 100px;margin-right: 20px;">
              <span v-if="!modelLoading">提交</span>
              <span v-else>提交中...</span>
            </Button>
            <Button type="primary" ghost @click="showPermissionEdit = false;editPowerClose()" :disabled="modelLoading" style="width: 100px;">取消</Button>
        </div>
      </Modal>
    </maskLayer>
  </div>
</template>

<script>
import { mapActions, mapMutations } from 'vuex'
import maskLayer from '_c/common/mask-layer.vue'

export default {
  name: '',
  components: {
    maskLayer
  },
  data () {
    return {
      companyId: null,
      businessTypeId: '',
      showPermissionEdit: false,
      showHeader: false,
      flowList: [],
      flowCfgList: [],
      saveing: false,
      loading: false,
      modelLoading: false,
      flowExtend: {},
      cardPadding: 6,
      maskClosable: false,
      scrollable: true,
      labelInValue: true,
      businessAttrList: [],
      auditAttrList: [],
      attrValMap: {},
      roleList: [],
      userList: [],
      defaultValueTypeList: [],
      editAttrCfgHead: [
        {
          title: '字段名称',
          width: 200,
          render: this.editAttrNameRender
        },
        {
          title: '默认值类型',
          width: 150,
          render: this.editAttrDefaultValueTypeRender
        },
        {
          title: '默认值/默认值字段',
          width: 200,
          render: this.editAttrDefaultValueRender
        },
        {
          title: '是否必填',
          render: this.editAttrRequiredRender
        },
        {
          title: '删除',
          render: this.editAttrDelRender
        }
      ],
      editAttrCfgData: [],
      newEditAttrCfgData: [],
      editFileCfgHead: [
        {
          title: '附件名称',
          render: this.editFileNameReder
        },
        {
          title: '是否必填',
          render: this.editFileRequiredRender
        },
        {
          title: '删除',
          render: this.editFileDelRender
        }
      ],
      editFileCfgData: [],
      fileCfgList: [],
      editPowerHead: [
        {
          title: '权限类型',
          key: 'powerType',
          width: 150
        },
        {
          title: '权限数据',
          render: this.editPowerRender,
          align: 'left'
        }
      ],
      editPowerData: [
        {
          powerType: '用户组',
          powerTypeCode: 'role',
          roleIds: []
        },
        {
          powerType: '其它用户',
          powerTypeCode: 'user',
          userIds: []
        }
      ],
      powerData: {
        roleIds: [],
        userIds: []
      }
    }
  },
  mounted () {
    const query = this.$route.query
    this.businessTypeId = query.businessTypeId
    this.getData()
  },
  methods: {
    ...mapMutations([
      'closeTag'
    ]),
    ...mapActions([
      'getBusinessFlowExtendAll',
      'workFlowExists',
      'editWorkFlow',
      'findCompanyRoleList',
      'findCompanyUser',
      'findFlowExtendSettingInfo',
      'savePower'
    ]),
    getData: function () {
      const data = {
        businessTypeId: this.businessTypeId
      }
      this.loading = true
      this.getBusinessFlowExtendAll(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          let data = res.data.data
          this.flowList = this.createColumns(data.flowList)
          this.flowCfgList = this.createDatas(data.flowCfgList)
          this.loading = false
        }
      })
    },
    createColumns: function (flowList) {
      if (flowList === undefined || flowList === null || flowList.length === 0) {
        return []
      }

      let columns = [{
        title: ' ',
        align: 'left',
        key: 'flowNameView',
        width: 220
      }]
      for (let flow of flowList) {
        columns.push({
          title: flow.flowName,
          key: 1,
          align: 'center',
          flowId: flow.flowId,
          flowCode: flow.flowCode,
          flowName: flow.flowName,
          render: this.render
        })
      }
      return columns
    },
    createDatas: function (flowCfgList) {
      if (flowCfgList === undefined || flowCfgList === null || flowCfgList.length === 0) {
        return []
      }

      let datas = []
      for (let flowCfg of flowCfgList) {
        let data = {
          flowId: flowCfg.flowId,
          flowName: flowCfg.flowName,
          flowNameView: '从【' + flowCfg.flowName + '】可流转到',
          flowCode: flowCfg.flowCode,
          flowType: flowCfg.flowType,
          flowExtends: {}
        }

        for (let temp of flowCfgList) {
          data['myIconColor-' + flowCfg.flowId + '-' + temp.flowId] = '#3f4a56'
          let flowExtend = flowCfg.flowExtendList.find((v) => {
            return v.flowId === temp.flowId
          })

          let hasChecked = !(flowExtend === undefined || flowExtend == null)
          data.flowExtends[temp.flowId] = {}
          data.flowExtends[temp.flowId]['value'] = hasChecked
          data.flowExtends[temp.flowId]['flowCode'] = temp.flowCode
          data.flowExtends[temp.flowId]['flowName'] = temp.flowName
        }

        datas.push(data)
      }

      return datas
    },
    render: function (h, { row, column, index }) {
      return h('div',
        [
          h('Checkbox', {
            props: {
              'value': row.flowExtends[column.flowId]['value'],
              'disabled': row.flowId === column.flowId || row.flowType === 0
            },
            on: {
              'on-change': (value) => {
                row.flowExtends[column.flowId]['value'] = value
                this.flowCfgList[index].flowExtends[column.flowId]['value'] = value
              }
            }
          }),
          h('span', {
            style: {
              cursor: 'pointer'
            },
            on: {
              mouseover: () => {
                row['myIconColor-' + row.flowId + '-' + column.flowId] = '#3582fb'
              },
              mouseleave: () => {
                row['myIconColor-' + row.flowId + '-' + column.flowId] = '#3f4a56'
              }
            }
          },
          row.flowExtends[column.flowId]['value'] && row.flowType !== 0 ? [
            h('Icon', {
              props: {
                type: 'md-build',
                size: 14,
                color: row['myIconColor-' + row.flowId + '-' + column.flowId]
              },
              on: {
                click: () => {
                  const data = {
                    businessTypeId: this.businessTypeId,
                    beforeFlow: row.flowCode,
                    afterFlow: column.flowCode
                  }
                  this.workFlowExists(data).then(res => {
                    if (res && res.status === 200 && res.data.code === 10000) {
                      let data = res.data.data
                      if (data.isExists) {
                        this.flowExtend.beforeFlow = row.flowName
                        this.flowExtend.beforeFlowCode = row.flowCode
                        this.flowExtend.afterFlow = column.flowName
                        this.flowExtend.afterFlowCode = column.flowCode
                        this.showPermissionEdit = true

                        let param = {
                          businessTypeId: this.businessTypeId,
                          beforeFlow: row.flowCode,
                          afterFlow: column.flowCode
                        }
                        this.findFlowExtendSettingInfo(param).then(res => {
                          if (res && res.status === 200 && res.data.code === 10000) {
                            let data = res.data.data
                            this.businessAttrList = data.businessAttrList
                            this.auditAttrList = data.auditAttrList
                            this.attrValMap = data.attrValMap
                            this.defaultValueTypeList = data.defaultValueTypeList
                            this.fileCfgList = data.fileCfgList
                            this.editAttrCfgData = data.editAttrCfgData
                            this.newEditAttrCfgData = []
                            for (let attrData of data.editAttrCfgData) {
                              this.newEditAttrCfgData.push(Object.assign({}, attrData))
                            }
                            this.editFileCfgData = data.editFileCfgData
                            this.editPowerData[0].roleIds = data.roleIds
                            this.editPowerData[1].userIds = data.userIds
                            this.powerData.roleIds = data.roleIds
                            this.powerData.userIds = data.userIds
                          }
                        })

                        this.findCompanyRoleList({ }).then(res => {
                          if (res && res.status === 200 && res.data.code === 10000) {
                            this.roleList = res.data.data
                          }
                        })

                        this.findCompanyUser({}).then(res => {
                          if (res && res.status === 200 && res.data.code === 10000) {
                            this.userList = res.data.data
                          }
                        })
                      } else {
                        this.$Message.warning({ content: '请先保存该流程扭转配置，再进行下一步操作！', duration: 5 })
                      }
                    }
                  })
                }
              }
            })
          ] : [h('span', {
            style: {
              paddingLeft: '14px'
            }
          })]
          )
        ]
      )
    },
    // 保存流程配置
    save: function () {
      this.saveing = true
      this.loading = true
      let data = {
        businessTypeId: this.businessTypeId,
        addWorkFlowList: []
      }

      for (let flowCfg of this.flowCfgList) {
        let addWorkFlow = {
          flowCode: flowCfg.flowCode,
          flowExtendList: []
        }

        Object.keys(flowCfg.flowExtends).forEach(key => {
          let flowExtend = flowCfg.flowExtends[key]
          if (flowExtend.value) {
            addWorkFlow.flowExtendList.push(flowExtend.flowCode)
          }
        })

        data.addWorkFlowList.push(addWorkFlow)
      }

      this.editWorkFlow(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success({ content: '保存成功！', duration: 5 })
        } else {
          this.$Message.error({ content: res.data === undefined || res.data === null ? res.message : res.data.message, duration: 5 })
        }
        this.saveing = false
        this.loading = false
      })
    },
    cancel: function () {
      this.closeTag(this.$route)
    },
    editAttrNameRender: function (h, { row, column, index }) {
      let options = []
      let auditAttrList = this.auditAttrList
      let editAttrCfgData = this.newEditAttrCfgData
      for (let attr of auditAttrList) {
        if (attr.id !== 0 && attr.id === row.businessAttrId) {
          attr.hasChecked = true
        }
        options.push(
          h('Option', {
            props: {
              value: attr.id,
              label: attr.columnCh,
              disabled: attr.id === row.businessAttrId ? !attr.hasChecked : attr.hasChecked
            }
          })
        )
      }
      return h('Select', {
        props: {
          value: row.businessAttrId,
          transfer: true,
          clearable: true
        },
        on: {
          'on-change': function (value) {
            // 释放原来选中的选项
            let attr = auditAttrList.find(attr => {
              return attr.id === row.businessAttrId
            })

            if (attr && attr.id !== 0) {
              attr.hasChecked = false
            }

            // 锁定当前选中的项
            attr = auditAttrList.find(attr => {
              return attr.id === value
            })

            if (attr && attr.id !== 0) {
              attr.hasChecked = true
            }

            // 修改行数据
            row.businessAttrId = value
            row.hasClosedType = attr.hasClosedType

            // 修改原数据
            editAttrCfgData[index].businessAttrId = value
            editAttrCfgData[index].hasClosedType = attr.hasClosedType
          }
        }
      },
      options)
    },
    editAttrDefaultValueTypeRender: function (h, { row, column, index }) {
      let editAttrCfgData = this.newEditAttrCfgData
      let options = []
      for (let defaultValueType of this.defaultValueTypeList) {
        if (row.hasClosedType === 1 && defaultValueType.dickKey !== 'fixed_value') {
          continue
        }
        options.push(
          h('Option', {
            props: {
              value: defaultValueType.dickKey,
              label: defaultValueType.dickValue
            }
          })
        )
      }
      return h('Select', {
        props: {
          value: row.defaultValueType,
          transfer: true,
          clearable: true
        },
        on: {
          'on-change': function (value) {
            // 修改行数据
            row.defaultValueType = value
            row.defaultValue = ''

            // 修改原数据
            editAttrCfgData[index].defaultValueType = value
            editAttrCfgData[index].defaultValue = ''
          }
        }
      },
      options)
    },
    editAttrDefaultValueRender: function (h, { row, column, index }) {
      let editAttrCfgData = this.newEditAttrCfgData
      let options = []
      for (let attr of this.businessAttrList) {
        options.push(
          h('Option', {
            props: {
              value: attr.columnName,
              label: attr.columnCh
            }
          })
        )
      }
      let select = h('Select', {
        props: {
          value: row.defaultValue,
          transfer: true,
          clearable: true
        },
        on: {
          'on-change': function (value) {
            // 修改行数据
            row.defaultValue = value

            // 修改原数据
            editAttrCfgData[index].defaultValue = value
          }
        }
      },
      options)

      let optionsClose = []
      let defaultOption = null
      let attrValList = this.attrValMap[row.businessAttrId]

      if (row.hasClosedType === 1 && attrValList && attrValList.length > 0) {
        defaultOption = attrValList[0].id
        for (let attrVal of attrValList) {
          optionsClose.push(
            h('Option', {
              props: {
                value: attrVal.id,
                label: attrVal.valueCh
              }
            })
          )
        }
      }

      let selectClose = h('Select', {
        props: {
          value: row.defaultValue ? parseInt(row.defaultValue) : defaultOption,
          transfer: true,
          clearable: true
        },
        on: {
          'on-change': function (value) {
            // 修改行数据
            row.defaultValue = value

            // 修改原数据
            editAttrCfgData[index].defaultValue = value
          }
        }
      }, optionsClose)

      let input = h('Input', {
        props: {
          value: row.defaultValue,
          placeholder: '请输入默认值...'
        },
        on: {
          'on-keyup': function (e) {
            // 修改行数据
            row.defaultValue = e.path[0].value

            // 修改原数据
            editAttrCfgData[index].defaultValue = e.path[0].value
          }
        }
      })

      let span = h('span')
      return row.defaultValueType === 'column_value' ? select : row.defaultValueType === 'fixed_value' ? (row.hasClosedType === 1 ? selectClose : input) : span
    },
    editAttrRequiredRender: function (h, { row, column, index }) {
      let editAttrCfgData = this.newEditAttrCfgData
      return h('Checkbox', {
        props: {
          value: row.required
        },
        on: {
          'on-change': function (value) {
            row.required = value

            // 修改原数据
            editAttrCfgData[index].required = value
          }
        }
      })
    },
    editAttrDelRender: function (h, { row, column, index }) {
      let editAttrCfgData = this.editAttrCfgData
      let newEditAttrCfgData = this.newEditAttrCfgData
      let auditAttrList = this.auditAttrList
      return h('Button', {
        props: {
          type: 'text',
          icon: 'md-close'
        },
        on: {
          click: function () {
            // 释放本行选中的选项
            let attr = auditAttrList.find(attr => {
              return attr.id === row.businessAttrId
            })
            if (attr.id !== 0) {
              attr.hasChecked = false
            }

            editAttrCfgData.splice(index, 1)
            newEditAttrCfgData.splice(index, 1)
          }
        }
      })
    },
    addEditAttrCfgRow: function () {
      let editAttrCfgRow = {
        businessAttrId: 0,
        required: false,
        defaultValueType: '',
        defaultValue: ''
      }
      this.editAttrCfgData.push(editAttrCfgRow)
      this.newEditAttrCfgData.push(editAttrCfgRow)
    },
    editFileNameReder: function (h, { row, column, index }) {
      let fileCfgList = this.fileCfgList
      let editFileCfgData = this.editFileCfgData
      let options = []
      for (let fileCfg of fileCfgList) {
        if (fileCfg.id !== 0 && fileCfg.id === row.fileId) {
          fileCfg.hasChecked = true
        }
        options.push(
          h('Option', {
            props: {
              value: fileCfg.id,
              label: fileCfg.fileName,
              disabled: fileCfg.id === row.fileId ? !fileCfg.hasChecked : fileCfg.hasChecked
            }
          })
        )
      }
      return h('Select', {
        props: {
          value: row.fileId,
          'label-in-value': true,
          transfer: true
        },
        on: {
          'on-change': function (valObj) {
            // 当数据源被删除是会出发此事件，参数是undefined
            if (valObj === undefined) {
              return
            }

            // 释放原来选中的选项
            let file = fileCfgList.find(file => {
              return file.id === row.fileId
            })
            if (file.id !== 0) {
              file.hasChecked = false
            }

            // 锁定当前选中的项
            file = fileCfgList.find(file => {
              return file.id === valObj.value
            })

            if (file.id !== 0) {
              file.hasChecked = true
            }

            // 修改行数据
            row.fileId = valObj.value

            // 修改源数据
            editFileCfgData[index].fileId = valObj.value
          }
        }
      },
      options)
    },
    editFileRequiredRender: function (h, { row, column, index }) {
      let editFileCfgData = this.editFileCfgData
      return h('Checkbox', {
        props: {
          value: row.required
        },
        on: {
          'on-change': function (value) {
            row.required = value

            // 修改原数据
            editFileCfgData[index].required = value
          }
        }
      })
    },
    editFileDelRender: function (h, { row, column, index }) {
      let editFileCfgData = this.editFileCfgData
      return h('Button', {
        props: {
          type: 'text',
          icon: 'md-close'
        },
        on: {
          click: function () {
            editFileCfgData.splice(index, 1)
          }
        }
      })
    },
    addEditFileCfgRow: function () {
      let editFileCfgRow = {
        workFlowId: null,
        fileId: 0,
        required: false
      }
      this.editFileCfgData.push(editFileCfgRow)
    },
    editPowerRender: function (h, { row, column, index }) {
      let newH = null
      let powerData = this.powerData
      if (row.powerTypeCode === 'role') {
        let options = []
        let roleList = this.roleList
        for (let role of roleList) {
          options.push(
            h('Checkbox',
              {
                props: {
                  label: role.id
                },
                style: {
                  marginRight: '20px'
                }
              }
              , role.roleName)
          )
        }
        newH = h('CheckboxGroup', {
          props: {
            value: row.roleIds

          },
          on: {
            'on-change': function (roleIds) {
              row.roleIds = roleIds

              powerData.roleIds = roleIds
            }
          }
        }, options)
      } else if (row.powerTypeCode === 'user') {
        let options = []
        let userList = this.userList
        for (let user of userList) {
          options.push(
            h(
              'Option',
              {
                props: {
                  value: user.userId,
                  label: user.userName
                }
              }
            )
          )
        }

        newH = h('Select', {
          props: {
            multiple: true,
            filterable: true,
            // 'remote-method': function () {

            // },
            // remote: true,
            transfer: true,
            value: row.userIds
          },
          on: {
            'on-change': function (userIds) {
              row.userIds = userIds

              powerData.userIds = userIds
            }
          }
        }, options)
      }

      return newH
    },
    // 保存流程扭转权限、字段与附件配置
    savePowerEdit: function () {
      this.modelLoading = true
      this.scrollable = false
      let data = {
        businessTypeId: this.businessTypeId,
        beforeFlow: this.flowExtend.beforeFlowCode,
        afterFlow: this.flowExtend.afterFlowCode,
        roleIds: this.powerData.roleIds,
        userIds: this.powerData.userIds
      }
      console.log(this.newEditAttrCfgData)
      let workFlowAttrList = []
      let sort = 1
      for (let editAttrCfg of this.newEditAttrCfgData) {
        if (editAttrCfg.businessAttrId === 0) {
          continue
        }
        workFlowAttrList.push({
          businessAttrId: editAttrCfg.businessAttrId,
          required: editAttrCfg.required ? 1 : 0,
          defaultValueType: editAttrCfg.defaultValueType,
          defaultValue: editAttrCfg.defaultValue,
          sort: sort
        })
        sort++
      }
      data.workFlowAttrList = workFlowAttrList

      let workFlowFileList = []
      sort = 1
      for (let editFileCfg of this.editFileCfgData) {
        if (editFileCfg.fileId === 0) {
          continue
        }
        workFlowFileList.push({
          businessFileId: editFileCfg.fileId,
          required: editFileCfg.required ? 1 : 0,
          sort: sort
        })
        sort++
      }
      data.workFlowFileList = workFlowFileList

      this.savePower(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success({ content: '提交成功！', duration: 5 })
          this.showPermissionEdit = false
        }

        this.modelLoading = false
        this.scrollable = true
        this.editPowerClose()
      })
    },
    // 流程扭转权限配置页面关闭时重置相关数据
    editPowerClose: function () {
      this.flowExtend.beforeFlowCode = null
      this.flowExtend.afterFlowCode = null
      this.editPowerData[0].roleIds = []
      this.editPowerData[1].userIds = []
      this.powerData.roleIds = []
      this.powerData.userIds = []
      this.editAttrCfgData = []
      this.newEditAttrCfgData = []
      this.editFileCfgData = []
    }
  }
}
</script>
