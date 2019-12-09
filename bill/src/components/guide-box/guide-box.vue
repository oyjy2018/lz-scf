<style lang="less" scoped>
@import "./guide-box.less";
</style>

<template>
  <div class="guide-box">
    <div @mouseover="top = true"
         @mouseleave="top = false"
         @click="goTop"
         style="border-bottom: 1px solid #fff; padding-bottom:10px;">
      <img v-show="top"
           src="../../assets/images/front/guide/top1.png">
      <img v-show="!top"
           src="../../assets/images/front/guide/top2.png">
    </div>
    <!-- <div @mouseover="we = true"
         @mouseleave="we = false">
      <img v-show="we"
           src="../../assets/images/front/guide/we1.png">
      <img v-show="!we"
           src="../../assets/images/front/guide/we2.png">
    </div>
    <div @mouseover="c = true"
         @mouseleave="c = false">
      <img v-show="c"
           src="../../assets/images/front/guide/c1.png">
      <img v-show="!c"
           src="../../assets/images/front/guide/c2.png">
    </div>
    <div @mouseover="e = true"
         @mouseleave="e = false">
      <img v-show="e"
           src="../../assets/images/front/guide/e1.png">
      <img v-show="!e"
           src="../../assets/images/front/guide/e2.png">
    </div> -->
  </div>
</template>
<script>
import { scrollTop } from '@/libs/util'

export default {
  name: 'GuideBox',
  data () {
    return {
      top: false,
      we: false,
      c: false,
      e: false
    }
  },
  props: {
    container: {
      type: null,
      default: window
    },
    duration: {
      type: Number,
      default: 1000
    }
  },
  computed: {
    containerEle () {
      return this.container === window ? window : document.querySelector(this.container)
    }
  },
  methods: {
    goTop () {
      let target = typeof this.container === 'string' ? this.containerEle : (document.documentElement || document.body)
      const sTop = target.scrollTop
      scrollTop(this.containerEle, sTop, 0, this.duration)
    }
  }
}
</script>
