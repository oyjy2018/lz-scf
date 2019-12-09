<template>
  <Card>
    <MyForm class="form"
            style="padding-bottom: 240px; width: 600px;"
            ref="form"
            :model="formValidate"
            :label-width="180"
            @keydown.enter.native="handleSubmit">
      <FormItem>
        <h3>公司信息</h3>
      </FormItem>

      <FormItem prop="blicUrl"
                label="营业执照">
        <image-pdf :url="form.blicUrl"
                   :file-name="form.blicUrlFileName"
                   @handle-view="handleView"></image-pdf>
      </FormItem>
      <FormItem prop="companyName"
                label="公司全称">
        {{form.companyName}}
      </FormItem>
      <FormItem prop="creditCode"
                label="统一社会信用代码">
        {{form.creditCode}}
      </FormItem>
      <FormItem prop="provinceName"
                label="公司地址">
        {{form.provinceName}}-{{form.cityName}}
      </FormItem>
      <FormItem prop="detailAddr"
                label="详细地址">
        {{form.detailAddr}}
      </FormItem>
      <FormItem prop="blicEndDate"
                label="营业期限（止）">
        {{form.blicEndDate}}
      </FormItem>

      <FormItem prop="companyNature"
                label="行业大类">
        {{form.companyNature}}
      </FormItem>
      <FormItem prop="companyNatureConcrete"
                label="具体行业">
        {{form.companyNatureConcrete}}
      </FormItem>
      <FormItem prop="staffSize"
                label="公司规模">
        {{form.staffSize}}
      </FormItem>
      <FormItem prop="contactNumber"
                label="联系电话">
        {{form.contactNumber}}
      </FormItem>
      <Divider dashed />
      <FormItem>
        <h3>法人信息</h3>
      </FormItem>
      <FormItem prop="legalPersonCertBackUrl"
                label="身份证头像页">
        <image-pdf :url="form.legalPersonCertBackUrl"
                   :file-name="form.legalPersonCertBackFileName"
                   @handle-view="handleView"></image-pdf>
      </FormItem>
      <FormItem prop="legalPersonCertFrontUrl"
                label="身份证国徽页">
        <image-pdf :url="form.legalPersonCertFrontUrl"
                   :file-name="form.legalPersonCertFrontFileName"
                   @handle-view="handleView"></image-pdf>
      </FormItem>
      <FormItem prop="legalPersonName"
                label="姓名">
        {{form.legalPersonName}}
      </FormItem>
      <FormItem prop="legalPersonCertNo"
                label="身份证号">
        {{form.legalPersonCertNo}}
      </FormItem>
      <FormItem prop="legalPersonCertEndDate"
                label="有效期限（止）">
        {{form.legalPersonCertEndDate}}
      </FormItem>
      <Divider dashed />
      <FormItem>
        <h3>被授权人身份信息</h3>
      </FormItem>
      <FormItem prop="porxyPersonCertBackUrl"
                label="被授权人身份证头像页">
        <image-pdf :url="form.porxyPersonCertBackUrl"
                   :file-name="form.porxyPersonCertBackFileName"
                   @handle-view="handleView"></image-pdf>
      </FormItem>
      <FormItem prop="porxyPersonCertFrontUrl"
                label="被授权人身份证国徽页">
        <image-pdf :url="form.porxyPersonCertFrontUrl"
                   :file-name="form.porxyPersonCertFrontFileName"
                   @handle-view="handleView"></image-pdf>
      </FormItem>
      <FormItem prop="porxyPersonName"
                label="姓名">
        {{form.porxyPersonName}}
      </FormItem>
      <FormItem prop="porxyPersonCertNo"
                label="身份证号">
        {{form.porxyPersonCertNo}}
      </FormItem>
      <FormItem prop="porxyPersonCertEndDate"
                label="有效期限（止）">
        {{form.porxyPersonCertEndDate}}
      </FormItem>
      <FormItem v-show="form.porxyCommissionUrl"
                prop="porxyCommissionUrl"
                label="授权委托书">
        <image-pdf :url="form.porxyCommissionUrl"
                   :file-name="form.porxyCommissionFileName"
                   @handle-view="handleView"></image-pdf>
      </FormItem>
      <Divider dashed />
      <FormItem>
        <h3>审批信息</h3>
      </FormItem>
      <MyFormItem label="公司类别"
                  prop="companyCategory"
                  :rules="{ required: true, message: '选择公司类别', trigger: 'change' }">
        <Select v-model="formValidate.companyCategory"
                placeholder="选择公司类别">
          <Option value="保理公司">保理公司</Option>
          <Option value="工程公司">工程公司</Option>
          <Option value="材料公司">材料公司</Option>
          <Option value="银行">银行</Option>
          <Option value="平台研发公司">平台研发公司</Option>
        </Select>
      </MyFormItem>
      <MyFormItem v-if="ticketSty"
                  label="绑定的系统版本1"
                  prop="ticketSystemVersionId"
                  :rules="{ required: !(ticketSty && controlSty), message: '选择系统版本', trigger: 'change' }">
        <Row>
          <Col span="11">
          <Select value="商票交易平台"
                  placeholder="选择公司类别">
            <Option value="商票交易平台">商票交易平台</Option>
          </Select>
          </Col>
          <Col span="11"
               offset="2">
          <Select v-model="formValidate.ticketSystemVersionId"
                  placeholder="不绑定">
            <Option value="2">标准版</Option>
            <Option value="">不绑定</Option>
          </Select>
          </Col>
        </Row>
      </MyFormItem>
      <MyFormItem v-if="controlSty"
                  label="绑定的系统版本2"
                  prop="riskControlSystemVersionId"
                  :rules="{ required: !(ticketSty && controlSty), message: '选择系统版本', trigger: 'change' }">
        <Row>
          <Col span="11">
          <Select value="风控平台"
                  placeholder="选择公司类别">
            <Option value="风控平台">风控平台</Option>
          </Select>
          </Col>
          <Col span="11"
               offset="2">
          <Select v-model="formValidate.riskControlSystemVersionId"
                  placeholder="不绑定"
                  @on-change="onChange">
            <Option value="1">标准版</Option>
            <Option value="">不绑定</Option>
          </Select>
          </Col>
        </Row>
      </MyFormItem>
      <MyFormItem v-if="controlSty"
                  label="可使用的业务流程"
                  prop="businessFlowList"
                  :rules="[ { required: formValidate.riskControlSystemVersionId, type: 'array', min: 1, message: '选择可使用的业务流程', trigger: 'change' }]">
        <Select multiple
                v-model="formValidate.businessFlowList"
                :disabled="!formValidate.riskControlSystemVersionId"
                placeholder="选择可使用的业务流程">
          <Option value="授信审批流程">授信审批流程</Option>
          <Option value="用信审批流程">用信审批流程</Option>
        </Select>
      </MyFormItem>
      <FormItem style="text-align: center;">
        <Button @click="handleSubmit(1)"
                type="primary"
                :loading="loading"
                style="">审核通过</Button>
        <Button @click="confirmModal = true"
                :loading="loading"
                style="margin-left: 28px;">审核拒绝</Button>
        <Button @click="handleCancel()"
                style="margin-left: 28px;">取消</Button>
      </FormItem>
      <Modal v-model="visible"
             width="800px"
             :footer-hide="true"
             title="查看文件">
        <iframe :src="viewUrl"
                frameborder="0"
                width="100%"
                height="600px"></iframe>
      </Modal>
    </MyForm>
    <Modal v-model="confirmModal" width="360">
      <p slot="header" style="color:#f60;text-align:center">
        <Icon type="ios-information-circle"></Icon>
        <span>审核拒绝确认</span>
      </p>
      <div style="text-align:center">
        <p>审核拒绝后, 该公司将无法使用.</p>
        <p>您确认拒绝该公司?</p>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long :loading="false" @click="handleSubmit(2)">确认</Button>
      </div>
    </Modal>
  </Card>
