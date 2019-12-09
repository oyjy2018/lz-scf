<style lang="less" scoped>
@import "../../index.less";
.text-area-bottom-div {
    position: absolute;
    background: #fff;
    line-height: 14px;
    width: 100%;
    text-align: right;
    margin-top: -5px;
    padding-right: 5px;
    padding-bottom: 2px;
    border-color: #dcdee2;
    border-width: 0px 1px 1px 1px;
    border-style: solid;
    border-radius: 0px 0px 4px 4px;
    -webkit-border-radius: 0px 0px 4px 4px;
    -moz-border-radius: 0px 0px 4px 4px;

    transition: border 0.2s ease-in-out, background 0.2s ease-in-out, box-shadow 0.2s ease-in-out, -webkit-box-shadow 0.2s ease-in-out;

}

</style>
<template>
  <div ref="centent" class="text-area-bottom-content">
    <slot></slot>
    <div class="text-area-bottom-div">
      {{length}}/{{maxlength}}
    </div>
  </div>
</template>
<script>
export default {
  name: 'TextAreaBottom',
  props: ['value'],
  data () {
    return {
      maxlength: 0,
      length: 0
    }
  },
  mounted () {
    let input = this.$refs['centent'].getElementsByClassName('ivu-input')[0]
    this.maxlength = input.attributes['maxlength'].value
    this.length = this.value.length
  },
  // computed: {
  //   lengthT () {
  //     let input = this.$refs['centent'].getElementsByClassName('ivu-input')[0]
  //     this.length = input.value.length
  //     return input.value.length
  //   }
  // },
  watch: {
    value (value) {
      this.length = value.length
    }
  }
}
</script>
