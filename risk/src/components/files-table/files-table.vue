<style lang="less">
.file-view {
  display: flex;
  flex-direction: row;
  margin-bottom: 10px;
  width: 80%;
  .file-name {
    flex: 1;
    -o-text-overflow: ellipsis;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    overflow: hidden;
    white-space: normal;
    word-break: break-all;
    cursor: pointer;
    &:hover {
      color: #2c8ef3;
    }
  }
  .file-icon {
    width: 20px;
    margin-right: 10px;
    cursor: pointer;
    color: #ccc;
    &:hover {
      color: #2c8ef3;
    }
  }

  .image::before {
    content: url("../../assets/images/file-icons/image.png");
  }
  .word::before {
    content: url("../../assets/images/file-icons/word.png");
  }
  .ppt::before {
    content: url("../../assets/images/file-icons/ppt.png");
  }
  .pdf::before {
    content: url("../../assets/images/file-icons/pdf.png");
  }
  .txt::before {
    content: url("../../assets/images/file-icons/txt.png");
  }
  .zip::before {
    content: url("../../assets/images/file-icons/zip.png");
  }
  .rar::before {
    content: url("../../assets/images/file-icons/rar.png");
  }
  .excel::before {
    content: url("../../assets/images/file-icons/excel.png");
  }
}
.showImg {
  position: absolute;
  left: 0;
  right: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  button {
    position: absolute;
    right: 0;
  }
  img {
    position: absolute;
    left: 0;
    right: 0;
    margin: auto;
    top: 30%;
  }
}
</style>

<template>
  <div style="margin-top: 20px;">
    <Row>
      <Col span="2"
           style="text-align: center;"><b>序号</b></Col>
      <Col span="8"
           style="text-align: center;"><b>文件分类</b></Col>
      <Col span="14"><b>文件</b></Col>
    </Row>
    <Divider />

    <Row v-for="(item, index) in list"
         :key="item.fileCode"
         style="padding: 15px 0; border-bottom: 1px dashed #dcdee2;">
      <Col span="2"
           style="text-align: center;">{{index+1}}</Col>
      <Col span="8"
           style="text-align: center;">{{item.fileName}}</Col>
      <Col span="14">
      <template v-for="file in fileList">
        <div class="file-view"
             :key="file.newFileName"
             v-if="file.fileCode === item.fileCode && file.originalFileName">
          <div style="width: 50px;">
            <!-- <Icon class="file-icon"
                  size="36"
                  color="#8791A0"
                  :type="file.fileType === 'image' ? 'md-image' : 'md-document'" /> -->
            <Icon class="file-icon"
                  size="36"
                  color="#8791A0"
                  :custom="file.fileType" />

          </div>
          <div style="flex: 1">
            <span class="file-name">{{file.originalFileName}}</span>

            <Icon v-if="file.fileType === 'image' || file.fileType === 'pdf'"
                  class="file-icon"
                  size="20"
                  @click="handleFile(file)"
                  type="md-eye" />
            <Icon class="file-icon"
                  size="20"
                  @click="handleDown(file)"
                  type="md-download" />
          </div>
        </div>
      </template>
      </Col>
    </Row>
    <Modal v-model="modal"
           width="1000px"
           :footer-hide="true"
           title="查看文件">
      <!-- <iframe :src="src"
              frameborder="0"
              width="100%"
              height="600px"></iframe> -->
      <img :src="src"
           style="width: 100%">
    </Modal>
    <!-- <div class="showImg"
         v-if="modal">
      <button @click="!modal"
              icon="md-close"
              size="large"></button>
      <img src="src"
           alt="">
    </div> -->
  </div>
</template>

<script>
export default {
  name: 'FilesTable',

  data () {
    return {
      modal: false,
      src: '',
      imgWidth: ''
    }
  },
  props: {
    list: {
      type: Array,
      default () {
        return []
      }
    },
    fileList: {
      type: Array,
      default () {
        return []
      }
    }
  },
  computed: {
    imgWidthn: function () {
      return this.imgWidth
    }
  },
  methods: {
    handleFile (file) {
      if (file.fileType === 'image' || file.fileType === 'pdf') {
        this.modal = true
        this.src = file.viewFileUrl
        var IMG = new Image()
        IMG.src = file.viewFileUrl
        setTimeout(function () {
          var imgW = IMG.width
          console.log(imgW)
          this.imgWidth = imgW
        }, 300)

        console.log(this.imgWidth)
        // return imgW
      }
    },
    handleDown (file) {
      window.location.href = file.downloadFileUrl
    }
  }
}
</script>
