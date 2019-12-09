<style lang="less" scoped>
@import "../project.less";
.tab-div-active {
  display: block;
}

.tab-div-no-active {
  display: none;
}

.ivu-radio-group-button > .ivu-radio-wrapper {
  background: #FFFFFF;
  color: #56A5F5;
  border:1px solid rgba(86,165,245,1);
}

.ivu-radio-group-button > .ivu-radio-wrapper:first-child {
  border-left: 1px solid #56a5f5;
}

.ivu-radio-group-button > .ivu-radio-wrapper-checked {
  background: #56A5F5;
  color: #FFFFFF;
}

.ivu-radio-group-button > .ivu-radio-wrapper-checked:hover {
  background: #56A5F5;
  color: #FFFFFF;
}

.child-tab {
  width: 114px;
  text-align: center
}
.tabs-span {
  margin-left: 4px;
  margin-right: 4px;
}
</style>
<template>
  <div class="project-tab-div">
    <div style="margin-bottom: 20px;">
      <RadioGroup v-model="childTab" type="button">
          <Radio label="1" class="child-tab">项目资产</Radio>
          <Radio label="2" class="child-tab">项目负债+利润</Radio>
      </RadioGroup>
    </div>
    <div :class="{'tab-div-active': (childTab === '1'), 'tab-div-no-active': (childTab === '2')}">
      <DatePicker :editable="commFalse" transfer :clearable="false"
                  v-model="assetStartDate"
                  type="month"
                  style="width: 150px;margin-bottom: 20px;"
                  @on-change="(date, dateType) => dateChange(date, dateType, 'showAssetEndDate', 'assetEndDate')"
                  ></DatePicker>
      <span class="tabs-span">至</span>
      <DatePicker :editable="commFalse" transfer :clearable="false"
                  v-model="assetEndDate"
                  type="month"
                  :open="showAssetEndDate"
                  @on-change="closeDatePicker('showAssetEndDate')"
                  :options="assetEndOptions"
                  style="width: 150px;margin-bottom: 20px;margin-right: 20px;"></DatePicker>
      <Button type="primary" icon="ios-search" @click="findProjectAssetData()">查询</Button>
      <Table border :columns="assetColumns" :data="assetData" style="margin-bottom: 20px;"></Table>
      <div style="height: 592px" id="assetEcharts"></div>
    </div>
    <div :class="{'tab-div-active': (childTab === '2'), 'tab-div-no-active': (childTab === '1')}">
      <DatePicker :editable="commFalse" transfer :clearable="false"
                  v-model="liabilitiesStartDate"
                  type="month"
                  style="width: 150px;margin-bottom: 20px;"
                  @on-change="(date, dateType) => dateChange(date, dateType, 'showLiabilitiesEndDate', 'liabilitiesEndDate')"
                  ></DatePicker>
      <span class="tabs-span">至</span>
      <DatePicker :editable="commFalse" transfer :clearable="false"
                  v-model="liabilitiesEndDate"
                  type="month"
                  :open="showLiabilitiesEndDate"
                  @on-change="closeDatePicker('showLiabilitiesEndDate')"
                  :options="liabilitiesEndOptions"
                  style="width: 150px;margin-bottom: 20px;margin-right: 20px;"></DatePicker>
      <Button type="primary" icon="ios-search" @click="findLiabilitiesData()">查询</Button>
      <Table border :columns="liabilitiesColumns" :data="liabilitiesData" style="margin-bottom: 20px;"></Table>
      <div style="height: 592px" id="liabilitiesEcharts"></div>
    </div>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney, disposeListNullVal } from '@/libs/tools'
import DateUtil from '@/libs/data-util'

// 引入基础模板
const echarts = require('echarts')
// 引入折线图
require('echarts/lib/chart/line')
// 引入提示框和标题组件
require('echarts/lib/component/tooltip')
require('echarts/lib/component/title')

