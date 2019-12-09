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
  }
  .content {
    margin-top: 30px;
  }
}
</style>
<template>
  <div class="main">
    <div class="title">请选择您要签署的合同版本</div>
    <div class="content">
      <RadioGroup v-model="contractType"
                  vertical>
        <Radio label="0">
          <span>商业承兑汇票交易合同</span>
          <Button :loading="loading2"
                  @click="getInitContractViewUrlData"
                  style="margin-left: 40px;">预览合同</Button>
        </Radio>
        <Radio label="1">
          <span>上传自己的合同</span>
        </Radio>
        <div v-show="contractType === '1'">
          <Upload ref="upload"
                  accept=".pdf"
                  :format="['pdf']"
                  :max-size="10240"
                  :on-success="handleSuccess"
                  :on-format-error="handleFormatError"
                  :on-exceeded-size="handleMaxSize"
                  :before-upload="handleBeforeUpload"
                  :on-preview="handlePreview"
                  :action="upUrl">
            <Button icon="ios-cloud-upload-outline">选择文件</Button>
            <span style="color:#999;padding-left: 20px;">支持的文件类型为*.pdf，文件最大支持10M。</span>
          </Upload>
        </div>
      </RadioGroup>

      <div style="margin-top: 50px;">
        <Button type="primary"
                @click="submit"
                :loading="loading">下一步</Button>
      </div>
    </div>
    <ticketTransferAgreement ref="agreement"
                             :modal="showModal"
                             party="b"
                             @on-click="handleClose" />
    <Modal v-model="visible1"
           width="800px"
           :footer-hide="true"
           title="查看文件">
      <iframe :src="viewUrl1"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
    </Modal>
    <Modal v-model="visible"
           width="800px"
           :footer-hide="true"
           @on-cancel="cancel"
           title="查看文件">
      <iframe :src="viewUrl"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
    </Modal>
  </div>
</template>
<script>
import config from '@/config'
import { mapActions } from 'vuex'
import ticketTransferAgreement from '../../ticket-transfer-agreement/ticket-transfer-agreement'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'selectcontract',
  components: {
    ticketTransferAgreement
  },
  data () {
    return {
      visible1: false,
      viewUrl1: '',
      visible: false,
      viewUrl: '',
      loading: false,
      loading2: false,
      showModal: false,
      upUrl: upUrl + 'file/upload',
      type: null,
      orderId: null,
      contractType: '',
      fileUrl: '',
      uploadList: [],
      resUrl: ''
    }
  },
  mounted () {
    const { orderId, type } = this.$route.query
    this.orderId = orderId
    this.type = type
    this.uploadList = this.$refs.upload.fileList
  },
  methods: {
    ...mapActions([
      'changeContract',
      'isOnlyKeyWord',
      'getInitContractViewUrl'
    ]),
    handleSuccess (res, file) {
      if (res && res.code === 10000) {
        this.fileUrl = res.data.fileUrl
      }
    },
    handleFormatError (file) {
      this.$Notice.warning({
        title: '文件类型不正确',
        desc: '请选择.pdf文件'
      })
    },
    handleMaxSize (file) {
      this.$Notice.warning({
        title: '文件大小不正确',
        desc: '请选择小于10M的文件.'
      })
    },
    handleBeforeUpload () {
      const check = this.uploadList.length < 1
      if (!check) {
        this.$Notice.warning({
          title: '只能上传一张图片'
        })
      }
      return check
    },
    handlePreview (file) {
      if (file.response.code === 10000) {
        window.open(file.response.data.viewFileUrl, '_blank')
      }
    },
    handleClose (src) {
      this.showModal = false
      if (src) {
        setTimeout(() => {
          this.visible = true
          this.viewUrl = src
        }, 500)
      }
    },
    cancel () {
      this.$router.go(-1)
    },
    getInitContractViewUrlData () {
      this.loading2 = true
      const data = {
        id: this.orderId
      }
      this.getInitContractViewUrl(data).then(res => {
        this.loading2 = false
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.visible1 = true
          this.viewUrl1 = res.data.data
        }
      })
    },
    submit () {
      if (this.contractType === '') {
        return this.$Message.warning({
          content: '请选择您要签署的合同版本',
          duration: 10,
          closable: true
        })
      }

      if (this.contractType === '1' && this.fileUrl === '') {
        return this.$Message.warning({
          content: '请上传自己的合同',
          duration: 10,
          closable: true
        })
      }
      this.loading = true
      const data = {
        orderId: this.orderId,
        contractType: this.contractType,
        fileUrl: this.fileUrl
      }
      this.changeContract(data).then(res => {
        this.loading = false
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.showModal = true
          this.resUrl = res.data.data
          this.$refs.agreement.getContent(this.orderId, this.type, this.contractType, res.data.data)
        }
      })
    }
  }
}
</script>
