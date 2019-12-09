<template>
  <div class="form-steps">
    <Form ref="form"
          :model="form"
          :rules="ruleValidate"
          :label-width="100"
          style="width: 800px">
      <h4>新增评分项</h4>
      <FormItem label="评分项名称"
                prop="itemName">
        <Input v-model="form.itemName"
               @on-keyup="enter()"
               placeholder="请输入评分项名称（中英文）"></Input>
      </FormItem>
      <FormItem label="满分分值"
                prop="fullMark">
        <Input v-model="form.fullMark"
               placeholder="请输入满分分值"></Input>
      </FormItem>
      <OrderForm @subData='submitForm' />
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
      form: {
        riskControlModelId: '',
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
          { type: 'number', message: '请输入数字格式', transform (value) { return Number(value) } },
          { max: 10000, min: 0.1, type: 'number', message: '满分值必须大于0或小于等于10000' }
        ]

      }
    }
  },
  methods: {
    ...mapActions([
      'addScoreItem'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    submitForm (formAddScore, ID, name) {
      this.form.riskControlModelId = ID
      this.form.formulaList = formAddScore.items
      this.form.itemExplain = formAddScore.itemExplain
      // 处理拼接公式
      this.handleConcatFormula(formAddScore.items)
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.addScoreItem(this.form).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success('保存成功!')
              this.handleCancel()
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
    handleCancel () {
      this.closeTag({
        name: this.$route.name,
        query: {
          id: this.$route.query.id,
          type: this.$route.query.type
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
