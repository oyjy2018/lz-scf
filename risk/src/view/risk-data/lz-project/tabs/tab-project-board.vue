<style lang="less" scoped>
@import "../project.less";
  .board-card {
    width: 290px;
    height: 140px;
    border-radius: 8px;
    float: left;
    margin-right: 30px;
    position: relative;
  }
  .board-card > .card-left {
    width: 6px;
    height: 100%;
    float: left;
    border-top-left-radius: 8px;
    border-bottom-left-radius: 8px;
  }
  .board-card > .card-name {
    height:26px;
    font-size:20px;
    color:rgba(85,85,85,1);
    line-height:26px;
    left: 15px;
    top: 78px;
    position: absolute;
  }
  .board-card > .card-icon {
    width:96px;
    height:96px;
    position: absolute;
    top: 22px;
    right: 0px;
  }
  .board-card > .card-number {
    height:35px;
    font-size:26px;
    font-weight:bold;
    color:rgba(85,85,85,1);
    line-height:35px;
    left: 15px;
    top: 20px;
    position: absolute;
  }
</style>
<template>
  <div class="project-tab-div" >
    <div style="width: 1680px; height: 140px;margin-top: 40px">
      <div class="board-card" style="border: 1px solid rgba(86,165,245,1)">
        <div class="card-left" style="background-color: rgba(86,165,245,1)"/>
        <div class="card-number">{{cardData.balance}}</div>
        <div class="card-name">余额</div>
        <img class="card-icon" src="@/assets/images/lz-project/balance.png">
      </div>
      <div class="board-card" style="border: 1px solid rgba(64,204,150,1)">
        <div class="card-left" style="background-color: rgba(64,204,150,1)"/>
        <div class="card-number">{{cardData.gather}}</div>
        <div class="card-name">项目回款</div>
        <img class="card-icon" src="@/assets/images/lz-project/gather.png">
      </div>
      <div class="board-card" style="border: 1px solid rgba(213,206,113,1)">
        <div class="card-left" style="background-color: rgba(213,206,113,1)"/>
        <div class="card-number">{{cardData.pay}}</div>
        <div class="card-name">项目付款</div>
        <img class="card-icon" src="@/assets/images/lz-project/pay.png">
      </div>
      <div class="board-card" style="border: 1px solid rgba(246,115,115,1)">
        <div class="card-left" style="background-color: rgba(246,115,115,1)"/>
        <div class="card-number">{{cardData.marginsAndPledge}}</div>
        <div class="card-name">保证金/押金</div>
        <img class="card-icon" src="@/assets/images/lz-project/margin.png">
      </div>
      <div class="board-card" style="border: 1px solid rgba(247,175,101,1)">
        <div class="card-left" style="background-color: rgba(247,175,101,1)"/>
        <div class="card-number">{{cardData.loan}}</div>
        <div class="card-name">项目借款</div>
        <img class="card-icon" src="@/assets/images/lz-project/loan.png">
      </div>
    </div>
    <div class="project-tab-title-div" style="margin-top: 22px">
      <div class="project-tab-left-icon-div"/>
      <div class="project-tab-content-div">项目经营情况</div>
    </div>
    <div style="margin-bottom: 20px;position: relative;padding-top: 20px;padding-left: 20px">
      <DatePicker type="date"
                  show-week-numbers
                  :value="beginDate"
                  @on-change="changeBeginDate"
                  :editable="false"
                  placeholder="开始日期"
                  style="width: 150px;height: 32px;position: absolute;"></DatePicker>
      <div style="position: absolute;left: 178px;height: 32px;line-height: 32px">至</div>
      <DatePicker type="date"
                  show-week-numbers
                  :value="endDate"
                  @on-change="changeEndDate"
                  :editable="false"
                  placeholder="结束日期"
                  style="width: 150px;height: 32px;position: absolute;left: 200px"></DatePicker>
      <Button type="primary"
              @click="handleGetBoardReport"
              style="position: absolute;left: 360px">搜索</Button>
    </div>
    <div>
      <div style="height: 592px;margin-top: 40px" id="myEcharts">
      </div>
    </div>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

// 引入基础模板
const echarts = require('echarts')
// 引入折线图
require('echarts/lib/chart/line')
// 引入提示框和标题组件
require('echarts/lib/component/tooltip')
require('echarts/lib/component/title')

