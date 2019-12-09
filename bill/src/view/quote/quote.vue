<style lang="less" scoped>
.quote {
  &-step {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 48px;
    &-label {
      font-size: 14px;
      color: #8791a0;
      line-height: 16px;
      text-align: center;
    }
    .active {
      color: #2c8ef3;
    }
    img {
      margin-right: 3px;
      width: 32px;
      height: 32px;
    }
    .jt {
      margin-left: 10px;
      margin-right: 8px;
      width: 16px;
      height: 16px;
    }
  }
  &-form {
    display: flex;
    margin-top: 50px;
    margin-bottom: 20px;
    height: 300px;
    &-img {
      margin-left: 21px;
      margin-right: 40px;
      width: 540px;
      height: 300px;
      border-radius: 4px;
      background: #8791a0;
      overflow: hidden;
      img {
        width: 540px;
        height: 300px;
      }
    }
    &-inputs {
      flex: 1;
      &-item {
        display: flex;
        font-size: 14px;
        line-height: 36px;
        label {
          width: 85px;
          text-align: center;
          margin-right: 11px;
          border-right: 1px solid #ccc;
          padding: 3.2px 11px;
        }
        span {
          color: #000;
        }
        .price {
          color: #f67373;
        }
      }
    }
    &-ewm {
      margin-right: 20px;
      width: 140px;
    }
  }
  &-bottom {
    margin: 20px 0 70px 0;
    text-align: center;
    &-checkbox {
      margin-bottom: 16px;
    }
    button {
      width: 116px;
      height: 38px;
      font-size: 14px;
    }
  }
}
.close {
  margin: 8px;
  color: #000;
}
.quote-form-inputs {
  border: 1px solid #ccc;
}
</style>

<template>
  <Modal v-model="modal"
         width="1280px"
         draggable
         scrollable
         title="报价">
    <Icon class="close"
          slot="close"
          type="md-close"
          size="20"
          @click="handleClose" />
    <div class="quote"
         v-if="form">
      <div class="quote-step">
        <img src="../../assets/images/front/gc2.png">
        <div class="quote-step-label active">买家报价</div>
        <img class="jt"
             src="../../assets/images/front/jt.png">
        <img src="../../assets/images/front/gc.png">
        <div class="quote-step-label">卖家接受报价</div>
        <img class="jt"
             src="../../assets/images/front/jt.png">
        <img src="../../assets/images/front/qy.png">
        <div class="quote-step-label">双方签署合同</div>
        <img class="jt"
             src="../../assets/images/front/jt.png">
        <img src="../../assets/images/front/fk.png">
        <div class="quote-step-label"
             style="margin-top: 15px;">买家付款<br>（京东钱包）</div>
        <img class="jt"
             src="../../assets/images/front/jt.png">
        <img src="../../assets/images/front/bs.png">
        <div class="quote-step-label">卖家背书</div>
        <img class="jt"
             src="../../assets/images/front/jt.png">
        <img src="../../assets/images/front/wc.png">
        <div class="quote-step-label"
             style="margin-top: 15px;">票款到达卖家<br>银行账户</div>
      </div>
      <div class="quote-form">
        <div class="quote-form-img">
          <img :src="form.ticketFrontUrl"
               alt="商票">
        </div>

        <div class="quote-form-inputs">
          <!-- <Table border
                 :columns="columns1"
                 :data="form"></Table> -->
          <div class="quote-form-inputs-item"
               style="border-bottom: 1px solid #ccc;">
            <label>承兑人</label>
            <span style="line-height: 42px;">{{form.accepterName}}</span>
          </div>
          <div class="quote-form-inputs-item"
               style="border-bottom: 1px solid #ccc;">
            <label>票面金额</label>
            <span style="line-height: 42px;">{{form.billAmt}} 元</span>
          </div>
          <div class="quote-form-inputs-item"
               style="border-bottom: 1px solid #ccc;">
            <label>到期日期</label>
            <span style="line-height: 42px;">{{form.maturityDate}}（剩余{{form.surplusValidDays}}天）</span>
          </div>
          <div class="quote-form-inputs-item"
               style="border-bottom: 1px solid #ccc;">
            <label>背书次数</label>
            <span style="line-height: 42px;">{{form.endorsedCount}}次</span>
          </div>
          <div class="quote-form-inputs-item"
               style="border-bottom: 1px solid #ccc;">
            <label>票据瑕疵</label>
            <span style="line-height: 42px;">{{form.flaws}}</span>
          </div>
          <!-- <div class="quote-form-inputs-item">
            <label>买家报价</label>
            <span class="price">¥{{form.accepterName}}</span>
          </div> -->
          <div class="quote-form-inputs-item">
            <label></label>
            <span style="width: 145px; padding: 2px;">年化率</span>
            <span style="width: 147px;">每10万扣款</span>
            <span style="width: 110px;">成交金额</span>
          </div>
          <div class="quote-form-inputs-item">
            <label style="">我的报价<span class="price">* </span></label>
            <Input v-model="annualRate"
                   @on-change="blurAnnualRate"
                   style="width: 120px; margin: 0 5px 0 0px" />%
            <Input v-model="discountFee"
                   @on-change="blurDiscountFee"
                   style="width: 120px; margin: 0 5px 0 8px" />元
            <Input v-model="amount"
                   @on-change="blurAmount"
                   style="width: 120px; margin: 0 5px 0 8px" />元
          </div>
        </div>
        <!-- <div class="quote-form-ewm"></div> -->
      </div>
    </div>
    <div class="quote-bottom"
         slot="footer">
      <div class="quote-bottom-checkbox">
        <Checkbox v-model="hasSignProtocols">同意签署</Checkbox>
        <router-link to="/rulestatement"
                     target="_blank">《领筑数科票据交易平台信息撮合规则》</router-link>
        <!-- <a href="#"
           target="_blank">《票据收益权转让协议》</a> -->
      </div>
      <div class="quote-bottom-buttons">
        <Button @click="handleClose">取消</Button>
        <Button type="primary"
                style="margin-left: 40px;"
                @click="handleOk">确定报价</Button>
      </div>
    </div>
  </Modal>
