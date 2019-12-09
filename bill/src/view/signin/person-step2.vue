<template>
  <div class="form"
       style="text-align: center;">
    <img src="../../assets/images/reg-status.png"
         width="153"
         style="margin-bottom: 20px">
    <h1>注册成功</h1>
    <div>请妥善保管您的账户信息</div>
    <Button to="/login"
            type="primary"
            long
            replace
            style="font-size: 14px; width: 180px; margin-top:40px">登录</Button>
  </div>
</template>
<script>
export default {
  name: 'PersonStep2'
}
</script>
