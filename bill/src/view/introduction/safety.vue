<style lang="less">
@import "../index/index.less";
@import "./introduction.less";
</style>

<template>
  <div class="index-page"
       ref="page">
    <Header :isMin="true" />
    <div class="introduction">
      <div class="introduction-banner"></div>
      <div class="introduction-main">
        <Left active="safety" />
        <div class="introduction-main-right">
          <div class="title">资金安全</div>
          <div class="content">
            <p>买方先把资金转入买方在京东钱包的账户并冻结, 卖方背书、买方确认背书签收后，京东钱包将买方钱包资金解冻并划拨至卖方钱包账户，再自动提现到卖方绑定的结算账户中，此过程解决了买卖双方交易过程中不信任问题</p>
            <div class="cell">
              <img src="@/assets/images/front/ai5.png">
              <div class="cell-text">
                <b>担保交易</b>
                <span>平台引进第三方资金监管账户-京东钱包，该账户为用户的实名，归用户自己管理，以此解决了线上票据流转风险，保障在线票据和资金流转安全。</span>
              </div>
            </div>
          </div>

          <div class="title">交易安全</div>
          <div class="content">
            <div class="cell">
              <img src="@/assets/images/front/ai10.png">
              <div class="cell-text">
                <b>实名认证</b>
                <span>在票据交易前，用户必须通过企业工商信息、企业法人信息、对公账户打款等多项基础信息的严密认证，并通过平台人工审核，以保障用户资料真实、准确、完整。</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai9.png">
              <div class="cell-text">
                <b>电子交易合同</b>
                <span>买方报价卖方接受报价后，确定交易，需签订电子合同。若用户发生法律纠纷，可申请出具权威第三方电子证据报告，或申请公证处出具公证书或司法鉴定中心出具联合证据报告。</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai13.png">
              <div class="cell-text">
                <b>背书状态验证</b>
                <span>平台对接了ECDS通道，票据签收状态实时反馈，最大限度的避开票据流转的风险，并给用户带来便捷的操作体验。</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai12.png">
              <div class="cell-text">
                <b>成交信用评级</b>
                <span>系统从发布票据真伪、背书时长、支付时长、签收时长、无故撤销次数等多个维度评估用户的历史成交信用，对于信用较低者可能会影响其在平台的交易频次。</span>
              </div>
            </div>

          </div>

          <div class="title">系统安全</div>
          <div class="content">
            <div class="cell">
              <img src="@/assets/images/front/ai7.png">
              <div class="cell-text">
                <b>SSL加密</b>
                <span>平台所有访问数据都通过SSL加密技术处理，基于证书利用数字签名方法对服务器和客户端进行身份验证, 利用对称密钥算法对传输的数据进行自动加密, 消息传输过程中使用MAC算法来检验消息的完整性。</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai6.png">
              <div class="cell-text">
                <b>云防火墙</b>
                <span>阿里云安全防火墙通过采用SDN技术、集群部署模式、双Available Zone （AZ，可用区）部署，统一管理互联网与业务间（南北向）、业务和业务间（东西向）的隔离以及访问控制，并内置了威胁入侵检测模块（IPS），全面保护网络安全。</span>
              </div>
            </div>
            <div class="cell">
              <img src="@/assets/images/front/ai8.png">
              <div class="cell-text">
                <b>数据加密</b>
                <span>通过加密算法和加密密钥将明文转变为密文，利用密码技术对信息进行加密，实现信息隐蔽，提高信息系统和数据的安全性和保密性</span>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
    <Footer />
    <guide-box container=".index-page" />
  </div>
</template>
<script>
import Header from '_c/header'
import Footer from '_c/footer'
import GuideBox from '_c/guide-box'
import Left from './left'

export default {
  name: 'safety',
  components: {
    Header,
    Footer,
    GuideBox,
    Left
  }
}
</script>