export default {
  name: 'ProjectBoard',
  data () {
    return {
      cardData: { // 卡片数据
        balance: 0,
        gather: 0,
        loan: 0,
        marginsAndPledge: 0,
        pay: 0
      },
      option: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: ['余额', '项目回款', '项目付款', '保证金/押金', '项目借款']
        },
        grid: {
          left: '20',
          bottom: '3%',
          width: '98%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: ['0%', '0.1%'],
            data: []
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '余额',
            type: 'line',
            data: []
          },
          {
            name: '项目回款',
            type: 'line',
            data: []
          },
          {
            name: '项目付款',
            type: 'line',
            data: []
          },
          {
            name: '保证金/押金',
            type: 'line',
            data: []
          },
          {
            name: '项目借款',
            type: 'line',
            data: []
          }
        ]
      },
      beginDate: '',
      endDate: '',
      contractNo: ''
    }
  },
  mounted () {
    // 这里是为了能根据自动产生的宽度来设置图形报表的宽度，Echarts的容器必须设定宽度才能用，且不能使用百分比
    // let myEchartsDiv = document.getElementById('myEcharts')
    // myEchartsDiv.style.width = (myEchartsDiv.parentElement.offsetWidth - 40) + 'px'
    this.beginDate = this.getDefaultBeginDate()
    this.endDate = this.getDefaultEndDate()
  },
  methods: {
    ...mapActions([
      'getBoardCard',
      'getBoardReport'
    ]),
    // 初始化报表
    initEcharts () {
      let myEchartsDiv = document.getElementById('myEcharts')
      let myEcharts = echarts.init(myEchartsDiv)
      myEcharts.setOption(this.option)
    },
    // 获取页面数据
    getData (contractNo) {
      this.contractNo = contractNo
      this.handleGetBoardCard(contractNo)
      this.handleGetBoardReport()
    },
    // 获取折线图报表数据
    handleGetBoardReport () {
      if (this.beginDate === '') {
        this.$Modal.error({
          title: '提示',
          content: '开始日期不能为空'
        })
        return
      }
      if (this.endDate === '') {
        this.$Modal.error({
          title: '提示',
          content: '结束日期不能为空'
        })
        return
      }
      if (this.beginDate > this.endDate) {
        this.$Modal.error({
          title: '提示',
          content: '结束日期必须大于等于开始日期'
        })
        return
      }
      let data = {
        contractNo: this.contractNo,
        beginDate: this.beginDate,
        endDate: this.endDate
      }
      this.getBoardReport(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.option.xAxis[0].data = res.data.data.days
          this.option.series[0].data = res.data.data.balanceArray
          this.option.series[1].data = res.data.data.gatherArray
          this.option.series[2].data = res.data.data.payArray
          this.option.series[3].data = res.data.data.marginsAndPledgeArray
          this.option.series[4].data = res.data.data.loanArray
          this.initEcharts()
        }
      })
    },
    // 获取卡片数据
    handleGetBoardCard (contractNo) {
      let data = {
        contractNo: contractNo
      }
      this.getBoardCard(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.cardData.balance = absMoney(res.data.data.balance)
          this.cardData.gather = absMoney(res.data.data.gather)
          this.cardData.pay = absMoney(res.data.data.pay)
          this.cardData.loan = absMoney(res.data.data.loan)
          this.cardData.marginsAndPledge = absMoney(res.data.data.marginsAndPledge)
        }
      })
    },
    // 改变开始时间 （因为v-model返回的date类型）
    changeBeginDate (date) {
      this.beginDate = date
    },
    // 改变结束时间 （因为v-model返回的date类型）
    changeEndDate (date) {
      this.endDate = date
    },
    /**
     * 获取默认开始日期（当前日期）
     * 格式YYYY-MM-DD
     */
    getDefaultBeginDate () {
      let date = new Date()
      date.setDate(date.getDate() - 365)
      let dateStr = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate()
      return dateStr
    },
    /**
     * 获取默认结束时间（当前日期）
     * 格式YYYY-MM-DD
     */
    getDefaultEndDate () {
      let date = new Date()
      let str = '-'
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let strDate = date.getDate()
      let dateStr = year + str + month + str + strDate
      return dateStr
    }
  }
}
</script>
