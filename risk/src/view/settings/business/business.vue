<style lang="less" scoped>
@import "./business.less";
</style>
<template>
  <Card class="back-card">
    <Row v-for="(flow,index) in flowList"
         :key="index"
         v-if="index%2 === 0">

      <Card class="my-card">
        <div class="ico-dev"
             :class="flow.ico"></div>
        <div>
          <span class="top-span">{{flow.businessType}}</span>
        </div>
        <div>
          <!-- <span class="dow-span">字段设置</span>
                    <span class="dow-span">显示设置</span> -->
          <span class="dow-span"
                @click="flowSettings(flow.businessTypeId)">工作流设置</span>
          <!-- <span class="dow-span">启用设置</span> -->
        </div>
      </Card>
      <Card class="my-card"
            v-if="index+1 < flowList.length">
        <div class="ico-dev"
             :class="flowList[index+1].ico"></div>
        <div>
          <span class="top-span">{{flowList[index+1].businessType}}</span>
        </div>
        <div>
          <!-- <span class="dow-span">字段设置</span>
                    <span class="dow-span">显示设置</span> -->
          <span class="dow-span"
                @click="flowSettings(flowList[index+1].businessTypeId)">工作流设置</span>
          <!-- <span class="dow-span">启用设置</span> -->
        </div>
      </Card>

    </Row>
  </Card>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  name: 'business',
  components: {},
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  data () {
    return {
      flowList: [{
        businessType: 'test',
        businessTypeId: 1
      }]
    }
  },
  mounted () {
    this.getData()
  },
  methods: {
    ...mapActions([
      'getBusinessTypeList'
    ]),
    getData: function () {
      this.getBusinessTypeList({}).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          let businessList = res.data.data.businessList
          for (let business of businessList) {
            business['ico'] = business.businessType === '申请授信' ? { 'ico-credit': true } : { 'ico-credit-use': true }
          }
          this.flowList = businessList
          // console.log(this.flowList)
        }
      })
    },
    flowSettings: function (businessTypeId) {
      // console.log(businessTypeId)
      const route = {
        name: 'setting-business-flowExtend',
        query: {
          businessTypeId: businessTypeId
        }
      }
      this.$router.push(route)
    }
  }
}
</script>
