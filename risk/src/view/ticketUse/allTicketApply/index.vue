<style lang="less" scoped>
@import "../../../index.less";
.ivu-dropdown-item:hover {
  background-color: #ffffff;
}
</style>

<template>
  <div class="list-table">
    <Card :bordered="false">
      <div class="search-con-top">
        <Input clearable
               placeholder="输入申请人搜索"
               class="search-input"
               v-model="form.applyUserName"
               style="width: 200px" />
        <!-- <Select v-model="form.businessTypeId"
                placeholder="选择业务类型"
                clearable
                style="width:200px">
          <Option :value="2">开商票</Option>
        </Select> -->
        <Input clearable
               placeholder="输入授信项目搜索"
               class="search-input"
               v-model="form.creditItemName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入用信申请ID搜索"
               class="search-input"
               v-model="form.id"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联授信ID搜索"
               class="search-input"
               v-model="form.creditId"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联授信申请ID搜索"
               class="search-input"
               v-model="form.creditApplyId"
               style="width: 200px" />
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <Table ref="selection"
             border
             stripe
             :loading="tableLoading"
             :columns="columns"
             :data="list"
             :highlight-row="true">
        <template slot-scope="{ row, index }"
                  slot="icon">
          <div @mouseenter="show(index)">
            <Dropdown placement="bottom-start">
              <a href="javascript:void(0)">
                <Icon v-show="index === activeIndex"
                      size="20"
                      type="ios-settings"
                      class="table-icon-setting"></Icon>
              </a>
              <DropdownMenu slot="list">
                <DropdownItem>
                  <Button :disabled="!(row.flowCode === 'ticket99' && (row.hasUse === 0 || row.hasUse === null))"
                          @click="addTicketUse(row)"
                          type="text">录入用信</Button>
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
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
    </Card>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'allticketapply',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        businessTypeId: 2,
        creditId: '',
        creditItemName: '',
        applyUserName: '',
        creditApplyId: '',
        id: '',
        size: 10,
        current: 1
      },
      total: 0,
      pages: 0,
      list: [],
      activeIndex: -1,
      columns: [
        {
          title: '',
          width: 50,
          align: 'center',
          slot: 'icon'
        },
        {
          title: '申请ID',
          key: 'id',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewticketapply(params.row)
                }
              }
            }, params.row.id)
          }
        },
        {
          title: '申请人',
          align: 'center',
          key: 'applyUserName',
          width: 200,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.applyUserName)
          }
        },
        {
          title: '业务类型',
          align: 'center',
          width: 100,
          key: 'applyBusiness',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.applyBusiness)
          }
        },
        {
          title: '申请用信项目',
          align: 'center',
          width: 200,
          key: 'creditItemName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.creditItemName)
          }
        },
        {
          title: '申请用信金额（元）',
          key: 'applyMoney',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.applyMoney))
          }
        },
        {
          title: '实际用信金额（元）',
          key: 'useMoney',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.useMoney))
          }
        },
        {
          title: '申请用信期限',
          align: 'center',
          width: 120,
          key: 'applyDeadline',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.applyDeadline)
          }
        },
        {
          title: '申请日期',
          align: 'center',
          width: 150,
          key: 'createTime',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.createTime)
          }
        },
        {
          title: '审批用信金额（元）',
          key: 'auditMoney',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.auditMoney))
          }
        },
        {
          title: '审批用信期限',
          width: 120,
          align: 'center',
          key: 'auditDeadline',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.auditDeadline)
          }
        },
        {
          title: '流程状态',
          align: 'center',
          width: 120,
          key: 'flowName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.flowName)
          }
        },
        {
          title: '关联授信金额（元）',
          key: 'creditMoney',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.creditMoney))
          }
        },
        {
          title: '关联授信ID',
          align: 'center',
          key: 'creditId',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.allaudit(params.row)
                }
              }
            }, params.row.creditId)
          }
        },
        {
          title: '关联授信申请ID',
          align: 'center',
          key: 'creditApplyId',
          width: 150,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.view(params.row.creditApplyId, params.row)
                }
              }
            }, params.row.creditApplyId)
          }
        },
        {
          title: '当前处理用户组',
          align: 'center',
          key: 'disposeRoleNames',
          width: 150,
          render: (h, params) => {
            return h('Tooltip',
              [
                params.row.showDisposeRoleNames, // 显示文字
                h('div', {
                  slot: 'content',
                  style: {
                    whiteSpace: 'normal'
                  }
                }, params.row.disposeRoleNames)// 文字提示内容
              ])
          }
        },
        {
          title: '当前处理人',
          align: 'center',
          key: 'disposeUserNames',
          width: 150,
          render: (h, params) => {
            return h('Tooltip',
              [
                params.row.showDisposeUserNames, // 显示文字
                h('div', {
                  slot: 'content',
                  style: {
                    whiteSpace: 'normal'
                  }
                }, params.row.disposeUserNames)// 文字提示内容
              ])
          }
        }
      ]
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    this.getData()
  },
  methods: {
    ...mapActions([
      'findCreditUseApplyAllList'
    ]),
    getData () {
      this.tableLoading = true
      this.findCreditUseApplyAllList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total } = res.data.data
          this.list = records
          this.total = total
          // this.pages = pages
        }
      })
    },
    view (id, e) {
      const route = {
        name: 'viewapply',
        query: {
          companyId: e.companyId,
          businessTypeId: e.businessTypeId,
          creditApplyId: id,
          flowCode: e.flowCode
        }
      }
      this.$router.push(route)
    },
    allaudit (row) {
      const route = {
        name: 'allaudit',
        query: {
          id: row.creditId
        }
      }
      this.$router.push(route)
    },
    viewticketapply (e) {
      const route = {
        name: 'viewticketapply',
        query: {
          companyId: e.companyId,
          businessTypeId: e.businessTypeId,
          creditApplyId: e.id,
          flowCode: e.flowCode
        }
      }
      this.$router.push(route)
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getData()
    },
    onClickTab (isWait) {
      this.form.isWait = isWait === 'name2'
      this.getData()
    },
    handleSubmit () {
      this.form.current = 1
      this.getData()
    },
    addTicketUse (row) {
      const route = {
        name: 'addticketuse',
        query: {
          useApplyId: row.id
        }
      }
      this.$router.push(route)
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
