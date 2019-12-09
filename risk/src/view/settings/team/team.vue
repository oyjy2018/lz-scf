<style lang="less">
@import "../../../index.less";
@import "./team.less";
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
                    :list="companyDeptTreeList"
                    :render="renderContent" />
        </div>
        <div slot="right"
             style="height:100%">
          <div class="team-right">
            <!-- <p>{{companyName}}<span v-if="deptName"> - {{deptName}}</span></p> -->
            <p>{{deptName}}</p>
            <Divider />
            <div class="search-con-top">
              <Select v-model="userForm.roleId"
                      style="width:200px"
                      clearable
                      placeholder="用户组">
                <Option v-for="item in roleList"
                        :value="item.id"
                        :key="item.id">{{ item.roleName }}</Option>
              </Select>
              <Input clearable
                     placeholder="请输入姓名搜索"
                     class="search-input"
                     v-model="userForm.userName"
                     style="width: 200px" />
              <CheckboxGroup class="checkbox"
                             v-model="userForm.status">
                <Checkbox :label="0">待激活</Checkbox>
                <Checkbox :label="1">在职</Checkbox>
                <Checkbox :label="2">离职</Checkbox>
              </CheckboxGroup>
              <Button @click="handleSubmit"
                      class="search-btn"
                      type="primary"
                      icon="ios-search">搜索</Button>
            </div>
            <div class="buttons">
              <Button type="primary"
                      icon="md-add"
                      @click="addTeam()"
                      ghost>添加成员</Button>
            </div>
            <Table ref="selection"
                   border
                   stripe
                   :columns="columns"
                   :data="list"
                   :highlight-row="true">
              <template slot-scope="{ row, index }"
                        slot="icon">
                <div @mouseenter="show(index)">
                  <Dropdown placement="bottom-start"
                            :transfer="true">
                    <a href="javascript:void(0)">
                      <Icon v-show="index === activeIndex"
                            class="table-icon-setting"
                            type="md-settings"
                            size="20" />
                    </a>
                    <DropdownMenu slot="list">
                      <DropdownItem>
                        <Button @click="eidtTeam(row)"
                                type="text">编辑</Button>
                      </DropdownItem>
                      <DropdownItem>
                        <Button :disabled="!(row.status === 0)"
                                @click="sendEmail(row)"
                                type="text">重发激活邮件</Button>
                      </DropdownItem>
                      <DropdownItem>
                        <Button :disabled="row.status === 1 || (row.status === 0)"
                                @click="editStatus(row.companyUserId, 1)"
                                type="text">在职</Button>
                      </DropdownItem>
                      <DropdownItem>
                        <Button :disabled="row.status === 2 || (row.status === 0)"
                                @click="editStatus(row.companyUserId, 2)"
                                type="text">离职</Button>
                      </DropdownItem>
                      <DropdownItem>
                        <Button @click="deleteTeam(row)"
                                type="text">删除</Button>
                      </DropdownItem>
                    </DropdownMenu>
                  </Dropdown>
                </div>
              </template>
              <template slot-scope="{ row, index }"
                        slot="status">
                <div @mouseenter="show(index)">{{row.status | statusText}}</div>
              </template>
            </Table>
            <div class="pagination">
              <Page :total="total"
                    :current="form.current"
                    :page-size="form.size"
                    @on-change="changePage"
                    show-elevator
                    show-total></Page>
            </div>
          </div>
        </div>
      </Split>

    </div>
    <Modal v-model="modal"
           :title="`添加子部门-${title}`"
           @on-ok="onOk">
      <Input v-model="form.deptName"
             placeholder="请输入部门名称"
             style="width: 300px; margin-bottom: 15px;" />
      <Select v-model="form.deptHead"
              style="width:300px">
        <Option v-for="item in userList"
                :value="item.id+''"
                :key="item.id">{{ item.userName }}</Option>
      </Select>
    </Modal>
  </div>
