<style lang="less" scoped>
@import "../project.less";

</style>
<template>
  <div class="project-tab-div">
    <Table border :columns="columns" :data="list"></Table>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'ProjectPay',
  data () {
    return {
      columns: [
        {
          title: '关联合同编号',
          key: 'relatedContractNo',
          align: 'center'
        },
        {
          title: '付款编号',
          key: 'payRegisterNo',
          align: 'center'
        },
        {
          title: '付款日期',
          key: 'payDate',
          align: 'center'
        },
        {
          title: '付款类型',
          key: 'payType',
          align: 'center'
        },
        {
          title: '付款方全称',
          key: 'payerFullName',
          align: 'center'
        },
        {
          title: '收款方全称',
          key: 'gatheringFullName',
          align: 'center'
        },
        {
          title: '收款金额（元）',
          key: 'actualPayMoney',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('div', absMoney(params.row.actualPayMoney))
          }
        }
      ],
      list: []
    }
  },
  methods: {
    ...mapActions([
      'lzProjectPayList'
    ]),
    getData (value) {
      const data = {
        contractNo: value
      }
      this.lzProjectPayList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.list = res.data.data
        }
      })
    }
  }
}
</script>
