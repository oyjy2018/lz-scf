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
  name: 'ProjectInsurance',
  data () {
    return {
      columns: [
        {
          title: '保单编号',
          key: 'insuranceNo',
          align: 'center'
        },
        {
          title: '保险到期日期',
          key: 'insuranceExpireDate',
          align: 'center'
        },
        {
          title: '保险类型',
          key: 'insuranceType',
          align: 'center'
        },
        {
          title: '团体险人数',
          key: 'groupsCount',
          align: 'center'
        },
        {
          title: '平均每人保额（元）',
          key: 'perCapitaCoverage',
          align: 'center',
          render: (h, params) => {
            return h('div', {
              style: {
                textAlign: 'right'
              }
            },
            absMoney(params.row.perCapitaCoverage))
          }
        },
        {
          title: '总保额（元）',
          key: 'totalCoverage',
          align: 'center',
          render: (h, params) => {
            return h('div', {
              style: {
                textAlign: 'right'
              }
            },
            absMoney(params.row.totalCoverage))
          }
        }
      ],
      list: []
    }
  },
  methods: {
    ...mapActions([
      'getInsuranceList'
    ]),
    getData (value) {
      const data = {
        contractNo: value
      }
      this.getInsuranceList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.list = res.data.data
        }
      })
    }
  }
}
</script>
