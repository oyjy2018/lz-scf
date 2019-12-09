<style lang="less">
  @import "../../../index.less";
  .sub-part {
    background-color: white;
    padding: 20px;
  }
  .tabs-div {
    background-color: white;
    padding-top: 20px;
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
  .tabs-div > .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab{
      border: 1px solid #fff;
      // border-bottom: 1px solid #dcdee2;
      border-radius: 0;
      background: #fff;
  }
  .tabs-div > .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab-active{
      border-top: 1px solid #3399ff;
      border-left: 1px solid #3399ff;
      border-right: 1px solid #3399ff;
      border-bottom: 1px solid #fff;
  }
  .tabs-div > .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-nav-container:focus .ivu-tabs-tab-focused {
    border-bottom-color: #fff !important;
  }

</style>
<template>
  <div>
    <div id="basicInfo" class="sub-part" style="height: 150px">
      <div style="height: 22px;line-height: 22px">
        <div style="height:22px;
                    font-size:16px;
                    font-weight:600;
                    color:rgba(74,74,74,1);
                    line-height:22px;
                    margin: auto;
                    float: left">{{basicInfo.projectName}}</div>&nbsp;
<!--        <div style="height:20px;line-height:20px;background:rgba(254,206,127,1);border-radius:8px 0px 8px 0px;float: left;vertical-align:middle;">-->
          <div ref="projectStatus"
               style="height:19px;
                      width:58px;
                      font-size:13px;
                      font-weight:400;
                      background:rgba(254,206,127,1);
                      color:rgba(255,255,255,1);
                      text-align: center;
                      float: left;
                      margin-left: 8px;
                      border-radius:8px 0px 8px 0px;
                      line-height:19px;">{{basicInfo.projectStatus}}
          </div>
      </div>
      <br>
      <Row>
        <Col span="1">
         <div class="column-name">项目编号</div></Col>
        <Col span="5">
          <div class="column-value">{{basicInfo.contractNo}}</div>
        </Col>
        <Col span="1">
         <div class="column-name">项目经理</div>
        </Col>
        <Col span="5">
         <div class="column-value">{{basicInfo.businessManager}}</div>
        </Col>
        <Col span="1">
         <div class="column-name">项目地址</div>
        </Col>
        <Col span="5">
         <div class="column-value">{{basicInfo.projectAddress}}</div>
        </Col>
        <Col span="1">
         <div class="column-name">项目类型</div>
        </Col>
        <Col span="5">
         <div class="column-value">{{basicInfo.projectType}}</div>
        </Col>
      </Row>
      <br>
      <Row>
        <Col span="1">
          <div class="column-name">甲方名称</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{basicInfo.firstPartyFullName}}</div>
        </Col>
        <Col span="1">
          <div class="column-name">经营模式</div>
        </Col>
        <Col span="5">
          <div class="column-value">{{basicInfo.businessModel}}</div>
        </Col>
      </Row>
    </div>
    <div style="height: 8px"></div>
    <div id="detailInfo" class="tabs-div">
      <Tabs :animated="false"  type="card" @on-click="onClickTab">
        <TabPane name="board" label="项目看板" v-if="permission.indexOf('common:lzProject:board') !== -1">
          <ProjectBoard ref="board"></ProjectBoard>
        </TabPane>
        <TabPane name="projectInfo" label="项目信息" v-if="permission.indexOf('common:lzProject:itemInfo') !== -1">
          <TabProjectInfo ref="projectInfo"></TabProjectInfo>
        </TabPane>
        <TabPane name="contractInfo" label="合同信息" v-if="permission.indexOf('common:lzProject:contract') !== -1">
          <TabContractInfo ref="contractInfo"></TabContractInfo>
        </TabPane>
        <TabPane name="projectProfitA" label="经营利润分析A" v-if="permission.indexOf('common:lzProject:analysisA') !== -1">
          <ProjectProfitA ref="projectProfitA" ></ProjectProfitA>
        </TabPane>
        <TabPane name="projectProfitB" label="经营利润分析B" v-if="permission.indexOf('common:lzProject:analysisB') !== -1">
          <ProjectProfitB ref="projectProfitB"></ProjectProfitB>
        </TabPane>
        <TabPane label="发票信息" name="invoice" v-if="permission.indexOf('common:lzProject:invoiceList') !== -1">
          <ProjectInvoice ref="invoice"></ProjectInvoice>
        </TabPane>
        <TabPane name="projectPay" label="项目付款" v-if="permission.indexOf('common:lzProject:payList') !== -1">
          <ProjectPay ref="projectPay"></ProjectPay>
        </TabPane>
        <TabPane name="gatheringTab" label="项目收款" v-if="permission.indexOf('common:lzProject:gatheringList') !== -1">
          <ProjectGathering ref="gatheringTab"></ProjectGathering>
        </TabPane>
        <TabPane name="ProjectMargins" label="保证金" v-if="permission.indexOf('common:lzProject:margins') !== -1">
          <ProjectMargins ref="ProjectMargins"></ProjectMargins>
        </TabPane>
        <TabPane name="ProjectCashPledge" label="押金" v-if="permission.indexOf('common:lzProject:getPledgeCash') !== -1">
        </TabPane>
        <TabPane label="项目借款" name="loan" v-if="permission.indexOf('common:lzProject:loanList') !== -1">
          <ProjectLoan ref="loan"></ProjectLoan>
        </TabPane>
        <TabPane label="个人借款" name="loanPerson" v-if="permission.indexOf('common:lzProject:loanList') !== -1">
          <ProjectLoanPerson ref="loanPerson"></ProjectLoanPerson>
        </TabPane>
        <TabPane label="项目保险" name="projectInsurance" v-if="permission.indexOf('common:lzProject:insuranceList') !== -1">
          <ProjectInsurance ref="projectInsurance"></ProjectInsurance>
        </TabPane>
        <TabPane label="采购物流信息" name="purchaseLogistics" v-if="permission.indexOf('common:lzProject:purchaseLogistics') !== -1">
          <ProjectPurchaseLogistics ref="purchaseLogistics"></ProjectPurchaseLogistics>
        </TabPane>
        <TabPane label="项目文件" name="file" v-if="permission.indexOf('common:lzProject:fileList') !== -1">
          <ProjectFile ref="file"></ProjectFile>
        </TabPane>
      </Tabs>
    </div>
  </div>
