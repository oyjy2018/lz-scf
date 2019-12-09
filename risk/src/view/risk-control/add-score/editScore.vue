<template>
  <div class="form-steps">
    <Form ref="form"
          :model="form"
          :rules="ruleValidate"
          :label-width="100"
          style="width: 800px">
      <h4>修改评分项</h4>
      <FormItem label="评分项名称"
                prop="itemName">
        <Input v-model="form.itemName"
               placeholder="请输入评分项名称（中英文）"
               @on-keyup="enter()"></Input>
      </FormItem>
      <FormItem label="满分分值"
                prop="fullMark">
        <Input v-model="form.fullMark"
               placeholder="请输入满分分值"></Input>
      </FormItem>
      <OrderForm @subData='submitForm'
                 :editData='scoreId'
                 ref='editRef' />
      <FormItem>
        <Button @click="Submit()"
                type="primary"
                style="width: 60px;margin-right:20px;">保存</Button>
        <Button @click="cancel()"
                style="width: 60px">取消</Button>
      </FormItem>
    </Form>

  </div>
</template>
<script>
import OrderForm from '_c/order-form'
import { mapActions, mapMutations } from 'vuex'
export default {
  name: 'modelStep3',
  components: {
    OrderForm

  },
  data () {
    return {
      scoreId: '',
      editFormData: '',
      form: {
        scoreItemId: '',
        itemName: '',
        fullMark: '',
        itemExplain: '',
        formula: '',
        formulaDesc: '',
        formulaList: []
      },
      ruleValidate: {
        itemName: [
          { required: true, message: '请输入评分项名称', trigger: 'blur' },
          { max: 100, message: '最长输入100个中英文字符' }
        ],
        fullMark: [
          { required: true, message: '请输入满分分值', trigger: 'blur' },
          { type: 'number', message: '请输入数字格式', trigger: 'blur', transform (value) { return Number(value) } },
          { max: 10000, min: 0.1, type: 'number', message: '满分值必须大于0或小于等于10000', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.scoreId = this.$route.query.scoreId
    this.getScoreDetail()
  },

  methods: {
    ...mapActions([
      'editScore', 'scoreDetail'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    submitForm (formAddScore, ID, name) {
      this.form.scoreItemId = ID
      this.form.formulaList = formAddScore.items
      this.form.itemExplain = formAddScore.itemExplain
      // 处理拼接公式
      this.handleConcatFormula(formAddScore.items)
      formAddScore.items.forEach((item, i) => {
        this.form.formula += item.formula + ';'
      })
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.editScore(this.form).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success('修改成功!')
              this.cancel()
            }
          })
        } else {
          this.$Message.error('请完善信息再提交!')
        }
      })
    },
    handleConcatFormula (items) {
      let formula = ''
      let formulaDesc = ''
      items.forEach((item, index) => {
        var _end = index === items.length - 1 ? '}' : '},'
        formula += item.criteria + '{' + item.formula + _end
        formulaDesc += item.criteriaDesc + '{' + item.formulaDesc + _end
      })
      this.form.formula = formula
      this.form.formulaDesc = formulaDesc
    },
    getScoreDetail () {
      this.scoreDetail(this.scoreId).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.form.fullMark = res.data.data.fullMark + ''
          this.form.itemName = res.data.data.itemName
          this.editFormData = res.data.data
        }
      })
    },
    Submit () {
      this.$refs.editRef.handleSubmit('formData')
    },
    cancel () {
      this.closeTag({
        name: this.$route.name,
        query: {
          id: this.$route.query.id,
          scoreId: this.$route.query.scoreId
        }
      })
    },
    enter (n) {
      let s = this.form.itemName
      let r = s.replace(/[\d]/g, '')
      this.form.itemName = r
    }
  }
}
</script>
<style lang="less">
</style>
