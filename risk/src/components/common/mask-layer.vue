<style lang="less" scoped>
@import "../../index.less";
.unique-common-mask-layer{
  position: fixed;
  left: 0px;
  top: 0px;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.2);
  text-align: center;
  z-index: 9000;
  .mask-layer-close {
    width: 40px;
    height: 40px;
    font-size: 20px;
    float: right;
    border-radius:0px 0px 0px 50px;
    background: rgba(0,0,0,0.5);
    cursor: pointer;
    text-align: right;
    line-height: 35px;
    color: rgba(255,255,255,0.7);
    padding-right: 8px;
  }
  .mask-layer-close:hover {
    background-color: rgba(0,0,0,0.7);
    -webkit-transition: background-color 0.15s;
    transition: background-color 0.15s;
  }
  .body {
    margin-top: 5%;
    height: 80%;
    width: 80%;
  }
}
</style>
<template>
  <div class="unique-common-mask-layer" @click="clickBg">
    <div class="mask-layer-close" @click="tclick">X</div>
    <div :class="{body: body}">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'mask-layer',
  components: {},
  props: [
    'mask-closable'
  ],
  data () {
    return {
      body: true
    }
  },
  created () {
    this.show = true
  },
  methods: {
    tclick (e) {
      this.$emit('close', e)
    },
    clickBg (e) {
      if (this['mask-closable'] === undefined || this['mask-closable'] === null || this['mask-closable']) {
        this.$emit('close', e)
      }
    }
  }
}
</script>
