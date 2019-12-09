<template>
  <div class="introduction-main-left">
    <div class="menu-list">
      <div class="menu-list-item">
        <div class="menu-list-item-label"
             :class="{'active': active === 'about'}">
          <router-link to="/about">公司介绍</router-link>
        </div>
        <div class="menu-list-item-label sub">
          <router-link to="/about">公司简介</router-link>
        </div>
        <div class="menu-list-item-label sub">
          <router-link to="/about">企业文化</router-link>
        </div>
        <div class="menu-list-item-label sub">
          <router-link to="/about">合作伙伴</router-link>
        </div>
      </div>
      <div class="menu-list-item">
        <div class="menu-list-item-label"
             :class="{'active': active === 'model'}">
          <router-link to="/model">交易流程</router-link>
        </div>
      </div>
      <div class="menu-list-item">
        <div class="menu-list-item-label"
             :class="{'active': active === 'rulestatement'}">
          <router-link to="/rulestatement"
                       target="_blank">撮合规则</router-link>
        </div>
      </div>
      <div class="menu-list-item">
        <div class="menu-list-item-label"
             :class="{'active': active === 'safety'}">
          <router-link to="/safety">安全保障</router-link>
        </div>
        <div class="menu-list-item-label sub">
          <router-link to="/safety">资金安全</router-link>
        </div>
        <div class="menu-list-item-label sub">
          <router-link to="/safety">交易安全</router-link>
        </div>
        <div class="menu-list-item-label sub">
          <router-link to="/safety">系统安全</router-link>
        </div>
      </div>
      <div class="menu-list-item">
        <div class="menu-list-item-label"
             :class="{'active': active === 'contact'}">
          <router-link to="/contact">联系我们</router-link>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'left',
  props: {
    active: {
      type: String,
      default: 'about'
    }
  }
}
</script>
