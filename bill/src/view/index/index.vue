<style lang="less">
@import "./index.less";
.table .ivu-table th {
  background: #e6e6e6;
}
.ivu-table-wrapper {
  border: 1px solid #bcd2fc;
  border-bottom: 0;
  border-right: 0;
}
.ivu-table:before,
.ivu-table:after {
  background-color: #bcd2fc;
}
</style>

<template>
  <div class="index-page"
       ref="page">
    <Header :isMin="isMin" />
    <div class="index-page-main">
      <div class="banner">
        <Carousel autoplay
                  loop
                  v-model="carouselBannerIndex"
                  dots="none"
                  :autoplay-speed="carouselBannerAutoplaySpeed">
          <CarouselItem v-if="showDefaultBanner">
            <img src="../../assets/images/front/banner.png"
                 alt="banner">
          </CarouselItem>
          <CarouselItem v-for="(banner, index) in carouselBannerList" :key="index" >
            <img @click="openAnotherPage" :src="banner.bannerFileUrl" style="max-width: 1920px;max-height: 550px">
          </CarouselItem>
        </Carousel>
      </div>
      <!-- banner -->
      <div class="setion1" style="margin-top: 50px"
           v-if="data">
        <div class="box">
          <div class="item">
            <div class="item-value">{{absMoney(data.dealCount.dealAmountDay)}} 万元</div>
            <div class="item-label">今日成交 {{data.dealCount.dealCountDay}} 笔</div>
          </div>
          <img class="item-dot"
               src="../../assets/images/front/dot.png"
               alt="">
          <div class="item">
            <div class="item-value">{{absMoney(data.dealCount.dealAmountMonth)}} 万元</div>
            <div class="item-label">本月成交 {{data.dealCount.dealCountMonth}} 笔</div>
          </div>
          <img class="item-dot"
               src="../../assets/images/front/dot.png"
               alt="">
          <div class="item">
            <div class="item-value">{{absMoney(data.dealCount.dealAmount)}} 万元</div>
            <div class="item-label">累计成交 {{data.dealCount.dealCount}} 笔</div>
          </div>
        </div>
        <div class="table">
          <Table :columns="columns"
                 :data="data.inquireList">
            <div class="table-more"
                 slot="footer">
              <router-link to="/billhall">
                点击查看更多
                <Icon type="ios-arrow-forward" />
              </router-link>
            </div>
          </Table>

        </div>
      </div>
      <!-- setion1 -->
      <div class="setion2">
        <div class="setion2-siders">
          <div class="setion2-sider"
               v-for="(carousel, index) in carouselArr"
               :key="carousel.title"
               @mouseover="carouselIndex = index">
            <Carousel loop
                      :autoplay="carouselIndex === index"
                      arrow="never"
                      dots="none"
                      :autoplay-speed='5000'
                      v-model="carousel.index">
              <CarouselItem v-for="item in carousel.list"
                            :key="item.label+'CarouselItem'">
                <img :src="item.bg">
              </CarouselItem>
            </Carousel>
            <div class="setion2-sider-title"
                 :class="{'mask-title' : carouselIndex !== index}">{{carousel.title}}</div>
            <div class="setion2-sider-dots">
              <div class="setion2-sider-dot"
                   v-for="(item, iIndex) in carousel.list"
                   :key="item.label+'dot'"
                   @mouseover="carousel.index = iIndex">
                <img v-show="carousel.index === iIndex && carouselIndex === index"
                     :src="item.aIcon">
                <img v-show="!(carousel.index === iIndex && carouselIndex === index)"
                     :src="item.icon">
                <p :class="{'active': carousel.index === iIndex && carouselIndex === index }">{{iIndex+1}}.{{item.label}}</p>
              </div>
            </div>
            <div v-show="carouselIndex === index"
                 class="setion2-sider-bottom">
              <div class="title">{{carousel.list[carousel.index].title}}</div>
              <div class="desc">{{carousel.list[carousel.index].desc}}</div>
            </div>
            <div v-show="carouselIndex !== index"
                 class="setion2-sider-mask"></div>
          </div>
        </div>
      </div>
      <div class="setion1">
        <div class="title">领筑服务&合作伙伴</div>
        <div class="server-items">
          <div class="server-item">
            <div class="server-item-logo">
              <img src="../../assets/images/front/lzjf.png"
                   alt=""
                   style="width:69px">
            </div>
            <div class="server-item-title">领筑金服</div>
            <div class="server-item-label">工程一站式贴心金融服务</div>
            <div class="server-item-desc">工程贷 | 信用贷 </div>
            <div class="server-item-desc">快速解决资金周转难题</div>
          </div>
          <div class="server-item">
            <div class="server-item-logo">
              <img src="../../assets/images/front/lzds.png"
                   alt="">
            </div>
            <div class="server-item-title">领筑电商</div>
            <div class="server-item-label">消防材料一站式采购平台</div>
            <div class="server-item-desc">交易透明 | 联合询价 |不赚差价 | 专人服务 </div>
            <div class="server-item-desc">省钱 省时 省力 省心</div>
          </div>
          <div class="server-item">
            <div class="server-item-logo">
              <img src="../../assets/images/front/jd.png"
                   alt="">
            </div>
            <div class="server-item-title">京东金融</div>
            <div class="server-item-label">第三方见证支付平台</div>
            <div class="server-item-desc">企业实名 | 对接ECDS| 第三方支付牌照</div>
            <div class="server-item-desc">安全 合规 高效</div>
          </div>
        </div>
      </div>
    </div>
    <Footer />
    <guide-box container=".index-page" />
    <Quote ref="quote"
           :modal='showBuyModal'
           :id="inquireId"
           @on-click="handleClose" />
  </div>
