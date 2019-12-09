<style lang="less">
@import "../../../index.less";
@import "../team/team.less";
.collapse-view {
  overflow: hidden;
  height: 74px;
}
.ivu-tree-title-selected {
  background: transparent;
}
</style>

<template>
  <div class="list-table"
       style="height:100%">
    <div class="team">
      <Split v-model="split"
             :min="0.1"
             :max="0.5"
             style="height:100%">
        <div slot="left"
             style="height:100%">
          <TeamLeft ref="left"
                    :openname="openname"
                    :roleActiveName="roleActiveName"
                    :roleGroupList="roleGroupList"
                    @on-select-submenu="onSelectSubmenu"
                    @add-modal="addModal"
                    @delete-modal="deleteModal"
                    @edit-modal="editModal" />
        </div>
        <div slot="right"
             style="height:100%">
          <div class="team-right">
            <Card :bordered="false">
              <p slot="title">
                {{companyName}}<span v-if="roleItem"> - {{roleItem.roleName}}</span>：{{userRoleList.length}}
              </p>
              <a href="#"
                 slot="extra"
                 @click.prevent="changeCollapse">
                <Icon :type="this.height === 'auto' ? 'ios-arrow-up' : 'ios-arrow-down'" />
                展开成员
              </a>
              <div class="collapse-view"
                   :style="{height: height}">
                <Button @click="handleShowModel"
                        type="dashed"
                        icon="ios-add"
                        style="margin-right: 4px">添加成员</Button>
                <Tag v-for="item in userRoleList"
                     :key="item.id"
                     @on-close="handelDeleteRoleUser(item)"
                     type="dot"
                     closable
                     color="success">{{item.userName || ''}}{{'(' + item.userEmail + ')'}}</Tag>
                <Tag type="dot"
                     v-if="!userRoleList.length">暂无成员</Tag>
              </div>
            </Card>
            <Card :bordered="false"
                  dis-hover
                  style="margin-top: 30px">
              <p slot="title">
                功能权限
              </p>
              <ButtonGroup size="large" v-if="systemId === 1">
                  <Button type="primary" @click="handleSystemButton(1)">风控平台</Button>
                  <Button @click="handleSystemButton(2)">商票交易平台</Button>
              </ButtonGroup>
              <ButtonGroup size="large" v-else>
                  <Button @click="handleSystemButton(1)">风控平台</Button>
                  <Button type="primary" @click="handleSystemButton(2)">商票交易平台</Button>
              </ButtonGroup>
              <Tree :data="allGroup"
                    show-checkbox
                    multiple v-if="allGroup.length > 0"></Tree>
            </Card>
            <Button v-if="allGroup.length && roleItem.isEditPrivilege === 1"
                    @click="handleSubmit"
                    style="width: 150px; margin: 20px 0 40px 0"
                    size="large"
                    type="primary">保存</Button>
          </div>
        </div>
      </Split>
    </div>
    <add-user-role v-if="model"
                   :show="model"
                   @handle-ok="handleAddRoleUser"
                   @handle-cancel="model=false"
                   :tree="deptUserTree"></add-user-role>
  </div>
</template>

<script>
import TeamLeft from './left'
import AddUserRole from './addUserRole'
import { mapState, mapActions } from 'vuex'
import { setTimeout } from 'timers'

