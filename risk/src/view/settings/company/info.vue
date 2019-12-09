<style lang="less">
@import "./company.less";
</style>

<template>
  <div>
    <Card :bordered="false">
      <div class="top-view">
        <div class="top-view-label">{{firstText}}</div>
        <div class="top-view-title">
          <label>{{companyName}}</label>
          <p>ID：{{companyId}} <span v-clipboard="clipOptions">复制</span></p>
        </div>
      </div>

      <div class="cards-view">

        <router-link to="/settings/team">
          <div class="cards-view-item">
            <div class="title">公司成员</div>
            <div class="content">
              <img src="@/assets/images/count.png">
              <label><span class="workCount">{{memberWorkCount}}</span><span class="memberCount">/{{memberCount}}人</span></label>
            </div>
          </div>
        </router-link>

        <div class="cards-view-item">
          <div class="title">系统版本</div>
          <div class="content">
            <img src="@/assets/images/version.png">
            <div style="margin-left: 20px;margin-top: 10px;">
              <p v-for="item in systemVersionList"
                 :key="item">{{item}} </p>
            </div>

          </div>
        </div>

        <router-link to="/settings/company/real">
          <div class="cards-view-item">
            <div class="title">领筑平台企业认证</div>
            <div class="content">
              <img src="@/assets/images/logo-min.png">
              <label v-if="jdVerified">已认证</label>
              <label v-else
                     style="color: #EA4545">未认证</label>
            </div>
          </div>
        </router-link>

        <div class="cards-view-item">
          <div class="title">e签宝企业注册</div>
          <div class="content">
            <img src="@/assets/images/c1.png">
            <label v-if="esignVerified">已认证</label>
            <label v-else
                   style="color: #EA4545">未认证</label>
          </div>
        </div>
      </div>

      <div class="cards-view">
        <router-link to="/settings/banks/pay">
          <div class="cards-view-item">
            <div class="title">收款账户</div>
            <div class="info"></div>
            <div class="content">
              <img src="@/assets/images/payBank.png">
              <label v-if="isRepayAccount"
                     style="color: #40CC96;">已设置</label>
              <label v-else
                     style="color: #EA4545">未设置</label>
            </div>
          </div>
        </router-link>

        <router-link to="/settings/banks/receipt">
          <div class="cards-view-item">
            <div class="title">收票账户</div>
            <div class="info"></div>
            <div class="content">
              <img src="@/assets/images/receiptBank.png">
              <label v-if="isReceiptAccount"
                     style="color: #40CC96;">已设置</label>
              <label v-else
                     style="color: #EA4545">未设置</label>
            </div>
          </div>
        </router-link>
      </div>

    </Card>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  name: 'company-info',
  data () {
    return {
      memberCount: 0,
      memberWorkCount: 0,
      jdVerified: 0,
      esignVerified: 0,
      isReceiptAccount: 0,
      isRepayAccount: 0,
      companyId: '',
      systemVersionList: []
    }
  },
  computed: {
    ...mapState({
      companyName: state => state.user.userName.slice(0, state.user.userName.indexOf('-')),
      firstText: state => state.user.userName.slice(0, 1)
    }),
    clipOptions () {
      return {
        value: this.companyId,
        success: (e) => {
          this.$Message.success('复制成功')
        },
        error: () => {
          this.$Message.error('复制失败')
        }
      }
    }
  },
  mounted () {
    this.getCompanyInfo().then(res => {
      if (res && res.status === 200 && res.data.code === 10000) {
        this.memberCount = res.data.data.memberCount
        this.memberWorkCount = res.data.data.memberWorkCount
        this.jdVerified = res.data.data.jdVerified
        this.esignVerified = res.data.data.esignVerified
        this.isReceiptAccount = res.data.data.isReceiptAccount
        this.isRepayAccount = res.data.data.isRepayAccount
        this.companyId = res.data.data.companyId
        this.systemVersionList = res.data.data.systemVersionList
      }
    })
  },
  methods: {
    ...mapActions([
      'getCompanyInfo'
    ])
  }
}
</script>
