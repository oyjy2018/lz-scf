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
               placeholder="输入报价公司"
               class="search-input"
               v-model="form.quotationCompanyName"
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
        <Button @click="getAllQuotation"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <Table ref="selection"
             :columns="columns"
             :data="list"
             :highlight-row="true">
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
  name: 'allQuotation',
  components: {},
  data () {
    return {
      quotationStatusArr: quotationStatusArr,
      inquireStatusArr: inquireStatusArr,
      form: {
        current: 1,
        size: 10,
        quotationId: '',
        quotationCompanyName: '',
        quotationStatus: '',
        quotationCreateStartTime: '',
        quotationCreateEndTime: '',
        inquireId: ''
      },
      activeIndex: -1,
      total: 0,
      list: [],
      columns: [
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
            }, [
              h('span', params.row.quotationId)
            ])
          }
        },
        {
          title: '报价公司',
          key: 'quotationCompanyName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.quotationCompanyName)
            ])
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
            }, [
              h('span', quotationStatusArr[params.row.quotationStatus - 1].label)
            ])
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
            }, [
              h('span', params.row.accepterName)
            ])
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
            }, [
              h('span', params.row.maturityDate)
            ])
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
            }, [
              h('span', params.row.surplusValidDays)
            ])
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
            }, [
              h('span', params.row.annualRate)
            ])
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
            }, [
              h('span', params.row.flaws)
            ])
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
            }, [
              h('span', params.row.quotationCreateTime)
            ])
          }
        },
        {
          title: '关联询价单',
          key: 'inquireId',
          width: 80,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.handleViewInquiredetail(params.row)
                }
              }
            }, [
              h('span', params.row.inquireId)
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
    const { inquireId } = this.$route.query
    if (inquireId) {
      this.form.inquireId = inquireId
    }
    this.getAllQuotation()
  },
  methods: {
    ...mapActions([
      'allQuotation'
    ]),
    getAllQuotation () {
      this.allQuotation(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.total
          this.list = res.data.data.records
        }
      })
    },
    handleViewInquiredetail (row) {
      const route = {
        name: 'operInquiredetail',
        query: {
          id: row.inquireId
        }
      }
      this.$router.push(route)
    },
    handleView (row) {
      const route = {
        name: 'operQuotationdetail',
        query: {
          quotationId: row.quotationId
        }
      }
      this.$router.push(route)
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getAllQuotation()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