export default {
  components: {
    TeamLeft,
    AddUserRole
  },
  data () {
    return {
      systemId: 1,
      split: 0.2,
      openname: [],
      roleGroupList: [],
      userRoleList: [],
      roleActiveName: '',
      rolePrivilegeData: null,
      form: {
        id: '',
        companyId: '',
        roleName: ''
      },
      height: '74px',
      value: '',
      searchValue: '',
      model1: '',
      model: false,
      deptUserTree: null,
      allGroup: [],
      systemFunctionVersionList: []
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    }),
    roleActiveArr () {
      return this.roleActiveName.split('-')
    },
    companyId () {
      if (this.roleGroupList.length) {
        return this.roleGroupList[this.roleActiveArr[0]].companyId
      }
      return ''
    },
    companyName () {
      if (this.roleGroupList.length) {
        return this.roleGroupList[this.roleActiveArr[0]].companyName
      }
      return ''
    },
    roleItem () {
      if (this.roleGroupList.length) {
        return this.roleGroupList[this.roleActiveArr[0]].roles[this.roleActiveArr[1]]
      }
      return null
    }
  },
  mounted () {
    this.getRoleGroupList().then(res => {
      if (res && res.status === 200 && res.data.code === 10000) {
        this.roleGroupList = res.data.data
        this.openname = ['0']
        this.roleActiveName = '0-0'
        this.getUserRoleListData()
        this.getSystemFunctionData()
      }
    })
  },
  methods: {
    ...mapActions([
      'getRoleGroupList',
      'getUserRoleList',
      'getSystemFunctionVersionList',
      'getRolePrivilege',
      'editRolePrivilege',
      'getDeptUserTree',
      'addRoleUser',
      'deleteRoleUser',
      'addRole',
      'editRole',
      'deleteRole'
    ]),
    // 获取当前 角色 的 成员
    getUserRoleListData () {
      const data = {
        companyId: this.companyId,
        id: this.roleItem.id
      }
      this.getUserRoleList(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.userRoleList = res.data.data
        }
      })
    },
    handleSystemButton (systemId) {
      this.systemId = systemId
      this.getSystemFunctionData()
    },
    getSystemFunctionData () {
      this.allGroup = []
      this.getSystemFunctionVersionList({ systemId: this.systemId, companyId: this.companyId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          if (res.data.data !== null) {
            this.getRolePrivilegeData(res.data.data)
          }
        }
      })
    },
    // 获取当前 角色 的 数据权限 和 功能权限
    getRolePrivilegeData (list) {
      this.getRolePrivilege({ id: this.roleItem.id }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.rolePrivilegeData = res.data.data
          this.allGroup = list.map(item => {
            item.title = item.menuName
            item.expand = false
            item.children = item.menuList.map(child => {
              child.title = child.menuName
              child.expand = false
              child.children = child.functionList.map(c => {
                c.title = c.functionName
                c.expand = false
                c.checked = res.data.data.functCodes.indexOf(c.functionCode) !== -1
                return c
              })
              return child
            })
            return item
          })
        }
      })
    },
    // left菜单点击子菜单
    onSelectSubmenu (data) {
      if (this.roleActiveName !== data) {
        this.roleActiveName = data
        this.getUserRoleListData()
        this.getSystemFunctionData()
      }
    },
    formInit () {
      this.form = {
        id: '',
        companyId: '',
        roleName: ''
      }
      this.getRoleGroupList().then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.roleGroupList = res.data.data
        }
      })
    },
    addRoleFn () {
      this.addRole(this.form).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success({
            content: '添加用户组成功',
            duration: 3
          })
          this.formInit()
        }
      })
    },
    addModal (item) {
      this.form.companyId = item.companyId
      this.$Modal.confirm({
        title: '添加用户组',
        onOk: () => {
          if (this.form.roleName === '') {
            this.$Message.warning('用户名不能为空')
          } else {
            this.addRoleFn()
          }
        },
        render: (h) => {
          return h('div', [
            h('Input', {
              props: {
                value: this.form.roleName,
                autofocus: true,
                placeholder: '输入用户组名称'
              },
              style: {
                marginBottom: '10px'
              },
              on: {
                input: (val) => {
                  this.form.roleName = val
                }
              }
            })
          ])
        }
      })
    },
    editRoleFn () {
      this.editRole(this.form).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success({
            content: '重命名成功',
            duration: 3
          })
          this.formInit()
        }
      })
    },
    editModal (item, dept) {
      this.form.companyId = item.id
      if (dept) {
        this.form.id = dept.id
        this.form.roleName = dept.roleName
      }
      this.$Modal.confirm({
        title: '重命名',
        onOk: () => {
          if (this.form.roleName === '') {
            this.$Message.warning('用户名不能为空')
          } else {
            this.editRoleFn()
          }
        },
        render: (h) => {
          return h('div', [
            h('Input', {
              props: {
                value: this.form.roleName,
                autofocus: true,
                placeholder: '输入用户组名称'
              },
              style: {
                marginBottom: '10px'
              },
              on: {
                input: (val) => {
                  this.form.roleName = val
                }
              }
            })
          ])
        }
      })
    },
    deleteModal (dept) {
      this.$Modal.confirm({
        title: '提示',
        content: '确定删除【' + dept.roleName + '】吗？',
        onOk: () => {
          setTimeout(() => {
            this.deleteRole({ id: dept.id }).then(res => {
              if (res && res.status === 200 && res.data.code === 10000) {
                this.$Message.success({
                  content: '删除用户组成功',
                  duration: 3
                })
                this.formInit()
              }
            })
          }, 500)
        },
        onCancel: () => {
          this.$Message.info('取消删除')
        }
      })
    },
    // right
    changeCollapse () {
      this.height === 'auto' ? this.height = '74px' : this.height = 'auto'
    },
    dataToDeptUserTree (data) {
      if (data.childrenList && data.childrenList.length) {
        data.childrenList.map(item => {
          item.codes = []
          item.userList.map(subItem => {
            if (subItem) {
              subItem.disabled = false
              this.userRoleList.forEach(user => {
                if (subItem.userId === user.userId) {
                  subItem.disabled = true
                }
              })
              if (!subItem.disabled) {
                item.codes.push(subItem.userId)
              }
            }
            return subItem
          })
          if (item.childrenList && item.childrenList.length) {
            item.childrenList.map(item1 => {
              this.dataToDeptUserTree(item1)
            })
          }
          return item
        })
      }

      data.codes = []
      data.userList.map(subItem => {
        if (subItem) {
          subItem.disabled = false
          this.userRoleList.forEach(user => {
            if (subItem.userId === user.userId) {
              subItem.disabled = true
            }
          })
          if (!subItem.disabled) {
            data.codes.push(subItem.userId)
          }
        }
        return subItem
      })
      return data
    },
    handleShowModel () {
      if (this.companyId) {
        this.deptUserTree = null
        this.getDeptUserTree({ companyId: this.companyId }).then((res) => {
          if (res && res.status === 200 && res.data.code === 10000 && res.data.data) {
            this.deptUserTree = this.dataToDeptUserTree(res.data.data)
            this.model = !this.model
            // this.handleRender()
          } else {
            this.$Message.warning('还没有成员')
          }
        })
      }
    },
    handleRender () {
      this.$Modal.confirm({
        title: '添加成员',
        render: (h) => {
          return h('div', {
            class: 'title'
          }, this.deptUserTree.deptName)
        }
      })
    },
    handleAddRoleUser ({ userIds }) {
      this.model = !this.model
      const data = {
        roleId: this.roleItem.id,
        userIds: userIds
      }

      this.addRoleUser(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.getUserRoleListData()
          this.$Message.success('添加成员成功')
        }
      })
    },
    handelDeleteRoleUser ({ roleUserId, userName, userEmail }) {
      this.$Modal.confirm({
        title: '提示',
        content: `确定删除【${userName || ''}(${userEmail})】吗？`,
        onOk: () => {
          this.deleteRoleUser({ roleUserId }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.getUserRoleListData()
              this.$Message.success('删除成员成功')
            }
          })
        }
      })
    },
    Dropdown () {
      this.$Modal.confirm({
        title: '重命名',
        loading: true,
        onOk: () => {
          setTimeout(() => {
            this.$Modal.remove()
            this.$Message.success('重命名成功！')
          }, 2000)
        },
        render: (h) => {
          return h('Input', {
            props: {
              value: this.value,
              autofocus: true,
              placeholder: '请输入'
            },
            on: {
              input: (val) => {
                this.value = val
              }
            }
          })
        }
      })
    },
    handleSubmit () {
      if (this.roleItem) {
        let data = {
          functions: [],
          systemId: this.systemId,
          roleId: this.roleItem.id
        }
        this.allGroup.forEach(item => {
          item.children = item.menuList.forEach(child => {
            child.children = child.functionList.forEach(c => {
              if (c.checked) {
                let obj = {
                  functCode: c.functionCode,
                  systemId: c.systemId
                }
                data.functions.push(obj)
              }
            })
          })
        })
        this.editRolePrivilege(data).then(res => {
          if (res && res.status === 200 && res.data.code === 10000) {
            this.$Message.success('设置权限成功')
            this.getSystemFunctionData()
          }
        })
      }
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