</template>
<script>
import MyForm from '_c/my-form'
import MyFormItem from '_c/my-form-item'
import ImagePdf from './imagePdf.vue'
import { ValidatePass, idCardValid } from '@/libs/util'
import { mapState, mapActions, mapMutations } from 'vuex'
import config from '@/config'
const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'company-audit',
  components: {
    MyForm,
    MyFormItem,
    ImagePdf
  },
  data () {
    const validateCode = (rule, value, callback) => {
      if (value.length < 18) {
        callback(new Error('统一社会信用代码小于18位'))
      } else {
        callback()
      }
    }
    const validateProvinceCity = (rule, value, callback) => {
      if (this.form.cityName === '') {
        callback(new Error('请选择公司地址市'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 8) {
        callback(new Error('密码至少8位'))
      } else if (ValidatePass(value) < 2) {
        callback(new Error('至少8位，包含大小写字母、数字、符号中至少2种'))
      } else {
        callback()
      }
    }
    const validateIdcard = (rule, value, callback) => {
      if (!idCardValid(value)) {
        callback(new Error('身份证号不正确'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      controlSty: false,
      ticketSty: false,
      uploadKey: '',
      aupisLep: false,
      accept: '.png,.jpg,.jpeg,.gif,.bmp',
      format: ['png', 'jpg', 'jpeg', 'gif', 'bmp'],
      upUrl: upUrl + 'file/upload',
      file: '',
      provinceCity: [],
      isBlicEndDate: '固定日期',
      isLegalPersonCertEndDate: '固定日期',
      isPorxyPersonCertEndDate: '固定日期',
      form: {
        'blicUrl': '',
        'blicUrlFileName': '',
        'companyName': '',
        'creditCode': '',
        'provinceName': '',
        'cityName': '',
        'detailAddr': '',
        'blicEndDate': '',
        'companyNature': '',
        'companyNatureConcrete': '',
        'staffSize': '',
        'contactNumber': '',
        'legalPersonCertFrontUrl': '',
        'legalPersonCertFrontFileName': '',
        'legalPersonCertBackUrl': '',
        'legalPersonCertBackFileName': '',
        'legalPersonName': '',
        'legalPersonCertNo': '',
        'legalPersonCertEndDate': '',
        'aupisLep': 0,
        'porxyPersonCertFrontUrl': '',
        'porxyPersonCertFrontFileName': '',
        'porxyPersonCertBackUrl': '',
        'porxyPersonCertBackFileName': '',
        'porxyPersonName': '',
        'porxyPersonCertNo': '',
        'porxyPersonCertEndDate': '',
        'porxyCommissionUrl': '',
        'porxyCommissionFileName': '',
        'porxyPersonEmail': '',
        'porxyPersonPassword': '',
        'read': []
      },
      NatureConcreteList: [],
      viewUrl: '',
      visible: false,
      uploadList: {
        blicUrl: [],
        legalPersonCertFrontUrl: [],
        legalPersonCertBackUrl: [],
        porxyPersonCertFrontUrl: [],
        porxyPersonCertBackUrl: [],
        porxyCommissionUrl: []
      },
      creditCodeRules: validateCode,
      provinceCityRules: validateProvinceCity,
      passRules: validatePass,
      idcardRules: validateIdcard,
      formValidate: {
        companyCategory: '',
        riskControlSystemVersionId: '',
        ticketSystemVersionId: '',
        businessFlowList: [],
        status: 1
      },
      confirmModal: false
    }
  },
  computed: {
    ...mapState({
      natureList: state => state.app.natureList,
      staffsizeList: state => state.app.staffsizeList,
      systemVersionList: state => state.app.systemVersionList,
      bankList: state => state.app.bankList,
      productList: state => state.app.productList
    }),
    rules () {
      return {
        creditCode: [
          { required: true, message: '请输入18位统一社会信用代码', trigger: 'blur' },
          { validator: this.creditCodeRules, trigger: 'change' }
        ],
        provinceName: [
          { required: true, message: '请选择公司地址省市', trigger: 'change' },
          { validator: this.provinceCityRules, trigger: 'change' }
        ],
        legalPersonCertNo: [
          { required: true, message: '请输入法人身份证号', trigger: 'blur' },
          { required: true, validator: this.idcardRules, trigger: 'blur' }
        ],
        porxyPersonCertNo: [
          { required: true, message: '请输入被授权人身份证号', trigger: 'blur' },
          { required: true, validator: this.idcardRules, trigger: 'blur' }
        ],
        porxyPersonPassword: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { required: true, validator: this.passRules, trigger: 'blur' }
        ],
        read: { required: true, type: 'array', min: 1, message: '请先勾选协议', trigger: 'change' }
      }
    }
  },
  mounted () {
    const id = this.$route.query.id
    if (id) {
      this.getCompanyAuditBasicInfo({ companyAuditId: id }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          if (res.data.data.systemIdList) {
            if (res.data.data.systemIdList.indexOf('1') !== -1) {
              this.controlSty = true
              this.formValidate.riskControlSystemVersionId = '1'
            } else {
              this.controlSty = false
            }
            if (res.data.data.systemIdList.indexOf('2') !== -1) {
              this.ticketSty = true
              this.formValidate.ticketSystemVersionId = '2'
            } else {
              this.ticketSty = false
            }
          }
          this.form = res.data.data
        }
      })
    }
    // this.uploadList.blicUrl = this.$refs.blicUrl.fileList
    // this.uploadList.legalPersonCertFrontUrl = this.$refs.legalPersonCertFrontUrl.fileList
    // this.uploadList.legalPersonCertBackUrl = this.$refs.legalPersonCertBackUrl.fileList
    // this.uploadList.porxyPersonCertFrontUrl = this.$refs.porxyPersonCertFrontUrl.fileList
    // this.uploadList.porxyPersonCertBackUrl = this.$refs.porxyPersonCertBackUrl.fileList
    // this.uploadList.porxyCommissionUrl = this.$refs.porxyCommissionUrl.fileList
  },
  methods: {
    ...mapActions([
      'getCompanyAuditBasicInfo',
      'getNatureSelect',
      'getNatureSelectData',
      'companyAudit'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getNatureSelect (value) {
      this.natureList.forEach(item => {
        if (item.dictValue === value) {
          this.getNatureSelectData({ key: item.dictKey }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000 && res.data.data.length) {
              this.NatureConcreteList = res.data.data
            }
          })
        }
      })
    },
    handleChangeRadio (value, name) {
      if (value === '长期') {
        this.form[name] = '长期'
      } else {
        this.form[name] = ''
      }
    },
    handleChangeCheckbox (value) {
      if (value) {
        this.form.porxyPersonCertFrontUrl = this.form.legalPersonCertFrontUrl
        this.form.porxyPersonCertBackUrl = this.form.legalPersonCertBackUrl
        this.form.porxyPersonName = this.form.legalPersonName
        this.form.porxyPersonCertNo = this.form.legalPersonCertNo
        this.form.porxyPersonCertEndDate = this.form.legalPersonCertEndDate
      } else {
        this.form.porxyPersonCertFrontUrl = ''
        this.form.porxyPersonCertBackUrl = ''
        this.form.porxyPersonName = ''
        this.form.porxyPersonCertNo = ''
        this.form.porxyPersonCertEndDate = ''
      }
    },
    handleSelector (data) {
      this.form.provinceName = data[0]
      this.form.cityName = data.length === 2 ? data[1] : ''
    },
    handleView (viewUrl) {
      this.viewUrl = viewUrl
      this.visible = true
    },
    handleRemove (file) {
      this.form[this.uploadKey] = ''
      const fileList = this.$refs[this.uploadKey].fileList
      this.$refs[this.uploadKey].fileList.splice(fileList.indexOf(file), 1)
    },
    handleSuccess (res, file) {
      if (res && res.code === 10000) {
        file.url = upUrl + '/file/downloadFile?fileUrl=' + res.data.fileUrl + '&suffix=' + res.data.suffix
        file.name = res.data.newFileName
        this.form[this.uploadKey] = res.data.fileUrl
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
        desc: '图片类型错误，请选择jpg或者png格式'
      })
    },
    handleMaxSize (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '图片大于5M.'
      })
    },
    handleBeforeUpload (name) {
      this.uploadKey = name
      const check = this.uploadList[name].length < 1
      if (!check) {
        this.$Notice.warning({
          title: '最多上传1张图片'
        })
      }
      return check
    },
    onChange (value) {
      if (!value) {
        this.formValidate.businessFlowList = []
      }
    },
    handleSubmit (status) {
      if (status === 1) {
        this.$refs.form.validate((valid, errors) => {
          if (valid) {
            if ((this.controlSty && this.ticketSty) && !this.formValidate.ticketSystemVersionId && !this.formValidate.riskControlSystemVersionId) {
              this.$Message.error({
                content: '绑定的系统版本1、绑定的系统版本2,请选择一个',
                duration: 5,
                closable: true
              })
            } else {
              this.loading = true
              this.formValidate.companyAuditId = this.$route.query.id
              this.formValidate.status = status
              this.formValidate.ticketSystemVersionId = this.formValidate.ticketSystemVersionId ? this.formValidate.ticketSystemVersionId : ''
              this.formValidate.riskControlSystemVersionId = this.formValidate.riskControlSystemVersionId ? this.formValidate.riskControlSystemVersionId : ''
              this.companyAudit(this.formValidate).then(res => {
                this.loading = false
                if (res && res.status === 200 && res.data.code === 10000) {
                  this.closeTag({
                    name: 'company-audit',
                    query: {
                      id: this.$route.query.id,
                      name: this.$route.query.name
                    }
                  })
                  this.$Message.success({
                    content: '审核成功',
                    duration: 3
                  })
                  this.$router.push({ 'name': 'company-audit-table' })
                }
              })
            }
          } else {
            this.$Message.error({
              content: errors[0],
              duration: 5,
              closable: true
            })
          }
        })
      } else {
        this.loading = true
        this.formValidate.companyAuditId = this.$route.query.id
        this.formValidate.status = status
        this.formValidate.ticketSystemVersionId = this.formValidate.ticketSystemVersionId ? this.formValidate.ticketSystemVersionId : ''
        this.formValidate.riskControlSystemVersionId = this.formValidate.riskControlSystemVersionId ? this.formValidate.riskControlSystemVersionId : ''
        this.companyAudit(this.formValidate).then(res => {
          this.loading = false
          this.leaveConfirmModal = false
          if (res && res.status === 200 && res.data.code === 10000) {
            this.closeTag({
              name: 'company-audit',
              query: {
                id: this.$route.query.id,
                name: this.$route.query.name
              }
            })
            this.$Message.success({
              content: '审核成功',
              duration: 3
            })
            this.$router.push({ 'name': 'company-audit-table' })
          }
        })
      }
    },
    handleCancel () {
      this.closeTag({
        name: 'company-audit',
        query: {
          id: this.$route.query.id,
          name: this.$route.query.name
        }
      })
    }
  }
}
</script>
