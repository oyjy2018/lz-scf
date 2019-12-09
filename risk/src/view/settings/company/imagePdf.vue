<template>
  <div>
    <viewer v-if="imageTruen" :options="options">
      <i-col span="4">
        <div class="detailed">
          <img class="viewerImg"
               :src="url"
               style="width:100px"
               alt="">
        </div>
      </i-col>
    </viewer>
    <div v-else>
      <div class="file-view-down"
           v-if="url.indexOf('.pdf') !== -1"
           @click="handleView(url)">
        <img src="@/assets/images/pdf.png">
        <div class="file-view-down-content">
          <span>{{ fileName }}</span>
          <Icon type="ios-eye-outline"
                :size="18"></Icon>
        </div>
      </div>
      <div v-else
           class="upload-items">
        <div class="upload-list">
          <img :src="url">
          <div class="upload-list-cover">
            <Icon type="ios-eye-outline"
                  @click.native="handleView(url)"></Icon>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'image-type',
  props: {
    url: {
      type: String,
      default () {
        return {}
      }
    },
    fileName: {
      type: String,
      default () {
        return {}
      }
    }
  },
  data: function () {
    return {
      type: '',
      imageTrue: null,
      options: {
        title: false
      }
    }
  },
  mounted () {
    var _this = this
    setTimeout(function () {
      _this.getFileName(_this.fileName)
    }, 300)
  },
  computed: {
    imageTruen () {
      return this.imageTrue
    }
  },
  methods: {
    getFileName (Name) {
      if (Name === null) return
      var index = Name.lastIndexOf('.')
      var ext = Name.substr(index + 1)
      // return ext
      let imgArry = ['gif', 'jpg', 'jpeg', 'png', 'GIF', 'JPG', 'PNG', 'bmp']
      if (imgArry.indexOf(ext) > -1) {
        this.imageTrue = true
      } else {
        this.imageTrue = false
      }
      return this.imageTrue
    },
    handleView () {
      this.$emit('handle-view', this.url)
    }
  }
}
</script>
<style lang="less">
.viewerImg:hover {
  filter: brightness(0.6);
  cursor: pointer;
}
</style>
