<template>
  <div class="form"
       style="text-align: center;">
    <img src="../../assets/images/reg-status.png"
         width="153"
         style="margin-bottom: 20px">
    <h1>修改成功</h1>
    <div style="margin-bottom: 40px">{{second}}秒后系统将自动跳转到<a href="#">登录页面</a></div>
    <Button type="primary"
            to="/login"
            size="large"
            long
            replace>马上去登录</Button>
  </div>
</template>
<script>
export default {
  name: 'ForgetStep3',
  data () {
    return {
      second: 5,
      timer: null
    }
  },
  methods: {
    backPrev () {
      this.$router.go(-1)
    }
  },
  mounted () {
    this.timer = setInterval(() => {
      if (this.second === 0) this.backPrev()
      else this.second--
    }, 1000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  }
}
</script>
