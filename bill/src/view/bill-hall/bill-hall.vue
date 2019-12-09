<style lang="less">
@import "../index/index.less";
@import "./bill-hall.less";
.table .ivu-table th {
  background: #ecf0f6;
}
.ivu-table-wrapper {
  border: 0 solid #ecf0f6;
  border-bottom: 0;
  border-right: 0;
}
.ivu-table:before,
.ivu-table:after {
  background-color: transparent;
}
</style>

<template>
  <div class="index-page"
       ref="page">
    <Header :isMin="true" />
    <div class="index-page-main"
         style="background: #f3f3f3; padding-top: 60px;">
      <div class="setion1">
        <div class="top-box">
          <div class="top-box-item">
            <div class="label">瑕疵</div>
            <div class="buttons">
              <span :class="{'active': form.hasFlaws === '1'}"
                    @click="handleHasFlaws('1')">有瑕疵</span>
              <span :class="{'active': form.hasFlaws === '0'}"
                    @click="handleHasFlaws('0')">无瑕疵</span>
            </div>
            <div class="inputs">
              <Input search
                     v-model="form.accepterName"
                     style="width: 336px"
                     placeholder="请输入承兑人"
                     @on-search="handleSearch" />
            </div>
          </div>
          <div class="top-box-item">
            <div class="label">票面金额</div>
            <div class="buttons">
              <span v-for="(item, index) in billAmts"
                    :key="item"
                    :class="{'active': billAmtsIndex === index}"
                    @click="handleBillAmts(index)">{{item}}</span>
            </div>
            <div class="inputs">
              <Input style="width: 160px"
                     v-model="form.billAmtBegin"
                     placeholder="最低金额（万）" />
              <span style="padding: 0 6px;">-</span>
              <Input search
                     style="width: 160px"
                     v-model="form.billAmtEnd"
                     placeholder="最高金额（万）"
                     @on-search="handleSearch" />
            </div>
          </div>
          <div class="top-box-item">
            <div class="label">剩余天数</div>
            <div class="buttons">
              <span v-for="(item, index) in days"
                    :key="item"
                    :class="{'active': daysIndex === index}"
                    @click="handleDay(index)">{{item}}</span>
            </div>
            <div class="inputs">
              <Input style="width: 160px"
                     v-model="form.surplusValidDaysBegin"
                     placeholder="最少天数" />
              <span style="padding: 0 6px;">-</span>
              <Input search
                     style="width: 160px"
                     v-model="form.surplusValidDaysEnd"
                     placeholder="最多天数"
                     @on-search="handleSearch" />
            </div>
          </div>
          <!-- <div v-show="showMore"
               class="top-box-item">
            <div class="label">成交信用</div>
            <div class="buttons">
              <span>优秀</span>
              <span>良好</span>
              <span>一般</span>
            </div>
          </div>
          <div class="top-box-more"
               @click="showMore = !showMore">更多
            <Icon type="ios-arrow-down" />
          </div> -->
        </div>
        <!-- box -->
        <div class="table"
             style="margin-bottom: 40px; background: #fff;min-height: 620px">
          <Table :columns="columns"
                 :data="list">
            <div class="table-more"
                 slot="footer"
                 style="margin: 8px 20px 0 0; text-align: right">
              <Page :total="total"
                    :current="form.current"
                    :page-size="form.size"
                    @on-change="changePage"
                    show-elevator
                    show-total></Page>
            </div>
          </Table>

        </div>
        <!-- <table> -->
      </div>
    </div>
    <Footer />
    <Quote ref="quote"
           :modal='showBuyModal'
           :id="inquireId"
           @on-click="handleClose" />
    <guide-box container=".index-page" />
  </div>
</template>
<script>
import Header from '_c/header'
import Footer from '_c/footer'
import GuideBox from '_c/guide-box'
import { absMoney } from '@/libs/tools'
import Quote from '../quote/quote'
import { mapActions, mapState } from 'vuex'

