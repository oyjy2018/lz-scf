<template>
  <Card :bordered="false">
    <Form class="form"
          style="width: 600px;"
          ref="loginForm"
          :model="form"
          :rules="rules"
          :label-width="150"
          @keydown.enter.native="handleSubmit">
      <FormItem>
        <h3>基础信息</h3>
      </FormItem>
      <FormItem label="公司名称"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入公司名称"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="统一社会信用代码"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入统一社会信用代码"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="法人"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入法人"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="行业类型"
                prop="userName">
        <Row>
          <Col span="12">
          <Select v-model="model1"
                  size="large"
                  placeholder="请选择行业大类">
            <Option v-for="item in cityList"
                    :value="form.userName"
                    :key="item.value">{{ item.label }}</Option>
          </Select>
          </Col>
          <Col span="12"
               offset="0">
          <Select v-model="model1"
                  size="large"
                  placeholder="请选择具体行业">
            <Option v-for="item in cityList"
                    :value="form.userName"
                    :key="item.value">{{ item.label }}</Option>
          </Select>
          </Col>
        </Row>
      </FormItem>
      <FormItem label="公司规模"
                prop="">
        <Select v-model="model1"
                size="large"
                placeholder="请选择公司规模">
          <Option v-for="item in cityList"
                  :value="form.userName"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
      </FormItem>
      <FormItem label="公司地址"
                prop="userName">
        <Row>
          <Col span="7">
          <Select v-model="model1"
                  size="large"
                  placeholder="请选择省">
            <Option v-for="item in cityList"
                    :value="form.userName"
                    :key="item.value">{{ item.label }}</Option>
          </Select>
          </Col>
          <Col span="7"
               offset="1">
          <Select v-model="model1"
                  size="large"
                  placeholder="请选择市">
            <Option v-for="item in cityList"
                    :value="form.userName"
                    :key="item.value">{{ item.label }}</Option>
          </Select>
          </Col>
          <Col span="7"
               offset="1">
          <Select v-model="model1"
                  size="large"
                  placeholder="请选择区">
            <Option v-for="item in cityList"
                    :value="form.userName"
                    :key="item.value">{{ item.label }}</Option>
          </Select>
          </Col>
        </Row>
      </FormItem>
      <FormItem label=""
                prop="userName">
        <Input v-model="form.userName"
               placeholder="详细地址"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="联系人"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入联系人"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="联系人手机号"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入联系人手机号"
               :maxlength="11"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem prop="userName">
        <h3>公司详情</h3>
      </FormItem>
      <FormItem label="绑定的系统版本"
                prop="">
        <Select v-model="model1"
                size="large"
                placeholder="请选择绑定的系统版本">
          <Option v-for="item in cityList"
                  :value="form.userName"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
      </FormItem>
      <FormItem label="可申请的金融产品"
                prop="">
        <Select v-model="model1"
                size="large"
                placeholder="请选择可申请的金融产品（多选）">
          <Option v-for="item in cityList"
                  :value="form.userName"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
      </FormItem>
      <FormItem label="公司营业执照">
        <div class="upload-items">
          <div class="upload-list"
               v-for="(item, index) in uploadList"
               :key="item.name">
            <template v-if="item.status === 'finished'">
              <img :src="item.url">
              <div class="upload-list-cover">
                <Icon type="ios-eye-outline"
                      @click.native="handleView(index)"></Icon>
                <Icon type="ios-trash-outline"
                      @click.native="handleRemove(item)"></Icon>
              </div>
            </template>
            <template v-else>
              <Progress v-if="item.showProgress"
                        :percent="item.percentage"
                        hide-info></Progress>
            </template>
          </div>
          <Upload ref="upload"
                  :show-upload-list="false"
                  :on-success="handleSuccess"
                  accept=".doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.bmp,.txt,.rar,.zip,.pdf"
                  :format="['doc','docx','xls','xlsx','ppt','pptx','png','jpg','jpeg','gif','bmp','txt','rar','zip','pdf']"
                  :max-size="5120"
                  :multiple="false"
                  :on-format-error="handleFormatError"
                  :on-exceeded-size="handleMaxSize"
                  :before-upload="handleBeforeUpload"
                  type="drag"
                  :action="upUrl"
                  style="display: inline-block;width:100px;">
            <div class="upload-add">
              <Icon type="ios-add"
                    size="40"></Icon>
              <span>上传营业执照</span>
            </div>
          </Upload>
          <Modal title="View Image"
                 v-model="visible">
            <img :src="uploadList[indexImg].url"
                 v-if="visible"
                 style="width: 100%">
          </Modal>
        </div>
      </FormItem>
      <FormItem label="电话"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入电话"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="邮箱"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入邮箱"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="网址"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入网址"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="传真"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入传真"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="公司介绍"
                prop="userName">
        <Input v-model="form.userName"
               :autosize="{minRows: 2,maxRows: 10}"
               placeholder="请输入公司介绍"
               size="large"
               type="textarea"
               style="text-algin: center" />
      </FormItem>
      <FormItem>
        <h3>财务信息</h3>
      </FormItem>
      <FormItem label="企业税号"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入企业税号"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="发票抬头"
                prop="userName">
        <Input v-model="form.userName"
               placeholder="请输入发票抬头"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="开户银行"
                prop="">
        <Select v-model="model1"
                size="large"
                placeholder="请选择开户银行">
          <Option v-for="item in cityList"
                  :value="form.userName"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
      </FormItem>
      <FormItem label=""
                prop="">
        <Input v-model="form.userName"
               placeholder="请输入开户银行支行"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="银行账号"
                prop="">
        <Input v-model="form.userName"
               placeholder="请输入银行账号"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="商票收票账号银行"
                prop="">
        <Select v-model="model1"
                size="large"
                placeholder="请选择商票收票账号银行">
          <Option v-for="item in cityList"
                  :value="form.userName"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
      </FormItem>
      <FormItem label=""
                prop="">
        <Input v-model="form.userName"
               placeholder="请输入商票收票银行支行"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="商票收票银行账号"
                prop="">
        <Input v-model="form.userName"
               placeholder="请输入商票收票银行账号"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem>
        <Button @click="handleSubmit"
                type="primary"
                long
                style="padding: 10px 15px;font-size: 18px;">提交</Button>
      </FormItem>
    </Form>
  </card>