</template>
<script>
import { mapActions } from 'vuex'
import { fomatFloat } from '@/libs/tools'

export default {
  name: 'quote',
  props: {
    modal: {
      type: Boolean,
      default: false
    },
    id: {
      type: Number,
      default: -1
    }
  },
  data () {
    return {
      value: '',
      hasSignProtocols: false,
      annualRate: '',
      discountFee: '',
      amount: '',
      form: null,
      columns1: [

      ],
      data1: [
        {
          name: 'John Brown',
          age: 18,
          address: 'New York No. 1 Lake Park',
          date: '2016-10-03'
        },
        {
          name: 'Jim Green',
          age: 24,
          address: 'London No. 1 Lake Park',
          date: '2016-10-01'
        },
        {
          name: 'Joe Black',
          age: 30,
          address: 'Sydney No. 1 Lake Park',
          date: '2016-10-02'
        },
        {
          name: 'Jon Snow',
          age: 26,
          address: 'Ottawa No. 2 Lake Park',
          date: '2016-10-04'
        }
      ]
    }
  },
  methods: {
    ...mapActions([
      'getInquireDetail',
      'quoteTicket'
    ]),
    getDetail (id) {
      if (id) {
        const data = {
          id
        }
        this.getInquireDetail(data).then(res => {
          if (res && res.status === 200 && res.data && res.data.code === 10000) {
            this.form = res.data.data
          }
        })
      }
    },
    // 年化率
    blurAnnualRate (event) {
      this.annualRate = this.annualRate.trim()
      setTimeout(() => {
        if (event.target.value > -1 && event.target.value < 100) {
          // 总扣款 =( 票面金额 * 年化率 / 360 ) *（ 票据到期日 - 今天 ）=【( 票面金额 * 年化率 ) *（ 票据到期日 - 今天 ）】/ 360
          const totall = ((this.form.billAmt * event.target.value / 100) * this.form.surplusValidDays) / 360
          // 每10万扣款 = 总扣款 / ( 票面金额/100000)   ，结果保留4位小数，四舍五入
          this.discountFee = event.target.value === '' ? '' : fomatFloat((totall / (this.form.billAmt / 100000)), 4)
          // 成交金额= 票面金额  - 总扣款 = 票面金额 -  ( 票面金额 * 年化率 / 360 ) *（ 票据到期日 - 今天 ），结果保留2位小数 ， 四舍五入
          this.amount = event.target.value === '' ? '' : fomatFloat((this.form.billAmt - totall), 4)
        } else {
          this.discountFee = ''
          this.amount = ''
        }
      }, 500)
    },
    // 每十万扣款
    blurDiscountFee (event) {
      this.discountFee = this.discountFee.trim()
      setTimeout(() => {
        if (event.target.value !== '') {
          // 总扣款 = 每10万扣款 * ( 票面金额/100000)  ，结果保留2位小数，四舍五入
          const totall = this.form.billAmt * event.target.value / 100000
          // 成交金额 = 票面金额 - 总扣款
          this.amount = event.target.value === '' ? '' : fomatFloat((this.form.billAmt - totall), 4)
          // 年化率 = ((总扣款 / 票面金额) / 剩余天数)*360 ， 结果保留10位小数
          this.annualRate = fomatFloat((((totall / this.form.billAmt) / this.form.surplusValidDays) * 360) * 100, 10)
        } else {
          this.amount = ''
          this.annualRate = ''
        }
      }, 500)
    },
    // 成交价格
    blurAmount (event) {
      this.amount = this.amount.trim()
      setTimeout(() => {
        if (event.target.value !== '') {
          // 总扣款 = 票面金额 - 成交金额
          const totall = this.form.billAmt - event.target.value
          // 年化率 = ((总扣款 / 票面金额) / 剩余天数)*360 ， 结果保留10位小数
          this.annualRate = fomatFloat((((totall / this.form.billAmt) / this.form.surplusValidDays) * 360) * 100, 10)
          // 每10万扣款 = 总扣款 / ( 票面金额/100000)   ，结果保留4位小数，四舍五入
          this.discountFee = fomatFloat((totall / (this.form.billAmt / 100000)), 4)
        } else {
          this.annualRate = ''
          this.discountFee = ''
        }
      }, 500)
    },
    handleClose () {
      this.amount = ''
      this.annualRate = ''
      this.discountFee = ''
      this.$emit('on-click')
    },
    handleOk () {
      if (this.annualRate === '') {
        return this.$Message.warning({
          content: '请输入年化率',
          duration: 5,
          closable: true
        })
      }
      if (this.discountFee === '') {
        return this.$Message.warning({
          content: '请输入每10万扣款',
          duration: 5,
          closable: true
        })
      }
      if (this.amount === '') {
        return this.$Message.warning({
          content: '请输入成交金额',
          duration: 5,
          closable: true
        })
      }
      if (!this.hasSignProtocols) {
        return this.$Message.warning({
          content: '请同意签署《领筑数科票据交易平台信息撮合规则》',
          duration: 5,
          closable: true
        })
      }
      if (this.amount < this.form.billAmt * 0.7) {
        return this.$Message.warning({
          content: '成交金额不能小于票面金额的70%',
          duration: 5,
          closable: true
        })
      }
      if (this.amount > this.form.billAmt) {
        return this.$Message.warning({
          content: '成交金额不能大于票面金额',
          duration: 5,
          closable: true
        })
      }
      const data = {
        inquireId: this.id,
        amount: this.amount,
        annualRate: this.annualRate,
        discountFee: this.discountFee,
        hasSignProtocols: this.hasSignProtocols ? 1 : 0,
        surplusValidDays: this.form.surplusValidDays
      }
      this.quoteTicket(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.$Message.success('您的报价提交成功，请等待卖家答复')
          this.$emit('on-click')
          this.form = null
        }
      })
    }
  }
}
</script>
