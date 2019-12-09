<template>
  <div>
    <Form ref="form"
          :model="form"
          :rules="ruleValidate"
          :label-width="100"
          style="width: 600px">
      <h4>模型计算公式</h4>
      <div class="riskModel3">
        <Row>
          <Col span='20'>
          <ul>
            <li>
              <span v-for="(name,index) in this.nameList"
                    :key='index'
                    :idData='name.id'
                    @click='chooseList(name.itemName)'>{{name.itemName}}</span>
            </li>
          </ul>
          </Col>
        </Row>
      </div>
      <FormItem prop="value"
                :label-width="0">
          <Input v-model="form.value"
                 type="textarea"
                 :maxlength="500"
                 :rows="4"
                 placeholder="模型计算公式" />
      </FormItem>
      <Button style="margin-top: 16px;margin-right: 16px;"
              type="primary"
              @click="submitBtn('form')">保存 & 查看</Button>
      <Button style="margin-top: 16px;"
              @click="cancel()">取消</Button>
    </Form>
  </div>
</template>
<script>
import { mapActions, mapMutations } from 'vuex'
export default {
  name: 'modelStep3',
  data () {
    return {
      nameList: [],
      form: {
        value: '',
        id: ''
      },
      ruleValidate: {
        value: [
          { required: true, message: '请输入模型计算公式', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    let Id = this.$route.query.id
    this.form.id = Id
    this.getModelFormulaData()
  },
  methods: {
    ...mapActions(['getModelFormula', 'updateModelFormula']),
    ...mapMutations([
      'closeTag'
    ]),
    chooseList (name) {
      this.form.value += `[` + name + ']'
    },
    getModelFormulaData () {
      const data = { id: this.form.id }
      this.getModelFormula(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.nameList = res.data.data.scoreItemsList
          this.form.value = (res.data.data.modelFormulaCH === null ? '' : res.data.data.modelFormulaCH + '')
        }
      })
    },
    submitBtn: function (name) {
      console.log(name)
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.updateModelFormula(this.form).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success('保存成功!')
              this.closeTag({
                name: this.$route.name,
                query: {
                  id: this.form.id,
                  current: 2
                }
              })
            }
          })
        } else {
          this.$Message.error('请完善信息再提交!')
        }
      })
    },
    cancel () {
      this.closeTag({
        name: this.$route.name,
        query: {
          id: this.form.id,
          current: 2
        }
      })
      setTimeout(() => {
        this.$router.push({ path: '/riskModel/riskControl/model/edit-model', query: { id: this.form.id, current: 1 } })
      }, 10)
    }
  }
}
</script>
<style lang="less">
ul {
  list-style: none;
}
.riskModel3 ul {
  margin: 20px 0;
}
.riskModel3 ul li span {
  word-spacing: 2px;
  padding: 3px;
  border: 1px solid #cccccc;
  padding: 5px 10px;
  border-radius: 4px;
  margin: 5px 0;
  margin-right: 10px;
  display: inline-block;
  text-align: center;
  color: #555555;
  cursor: pointer;
  font-size: 12px;
}
.riskModel3 ul li span:hover {
  border: 1px solid #2c8ef3;
  color: #2c8ef3;
}
</style>
