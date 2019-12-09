<template>
  <div class="form"
       style="text-align: center;">
    <img src="../../assets/images/reg-suc.png"
         width="153"
         style="margin-bottom: 20px">
    <h1 v-if="isNewUser === 'true'">注册成功</h1>
    <h1 v-if="isNewUser === 'false'">激活成功</h1>
    <div v-if="isNewUser === 'true'">请妥善保管您的账户信息</div>
    <div v-if="isNewUser === 'false'">您可以通过邮箱接收系统通知了</div>
    <Button to="/login"
            type="primary"
            long
            replace
            style="font-size: 14px; width: 180px; margin-top:40px">登录</Button>
  </div>
</template>
<script>
export default {
  name: 'PersonStep2',
  props: {
    isNewUser: {
      type: String,
      default: 'true'
    }
  }
}
</script>
