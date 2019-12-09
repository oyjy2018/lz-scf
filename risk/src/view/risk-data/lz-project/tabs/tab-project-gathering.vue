<style lang="less" scoped>
@import "../project.less";

</style>
<template>
  <div class="project-tab-div">
    <Table border :columns="columns" :data="data"></Table>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'ProjectGathering',
  data () {
    return {
      columns: [
        {
          title: '收款编号',
          key: 'gatheringNo',
          align: 'center'
        },
        {
          title: '收款日期',
          key: 'gatheringDate',
          align: 'center'
        },
        {
          title: '收款类型',
          key: 'gatheringType',
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
          key: 'gatheringMoney',
          align: 'center',
          render: (h, params) => {
            return h('div', {
              style: {
                textAlign: 'right'
              }
            },
            absMoney(params.row.gatheringMoney))
          }
        }
      ],
      data: []
    }
  },
  methods: {
    ...mapActions([
      'gatheringList'
    ]),
    getData (contractNo) {
      let data = {
        contractNo: contractNo
      }
      this.gatheringList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.data = res.data.data
        }
      })
    }
  }
}
</script>
