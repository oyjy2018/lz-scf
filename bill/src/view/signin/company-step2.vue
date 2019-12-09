<template>
  <Form class="form"
        style="padding-bottom: 240px; width: 600px;"
        ref="form"
        :model="form"
        :rules="rules"
        :label-width="180"
        @keydown.enter.native="handleSubmit">
    <h3>公司信息</h3>
    <Divider dashed />
    <FormItem prop="blicUrl"
              label="营业执照"
              :rules="{ required: true, message: '请上传营业执照', trigger: 'change' }">
      <div class="upload-items">
        <div class="upload-list"
             v-for="item in uploadList.blicUrl"
             :key="item.name">
          <template v-if="item.status === 'finished'">
            <img v-if="item.fileType === 'image'"
                 :src="item.viewFileUrl">
            <span v-else>PDF</span>
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewFileUrl)"></Icon>
              <Icon type="ios-trash-outline"
                    @click.native="handleRemove(item, 'blicUrl')"></Icon>
            </div>
          </template>
          <template v-else>
            <Progress v-if="item.showProgress"
                      :percent="item.percentage"
                      hide-info></Progress>
          </template>
        </div>
        <Upload ref="blicUrl"
                :show-upload-list="false"
                :on-success="handleSuccess"
                :accept="accept"
                :format="format"
                :max-size="5120"
                :multiple="false"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="() => handleBeforeUpload('blicUrl')"
                type="drag"
                :action="upUrl"
                style="display: inline-block;width:100px;">
          <div class="upload-add">
            <Icon type="ios-add"
                  size="40"></Icon>
            <span>上传营业执照</span>
          </div>
        </Upload>
        <div class="upload-list"
             style="margin-left: 10px;">
          <img src="../../assets/images/example/blic-s.png">
          <div class="upload-list-cover"
               style="display: block;"
               @click="handleView(blic)">
            <span style="color:#fff;cursor: pointer;">查看示例</span>
            <Icon type="ios-eye-outline"></Icon>
          </div>
        </div>
      </div>
    </FormItem>
    <FormItem prop="companyName"
              label="公司全称"
              :rules="{ required: true, message: '请输入公司全称', trigger: 'blur' }">
      <Row>
        <Col span="10">
        <Input v-model="form.companyName"
               placeholder="请输入公司全称"
               size="large" />
        </Col>
        <Col span="9"
             offset="1">
        <p>请填写营业执照中完整的公司全称,注册后不可变更</p>
        </Col>
      </Row>
    </FormItem>
    <FormItem prop="creditCode"
              label="统一社会信用代码">
      <Input v-model="form.creditCode"
             :maxlength="18"
             placeholder="请输入18位统一社会信用代码"
             size="large" />
    </FormItem>
    <FormItem prop="provinceName"
              label="公司地址">
      <Row>
        <Col span="24">
        <al-selector data-type="name"
                     size="large"
                     level="1"
                     v-model="provinceCity"
                     @on-change="handleSelector" />
        </Col>
      </Row>
    </FormItem>
    <FormItem prop="detailAddr"
              label="详细地址"
              :rules="{ required: true, message: '请输入详细地址', trigger: 'blur' }">
      <Input v-model="form.detailAddr"
             placeholder="请输入详细地址"
             size="large" />
    </FormItem>
    <FormItem prop="blicEndDate"
              label="营业期限（止）"
              :rules="[{ required: true, message: '请选择营业期限', trigger: 'change' }, dateRules]">
      <RadioGroup v-model="isBlicEndDate"
                  @on-change="(value) => form.blicEndDate = value">
        <Radio label="长期"></Radio>
        <Radio label="固定日期"></Radio>
      </RadioGroup>
      <DatePicker :editable="false"
                  type="date"
                  placeholder="选择固定日期"
                  :disabled="isBlicEndDate === '长期'"
                  v-model="form.blicEndDate"
                  @on-change="(value) => form.blicEndDate = value"
                  format="yyyy-MM-dd"
                  style="width: 200px"></DatePicker>
    </FormItem>

    <FormItem prop="companyNature"
              label="行业大类"
              :rules="{ required: true, message: '请选择行业大类', trigger: 'change' }">
      <Select v-model="form.companyNature"
              @on-change="getNatureSelect"
              size="large"
              placeholder="请选择行业大类">
        <Option v-for="item in natureList"
                :value="item.dictValue"
                :key="item.id">{{ item.dictValue }}</Option>
      </Select>
    </FormItem>
    <FormItem prop="companyNatureConcrete"
              label="具体行业"
              :rules="{ required: true, message: '请选择具体行业', trigger: 'change' }">
      <Select v-model="form.companyNatureConcrete"
              size="large"
              placeholder="请选择具体行业">
        <Option v-for="item in NatureConcreteList"
                :value="item.dictValue"
                :key="item.id">{{ item.dictValue }}</Option>
      </Select>
    </FormItem>
    <FormItem prop="staffSize"
              label="公司规模"
              :rules="{ required: true, message: '请选择公司规模', trigger: 'change' }">
      <Select v-model="form.staffSize"
              size="large"
              placeholder="请选择公司规模">
        <Option v-for="item in staffsizeList"
                :value="item.dictValue"
                :key="item.id">{{ item.dictValue }}</Option>
      </Select>
    </FormItem>
    <FormItem prop="contactNumber"
              label="联系电话"
              :rules="{ required: true, message: '请输入公司联系电话', trigger: 'blur' }">
      <Input v-model="form.contactNumber"
             placeholder="请输入公司联系电话"
             size="large" />
    </FormItem>
    <h3>法人信息</h3>
    <Divider dashed />

    <FormItem prop="legalPersonCertBackUrl"
              label="身份证头像页"
              :rules="{ required: true, message: '请上传身份证头像页', trigger: 'change' }">
      <div class="upload-items">
        <div class="upload-list"
             v-for="item in uploadList.legalPersonCertBackUrl"
             :key="item.name">
          <template v-if="item.status === 'finished'">
            <img v-if="item.fileType === 'image'"
                 :src="item.viewFileUrl">
            <span v-else>PDF</span>
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewFileUrl)"></Icon>
              <Icon type="ios-trash-outline"
                    @click.native="handleRemove(item, 'legalPersonCertBackUrl')"></Icon>
            </div>
          </template>
          <template v-else>
            <Progress v-if="item.showProgress"
                      :percent="item.percentage"
                      hide-info></Progress>
          </template>
        </div>
        <Upload ref="legalPersonCertBackUrl"
                :show-upload-list="false"
                :on-success="handleSuccess"
                :accept="accept"
                :format="format"
                :max-size="5120"
                :multiple="false"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="() => handleBeforeUpload('legalPersonCertBackUrl')"
                type="drag"
                :action="upUrl"
                style="display: inline-block;width:100px;">
          <div class="upload-add">
            <Icon type="ios-add"
                  size="40"></Icon>
            <span>上传身份证头像页</span>
          </div>
        </Upload>
        <div class="upload-list"
             style="margin-left: 10px;">
          <img src="../../assets/images/example/idcard-f-s.png">
          <div class="upload-list-cover"
               style="display: block;"
               @click="handleView(idcardf)">
            <span style="color:#fff;cursor: pointer;">查看示例</span>
            <Icon type="ios-eye-outline"></Icon>
          </div>
        </div>
      </div>
    </FormItem>
    <FormItem prop="legalPersonCertFrontUrl"
              label="身份证国徽页"
              :rules="{ required: true, message: '请上传身份证国徽页', trigger: 'change' }">
      <div class="upload-items">
        <div class="upload-list"
             v-for="item in uploadList.legalPersonCertFrontUrl"
             :key="item.name">
          <template v-if="item.status === 'finished'">
            <img v-if="item.fileType === 'image'"
                 :src="item.viewFileUrl">
            <span v-else>PDF</span>
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewFileUrl)"></Icon>
              <Icon type="ios-trash-outline"
                    @click.native="handleRemove(item, 'legalPersonCertFrontUrl')"></Icon>
            </div>
          </template>
          <template v-else>
            <Progress v-if="item.showProgress"
                      :percent="item.percentage"
                      hide-info></Progress>
          </template>
        </div>
        <Upload ref="legalPersonCertFrontUrl"
                :show-upload-list="false"
                :on-success="handleSuccess"
                :accept="accept"
                :format="format"
                :max-size="5120"
                :multiple="false"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="() => handleBeforeUpload('legalPersonCertFrontUrl')"
                type="drag"
                :action="upUrl"
                style="display: inline-block;width:100px;">
          <div class="upload-add">
            <Icon type="ios-add"
                  size="40"></Icon>
            <span>上传身份证国徽页</span>
          </div>
        </Upload>
        <div class="upload-list"
             style="margin-left: 10px;">
          <img src="../../assets/images/example/idcard-b-s.png">
          <div class="upload-list-cover"
               style="display: block;"
               @click="handleView(idcardb)">
            <span style="color:#fff;cursor: pointer;">查看示例</span>
            <Icon type="ios-eye-outline"></Icon>
          </div>
        </div>
      </div>
    </FormItem>
    <FormItem prop="legalPersonName"
              label="姓名"
              :rules="{ required: true, message: '请根据营业执照输入法人姓名', trigger: 'blur' }">
      <Input v-model="form.legalPersonName"
             placeholder="请根据营业执照输入法人姓名"
             size="large"
             @on-blur="handleBlurName" />
    </FormItem>
    <FormItem prop="legalPersonCertNo"
              label="身份证号">
      <Input v-model="form.legalPersonCertNo"
             placeholder="请输入法人身份证号"
             size="large"
             @on-blur="handleBlurIdcard" />
    </FormItem>
    <FormItem prop="legalPersonCertEndDate"
              label="有效期限（止）"
              :rules="[{ required: true, message: '请选择有效期限', trigger: 'change' }, dateRules]">
      <RadioGroup v-model="isLegalPersonCertEndDate"
                  @on-change="(value) => {
                    form.legalPersonCertEndDate = value
                    if (aupisLep) {
                      form.porxyPersonCertEndDate = value
                    }
                  }">
        <Radio label="长期"></Radio>
        <Radio label="固定日期"></Radio>
      </RadioGroup>
      <DatePicker :editable="false"
                  type="date"
                  placeholder="请选择固定日期"
                  :disabled="isLegalPersonCertEndDate === '长期'"
                  @on-change="(value) => {
                    form.legalPersonCertEndDate = value
                    if (aupisLep) {
                      form.porxyPersonCertEndDate = value
                    }
                  }"
                  v-model="form.legalPersonCertEndDate"
                  format="yyyy-MM-dd"
                  style="width: 200px"></DatePicker>
    </FormItem>
    <h3>被授权人身份信息</h3>
    <Divider dashed />

    <FormItem>
      <Checkbox v-model="aupisLep"
                @on-change="handleChangeCheckbox">被授权人同法人</Checkbox>
    </FormItem>
    <FormItem prop="porxyPersonCertBackUrl"
              label="被授权人身份证头像页"
              :rules="{ required: true, message: '请上传被授权人身份证头像页', trigger: 'change' }">
      <Input v-model="form.porxyPersonCertBackUrl"
             style="display:none" />
      <div class="upload-items">
        <div class="upload-list"
             v-for="item in uploadList.porxyPersonCertBackUrl"
             :key="item.name">
          <template v-if="item.status === 'finished'">
            <img v-if="item.fileType === 'image'"
                 :src="item.viewFileUrl">
            <span v-else>PDF</span>
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewFileUrl)"></Icon>
              <Icon v-if="!aupisLep"
                    type="ios-trash-outline"
                    @click.native="handleRemove(item, 'porxyPersonCertBackUrl')"></Icon>
            </div>
          </template>
          <template v-else>
            <Progress v-if="item.showProgress"
                      :percent="item.percentage"
                      hide-info></Progress>
          </template>
        </div>
        <Upload ref="porxyPersonCertBackUrl"
                :show-upload-list="false"
                :on-success="handleSuccess"
                :accept="accept"
                :format="format"
                :max-size="5120"
                :multiple="false"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="() => handleBeforeUpload('porxyPersonCertBackUrl')"
                type="drag"
                :action="upUrl"
                style="display: inline-block;width:100px;">
          <div class="upload-add">
            <Icon type="ios-add"
                  size="40"></Icon>
            <span>上传被授权人身份证头像页</span>
          </div>
        </Upload>
        <div class="upload-list"
             style="margin-left: 10px;">
          <img src="../../assets/images/example/idcard-f-s.png">
          <div class="upload-list-cover"
               style="display: block;"
               @click="handleView(idcardf)">
            <span style="color:#fff;cursor: pointer;">查看示例</span>
            <Icon type="ios-eye-outline"></Icon>
          </div>
        </div>
      </div>
    </FormItem>
    <FormItem prop="porxyPersonCertFrontUrl"
              label="被授权人身份证国徽页"
              :rules="{ required: true, message: '请上传被授权人身份证国徽页', trigger: 'change' }">
      <Input v-model="form.porxyPersonCertFrontUrl"
             style="display:none" />
      <div class="upload-items">
        <div class="upload-list"
             v-for="item in uploadList.porxyPersonCertFrontUrl"
             :key="item.name">
          <template v-if="item.status === 'finished'">
            <img v-if="item.fileType === 'image'"
                 :src="item.viewFileUrl">
            <span v-else>PDF</span>
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewFileUrl)"></Icon>
              <Icon v-if="!aupisLep"
                    type="ios-trash-outline"
                    @click.native="handleRemove(item, 'porxyPersonCertFrontUrl')"></Icon>
            </div>
          </template>
          <template v-else>
            <Progress v-if="item.showProgress"
                      :percent="item.percentage"
                      hide-info></Progress>
          </template>
        </div>
        <Upload ref="porxyPersonCertFrontUrl"
                :show-upload-list="false"
                :on-success="handleSuccess"
                :accept="accept"
                :format="format"
                :max-size="5120"
                :multiple="false"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="() => handleBeforeUpload('porxyPersonCertFrontUrl')"
                type="drag"
                :action="upUrl"
                style="display: inline-block;width:100px;">
          <div class="upload-add">
            <Icon type="ios-add"
                  size="40"></Icon>
            <span>上传被授权人身份证国徽页</span>
          </div>
        </Upload>
        <div class="upload-list"
             style="margin-left: 10px;">
          <img src="../../assets/images/example/idcard-b-s.png">
          <div class="upload-list-cover"
               style="display: block;"
               @click="handleView(idcardb)">
            <span style="color:#fff;cursor: pointer;">查看示例</span>
            <Icon type="ios-eye-outline"></Icon>
          </div>
        </div>
      </div>
    </FormItem>
    <FormItem prop="porxyPersonName"
              label="姓名"
              :rules="{ required: true, message: '请输入被授权人姓名', trigger: 'change' }">
      <Input v-model="form.porxyPersonName"
             :disabled="aupisLep"
             placeholder="请输入被授权人姓名"
             size="large" />
    </FormItem>
    <FormItem prop="porxyPersonCertNo"
              label="身份证号">
      <Input v-model="form.porxyPersonCertNo"
             :disabled="aupisLep"
             placeholder="请输入被授权人身份证号"
             size="large" />
    </FormItem>
    <FormItem v-if="aupisLep"
              prop="porxyPersonCertEndDate"
              label="有效期限（止）"
              :rules="[{ required: true, message: '请选择有效期限', trigger: 'change' }, dateRules]">
      <Input v-model="form.porxyPersonCertEndDate"
             :disabled="aupisLep"
             placeholder="请输入有效期限（止）"
             size="large" />
    </FormItem>
    <FormItem v-else
              prop="porxyPersonCertEndDate"
              label="有效期限（止）"
              :rules="[{ required: true, message: '请选择有效期限', trigger: 'change' }, dateRules]">
      <RadioGroup v-model="isPorxyPersonCertEndDate"
                  @on-change="(value) => form.porxyPersonCertEndDate = value">
        <Radio label="长期"></Radio>
        <Radio label="固定日期"></Radio>
      </RadioGroup>
      <DatePicker :editable="false"
                  ref="porxyPersonCertEndDate"
                  type="date"
                  :disabled="isPorxyPersonCertEndDate === '长期'"
                  placeholder="请选择固定日期"
                  @on-change="(value) => form.porxyPersonCertEndDate = value"
                  v-model="form.porxyPersonCertEndDate"
                  format="yyyy-MM-dd"
                  style="width: 200px"></DatePicker>
    </FormItem>
    <FormItem v-show="!aupisLep"
              prop="porxyCommissionUrl"
              label="授权委托书"
              :rules="!aupisLep ? { required: true, message: '请上传授权委托书', trigger: 'change' } : {}">
      <div class="upload-items">
        <div class="upload-list"
             v-for="item in uploadList.porxyCommissionUrl"
             :key="item.name">
          <template v-if="item.status === 'finished'">
            <img v-if="item.fileType === 'image'"
                 :src="item.viewFileUrl">
            <span v-else>PDF</span>
            <div class="upload-list-cover">
              <Icon type="ios-eye-outline"
                    @click.native="handleView(item.viewFileUrl)"></Icon>
              <Icon type="ios-trash-outline"
                    @click.native="handleRemove(item, 'porxyCommissionUrl')"></Icon>
            </div>
          </template>
          <template v-else>
            <Progress v-if="item.showProgress"
                      :percent="item.percentage"
                      hide-info></Progress>
          </template>
        </div>
        <Upload ref="porxyCommissionUrl"
                :show-upload-list="false"
                :on-success="handleSuccess"
                :accept="accept"
                :format="format"
                :max-size="5120"
                :multiple="false"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="() => handleBeforeUpload('porxyCommissionUrl')"
                type="drag"
                :action="upUrl"
                style="display: inline-block;width:100px;">
          <div class="upload-add">
            <Icon type="ios-add"
                  size="40"></Icon>
            <span>上传授权委托书</span>
          </div>
        </Upload>
        <div class="upload-list"
             style="margin-left: 10px;">
          <img src="../../assets/images/example/signature-s.png">
          <div class="upload-list-cover"
               style="display: block;"
               @click="handleView(signature)">
            <span style="color:#fff;cursor: pointer;">查看示例</span>
            <Icon type="ios-eye-outline"></Icon>
          </div>
        </div>
      </div>
      <a href="../file/授权委托书.pdf"
         target="_blank"
         download="授权委托书.pdf"
         style="line-height: 50px;">下载授权委托书</a>
    </FormItem>
    <h3>被授权人系统账号</h3>
    <Divider dashed />

    <FormItem prop="porxyPersonEmail"
              label="登录邮箱"
              :rules="[{ required: true, message: '邮箱不能为空', trigger: 'blur' }, { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]">
      <Input v-model="form.porxyPersonEmail"
             placeholder="请输入登录邮箱"
             size="large" />
    </FormItem>
    <FormItem prop="porxyPersonPassword"
              label="登录密码">
      <Row>
        <Col span="13">
        <Input v-model="form.porxyPersonPassword"
               type="password"
               placeholder="请输入登录密码"
               size="large" />
        </Col>
        <Col span="10"
             offset="1">
        <p>至少8位，包含大小写字母、数字、符号中至少2种</p>
        </Col>
      </Row>
    </FormItem>
    <FormItem prop="read">
      <CheckboxGroup v-model="form.read">
        <Checkbox label="yes">阅读并同意
          <router-link to="/signin/service-agreement"
                       target="_blank">《用户服务协议》</router-link>
          <router-link to="/signin/privacy-statement"
                       target="_blank">《平台隐私声明》</router-link>
          <router-link to="/signin/signature-statement"
                       target="_blank">《电子签名数字证书用户使用须知》</router-link>
        </Checkbox>
      </CheckboxGroup>
    </FormItem>
    <FormItem style="text-align: center;">
      <Button @click="handleSubmit"
              type="primary"
              long
              style="font-size: 14px; width: 180px;">
        <span v-if="!loading">提交审核</span>
        <span v-else>Loading...</span>
      </Button>
    </FormItem>
    <Modal v-model="visible"
           width="800px"
           :footer-hide="true"
           title="查看文件">
      <iframe :src="viewUrl"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
    </Modal>
  </Form>
