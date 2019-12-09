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
        <!-- <Input clearable
               placeholder="输入流程状态搜索"
               class="search-input"
               v-model="form.flowCode"
               style="width: 200px" /> -->
        <Select v-model="form.flowCode"
                clearable
                placeholder="选择流程状态搜索"
                style="width:200px">
          <Option v-for="item in flowCodeList"
                  :value="item.flowCode"
                  :key="item.flowCode">{{ item.flowName }}</Option>
        </Select>
        <!-- <Select v-model="form.businessTypeId"
                placeholder="选择业务类型"
                clearable
                style="width:200px">
          <Option value="0">开商票</Option>
        </Select> -->
        <Input clearable
               placeholder="输入授信项目搜索"
               class="search-input"
               v-model="form.creditItemName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联授信ID搜索"
               class="search-input"
               v-model="form.creditId"
               style="width: 200px" />
        <Input clearable
               placeholder="输入申请ID搜索"
               class="search-input"
               v-model="form.id"
               style="width: 200px" />
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <!-- <div class="buttons">
        <Tabs value="name2"
              @on-click="onClickTab">
          <TabPane label="所有"
                   name="name1"></TabPane>
          <TabPane label="我的待办"
                   name="name2"></TabPane>
        </Tabs>
      </div> -->
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
                  <Button :disabled="row.status === 1"
                          @click="auditTicket(row)"
                          type="text">审批</Button>
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
  name: 'credituseaudit',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        id: '',
        creditItemName: '',
        creditId: '',
        businessTypeId: '',
        flowCode: '',
        size: 10,
        current: 1
      },
      flowCodeList: [],
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
          title: '申请用信期限',
          align: 'center',
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
          title: '关联授信金额（元）',
          key: 'creditMoney',
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
          width: 100
        },
        {
          title: '关联授信申请ID',
          align: 'center',
          key: 'creditApplyId',
          width: 140,
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
          title: '流程状态',
          align: 'center',
          key: 'flowName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.flowName)
            ])
          }
        },
        {
          title: '当前处理用户组',
          align: 'center',
          key: 'disposeRoleNames',
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
    this.getFlowCodeList1()
  },
  methods: {
    ...mapActions([
      'findCreditUseAuditList',
      'getFlowCodeList'
    ]),
    getData () {
      this.tableLoading = true
      this.findCreditUseAuditList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total } = res.data.data
          this.list = records
          this.total = total
          // this.pages = pages
        }
      })
    },
    getFlowCodeList1 () {
      const data = {
        param: '申请开商票'
      }
      this.getFlowCodeList(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.flowCodeList = res.data.data
        }
      })
    },
    view (id, e) {
      // if (this.permission.indexOf('sxgl:credit:record:my:apply:info') === -1) {
      //   return this.$Message.info('没有权限查看【关联授信申请ID】')
      // }
      const route = {
        name: 'viewapply',
        query: {
          companyId: e.companyId,
          businessTypeId: e.businessTypeId,
          creditApplyId: id,
          flowCode: null
        }
      }
      this.$router.push(route)
    },
    allaudit (creditApplyId) {
      // if (this.permission.indexOf('sxgl:credit:record:my:apply:info') === -1) {
      //   return this.$Message.info('没有权限查看【关联授信申请ID】')
      // }
      const route = {
        name: 'allaudit',
        query: {
          creditApplyId
        }
      }
      this.$router.push(route)
    },
    viewticketapply (e) {
      // if (this.permission.indexOf('sxgl:credit:record:my:apply:info') === -1) {
      //   return this.$Message.info('没有权限查看【关联授信申请ID】')
      // }
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
    auditTicket (row) {
      // if (this.permission.indexOf('sxgl:credit:audit:commit') === -1) {
      //   return this.$Message.info('没有权限【审核】')
      // }
      const route = {
        name: 'auditticket',
        query: {
          companyId: row.companyId,
          businessTypeId: row.businessTypeId,
          creditApplyId: row.id,
          flowCode: row.flowCode
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
