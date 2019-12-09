<style lang="less">
@import url("./signin.less");
</style>

<template>
  <div class="signin">
    <page-header style="box-shadow:0px 1px 3px 0px rgba(0,0,0,0.5); margin-bottom: 4px;">
      <div class="header-right">
        <a v-if="!company"
           @click="$router.back(-1)">返回首页</a>
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
          <PersonStep1  v-show="step === 1"
                       :femail="email"
                       @on-success-valid="handleSubmit" />
          <PersonStep2 v-show="step === 2"
                       :isNewUser="isNewUser"/>
        </div>
      </div>

    </div>
    <Pagefooter />

    <Modal v-model="expiredModal" width="360" :closable="false" :mask-closable="false">
      <p slot="header" style="color:#f60;text-align:left">
        <Icon type="ios-information-circle"></Icon>
        <span>信息提示</span>
      </p>
      <div style="text-align:left">
        <p>此链接已过期，链接的有效期是5天。</p>
        <P>请联系公司管理员重新发送。</P>
        <a @click="handleRedirect">返回首页</a>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long :loading="modal_loading" @click="handleConfirmClick">确定</Button>
      </div>
    </Modal>
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
      isNewUser: 'true', // 是否为新注册用户，新用户需要填写用户信息，老用户点击加入相应公司建立关联
      step: 1,
      loading: false,
      expiredTime: 0,
      expiredModal: false,
      modal_loading: false
    }
  },
  mounted () {
    const query = this.$route.query
    if (query && query.param) {
      const params = decodeURI(query.param).split(',')
      this.email = params[0]
      this.company = params[1]
      this.companyId = params[2]
      this.expiredTime = params[3]
      const nowTime = Date.parse(new Date())
      if (!this.expiredTime || nowTime > this.expiredTime) {
        this.expiredModal = true
      }
      if (this.email) {
        this.handleCheckIsNewUser()
      }
    }
  },
  methods: {
    ...mapActions([
      'acceptInviteAndRegister',
      'acceptInvite',
      'checkIsNewUser'
    ]),
    // 检查是否为新用户
    handleCheckIsNewUser () {
      const data = {
        email: this.email
      }
      this.checkIsNewUser(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.isNewUser = res.data.data + ''
        }
        // 如果不是新用户直接接受邀请加入公司
        if (this.isNewUser && this.isNewUser === 'false' && this.expiredTime > Date.parse(new Date())) {
          this.handleAcceptInvite()
        }
      })
    },
    handleSubmit ({ form }) {
      this.loading = true
      this.form = Object.assign(this.form, form)
      this.form.companyId = this.companyId
      this.acceptInviteAndRegister(this.form).then(res => {
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
    },
    handleAcceptInvite () {
      const data = {
        companyId: this.companyId,
        email: this.email
      }
      this.acceptInvite(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.step++
        } else {
          this.$Modal.error({
            title: '提示',
            content: res.data.message
          })
        }
      })
    },
    handleConfirmClick () {
      window.opener = null
      window.open('about:blank', '_top').close()
    },
    handleRedirect () {
      this.$router.push({
        name: 'login'
      })
    }
  }
}
</script>
