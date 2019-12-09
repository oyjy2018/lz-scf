<style lang="less" scoped>
@import "../../index.less";
.indicator{
  float: right;
  font-size: 12px;
  font-weight: normal;
  vertical-align: auto;
  color: #8791A0;
  .current {
    font-size: 14px;
    color: #3A9AFD;
    span {
      color: #8791A0;
    }
  }
}
</style>

<template>
  <span class="indicator">
    <span v-for="(flow, idx) in forwardFlow" :key="idx" :fCode="flow.flowCode" :class="{ current: flow.flowCode == flowCode}">
      {{flow.flowName}} <span v-if="idx != forwardFlow.length - 1">></span>
    </span>
  </span>
</template>

<script>
export default {
  name: 'FlowIndicator',
  props: ['forwardFlow', 'flowCode']

}
</script>
