<style lang="less" scoped>
@import "./header.less";
.ivu-select-dropdown {
  margin-top: -100px;
}
</style>

<template>
  <div class="header-page"
       :class="{'min-height': isMin}">
    <div class="header-page-center">
      <img class="header-page-center-logo"
           src="../../assets/images/front/logo.png"
           alt="logo">
      <div class="header-page-center-menu">
        <router-link :class="{'active': name === 'index'}"
                     to="/">首页</router-link>
        <router-link :class="{'active': name === 'billhall'}"
                     to="/billhall">交易大厅</router-link>
        <router-link :class="{'active': name === 'publishticket'}"
                     to="/publishticket">发布票据</router-link>
        <router-link v-if="userName && checkMenuPerm('运营管理')" :class="{'active': name === 'operationsMgt'}"
                     to="/operationsMgt">运营管理</router-link>
        <Dropdown v-if="userName" :transfer="trueObj" style="line-height: 30px;">
          <a href="javascript:void(0)"
             :class="{'active': (name === 'sell' || name === 'buy')}">
            用户中心
            <Icon type="ios-arrow-down"></Icon>
          </a>
          <DropdownMenu slot="list">
            <DropdownItem>
              <router-link tag="li" :to="{path:'/sell',query:{sellShow:'/buy'}}"
                           style="line-height: 30px">我是票方</router-link>
            </DropdownItem>
            <DropdownItem>
              <router-link tag="li" :to="{path:'/buy',query:{sellShow:'/sell'}}"
                           style="line-height: 30px">我是资方</router-link>
            </DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </div>
      <Dropdown v-if="userName" @on-click="logout" :transfer="trueObj"
                style="font-size: 16px;cursor: pointer;line-height: 40px;margin-top: 15px;">
        <span >
          {{userName}}
          <Icon type="ios-arrow-down"></Icon>
        </span>
        <DropdownMenu slot="list">
          <DropdownItem>
            <span @click="logout">退出登录</span>
          </DropdownItem>
        </DropdownMenu>
      </Dropdown>
      <template v-else>
        <router-link to="/login"
                     class="login">登录</router-link>
        <router-link to="/signin/company"
                     class="reg">注册</router-link>
      </template>
    </div>
  </div>
</template>
<script>
import { mapState, mapActions } from 'vuex'
import { getAccess } from '@/libs/util'

export default {
  name: 'headerPage',
  props: {
    isMin: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      trueObj: true, // 公用字段，所有需要固定使用true值的地方都可以使用
      currentPath: '',
      clistArry: []
    }
  },
  computed: {
    ...mapState({
      userName: state => {
        let name = ''
        name = state.user.userName.slice(state.user.userName.lastIndexOf('-') + 1, state.user.userName.length)
        return name
      }
    }),
    name: function () {
      // 这句的作用仅仅是为了监控路由发生变化时，触发这个计算属性的函数
      this.currentPath.substring(1)
      return this.$route.matched[0].path.substring(1)
    },
    key () {
      return this.$route.name !== undefined ? this.$route.name + new Date() : this.$route + new Date()
    }
  },
  mounted () {
    this.currentPath = this.$route.name
    this.clistArry = getAccess()
  },
  methods: {
    ...mapActions([
      'handleLogOut'
    ]),
    logout (name) {
      this.handleLogOut().then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          window.location.reload()
        }
      })
    },
    checkMenuPerm (menuTitle) {
      // 判断是否存在该菜单权限
      for (let c of this.clistArry) {
        if (menuTitle === c.split('-')[0]) {
          return true
        }
      }
      return false
    }
  }
}
</script>
