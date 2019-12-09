<style lang="less" scoped>
@import "../project.less";

</style>
<template>
  <div class="project-tab-div">
    <Table border :columns="columns" style="width: 792px" :data="list"></Table>
    <Drawer :title="drawerTitle"
            :closable="false"
            width="850"
            v-model="isDrawer">
      <Table border
             style="width: 752px"
             :columns="fileColumns"
             :data="DrawerData"></Table>
    </Drawer>
    <viewer>
      <div class="detailed">
        <img class="viewerImg"
             ref="viewerImgRef"
             id="viewerImgId"
             src=""
             style="display: none"
             alt="">
      </div>
    </viewer>
    <MaskLayer v-if="fileType === 'pdf' && showFile" @close="close">
      <iframe :src="fileUrl" style="width: 80%;height:100%;"></iframe>
    </MaskLayer>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import MaskLayer from '_c/common/mask-layer'

export default {
  name: 'ProjectFile',
  components: {
    MaskLayer
  },
  data () {
    return {
      columns: [
        {
          type: 'index',
          title: '序号',
          width: 80,
          align: 'center'
        },
        {
          title: '文件分类',
          align: 'center',
          width: 560,
          key: 'fileClassify',
          render: (h, params) => {
            let html = params.row.fileClassify
            if (params.row.count > 0) {
              html = `<a style="text-decoration: underline;" title="${params.row.fileClassify}">${params.row.fileClassify}</a>`
            }
            return h('div', {
              on: {
                click: () => {
                  this.drawerTitle = params.row.fileClassify
                  this.DrawerData = params.row.oneClassifyFileList
                  this.isDrawer = true
                }
              },
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '数量',
          width: 150,
          align: 'center',
          key: 'list',
          render: (h, params) => {
            return h('div', params.row.count)
          }
        }
      ],
      list: [],
      isDrawer: false,
      drawerTitle: '',
      DrawerData: [],
      fileColumns: [
        {
          title: '文件',
          width: 450,
          align: 'center',
          key: 'originalFileName',
          render: (h, params) => {
            let html = `<a style="text-decoration: underline;" title="${params.row.fileClassify}">${params.row.originalFileName}</a>`
            return h('div', {
              on: {
                click: () => {
                  this.fileType = params.row.fileType
                  if (params.row.fileType === 'image') {
                    this.$refs.viewerImgRef.src = params.row.fileUrl
                    document.getElementById('viewerImgId').click()
                  } else {
                    // window.location.href = params.row.fileUrl
                    console.log(params.row.fileUrl)
                    this.showFile = true
                    this.fileUrl = params.row.fileUrl
                  }
                }
              },
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '上传时间',
          width: 150,
          align: 'center',
          key: 'uploadTime'
        },
        {
          title: '上传人',
          width: 150,
          align: 'center',
          key: 'uploadPerson'
        }
      ],
      fileType: '',
      fileUrl: '',
      showFile: false
    }
  },
  methods: {
    ...mapActions([
      'getFileList'
    ]),
    getData (value) {
      const data = {
        contractNo: value
      }
      this.getFileList(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.list = res.data.data
        }
      })
    },
    close (e) {
      this.showFile = false
      console.log(e)
    }
  }
}
</script>
