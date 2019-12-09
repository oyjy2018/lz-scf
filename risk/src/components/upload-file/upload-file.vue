<template>
  <div>
    <span style="color: #999">最多上传{{sitem.uploadLimit}}张，每个文件大小不超过{{sitem.onceUploadSize}}M</span>
    <Upload ref="upload"
            :multiple="multiple"
            :max-size="sitem.onceUploadSize * 1024"
            :accept="accept"
            :format="format"
            :default-file-list="defaultList"
            :on-success="handleSuccess"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-format-error="handleFormatError"
            :on-exceeded-size="handleMaxSize"
            :before-upload="handleBeforeUpload"
            type="drag"
            :action="action">
      <div style="padding: 20px 0">
        <Icon type="ios-cloud-upload"
              size="52"
              style="color: #3399ff"></Icon>
        <p>添加 | 拖曳上传 </p>
      </div>
    </Upload>
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
export default {
  name: 'UploadFile',
  data () {
    return {
      visible: false,
      viewUrl: '',
      uploadList: []
    }
  },
  props: {
    multiple: {
      type: Boolean,
      default: false
    },
    accept: {
      type: String,
      default: '.png,.jpg,.jpeg'
    },
    format: {
      type: Array,
      default () {
        return ['png', 'jpg', 'jpeg']
      }
    },
    defaultList: {
      type: Array,
      default () {
        return []
      }
    },
    action: {
      type: String,
      default: ''
    },
    sitem: {
      type: Object,
      defualt () {
        return {
          uploadLimit: 1,
          onceUploadSize: 1,
          fileCode: ''
        }
      }
    }
  },
  methods: {
    handlePreview (file) {
      if (file && file.percentage === 100 && (file.response.data.fileType === 'image' || file.response.data.fileType === 'pdf')) {
        this.visible = true
        this.viewUrl = file.response.data.viewFileUrl
      } else {
        this.$Notice.warning({
          title: '提示',
          desc: '该文件类型暂不能预览'
        })
      }
    },
    handleRemove (file) {
      // const fileList = this.$refs.upload.fileList
      // this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
      this.$emit('on-remove', {
        file,
        sitem: this.sitem
      })
    },
    handleSuccess (res, file) {
      this.$emit('on-success', {
        res,
        sitem: this.sitem
      })
    },
    handleFormatError (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '文件类型错误'
      })
    },
    handleMaxSize (file) {
      this.$Notice.warning({
        title: '提示',
        desc: `文件大于${this.sitem.onceUploadSize}M`
      })
    },
    handleBeforeUpload () {
      const check = this.uploadList.length < this.sitem.uploadLimit
      if (!check) {
        this.$Notice.warning({
          title: `最多上传${this.sitem.uploadLimit}个文件`
        })
      }
      return check
    }
  },
  mounted () {
    this.uploadList = this.$refs.upload.fileList
  }
}
</script>
