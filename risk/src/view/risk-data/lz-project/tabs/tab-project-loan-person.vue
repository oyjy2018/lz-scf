<style lang="less" scoped>
@import "../project.less";
</style>
<template>
  <div class="project-tab-div">
    <Table border :columns="columns" :data="list"></Table>
    <Modal v-model="showRefund" draggable footer-hide  width="850">
      <p slot="header" style="color:rgba(44,142,243,1);text-align:left">
        <span>还款明细</span>
      </p>
      <Table border :columns="refundColumns" :data="refundList"></Table>
    </Modal>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'ProjectLoanPerson',
  data () {
    return {
      modal_loading: false,
      showRefund: false,
      columns: [
        {
          title: '借款单编号',
          key: 'loanNo',
          align: 'center'
        },
        {
          title: '借款人姓名',
          key: 'debtorName',
          align: 'center'
        },
        {
          title: '放款日期',
          key: 'loanDate',
          align: 'center'
        },
        {
          title: '借款类型',
          key: 'loanType',
          align: 'center'
        },
        {
          title: '借款金额（元）',
          key: 'loanMoney',
          align: 'right',
          render: (h, params) => {
            return h('div', absMoney(params.row.loanMoney))
          }
        },
        {
          title: '已还本金（元）',
          key: 'hasRefundMoney',
          align: 'right',
          render: (h, params) => {
            return h('div', absMoney(params.row.hasRefundMoney))
          }
        },
        {
          title: '应结清日',
          key: 'shouldSettleDate',
          align: 'center'
        },
        {
          title: '是否结清',
          key: 'isSettle',
          align: 'center',
          render: (h, params) => {
            if (params.row.isSettle === '0') {
              return h('div', '否')
            } else if (params.row.isSettle === '1') {
              return h('div', '是')
            }
          }
        },
        {
          title: '资金端',
          key: 'moneySource',
          align: 'center'
        },
        {
          title: '还款明细',
          key: '',
          align: 'center',
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewRefund(params.row.loanNo)
                }
              }
            }, '查看还款')
          }
        }
      ],
      refundColumns: [
        {
          title: '关联借款单编号',
          key: 'loanNo',
          align: 'center'
        },
        {
          title: '还款编号',
          key: 'refundNo',
          align: 'center'
        },
        {
          title: '还款日期',
          key: 'refundDate',
          align: 'center'
        },
        {
          title: '还款金额（元）',
          key: 'refundMoney',
          align: 'right',
          render: (h, params) => {
            return h('div', absMoney(params.row.refundMoney))
          }
        }
      ],
      list: [],
      refundList: []
    }
  },
  methods: {
    ...mapActions([
      'getLoanList',
      'getRefundList'
    ]),
    getData (value) {
      const data = {
        contractNo: value,
        isPersonal: '1'
      }
      this.getLoanList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.list = res.data.data
        }
      })
    },
    viewRefund (loanNo) {
      this.showRefund = true
      if (loanNo) {
        const data = {
          loanNo: loanNo
        }
        this.getRefundList(data).then(res => {
          if (res && res.status === 200 && res.data && res.data.code === 10000) {
            this.refundList = res.data.data
          }
        })
      }
    }
  }
}
</script>
