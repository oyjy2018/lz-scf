<template>
  <div>
    <Form ref="form"
          :model="form"
          :rules="ruleValidate"
          style="width: 600px">
      <h4>模型基本信息</h4>
      <FormItem prop="modelName"
                :label-width="100"
                label="模型名称">
        <Input placeholder="请输入模型名称（中英文）"
               v-model="form.modelName"
               @on-keyup="enter()"></Input>
      </FormItem>
      <Row>
        <Col span='12'>
        <FormItem prop="systemId"
                  label="所属业务"
                  :label-width="100">
          <Select placeholder="选择平台"
                  v-model="riskType"
                  style="width: 200px">
            <Option :value="riskType">{{riskType}}</Option>
          </Select>
        </FormItem>
        </Col>
        <Col span='11'
             offset="1">
        <FormItem prop="businessTypeName">
          <Select placeholder="选择业务类型"
                  v-model="form.businessTypeName"
                  ref="businessType"
                  @on-change="v=>{getTypeId(v)}">
            <Option v-for="item in typeList"
                    :key="item.businessType"
                    :value="item.businessType">{{item.businessType}}</Option>
          </Select>
        </FormItem>
        </Col>
      </Row>
      <FormItem label="模型说明"
                :label-width="100">
        <TextAreaBottom :value="form.modelExplain">
          <Input type="textarea"
                 :rows="4"
                 :maxlength="maxlength"
                 v-model="form.modelExplain"
                 placeholder="模型说明" />
        </TextAreaBottom>
      </FormItem>
    </Form>
    <Button style="margin-top: 16px;margin-right: 16px;"
            type="primary"
            @click="submitBtn('form')">保存 & 下一步</Button>
    <Button style="margin-top: 16px;"
            @click="cancel()">取消</Button>
  </div>
</template>
<script>
import { mapActions, mapMutations } from 'vuex'
import { trimSpace } from '@/libs/tools'
import TextAreaBottom from '_c/common/text-area-bottom'
export default {
  components: {
    TextAreaBottom
  },
  data () {
    return {
      current: 1,
      riskType: '风控平台',
      maxlength: 500,
      businessTypeId: '',
      form: {
        businessTypeId: '',
        businessTypeName: '',
        modelExplain: '',
        modelName: '',
        systemId: 1
      },
      ruleValidate: {
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' },
          { max: 50, message: '最长输入50个中英文字符' }
        ],
        systemId: [
          { required: true, type: 'number', message: '请选择所属平台', trigger: 'blur' }
        ],
        businessTypeName: [
          { required: true, type: 'string', message: '请选择所属业务', trigger: 'blur' }
        ]
      },
      name: 'modelStep1',
      typeList: [],
      modelId: ''
    }
  },
  mounted () {
    if (this.$route.name === 'addModel') {
      this.getTypeData()
    }
  },
  methods: {
    ...mapActions([
      'getStandardBusinessTypeList', 'insertModel'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getTypeData: function () {
      this.getStandardBusinessTypeList({}).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.typeList = res.data.data.businessList
        }
      })
    },
    getTypeId: function (value) {
      this.$refs.form.validate(() => {
        this.typeList.map((n) => {
          if (n.businessType === value) {
            this.form.businessTypeId = n.businessTypeId
          }
        })
      })
    },
    submitBtn: function (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.insertModel(this.form).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.$Message.success('保存成功!')
              this.modelId = res.data.data
              this.cancel()
              setTimeout(() => {
                this.$router.push({ path: '/riskModel/riskControl/model/add-model', query: { id: this.modelId, current: 1 } })
              }, 10)
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
          current: 0
        }
      })
    },
    enter () {
      this.form.modelName = trimSpace(this.form.modelName)
    }
  }

}
</script>
<style lang="less">
</style>