</template>
<script>
import Header from '_c/header'
import Footer from '_c/footer'
import GuideBox from '_c/guide-box'
import Quote from '../quote/quote'
import { on, off, absMoney } from '@/libs/tools'
import { mapActions, mapState } from 'vuex'
import config from '@/config'

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
      absMoney: absMoney,
      hasReal: false,
      hasReceipt: false,
      hasRepay: false,
      inquireId: -1,
      showBuyModal: false,
      data: null,
      isMin: false,
      carouselIndex: 0,
      carouselBannerList: [],
      showDefaultBanner: true,
      carouselBannerIndex: 0,
      carouselBannerAutoplaySpeed: 3000, // 轮播速度 单位毫秒
      carouselArr: [{
        title: '我是票方',
        index: 0,
        list: [{
          bg: require('../../assets/images/front/pf-f.png'),
          aIcon: require('../../assets/images/front/p1.png'),
          icon: require('../../assets/images/front/p2.png'),
          label: '发布票据',
          title: '发布票据',
          desc: '票方录入票据相关信息'
        }, {
          bg: require('../../assets/images/front/qr.png'),
          aIcon: require('../../assets/images/front/check1.png'),
          icon: require('../../assets/images/front/check2.png'),
          label: '确认意向',
          title: '确认意向',
          desc: '接受资方报价，签订电子合约'
        }, {
          bg: require('../../assets/images/front/qs.png'),
          aIcon: require('../../assets/images/front/bs1.png'),
          icon: require('../../assets/images/front/bs2.png'),
          label: '背书',
          title: '背书',
          desc: '票方线下通过网银背书、资方线下通过网银签收票据'
        }, {
          bg: require('../../assets/images/front/pf-w.png'),
          aIcon: require('../../assets/images/front/over1.png'),
          icon: require('../../assets/images/front/over2.png'),
          label: '完成',
          title: '完成',
          desc: '票款进入票方收款银行账户'
        }]
      }, {
        title: '我是资方',
        index: 0,
        list: [{
          bg: require('../../assets/images/front/zf-b.png'),
          aIcon: require('../../assets/images/front/p1.png'),
          icon: require('../../assets/images/front/p2.png'),
          label: '报价',
          title: '报价',
          desc: '资方对票据报价'
        }, {
          bg: require('../../assets/images/front/qr.png'),
          aIcon: require('../../assets/images/front/check1.png'),
          icon: require('../../assets/images/front/check2.png'),
          label: '确认意向',
          title: '确认意向',
          desc: '票方接受报价，签订电子合约'
        }, {
          bg: require('../../assets/images/front/zf-z.png'),
          aIcon: require('../../assets/images/front/pay1.png'),
          icon: require('../../assets/images/front/pay2.png'),
          label: '支付',
          title: '支付',
          desc: '资方通过京东支付平台支付票款'
        }, {
          bg: require('../../assets/images/front/qs.png'),
          aIcon: require('../../assets/images/front/bs1.png'),
          icon: require('../../assets/images/front/bs2.png'),
          label: '签收',
          title: '签收',
          desc: '票方线下通过网银背书、资方线下通过网银签收票据'
        }, {
          bg: require('../../assets/images/front/zf-w.png'),
          aIcon: require('../../assets/images/front/over1.png'),
          icon: require('../../assets/images/front/over2.png'),
          label: '完成',
          title: '完成',
          desc: '票款进入票方收款银行账户'
        }]
      }],
      columns: [
        {
          title: '票据类型',
          key: 'inquireId',
          align: 'center',
          render: (h, params) => {
            return h('span', '商票')
          }
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
      ]
    }
  },
  computed: {
    ...mapState({
      token: state => state.user.token
    })
  },
  mounted () {
    this.getIndex().then(res => {
      if (res && res.status === 200 && res.data && res.data.code === 10000) {
        this.data = res.data.data
      }
    })
    if (this.token) {
      this.getCompanyJdProperty().then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.hasReal = res.data.data.hasReal
          this.hasReceipt = res.data.data.hasReceipt
          this.hasRepay = res.data.data.hasRepay
        }
      })
    }
    this.handleGetBannerCarousel()
    on(this.$refs.page, 'scroll', this.handleScroll)
    on(this.$refs.page, 'resize', this.handleScroll)
  },
  destroyed () {
    off(this.$refs.page, 'scroll', this.handleScroll)
    off(this.$refs.page, 'resize', this.handleScroll)
  },
  methods: {
    ...mapActions([
      'getIndex',
      'getCompanyJdProperty',
      'getBannerCarousel'
    ]),
    // 新标签打开链接
    openAnotherPage () {
      if (!this.carouselBannerList[this.carouselBannerIndex].jumpUrl || this.carouselBannerList[this.carouselBannerIndex].jumpUrl === '') {
        return
      }
      window.open(this.carouselBannerList[this.carouselBannerIndex].jumpUrl)
    },
    handleGetBannerCarousel () {
      this.getBannerCarousel().then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000 && res.data.data && res.data.data.length > 0) {
          this.carouselBannerList = res.data.data
          this.showDefaultBanner = false
        }
      })
    },
    handleScroll (e) {
      let scrollTop = this.$refs.page.scrollTop
      this.isMin = scrollTop > 80
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
      this.inquireId = row.inquireId
      this.showBuyModal = true
      this.$refs.quote.getDetail(row.inquireId)
    },
    handleClose () {
      this.showBuyModal = false
    }
  }
}
</script>
