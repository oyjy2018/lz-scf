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

export default {
  name: 'ProjectPurchaseLogistics',
  data () {
    return {
      columns: [
        {
          title: '采购合同编号',
          key: 'purchaseContractNo',
          align: 'center'
        },
        {
          title: '发货物流单号',
          key: 'shipmentsNo',
          align: 'center'
        },
        {
          title: '发货人',
          key: 'shipper',
          align: 'center'
        },
        {
          title: '发货日期',
          key: 'shipmentsDate',
          align: 'center'
        },
        {
          title: '收货人',
          key: 'consignee',
          align: 'center'
        },
        {
          title: '收货地址',
          key: 'consigneeAddress',
          align: 'center',
          width: 400
        }
      ],
      list: []
    }
  },
  methods: {
    ...mapActions([
      'getPurchaseLogistics'
    ]),
    getData (value) {
      const data = {
        contractNo: value
      }
      this.getPurchaseLogistics(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.list = res.data.data
        }
      })
    }
  }
}
</script>
