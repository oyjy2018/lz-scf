<style lang="less" scoped>
.main {
  padding: 20px;
  width: 100%;
  height: 100%;
  background: #fff;
  .title {
    width: 100%;
    height: 38px;
    line-height: 38px;
    font-size: 16px;
    border-bottom: 1px dashed #979797;
    span {
      border-left: 2px solid #2c8ef3;
      padding-left: 5px;
    }
  }
  .content {
    margin-bottom: 30px;
    display: flex;
    justify-content: space-between;
    &-left {
      flex: 1;
      margin-top: 10px;
      &-item {
        font-size: 14px;
        line-height: 34px;
      }
      &-item-flex {
        display: flex;
        justify-content: space-between;
        div {
          flex: 1;
        }
      }
      .table-title,
      .table-value {
        display: flex;
        justify-content: space-around;
        height: 44px;
        line-height: 44px;
        background: #ecf0f6;
        span {
          width: 120px;
          text-align: center;
          word-break: break-all;
        }
      }
      .table-value {
        background: #fff;
        height: auto;
      }
    }
    &-right {
      width: 300px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>

<template>
  <div class="main"
       v-if="form">
    <div class="title"><span>询价方</span></div>
    <div class="content">
      <div class="content-left">
        <div class="content-left-item content-left-item-flex">
          <div>
            <span>{{form.inquireCompanyName}}</span>
          </div>
          <div>
            <label>联系人：</label>
            <span>{{form.inquireUserName}}</span>
          </div>
          <div>
            <label>联系电话：</label>
            <span>{{form.inquireUserPhone}}</span>
          </div>
        </div>
        <div class="content-left-item content-left-item-flex">
          <div>
            <label>邮箱：</label>
            <span>{{form.inquireUserEmail}}</span>
          </div>
          <div>
            <a @click="handleViewInquireDetail">查看询价单详情
              <Icon type="ios-arrow-forward" /></a>
          </div>
          <div>
          </div>
        </div>
      </div>
    </div>

    <div class="title"><span>报价方</span></div>
    <div class="content">
      <div class="content-left">
        <div class="content-left-item content-left-item-flex">
          <div>
            <span>{{form.quotationCompanyName}}</span>
          </div>
          <div>
            <label>联系人：</label>
            <span>{{form.quotationUserName}}</span>
          </div>
          <div>
            <label>联系电话：</label>
            <span>{{form.quotationUserPhone}}</span>
          </div>
        </div>
        <div class="content-left-item">
          <label>邮箱：</label>
          <span>{{form.quotationUserEmail}}</span>
        </div>
      </div>
    </div>

    <div class="title"><span>报价信息</span></div>
    <div class="content">
      <div class="content-left">
        <div class="content-left-item">
          <label>报价时间：</label>
          <span>{{form.quotationTime}}</span>
        </div>
        <div class="content-left-item">
          <label>有效期至：</label>
          <span>{{form.maturityDate}}({{form.quotationSurplusValidDays}}天)</span>
        </div>
        <div class="content-left-item">
          <label>报价状态：</label>
          <span>{{quotationStatus[form.quotationStatus - 1]}}</span>
        </div>
      </div>
    </div>
    <div class="content">
      <div class="content-left"
           style="border:1px solid #DDDDDD">
        <div class="table-title">
          <span style="flex: 1">承兑人</span><span style="flex: 1">票据号码</span><span>票面金额(元)</span><span>到期日期</span>
          <span>剩余天数(报价时)</span><span>背书次数</span><span style="flex: 1">票据瑕疵</span>
          <span>报价(元)</span><span>年化率</span><span>每10万扣款(元)</span>
        </div>
        <div class="table-value">
          <span style="flex: 1">{{form.accepterName}}</span><span style="flex: 1">{{form.billNo}}</span><span>{{absMoney(form.billAmt)}}</span><span>{{form.maturityDate}}</span>
          <span>{{form.quotationSurplusValidDays}}</span><span>{{form.endorsedCount}}</span><span style="flex: 1">{{form.flaws}}</span>
          <span>{{absMoney(form.amount)}}</span><span>{{form.annualRate}}</span><span>{{absMoney(form.discountFee)}}</span>
        </div>
      </div>
    </div>

  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

// quotationStatus报价状态(1:待接受报价;2:接受报价;3:不接受报价;4:已撤回)
const quotationStatus = ['待接受报价', '接受报价', '不接受报价', '已撤回']

export default {
  name: 'sellquotationdetail',
  data () {
    return {
      absMoney: absMoney,
      quotationStatus: quotationStatus,
      form: null
    }
  },
  mounted () {
    const { quotationId } = this.$route.query
    if (quotationId) {
      const data = {
        quotationId
      }
      this.getQuotationDetail(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.form = res.data.data
        }
      })
    }
  },
  methods: {
    ...mapActions([
      'getQuotationDetail'
    ]),
    handleViewInquireDetail () {
      const route = {
        name: 'operation-inquiredetail',
        query: {
          id: this.form.inquireId
        }
      }
      this.$router.push(route)
    }
  }
}
</script>
