<template>
  <div>
    <Row style="background:#eee;padding:20px">
      <Col span="11">
        <Card :bordered="false">
          <p slot="title">公司信息</p>
          <Form class="form" style="width: 600px;" ref="form" :label-width="180">
            <FormItem prop="blicUrl"
                label="营业执照">
              <image-pdf :url="form.blicUrl"
                        :file-name="form.blicUrlFileName"
                        @handle-view="handleView"></image-pdf>
            </FormItem>
            <FormItem prop="companyName"
                      label="公司全称">
              {{form.companyName}}
            </FormItem>
            <FormItem prop="creditCode"
                      label="统一社会信用代码">
              {{form.creditCode}}
            </FormItem>
            <FormItem prop="provinceName"
                      label="公司地址">
              {{form.provinceName}}-{{form.cityName}}
            </FormItem>
            <FormItem prop="detailAddr"
                      label="详细地址">
              {{form.detailAddr}}
            </FormItem>
            <FormItem prop="blicEndDate"
                      label="营业期限（止）">
              {{form.blicEndDate}}
            </FormItem>

            <FormItem prop="companyNature"
                      label="行业大类">
              {{form.companyNature}}
            </FormItem>
            <FormItem prop="companyNatureConcrete"
                      label="具体行业">
              {{form.companyNatureConcrete}}
            </FormItem>
            <FormItem prop="staffSize"
                      label="公司规模">
              {{form.staffSize}}
            </FormItem>
            <FormItem prop="contactNumber"
                      label="联系电话">
              {{form.contactNumber}}
            </FormItem>
          </Form>
        </Card>
      </Col>
    </Row>
    <Row style="background:#eee;padding:20px">
      <Col span="11">
        <Card :bordered="false">
          <p slot="title">法人信息</p>
          <Form class="form" style="width: 600px;" ref="form" :label-width="180">
            <FormItem prop="legalPersonCertBackUrl"
                label="身份证头像页">
              <image-pdf :url="form.legalPersonCertBackUrl"
                        :file-name="form.legalPersonCertBackFileName"
                        @handle-view="handleView"></image-pdf>
            </FormItem>
            <FormItem prop="legalPersonCertFrontUrl"
                      label="身份证国徽页">
              <image-pdf :url="form.legalPersonCertFrontUrl"
                        :file-name="form.legalPersonCertFrontFileName"
                        @handle-view="handleView"></image-pdf>
            </FormItem>
            <FormItem prop="legalPersonName"
                      label="姓名">
              {{form.legalPersonName}}
            </FormItem>
            <FormItem prop="legalPersonCertNo"
                      label="身份证号">
              {{form.legalPersonCertNo}}
            </FormItem>
            <FormItem prop="legalPersonCertEndDate"
                      label="有效期限（止）">
              {{form.legalPersonCertEndDate}}
            </FormItem>
          </Form>
        </Card>
      </Col>
    </Row>
    <Row style="background:#eee;padding:20px">
      <Col span="11">
        <Card :bordered="false">
          <p slot="title">被授权人信息</p>
          <Form class="form" style="width: 600px;" ref="form" :label-width="180">
            <FormItem prop="porxyPersonCertBackUrl"
                label="被授权人身份证头像页">
              <image-pdf :url="form.porxyPersonCertBackUrl"
                        :file-name="form.porxyPersonCertBackFileName"
                        @handle-view="handleView"></image-pdf>

            </FormItem>
            <FormItem prop="porxyPersonCertFrontUrl"
                      label="被授权人身份证国徽页">
              <image-pdf :url="form.porxyPersonCertFrontUrl"
                        :file-name="form.porxyPersonCertFrontFileName"
                        @handle-view="handleView"></image-pdf>
            </FormItem>
            <FormItem prop="porxyPersonName"
                      label="姓名">
              {{form.porxyPersonName}}
            </FormItem>
            <FormItem prop="porxyPersonEmail"
                      label="被授权人邮箱">
              {{form.porxyPersonEmail}}
            </FormItem>
            <FormItem prop="porxyPersonCertNo"
                      label="身份证号">
              {{form.porxyPersonCertNo}}
            </FormItem>
            <FormItem prop="porxyPersonCertEndDate"
                      label="有效期限（止）">
              {{form.porxyPersonCertEndDate}}
            </FormItem>
            <FormItem v-show="form.porxyCommissionUrl"
                      prop="porxyCommissionUrl"
                      label="授权委托书">
              <image-pdf :url="form.porxyCommissionUrl"
                        :file-name="form.porxyCommissionFileName"
                        @handle-view="handleView"></image-pdf>
            </FormItem>
          </Form>
        </Card>
      </Col>
    </Row>
    <Row style="background:#eee;padding:20px">
      <Col span="11">
        <Card :bordered="false">
          <p slot="title">审批信息</p>
          <Form class="form" style="width: 600px;" ref="form" :label-width="180">
            <FormItem label="公司类别"
                prop="companyCategory">
              {{form.companyCategory}}
            </FormItem>
            <FormItem v-if="ticketSty"
                      label="绑定的系统版本1"
                      prop="ticketSystemVersionId">
              <Row>
                <Col span="11">
                <Select value="商票交易平台"
                        disabled
                        placeholder="选择公司类别">
                  <Option value="商票交易平台">商票交易平台</Option>
                </Select>
                </Col>
                <Col span="11"
                    offset="2">
                {{ form.ticketSystemVersionName }}
                </Col>
              </Row>
            </FormItem>
            <FormItem v-if="controlSty"
                      label="绑定的系统版本2"
                      prop="riskControlSystemVersionId">
              <Row>
                <Col span="11">
                <Select value="风控平台"
                        disabled
                        placeholder="选择公司类别">
                  <Option value="风控平台">风控平台</Option>
                </Select>
                </Col>
                <Col span="11"
                    offset="2">
                {{ form.riskControlSystemVersionName }}
                </Col>
              </Row>
            </FormItem>
            <FormItem v-if="controlSty"
                      label="可使用的业务流程"
                      prop="flowList">
              <Select multiple
                      disabled
                      v-model="form.flowList"
                      placeholder="选择可使用的业务流程">
                <Option value="1">授信审批流程</Option>
                <Option value="2">用信审批流程</Option>
              </Select>
            </FormItem>
          </Form>
        </Card>
      </Col>
    </Row>
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
</template>
<script>
import { mapActions } from 'vuex'
import ImagePdf from './imagePdf.vue'

