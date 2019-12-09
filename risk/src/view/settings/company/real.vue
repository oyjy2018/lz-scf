<template>
  <div style="height: 100%">
    <iframe :src="url"
            frameborder="0"
            width="100%"
            height="100%"
            id="show-iframe"></iframe>
    <Spin size="large"
          fix
          v-if="spinShow"></Spin>
  </div>
</template>
<script>
import { mapActions, mapMutations } from 'vuex'

export default {
  name: 'company-real',
  data () {
    return {
      url: ''
      // spinShow: true
    }
  },
  mounted () {
    this.getCompanyRealUrl().then(res => {
      if (res && res.status === 200 && res.data.code === 10000) {
        if (res.data.data.success) {
          this.url = res.data.data.message
        } else {
          this.$Modal.warning({
            title: '失败提醒',
            content: '<p>' + res.data.data.message + '</p>',
            onOk: () => {
              this.closeTag({
                name: 'company-real'
              })
            }
          })
        }
      }
    })
  },
  methods: {
    ...mapActions([
      'getCompanyRealUrl'
    ]),
    ...mapMutations([
      'closeTag'
    ])
  }
}
</script>