export default {
  name: 'ProjectProfitA',
  data () {
    return {
      commFalse: false,
      childTab: '1',
      contractNo: '',
      showAssetEndDate: false,
      assetEndOptions: {
        disabledDate: date => this.assetDisabledDate(date, 'end', this.assetStartDate, this.assetEndDate)
      },
      showLiabilitiesEndDate: false,
      liabilitiesEndOptions: {
        disabledDate: date => this.assetDisabledDate(date, 'end', this.liabilitiesStartDate, this.liabilitiesEndDate)
      },
      assetColumns: [
        {
          title: '科目/时间',
          key: 'subject',
          align: 'center'
        }
      ],
      assetData: [
        {
          subject: '项目账户余额',
          list: []
        },
        {
          subject: '应收票据',
          list: []
        },
        {
          subject: '开票余额',
          list: []
        },
        {
          subject: '材料预付款',
          list: []
        },
        {
          subject: '材料存货',
          list: []
        },
        {
          subject: '项目回款',
          list: []
        }
      ],
      option: {
        title: {},
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {},
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: []
      },
      assetStartDate: '',
      assetEndDate: '',
      assetLegend: ['项目账户余额', '应收票据', '开票余额', '材料预付款', '材料存货', '项目回款'],
      assetDays: [],
      projectBalance: [],
      billReceivable: [],
      invoiceBalance: [],
      materialsAdvance: [],
      materialInventory: [],
      totalGather: [],
      liabilitiesColumns: [
        {
          title: '科目/时间',
          key: 'subject',
          align: 'center'
        }
      ],
      liabilitiesData: [
        {
          subject: '应付借款利息',
          list: []
        },
        {
          subject: '工程贷剩余本金',
          list: []
        },
        {
          subject: '商票用信剩余本金',
          list: []
        },
        {
          subject: '经营借款剩余本金',
          list: []
        },
        {
          subject: '应付材料款余额',
          list: []
        },
        {
          subject: '已支付管理费、税金',
          list: []
        },
        {
          subject: '已支付人工费',
          list: []
        },
        {
          subject: '已支付材料费',
          list: []
        },
        {
          subject: '已支付项目费用',
          list: []
        },
        {
          subject: '项目经营剩余',
          list: []
        }
      ],
      liabilitiesStartDate: '',
      liabilitiesEndDate: '',
      liabilitiesLegend: ['应付借款利息', '工程贷剩余本金', '商票用信剩余本金', '经营借款剩余本金', '应付材料款余额', '已支付管理费、税金', '已支付人工费',
        '已支付材料费',
        '已支付项目费用', '项目经营剩余'
      ],
      liabilitiesDays: [],
      repayInterest: [],
      projectLoanRepayPrincipal: [],
      billRepayPrincipal: [],
      dealInLoanRepayPrincipal: [],
      repayMaterialsFee: [],
      pdPaidManageFee: [],
      pdPaidLaborFee: [],
      pdPaidMaterialsFee: [],
      pdPaidProjectFee: [],
      projectManageSurplus: []
    }
  },
  mounted () {
    // 这里是为了能根据自动产生的宽度来设置图形报表的宽度，Echarts的容器必须设定宽度才能用，且不能使用百分比
    // let assetEchartsDiv = document.getElementById('assetEcharts')
    // assetEchartsDiv.style.width = (assetEchartsDiv.parentElement.parentElement.offsetWidth - 40) + 'px'

    // let liabilitiesEchartsDiv = document.getElementById('liabilitiesEcharts')
    // liabilitiesEchartsDiv.style.width = (liabilitiesEchartsDiv.parentElement.parentElement.offsetWidth - 40) + 'px'

    let recentlyMonth = DateUtil.getRecentlyMonth(12)
    this.assetStartDate = recentlyMonth[recentlyMonth.length - 1]
    this.assetEndDate = recentlyMonth[0]

    this.liabilitiesStartDate = recentlyMonth[recentlyMonth.length - 1]
    this.liabilitiesEndDate = recentlyMonth[0]
    console.log(document.body.offsetWidth)
  },
  methods: {
    ...mapActions([
      'findProjectAsset',
      'findLiabilities'
    ]),
    initEcharts: function () {
      let assetEchartsDiv = document.getElementById('assetEcharts')
      let assetEcharts = echarts.init(assetEchartsDiv)
      let option = JSON.parse(JSON.stringify(this.option))
      option.title.text = '项目经营情况'
      option.legend.data = this.assetLegend
      option.xAxis = [
        {
          type: 'category',
          boundaryGap: false,
          data: this.assetDays
        }
      ]
      option.series = [
        {
          name: '项目账户余额',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          connectNulls: true,
          data: disposeListNullVal(this.projectBalance)
        },
        {
          name: '应收票据',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.billReceivable)
        },
        {
          name: '开票余额',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.invoiceBalance)
        },
        {
          name: '材料预付款',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.materialsAdvance)
        },
        {
          name: '材料存货',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.materialInventory)
        },
        {
          name: '项目回款',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.totalGather)
        }
      ]
      assetEcharts.setOption(option)
    },
    liabilitiesEcharts () {
      let liabilitiesEchartsDiv = document.getElementById('liabilitiesEcharts')
      let liabilitiesEcharts = echarts.init(liabilitiesEchartsDiv)
      let option = JSON.parse(JSON.stringify(this.option))
      option.title.text = '项目负债、利润'
      option.legend.data = this.liabilitiesLegend
      option.xAxis = [
        {
          type: 'category',
          boundaryGap: false,
          data: this.liabilitiesDays
        }
      ]
      option.series = [
        {
          name: '应付借款利息',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.repayInterest)
        },
        {
          name: '工程贷剩余本金',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.projectLoanRepayPrincipal)
        },
        {
          name: '商票用信剩余本金',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.billRepayPrincipal)
        },
        {
          name: '经营借款剩余本金',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.dealInLoanRepayPrincipal)
        },
        {
          name: '应付材料款余额',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.repayMaterialsFee)
        },
        {
          name: '已支付管理费、税金',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.pdPaidManageFee)
        },
        {
          name: '已支付人工费',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.pdPaidLaborFee)
        },
        {
          name: '已支付材料费',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.pdPaidMaterialsFee)
        },
        {
          name: '已支付项目费用',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.pdPaidProjectFee)
        },
        {
          name: '项目经营剩余',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          data: disposeListNullVal(this.projectManageSurplus)
        }
      ]
      liabilitiesEcharts.setOption(option)
    },
    getData (contractNo) {
      this.contractNo = contractNo
      this.findProjectAssetData()
      this.findLiabilitiesData()
    },
    findProjectAssetData () {
      let data = {
        contractNo: this.contractNo,
        startDate: DateUtil.formatDateMonth(this.assetStartDate),
        endDate: DateUtil.formatDateMonth(this.assetEndDate)
      }
      this.findProjectAsset(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.assetDays = res.data.data.days
          this.projectBalance = res.data.data.projectBalance
          this.billReceivable = res.data.data.billReceivable
          this.invoiceBalance = res.data.data.invoiceBalance
          this.materialsAdvance = res.data.data.materialsAdvance
          this.materialInventory = res.data.data.materialInventory
          this.totalGather = res.data.data.totalGather
          this.initTableData(res.data.data)
          this.initEcharts()
        }
      })
    },
    findLiabilitiesData () {
      let data = {
        contractNo: this.contractNo,
        startDate: DateUtil.formatDateMonth(this.liabilitiesStartDate),
        endDate: DateUtil.formatDateMonth(this.liabilitiesEndDate)
      }
      this.findLiabilities(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.liabilitiesDays = res.data.data.days
          this.repayInterest = res.data.data.repayInterest
          this.projectLoanRepayPrincipal = res.data.data.projectLoanRepayPrincipal
          this.billRepayPrincipal = res.data.data.billRepayPrincipal
          this.dealInLoanRepayPrincipal = res.data.data.dealInLoanRepayPrincipal
          this.repayMaterialsFee = res.data.data.repayMaterialsFee
          this.pdPaidManageFee = res.data.data.pdPaidManageFee
          this.pdPaidLaborFee = res.data.data.pdPaidLaborFee
          this.pdPaidMaterialsFee = res.data.data.pdPaidMaterialsFee
          this.pdPaidProjectFee = res.data.data.pdPaidProjectFee
          this.projectManageSurplus = res.data.data.projectManageSurplus
          this.initLiabilitiesTableData(res.data.data)
          this.liabilitiesEcharts()
        }
      })
    },
    initTableData (data) {
      let days = data.days
      this.assetColumns = [
        {
          title: '科目/时间',
          key: 'subject',
          align: 'center'
        }
      ]
      for (let day of days) {
        this.assetColumns.push(
          {
            title: day,
            key: '',
            align: 'right',
            render: function (h, { row, column, index }) {
              return h('span', absMoney(row.list[column._index - 1]))
            }
          }
        )
      }

      this.assetData[0].list = data.projectBalance
      this.assetData[1].list = data.billReceivable
      this.assetData[2].list = data.invoiceBalance
      this.assetData[3].list = data.materialsAdvance
      this.assetData[4].list = data.materialInventory
      this.assetData[5].list = data.totalGather
    },
    initLiabilitiesTableData (data) {
      let days = data.days
      this.liabilitiesColumns = [
        {
          title: '科目/时间',
          key: 'subject',
          align: 'center'
        }
      ]
      for (let day of days) {
        this.liabilitiesColumns.push(
          {
            title: day,
            key: '',
            align: 'right',
            render: function (h, { row, column, index }) {
              return h('span', absMoney(row.list[column._index - 1]))
            }
          }
        )
      }

      this.liabilitiesData[0].list = data.repayInterest
      this.liabilitiesData[1].list = data.projectLoanRepayPrincipal
      this.liabilitiesData[2].list = data.billRepayPrincipal
      this.liabilitiesData[3].list = data.dealInLoanRepayPrincipal
      this.liabilitiesData[4].list = data.repayMaterialsFee
      this.liabilitiesData[5].list = data.pdPaidManageFee
      this.liabilitiesData[6].list = data.pdPaidLaborFee
      this.liabilitiesData[7].list = data.pdPaidMaterialsFee
      this.liabilitiesData[8].list = data.pdPaidProjectFee
      this.liabilitiesData[9].list = data.projectManageSurplus
    },
    assetDisabledDate (date, target, startDate, endDate) {
      if (target === 'start') {
        return date && (DateUtil.monthsBetw(date, new Date(endDate)) > 12 || DateUtil.monthsBetw(date, new Date(endDate)) <= 0)
      } else {
        return date && (DateUtil.monthsBetw(new Date(startDate), date) > 12 || DateUtil.monthsBetw(new Date(startDate), date) <= 0)
      }
    },
    dateChange (date, dateType, showDate, endDate) {
      this[showDate] = true
      let newDate = new Date(date)
      let newEndDate = new Date(this[endDate])
      let type = DateUtil.monthsBetw(newDate, newEndDate) > 12 || DateUtil.monthsBetw(newDate, newEndDate) <= 0
      if (type) {
        let tmp = DateUtil.getDateMonth(newDate, 12)
        this[endDate] = tmp[tmp.length - 1]
      }
    },
    closeDatePicker (showDate) {
      this[showDate] = false
    }
  }
}
</script>
