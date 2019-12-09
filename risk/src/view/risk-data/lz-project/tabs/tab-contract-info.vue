<style lang="less" scoped>
  .divStyle1 {
    width:1780px;
    height:782px;
    background:rgba(255,255,255,1);
  }
  .divStyle2 {
    width:1710px;
    height:170px;
    border:1px solid rgba(236,240,246,1);
  }
  .divStyle3 {
    width:1620px;
    height:50px;
    background:rgba(236,240,246,1);
  }
  .divStyle4 {
    width:2px;
    height:16px;
    background:rgba(44,142,243,1);
    float:left;
    margin-left:20px;
    margin-top: 17px
  }
  .divStyle5 {
    width:400px;
    height:19px;
    font-size:14px;
    font-family:MicrosoftYaHei-Bold,MicrosoftYaHei;
    font-weight:bold;
    color:rgba(85,85,85,1);
    line-height:19px;
    float:left;
    margin-top: 15.5px;
    margin-left: 3px
  }

  .column-name {
    height:19px;
    font-size:14px;
    font-family:MicrosoftYaHei;
    color:rgba(102,102,102,1);
    line-height:19px;
  }
  .column-value {
    height:19px;
    font-size:14px;
    font-family:MicrosoftYaHei;
    color:rgba(34,34,34,1);
    line-height:19px;
  }
  .file-name {
    cursor: pointer;
    margin-left: 15px;
    text-decoration: underline;
    color: #2c8ef3;
  }
</style>
<template>
  <div class="divStyle1">
    <div class="divStyle2">
      <div class="divStyle3">
        <div class="divStyle4"/>
        <div class="divStyle5">{{basic.projectName}}</div>
      </div>
      <br>
      <Row style="margin-left:20px;">
        <Col span="1">
          <div class="column-name">合同编号</div></Col>
        <Col span="5">
          <div class="column-value">{{basic.subContractNo}}</div>
        </Col>
        <Col span="1">
          <div class="column-name">合同日期</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{basic.contractBeginDate | formatDate}} 至 {{basic.contractEndDate | formatDate}}</div>
        </Col>
        <Col span="1">
          <div class="column-name">合同金额</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{basic.contractMoney | formatMoney}}</div>
        </Col>
        <Col span="1">
          <div class="column-name">合同电子版</div>
        </Col>
        <Col span="5">
          <div class="column-value">
              <span class="file-name"
                    v-if="basic.fileUrl"
                    @click="handleFile(basic.fileUrl)">主合同.pdf</span>
              <span style="margin-left: 15px;" v-else>——</span>
          </div>
        </Col>
      </Row>
      <br>
      <Row style="margin-left:20px;">
        <Col span="1">
          <div class="column-name">合同甲方</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{basic.firstPartyFullName}}</div>
        </Col>
        <Col span="1">
          <div class="column-name">合同乙方</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{basic.secondPartyFullName}}</div>
        </Col>
      </Row>
    </div>
    <br>

    <div v-for="(item,index) in this.sub" :key="index">
      <div class="divStyle2">
        <div class="divStyle3">
          <div class="divStyle4"/>
          <div class="divStyle5">{{item.contractType | formatTitle}}</div>
        </div>
        <br>
        <Row style="margin-left:20px;">
          <Col span="1">
            <div class="column-name">合同编号</div>
          </Col>
          <Col span="5">
            <div class="column-value">{{item.subContractNo}}</div>
          </Col>
          <Col span="1">
            <div class="column-name">合同日期</div>
          </Col>
          <Col span="5">
            <div class="column-value">{{item.contractBeginDate | formatDate}} 至 {{item.contractEndDate | formatDate}}</div>
          </Col>
          <Col span="1">
            <div class="column-name">合同金额</div>
          </Col>
          <Col span="5">
            <div class="column-value">{{item.contractMoney | formatMoney}}</div>
          </Col>
          <Col span="1">
            <div class="column-name">合同电子版</div>
          </Col>
          <Col span="5">
              <span class="file-name"
                    v-if="item.fileUrl"
                    @click="handleFile(item.fileUrl)">{{item.contractType | formatPDF}}</span>
              <span style="margin-left: 15px;" v-else>——</span>
          </Col>
        </Row>
        <br>
        <Row style="margin-left:20px;">
          <Col span="1">
            <div class="column-name">合同甲方</div>
          </Col>
          <Col span="5">
            <div class="column-value">{{item.firstPartyFullName}}</div>
          </Col>
          <Col span="1">
            <div class="column-name">合同乙方</div>
          </Col>
          <Col span="5">
            <div class="column-value">{{item.secondPartyFullName}}</div>
          </Col>
        </Row>
      </div>
      <br>
    </div>
    <div>
      <Modal v-model="visible"
          width="800px"
          :footer-hide="true"
          title="查看文件">
      <iframe :src="viewUrl"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
     </Modal>
    </div>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'
export default {
  name: 'TabContractInfo',
  data () {
    return {
      visible: false,
      viewUrl: '',
      basic: {
        projectName: '',
        subContractNo: '',
        contractBeginDate: '',
        contractEndDate: '',
        contractMoney: '',
        firstPartyFullName: '',
        secondPartyFullName: '',
        fileUrl: ''
      },
      sub: []
    }
  },
  filters: {
    formatMoney (val) {
      if (!val) return ''
      return absMoney(val) + '元'
    },
    formatDate (val) {
      return !val ? '——' : val
    },
    formatTitle (val) {
      return val === 1 ? '劳务合同' : '购销合同'
    },
    formatPDF (val) {
      return val === 1 ? '劳务合同.pdf' : '购销合同.pdf'
    }
  },
  methods: {
    ...mapActions([
      'lzProjectContractInfo'
    ]),
    getData (value) {
      const data = {
        contractNo: value
      }
      this.lzProjectContractInfo(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          let _data = res.data.data
          if (_data.basic) {
            this.basic = _data.basic
          }
          if (_data.sub) {
            this.sub = _data.sub
          }
        }
      })
    },
    handleFile (url) {
      this.visible = true
      this.viewUrl = url
    }
  }
}
</script>