export default {
  name: 'company-detail',
  components: {
    ImagePdf
  },
  data () {
    return {
      controlSty: false,
      ticketSty: false,
      form: {
        'blicUrl': '',
        'blicUrlFileName': '',
        'companyName': '',
        'creditCode': '',
        'provinceName': '',
        'cityName': '',
        'detailAddr': '',
        'blicEndDate': '',
        'companyNature': '',
        'companyNatureConcrete': '',
        'staffSize': '',
        'contactNumber': '',
        'legalPersonCertFrontUrl': '',
        'legalPersonCertFrontFileName': '',
        'legalPersonCertBackUrl': '',
        'legalPersonCertBackFileName': '',
        'legalPersonName': '',
        'legalPersonCertNo': '',
        'legalPersonCertEndDate': '',
        'aupisLep': 0,
        'porxyPersonCertFrontUrl': '',
        'porxyPersonCertFrontFileName': '',
        'porxyPersonCertBackUrl': '',
        'porxyPersonCertBackFileName': '',
        'porxyPersonName': '',
        'porxyPersonCertNo': '',
        'porxyPersonCertEndDate': '',
        'porxyCommissionUrl': '',
        'porxyCommissionFileName': '',
        'porxyPersonEmail': '',
        'porxyPersonPassword': '',
        'read': []
      },
      viewUrl: '',
      visible: false
    }
  },
  mounted () {
    const id = this.$route.query.id
    if (id) {
      this.getCompanyAuditBasicInfo({ companyAuditId: id }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          if (res.data.data.systemIdList) {
            this.controlSty = res.data.data.systemIdList.indexOf('1') !== -1
            this.ticketSty = res.data.data.systemIdList.indexOf('2') !== -1
          }
          if (res.data.data.flowList) {
            res.data.data.flowList = res.data.data.flowList.split(',')
          }
          this.form = res.data.data
        }
      })
    }
  },
  methods: {
    ...mapActions([
      'getCompanyAuditBasicInfo'
    ]),
    handleView (viewUrl) {
      this.viewUrl = viewUrl
      this.visible = true
    }
  }
}
</script>
