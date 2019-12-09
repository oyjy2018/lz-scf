<style lang="less" scoped>
@import "../../../index.less";
.title {
  border-left: 3px solid #2c8ef3;
  padding-left: 10px;
  margin-bottom: 20px;
}
</style>
<template>
  <Card :border="false"
        style="min-height: 500px;">
    <h3 class="title">导入授信</h3>
    <div v-if="errorCount === 0">
      <div>
        <label>第一步：</label>
        <a href="../file/导入授信记录模板.xlsx"
           style="line-height: 50px;">下载excel模板</a><span style="font-size: 12px;color: #999">(导入文件中，需设置“身份证号”的格式为：文本；)</span>
      </div>
      <div>
        <label>第二步：</label>
        <span>上传文件</span>
        <span style="font-size: 12px;color: #999">(导入记录个数需小于100，所有允许导入的需求字段请参考模版；需求字段不符合规则，整条需求不予以导入)</span>
      </div>
      <div>
        <Upload ref="upload"
                style="width: 500px; margin: 10px 0"
                :accept="'.xls,.xlsx'"
                :format="['xls','xlsx']"
                :max-size="2048"
                :on-success="handleSuccess"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="handleBeforeUpload"
                :action="upUrl">
          <Button icon="ios-cloud-upload-outline">选择文件</Button>
        </Upload>
      </div>
      <div style="margin: 40px 0;">
        <Button :loading="loading"
                @click="handleSubmit"
                type="primary">下一步</Button>
      </div>
    </div>
    <div v-else>
      <div><span style="color: red">{{errorCount}}</span> 条数据匹配失败，详情请点击<a :href="errorMsgDownloadUrl">文件下载</a></div>
      <div style="margin: 40px 0;">
        <Button :loading="loading"
                @click="handleRest"
                type="primary">重新导入</Button>
      </div>
    </div>
  </Card>
</template>

<script>
import config from '@/config'
import { mapActions } from 'vuex'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'exportxlsx',
  data () {
    return {
      loading: false,
      upUrl: upUrl + 'file/upload',
      uploadList: [],
      fileUrl: '',
      errorCount: 0,
      errorMsgDownloadUrl: ''
    }
  },
  mounted () {
    this.uploadList = this.$refs.upload.fileList
  },
  methods: {
    ...mapActions([
      'importRecord'
    ]),
    handleRemove (file) {
      const fileList = this.$refs.upload.fileList
      this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
    },
    handleSuccess (res, file) {
      if (res && res.code === 10000) {
        file.name = res.data.newFileName
        file.viewFileUrl = res.data.viewFileUrl
        file.fileType = res.data.fileType
        this.fileUrl = res.data.fileUrl
      } else {
        this.$Notice.warning({
          title: '提示',
          desc: res.message
        })
      }
    },
    handleFormatError (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '文件类型错误，请选择xls或者xlsx格式'
      })
    },
    handleMaxSize (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '文件大于2M.'
      })
    },
    handleBeforeUpload () {
      const check = this.uploadList.length < 1
      if (!check) {
        this.$Notice.warning({
          title: '最多上传1个文件'
        })
      }
      return check
    },
    handleRest () {
      this.uploadList = []
      this.fileUrl = ''
      this.errorCount = 0
      this.errorMsgDownloadUrl = ''
    },
    handleSubmit () {
      if (this.fileUrl) {
        this.loading = true
        const data = {
          fileUrl: this.fileUrl
        }
        this.importRecord(data).then(res => {
          this.loading = false
          if (res && res.status === 200 && res.data && res.data.code === 10000) {
            if (res.data.data.errorCount) {
              this.errorMsgDownloadUrl = res.data.data.errorMsgDownloadUrl
              this.errorCount = res.data.data.errorCount
            } else {
              this.$Modal.success({
                title: '提示',
                content: '导入成功'
              })
            }
          }
        })
      } else {
        this.$Notice.warning({
          title: '请上传文件'
        })
      }
    }
  }
}
</script>
