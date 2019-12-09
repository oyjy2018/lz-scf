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
               placeholder="所属公司"
               class="search-input"
               v-model="form.companyName"
               style="width: 200px" />
        <Select clearable
                v-model="form.accountType"
                placeholder="账户类型"
                style="width:200px">
          <Option v-for="(value, key) in ACCOUNTTYPE"
                  :value="key"
                  :key="key">{{ value }}</Option>
        </Select>
        <Select clearable
                v-model="form.accountStatus"
                placeholder="账户状态"
                style="width:200px">
          <Option v-for="(value, key) in ACCOUNTSTATUS"
                  :value="key"
                  :key="key">{{ value }}</Option>
        </Select>
        <Button @click="getBanks"
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

// 账户是否授权ECDS状态（0：未认证；1：已认证）ecdsType
const ECDSTYPE = {
  0: '未认证',
  1: '已认证'
}
// 账户状态(1:认证中 2:认证成功 3:认证失败 4:认证过期) accountStatus
const ACCOUNTTYPE = {
  1: '收票账户',
  2: '收款账户'
}
// 账户状态(1:认证中 2:认证成功 3:认证失败 4:认证过期) accountStatus
const ACCOUNTSTATUS = {
  1: '认证中',
  2: '认证成功',
  3: '认证失败',
  4: '认证过期'
}
// 打款认证状态 subStatus
const SUBSTATUS = {
  11: '待发起代付',
  12: '待打款',
  13: '打款成功',
  21: '代付失败',
  22: '打款失败',
  23: '金额确认失败',
  24: '打款申请失败次数超限',
  25: '金额验证失败次数超限',
  30: '退票',
  40: '金额确认成功',
  50: '认证过期'
}

export default {
  name: 'operration-banks',
  components: {},
  data () {
    return {
      ACCOUNTTYPE: ACCOUNTTYPE,
      ACCOUNTSTATUS: ACCOUNTSTATUS,
      form: {
        current: 1,
        size: 10,
        companyName: '',
        accountType: '',
        accountStatus: ''
      },
      activeIndex: -1,
      total: 0,
      list: [],
      columns: [
        {
          title: '银行账号',
          key: 'bankAccountNo',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.isReceiptDefault === 0
              ? params.row.bankAccountNo
              : [h('span', params.row.bankAccountNo), h('icon', { props: { type: 'ios-checkmark', size: 19 }, style: { color: 'red' } }), h('span', '默认')]
            )
          },
          width: 260
        },
        {
          title: '户名',
          key: 'bankAccountName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.bankAccountName)
          }
        },
        {
          title: '银行名称',
          key: 'bankName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.bankName)
          },
          width: 150
        },
        {
          title: '银行省市',
          key: 'provinceName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.provinceName + params.row.cityName)
          }
        },
        {
          title: '银行支行',
          key: 'bankChildName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.bankChildName)
          }
        },
        {
          title: '账户状态',
          key: 'accountStatus',
          width: '90px',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, ACCOUNTSTATUS[params.row.accountStatus])
          }
        },
        {
          title: '打款认证状态',
          key: 'subStatus',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, SUBSTATUS[params.row.subStatus])
          }
        },
        {
          title: '是否授权京东查询银行账户',
          key: 'ecdsType',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, ECDSTYPE[params.row.ecdsType])
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
    if (this.permission.indexOf('operationsMgt:companyBanks:view') !== -1) {
      this.getBanks()
    }
  },
  methods: {
    ...mapActions([
      'companyBanks'
    ]),
    getBanks () {
      this.companyBanks(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.total
          this.list = res.data.data.records
        }
      })
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getBanks()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
