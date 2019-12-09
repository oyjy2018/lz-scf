<style lang="less" scoped>
@import "../project.less";

</style>
<template>
  <div class="project-tab-div">
    <div class="project-tab-title-div">
      <div class="project-tab-left-icon-div"/>
      <div class="project-tab-content-div">保证金缴纳</div>
    </div>
    <Table border :columns="payColumns" :data="marginsPayList" style="margin-bottom: 20px;"></Table>
    <div class="project-tab-title-div">
      <div class="project-tab-left-icon-div"/>
      <div class="project-tab-content-div">保证金提前</div>
    </div>
    <Table border :columns="extractColumns" :data="marginsExtractList" style="margin-bottom: 20px;"></Table>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'ProjectMargins',
  data () {
    return {
      contractNo: '',
      payColumns: [
        {
          title: '保证金编号',
          key: 'marginsNo',
          align: 'center'
        },
        {
          title: '保证金金额（元）',
          key: 'payMoney',
          align: 'right',
          render: function (h, { row, column, index }) {
            return h('span', absMoney(row.payMoney))
          }
        },
        {
          title: '缴纳日期',
          key: 'payDate',
          align: 'center'
        },
        {
          title: '保证金类型',
          key: 'marginsType',
          align: 'center'
        },
        {
          title: '保证金状态',
          key: 'marginsStatus',
          align: 'center'
        }
      ],
      marginsPayList: [],
      extractColumns: [
        {
          title: '提取编号',
          key: 'extractMarginsNo',
          align: 'center'
        },
        {
          title: '提取金额（元）',
          key: 'extractMoney',
          align: 'right',
          render: function (h, { row, column, index }) {
            return h('span', absMoney(row.extractMoney))
          }
        },
        {
          title: '提取日期',
          key: 'extractDate',
          align: 'center'
        },
        {
          title: '对应缴纳保证金编号',
          key: 'marginsNo',
          align: 'center'
        }
      ],
      marginsExtractList: []
    }
  },
  methods: {
    ...mapActions([
      'findMarginsList'
    ]),
    getData (contractNo) {
      this.contractNo = contractNo
      let data = {
        contractNo: this.contractNo
      }
      this.findMarginsList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.marginsPayList = res.data.data.marginsPayList
          this.marginsExtractList = res.data.data.marginsExtractList
        }
      })
    }
  }
}
</script>
