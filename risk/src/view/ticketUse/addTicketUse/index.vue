<style lang="less" scoped>
.upload-list {
  display: inline-block;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 10px;
}
.upload-list img {
  width: 100%;
  height: 100%;
}
.upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.upload-list:hover .upload-list-cover {
  display: block;
}
.upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
</style>

<template>
  <div>
    <Card title="录入用信">
      <MyForm ref="form"
              :model="form"
              :rules="ruleValidate"
              :label-width="120"
              style="width: 600px">
        <h4>基本信息</h4>
        <Divider />
        <MyFormItem label="上传商票正面"
                    prop="ticketFrontImgUrl">
          <div class="upload-list"
               v-for="(item, index) in fileList['ticketFrontImgUrl'].list"
               :key="item.newFileName">
            <img :src="item.viewUrl">
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewUrl)"></Icon>
              <Icon type="ios-trash-outline"
                    @click.native="handleRemove('ticketFrontImgUrl', index)"></Icon>
            </div>

          </div>
          <Upload v-show="form.ticketFrontImgUrl === ''"
                  ref="ticketFrontImgUrl"
                  :show-upload-list="false"
                  :accept="accept"
                  :format="format"
                  :max-size="5120"
                  :multiple="false"
                  :on-exceeded-size="(file) => handleMaxSize(file, 5120)"
                  :before-upload="(file) => handleBeforeUpload(file, 'ticketFrontImgUrl')"
                  :on-success="(res, file) => handleSuccess(res, file, 'ticketFrontImgUrl')"
                  type="drag"
                  :action="upUrl"
                  style="display: inline-block;width:178px;margin-right: 10px">
            <div style="width: 178px;height:178px;">
              <Icon type="ios-add"
                    size="70"></Icon>
              <p>
                上传票据正面图片
              </p>
              <p>（可将文件拖曳到此区域上传）</p>
            </div>
          </Upload>
        </MyFormItem>
        <MyFormItem label="上传商票背面"
                    prop="ticketBackImgUrl">
          <div class="upload-list"
               v-for="(item, index) in fileList['ticketBackImgUrl'].list"
               :key="item.newFileName">
            <img :src="item.viewUrl">
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewUrl)"></Icon>
              <Icon type="ios-trash-outline"
                    @click.native="handleRemove('ticketBackImgUrl', index)"></Icon>
            </div>

          </div>
          <Upload v-show="form.ticketBackImgUrl === ''"
                  ref="ticketBackImgUrl"
                  :show-upload-list="false"
                  :accept="accept"
                  :format="format"
                  :max-size="5120"
                  :multiple="false"
                  :on-exceeded-size="(file) => handleMaxSize(file, 5120)"
                  :before-upload="(file) => handleBeforeUpload(file, 'ticketBackImgUrl')"
                  :on-success="(res, file) => handleSuccess(res, file, 'ticketBackImgUrl')"
                  type="drag"
                  :action="upUrl"
                  style="display: inline-block;width:178px;">
            <div style="width: 178px;height:178px;">
              <Icon type="ios-add"
                    size="70"></Icon>
              <p>上传票据背面图片</p>
              <p>（可将文件拖曳到此区域上传）</p>
            </div>
          </Upload>
        </MyFormItem>
        <FormItem label="用信类型"
                  prop="useType">
          <Select v-model="form.useType"
                  placeholder="选择用信类型">
            <Option :value="0">开商票</Option>
          </Select>
        </FormItem>
        <Row>
          <Col span="12">
          <MyFormItem label="出票日期"
                      prop="ticketStart">
            <DatePicker :editable="false"
                        type="date"
                        placeholder="选择出票日期"
                        v-model="form.ticketStart"
                        style="width: 180px"></DatePicker>
          </MyFormItem>
          </Col>
          <Col span="12">
          <MyFormItem label="汇票到期日"
                      prop="ticketEnd">
            <DatePicker :editable="false"
                        type="date"
                        placeholder="选择汇票到期日"
                        v-model="form.ticketEnd"
                        style="width: 180px"></DatePicker>
          </MyFormItem>
          </Col>
        </Row>
        <Row>
          <Col span="14">
          <MyFormItem label="票据号码"
                      prop="ticketNo">
            <Input v-model="form.ticketNo"
                   :maxlength="30"
                   placeholder="输入票据号码"></Input>
          </MyFormItem>
          </Col>
          <Col span="10">
          <MyFormItem label="票据状态"
                      prop="ticketStatus">
            <Select v-model="form.ticketStatus"
                    placeholder="选择票据状态"
                    style="width: 130px">
              <Option :value="0">已开票</Option>
              <Option :value="1">已收票</Option>
              <Option :value="2">提示付款中</Option>
              <Option :value="3">付款签收中</Option>
              <Option :value="4">已解付</Option>
              <Option :value="5">贴现申请中</Option>
              <Option :value="6">背书签收中</Option>
              <Option :value="7">背书已签收</Option>
            </Select>
          </MyFormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <MyFormItem label="票据金额"
                      prop="ticketMoney">
            <Poptip trigger="focus">
              <Input v-model="form.ticketMoney"
                     placeholder="可输入小数"
                     :maxlength="11"
                     @on-blur="onBlurDecimal"><span slot="append">元</span></Input>
              <div slot="content">{{ formatNumber }}</div>
            </Poptip>
          </MyFormItem>
          </Col>
          <Col span="12">
          <MyFormItem label="能否转让"
                      prop="isTransfer">
            <RadioGroup v-model="form.isTransfer">
              <Radio :label="1">可转让</Radio>
              <Radio :label="0">不可转让</Radio>
            </RadioGroup>
          </MyFormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <MyFormItem label="交易合同号"
                      prop="tradeContractNo">
            <Input v-model="form.tradeContractNo"
                   placeholder="请输入交易合同号"></Input>
          </MyFormItem>
          </Col>
          <Col span="12">
          <!-- <FormItem label="商票状态"
                    prop="isTransfer">
            <Select v-model="form.status"
                    placeholder="选择票据状态"
                    style="width: 130px">
              <Option :value="0">开商票</Option>
            </Select>
          </FormItem> -->
          </Col>
        </Row>

        <h4>出票人</h4>
        <Divider />
        <MyFormItem label="公司全称"
                    prop="ticketGiveCompany">
          <Input v-model="form.ticketGiveCompany"
                 placeholder="请输入公司全称"></Input>
        </MyFormItem>

        <h4>收票人</h4>
        <Divider />
        <MyFormItem label="公司全称"
                    prop="ticketGetCompany">
          <Input v-model="form.ticketGetCompany"
                 placeholder="请输入公司全称"></Input>
        </MyFormItem>

        <h4>承兑人信息</h4>
        <Divider />
        <MyFormItem label="公司全称"
                    prop="acceptorCompany">
          <Input v-model="form.acceptorCompany"
                 placeholder="请输入公司全称"></Input>
        </MyFormItem>
        <MyFormItem label="开户行行号"
                    prop="acceptorBankNo">
          <Input v-model="form.acceptorBankNo"
                 placeholder="请输入开户行行号"></Input>
        </MyFormItem>
        <MyFormItem label="账号"
                    prop="acceptorAccount">
          <Input v-model="form.acceptorAccount"
                 placeholder="请输入账号"></Input>
        </MyFormItem>
        <MyFormItem label="开户行名称"
                    prop="acceptorBank">
          <Select v-model="form.acceptorBank"
                  placeholder="选择开户行">
            <Option v-for="item in banks"
                    :value="item"
                    :key="item">{{item}}</Option>
          </Select>
        </MyFormItem>
        <MyFormItem label="开户行省市"
                    prop="acceptorBankProvinceCode">
          <Cascader :data="citys"
                    size="large"
                    trigger="hover"
                    placeholder="选择开户行省市"
                    @on-change="onCascaderChang"></Cascader>
        </MyFormItem>
        <MyFormItem label="开户行支行"
                    prop="acceptorBankBranch">
          <Input v-model="form.acceptorBankBranch"
                 placeholder="请输入开户行支行"></Input>
        </MyFormItem>
        <FormItem>
          <Button @click="handleSubmit"
                  :loading="loading"
                  type="primary"
                  style="width: 150px">提交</Button>
        </FormItem>

        <br><br>
      </MyForm>
    </Card>
    <Modal title="查看图片"
           v-model="visible"
           width="800px">
      <img :src="viewUrl"
           v-if="visible"
           style="width: 100%">
      <div slot="footer"></div>
    </Modal>
  </div>
