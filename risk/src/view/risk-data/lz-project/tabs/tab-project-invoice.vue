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
  name: 'ProjectInvoice',
  data () {
    return {
      columns: [
        {
          title: '发票编号',
          key: 'invoiceNo',
          align: 'center'
        },
        {
          title: '发票日期',
          key: 'invoiceDate',
          align: 'center'
        },
        {
          title: '发票类型',
          key: 'invoiceType',
          align: 'center'
        },
        {
          title: '开出/收到',
          key: 'registerType',
          align: 'center'
        },
        {
          title: '开发票公司全称',
          key: 'invoicingCompanyFullName',
          align: 'center'
        },
        {
          title: '发票金额（元）',
          key: 'invoiceMoney',
          align: 'center',
          render: (h, params) => {
            return h('div', {
              style: {
                textAlign: 'right'
              }
            },
            absMoney(params.row.invoiceMoney))
          }
        }
      ],
      list: []
    }
  },
  methods: {
    ...mapActions([
      'getInvoiceList'
    ]),
    getData (value) {
      const data = {
        contractNo: value
      }
      this.getInvoiceList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.list = res.data.data
        }
      })
    }
  }
}
</script>
