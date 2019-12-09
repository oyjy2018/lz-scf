<style lang="less">
@import url("./signin.less");
</style>

<template>
  <div class="signin">
    <page-header style="box-shadow:0px 1px 3px 0px rgba(0,0,0,0.5); margin-bottom: 4px;">
      <div class="header-right">
        <!-- <a @click="$router.back(-1)">返回首页</a> -->
        <router-link to="/">返回首页</router-link>
      </div>
    </page-header>
    <div class="signin-context">
      <div class="signin-context-con">
        <div class="signin-context-top">
          <div class="signin-context-top-tip">欢迎注册</div>
          <div class="signin-context-top-steps"
               style="padding: 30px 75px 0 75px;">
            <div class="step"><span :class="{'active-span': step >= 0}">选择产品</span></div>
            <div class="step"><span :class="{'active-span': step >= 1}">填写手机号码</span></div>
            <div class="step"><span :class="{'active-span': step >= 2}">完善资料</span></div>
            <div class="step"><span :class="{'active-span': step >= 3}">平台审核</span></div>
            <div class="step"><span :class="{'active-span': step >= 4}">注册成功</span></div>
          </div>
          <div class="signin-context-top-steps">
            <div class="border"
                 style="width: 75px"
                 :class="{'active': step >= 0}"></div>
            <div class="step border"
                 :class="{'active': step >= 0}"><i :class="{'active-i': step >= 0}">1</i></div>
            <div class="step border"
                 :class="{'active': step >= 1}"><i :class="{'active-i': step >= 1}">2</i></div>
            <div class="step border"
                 :class="{'active': step >= 2}"><i :class="{'active-i': step >= 2}">3</i></div>
            <div class="step border"
                 :class="{'active': step >= 3}"><i :class="{'active-i': step >= 3}">4</i></div>
            <div class="step border"
                 :class="{'active': step >= 4}"><i :class="{'active-i': step >= 4}">5</i></div>
            <div class="border"
                 :class="{'active': step >= 4}"
                 style="width: 75px"></div>
          </div>
        </div>
        <div class="signin-context-form">
          <!-- <keep-alive :include="['CompanyStep1', 'CompanyStep2']">
            <router-view />
          </keep-alive> -->
          <CompanyStep0 v-show="step === 0"
                        @on-success-valid="handleStep0" />
          <CompanyStep1 v-show="step === 1"
                        @on-success-valid="handleStep" />
          <company-step2 v-show="step === 2"
                         :loading="loading"
                         @on-success-valid="handleSubmit"></company-step2>
          <company-step3 v-show="step === 3" />
        </div>
      </div>

    </div>
    <Pagefooter />
  </div>
</template>

<script>
import PageHeader from '_c/page-header'
import Pagefooter from '_c/page-footer'
import CompanyStep0 from './company-step0'
import CompanyStep1 from './company-step1'
import CompanyStep2 from './company-step2'
import CompanyStep3 from './company-step3'
import { mapActions } from 'vuex'
// import { toDateString } from '@/libs/tools'

export default {
  name: 'SigninCompany',
  components: {
    PageHeader,
    Pagefooter,
    CompanyStep0,
    CompanyStep1,
    CompanyStep2,
    CompanyStep3
  },
  data () {
    return {
      form: {},
      step: 0,
      loading: false
    }
  },
  mounted () {
    this.getNatureSelectData('null')
    this.getStaffsizeSelectData()
  },
  methods: {
    ...mapActions([
      'handleReg',
      'getNatureSelectData',
      'getStaffsizeSelectData'
    ]),
    handleStep0 ({ systemIdList }) {
      this.step++
      this.form.systemIdList = systemIdList
    },
    handleStep ({ phone }) {
      this.step++
      this.form.porxyPersonPhone = phone
    },
    handleSubmit ({ from }) {
      this.loading = true
      // form.legalPersonCertEndDate = toDateString(form.porxyPersonCertEndDate, '')
      // form.porxyPersonCertEndDate = toDateString(form.porxyPersonCertEndDate, '')
      this.form = Object.assign(this.form, from)
      this.handleReg(this.form).then(res => {
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
