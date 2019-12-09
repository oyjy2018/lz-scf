<style lang="less">
  @import "../../index/index.less";
</style>

<template>
  <div style="padding-left: 20px;padding-top: 20px;">
    <H3 style="margin-left: 30px">{{title}}</H3>
    <Form ref="form"
          :model="form"
          :rules="ruleValidate"
          style="margin-top: 20px"
          :label-width="160">
      <FormItem label="Banner名称：" prop="bannerName">
        <Input v-model="form.bannerName"
               style="width: 400px"
               :maxlength="50"
               @on-keyup="trimSpace()"></Input>
      </FormItem>
      <FormItem label="图片：" prop="bannerFileUrl">
        <Upload :action="upUrl"
                v-model="form.bannerFileUrl"
                accept=".png,.jpg,.jpeg,.WebP"
                :format="['png','jpg','jpeg','WebP']"
                :max-size="3072"
                :show-upload-list="false"
                :on-exceeded-size="(file) => handleMaxSize(file, 3072)"
                :on-success="(res, file) => uploadSuccess(res, file)"
                style="float: left">
          <a>上传</a>
        </Upload>
        <viewer>
          <div class="detailed">
            <img class="viewerImg"
                 id="viewerImgId"
                 style="width: 20px;height: 20px;float: left;margin-left: 10px;margin-top: 5px;display: none"
                 alt="">
          </div>
        </viewer>
        <span style="margin-left: 10px;float: left">请上传1920*550、小于3M的图片，支持格式：jpg/jpeg/png/WebP</span>
      </FormItem>
      <FormItem label="启用状态：" prop="useStatus">
        <RadioGroup v-model="form.useStatus">
          <Radio label="1">启用</Radio>
          <Radio label="0">禁用</Radio>
        </RadioGroup>
      </FormItem>
      <FormItem label="点击Banner后跳转链接：">
        <Input v-model="form.jumpUrl"
               :maxlength="500"
               style="width: 400px"
               @on-keyup="trimSpace()"></Input>
      </FormItem>
      <FormItem>
            <Button type="primary"
                    :loading="loading"
                    @click="handleSubmit('form')">{{btnName}}
            </Button>
            <Button type="primary"
                    style="margin-left: 10px"
                    :loading="loading"
                    @click="goToBannerList">取消
            </Button>
      </FormItem>
    </Form>
  </div>
</template>
<script>
import config from '@/config'
import { mapState, mapActions } from 'vuex'
import { trimSpace } from '@/libs/tools'
const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'index',
  data () {
    return {
      title: '添加Banner',
      btnName: '确认新增',
      upUrl: upUrl + 'file/upload',
      loading: false,
      isUploading: false,
      form: {
        id: 0,
        bannerName: '',
        bannerFileUrl: '',
        useStatus: '1',
        jumpUrl: ''
      },
      ruleValidate: {
        bannerName: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        useStatus: [
          { required: true, message: '请选择启用状态', trigger: 'blur' }
        ],
        bannerFileUrl: [
          { required: true, message: ' ', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      token: state => state.user.token
    })
  },
  mounted () {
    this.form.id = this.$route.query.id
    if (this.form.id && this.form.id !== 0) {
      this.getDetail()
    }
  },
  methods: {
    ...mapActions([
      'addBanner',
      'bannerDetail',
      'updateBanner'
    ]),
    getDetail () {
      this.bannerDetail(this.form).then(res => {
        this.loading = false
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.form.bannerName = res.data.data.bannerName
          this.form.bannerFileUrl = res.data.data.bannerFileUrl
          this.form.useStatus = res.data.data.useStatus + ''
          this.form.jumpUrl = res.data.data.jumpUrl
          document.getElementById('viewerImgId').src = res.data.data.bannerFileViewUrl
          document.getElementById('viewerImgId').style.display = 'inline'
          this.title = '修改Banner'
          this.btnName = '保存修改'
        }
      })
    },
    // 文件上传成功后的处理
    uploadSuccess (res, file) {
      this.isUploading = false
      this.$Message.success({
        content: '文件上传成功',
        duration: 5,
        top: 300
      })
      if (res && res.code === 10000) {
        this.form.bannerFileUrl = res.data.fileUrl
        document.getElementById('viewerImgId').src = res.data.viewFileUrl
        document.getElementById('viewerImgId').style.display = 'inline'
      }
    },
    // 文件超过最大限制提示
    handleMaxSize (file, size) {
      this.isUploading = false
      this.$Message.warning({
        content: file.name + ' 大于' + size / 1024 + 'M'
      })
    },
    // banner名称去空格处理
    trimSpace () {
      this.form.bannerName = trimSpace(this.form.bannerName)
      this.form.jumpUrl = trimSpace(this.form.jumpUrl
      )
    },
    // 提交操作
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.loading = true
          // 修改
          if (this.form.id && this.form.id !== 0) {
            this.updateBanner(this.form).then(res => {
              this.loading = false
              if (res && res.status === 200 && res.data && res.data.code === 10000) {
                this.$Message.success({
                  content: '修改成功'
                })
                this.goToBannerList()
              }
            })
          } else { // 新增
            this.addBanner(this.form).then(res => {
              this.loading = false
              if (res && res.status === 200 && res.data && res.data.code === 10000) {
                this.$Message.success({
                  content: '添加成功'
                })
                this.goToBannerList()
              }
            })
          }
        } else {
          this.$Message.error({
            content: '请完成所有带*的必填项',
            duration: 5,
            closable: true
          })
        }
      })
    },
    goToBannerList () {
      const route = {
        name: 'bannerList'
      }
      this.$router.push(route)
    }
  }
}
</script>
