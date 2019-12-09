<style lang="less" scoped>
.main {
  padding: 20px;
  width: 100%;
  height: 100%;
  .title {
    width: 100%;
    height: 38px;
    line-height: 38px;
    font-size: 16px;
    border-bottom: 1px dashed #979797;
    span {
      border-left: 2px solid #2c8ef3;
      padding-left: 5px;
    }
  }
  .content {
    margin-bottom: 30px;
    display: flex;
    justify-content: space-between;
    &-left {
      flex: 1;
      margin-top: 10px;
      &-item {
        font-size: 14px;
        line-height: 34px;
      }
      &-item-flex {
        display: flex;
        justify-content: space-between;
        div {
          flex: 1;
        }
      }
      .table-title,
      .table-value {
        display: flex;
        justify-content: space-around;
        height: 44px;
        line-height: 44px;
        background: #ecf0f6;
        span {
          width: 140px;
          text-align: center;
          word-break: break-all;
        }
      }
      .table-value {
        background: #fff;
        height: auto;
      }
    }
    &-right {
      width: 300px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
  }
  .file-view {
    display: flex;
    flex-direction: row;
    margin-bottom: 10px;
    width: 80%;
    .file-name {
      flex: 1;
      -o-text-overflow: ellipsis;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 1;
      overflow: hidden;
      white-space: normal;
      word-break: break-all;
      cursor: pointer;
      &:hover {
        color: #2c8ef3;
      }
    }
    .file-icon {
      width: 20px;
      margin-right: 10px;
      cursor: pointer;
      color: #999;
      &:hover {
        color: #2c8ef3;
      }
    }
  }
}
</style>

<template>
  <div class="main"
       v-if="form">
    <div class="title titleS"> <a href="javascript:;"></a><span>订单</span></div>
    <div class="content">
      <div class="content-left">
        <div class="content-left-item">
          <label>创建时间：</label>
          <span>{{form.createTime}}</span>
        </div>
        <div class="content-left-item content-left-item-flex">
          <div>
            <label>卖方（票方）：</label>
            <span>{{form.inquireCompanyName}}</span>
          </div>
          <div>
            <label>联系人：</label>
            <span>{{form.publishPerson}}</span>
          </div>
          <div>
            <label>联系电话：</label>
            <span>{{form.publishPersonPhone}}</span>
          </div>
        </div>
        <div class="content-left-item content-left-item-flex">
          <div><label>买方（资方）：</label>
            <span>{{form.quotationCompanyName}}</span></div>
          <div><label>联系人：</label>
            <span>{{form.quotationPerson}}</span></div>
          <div><label>联系电话：</label>
            <span>{{form.quotationPersonPhone}}</span></div>
        </div>
        <div class="content-left-item content-left-item-flex">
          <div><label>交易价：</label>
            <span>{{form.amount}}</span></div>
          <div><label>年化率：</label>
            <span>{{form.annualRate}}</span></div>
          <div><label>每10万扣款：</label>
            <span>{{form.discountFee}}</span></div>
        </div>
        <div class="content-left-item content-left-item-flex">
          <div><label>订单状态：</label>
            <span>{{form.orderStatusCH}}</span></div>
          <div><a @click="handleViewInquireDetail">查看询价单详情
              <Icon type="ios-arrow-forward" /></a></div>
          <div><a @click="handleViewQuotationDetail">查看报价单详情
              <Icon type="ios-arrow-forward" /></a></div>
        </div>
      </div>
    </div>

    <div class="title titleS"><a href="javascript:;"></a><span>订单票据</span></div>
    <div class="content">
      <div class="content-left"
           style="border:1px solid #DDDDDD">
        <div class="table-title">
          <span style="flex: 1">承兑人</span><span style="flex: 1">票据号码</span><span>票面金额(元)</span><span>到期日期</span>
          <span>剩余天数(生成订单时)</span><span>背书次数</span><span style="flex: 1">票据瑕疵</span>
        </div>
        <div class="table-value">
          <span style="flex: 1">{{form.accepterName}}</span><span style="flex: 1">{{form.billNo}}</span><span>{{absMoney(form.billAmt)}}</span><span>{{form.maturityDate}}</span>
          <span>{{form.surplusValidDays}}</span><span>{{form.endorsedCount}}</span><span style="flex: 1">{{form.flaws}}</span>
        </div>
      </div>
    </div>

    <div class="title"><span>商票正面</span></div>
    <div class="content">
      <div class="content-left">
        <template v-for="item in form.ticketUrlList">
          <img v-if="item.fileType === 'ticket_front'"
               :key="'img'+ item.id"
               :src="item.viewFileUrl"
               style="max-width: 80%"></template>
      </div>
    </div>

    <div class="title"><span>商票背面</span></div>
    <div class="content">
      <div class="content-left">
        <template v-for="item in form.ticketUrlList">
          <img v-if="item.fileType === 'ticket_back'"
               :key="'img'+ item.id"
               :src="item.viewFileUrl"
               style="max-width: 80%"></template>
      </div>
    </div>

    <div class="title"><span>采购合同</span></div>
    <div class="content">
      <div class="content-left">
        <template v-for="item in form.fileList">
          <div v-if="item.fileType === 'purchase_contract'"
               :key="'img'+ item.id">
            <img v-if="item.suffix !== '.pdf'"
                 :src="item.viewFileUrl"
                 style="max-width: 80%" />
            <div v-else
                 class="file-view">
              <div style="width: 50px;">
                <Icon class="file-icon"
                      size="36"
                      color="#666"
                      type="md-document" />
              </div>
              <div style="flex: 1">
                <span class="file-name"
                      @click="handleFile(item)">{{item.originalFileName}}</span>
                <Icon class="file-icon"
                      size="20"
                      @click="handleFile(item)"
                      type="md-eye" />
                <Icon class="file-icon"
                      size="20"
                      @click="handleDown(item)"
                      type="md-download" />
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="title"><span>采购发票</span></div>
    <div class="content">
      <div class="content-left">
        <template v-for="item in form.fileList">
          <div v-if="item.fileType === 'purchase_invoice'"
               :key="'img'+ item.id">
            <img v-if="item.suffix !== '.pdf'"
                 :src="item.viewFileUrl"
                 style="max-width: 80%" />
            <div v-else
                 class="file-view">
              <div style="width: 50px;">
                <Icon class="file-icon"
                      size="36"
                      color="#666"
                      type="md-document" />
              </div>
              <div style="flex: 1">
                <span class="file-name"
                      @click="handleFile(item)">{{item.originalFileName}}</span>
                <Icon class="file-icon"
                      size="20"
                      @click="handleFile(item)"
                      type="md-eye" />
                <Icon class="file-icon"
                      size="20"
                      @click="handleDown(item)"
                      type="md-download" />
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="title"><span>商票交易背景资料</span></div>
    <div class="content">
      <div class="content-left">
        <template v-for="item in form.ticketUrlList">
          <div v-if="item.fileType === 'trade_data'"
               :key="'img'+ item.id">
            <img v-if="item.suffix !== '.pdf'"
                 :src="item.viewFileUrl"
                 style="max-width: 80%">
            <!-- <a :href="item.viewFileUrl">{{item.originalFileName}}</a> -->
            <div v-else
                 class="file-view">
              <div style="width: 50px;">
                <Icon class="file-icon"
                      size="36"
                      color="#666"
                      type="md-document" />
              </div>
              <div style="flex: 1">
                <span class="file-name"
                      @click="handleFile(item)">{{item.originalFileName}}</span>
                <Icon class="file-icon"
                      size="20"
                      @click="handleFile(item)"
                      type="md-eye" />
                <Icon class="file-icon"
                      size="20"
                      @click="handleDown(item)"
                      type="md-download" />
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <Modal v-model="visible"
           width="800px"
           :footer-hide="true"
           title="查看文件">
      <iframe :src="viewUrl"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
    </Modal>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'orderdetail',
  data () {
    return {
      absMoney: absMoney,
      visible: false,
      viewUrl: '',
      form: null
    }
  },
  mounted () {
    const { orderId } = this.$route.query
    if (orderId) {
      const data = {
        orderId
      }
      this.findOrderDetails(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.form = res.data.data
        }
      })
    }
  },
  methods: {
    ...mapActions([
      'findOrderDetails'
    ]),
    handleViewInquireDetail () {
      const route = {
        name: 'inquiredetail',
        query: {
          id: this.form.inquireId
        }
      }
      this.$router.push(route)
    },
    handleViewQuotationDetail () {
      const route = {
        name: 'sellquotationdetail',
        query: {
          quotationId: this.form.quotationId
        }
      }
      this.$router.push(route)
    },
    handleFile (file) {
      this.visible = true
      this.viewUrl = file.viewFileUrl
    },
    handleDown (file) {
      window.location.href = file.downloadFileUrl
    }
  }
}
</script>