</template>
<script>
import { mapState, mapActions } from 'vuex'
import TabProjectInfo from './tabs/tab-project-info'
import TabContractInfo from './tabs/tab-contract-info'
import ProjectGathering from './tabs/tab-project-gathering'
import ProjectPay from './tabs/tab-project-pay'
import ProjectProfitB from './tabs/tab-project-profit-B'
import ProjectProfitA from './tabs/tab-project-profit-A'
import ProjectPurchaseLogistics from './tabs/tab-project-purchase-logistics'
import ProjectLoan from './tabs/tab-project-loan'
import ProjectLoanPerson from './tabs/tab-project-loan-person'
import ProjectFile from './tabs/tab-project-file'
import ProjectBoard from './tabs/tab-project-board'
import ProjectCashPledge from './tabs/tab-project-cash-pledge'
import ProjectInsurance from './tabs/tab-project-insurance'
import ProjectInvoice from './tabs/tab-project-invoice'
import ProjectMargins from './tabs/tab-project-margins'

export default {
  name: 'lzProjectDetail',
  components: {
    TabProjectInfo,
    TabContractInfo,
    ProjectGathering,
    ProjectPay,
    ProjectProfitB,
    ProjectProfitA,
    ProjectPurchaseLogistics,
    ProjectLoan,
    ProjectLoanPerson,
    ProjectFile,
    ProjectBoard,
    ProjectCashPledge,
    ProjectInsurance,
    ProjectInvoice,
    ProjectMargins
  },
  data () {
    return {
      basicInfo: {
        contractNo: '',
        projectName: '',
        projectStatus: '',
        businessManager: '',
        projectAddress: '',
        projectType: '',
        firstPartyFullName: '',
        businessModel: ''
      },
      hasClick: {
        'projectInfo': false,
        'contractInfo': false,
        'projectPay': false,
        'gatheringTab': false,
        'projectProfitB': false,
        'projectProfitA': false,
        'purchaseLogistics': false,
        'loan': false,
        'loanPerson': false,
        'file': false,
        'board': true, // 第一个tab页  默认打开
        'ProjectCashPledge': false,
        'projectInsurance': false,
        'invoice': false,
        'ProjectMargins': false
      }
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    this.contractNo = this.$route.query.contractNo
    this.getLzProjectBasicInfo()
    this.$refs.board.getData(this.contractNo) // 默认打开项目看板
  },
  methods: {
    ...mapActions([
      'lzProjectBasicInfo'
    ]),
    getLzProjectBasicInfo () {
      const data = {
        contractNo: this.contractNo
      }
      this.lzProjectBasicInfo(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.basicInfo = res.data.data
        }
        if (this.basicInfo.projectStatus === '已竣工') {
          this.$refs.projectStatus.style.background = 'rgba(64,204,150,1)'
        }
      })
    },
    onClickTab (value) {
      // 当前tab是否第一次点击
      if (this.hasClick[value] || this.hasClick[value] === null || this.hasClick[value] === undefined) {
        return
      }

      this.$refs[value].getData(this.basicInfo.contractNo)
      this.hasClick[value] = true
    }
  }
}
</script>
