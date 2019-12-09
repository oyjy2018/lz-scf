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
        <Left active="contact" />
        <div class="introduction-main-right">
          <div class="content">
            <div>地址：深圳市南山区艺园路202号马家龙文化体育中心B栋801</div>
            <div> 客服热线：0755-86571036</div>
            <img style="margin-top:46px; width: 100%"
                 src="@/assets/images/front/map.png">
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
  name: 'contact',
  components: {
    Header,
    Footer,
    GuideBox,
    Left
  }
}
</script>
