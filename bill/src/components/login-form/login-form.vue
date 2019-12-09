<template>
  <Form ref="loginForm"
        :model="form"
        :rules="rules"
        @keydown.enter.native="handleSubmit">
    <FormItem prop="loginAccount">
      <Input v-model="form.loginAccount"
             placeholder="请输入邮箱或手机"
             size="large"
             style="text-algin: center" />
    </FormItem>
    <FormItem prop="password">
      <Input type="password"
             v-model="form.password"
             placeholder="请输入密码"
             size="large" />
    </FormItem>
    <div class="forget"><span @click="$emit('on-forget')">忘记密码？</span></div>
    <Button @click="handleSubmit"
            :loading="loading"
            type="primary"
            long
            style="padding: 10px 15px;font-size: 18px;">
      <span v-if="!loading">登录</span>
      <span v-else>loading...</span>
    </Button>
  </Form>
</template>
<script>
import { ValidatePhone, validateEmail } from '../../libs/util'

export default {
  name: 'LoginForm',
  props: {
    loading: {
      type: Boolean,
      default: false
    }
  },
  data () {
    const validateLoginAccount = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('账号不能为空'))
      } else if (!ValidatePhone(value) && !validateEmail(value)) {
        callback(new Error('手机号码或者邮箱格式不正确'))
      } else {
        callback()
      }
    }
    return {
      form: {
        loginAccount: '',
        password: ''
      },
      loginAccountRules: [
        { validator: validateLoginAccount, trigger: 'blur' }
      ],
      passwordRules: [
        { required: true, message: '密码不能为空', trigger: 'blur' }
      ]
    }
  },
  computed: {
    rules () {
      return {
        loginAccount: this.loginAccountRules,
        password: this.passwordRules
      }
    }
  },
  methods: {
    handleSubmit () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$emit('on-success-valid', {
            loginAccount: this.form.loginAccount,
            password: this.form.password
          })
        }
      })
    }
  }
}
</script>
