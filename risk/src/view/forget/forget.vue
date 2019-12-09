<style lang="less">
@import url("../signin/signin.less");
</style>

<template>
  <div class="signin">
    <page-header style="box-shadow:0px 1px 3px 0px rgba(0,0,0,0.5); margin-bottom: 4px;">
      <div class="header-right">
        <a @click="$router.back(-1)">返回首页</a>
      </div>
    </page-header>
    <div class="signin-context">
      <div class="signin-context-con">
        <div class="signin-context-top">
          <div class="signin-context-top-tip">忘记密码</div>
          <div class="signin-context-top-steps"
               style="padding: 30px 75px 0 75px;">
            <div class="step"><span :class="{'active-span': step >= 1}">验证手机</span></div>
            <div class="step"><span :class="{'active-span': step >= 2}">修改密码</span></div>
            <div class="step"><span :class="{'active-span': step >= 3}">修改成功</span></div>
          </div>
          <div class="signin-context-top-steps">
            <div class="border"
                 style="width: 75px"
                 :class="{'active': step >= 1}"></div>
            <div class="step border"
                 :class="{'active': step >= 1}"><i :class="{'active-i': step >= 1}">1</i></div>
            <div class="step border"
                 :class="{'active': step >= 2}"><i :class="{'active-i': step >= 2}">2</i></div>
            <div class="step border"
                 :class="{'active': step >= 3}"><i :class="{'active-i': step >= 3}">3</i></div>
            <div class="border"
                 :class="{'active': step >= 3}"
                 style="width: 75px"></div>
          </div>
        </div>
        <div class="signin-context-form">
          <ForgetStep1 v-show="step === 1"
                       @on-success-valid="handleStep" />
          <ForgetStep2 v-show="step === 2"
                       :loading="loading"
                       @on-success-valid="handleSubmit" />
          <ForgetStep3 v-if="step === 3" />
        </div>
      </div>

    </div>
    <Pagefooter />
  </div>
</template>

<script>
import PageHeader from '_c/page-header'
import Pagefooter from '_c/page-footer'
import ForgetStep1 from './forget-step1'
import ForgetStep2 from './forget-step2'
import ForgetStep3 from './forget-step3'
import { mapActions } from 'vuex'

export default {
  name: 'Forget',
  components: {
    PageHeader,
    Pagefooter,
    ForgetStep1,
    ForgetStep2,
    ForgetStep3
  },
  data () {
    return {
      form: {},
      step: 1,
      loading: false
    }
  },
  mounted () {

  },
  methods: {
    ...mapActions([
      'updatePassword'
    ]),
    handleStep ({ phone }) {
      this.step++
      this.form.phone = phone
    },
    handleSubmit ({ newPassword }) {
      this.loading = true
      this.form.newPassword = newPassword

      this.updatePassword(this.form).then(res => {
        this.loading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          this.step++
        } else {
          this.$Modal.error({
            title: '提示',
            content: '提交失败！'
          })
        }
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>
