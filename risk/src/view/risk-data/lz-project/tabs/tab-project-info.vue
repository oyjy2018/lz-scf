<style>
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
</style>
<template>
  <div style="width:1680px;
              height:782px;
              background:rgba(255,255,255,1);">
    <div style="width:1620px;height:170px;border:1px solid rgba(236,240,246,1);">
      <div style="width:1620px;height:50px;background:rgba(236,240,246,1);">
        <div style="width:2px;height:16px;background:rgba(44,142,243,1);float:left;margin-left:20px;margin-top: 17px"/>
        <div style="width:56px;height:19px;font-size:14px;font-family:MicrosoftYaHei-Bold,MicrosoftYaHei;font-weight:bold;color:rgba(85,85,85,1);line-height:19px;float:left;margin-top: 15.5px;margin-left: 3px">项目信息</div>
      </div>
      <br>
      <Row style="margin-left:20px;">
        <Col span="1">
          <div class="column-name">建筑类型</div></Col>
        <Col span="5">
          <div class="column-value">{{itemInfo.buildingType}}</div>
        </Col>
        <Col span="1">
          <div class="column-name">项目造价</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{itemInfo.projectCost}}&nbsp;元</div>
        </Col>
        <Col span="1">
          <div class="column-name">建筑面积</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{itemInfo.buildingArea}}&nbsp;m²</div>
        </Col>
        <Col span="6">
          <div class="column-name" style="float: left">所属分公司</div>
          <div class="column-value" style="float: left;margin-left: 8px">{{itemInfo.branchCompany}}</div>
        </Col>
      </Row>
      <br>
      <Row style="margin-left:20px;">
        <Col span="1">
          <div class="column-name">商务经理</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{itemInfo.businessManager}}</div>
        </Col>
        <Col span="1">
          <div class="column-name">计划日期</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{itemInfo.planCommenceDate}} 至 {{itemInfo.planCompleteDate}}</div>
        </Col>
      </Row>
    </div>
    <br>
    <div style="width:800px;height:170px;border:1px solid rgba(236,240,246,1);float: left">
      <div style="width:800px;height:50px;background:rgba(236,240,246,1);">
        <div style="width:2px;height:16px;background:rgba(44,142,243,1);float:left;margin-left:20px;margin-top: 17px"/>
        <div style="width:56px;height:19px;font-size:14px;font-family:MicrosoftYaHei-Bold,MicrosoftYaHei;font-weight:bold;color:rgba(85,85,85,1);line-height:19px;float:left;margin-top: 15.5px;margin-left: 3px">甲方信息</div>
      </div>
      <br>
      <Row style="margin-left:20px;">
        <Col span="2">
          <div class="column-name">甲方全称</div></Col>
        <Col span="22">
          <div class="column-value">{{itemInfo.firstPartyFullName}}</div>
        </Col>
      </Row>
      <br>
      <Row style="margin-left:20px;">
        <Col span="2">
          <div class="column-name">公司地址</div></Col>
        <Col span="22">
          <div class="column-value">{{itemInfo.firstPartyAddress}}</div>
        </Col>
      </Row>
    </div>
    <div style="width:800px;height:170px;border:1px solid rgba(236,240,246,1);float: left;margin-left: 20px">
      <div style="width:800px;height:50px;background:rgba(236,240,246,1);">
        <div style="width:2px;height:16px;background:rgba(44,142,243,1);float:left;margin-left:20px;margin-top: 17px"/>
        <div style="width:56px;height:19px;font-size:14px;font-family:MicrosoftYaHei-Bold,MicrosoftYaHei;font-weight:bold;color:rgba(85,85,85,1);line-height:19px;float:left;margin-top: 15.5px;margin-left: 3px">乙方信息</div>
      </div>
      <br>
      <Row style="margin-left:20px;">
        <Col span="2">
          <div class="column-name">乙方全称</div></Col>
        <Col span="22">
          <div class="column-value">{{itemInfo.secondPartyFullName}}</div>
        </Col>
      </Row>
      <br>
      <Row style="margin-left:20px;">
        <Col span="2">
          <div class="column-name">公司地址</div></Col>
        <Col span="22">
          <div class="column-value">{{itemInfo.secondPartyAddress}}</div>
        </Col>
      </Row>
    </div>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'
export default {
  name: 'TabProjectInfo',
  data () {
    return {
      itemInfo: {
        buildingType: '',
        projectCost: '',
        buildingArea: '',
        businessManager: '',
        branchCompany: '',
        planCommenceDate: '',
        planCompleteDate: '',
        firstPartyFullName: '',
        firstPartyAddress: '',
        secondPartyFullName: '',
        secondPartyAddress: ''
      }
    }
  },
  methods: {
    ...mapActions([
      'lzProjectItemInfo'
    ]),
    getData (value) {
      const data = {
        contractNo: value
      }
      this.lzProjectItemInfo(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.itemInfo = res.data.data
          this.itemInfo.projectCost = absMoney(this.itemInfo.projectCost)
        }
      })
    }
  }
}
</script>
