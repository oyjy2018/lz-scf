<template>
  <div class="container">
    <div class="title-nav">
      <Steps :current="current"
             style='width:600px'>
        <Step @click='step()'
              title="模型基本信息"></Step>
        <Step title="评分项计算公式"></Step>
        <Step title="模型计算公式"></Step>
      </Steps>
    </div>
    <div class="form-steps">
      <div>
        <div v-if="current === 0">
          <riskModelsStep1/>
        </div>
      </div>
      <div v-if="current === 1">
          <riskModelsStep2 :modelId='modelId'/>
      </div>
      <div>
        <div v-if="current === 2">
          <riskModelsStep3 :modelId='modelId'/>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import riskModelsStep1 from '_c/risk-model-step/riskModelsStep1'
import riskModelsStep2 from '_c/risk-model-step/riskModelsStep2'
import riskModelsStep3 from '_c/risk-model-step/riskModelsStep3'
export default {
  components: {
    riskModelsStep1,
    riskModelsStep2,
    riskModelsStep3
  },
  data () {
    return {
      routerName: '',
      current: 0,
      modelId: 0
    }
  },
  mounted () {
    this.current = parseInt(this.$route.query.current)
    if (this.$route.query.id) {
      this.modelId = parseInt(this.$route.query.id)
    }
  },
  methods: {
    step () {
      this.current = 2
    }
  }
}
</script>
<style lang="less">
</style>
