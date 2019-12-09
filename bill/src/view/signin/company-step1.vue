<template>
  <Form class="form"
        ref="step1"
        :model="form"
        :rules="rules"
        @keydown.enter.native="handleSubmit">
    <FormItem prop="phone">
      <Input v-model="form.phone"
             placeholder="请输入手机号码"
             :maxlength="11"
             size="large"
             style="text-algin: center" />
    </FormItem>
    <FormItem prop="vcode">
      <Row>
        <Col span="14">
        <Input type="text"
               size="large"
               v-model="form.vcode"
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
    <FormItem style="text-align: center;">
      <Button @click="handleSubmit"
              :loading="loading"
              type="primary"
              long
              style="font-size: 14px; width: 180px;">
        <span v-if="!loading">下一步</span>
        <span v-else>Loading...</span>
      </Button>
    </FormItem>
    <!-- <FormItem style="text-align: center;">
      <a style="font-size: 14px;"
         @click="$router.back(-1)">使用已有账户登录</a>
    </FormItem> -->
  </Form>
</template>
<script>
import { ValidatePhone } from '../../libs/util'
import { mapActions } from 'vuex'
export default {
  name: 'CompanyStep1',
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
    return {
      form: {
        phone: '',
        vcode: ''
      },
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
        phone: this.phoneRules,
        vcode: {
          required: true, message: '验证码不能为空', trigger: 'blur'
        }
      }
    }
  },
  methods: {
    ...mapActions([
      'checkedRegisterPhone',
      'getVCode',
      'checkVCode'
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
      if (ValidatePhone(this.form.phone)) {
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
      }
    },
    send () {
      this.getVCode({ phone: this.form.phone, smsType: '1' }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.setInTime()
        }
      })
    },
    handleSubmit () {
      this.$refs.step1.validate((valid) => {
        if (valid) {
          this.checkVCode(this.form).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$emit('on-success-valid', {
                phone: this.form.phone
              })
            }
          })
        }
      })
    }
  }
}
</script>