</template>

<script>
import MyForm from '_c/my-form'
import MyFormItem from '_c/my-form-item'
import { CITYS } from '@/libs/citys.js'
import { mapActions, mapMutations } from 'vuex'
import { BANKS } from '@/libs/status.js'
import config from '@/config'
import { deepCopy } from '@/libs/util'
import { toDateString, convertCurrency } from '@/libs/tools'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'addticketuse',
  components: {
    MyForm,
    MyFormItem
  },
  data () {
    return {
      accept: '.png,.jpg,.jpeg,.gif,.bmp',
      format: ['png', 'jpg', 'jpeg', 'gif', 'bmp'],
      base64: '',
      viewUrl: '',
      visible: false,
      loading: false,
      fileList: {
        'ticketFrontImgUrl': {
          maxNum: 1,
          list: []
        },
        'ticketBackImgUrl': {
          maxNum: 1,
          list: []
        }
      },
      upUrl: upUrl + 'file/upload',
      banks: BANKS,
      form: {
        enter: 'ticketApply',
        creditId: '',
        acceptorAccount: '',
        acceptorBank: '',
        acceptorBankBranch: '',
        acceptorBankCityCode: '',
        acceptorBankNo: '',
        acceptorBankProvinceCode: '',
        acceptorCompany: '',
        isTransfer: 0,
        ticketEnd: '',
        ticketGetCompany: '',
        ticketGiveCompany: '',
        ticketMoney: '',
        ticketNo: '',
        ticketStart: '',
        ticketStatus: 0,
        tradeContractNo: '',
        useApplyId: '',
        useType: 0,
        ticketFrontImgUrl: '',
        ticketBackImgUrl: ''
      },
      ruleValidate: {
        ticketFrontImgUrl: [
          { required: true, message: '请上传票据正面图片', trigger: 'change' }
        ],
        ticketBackImgUrl: [
          { required: true, message: '请上传票据背面图片', trigger: 'change' }
        ],
        acceptorAccount: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        acceptorBank: [
          { required: true, message: '选择开户行', trigger: 'change' }
        ],
        acceptorBankBranch: [
          { required: true, message: '请输入开户行支行', trigger: 'blur' }
        ],
        acceptorBankNo: [
          { required: true, message: '请输入开户行行号', trigger: 'blur' }
        ],
        acceptorBankProvinceCode: [
          { required: true, message: '选择开户行省市', trigger: 'change' }
        ],
        acceptorCompany: [
          { required: true, message: '请输入承兑人公司全称', trigger: 'blur' }
        ],
        ticketGetCompany: [
          { required: true, message: '请输入收票人公司全称', trigger: 'blur' }
        ],
        ticketGiveCompany: [
          { required: true, message: '请输入出票人公司全称', trigger: 'blur' }
        ],
        ticketMoney: [
          { required: true, message: '请输入票据金额', trigger: 'blur' }
        ],
        ticketNo: [
          { required: true, message: '请输入票据号码', trigger: 'blur' }
        ],
        ticketStart: [
          { required: true, type: 'date', message: '请选择出票日期', trigger: 'change' }
        ],
        ticketEnd: [
          { required: true, type: 'date', message: '请选择汇票到期日', trigger: 'change' }
        ]
        // tradeContractNo: [
        //   { required: true, message: '请输入交易合同号', trigger: 'blur' }
        // ]
      }
    }
  },
  computed: {
    citys () {
      return CITYS.map(item => {
        item.children.map(subItem => {
          delete subItem.children
          return subItem
        })
        return item
      })
    },
    formatNumber () {
      if (this.form.ticketMoney === '') return '请输入票据金额'
      return convertCurrency(this.form.ticketMoney)
    }
  },
  mounted () {
    const { useApplyId, enter, creditId } = this.$route.query
    if (useApplyId) {
      this.form.useApplyId = useApplyId
    }
    if (enter) {
      this.form.enter = enter
    }
    if (creditId) {
      this.form.creditId = creditId
    }
  },
  methods: {
    ...mapActions([
      'addCreditUseRecord',
      'ocrTicket'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    onBlurDecimal (event) {
      if (event.target.value !== '') {
        let value = parseFloat(event.target.value).toFixed(2).toString()
        this.form.ticketMoney = value
      }
    },
    handleView (viewUrl) {
      this.viewUrl = viewUrl
      this.visible = true
    },
    handleRemove (fileType, index) {
      this.fileList[fileType].list.splice(index, 1)
      this.form[fileType] = ''
    },
    handleFormatError (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '选择' + file.name + ' 格式不正确'
      })
    },
    handleMaxSize (file, size) {
      this.$Notice.warning({
        title: '提示',
        desc: file.name + ' 大于' + size / 1024 + 'M'
      })
    },
    handleBeforeUpload (file, fileType) {
      const check = this.fileList[fileType].list.length < this.fileList[fileType].maxNum
      if (!check) {
        this.$Notice.warning({
          title: '最多上传1张图片'
        })
      } else {
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
    handleSuccess (res, file, fileType) {
      if (res && res.code === 10000) {
        res.data.fileType = fileType
        res.data.viewUrl = this.base64
        if (fileType === 'ticketFrontImgUrl') {
          this.handleOcrTicket()
        } else {
          this.base64 = ''
        }
        this.fileList[fileType].list.push(res.data)
        this.form[fileType] = res.data.fileUrl
      }
    },
    handleOcrTicket () {
      const data = {
        value: this.base64
      }
      this.ocrTicket(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.base64 = ''
          this.form.ticketNo = res.data.data.ticketNo || ''
          this.form.ticketMoney = (res.data.data.ticketMoney / 100) + '' || ''
          this.form.ticketStart = this.stringToDate(res.data.data.ticketStart)
          this.form.ticketEnd = this.stringToDate(res.data.data.ticketEnd)
          this.form.acceptorAccount = res.data.data.acceptorAccount || ''
          this.form.acceptorBank = res.data.data.acceptorBank || ''
          this.form.acceptorCompany = res.data.data.acceptorCompany || ''
          this.form.acceptorBankNo = res.data.data.acceptorBankNo || ''
          this.form.ticketGiveCompany = res.data.data.ticketGiveCompany || ''
          this.form.ticketGetCompany = res.data.data.ticketGetCompany || ''
        }
      })
    },
    stringToDate (soure, start, newStr) {
      let s = ''
      if (soure) {
        s = soure.slice(0, 4) + '-' + soure.slice(4, 6) + '-' + soure.slice(6)
      }
      return s
    },
    onCascaderChang (value) {
      this.form.acceptorBankProvinceCode = value[0]
      this.form.acceptorBankCityCode = value[1]
    },
    handleSubmit () {
      this.$refs['form'].validate((valid, errors) => {
        if (valid) {
          this.loading = true
          const ticketStart = toDateString(this.form.ticketStart, '')
          const ticketEnd = toDateString(this.form.ticketEnd, '')
          const data = deepCopy(this.form)
          data.ticketStart = ticketStart
          data.ticketEnd = ticketEnd
          this.addCreditUseRecord(data).then(res => {
            this.loading = false
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.closeTag({
                name: 'addticketuse',
                query: this.$route.query
              })
              this.$Modal.success({
                title: '提示',
                content: '提交成功'
              })
            }
          })
        } else {
          this.$Message.error({
            content: errors[0],
            duration: 5,
            closable: true
          })
        }
      })
    }
  }
}
</script>
