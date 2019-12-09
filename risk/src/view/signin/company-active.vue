<template>
  <div class="signin">
    <Modal v-model="expiredModal" width="360" :closable="false" :mask-closable="false">
      <p slot="header" style="color:#f60;text-align:left">
        <Icon type="ios-information-circle"></Icon>
        <span>信息提示</span>
      </p>
      <div style="text-align:left">
        <p>此链接已过期。链接的有效期是5天。</p>
        <P>请<a @click="handleSendCompanyActiveEmail">点击这里</a>重新发送邮件。。</P>
        <a @click="handleRedirect">返回首页</a>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long :loading="modal_loading" @click="handleConfirmClick">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'company-active',
  data () {
    return {
      companyAuditId: -1,
      expiredModal: false,
      modal_loading: false
    }
  },
  mounted () {
    const query = this.$route.query
    if (query && query.param) {
      const params = decodeURI(query.param).split(',')
      this.companyAuditId = params[0]
      if (!params[1] || Date.parse(new Date()) > params[1]) {
        this.expiredModal = true
      } else {
        this.handleCompanyActive()
      }
    }
  },
  methods: {
    ...mapActions([
      'companyActive',
      'sendCompanyActiveEmail'
    ]),
    handleCompanyActive () {
      this.companyActive({ companyAuditId: this.companyAuditId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          const title = '激活通知'
          const content = '<p>激活成功</p>'
          this.$Modal.info({
            title: title,
            content: content,
            onOk: () => {
              this.$router.push({
                name: 'login'
              })
            }
          })
        }
      })
    },
    handleSendCompanyActiveEmail () {
      this.expiredModal = false
      this.sendCompanyActiveEmail({ companyAuditId: this.companyAuditId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          const title = '发送邮件通知'
          const content = '<p>已向你的邮箱发送邮件</p>'
          this.$Modal.info({
            title: title,
            content: content,
            onOk: () => {
              window.opener = null
              window.open('about:blank', '_top').close()
            }
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
