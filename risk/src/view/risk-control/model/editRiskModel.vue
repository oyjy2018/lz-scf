<template>
  <div class="container">
    <div class="title-nav">
      <Steps :current="current" style='width:600px;cursor:pointer;'>
        <Step @click.native='handleClickStep(0)' title="模型基本信息"></Step>
        <Step @click.native='handleClickStep(1)' title="评分项计算公式"></Step>
        <Step @click.native='handleClickStep(2)' title="模型计算公式"></Step>
      </Steps>
    </div>
    <div class="form-steps">
      <div>
        <div v-if="current === 0">
          <editModelsStep1/>
        </div>
      </div>
      <div v-if="current === 1">
        <editModelsStep2/>
      </div>
      <div>
        <div v-if="current === 2">
          <editModelsStep3/>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapMutations } from 'vuex'
import editModelsStep1 from '_c/risk-model-step/editModelsStep1'
import editModelsStep2 from '_c/risk-model-step/editModelsStep2'
import editModelsStep3 from '_c/risk-model-step/editModelsStep3'
export default {
  components: {
    editModelsStep1,
    editModelsStep2,
    editModelsStep3
  },
  data () {
    return {
      routerName: '',
      current: 0,
      modelId: ''
    }
  },
  mounted () {
    this.current = parseInt(this.$route.query.current)
    this.modelId = parseInt(this.$route.query.id)
  },
  methods: {
    ...mapMutations([
      'closeTag'
    ]),
    handleClickStep (current) {
      if (current !== this.current) {
        this.handleCancel()
        this.current = current
        this.$router.push({ path: '/riskModel/riskControl/model/edit-model', query: { id: this.modelId, current: current } })
      }
    },
    handleCancel () {
      this.closeTag({
        name: this.$route.name,
        query: {
          id: this.modelId,
          current: this.current
        }
      })
    }
  }
}
</script>
<style lang="less">
</style>
