<style lang="less" scoped>
.ivu-dropdown-item:hover {
  background-color: #ffffff;
}
</style>

<template>
  <div class="list-table">
    <Card :bordered="false">
      <div class="search-con-top">
        <Input clearable
               placeholder="输入询价公司"
               class="search-input"
               v-model="form.inquireCompanyName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入报价单ID"
               class="search-input"
               v-model="form.quotationId"
               @on-blur="(event) => form.quotationId = isNaN(parseInt(event.target.value)) ? '' : parseInt(event.target.value) + ''"
               style="width: 200px" />
        <Select clearable
                v-model="form.quotationStatus"
                placeholder="选择报价单状态"
                style="width:200px">
          <Option v-for="item in quotationStatusArr"
                  :value="item.value"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
        报价日期 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择报价日期"
                    @on-change="(value) =>form.quotationCreateStartTime = value"
                    format="yyyy-MM-dd HH:mm:ss"
                    v-model="form.quotationCreateStartTime"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择报价日期"
                    format="yyyy-MM-dd HH:mm:ss"
                    @on-change="(value) => form.quotationCreateEndTime = value"
                    v-model="form.quotationCreateEndTime"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="getMyQuotationListData"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <Table ref="selection"
             :columns="columns"
             :data="list"
             :highlight-row="true">
        <template slot-scope="{ row, index }"
                  slot="icon">
          <div @mouseenter="show(index)">
            <Dropdown placement="right">
              <a href="javascript:void(0)">
                <Icon v-show="index === activeIndex"
                      size="20"
                      type="ios-settings"
                      class="table-icon-setting"></Icon>
              </a>
              <DropdownMenu slot="list">
                <DropdownItem>
                  <Button @click="handleView(row)"
                          type="text">查看关联询价单</Button>
                </DropdownItem>
                <DropdownItem>
                  <Button @click="handleCancelQuotation(row)"
                          type="text">撤回报价</Button>
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
import { quotationStatusArr, inquireStatusArr } from '@/libs/status'

export default {
  name: 'myquotationlist',
  components: {},
  data () {
    return {
      quotationStatusArr: quotationStatusArr,
      inquireStatusArr: inquireStatusArr,
      form: {
        current: 1,
        size: 10,
        quotationId: '',
        inquireCompanyName: '',
        quotationStatus: '',
        quotationCreateStartTime: '',
        quotationCreateEndTime: '',
        surplusVaquotationIdlidDaysBegin: ''
      },
      activeIndex: -1,
      total: 0,
      list: [],
      columns: [
        {
          title: '',
          width: 50,
          align: 'center',
          slot: 'icon'
        },
        {
          title: '报价单ID',
          key: 'quotationId',
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.handleView(params.row)
                }
              }
            }, params.row.quotationId)
          }
        },
        {
          title: '报价单状态',
          key: 'quotationStatus',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, quotationStatusArr[params.row.quotationStatus - 1].label)
          }
        },
        {
          title: '询价公司',
          key: 'inquireCompanyName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.inquireCompanyName)
          }
        },
        {
          title: '关联询价单状态',
          key: 'inquireStatus',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, inquireStatusArr[params.row.inquireStatus - 1].label)
          }
        },
        {
          title: '承兑人',
          key: 'accepterName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.accepterName)
          }
        },
        {
          title: '票面金额（元）',
          key: 'billAmt',
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
          title: '报价（元）',
          key: 'amount',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.amount))
          }
        },
        {
          title: '到期日',
          key: 'maturityDate',
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
          title: '剩余天数',
          key: 'surplusValidDays',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.surplusValidDays)
          }
        },
        {
          title: '年化率（%）',
          key: 'annualRate',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.annualRate)
          }
        },
        {
          title: '每10万扣款（元）',
          key: 'discountFee',
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.discountFee))
          }
        },
        {
          title: '瑕疵',
          key: 'flaws',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.flaws)
          }
        },
        {
          title: '报价操作人',
          key: 'quotationPerson',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.quotationPerson)
          }
        },
        {
          title: '报价时间',
          key: 'quotationCreateTime',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.quotationCreateTime)
          }
        },
        {
          title: '关联询价单',
          key: 'inquireId',
          width: 80,
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.handleViewInquireDetail(params.row)
                }
              }
            }, params.row.inquireId)
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
    if (this.permission.indexOf('ticket:quotation:owner') !== -1) {
      this.getMyQuotationListData()
    }
  },
  methods: {
    ...mapActions([
      'getMyQuotationList',
      'cancelQuotation'
    ]),
    getMyQuotationListData () {
      this.getMyQuotationList(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.total
          this.list = res.data.data.records
        }
      })
    },
    handleView (row) {
      const route = {
        name: 'buyquotationdetail',
        query: {
          quotationId: row.quotationId
        }
      }
      this.$router.push(route)
    },
    handleViewInquireDetail (row) {
      const route = {
        name: 'inquiredetail',
        query: {
          id: row.inquireId
        }
      }
      this.$router.push(route)
    },
    handleCancelQuotation (row) {
      this.$Modal.confirm({
        title: '是否确定取消报价',
        content: '取消将会扣除您公司在平台的信用分,如有疑问，请咨询0755-86571036',
        onOk: () => {
          this.cancelQuotation({ quotationId: row.quotationId }).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.$Message.info('撤回报价成功')
              this.form.current = 1
              this.getMyQuotationListData()
            }
          })
        }
      })
    },
    editStatus (id, status) {
      // if (this.permission.indexOf('gsgl:company:status:edit') !== -1) {
      this.editCompanyStatus({ id, status }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success(status === 1 ? '启用成功' : '禁用成功')
          this.getData()
        }
      })
      // } else {
      //   this.$Message.info('没有权限【编辑公司状态】')
      // }
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getMyQuotationListData()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
