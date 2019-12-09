<template>
  <div style="height: 100%">
    <iframe :src="url"
            frameborder="0"
            width="100%"
            height="100%"
            id="ifra"
            name="ifra"
            @load="test"></iframe>
  </div>
</template>
<script>
import { mapActions, mapMutations } from 'vuex'

export default {
  name: 'exceptional-order',
  data () {
    return {
      url: ''
    }
  },
  mounted () {
    this.queryUserCenterUrl().then(res => {
      if (res && res.status === 200 && res.data.code === 10000) {
        if (!res.data.data || !res.data.data.url) {
          this.$Modal.error({
            title: '提示',
            content: '<p>' + res.data.message + '</p>',
            onOk: () => {
              this.closeTag({
                name: 'company-exceptionalorder'
              })
            }
          })
        }
        this.url = res.data.data.url
        console.log(res)
      }
    }).catch(function (reason) {
      log('Failed: ')
    })
    const self = this
    this.$nextTick(() => {
      const iframe = document.querySelector('#ifra')
      if (iframe.attachEvent) {
        iframe.attachEvent('onload', function () {
          const iframeNode = window.frames['ifra'].document
          self.getDom(iframeNode)
        })
        console.log('err')
      } else {
        iframe.onload = function () {
          // const iframeNode = window.frames['ifra'].document
          // self.getDom(iframeNode)
          console.log('iframe')
        }
        // console.log(iframe)
      }
    })
  },
  methods: {
    ...mapActions([
      'queryUserCenterUrl'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    test () {
      console.log('trest')
    }
  }
}
</script>