</template>

<script>
import config from '@/config'
const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'company-add',
  props: {
    userNameRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '账号不能为空', trigger: 'blur' }
        ]
      }
    },
    passwordRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  data () {
    return {
      upUrl: upUrl + 'file/upload',
      form: {
        usinessLicenceUrl: '',
        userName: '',
        password: ''
      },
      cityList: [
        {
          value: 'New York',
          label: 'New York'
        },
        {
          value: 'London',
          label: 'London'
        },
        {
          value: 'Sydney',
          label: 'Sydney'
        },
        {
          value: 'Ottawa',
          label: 'Ottawa'
        },
        {
          value: 'Paris',
          label: 'Paris'
        },
        {
          value: 'Canberra',
          label: 'Canberra'
        }
      ],
      model1: '',
      indexImg: '',
      visible: false,
      uploadList: []
    }
  },
  computed: {
    rules () {
      return {
        userName: this.userNameRules,
        password: this.passwordRules
      }
    }
  },
  mounted () {
    this.uploadList = this.$refs.upload.fileList
  },
  methods: {
    handleSubmit () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$emit('on-success-valid', {
            userName: this.form.userName,
            password: this.form.password
          })
        }
      })
    },
    handleView (indexImg) {
      this.indexImg = indexImg
      this.visible = true
    },
    handleRemove (file) {
      const fileList = this.$refs.upload.fileList
      this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
    },
    handleSuccess (res, file) {
      if (res && res.code === 10000) {
        file.url = upUrl + '/file/downloadFile?fileUrl=' + res.data.fileUrl + '&suffix=' + res.data.suffix
        file.name = res.data.newFileName
        this.form.businessLicenceUrl = res.data.fileUrl
      }
    },
    handleFormatError (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '图片类型错误，请选择jpg或者png格式'
      })
    },
    handleMaxSize (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '图片大于5M.'
      })
    },
    handleBeforeUpload () {
      const check = this.uploadList.length < 1
      if (!check) {
        this.$Notice.warning({
          title: '最多上传1张图片'
        })
      }
      return check
    }
  }
}
</script>
