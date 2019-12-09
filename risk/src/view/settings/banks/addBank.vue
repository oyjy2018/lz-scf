<template>
  <Card :bordered="false">
    <div v-if="isJdReal === 1">
      <Steps :current="0" style="width: 1000px;height: 100px;" v-if="form.accountType === 2">
          <Step title="提交" content="提交收款银行账号"></Step>
          <Step title="验证" content="小额打款验证"></Step>
          <Step title="成功" content="添加成功（收款账户）"></Step>
      </Steps>
      <Steps :current="0" style="width: 1000px;height: 100px;" v-else>
        <Step title="提交"
              content="提交收票银行账号"></Step>
        <Step title="验证"
              content="小额打款验证"></Step>
        <Step title="授权"
              content="线下网银授权ECDS查询"></Step>
        <Step title="成功"
              content="添加成功（收票账户）"></Step>
      </Steps>
      <Form class="form"
            style="width: 600px;"
            ref="form"
            :model="form"
            :rules="rules"
            :label-width="100"
            @keydown.enter.native="showModal('form')">
        <FormItem label="银行账号"
                  prop="bankAccountNo">
          <Input v-model="form.bankAccountNo"
                placeholder="请输入银行账号"
                size="large"
                :maxlength=22
                style="text-algin: center"/>
        </FormItem>
        <FormItem label="户名">
          <Input v-model="companyName"
                placeholder="请输入户名"
                size="large"
                disabled
                style="text-algin: center" />
        </FormItem>
        <FormItem label="银行名称"
                  prop="cnapBankCode">
          <Select v-model="form.cnapBankCode"
                  placeholder="请选择银行"
                  @on-open-change="remoteMethod1"
                  :loading="loading1">
            <Option v-for="option in jdBanks"
                    :value="option.cnapBankCode+''"
                    :key="option.issuerId">
              {{option.bankName}}
            </Option>
          </Select>
        </FormItem>
        <Row>
          <Col span="12">
            <FormItem  label="银行省市"  prop="provinceId">
              <Select v-model="form.provinceId"
                      placeholder="请选择银行省"
                      @on-open-change="remoteMethod2"
                      :loading="loading1">
                <Option v-for="option in jdBankProvinces"
                        :value="option.provinceId"
                        :key="option.provinceId">
                  {{option.provinceName}}
                </Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem prop="cityId">
              <Select v-model="form.cityId"
                      placeholder="请选择银行市"
                      @on-open-change="remoteMethod3"
                      :loading="loading1">
                <Option v-for="option in jdBankCitys"
                        :value="option.cityId"
                        :key="option.cityId">
                  {{option.cityName}}
                </Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <FormItem label="银行支行"
                  prop="bankChildName">
          <Select v-model="form.bankChildName"
                  placeholder="请选择银行支行"
                  @on-open-change="remoteMethod4"
                  :loading="loading1">
            <Option v-for="option in jdBankChilds"
                    :value="option.bankChildName"
                    :key="option.bankChildName">
              {{option.bankChildName}}
            </Option>
          </Select>
        </FormItem>
        <FormItem>
          <Button type="primary"
                  @click="showModal('form')">提交</Button>
        </FormItem>
      </Form>
    </div>
    <div v-else style="padding: 20px;" class="noReal">
      <p>
        <Icon type="md-alert" size="40" color="#f9a409"></Icon>
        <span class="noReal-title">添加收款账户需要先通过京东的实名认证，请前往<a href="#" @click="handleRedirect('real')">企业认证。</a></span>
      </p>
      <p class="noReal-content">如有疑问，请咨询客服电话：0755-86571036</p>
      <p class="noReal-confirm"><Button type="primary" @click="handleRedirect('banks')">返回</Button></p>
    </div>
    <Modal v-model="modal1"
           width="800"
           cancel-text=""
           scrollable
           @on-ok="handleSubmit">
      <Steps slot="header"
             :current="0"
             size="small">
        <Step title="提交"
              content="提交收款银行账号"></Step>
        <Step title="验证"
              content="小额打款验证"></Step>
        <Step title="授权"
              content="线下网银授权ECDS查询"></Step>
        <Step title="成功"
              content="添加成功（收票账户）"></Step>
      </Steps>
      <div>
        <h1>确认提交？</h1>
        <br />
        <p>提交后，系统将向您填写的银行账号发起小额打款，您准确输入打款金额后，小额打款即可认证成功。</p>
      </div>
    </Modal>
    <Modal v-model="modal2"
           width="800"
           cancel-text=""
           scrollable
           @on-ok="handleSubmit">
      <Steps slot="header"
             :current="0"
             size="small">
        <Step title="提交"
              content="提交收款银行账号"></Step>
        <Step title="验证"
              content="小额打款验证"></Step>
        <Step title="成功"
              content="添加成功（收款账户）"></Step>
      </Steps>
      <div>
        <h1>确认提交？</h1>
        <br />
        <p>提交后，系统将向您填写的银行账号发起小额打款，您准确输入打款金额后，收款账户即可设置成功。</p>
      </div>
    </Modal>
  </Card>
</template>

<script>
import { mapState, mapActions, mapMutations } from 'vuex'

