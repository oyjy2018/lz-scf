<template>
  <Form :model="form"
        :label-width="160"
        :rules="ruleValidate"
         ref="form">
    <br>
    <H3 style="margin-left: 15px">按交易金额收费</H3>
    <br>
    <Row>
      <Col span="3">
        <FormItem label="收取服务费百分比：" prop="rate">
          <Input v-model="form.rate" style="width: 128px">
            <span slot="append">%</span>
          </Input>
        </FormItem>
      </Col>
      <Col span="10" offset>
        <FormItem>平台服务费 = 交易额 * 收取服务费百分比</FormItem>
      </Col>
    </Row>
    <FormItem>
      <Button type="primary" :loading="loading" style="width: 128px" @click="handleSubmit('form')">保存</Button>
    </FormItem>
  </Form>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  name: 'serviceRate',
  data () {
    return {
      loading: false,
      form: {
        rate: ''
      },
      ruleValidate: {
        rate: [
          { required: true, message: '此项必填', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.handlePlatformServiceRateQuery()
  },
  methods: {
    ...mapActions([
      'platformServiceRateSave',
      'platformServiceRateQuery'
    ]),
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.loading = true
          const data = {
            value: this.form.rate
          }
          this.platformServiceRateSave(data).then(res => {
            this.loading = false
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.$Modal.success({
                title: '提示',
                content: '保存成功'
              })
            }
          })
        }
      })
    },
    handlePlatformServiceRateQuery () {
      this.platformServiceRateQuery().then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.form.rate = res.data.data + ''
        }
      })
    }
  }
}
</script>
