<style lang="less" scoped>
.layout {
  background: #f3f3f3;
  position: relative;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  &-header {
    background: transparent;
  }
  &-center {
    display: flex;
    justify-content: center;
    padding: 20px 0;
    width: 100%;
    min-height: 800px;
    &-main {
      display: flex;
      // width: 1280px;
      width: 100%;
      &-sider {
        width: 200px;
        overflow: hidden;
        background: transparent;
      }
      &-content {
        margin-left: 8px;
        background: #fff;
      }
    }
  }
}
.layout-footer {
  padding: 0;
}
</style>

<template>
  <div class="layout">
    <Layout>
      <Header class="layout-header">
        <Headers :isMin="true" />
      </Header>
      <Layout class="layout-center">
        <div class="layout-center-main">
          <Sider class="layout-center-main-sider"
                 hide-trigger>
            <SiderMenu @on-select="refreshPage" />
          </Sider>
          <Content class="layout-center-main-content">
            <router-view v-if="refresh" :key="key" />
          </Content>
        </div>
      </Layout>
      <Footer class="layout-footer">
        <Footers />
      </Footer>
    </Layout>
  </div>
</template>
<script>
import Headers from '_c/header'
import Footers from '_c/footer'
import SiderMenu from './sider-menu'

export default {
  name: 'Center',
  components: {
    Headers,
    Footers,
    SiderMenu
  },
  data () {
    return {
      refresh: true
    }
  },
  computed: {
    key () {
      let key = this.$route.name !== undefined ? this.$route.name + new Date() : this.$route + new Date()
      console.log(key)
      return key
    }
  },
  methods: {
    refreshPage (name) {
      if (this.$route.name === name) {
        this.refresh = false
        this.$nextTick(function () {
          this.refresh = true
        })
      }
    }
  }
}
</script>