</template>
<script>
import { ValidatePass, idCardValid } from '../../libs/util'
import { mapState, mapActions } from 'vuex'
import config from '@/config'
import signature from '@/assets/images/example/signature.png'
import blic from '@/assets/images/example/blic.png'
import idcardf from '@/assets/images/example/idcard-f.png'
import idcardb from '@/assets/images/example/idcard-b.png'

const upUrl = process.env.NODE_ENV === 'development' ? config.upUrl.dev : config.upUrl.pro

export default {
  name: 'CompanyStep2',
  props: {
    loading: {
      type: Boolean,
      default: false
    }
  },
  data () {
    const validateCode = (rule, value, callback) => {
      if (value.length < 18) {
        callback(new Error('统一社会信用代码不能小于18位'))
      } else {
        callback()
      }
    }
    const validateProvinceCity = (rule, value, callback) => {
      if (this.form.cityName === '') {
        callback(new Error('请选择公司地址市'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 8) {
        callback(new Error('密码至少8位'))
      } else if (ValidatePass(value) < 2) {
        callback(new Error('至少8位，包含大小写字母、数字、符号中至少2种'))
      } else {
        callback()
      }
    }
    const validateIdcard = (rule, value, callback) => {
      if (!idCardValid(value)) {
        callback(new Error('身份证号不正确'))
      } else {
        callback()
      }
    }
    const validateDate = (rule, value, callback) => {
      if (value === '' || value === '固定日期') {
        callback(new Error('请选择日期'))
      } else {
        callback()
      }
    }
    return {
      blic: blic,
      signature: signature,
      idcardf: idcardf,
      idcardb: idcardb,
      uploadKey: '',
      aupisLep: false,
      accept: '.png,.jpg,.jpeg,.gif,.bmp,.pdf',
      format: ['png', 'jpg', 'jpeg', 'gif', 'bmp', 'pdf'],
      upUrl: upUrl + 'file/upload',
      file: '',
      provinceCity: [],
      isBlicEndDate: '固定日期',
      isLegalPersonCertEndDate: '固定日期',
      isPorxyPersonCertEndDate: '固定日期',
      form: {
        'blicUrl': '',
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
        'legalPersonCertBackUrl': '',
        'legalPersonName': '',
        'legalPersonCertNo': '',
        'legalPersonCertEndDate': '',
        'aupisLep': 0,
        'porxyPersonCertFrontUrl': '',
        'porxyPersonCertBackUrl': '',
        'porxyPersonName': '',
        'porxyPersonCertNo': '',
        'porxyPersonCertEndDate': '',
        'porxyCommissionUrl': '',
        'porxyPersonEmail': '',
        'porxyPersonPassword': '',
        'read': []
      },
      NatureConcreteList: [],
      model1: '',
      viewUrl: '',
      visible: false,
      uploadList: {
        blicUrl: [],
        legalPersonCertFrontUrl: [],
        legalPersonCertBackUrl: [],
        porxyPersonCertFrontUrl: [],
        porxyPersonCertBackUrl: [],
        porxyCommissionUrl: []
      },
      creditCodeRules: validateCode,
      provinceCityRules: validateProvinceCity,
      passRules: validatePass,
      idcardRules: validateIdcard,
      dateRules: validateDate
    }
  },
  computed: {
    ...mapState({
      natureList: state => state.app.natureList,
      staffsizeList: state => state.app.staffsizeList
    }),
    rules () {
      return {
        creditCode: [
          { required: true, message: '请输入统一社会信用代码', trigger: 'blur' },
          { required: true, validator: this.creditCodeRules, trigger: 'blur' }
        ],
        provinceName: [
          { required: true, message: '请选择公司地址省市', trigger: 'change' },
          { validator: this.provinceCityRules, trigger: 'change' }
        ],
        legalPersonCertNo: [
          { required: true, message: '请输入法人身份证号', trigger: 'blur' },
          { required: true, validator: this.idcardRules, trigger: 'blur' }
        ],
        porxyPersonCertNo: [
          { required: true, message: '请输入被授权人身份证号', trigger: 'change' },
          { required: true, validator: this.idcardRules, trigger: 'change' }
        ],
        porxyPersonPassword: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { required: true, validator: this.passRules, trigger: 'blur' }
        ],
        read: { required: true, type: 'array', min: 1, message: '请先勾选协议', trigger: 'change' }
      }
    }
  },
  mounted () {
    this.uploadList.blicUrl = this.$refs.blicUrl.fileList
    this.uploadList.legalPersonCertFrontUrl = this.$refs.legalPersonCertFrontUrl.fileList
    this.uploadList.legalPersonCertBackUrl = this.$refs.legalPersonCertBackUrl.fileList
    this.uploadList.porxyPersonCertFrontUrl = this.$refs.porxyPersonCertFrontUrl.fileList
    this.uploadList.porxyPersonCertBackUrl = this.$refs.porxyPersonCertBackUrl.fileList
    this.uploadList.porxyCommissionUrl = this.$refs.porxyCommissionUrl.fileList
  },
  methods: {
    ...mapActions([
      'getNatureSelectData'
    ]),
    getNatureSelect (value) {
      this.natureList.forEach(item => {
        if (item.dictValue === value) {
          this.getNatureSelectData({ key: item.dictKey }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000 && res.data.data.length) {
              this.NatureConcreteList = res.data.data
            }
          })
        }
      })
    },
    handleBlurName (e) {
      if (this.aupisLep) {
        this.form.porxyPersonName = e.target.value
      }
    },
    handleBlurIdcard (e) {
      if (this.aupisLep) {
        this.form.porxyPersonCertNo = e.target.value
      }
    },
    handleChangeCheckbox (value) {
      if (value) {
        this.uploadList.porxyPersonCertFrontUrl = this.$refs.legalPersonCertFrontUrl.fileList
        this.uploadList.porxyPersonCertBackUrl = this.$refs.legalPersonCertBackUrl.fileList
        this.form.porxyPersonCertFrontUrl = this.form.legalPersonCertFrontUrl
        this.form.porxyPersonCertBackUrl = this.form.legalPersonCertBackUrl
        this.form.porxyPersonName = this.form.legalPersonName
        this.form.porxyPersonCertNo = this.form.legalPersonCertNo
        this.form.porxyPersonCertEndDate = this.form.legalPersonCertEndDate
      } else {
        this.form.porxyPersonCertFrontUrl = ''
        this.form.porxyPersonCertBackUrl = ''
        this.form.porxyPersonName = ''
        this.form.porxyPersonCertNo = ''
        this.form.porxyPersonCertEndDate = ''
        this.uploadList.porxyPersonCertFrontUrl = this.$refs.porxyPersonCertFrontUrl.fileList = []
        this.uploadList.porxyPersonCertBackUrl = this.$refs.porxyPersonCertBackUrl.fileList = []
      }
    },
    handleSelector (data) {
      this.form.provinceName = data[0]
      this.form.cityName = data.length === 2 ? data[1] : ''
    },
    handleView (viewFileUrl) {
      this.viewUrl = viewFileUrl
      this.visible = true
    },
    handleRemove (file, name) {
      this.form[name] = ''
      const fileList = this.$refs[name].fileList
      this.$refs[name].fileList.splice(fileList.indexOf(file), 1)
      if (this.aupisLep) {
        if (name === 'legalPersonCertBackUrl') {
          this.form.porxyPersonCertBackUrl = ''
        }
        if (name === 'legalPersonCertFrontUrl') {
          this.form.porxyPersonCertFrontUrl = ''
        }
      }
    },
    handleSuccess (res, file) {
      if (res && res.code === 10000) {
        file.name = res.data.newFileName
        file.viewFileUrl = res.data.viewFileUrl
        file.fileType = res.data.fileType
        this.form[this.uploadKey] = res.data.fileUrl
        if (this.aupisLep) {
          if (this.uploadKey === 'legalPersonCertBackUrl') {
            this.form.porxyPersonCertBackUrl = res.data.fileUrl
          }
          if (this.uploadKey === 'legalPersonCertFrontUrl') {
            this.form.porxyPersonCertFrontUrl = res.data.fileUrl
          }
        }
      } else {
        this.$Notice.warning({
          title: '提示',
          desc: res.message
        })
      }
    },
    handleFormatError (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '图片类型错误，请选择jpg或者png格式'
      })
    },
    handleMaxSize (file) {
      this.$Notice.warning({
        title: '提示',
        desc: '图片大于5M.'
      })
    },
    handleBeforeUpload (name) {
      this.uploadKey = name
      const check = this.uploadList[name].length < 1
      if (!check) {
        this.$Notice.warning({
          title: '最多上传1张图片'
        })
      }
      return check
    },
    handleSubmit () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.form.aupisLep = this.aupisLep ? 1 : 0
          this.$emit('on-success-valid', {
            from: this.form
          })
        } else {
          this.$Message.error({
            content: '信息填写不正确',
            duration: 5,
            closable: true
          })
        }
      })
    }
  }
}
</script>
