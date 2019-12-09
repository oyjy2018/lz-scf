<style lang="less">
@import "../index/index.less";
@import "./publish-ticket.less";
.ivu-upload-drag {
  border: 1px dashed #ccc;
  border-radius: 4px;
  margin: 0 10px 10px 0;
}
.ivu-upload-drag:hover {
  border: 1px dashed #ccc;
  box-shadow: 0px 0px 5px 0px rgba(44, 142, 243, 0.6);
}
.ivu-form .ivu-form-item-label {
  font-size: 14px;
}
</style>

<template>
  <div class="index-page"
       ref="page">
    <Header :isMin="true" />
    <div class="index-page-main"
         style="background: #f3f3f3; ">
      <div class="setion1">
        <div class="top-box1">
          <div class="top-box1-step">
            <b>步骤流程：</b>
            发布票据 &nbsp;>&nbsp; 确认意向 &nbsp;>&nbsp; 先款后背 &nbsp;>&nbsp; 交易成功
          </div>
        </div>
        <!-- box -->
        <div class="form-box">
          <div class="form-box-title">发布票据</div>
          <Spin fix
                v-show="isUploading">文件上传中...</Spin>
          <Form ref="form"
                :model="form"
                :rules="ruleValidate"
                :label-width="120">
            <div class="form-box-form">
              <div class="form-box-form-uploads">
                <div class="form-box-form-upload">
                  <Upload ref="ticket_front"
                          type="drag"
                          accept=".png,.jpg,.jpeg,.gif"
                          :format="['png','jpg','jpeg','gif']"
                          :action="upUrl"
                          :max-size="5120"
                          :show-upload-list="false"
                          :on-format-error="handleFormatError"
                          :on-exceeded-size="(file) => handleMaxSize(file, 5120)"
                          :before-upload="(file) => handleBeforeUpload(file, 'ticket_front')"
                          :on-success="(res, file) => handleSuccess(res, file, 'ticket_front', 1)">
                    <div class="form-box-form-upload-icon">
                      <img src="../../assets/images/front/upload.png"
                           title="上传图片">
                      <p>
                        <font color="red">*</font>上传票据正面图片
                      </p>
                      <div class="form-box-form-upload-label">正面
                        <a href="#"
                           @click.stop="handleView(ticketf)">示例</a>
                      </div>
                      <div title="预览图片"
                           @click.stop="handleViewFiles('ticket_front')"
                           v-if="fileList['ticket_front'].list.length"
                           class="form-box-form-upload-num"
                           :class="{'upload-num-active': fileListKey === 'ticket_front'}">
                        {{fileList['ticket_front'].list.length}}
                        <img :class="{'img-active': fileListKey === 'ticket_front'}"
                             src="@/assets/images/front/down.png"
                             title="预览图片">
                      </div>
                    </div>
                  </Upload>
                </div>
                <div class="form-box-form-upload">
                  <Upload ref="ticket_back"
                          multiple
                          type="drag"
                          accept=".png,.jpg,.jpeg,.gif"
                          :format="['png','jpg','jpeg','gif']"
                          :action="upUrl"
                          :max-size="5120"
                          :show-upload-list="false"
                          :on-format-error="handleFormatError"
                          :on-exceeded-size="(file) => handleMaxSize(file, 5120)"
                          :before-upload="(file) => handleBeforeUpload(file, 'ticket_back')"
                          :on-success="(res, file) => handleSuccess(res, file, 'ticket_back', 1)">
                    <div class="form-box-form-upload-icon">
                      <img src="../../assets/images/front/upload.png"
                           title="上传图片">
                      <p>
                        <font color="red">*</font>上传票据背面图片
                      </p>
                      <div class="form-box-form-upload-label">
                        背面<a href="#"
                           @click.stop="handleView(ticketb)">示例</a> / 无背书 <a href="#"
                           @click.stop="handleView(ticketbn)">示例</a>
                      </div>
                    </div>
                    <div title="预览图片"
                         @click.stop="handleViewFiles('ticket_back')"
                         v-if="fileList['ticket_back'].list.length"
                         class="form-box-form-upload-num"
                         :class="{'upload-num-active': fileListKey === 'ticket_back'}">
                      {{fileList['ticket_back'].list.length}}
                      <img :class="{'img-active': fileListKey === 'ticket_back'}"
                           src="@/assets/images/front/down.png"
                           title="预览图片">
                    </div>
                  </Upload>
                </div>

                <div class="form-box-form-upload">
                  <Upload ref="purchase_contract"
                          multiple
                          accept=".png,.jpg,.jpeg,.gif,.pdf"
                          :format="['png','jpg','jpeg','gif','pdf']"
                          type="drag"
                          :max-size="10240"
                          :show-upload-list="false"
                          :on-format-error="handleFormatError"
                          :on-exceeded-size="(file) => handleMaxSize(file, 10240)"
                          :before-upload="(file) => handleBeforeUpload(file, 'purchase_contract')"
                          :on-success="(res, file) => handleSuccess(res, file, 'purchase_contract', 1)"
                          :action="upUrl">
                    <div class="form-box-form-upload-icon">
                      <img src="../../assets/images/front/upload.png"
                           title="上传图片">
                      <p>
                        <font color="red">*</font>上传采购合同
                      </p>
                    </div>
                    <div title="预览图片"
                         @click.stop="handleViewFiles('purchase_contract')"
                         v-if="fileList['purchase_contract'].list.length"
                         class="form-box-form-upload-num"
                         :class="{'upload-num-active': fileListKey === 'purchase_contract'}">
                      {{fileList['purchase_contract'].list.length}}
                      <img :class="{'img-active': fileListKey === 'purchase_contract'}"
                           src="@/assets/images/front/down.png"
                           title="预览图片">
                    </div>
                  </Upload>
                </div>
                <div class="form-box-form-upload">
                  <Upload ref="purchase_invoice"
                          multiple
                          accept=".png,.jpg,.jpeg,.gif,.pdf"
                          :format="['png','jpg','jpeg','gif','pdf']"
                          type="drag"
                          :max-size="10240"
                          :show-upload-list="false"
                          :on-format-error="handleFormatError"
                          :on-exceeded-size="(file) => handleMaxSize(file, 10240)"
                          :before-upload="(file) => handleBeforeUpload(file, 'purchase_invoice')"
                          :on-success="(res, file) => handleSuccess(res, file, 'purchase_invoice', 1)"
                          :action="upUrl">
                    <div class="form-box-form-upload-icon">
                      <img src="../../assets/images/front/upload.png"
                           title="上传图片">
                      <p>
                        <font color="red">*</font>上传采购发票
                      </p>
                    </div>
                    <div title="预览图片"
                         @click.stop="handleViewFiles('purchase_invoice')"
                         v-if="fileList['purchase_invoice'].list.length"
                         class="form-box-form-upload-num"
                         :class="{'upload-num-active': fileListKey === 'purchase_invoice'}">
                      {{fileList['purchase_invoice'].list.length}}
                      <img :class="{'img-active': fileListKey === 'purchase_invoice'}"
                           src="@/assets/images/front/down.png"
                           title="预览图片">
                    </div>
                  </Upload>
                </div>

                <div class="form-box-form-upload">
                  <Upload ref="trade_data"
                          multiple
                          accept=".png,.jpg,.jpeg,.gif,.pdf"
                          :format="['png','jpg','jpeg','gif','pdf']"
                          type="drag"
                          :max-size="10240"
                          :show-upload-list="false"
                          :on-format-error="handleFormatError"
                          :on-exceeded-size="(file) => handleMaxSize(file, 10240)"
                          :before-upload="(file) => handleBeforeUpload(file, 'trade_data')"
                          :on-success="(res, file) => handleSuccess(res, file, 'trade_data', 1)"
                          :action="upUrl">
                    <div class="form-box-form-upload-icon">
                      <img src="../../assets/images/front/upload.png"
                           title="上传图片">
                      <p>上传交易背景资料</p>
                    </div>
                    <div title="预览图片"
                         @click.stop="handleViewFiles('trade_data')"
                         v-if="fileList['trade_data'].list.length"
                         class="form-box-form-upload-num"
                         :class="{'upload-num-active': fileListKey === 'trade_data'}">
                      {{fileList['trade_data'].list.length}}
                      <img :class="{'img-active': fileListKey === 'trade_data'}"
                           src="@/assets/images/front/down.png"
                           title="预览图片">
                    </div>
                  </Upload>
                </div>

              </div>
              <!-- 文件列表 -->
              <div v-if="fileListKey && fileList[fileListKey].list.length"
                   class="form-box-form-files">
                <div class="form-box-form-files-item"
                     v-for="(item, index) in fileList[fileListKey].list"
                     :key="item.newFileName">
                  <div class="form-box-form-files-item-img">
                    <img v-if="item.suffix === '.pdf'"
                         style="width: 29px; height: 29px"
                         src="@/assets/images/front/pdf.png">
                    <img v-else
                         style="width: 30px; height: 28px"
                         src="@/assets/images/front/img.png">
                  </div>
                  <div class="item-context">
                    <div class="item-context-title"
                         :title="item.originalFileName">{{item.originalFileName}}</div>
                    <div class="item-context-icons">
                      <div @click="handleView(item.viewFileUrl)"
                           class="item-context-view"
                           title="查看"></div>
                      <div @click="handleDown(item.downloadFileUrl)"
                           class="item-context-load"
                           title="下载"></div>
                      <div @click="handleRemove(fileListKey, index)"
                           class="item-context-del"
                           title="删除"></div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="form-box-form-inputs">
                <Row>
                  <Col span="10">
                  <FormItem label="承兑人"
                            prop="accepterName">
                    <Input v-model="form.accepterName"
                           placeholder="请输入承兑人"
                           @on-change="handleChange"></Input>
                  </FormItem>
                  <FormItem label="票据号码"
                            prop="billNo">
                    <Input v-model="form.billNo"
                           placeholder="请输入票据号码"
                           :maxlength="30"
                           @on-change="handleChange"></Input>
                  </FormItem>
                  <FormItem label="票面金额"
                            prop="billAmt">
                    <Row>
                      <Col span="22">
                      <Input v-model="form.billAmt"
                             placeholder="请输入票面金额"
                             @on-blur="(event) => form.billAmt = isNaN(parseFloat(event.target.value)) ? '' : parseFloat(event.target.value) + ''"
                             @on-change="handleChange"></Input>
                      </Col>
                      <Col span="2"
                           style="text-align: center">元</Col>
                    </Row>
                  </FormItem>
                  <FormItem label="背书次数"
                            prop="endorsedCount">
                    <Row>
                      <Col span="13">
                      <RadioGroup v-model="form.endorsedCount"
                                  type="button"
                                  @on-change="handleChange">
                        <Radio :label="0">0</Radio>
                        <Radio :label="1">1</Radio>
                        <Radio :label="2">2</Radio>
                        <Radio :label="3">3</Radio>
                        <Radio :label="4">4</Radio>
                      </RadioGroup>
                      </Col>
                      <Col span="9">
                      <Input number
                             v-model="form.endorsedCount"
                             placeholder="请输入背书次数"></Input>
                      </Col>
                      <Col span="2"
                           style="text-align: center">次
                      </Col>
                    </Row>
                  </FormItem>
                  </Col>
                  <Col span="10"
                       offset="2">
                  <FormItem label="到期日期"
                            prop="maturityDate">
                    <Row>
                      <Col span="20">
                      <DatePicker :editable="false"
                                  ref='maturityDate'
                                  style="width: 300px"
                                  type="date"
                                  placeholder="请选择到期日期"
                                  :options="options"
                                  v-model="form.maturityDate"
                                  @on-change="handleChange"></DatePicker>
                      </Col>
                      <Col span="4">
                      剩余{{surplusValidDays}}天
                      </Col>
                    </Row>
                  </FormItem>
                  <div class="ivu-form-item ivu-form-item-required"><label class="ivu-form-item-label"
                           style="width: 120px;">票据瑕疵</label>
                    <div class="ivu-form-item-content"
                         style="margin-left: 120px;">
                      <div class="ivu-dropdown ivu-input"
                           style="width: 408px;height:auto; min-height: 34px;">
                        <div class="ivu-dropdown-rel">
                          <Dropdown style="width: 408px;">
                            <a href="javascript:void(0)"
                               :style="{color: form.flaws.length ? '#666' : '#ddd'}">
                              <label style="width: 380px;display: inline-block;">
                                <Tag v-for="item in form.flaws"
                                     :key="item"
                                     closable
                                     @on-close="handleFlawsClose(item)">{{item}}</Tag>
                              </label>
                              <Icon type="ios-arrow-down"></Icon>
                            </a>
                            <DropdownMenu slot="list">
                              <DropdownItem v-for="item in flawsArray"
                                            :key="item.value"
                                            @click.native="handleFlaws(item.value)">
                                <Tooltip max-width="200"
                                         :content="item.tip"
                                         placement="right">
                                  {{ item.label }}
                                </Tooltip>
                              </DropdownItem>
                            </DropdownMenu>
                          </Dropdown>
                        </div>
                      </div>
                    </div>
                  </div>
                  <Row>
                    <Col span="14">
                    <FormItem label="发布截止日"
                              prop="expirationDate">
                      <DatePicker :editable="false"
                                  style="width: 194px"
                                  type="date"
                                  :options="options"
                                  placeholder="请选择发布截止日期"
                                  v-model="form.expirationDate"
                                  format="yyyy-MM-dd"
                                  @on-change="handleChange"></DatePicker>
                    </FormItem>
                    </Col>
                    <Col span="10">
                    <FormItem prop="time">
                      <TimePicker style="width: 184px;margin-left: -86px;"
                                  v-model="form.time"
                                  :editable="false"
                                  type="time"
                                  placeholder="请选择发布截止时间"
                                  @on-change="handleChange"></TimePicker>
                    </FormItem>
                    </Col>
                  </Row>
                  </Col>
                </Row>

              </div>
            </div>

            <FormItem prop="hasSignProtocols"
                      style="text-align: center;">
              <Checkbox v-model="form.hasSignProtocols">同意签署</Checkbox>
              <router-link to="/rulestatement"
                           target="_blank">《领筑数科票据信息平台撮合交易规则》</router-link>
              <!-- <a href="#"
                 target="_blank">《领筑数科商票交易平台信息撮合规则》</a> -->
            </FormItem>
            <FormItem style="text-align: center;">
              <Button type="primary"
                      :loading="loading"
                      @click="handleSubmit('form')">立即发布</Button>
              <Button type="text"
                      :loading="loading2"
                      @click="handleQuotation()"
                      style="margin-left: 8px; color:#2C8EF3">指定资方</Button>
              <span v-if="companyList.length && companyIndex > -1">
                <Tag type="border"
                     closable
                     @on-close="companyIndex = -1">{{companyList[companyIndex].companyName}}</Tag>
              </span>
            </FormItem>
          </Form>
        </div>
        <!-- form -->
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
    <Modal v-model="modal"
           title="指定买方">
      &nbsp;
      <Select v-model="companyIndex"
              clearable
              filterable
              style="width:200px">
        <Option v-for="(item, index) in companyList"
                :value="index"
                :key="item.id">{{ item.companyName }}</Option>
      </Select>
    </Modal>
    <Footer />
    <guide-box container=".index-page" />
  </div>
