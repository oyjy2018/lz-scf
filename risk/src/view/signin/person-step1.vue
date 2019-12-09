<template>
  <Form class="form"
        ref="step1"
        :model="form"
        :rules="rules"
        @keydown.enter.native="handleSubmit">
    <FormItem v-if="femail"
              prop="email">
      <Input v-model="femail"
             disabled
             placeholder="请输入登录邮箱"
             size="large"
             style="text-algin: center" />
    </FormItem>
    <FormItem v-else
              prop="email">
      <Input v-model="form.email"
             placeholder="请输入登录邮箱"
             size="large"
             style="text-algin: center" />
    </FormItem>
    <FormItem prop="userName">
      <Input v-model="form.userName"
             placeholder="请输入你的真实姓名"
             size="large"
             style="text-algin: center" />
    </FormItem>
    <FormItem prop="password">
      <Row>
        <Col span="13">
        <Input v-model="form.password"
               type="password"
               placeholder="请输入密码"
               size="large"
               style="text-algin: center" />
        </Col>
        <Col span="10"
             offset="1">
        <p>至少8位，包含大小写字母、数字、符号中至少2种</p>
        </Col>
      </Row>
    </FormItem>
    <FormItem prop="phone">
      <Input v-model="form.phone"
             placeholder="请输入手机号码"
             :maxlength="11"
             size="large"
             style="text-algin: center" />
    </FormItem>
    <FormItem prop="code">
      <Row>
        <Col span="14">
        <Input type="text"
               size="large"
               v-model="form.code"
               :maxlength="6"
               placeholder="请输入验证码"></Input>
        </Col>
        <Col span="8"
             offset="2">
        <Button :disabled="!vPhone(form.phone) || auth_time !== 0"
                @click="checkedPhone"
                shape="circle"
                type="primary"
                style="width: 130px"
                size="large">{{auth_time ? '重新发送('+auth_time+'s)' : '获取短信验证码'}}</Button>
        </Col>
      </Row>
    </FormItem>
    <FormItem prop="read">
      <CheckboxGroup v-model="form.read">
        <Checkbox label="yes">阅读并同意
          <router-link to="/signin/service-agreement"
                       target="_blank">《用户服务协议》</router-link>
          <router-link to="/signin/privacy-statement"
                       target="_blank">《平台隐私声明》</router-link>
        </Checkbox>
      </CheckboxGroup>
    </FormItem>
    <FormItem style="text-align: center;">
      <Button @click="handleSubmit"
              :loading="loading"
              type="primary"
              long
              size="large"
              style="font-size: 14px; width: 180px;">
        <span v-if="!loading">注册</span>
        <span v-else>Loading...</span>
      </Button>
    </FormItem>
  </Form>
</template>
<script>
import { ValidatePhone, ValidatePass } from '../../libs/util'
import { mapActions } from 'vuex'
export default {
  name: 'person-step1',
  props: {
    femail: {
      type: String,
      default: ''
    }
  },
  data () {
    const validatePhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('手机号码不能为空'))
      } else if (!ValidatePhone(value)) {
        callback(new Error('手机号码格式不正确'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 8) {
        callback(new Error('密码至少8位'))
      } else if (ValidatePass(value) < 2) {
        callback(new Error('至少8位，包含大小写字母、数字、符号中至少2种'))
      } else {
        callback()
      }
    }
    return {
      form: {
        email: '',
        userName: '',
        password: '',
        phone: '',
        code: '',
        read: []
      },
      passRules: [
        { validator: validatePass, trigger: 'blur' }
      ],
      phoneRules: [
        { validator: validatePhone, trigger: 'blur' }
      ],
      vPhone: ValidatePhone,
      auth_time: 0,
      loading: false
    }
  },
  computed: {
    rules () {
      return {
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: '请输入你的真实姓名', trigger: 'blur' }
        ],
        password: this.passRules,
        phone: this.phoneRules,
        read: [
          { required: true, type: 'array', min: 1, message: '请先勾选协议', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    ...mapActions([
      'getVCode',
      'checkVCode',
      'checkedRegisterPhone'
    ]),
    setInTime () {
      this.auth_time = 60
      var auth_timetimer = setInterval(() => {
        this.auth_time--
        if (this.auth_time <= 0) {
          clearInterval(auth_timetimer)
        }
      }, 1000)
    },
    checkedPhone () {
      this.checkedRegisterPhone({ phone: this.form.phone }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          if (res.data.data.isRegister) {
            this.form.phone = ''
            this.$Modal.error({
              title: '提示',
              content: '手机号已注册'
            })
          } else {
            this.send()
          }
        }
      })
    },
    send () {
      this.getVCode({ phone: this.form.phone, smsType: '1' }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.setInTime()
        }
      })
    },
    handleSubmit () {
      if (this.femail) {
        this.form.email = this.femail
      }
      this.$refs.step1.validate((valid) => {
        if (valid) {
          this.$emit('on-success-valid', {
            form: this.form
          })
        }
      })
    }
  }
}
</script>
