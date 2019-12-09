<style lang="less" scoped>
@import "../project.less";

</style>

<template>
  <div class="project-tab-div">
    <div class="project-tab-title-div">
      <div class="project-tab-left-icon-div"/>
      <div class="project-tab-content-div">押金缴纳</div>
    </div>
    <Table border :columns="payColumns" :data="payList" style="margin-bottom: 20px;"></Table>
    <div class="project-tab-title-div">
      <div class="project-tab-left-icon-div"/>
      <div class="project-tab-content-div">押金提取</div>
    </div>
    <Table border :columns="extractColumns" :data="extractList" style="margin-bottom: 20px;"></Table>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'ProjectCashPledge',
  data () {
    return {
      contractNo: '',
      payColumns: [
        {
          title: '押金编号',
          key: 'pledgeCashNo',
          align: 'center'
        },
        {
          title: '押金金额（元）',
          key: 'payMoney',
          align: 'center',
          render: function (h, { row, column, index }) {
            return h('span', absMoney(row.payMoney))
          }

        },
        {
          title: '缴纳日期',
          key: 'payDateStr',
          align: 'center'
        },
        {
          title: '押金类型',
          key: 'pledgeCashType',
          align: 'center'
        },
        {
          title: '押金状态',
          key: 'pledgeCashStatus',
          align: 'center'
        }
      ],
      payList: [],
      extractColumns: [
        {
          title: '提取编号',
          key: 'extractNo',
          align: 'center'
        },
        {
          title: '提取金额（元）',
          key: 'extractMoney',
          align: 'center',
          render: function (h, { row, column, index }) {
            return h('span', absMoney(row.extractMoney))
          }

        },
        {
          title: '提取日期',
          key: 'extractDateStr',
          align: 'center'
        },
        {
          title: '对应缴纳押金编号',
          key: 'pledgeCashNo',
          align: 'center'
        }
      ],
      extractList: []
    }
  },
  methods: {
    ...mapActions([
      'getPledgeCashPayList',
      'getPledgeCashExtractList'
    ]),
    getData (contractNo) {
      this.contractNo = contractNo
      let data = {
        contractNo: this.contractNo
      }

      this.getPledgeCashPayList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.payList = res.data.data
        }
      })

      this.getPledgeCashExtractList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.extractList = res.data.data
        }
      })
    }
  }
}
</script>