</template>

<script>
import TeamLeft from './left'
import { mapState, mapActions } from 'vuex'
import { menuToTree } from '@/libs/util.js'

export default {
  name: 'team',
  components: {
    TeamLeft
  },
  data () {
    return {
      split: 0.2,
      companyDeptTreeList: [],
      companyId: '',
      deptId: '',
      deptName: '',
      selectKeyArr: [0],
      list: [],
      roleList: [],
      userForm: {
        companyId: '',
        deptId: '',
        roleId: '',
        userName: '',
        status: [],
        size: 10,
        current: 1
      },
      total: 0,
      pages: 0,
      title: '',
      modal: false,
      userList: [],
      form: {
        companyId: '',
        deptName: '',
        deptLevel: '',
        deptHead: '',
        remark: '',
        parentId: ''
      },
      activeIndex: -1,
      columns: [
        {
          title: '',
          width: 60,
          align: 'center',
          slot: 'icon'
        },
        {
          title: '姓名',
          key: 'userName',
          width: 120,
          align: 'center',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [h('span', params.row.userName)])
          }
        },
        {
          title: '成员邮箱',
          width: 200,
          align: 'center',
          key: 'email',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [h('span', params.row.email)])
          }
        },
        {
          title: '公司',
          width: 250,
          align: 'center',
          key: 'companyName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [h('span', params.row.companyName)])
          }
        },
        {
          title: '部门',
          width: 250,
          align: 'center',
          key: 'deptName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [h('span', params.row.deptName)])
          }
        },
        {
          title: '用户组',
          width: 200,
          align: 'center',
          key: 'roleName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [h('span', params.row.roleName)])
          }
        },
        {
          title: '加入公司时间',
          width: 150,
          align: 'center',
          key: 'createTime',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [h('span', params.row.createTime)])
          }
        },
        {
          title: '状态',
          key: 'status',
          slot: 'status',
          align: 'center',
          width: 80
        }
      ]
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  watch: {
    companyId (value) {
      if (value) {
        this.getRoleCompanyUserData()
      }
    }
  },
  filters: {
    statusText: value => {
      // 待激活0在职1离职2
      let text = '待激活'
      switch (value) {
        case 1:
          text = '在职'
          break
        case 2:
          text = '离职'
          break
      }
      return text
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    ...mapActions([
      'getCompanyDepTree',
      'getUserList',
      'deleteUserInfo',
      'addDept',
      'editDept',
      'deleteDept',
      'editUserStatus',
      'getRoleCompanyUser',
      'reSendInvite',
      'getUserByCompanyId'
    ]),
    // left
    renderParent (h, { root, node, data }) {
      return h('span', {
        style: {
          display: 'inline-block',
          cursor: 'pointer',
          color: node.node.selected ? '#2C8EF3' : '#666666', // 根据选中状态设置样式
          width: '90%'
        },
        on: {
          click: () => {
            if (!node.node.selected) {
              this.$refs.left.$refs.tree.handleSelect(node.nodeKey)
              this.selectNodeKey(root, node, data)
            } // 手动选择树节点
          }
        }
      }, [h('span', {
        style: {
          display: 'inline-block',
          lineHeight: '14px',
          fontSize: '14px',
          width: '80%',
          'overflow': 'hidden',
          'text-overflow': 'ellipsis',
          'white-space': 'nowrap'
        }
      }, data.title),
      h('span', {
        style: {
          display: 'inline-block',
          float: 'right'
        }
      }, [h('Dropdown', {
        placement: 'bottom-start'
      }, [h('Icon', {
        props: {
          type: 'ios-settings'
        },
        class: 'table-icon-setting'
      }),
      h('DropdownMenu', {
        slot: 'list'
      }, [h('DropdownItem', [
        h('span', {
          on: {
            click: () => {
              this.addTeam(data)
            }
          }
        }, '添加成员')
      ]),
      h('DropdownItem', [
        h('span', {
          on: {
            click: () => {
              this.addDeptModal(data)
            }
          }
        }, '添加子部门')
      ])])])])])
    },
    renderContent (h, { root, node, data }) {
      return h('span', {
        style: {
          display: 'inline-block',
          cursor: 'pointer',
          color: node.node.selected ? '#2C8EF3' : '#666666', // 根据选中状态设置样式
          width: '90%'
        },
        on: {
          click: (event) => {
            if (!node.node.selected) {
              event.preventDefault()
              this.$refs.left.$refs.tree.handleSelect(node.nodeKey)
              this.selectNodeKey(root, node, data)
            } // 手动选择树节点
          }
        }
      }, [h('span', {
        style: {
          display: 'inline-block',
          lineHeight: '14px',
          fontSize: '14px',
          width: '80%',
          'overflow': 'hidden',
          'text-overflow': 'ellipsis',
          'white-space': 'nowrap'
        }
      }, data.title),
      h('span', {
        style: {
          display: 'inline-block',
          float: 'right'
        }
      }, [h('Dropdown', {
        placement: 'bottom-start'
      }, [h('Icon', {
        props: {
          type: 'ios-settings'
        },
        style: {
          marginRight: '8px'
        },
        class: 'table-icon-setting'
      }),
      h('DropdownMenu', {
        slot: 'list'
      }, [h('DropdownItem', [
        h('span', {
          on: {
            click: (event) => {
              event.preventDefault()
              this.addTeam(data)
            }
          }
        }, '添加成员')
      ]),
      h('DropdownItem', [
        h('span', {
          on: {
            click: (event) => {
              event.preventDefault()
              this.addDeptModal(data)
            }
          }
        }, '添加子部门')
      ]),
      h('DropdownItem', [
        h('span', {
          on: {
            click: (event) => {
              event.preventDefault()
              this.editDeptModal(data)
            }
          }
        }, '编辑部门')
      ]),
      h('DropdownItem', [
        h('span', {
          on: {
            click: (event) => {
              event.preventDefault()
              this.deleteDeptModal(data)
            }
          }
        }, '删除部门')
      ])])])])])
    },
    selectNodeKey (root, node, data) {
      const parentKey = root.find(el => el === node).parent
      // const parent = root.find(el => el.nodeKey === parentKey).node
      // const index = parent.children.indexOf(data)
      // 判断是否有父级
      if (parentKey) {
        this.companyId = data.companyId
        this.deptId = data.id
        this.deptName = data.deptName
      } else {
        this.companyId = data.companyId
        this.deptId = data.id
        this.deptName = data.deptName
      }
      this.getUserListData()
    },
    init () {
      this.title = ''
      this.form = {
        companyId: '',
        deptName: '',
        deptLevel: '',
        deptHead: '',
        remark: '',
        parentId: ''
      }
      this.getCompanyDepTree().then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.companyId = res.data.data[0].companyId
          this.deptId = res.data.data[0].id
          this.deptName = res.data.data[0].deptName
          this.companyDeptTreeList = menuToTree(res.data.data, this.renderParent)
          this.$nextTick(() => {
            this.$refs.left.$refs.tree.handleSelect(0)
          })
          this.getUserListData()
        }
      })
    },
    getRoleCompanyUserData () {
      this.getRoleCompanyUser({ companyId: this.companyId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.roleList = res.data.data
        }
      })
    },
    // right
    getUserListData () {
      const data = this.userForm
      data.companyId = this.companyId
      data.deptId = this.deptId
      this.getUserList(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total, pages } = res.data.data
          this.list = records
          this.total = total
          this.pages = pages
        }
      })
    },
    addDeptFn () {
      this.addDept(this.form).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success({
            content: '添加子部门成功',
            duration: 3
          })
          this.init()
        }
      })
    },
    getUserByCompanyIdData ({ companyId }) {
      this.getUserByCompanyId({ companyId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.userList = res.data.data
        }
      })
    },
    addDeptModal (data) {
      // if (this.form.companyId !== data.companyId) {
      this.getUserByCompanyIdData({ companyId: data.companyId })
      // }
      this.title = data.title
      this.form.companyId = data.companyId
      this.form.parentId = data.id
      this.modal = true
    },
    onOk () {
      if (this.form.deptName === '') {
        this.$Message.warning({
          content: '部门名字不能为空',
          duration: 5,
          closable: true
        })
      } else {
        this.addDeptFn()
      }
    },
    editDeptFn () {
      this.editDept(this.form).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success({
            content: '编辑子部门成功',
            duration: 3
          })
          this.init()
        }
      })
    },
    editDeptModal (data) {
      this.form.companyId = data.companyId
      this.form.id = data.id
      this.form.deptName = data.deptName
      this.form.deptHead = data.deptHead
      this.form.remark = data.remark
      this.form.parentId = data.parentId

      this.$Modal.confirm({
        title: '编辑子部门',
        onOk: () => {
          if (this.form.deptName === '') {
            this.$Message.warning({
              content: '部门名字不能为空',
              duration: 5,
              closable: true
            })
          } else {
            this.editDeptFn()
          }
        },
        render: (h) => {
          return h('div', [
            h('Input', {
              props: {
                value: this.form.deptName,
                autofocus: true,
                placeholder: '请输入部门名称'
              },
              style: {
                marginBottom: '10px'
              },
              on: {
                input: (val) => {
                  this.form.deptName = val
                }
              }
            }),
            h('Select', {
              props: {
                value: this.form.deptHead,
                autofocus: true,
                placeholder: '请选择部门负责人'
              },
              on: {
                'on-change': (val) => {
                  this.form.deptHead = val
                }
              }
            }, [h('Option', {
              props: {
                value: ''
              }
            })])
          ])
        }
      })
    },
    deleteDeptModal (data) {
      let id = data.id
      let title = data.title

      this.$Modal.confirm({
        title: '提示',
        content: '确定删除【' + title + '】吗？',
        onOk: () => {
          this.deleteDept({ id }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success({
                content: '删除子部门成功',
                duration: 3
              })
              this.selectKeyArr = [0]
              this.init()
            }
          })
        },
        onCancel: () => {
          this.$Message.info('取消删除')
        }
      })
    },
    addTeam () {
      const route = {
        name: 'team-add',
        query: {
          companyId: arguments.length ? arguments[0].companyId : this.companyId,
          companyName: arguments.length ? arguments[0].deptName : this.deptName,
          deptId: arguments.length ? arguments[0].id : this.deptId
        }
      }
      this.$router.push(route)
    },
    eidtTeam (row) {
      const route = {
        name: 'team-edit',
        query: {
          companyId: this.companyId,
          userId: row.id,
          email: row.email
        }
      }
      this.$router.push(route)
    },
    sendEmail (row) {
      this.reSendInvite({ email: row.email }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success('激活邮件发送成功')
          this.getUserListData()
        }
      })
    },
    deleteTeam (row) {
      this.$Modal.confirm({
        title: '提示',
        content: `确定删除【${row.email}】吗？`,
        onOk: () => {
          this.deleteUserInfo({ companyUserId: row.companyUserId }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success('删除成功')
              this.getUserListData()
            }
          })
        },
        onCancel: () => {
          this.$Message.info('取消删除')
        }
      })
    },
    editStatus (companyUserId, status) {
      this.editUserStatus({ companyUserId, status }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success(status === 1 ? '修改在职成功' : '修改离职成功')
          this.getUserListData()
        }
      })
    },
    handleClear (e) {
      if (e.target.value === '') this.searchValue = this.value
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.userForm.current = current
      this.getUserListData()
    },
    handleSubmit ({ userName, password }) {
      this.userForm.current = 1
      this.getUserListData()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
