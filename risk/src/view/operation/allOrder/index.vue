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
               placeholder="输入卖方（票方）搜索"
               class="search-input"
               v-model="form.inquireCompanyName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入买方（资方）搜索"
               class="search-input"
               v-model="form.quotationCompanyName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入订单ID搜索"
               class="search-input"
               v-model="form.id"
               style="width: 200px" />
        成交时间 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择成交时间"
                    @on-change="(value) => onDateChang('dealStartDate', value)"
                    v-model="form.dealStartDate"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择成交时间"
                    @on-change="(value) => onDateChang('dealEndDate', value)"
                    v-model="form.dealEndDate"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
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
                  <Button :disabled="!(row.orderStatus === 4 || row.orderStatus === 5)"
                          @click="reStart(row.id)"
                          type="text">重新发起商票状态轮询</Button>
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
  name: 'allorder',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        id: '',
        inquireCompanyName: '',
        quotationCompanyName: '',
        dealEndDate: '',
        dealStartDate: '',
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
          title: '订单ID',
          key: 'id',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewDetail(params.row)
                }
              }
            }, params.row.id)
          }
        },
        {
          title: '订单状态',
          key: 'orderStatusCH',
          align: 'center',
          width: 200,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.orderStatusCH)
          }
        },
        {
          title: '商票状态',
          key: 'billStatusCH',
          align: 'center',
          width: 120,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.billStatusCH)
          }
        },
        {
          title: '支付状态',
          key: 'payStatusCH',
          align: 'center',
          width: 120,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.payStatusCH)
          }
        },
        {
          title: '票方（卖方）',
          key: 'inquireCompanyName',
          align: 'center',
          width: 250,
          ellipsis: true,
          tooltip: true,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('Tooltip', {
                props: { placement: 'top' }
              }, [
                h('span', {
                  style: {
                    display: 'inline-block',
                    overflow: 'hidden',
                    width: params.column._width * 0.8 + 'px',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }
                }, params.row.inquireCompanyName),
                h('span', {
                  slot: 'content',
                  style: { whiteSpace: 'normal', wordBreak: 'break-all' }
                }, params.row.inquireCompanyName)
              ])
            ])
          }
        },
        {
          title: '资方（买方）',
          key: 'quotationCompanyName',
          align: 'center',
          width: 250,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('Tooltip', {
                props: { placement: 'top' }
              }, [
                h('span', {
                  style: {
                    display: 'inline-block',
                    overflow: 'hidden',
                    width: params.column._width * 0.8 + 'px',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }
                }, params.row.quotationCompanyName),
                h('span', {
                  slot: 'content',
                  style: { whiteSpace: 'normal', wordBreak: 'break-all' }
                }, params.row.quotationCompanyName)
              ])
            ])
          }
        },
        {
          title: '承兑人',
          key: 'accepterName',
          align: 'center',
          width: 250,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('Tooltip', {
                props: { placement: 'top' }
              }, [
                h('span', {
                  style: {
                    display: 'inline-block',
                    overflow: 'hidden',
                    width: params.column._width * 0.8 + 'px',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }
                }, params.row.accepterName),
                h('span', {
                  slot: 'content',
                  style: { whiteSpace: 'normal', wordBreak: 'break-all' }
                }, params.row.accepterName)
              ])
            ])
          }
        },
        {
          title: '票面金额（元）',
          key: 'billAmt',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.billAmt))
          }
        },
        {
          title: '到期日',
          key: 'maturityDate',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.maturityDate)
          }
        },
        {
          title: '成交时间',
          key: 'dealTime',
          align: 'center',
          width: 150,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.dealTime)
          }
        },
        {
          title: '关联商票',
          key: 'billNo',
          align: 'center',
          width: 250
        },
        {
          title: '关联询价单',
          key: 'inquireId',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewInquireDetail(params.row)
                }
              }
            }, params.row.inquireId)
          }
        },
        {
          title: '关联报价单',
          key: 'quotationId',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewQuotationDetail(params.row)
                }
              }
            }, params.row.quotationId)
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
      'getAllOrderList',
      'reStartTicketStatusQuery'
    ]),
    getData () {
      this.tableLoading = true
      this.getAllOrderList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { list, total } = res.data.data
          this.list = list
          this.total = total
          // this.pages = pages
        }
      })
    },
    reStart (id) {
      const data = {
        id
      }
      this.reStartTicketStatusQuery(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.info('重新发起成功')
          this.getData()
        }
      })
    },
    viewDetail (row) {
      const route = {
        name: 'operation-orderdetail',
        query: {
          orderId: row.id
        }
      }
      this.$router.push(route)
    },
    viewInquireDetail (row) {
      const route = {
        name: 'operation-inquiredetail',
        query: {
          id: row.inquireId
        }
      }
      this.$router.push(route)
    },
    viewQuotationDetail (row) {
      const route = {
        name: 'operation-quotationdetail',
        query: {
          quotationId: row.quotationId
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
    handleSubmit () {
      this.form.current = 1
      this.getData()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
