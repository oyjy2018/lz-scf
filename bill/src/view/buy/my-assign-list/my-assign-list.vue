<style lang="less" scoped>
.ivu-dropdown-item:hover {
  background-color: #ffffff;
}
</style>

<template>
  <div class="list-table">
    <Card :bordered="false">
      <div class="search-con-top">
        <Select clearable
                v-model="form.status"
                placeholder="选择询价单状态"
                style="width:200px">
          <Option v-for="item in inquireStatusArr"
                  :value="item.value"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
        <Input clearable
               placeholder="输入询价方"
               class="search-input"
               v-model="form.companyName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入询价单ID"
               class="search-input"
               v-model="form.id"
               @on-blur="(event) => form.id = isNaN(parseInt(event.target.value)) ? '' : parseInt(event.target.value) + ''"
               style="width: 200px" />
        <Input clearable
               placeholder="输入承兑人"
               class="search-input"
               v-model="form.accepterName"
               style="width: 200px" />
        发布时间 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择发布时间"
                    @on-change="(value) =>form.createTimeBegin = value"
                    format="yyyy-MM-dd HH:mm:ss"
                    v-model="form.createTimeBegin"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择发布时间"
                    format="yyyy-MM-dd HH:mm:ss"
                    @on-change="(value) => form.createTimeEnd = value"
                    v-model="form.createTimeEnd"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="getMyAssignInquireListData"
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
                <DropdownItem v-if="row.status === 1">
                  <Button @click="handleQuote(row)"
                          type="text">报价</Button>
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
    <Quote ref="quote"
           :modal='showBuyModal'
           :id="inquireId"
           @on-click="handleClose" />
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'
import Quote from '../../quote/quote'
import { inquireStatusArr } from '@/libs/status'

export default {
  name: 'myassignlist',
  components: {
    Quote
  },
  data () {
    return {
      inquireStatusArr: inquireStatusArr,
      form: {
        current: 1,
        size: 10,
        accepterName: '',
        id: '',
        status: '',
        createTimeBegin: '',
        createTimeEnd: '',
        companyName: ''
      },
      showBuyModal: false,
      inquireId: -1,
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
          title: '询价单ID',
          key: 'id',
          render: (h, params) => {
            let h1 = h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.handleViewInquireDetail(params.row)
                }
              }
            }, params.row.id)

            let h2 = h('span', params.row.id)

            return list.indexOf('ticket:inquire:myList') !== -1 ? h1 : h2
          }
        },
        {
          title: '询价单状态',
          key: 'statusCH',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.statusCH)
          }
        },
        {
          title: '询价方',
          key: 'inquireName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.inquireName)
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
          title: '发布时间',
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
          title: '关联报价单',
          key: 'relatedQuotationId',
          width: 80,
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.handleViewQuotationDetail(params.row)
                }
              }
            }, params.row.relatedQuotationId)
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
    this.getMyAssignInquireListData()
  },
  methods: {
    ...mapActions([
      'myAssignInquireList'
    ]),
    getMyAssignInquireListData () {
      this.myAssignInquireList(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.records.length
          this.list = res.data.data.records
        }
      })
    },
    handleViewQuotationDetail (row) {
      const route = {
        name: 'sellquotationdetail',
        query: {
          quotationId: row.relatedQuotationId
        }
      }
      this.$router.push(route)
    },
    handleViewInquireDetail (row) {
      const route = {
        name: 'inquiredetail',
        query: {
          id: row.id
        }
      }
      this.$router.push(route)
    },
    handleQuote (row) {
      this.showBuyModal = true
      this.inquireId = row.id
      this.$refs.quote.getDetail(row.id)
    },
    handleClose () {
      this.showBuyModal = false
      this.getMyAssignInquireListData()
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getMyAssignInquireListData()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
