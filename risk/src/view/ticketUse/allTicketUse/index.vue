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
               placeholder="输入用信人搜索"
               class="search-input"
               v-model="form.applyUserName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入授信项目搜索"
               class="search-input"
               v-model="form.itemName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入用信ID搜索"
               class="search-input"
               v-model="form.id"
               style="width: 200px" />
        <Input clearable
               placeholder="输入用信申请ID搜索"
               class="search-input"
               v-model="form.useApplyId"
               style="width: 200px" />
      </div>
      <div class="search-con-top">
        用信开始日 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择用信开始日"
                    @on-change="(value) => onDateChang('useBeginDateBegin', value)"
                    v-model="form.useBeginDateBegin"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择用信开始日"
                    @on-change="(value) => onDateChang('useBeginDateEnd', value)"
                    v-model="form.useBeginDateEnd"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        应结清日 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择应结清日"
                    @on-change="(value) => onDateChang('shouldRefundDateBegin', value)"
                    v-model="form.shouldRefundDateBegin"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择应结清日"
                    @on-change="(value) => onDateChang('shouldRefundDateEnd', value)"
                    v-model="form.shouldRefundDateEnd"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
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
                  <Button :disabled="row.refundStatus === 0"
                          @click="updateStatus(row.id, 0)"
                          type="text">正常</Button>
                </DropdownItem>
                <DropdownItem>
                  <Button :disabled="row.refundStatus === 1"
                          @click="updateStatus(row.id, 1)"
                          type="text">逾期</Button>
                </DropdownItem>
                <DropdownItem>
                  <Button :disabled="row.refundStatus === 2"
                          @click="updateStatus(row.id, 2)"
                          type="text">已结清</Button>
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
  name: 'allticketuse',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        applyUserName: '',
        itemName: '',
        useApplyId: '',
        id: '',
        useBeginDateBegin: '',
        useBeginDateEnd: '',
        shouldRefundDateBegin: '',
        shouldRefundDateEnd: '',
        creditId: '',
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
          title: '用信ID',
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
                  this.viewticketDetail(params.row)
                }
              }
            }, params.row.id)
          }
        },
        {
          title: '用信类型',
          align: 'center',
          key: 'useTypeName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.useTypeName)
          }
        },
        {
          title: '用信人',
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
          title: '用信项目',
          align: 'center',
          key: 'itemName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.itemName)
          }
        },
        {
          title: '用信金额（元）',
          key: 'useMoney',
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
          title: '用信开始日期',
          key: 'useBeginDate',
          align: 'center',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.useBeginDate)
          }
        },
        {
          title: '应结清日',
          align: 'center',
          key: 'shouldRefundDate',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.shouldRefundDate)
          }
        },
        {
          title: '还款状态',
          align: 'center',
          key: 'refundStatusCH',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.refundStatusCH)
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
                  this.view(params.row.creditApplyId, params.row)
                }
              }
            }, params.row.creditApplyId)
          }
        },
        {
          title: '关联用信申请ID',
          align: 'center',
          key: 'useApplyId',
          width: 120,
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.viewticketapply(params.row.useApplyId, params.row)
                }
              }
            }, params.row.useApplyId)
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
      'findCreditUseAllList',
      'updateRefundStatus'
    ]),
    getData () {
      this.tableLoading = true
      this.findCreditUseAllList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { list, total } = res.data.data
          this.list = list
          this.total = total
          // this.pages = pages
        }
      })
    },
    updateStatus (id, refundStatus) {
      const data = {
        id,
        refundStatus
      }
      this.updateRefundStatus(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.info('修改成功')
          this.getData()
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
    viewticketapply (id, e) {
      const route = {
        name: 'viewticketapply',
        query: {
          companyId: e.companyId,
          businessTypeId: 2,
          creditApplyId: id,
          flowCode: 'ticket99'
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
    onClickTab (isWait) {
      this.form.isWait = isWait === 'name2'
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
