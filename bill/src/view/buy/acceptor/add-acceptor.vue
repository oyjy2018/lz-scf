<style lang="less">
  @import "../../index/index.less";

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
    <Header :isMin="true"/>
    <div class="index-page-main"
         style="background: #f3f3f3; ">
      <div class="setion1">
        <!-- box -->
        <div class="form-box">
          <Form ref="form"
                :model="form"
                :rules="ruleValidate"
                :label-width="150">
            <FormItem>
              <Row>
                <Col span="12" offset="4">
                  <div class="top-box1">
                    <div class="top-box1-step">
                      <b>新增承兑方</b>
                    </div>
                  </div>
                </Col>
              </Row>
            </FormItem>
                <FormItem label="承兑方公司全称：" prop="acceptorName">
                  <Input v-model="form.acceptorName"
                         placeholder="请输入承兑方公司全称"
                         :maxlength="50"
                         @on-change="handleChange"></Input>
                </FormItem>
                <FormItem label="授信额度：" prop="limitMoney">
                  <Tooltip>
                    <Input v-model="form.limitMoney" style="width: 250px"
                           placeholder="请输入授信额度"
                           :maxlength="13"
                           @on-blur="(event) => form.limitMoney = isNaN(parseFloat(event.target.value)) ? '' : parseFloat(event.target.value) + ''">
                      <span slot="append">元</span>
                    </Input>
                    <div slot="content">{{ formatNumber }}</div>
                  </Tooltip>
                </FormItem>
                <FormItem label="备注：" prop="remark">
                  <Input type="textarea" v-model="form.remark"
                         placeholder="请输入备注"
                         :rows="5"
                         :maxlength="200"
                         @on-change="handleChange"></Input>
                </FormItem>

            <FormItem>
              <Row>
                <Col span="12" offset="4">
                  <Button type="primary"
                          :loading="loading"
                          @click="handleSubmit('form')">确认新增
                  </Button>
                </Col>
              </Row>
            </FormItem>
          </Form>
        </div>
        <!-- form -->
      </div>
    </div>
  </div>
</template>
<script>
import Header from '_c/header'
import GuideBox from '_c/guide-box'
import { mapState, mapActions } from 'vuex'
import { deepCopy } from '@/libs/util'
import { convertCurrency } from '@/libs/tools'

export default {
  name: 'index',
  components: {
    Header,
    GuideBox
  },
  data () {
    return {
      loading: false,
      form: {
        acceptorName: '',
        limitMoney: '',
        remark: ''
      },
      ruleValidate: {
        acceptorName: [
          { required: true, message: '请输入承兑人', trigger: 'blur' }
        ],
        limitMoney: [
          { required: true, message: '授信额度', trigger: 'blur' }
        ],
        remark: [
          { required: false, message: '备注', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      token: state => state.user.token
    }),

    formatNumber () {
      if (this.form.limitMoney === '') return '请输入授信额度'
      return convertCurrency(this.form.limitMoney)
    }
  },
  mounted () {
  },
  methods: {
    ...mapActions([
      'addAcceptor'
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
      return true
    },
    handleSubmit (name) {
      if (!this.handleIsLogin()) {
        return false
      }
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.loading = true
          const data = deepCopy(this.form)
          this.addAcceptor(data).then(res => {
            this.loading = false
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              // this.form = {
              //   accepterAccount: '',
              //   accepterBank: '',
              //   accepterBankName: '',
              // }
              this.$Modal.success({
                title: '提示',
                content: '新增成功'
              })
              this.goToAcceptorList()
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
    },
    goToAcceptorList () {
      const route = {
        name: 'buyacceptorlist'
      }
      this.$router.push(route)
    }

  }

}
</script>
