<style lang="less" scoped>
.upload-list {
  display: inline-block;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 10px;
}
.upload-list img {
  width: 100%;
  height: 100%;
}
.upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.upload-list:hover .upload-list-cover {
  display: block;
}
.upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
</style>
<template>
  <div>
    <Card title="商票详情">
      <Form ref="form"
            :model="form"
            :label-width="120"
            style="width: 600px">
        <h4>基本信息</h4>
        <Divider />
        <FormItem label="商票正面">
          <!-- <div class="upload-list">
            <img :src="form.ticketFrontImgViewUrl">
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(form.ticketFrontImgViewUrl)"></Icon>
            </div>
          </div> -->
          <viewer>
            <i-col span="4">
              <div class="detailed">
                <img class="viewerImg"
                     :src="form.ticketFrontImgViewUrl"
                     style="width:100px"
                     alt="">
              </div>
            </i-col>
          </viewer>
        </FormItem>
        <FormItem label="商票背面">
          <!-- <div class="upload-list">
            <img :src="form.ticketBackImgViewUrl">
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(form.ticketBackImgViewUrl)"></Icon>
            </div>
          </div> -->
          <viewer>
            <i-col span="4">
              <div class="detailed">
                <img class="viewerImg"
                     :src="form.ticketBackImgViewUrl"
                     style="width:100px"
                     alt="">
              </div>
            </i-col>
          </viewer>
        </FormItem>
        <Row>
          <Col span="12">
          <FormItem label="用信ID"
                    prop="creditUseId">
            <Input v-model="form.creditUseId"
                   disabled
                   placeholder="用信ID"></Input>
          </FormItem>
          </Col>
          <Col span="12">
          <FormItem label="关联授信ID"
                    prop="creditId">
            <Input v-model="form.creditId"
                   disabled
                   placeholder="关联授信ID"></Input>
          </FormItem>
          </Col>
        </Row>
        <!-- <FormItem label="用信类型"
                  prop="useType">
          <Select v-model="form.useType"
                  disabled
                  placeholder="选择开户行">
            <Option :value="0">开商票</Option>
          </Select>
        </FormItem> -->
        <Row>
          <Col span="12">
          <FormItem label="出票日期"
                    prop="ticketStart">
            <DatePicker :editable="false"
                        type="date"
                        disabled
                        :value="form.ticketStart"
                        placeholder="选择出票日期"
                        format="yyyy-MM-dd"
                        style="width: 180px"></DatePicker>
          </FormItem>
          </Col>
          <Col span="12">
          <FormItem label="汇票到期日"
                    prop="ticketEnd">
            <DatePicker :editable="false"
                        type="date"
                        disabled
                        :value="form.ticketEnd"
                        format="yyyy-MM-dd"
                        placeholder="选择汇票到期日"
                        style="width: 180px"></DatePicker>
          </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="14">
          <FormItem label="票据号码"
                    prop="ticketNo">
            <Input v-model="form.ticketNo"
                   disabled
                   placeholder="输入票据号码"></Input>
          </FormItem>
          </Col>
          <Col span="10">
          <!-- <FormItem label="票据状态"
                    prop="ticketStatus">
            <Select v-model="form.ticketStatus"
                    disabled
                    placeholder="选择票据状态"
                    style="width: 130px">
              <Option :value="0">已开票</Option>
              <Option :value="1">已收票</Option>
              <Option :value="2">提示付款中</Option>
              <Option :value="3">付款签收中</Option>
              <Option :value="4">已解付</Option>
              <Option :value="5">贴现申请中</Option>
              <Option :value="6">背书签收中</Option>
              <Option :value="7">背书已签收</Option>
            </Select>
          </FormItem> -->
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <FormItem label="票据金额"
                    prop="ticketMoney">
            <Input v-model="form.ticketMoney"
                   disabled
                   placeholder="可输入小数"></Input>
          </FormItem>
          </Col>
          <Col span="12">
          <FormItem label="能否转让"
                    prop="isTransfer">
            <RadioGroup v-model="form.isTransfer">
              <Radio :label="1"
                     disabled>可转让</Radio>
              <Radio :label="0"
                     disabled>不可转让</Radio>
            </RadioGroup>
          </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <FormItem label="交易合同号"
                    prop="tradeContractNo">
            <Input v-model="form.tradeContractNo"
                   disabled
                   placeholder="请输入交易合同号"></Input>
          </FormItem>
          </Col>
          <Col span="12">
          <!-- <FormItem label="商票状态"
                    prop="isTransfer">
            <Select v-model="form.status"
                    placeholder="选择票据状态"
                    style="width: 130px">
              <Option :value="0">开商票</Option>
            </Select>
          </FormItem> -->
          </Col>
        </Row>

        <h4>出票人</h4>
        <Divider />
        <FormItem label="公司全称"
                  prop="ticketGiveCompany">
          <Input v-model="form.ticketGiveCompany"
                 disabled
                 placeholder="请输入公司全称"></Input>
        </FormItem>

        <h4>收票人</h4>
        <Divider />
        <FormItem label="公司全称"
                  prop="ticketGetCompany">
          <Input v-model="form.ticketGetCompany"
                 disabled
                 placeholder="请输入公司全称"></Input>
        </FormItem>

        <h4>承兑人信息</h4>
        <Divider />
        <FormItem label="公司全称"
                  prop="acceptorCompany">
          <Input v-model="form.acceptorCompany"
                 disabled
                 placeholder="请输入公司全称"></Input>
        </FormItem>
        <FormItem label="开户行行号"
                  prop="acceptorBankNo">
          <Input v-model="form.acceptorBankNo"
                 disabled
                 placeholder="请输入开户行行号"></Input>
        </FormItem>
        <FormItem label="账号"
                  prop="acceptorAccount">
          <Input v-model="form.acceptorAccount"
                 disabled
                 placeholder="请输入账号"></Input>
        </FormItem>
        <FormItem label="开户行名称"
                  prop="acceptorBank">
          <Select v-model="form.acceptorBank"
                  disabled
                  placeholder="选择开户行">
            <Option v-for="item in banks"
                    :value="item"
                    :key="item">{{item}}</Option>
          </Select>
        </FormItem>
        <FormItem label="开户行省市"
                  prop="acceptorBankProvinceCode">
          <Cascader :data="citys"
                    size="large"
                    trigger="hover"
                    disabled
                    v-model="form.acceptorBankProvinceCode"
                    placeholder="选择开户行省市"></Cascader>
        </FormItem>
        <FormItem label="开户行支行"
                  prop="acceptorBankBranch">
          <Input v-model="form.acceptorBankBranch"
                 disabled
                 placeholder="请输入开户行支行"></Input>
        </FormItem>
        <br><br>
      </Form>
    </Card>
    <Modal title="查看图片"
           v-model="visible"
           width="800px">
      <img :src="viewUrl"
           v-if="visible"
           style="width: 100%">
      <div slot="footer"></div>
    </Modal>
  </div>
