<style lang="less">
@import "./login.less";
</style>

<template>
  <div class="login">
    <PageHeader />
    <div class="login-center">
      <div class="login-center-form">
        <login-form :loading="loading"
                    @on-success-valid="handleSubmit"
                    @on-forget="handleForget"></login-form>
        <div class="login-center-form-footer"
             @click="handleSignin">还没有账号？ <span>注册公司</span></div>
      </div>
    </div>
    <Pagefooter />
  </div>
</template>

<script>
import LoginForm from '_c/login-form'
import PageHeader from '_c/page-header'
import Pagefooter from '_c/page-footer'
import { mapActions } from 'vuex'
export default {
  components: {
    LoginForm,
    PageHeader,
    Pagefooter
  },
  data () {
    return {
      loading: false
    }
  },
  methods: {
    ...mapActions([
      'handleLogin',
      'getUserInfo'
    ]),
    handleSubmit ({ loginAccount, password }) {
      this.loading = true
      // base64加密
      loginAccount = btoa(loginAccount)
      password = btoa(password)
      this.handleLogin({ loginAccount, password }).then(res => {
        this.loading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          // let redirect = decodeURIComponent(this.$route.query.redirect || '/')
          let redirect = decodeURIComponent('/')
          this.$router.push(redirect)
        } else {
          this.$Modal.error({
            title: '提示',
            content: res.data.message
          })
        }
      }).catch(() => {
        this.loading = false
      })
    },
    handleForget () {
      this.$router.push('/forget')
    },
    handleSignin () {
      this.$router.push('/signin/company')
    }
  }
}
</script>

<style>
</style>