export default {
  name: 'company-addbank',
  data () {
    return {
      modal1: false,
      modal2: false,
      isJdReal: 0,
      loading1: false,
      jdBanks: [],
      jdBankProvinces: [],
      jdBankCitys: [],
      jdBankChilds: [],
      form: {
        bankCode: '',
        bankName: '',
        bankCnaps: '',
        cnapBankCode: '',
        bankChildName: '',
        bankAccountNo: '',
        bankAccountName: '',
        accountType: -1,
        provinceId: '',
        provinceName: '',
        cityId: '',
        cityName: ''
      },
      rules: {
        bankAccountNo: [
          { required: true, message: '请输入银行账号', trigger: 'blur' },
          { type: 'number', message: '请输入正确的银行账号', trigger: 'blur', transform (value) { return Number(value) } }
        ],
        bankAccountName: [
          { required: true, message: '请输入户名', trigger: 'blur' }
        ],
        cnapBankCode: [
          { required: true, message: '请选择银行', trigger: 'change' }
        ],
        provinceId: [
          { required: true, message: '请选择银行省', trigger: 'change' }
        ],
        cityId: [
          { required: true, message: '请选择银行市', trigger: 'change' }
        ],
        bankChildName: [
          { required: true, message: '请选择银行支行', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      companyName: state => {
        const name = state.user.userName.slice(0, state.user.userName.indexOf('-'))
        return name
      }
    })
  },
  mounted () {
    this.form.accountType = parseInt(this.$route.query.accountType)
    this.checkIsJdReal()
  },
  methods: {
    ...mapActions([
      'getJdBanks',
      'getJdBankProvinces',
      'getJdBankCitys',
      'getJdBankCode',
      'addCompanyBank',
      'getCompanyIsJdReal'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    checkIsJdReal () {
      this.getCompanyIsJdReal().then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.isJdReal = res.data.data
        }
      })
    },
    handleRedirect (type) {
      this.closeTag({
        name: 'banks-add',
        query: {
          accountType: this.form.accountType
        }
      })
      if (type === 'real') {
        this.$router.push('../company/real')
      }
    },
    remoteMethod1 () {
      this.loading1 = true
      if (this.jdBanks.length === 0) {
        this.getJdBanks().then(res => {
          if (res && res.status === 200 && res.data.code === 10000) {
            this.jdBanks = res.data.data
          }
          this.loading1 = false
        })
      } else {
        this.loading1 = false
      }
    },
    remoteMethod2 (value) {
      this.loading1 = true
      if (this.jdBankProvinces.length === 0) {
        this.getJdBankProvinces().then(res => {
          if (res && res.status === 200 && res.data.code === 10000) {
            this.jdBankProvinces = res.data.data
          }
          this.loading1 = false
        })
      } else {
        this.loading1 = false
      }
      if (!value) {
        this.form.cityId = ''
      }
    },
    remoteMethod3 () {
      this.loading1 = true
      if (this.form.provinceId !== '') {
        this.getJdBankCitys({ provinceId: this.form.provinceId }).then(res => {
          if (res && res.status === 200 && res.data.code === 10000) {
            this.jdBankCitys = res.data.data
          }
          this.loading1 = false
        })
      } else {
        this.loading1 = false
      }
    },
    remoteMethod4 () {
      this.loading1 = true
      if (this.form.cnapBankCode === '') {
        this.$Message.warning({
          content: '请先选择银行',
          duration: 5,
          closable: true
        })
        return
      }
      if (this.form.cityId === '') {
        this.$Message.warning({
          content: '请先选择银行市',
          duration: 5,
          closable: true
        })
        return
      }
      this.getJdBankCode({ cnapBankCode: this.form.cnapBankCode, cityId: this.form.cityId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.jdBankChilds = res.data.data
        }
        this.loading1 = false
      })
    },
    showModal (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.form.accountType === 2) {
            this.modal2 = true
          } else {
            this.modal1 = true
          }
        } else {
          this.$Message.error({
            content: '请填写完整',
            duration: 5,
            closable: true
          })
        }
      })
    },
    handleSubmit () {
      this.jdBanks.forEach(item => {
        if (item.cnapBankCode + '' === this.form.cnapBankCode) {
          this.form.bankCode = item.bankCode
          this.form.bankName = item.bankName
        }
      })
      this.jdBankChilds.forEach(item => {
        if (item.bankChildName === this.form.bankChildName) {
          this.form.provinceName = item.provinceName
          this.form.cityName = item.cityName
          this.form.bankCnaps = item.bankCnaps
        }
      })
      this.form.bankAccountName = this.companyName
      this.addCompanyBank(this.form).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Modal.success({
            title: '提示',
            content: res.data.message
          })
          const _accountType = parseInt(this.$route.query.accountType)
          this.$router.push({
            name: _accountType === 1 ? 'banks-receipt' : 'banks-pay'
          })
          this.form = {
            bankCode: '',
            bankName: '',
            bankCnaps: '',
            cnapBankCode: '',
            bankChildName: '',
            bankAccountNo: '',
            bankAccountName: '',
            accountType: -1,
            provinceId: '',
            provinceName: '',
            cityId: '',
            cityName: ''
          }
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
.noReal {
  padding: 20px;
}
.noReal-title {
  font-size: 17px;
  display: inline-block;
  text-indent: 10px;
}
.noReal-title a {
  text-decoration: underline;
}
.noReal-content {
  padding: 20px 50px;
}
.noReal-confirm {
  text-align: right;
  width: 486px;
}
</style>