</template>

<script>
import { CITYS } from '@/libs/citys.js'
import { mapActions, mapMutations } from 'vuex'
import { BANKS } from '@/libs/status.js'

export default {
  name: 'ticketdetail',
  data () {
    return {
      viewUrl: '',
      visible: false,
      banks: BANKS,
      form: {
        acceptorAccount: '',
        acceptorBank: '',
        acceptorBankBranch: '',
        acceptorBankCityCode: '',
        acceptorBankNo: '',
        acceptorBankProvinceCode: [],
        acceptorCompany: '',
        isTransfer: 0,
        ticketEnd: '',
        ticketGetCompany: '',
        ticketGiveCompany: '',
        ticketMoney: '',
        ticketNo: '',
        ticketStart: '',
        ticketStatus: 0,
        tradeContractNo: '',
        useType: 0
      },
      id: ''
    }
  },
  computed: {
    citys () {
      return CITYS.map(item => {
        item.children.map(subItem => {
          delete subItem.children
          return subItem
        })
        return item
      })
    }
  },
  mounted () {
    const { id } = this.$route.query
    if (id) {
      this.id = id
      this.getData()
    }
  },
  methods: {
    ...mapActions([
      'ticketDetail'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getData () {
      const data = {
        id: this.id
      }
      this.ticketDetail(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.form = { ...this.form, ...res.data.data.detail }
          this.form.acceptorBankProvinceCode = [this.form.acceptorBankProvinceCode, this.form.acceptorBankCityCode]
        }
      })
    },
    handleView (viewUrl) {
      this.viewUrl = viewUrl
      this.visible = true
    }
  }
}
</script>
