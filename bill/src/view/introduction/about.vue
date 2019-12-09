<style lang="less">
@import "../index/index.less";
@import "./introduction.less";
</style>

<template>
  <div class="index-page"
       ref="page">
    <Header :isMin="true" />
    <div class="introduction">
      <div class="introduction-banner"></div>
      <div class="introduction-main">
        <Left active="about" />
        <div class="introduction-main-right">
          <div class="title">公司简介</div>
          <div class="content">
            <p>领筑数科的使命是“让工程更融易”，即通过金融技术与数据，建立工程项目以及材料交易的数据信用，解决工程融资难题。</p>
            <p>领筑数科致力于将30余年的工程项目管理经验与风控能力，与现代金融科技和大数据、云计算、区块链、移动互联网等先进技术进行融合，构建工程材料采购线上交易场景下的行业供应链金融解决方案，打造工程金融服务平台，连接工程公司、工程项目、材料电商、保理公司与银行等相关价值链主体，创新基于数据的信用模式，实现产业升级。</p>
            <p>领筑数科开发的“领筑金融云”即可独立支撑工程行业的授信、用信、风控、保理等金融业务，亦可与集团公司领筑互联开发的领筑电商、领筑工程云、领筑项目管理SaaS等关联平台协同，为工程公司、电商平台提供一揽子的解决方案，提高项目整体管理水平，提升产业效率。</p>
          </div>

          <div class="title">企业文化</div>
          <div class="content">
            <div class="cell">
              <img src="@/assets/images/front/ai1.png">
              <div class="cell-text">
                <b>诚信</b>
                <span>成为工程行业的“蚂蚁金服”，让工程更融易</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai4.png">
              <div class="cell-text">
                <b>合作</b>
                <span>合理运营银行授信，商票等金融工具，帮助领筑电商建立支付信用</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai3.png">
              <div class="cell-text">
                <b>突破</b>
                <span>应用金融科技、金融技术，为交易价值链赋能</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai2.png">
              <div class="cell-text">
                <b>共赢</b>
                <span>为工程行业上下游企业、个人、提供卓越的科技金融服务</span>
              </div>
            </div>
          </div>

          <div class="title">合作伙伴</div>
          <div class="content">
            <div class="imgs">
              <img src="@/assets/images/front/jdlogo.png">
              <img src="@/assets/images/front/lzjflogo.png">
              <img src="@/assets/images/front/shjslogo.png">
            </div>
          </div>

        </div>
      </div>
    </div>
    <Footer />
    <guide-box container=".index-page" />
  </div>
</template>
<script>
import Header from '_c/header'
import Footer from '_c/footer'
import GuideBox from '_c/guide-box'
import Left from './left'

export default {
  name: 'about',
  components: {
    Header,
    Footer,
    GuideBox,
    Left
  }
}
</script>
