<style lang="less">
@import url("./header.less");
</style>

<template>
  <div class="page-header">
    <div class="page-header-context">
      <div class="page-header-context-logo">
        <img src="../../assets/images/logo-top.png"
             height="40">
      </div>
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: 'PageHeader'
}
</script>
