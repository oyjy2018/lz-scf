<style lang="less" scoped>
@import "../project.less";

</style>

<template>
  <div class="project-tab-div">
    <div class="project-tab-title-div">
      <div class="project-tab-left-icon-div"/>
      <div class="project-tab-content-div">成本费用明细</div>
    </div>
    <Table border :columns="costColumns" :data="costList" style="margin-bottom: 20px;"></Table>
    <Table border :columns="profitColumns" :data="profitList" style="margin-bottom: 20px;"></Table>
    <div class="project-tab-title-div">
      <div class="project-tab-left-icon-div"/>
      <div class="project-tab-content-div">成本利润合计</div>
    </div>
    <Table border :columns="costProfitColumns" :data="costProfitTotalList" style="margin-bottom: 20px;"></Table>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'
const centerRightDIV = function (h, value, width) {
  return h('div', {
    style: {
      textAlign: 'right',
      width: (width || '50%'),
      margin: 'auto'
    }
  }, value)
}

export default {
  name: 'ProjectProfitB',
  data () {
    return {
      costColumns: [
        {
          title: '项目经营成本',
          key: 'feeType',
          align: 'center'
        },
        {
          title: '控制指标',
          key: 'guidepostsStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, row.guidepostsStr)
          }
        },
        {
          title: '预算比例',
          key: 'budgetRateStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, row.budgetRateStr, '25%')
          }
        },
        {
          title: '预算总额（元）',
          key: 'budgetMoneyStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.budgetMoneyStr))
          }
        },
        {
          title: '已使用金额（元）',
          key: 'useMoneyStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.useMoneyStr))
          }
        },
        {
          title: '冻结金额（元）',
          key: 'freezeMoneyStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.freezeMoneyStr))
          }
        },
        {
          title: '预算余额（元）',
          key: 'budgetBalanceStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.budgetBalanceStr))
          }
        }
      ],
      costList: [],
      profitColumns: [
        {
          title: '项目经营利润',
          key: 'feeType',
          align: 'center'
        },
        {
          title: '控制指标',
          key: 'guidepostsStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, row.guidepostsStr)
          }
        },
        {
          title: '预算比例',
          key: 'budgetRateStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, row.budgetRateStr, '25%')
          }
        },
        {
          title: '预算总额（元）',
          key: 'budgetMoneyStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.budgetMoneyStr))
          }
        },
        {
          title: '已使用金额（元）',
          key: 'useMoneyStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.useMoneyStr))
          }
        },
        {
          title: '冻结金额（元）',
          key: 'freezeMoneyStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.freezeMoneyStr))
          }
        },
        {
          title: '预算余额（元）',
          key: 'budgetBalanceStr',
          align: 'center',
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.budgetBalanceStr))
          }
        }
      ],
      profitList: [],
      costProfitColumns: [
        {
          title: '项目成本利润合计',
          key: 'totalType',
          align: 'center',
          width: 250
        },
        {
          title: '总金额(元)',
          key: 'totalMoney',
          align: 'center',
          width: 250,
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, absMoney(row.totalMoney))
          }
        },
        {
          title: '比例',
          key: 'totalRate',
          align: 'center',
          width: 250,
          render: function (h, { row, column, index }) {
            return centerRightDIV(h, row.totalRate, '25%')
          }
        },
        {
          title: '说明',
          key: 'explain',
          align: 'center',
          render: function (h, { row, column, index }) {
            return h('div', {
              style: {
                textAlign: 'left'
              }
            }, row.explain)
          }
        }
      ],
      costProfitTotalList: []
    }
  },
  methods: {
    ...mapActions([
      'findCostDetails'
    ]),
    getData (contractNo) {
      let data = {
        contractNo: contractNo
      }
      this.findCostDetails(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.costList = res.data.data.costList
          this.profitList = res.data.data.profitList
          this.costProfitTotalList = res.data.data.costProfitTotalList
        }
      })
    }
  }
}
</script>
