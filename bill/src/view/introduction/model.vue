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
        <Left active="model" />
        <div class="introduction-main-right">
          <div class="content">
            <img style="width:100%"
                 src="@/assets/images/front/lc.png">
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
  name: 'model',
  components: {
    Header,
    Footer,
    GuideBox,
    Left
  }
}
</script>