export default {
  name: 'index',
  components: {
    Header,
    Footer,
    GuideBox,
    Quote
  },
  data () {
    return {
      hasReal: false,
      hasReceipt: false,
      hasRepay: false,
      inquireId: -1,
      showBuyModal: false,
      billAmts: ['10万以下', '10-100万', '100万以上'],
      days: ['90天以内', '90-180天', '180-360天'],
      billAmtsIndex: -1,
      daysIndex: -1,
      showMore: false,
      form: {
        current: 1,
        size: 10,
        accepterName: '',
        hasFlaws: '',
        selectByLogin: false,
        billAmtBegin: '',
        billAmtEnd: '',
        surplusValidDaysBegin: '',
        surplusValidDaysEnd: ''
      },
      total: 0,
      columns: [
        {
          title: '票据类型',
          key: 'inquireId',
          align: 'center',
          render: (h, params) => {
            return h('div', '商票')
          }
        },
        {
          title: '发布时间',
          key: 'createTime',
          align: 'center'
        },
        {
          title: '承兑人',
          key: 'accepterName',
          align: 'center'
        },
        {
          title: '票据金额(元)',
          key: 'billAmt',
          align: 'right',
          render: (h, params) => {
            return h('span', absMoney(params.row.billAmt))
          }
        },
        {
          title: '到期日',
          key: 'maturityDate',
          align: 'center'
        },
        {
          title: '瑕疵',
          key: 'flaws',
          align: 'center'
        },
        {
          title: '操作',
          key: 'address',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'error',
                  size: 'small'
                },
                on: {
                  click: () => {
                    this.handleBuy(params.row)
                  }
                }
              }, '我要买')
            ])
          }
        }
      ],
      list: []
    }
  },
  computed: {
    ...mapState({
      token: state => state.user.token
    })
  },
  mounted () {
    this.getInquireListData()
    if (this.token) {
      this.getCompanyJdProperty().then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.hasReal = res.data.data.hasReal
          this.hasReceipt = res.data.data.hasReceipt
          this.hasRepay = res.data.data.hasRepay
        }
      })
    }
    this.$ba.trackPageview('/bill/billhall')
  },
  methods: {
    ...mapActions([
      'getInquireList',
      'getCompanyJdProperty'
    ]),
    changePage (current) {
      this.form.current = current
      this.getInquireListData()
    },
    getInquireListData () {
      this.getInquireList(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.total
          this.list = res.data.data.list
        }
      })
    },
    handleSearch () {
      this.form.current = 1
      this.getInquireListData()
    },
    handleDay (index) {
      this.form.current = 1
      if (this.daysIndex === index) {
        this.daysIndex = -1
        this.form.surplusValidDaysBegin = ''
        this.form.surplusValidDaysEnd = ''
      } else {
        this.daysIndex = index
        if (index === 0) {
          this.form.surplusValidDaysBegin = ''
          this.form.surplusValidDaysEnd = '90'
        }
        if (index === 1) {
          this.form.surplusValidDaysBegin = '90'
          this.form.surplusValidDaysEnd = '180'
        }
        if (index === 2) {
          this.form.surplusValidDaysBegin = '180'
          this.form.surplusValidDaysEnd = '360'
        }
      }
      this.getInquireListData()
    },
    handleBillAmts (index) {
      this.form.current = 1
      if (this.billAmtsIndex === index) {
        this.billAmtsIndex = -1
        this.form.billAmtBegin = ''
        this.form.billAmtEnd = ''
      } else {
        this.billAmtsIndex = index
        if (index === 0) {
          this.form.billAmtBegin = ''
          this.form.billAmtEnd = '10'
        }
        if (index === 1) {
          this.form.billAmtBegin = '10'
          this.form.billAmtEnd = '100'
        }
        if (index === 2) {
          this.form.billAmtBegin = '100'
          this.form.billAmtEnd = ''
        }
      }
      this.getInquireListData()
    },
    handleHasFlaws (hasFlaws) {
      this.form.current = 1
      if (this.form.hasFlaws === hasFlaws) {
        this.form.hasFlaws = ''
      } else {
        this.form.hasFlaws = hasFlaws
      }
      this.getInquireListData()
    },
    handleBuy (row) {
      if (!this.token) {
        this.$Message.warning({
          content: '您还没登录',
          duration: 5,
          closable: true
        })
        this.$router.push('/login')
        return false
      }
      if (!this.hasReal) {
        this.$Modal.confirm({
          title: '提示',
          content: '您还没企业实名认证,是否去企业实名认证？',
          onOk: () => {
            window.open(config.realUrl)
          }
        })
        return false
      }
      // 是否在京东绑定收票账户
      if (!this.hasReceipt) {
        this.$Modal.confirm({
          title: '提示',
          content: '您还没绑定收票账户,是否去绑定？',
          onOk: () => {
            window.open(config.bankUrl)
          }
        })
        return false
      }
      this.inquireId = row.id
      this.showBuyModal = true
      this.$refs.quote.getDetail(row.id)
    },
    handleClose () {
      this.showBuyModal = false
    }
  }
}
</script>
