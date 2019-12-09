<style lang="less" scoped>
.main {
  padding: 20px;
  width: 100%;
  height: 100%;
  background: #fff;
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
          width: 130px;
          word-break: break-all;
          text-align: center;
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
    <div class="title"><span>询价信息</span></div>
    <div class="content">
      <div class="content-left">
        <div class="content-left-item">
          <label>询价时间：</label>
          <span>{{form.createTime}}</span>
        </div>
        <div class="content-left-item">
          <label>报价截止：</label>
          <span>{{form.expirationDate}}</span>
        </div>
        <div class="content-left-item">
          <label>询价状态：</label>
          <span>{{form.statusCH}}</span>
        </div>
      </div>
      <div class="content-right">
        <!-- <Button type="primary"
                @click="handleViewQuotation(form.id)">查看报价（{{form.quotationCount}}）</Button> -->
        <span style="line-height: 40px;">距离报价结束还剩{{lastTime}}</span>
      </div>
    </div>

    <div class="title"><span>询价信息</span></div>
    <div class="content">
      <div class="content-left"
           style="border:1px solid #DDDDDD">
        <div class="table-title">
          <span style="flex: 1">承兑人</span><span style="flex: 1">票据号码</span><span>票面金额(元)</span><span>到期日期</span>
          <span>剩余天数(当前)</span><span>背书次数</span><span style="flex: 1">票据瑕疵</span>
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
        <template v-for="item in form.fileList">
          <img v-if="item.fileType === 'ticket_front'"
               :key="'img'+ item.id"
               :src="item.viewFileUrl"
               style="max-width: 80%" /></template>
      </div>
    </div>

    <div class="title"><span>商票背面</span></div>
    <div class="content">
      <div class="content-left">
        <template v-for="item in form.fileList">
          <img v-if="item.fileType === 'ticket_back'"
               :key="'img'+ item.id"
               :src="item.viewFileUrl"
               style="max-width: 80%" />
        </template>
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
        <template v-for="item in form.fileList">
          <div v-if="item.fileType === 'trade_data'"
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

    <div class="title"><span>联系方式</span></div>
    <div class="content">
      <div class="content-left">
        <div class="content-left-item">
          <label>公司名称：</label>
          <span>{{form.companyName}}</span>
        </div>
        <div class="content-left-item">
          <label>联系人：</label>
          <span>{{form.contactPerson}}</span>
        </div>
        <div class="content-left-item">
          <label>联系电话：</label>
          <span>{{form.contactPhone}}</span>
        </div>
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
import { getHandledValue, absMoney } from '@/libs/tools'

export default {
  name: 'inquiredetail',
  data () {
    return {
      absMoney: absMoney,
      visible: false,
      viewUrl: '',
      lastTime: '00天00时00分00秒',
      interval: null,
      form: null
    }
  },
  mounted () {
    const { id } = this.$route.query
    if (id) {
      const data = {
        id
      }
      this.getInquireDetail(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.form = res.data.data
          this.countTime()
        }
      })
    }
  },
  destroyed () {
    this.interval = null
  },
  methods: {
    ...mapActions([
      'getInquireDetail'
    ]),
    countTime () {
      this.interval = setInterval(() => {
        let str = ''
        const funtureDate = new Date(this.form.expirationDate.replace(new RegExp(/-/gm), '/'))
        // 3.获取现在的时间
        let presentDate = new Date()
        // 4.获取时间戳
        let funtureTime = funtureDate.getTime()
        let presenTime = presentDate.getTime()
        // 5.获取剩余的时间戳
        let allTime = funtureTime - presenTime
        if (allTime > 0) {
          // 6.把毫秒转换为秒
          let allSecond = parseInt(allTime / 1000)
          // 7.获取剩余多少天
          let day = getHandledValue(parseInt(allSecond / 3600 / 24))
          // 8.获取剩余多少小时
          let hour = getHandledValue(parseInt(allSecond / 3600 % 24))
          // 9.获取剩余多少分钟
          let minute = getHandledValue(parseInt(allSecond / 60 % 60))
          // 10.获取剩余多少秒
          let second = getHandledValue(parseInt(allSecond % 60))
          str += day + '天'
          str += hour + '时'
          str += minute + '分'
          str += second + '秒'
          this.lastTime = str
          if (allSecond < 0) {
            clearInterval(this.interval)
          }
        } else {
          clearInterval(this.interval)
        }
      }, 1000)
    },
    handleViewQuotation (id) {
      const route = {
        name: 'quotationlist',
        query: {
          inquireId: id
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
