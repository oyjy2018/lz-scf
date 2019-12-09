<template>
  <Form ref="formCustom"
        class="form"
        style="width: 450px; padding-bottom: 240px"
        :model="formCustom"
        :rules="ruleCustom">
    <FormItem prop="newPassword">
      <Row>
        <Col span="10">
        <Input v-model="formCustom.newPassword"
               type="password"
               placeholder="请输入新密码"
               size="large" />
        </Col>
        <Col span="13"
             offset="1">
        <p>至少8位，包含大小写字母、数字、符号中至少2种</p>
        </Col>
      </Row>
    </FormItem>
    <FormItem prop="passwdCheck">
      <Row>
        <Col span="10">
        <Input v-model="formCustom.passwdCheck"
               type="password"
               placeholder="请输入确定密码"
               size="large" />
        </Col>
        <Col span="13"
             offset="1">
        <p></p>
        </Col>
      </Row>
    </FormItem>
    <FormItem style="text-align: center;">
      <Button @click="handleSubmit('formCustom')"
              type="primary"
              long
              style="font-size: 14px; width: 180px;">

        <span v-if="!loading">保存修改</span>
        <span v-else>Loading...</span>
      </Button>
    </FormItem>
  </Form>
</template>

<script>
import { ValidatePass } from '../../libs/util'

export default {
  name: 'ForgetStep2',
  props: {
    loading: {
      type: Boolean,
      default: false
    }
  },
  data () {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else if (value.length < 8) {
        callback(new Error('密码至少8位'))
      } else if (ValidatePass(value) < 2) {
        callback(new Error('至少8位，包含大小写字母、数字、符号中至少2种'))
      } else {
        if (this.formCustom.passwdCheck !== '') {
          // 对第二个密码框单独验证
          this.$refs.formCustom.validateField('passwdCheck')
        }
        callback()
      }
    }
    const validatePassCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入确认密码'))
      } else if (value !== this.formCustom.newPassword) {
        callback(new Error('密码不一致'))
      } else {
        callback()
      }
    }
    return {
      formCustom: {
        newPassword: '',
        passwdCheck: ''
      },
      ruleCustom: {
        newPassword: [
          { validator: validatePass, trigger: 'blur' }
        ],
        passwdCheck: [
          { validator: validatePassCheck, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.$emit('on-success-valid', {
            newPassword: this.formCustom.newPassword
          })
        } else {
          this.$Message.error({
            content: '请正确填写',
            duration: 5,
            closable: true
          })
        }
      })
    }
  }
}
</script>