</template>
<script>
import Header from '_c/header'
import Footer from '_c/footer'
import GuideBox from '_c/guide-box'
import { mapState, mapActions } from 'vuex'
import { deepCopy } from '@/libs/util'
import { getDays, toDateString } from '@/libs/tools'
import { flawsArray } from '@/libs/status'
import config from '@/config'
import ticketf from '@/assets/images/example/ticket-f.png'
import ticketb from '@/assets/images/example/ticket-b.png'
import ticketbn from '@/assets/images/example/ticket-bn.png'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'index',
  components: {
    Header,
    Footer,
    GuideBox
  },
  data () {
    const validateEndorsedCount = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('背书次数不能为空'))
      }
      if (!Number.isInteger(value)) {
        callback(new Error('请输入数字'))
      } else {
        if (value < 0 || value > 4) {
          callback(new Error('背书次数0-4'))
        } else {
          callback()
        }
      }
    }
    const validateBillNo = (rule, value, callback) => {
      if (value.length !== 30) {
        return callback(new Error('请输入30位的票据号码'))
      }
      callback()
    }
    return {
      isUploading: false,
      loading: false,
      loading2: false,
      ticketf: ticketf,
      ticketb: ticketb,
      ticketbn: ticketbn,
      visible: false,
      modal: false,
      viewUrl: '',
      hasReal: false,
      hasReceipt: false,
      hasRepay: false,
      base64: '',
      fileListKey: '',
      fileList: {
        'ticket_front': {
          maxNum: 1,
          files: [],
          list: []
        },
        'ticket_back': {
          maxNum: 4,
          files: [],
          list: []
        },
        'purchase_contract': {
          maxNum: 10,
          files: [],
          list: []
        },
        'purchase_invoice': {
          maxNum: 5,
          files: [],
          list: []
        },
        'trade_data': {
          maxNum: 10,
          files: [],
          list: []
        }
      },
      options: {
        disabledDate (date) {
          return date && date.valueOf() < Date.now() - 86400000
        }
      },
      upUrl: upUrl + 'file/upload',
      flawsArray: flawsArray,
      companyList: [],
      companyIndex: -1,
      form: {
        accepterAccount: '',
        accepterBank: '',
        accepterBankName: '',
        accepterName: '',
        billAmt: '',
        billNo: '',
        drawerAccount: '',
        drawerBank: '',
        drawerBankBame: '',
        drawerName: '',
        endorsedCount: 0,
        expirationDate: '',
        time: '23:59:59',
        fileList: [],
        flaws: ['无瑕疵'],
        hasSignProtocols: false,
        issueDate: '',
        maturityDate: '',
        payeeAccount: '',
        payeeBank: '',
        payeeBankName: '',
        payeeName: '',
        surplusValidDays: 0,
        assignBuyerCompanyId: '',
        assignBuyerCompanyName: ''
      },
      ruleValidate: {
        accepterName: [
          { required: true, message: '请输入承兑人', trigger: 'blur' }
        ],
        billNo: [
          { required: true, message: '请输入票据号码', trigger: 'blur' },
          { validator: validateBillNo, trigger: 'blur' }
        ],
        billAmt: [
          { required: true, message: '请输入票面金额', trigger: 'blur' }
        ],
        endorsedCount: [
          { validator: validateEndorsedCount, trigger: 'change' },
          { validator: validateEndorsedCount, trigger: 'blur' }
        ],
        maturityDate: [
          { required: true, type: 'date', message: '请选择到期日期', trigger: 'change' }
        ],
        flaws: [
          { required: true, type: 'array', min: 1, message: '请选择票据瑕疵', trigger: 'change' }
        ],
        expirationDate: [
          { required: true, type: 'date', message: '请选择发布截止日期', trigger: 'change' }
        ],
        time: [
          { required: true, message: '请选择发布截止时间', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      token: state => state.user.token
    }),
    surplusValidDays () {
      let num = 0
      if (this.form.maturityDate) {
        num = getDays(this.form.maturityDate)
      }
      return num
    }
  },
  filters: {
    flawsText (arr) {
      let text = '请选择票据瑕疵'
      if (arr.length) {
        text = arr.join(',')
      }
      return text
    }
  },
  mounted () {
    if (this.token) {
      this.getCompanyJdProperty().then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.hasReal = res.data.data.hasReal
          this.hasReceipt = res.data.data.hasReceipt
          this.hasRepay = res.data.data.hasRepay
        }
      })
    }
    this.$ba.trackPageview('/bill/publishticket')
    this.fileList.ticket_front.files = this.$refs.ticket_front.fileList
    this.fileList.ticket_back.files = this.$refs.ticket_back.fileList
    this.fileList.purchase_contract.files = this.$refs.purchase_contract.fileList
    this.fileList.purchase_invoice.files = this.$refs.purchase_invoice.fileList
    this.fileList.trade_data.files = this.$refs.trade_data.fileList
  },
  methods: {
    ...mapActions([
      'getCompanyJdProperty',
      'inquireBusinessTicket',
      'ocrRecognize',
      'quotationCompanyList'
    ]),
    handleChange (e) {
      this.handleIsLogin()
    },
    handleIsLogin () {
      if (!this.token) {
        this.$Message.warning({
          content: '您还没登录',
          duration: 5,
          closable: true
        })
        this.$router.push('/login')
        return false
      }
      if (!this.hasReal) {
        this.$Modal.confirm({
          title: '提示',
          content: '您还没企业实名认证,是否去企业实名认证？',
          onOk: () => {
            window.open(config.realUrl)
          }
        })
        return false
      }
      // 是否在京东绑定收款账户
      if (!this.hasRepay) {
        this.$Modal.confirm({
          title: '提示',
          content: '您还没绑定收款账户,是否去绑定？',
          onOk: () => {
            window.open(config.bankUrl)
          }
        })
        return false
      }
      return true
    },
    handleViewFiles (key) {
      this.fileListKey = this.fileListKey === key ? '' : key
    },
    handleView (viewUrl) {
      this.viewUrl = viewUrl
      this.visible = true
    },
    handleDown (downloadFileUrl) {
      window.open(downloadFileUrl)
    },
    handleRemove (fileType, index) {
      const delArr = this.fileList[fileType].list.splice(index, 1)
      this.fileList[fileType].files.splice(index, 1)
      this.form.fileList = this.form.fileList.filter(item => {
        if (item.newFileName !== delArr[0].newFileName) {
          return item
        }
      })
    },
    handleFormatError (file) {
      this.isUploading = false
      this.$Notice.warning({
        title: '提示',
        desc: '选择' + file.name + ' 格式不正确'
      })
    },
    handleMaxSize (file, size) {
      this.isUploading = false
      this.$Notice.warning({
        title: '提示',
        desc: file.name + ' 大于' + size / 1024 + 'M'
      })
    },
    handleBeforeUpload (file, fileType) {
      if (!this.handleIsLogin()) {
        return false
      }
      const check = this.fileList[fileType].files.length < this.fileList[fileType].maxNum
      if (!check) {
        this.$Notice.warning({
          title: `最多上传${this.fileList[fileType].maxNum}张`
        })
      } else {
        this.isUploading = true
        this.base64Img(file)
      }
      return check
    },
    base64Img (file) {
      let reader = new FileReader()
      reader.onload = (e) => {
        this.base64 = e.target.result
      }
      reader.readAsDataURL(file)
    },
    handleSuccess (res, file, fileType, sort) {
      this.isUploading = false
      this.$Notice.success({
        title: '文件上传成功'
      })
      if (res && res.code === 10000) {
        res.data.fileType = fileType
        res.data.sort = sort
        res.data.viewUrl = this.base64
        if (fileType === 'ticket_front') {
          this.handleOcrRecognize()
        } else {
          this.base64 = ''
        }
        this.fileList[fileType].list.push(res.data)
        this.form.fileList.push(res.data)
      }
    },
    stringToDate (soure, start, newStr) {
      let s = ''
      if (soure) {
        s = soure.slice(0, 4) + '-' + soure.slice(4, 6) + '-' + soure.slice(6)
      }
      return s
    },
    handleOcrRecognize () {
      const data = {
        value: this.base64
      }
      this.ocrRecognize(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.base64 = ''
          this.form.billNo = res.data.data.billNo || ''
          this.form.billAmt = (res.data.data.billAmt / 100) + '' || ''
          this.form.issueDate = this.stringToDate(res.data.data.issueDate)
          this.form.maturityDate = this.stringToDate(res.data.data.maturityDate)
          this.form.drawerName = res.data.data.drawerName || ''
          this.form.drawerBank = res.data.data.drawerBank || ''
          this.form.drawerBankName = res.data.data.drawerBankName || ''
          this.form.drawerAccount = res.data.data.drawerAccount || ''
          this.form.accepterName = res.data.data.accepterName || ''
          this.form.accepterBankName = res.data.data.accepterBankName || ''
          this.form.accepterBank = res.data.data.accepterBank || ''
          this.form.accepterAccount = res.data.data.accepterAccount || ''
          this.form.payeeName = res.data.data.payeeName || ''
          this.form.payeeBankName = res.data.data.payeeBankName || ''
          this.form.payeeBank = res.data.data.payeeBank || ''
          this.form.payeeAccount = res.data.data.payeeAccount || ''
        }
      })
    },
    handleFlaws (value) {
      if (!this.handleIsLogin()) {
        return false
      }
      const index = this.form.flaws.indexOf(value)
      if (index === -1) {
        this.form.flaws.push(value)
      }
      if (value === '无瑕疵') {
        this.form.flaws = ['无瑕疵']
      } else {
        let i = this.form.flaws.indexOf('无瑕疵')
        if (i !== -1) {
          this.form.flaws.splice(i, 1)
        }
      }
    },
    handleFlawsClose (text) {
      this.form.flaws.splice(this.form.flaws.indexOf(text), 1)
    },
    handleQuotation () {
      this.loading2 = true
      this.quotationCompanyList().then((res) => {
        this.loading2 = false
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.modal = true
          this.companyList = res.data.data.list
        }
      })
    },
    handleSubmit (name) {
      if (!this.handleIsLogin()) {
        return false
      }
      if (this.fileList['ticket_front'].list.length === 0) {
        return this.$Message.error({
          content: '请上传票据正面',
          duration: 5,
          closable: true
        })
      }
      if (this.fileList['ticket_back'].list.length < 1) {
        return this.$Message.error({
          content: '请上传票据背面',
          duration: 5,
          closable: true
        })
      }
      if (this.fileList['purchase_contract'].list.length < 1) {
        return this.$Message.error({
          content: '请上传采购合同',
          duration: 5,
          closable: true
        })
      }
      if (this.fileList['purchase_invoice'].list.length < 1) {
        return this.$Message.error({
          content: '请上传采购发票',
          duration: 5,
          closable: true
        })
      }
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (!this.form.flaws.length === 0) {
            return this.$Message.error({
              content: '请选择票据瑕疵',
              duration: 5,
              closable: true
            })
          }
          if (!this.form.hasSignProtocols) {
            return this.$Message.error({
              content: '请同意签署《领筑数科票据交易平台信息撮合规则》',
              duration: 5,
              closable: true
            })
          }
          this.loading = true
          const maturityDate = toDateString(this.form.maturityDate, '')
          const expirationDate = toDateString(this.form.expirationDate, '')
          const data = deepCopy(this.form)
          data.maturityDate = maturityDate
          data.expirationDate = expirationDate + ' ' + this.form.time
          data.hasSignProtocols = data.hasSignProtocols ? 1 : 0
          data.flaws = data.flaws.join(',')
          data.surplusValidDays = this.surplusValidDays
          if (this.companyIndex > -1) {
            data.assignBuyerCompanyId = this.companyList[this.companyIndex].id
            data.assignBuyerCompanyName = this.companyList[this.companyIndex].companyName
          }
          this.inquireBusinessTicket(data).then(res => {
            this.loading = false
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.fileList['ticket_front'].list.length = 0
              this.fileList['ticket_back'].list.length = 0
              this.fileList['purchase_contract'].list.length = 0
              this.fileList['purchase_invoice'].list.length = 0
              this.fileList['trade_data'].list.length = 0
              this.companyIndex = -1
              this.form = {
                accepterAccount: '',
                accepterBank: '',
                accepterBankName: '',
                accepterName: '',
                billAmt: '',
                billNo: '',
                drawerAccount: '',
                drawerBank: '',
                drawerBankBame: '',
                drawerName: '',
                endorsedCount: 0,
                expirationDate: '',
                fileList: [],
                flaws: ['无瑕疵'],
                hasSignProtocols: false,
                issueDate: '',
                maturityDate: '',
                payeeAccount: '',
                payeeBank: '',
                payeeBankName: '',
                payeeName: '',
                surplusValidDays: 0
              }
              this.$Modal.success({
                title: '提示',
                content: '发布成功'
              })
            }
          })
        } else {
          this.$Message.error({
            content: '请填写完整',
            duration: 5,
            closable: true
          })
        }
      })
    }
  }
}
</script>
