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
               placeholder="输入项目搜索"
               class="search-input"
               v-model="form.creditItemName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联授信ID搜索"
               class="search-input"
               v-model="form.creditId"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联用信申请ID搜索"
               class="search-input"
               v-model="form.id"
               style="width: 200px" />
        申请用信申请日期 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择申请用信申请日期"
                    @on-change="(value) => onDateChang('applyDateBegin', value)"
                    v-model="form.applyDateBegin"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择申请用信申请日期"
                    @on-change="(value) => onDateChang('applyDateEnd', value)"
                    v-model="form.applyDateEnd"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <div class="buttons">
        <Tabs value="0"
              @on-click="onClickTab">
          <TabPane label="待录入"
                   name="0"></TabPane>
          <TabPane label="已录入"
                   name="1"></TabPane>
        </Tabs>
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
                  <Button :disabled="form.hasUse === '1'"
                          @click="addTicketUse(row)"
                          type="text">录入用信-商票</Button>
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
  name: 'auditfinishlist',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        creditId: '',
        creditItemName: '',
        id: '',
        applyDateBegin: '',
        applyDateEnd: '',
        hasUse: '0', // 是否录入用信，待录入列表传0，已录入列表传1
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
                mouseenter: () => {
                  this.show(params.index)
                },
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
          title: '用信项目',
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
          key: 'applyDeadline',
          align: 'center',
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
          key: 'applyTime',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.applyTime)
          }
        },
        {
          title: '审批用信金额（元）',
          key: 'auditMoney',
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
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.allaudit(params.row.creditId)
                }
              }
            }, params.row.creditId)
          }
        },
        {
          title: '关联授信申请ID',
          align: 'center',
          key: 'creditApplyId',
          width: 120,
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.view(params.row)
                }
              }
            }, params.row.creditApplyId)
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
    const { creditId } = this.$route.query
    if (creditId) {
      this.form.creditId = creditId
    }
    this.getData()
  },
  methods: {
    ...mapActions([
      'getAuditFinishList',
      'updateRefundStatus'
    ]),
    getData () {
      this.tableLoading = true
      this.getAuditFinishList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total } = res.data.data
          this.list = records
          this.total = total
          // this.pages = pages
        }
      })
    },
    addTicketUse (row) {
      const route = {
        name: 'addticketuse',
        query: {
          creditId: row.id,
          enter: 'creditRecord'
        }
      }
      this.$router.push(route)
    },
    view (e) {
      const route = {
        name: 'viewapply',
        query: {
          companyId: e.companyId,
          businessTypeId: e.businessTypeId,
          creditApplyId: e.id,
          flowCode: e.flowCode
        }
      }
      this.$router.push(route)
    },
    allaudit (id) {
      const route = {
        name: 'allaudit',
        query: {
          id
        }
      }
      this.$router.push(route)
    },
    viewticketDetail (e) {
      const route = {
        name: 'ticketusedetail',
        query: {
          id: e.id,
          useType: e.useType
        }
      }
      this.$router.push(route)
    },
    viewticketapply (row) {
      const route = {
        name: 'viewticketapply',
        query: {
          companyId: row.companyId,
          businessTypeId: row.businessTypeId,
          creditApplyId: row.id,
          flowCode: row.flowCode
        }
      }
      this.$router.push(route)
    },
    show (index) {
      this.activeIndex = index
    },
    onDateChang (name, value) {
      this.form[name] = value
    },
    changePage (current) {
      this.form.current = current
      this.getData()
    },
    onClickTab (hasUse) {
      this.form.hasUse = hasUse
      this.form.current = 1
      this.getData()
    },
    handleSubmit () {
      this.form.current = 1
      this.getData()
    },
    applyTickt (row) {
      const route = {
        name: 'ticketapply',
        query: {
          creditRecordId: row.id,
          businessTypeId: row.businessTypeId,
          creditTicketApplyId: '',
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
