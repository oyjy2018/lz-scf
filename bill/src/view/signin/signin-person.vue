<style lang="less">
@import url("./signin.less");
</style>

<template>
  <div class="signin">
    <page-header style="box-shadow:0px 1px 3px 0px rgba(0,0,0,0.5); margin-bottom: 4px;">
      <div class="header-right">
        <!-- <a v-if="!company"
           @click="$router.back(-1)">返回首页</a> -->
        <router-link to="/">返回首页</router-link>
      </div>
    </page-header>
    <div class="signin-context">
      <div class="signin-context-con">
        <div class="signin-context-top">
          <div class="signin-context-top-tip">
            <div class="person"
                 v-if="company">申请加入{{company}}</div>
            <div class="person"
                 v-else>申请注册</div>
          </div>
        </div>
        <div class="signin-context-form">
          <PersonStep1 v-show="step === 1"
                       :femail="email"
                       @on-success-valid="handleSubmit" />
          <PersonStep2 v-show="step === 2" />
        </div>
      </div>

    </div>
    <Pagefooter />
  </div>
</template>

<script>
import PageHeader from '_c/page-header'
import Pagefooter from '_c/page-footer'
import PersonStep1 from './person-step1'
import PersonStep2 from './person-step2'
import { mapActions } from 'vuex'

export default {
  name: 'signin-person',
  components: {
    PageHeader,
    Pagefooter,
    PersonStep1,
    PersonStep2
  },
  data () {
    return {
      form: {},
      company: '',
      email: '',
      companyId: '',
      step: 1,
      loading: false
    }
  },
  mounted () {
    const query = this.$route.query
    if (query && query.param) {
      const params = decodeURI(query.param).split(',')
      this.email = params[0]
      this.company = params[1]
      this.companyId = params[2]
    }
  },
  methods: {
    ...mapActions([
      'regPersonEmail'
    ]),
    handleSubmit ({ form }) {
      this.loading = true
      this.form = Object.assign(this.form, form)
      this.form.companyId = this.companyId
      this.regPersonEmail(this.form).then(res => {
        this.loading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          this.step++
        } else {
          this.$Modal.error({
            title: '提示',
            content: res.data.message
          })
        }
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>
