<style lang="less" scoped>
.dd {
  padding-left: 20px;
  height:19px;
  font-size:14px;
  color:rgba(247,175,101,1);
  line-height:19px;
}
.company-cards {
  width:60px;text-align: center;
  margin: 0 10px 20px 0;
  height:60px;
  background:rgba(229,1,19,1);
  border-radius:8px;
  font-size:40px;
  font-family:MicrosoftYaHei;
  color:rgba(255,255,255,1);
  line-height:60px;
}
.company-name-cards {
  height: 35px;
  font-size: 18px;
  font-family: MicrosoftYaHei;
  color: rgba(102,102,102,1);
}
.company-time-cards {
  height: 19px;
  font-size: 14px;
  font-family: MicrosoftYaHei;
  color: rgba(135,145,160,1);
  line-height: 19px;
}
</style>
<template>
<Card>
  <Tabs value="name1">
    <TabPane label="基本资料" name="name1">
      <Form :model="userInfo" :label-width="120">
        <FormItem label="真实姓名">
          <Input v-model="userInfo.userName" placeholder="请输入真实姓名" style="width:210px; height:38px;" :maxlength="10">{{ userInfo.userName }}</Input>
        </FormItem>
        <FormItem label="性别">
          <RadioGroup v-model="userInfo.gender">
            <Radio :label="1">男</Radio>
            <Radio :label="2">女</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="用户组">{{ userInfo.roleName }}</FormItem>
        <FormItem>
          <Button @click="handleUserInfoClick" type="primary">保存</Button>
        </FormItem>
      </Form>
    </TabPane>
    <TabPane label="账户安全" name="name2">
      <div v-show="accountSecurityShow">
        <Form :model="accountSecurity" :label-width="120">
          <div class="dd">进入帐号安全设置前，请输入你的密码以确保帐号安全</div>
          <FormItem label="登录邮箱">{{ userInfo.email }}</FormItem>
          <FormItem label="登录密码">
            <Input type="password" v-model="accountSecurity.password" placeholder="你的登录密码" style="width:210px; height:38px;" :maxlength="16"></Input>
          </FormItem>
          <FormItem>
            <Button @click="handleShowClick" type="primary">确定</Button>
          </FormItem>
        </Form>
      </div>
      <div v-show="!accountSecurityShow">
        <Collapse v-model="collapseShow" accordion style="width: 600px;">
          <Panel name="1">
              登录邮箱: {{ userInfo.email }}
              <p slot="content">
                <Form ref="emailData" :model="emailData" :rules="ruleEmailValidate" :label-width="100">
                  <FormItem label="新邮箱地址" prop="email">
                    <Input v-model="emailData.email" placeholder="新邮箱地址" :maxlength="40"></Input>
                  </FormItem>
                  <FormItem label="验证码" prop="code">
                    <Row>
                      <Col span="10">
                      <Input type="text"
                            size="large"
                            v-model="emailData.code"
                            :maxlength="6"
                            placeholder="请输入验证码"></Input>
                      </Col>
                      <Col span="8"
                          offset="2">
                      <Button :disabled="checkEmail"
                              @click="sendEmailCode"
                              shape="circle"
                              type="primary"
                              size="large">{{auth_time ? '重新发送('+auth_time+'s)' : '发送验证码到新邮箱'}}</Button>
                      </Col>
                    </Row>
                  </FormItem>
                  <FormItem>
                    <Button  type="primary" @click="handleUpdateEmailClick('emailData')">验证</Button>
                  </FormItem>
                </Form>
              </p>
          </Panel>
          <Panel name="2">
              手机号码: {{ userInfo.phone }}
              <p slot="content">
                <Form ref="phoneData" :model="phoneData" :rules="rulePhoneValidate" :label-width="80">
                  <FormItem label="新手机号" prop="phone">
                    <Input v-model="phoneData.phone" placeholder="你的手机号码" :maxlength="11"></Input>
                  </FormItem>
                  <FormItem label="验证码" prop="code">
                    <Row>
                      <Col span="10">
                      <Input type="text"
                            size="large"
                            v-model="phoneData.code"
                            :maxlength="6"
                            placeholder="请输入验证码"></Input>
                      </Col>
                      <Col span="8"
                          offset="2">
                      <Button :disabled="checkPhone"
                              @click="sendPhoneCode"
                              shape="circle"
                              type="primary"
                              size="large">{{phone_auth_time ? '重新发送('+phone_auth_time+'s)' : '发送验证码到新手机'}}</Button>
                      </Col>
                    </Row>
                  </FormItem>
                  <FormItem>
                    <Button  type="primary" @click="handleUpdatePhoneClick('phoneData')">验证</Button>
                  </FormItem>
                </Form>
              </p>
          </Panel>
          <Panel name="3">
              登录密码
              <p slot="content">
                <Form ref="passwordData" :model="passwordData" :rules="rulePasswordValidate" :label-width="70">
                  <FormItem label="原密码" prop="oldPassword">
                    <Input type="password" v-model="passwordData.oldPassword" placeholder="原密码" :maxlength="16"></Input>
                  </FormItem>
                  <FormItem label="新密码" prop="newPassword">
                    <Input type="password" v-model="passwordData.newPassword" placeholder="新密码" :maxlength="16"></Input>
                  </FormItem>
                  <FormItem label="重复密码" prop="newPasswordAgain">
                    <Input type="password" v-model="passwordData.newPasswordAgain" placeholder="再输一次" :maxlength="16"></Input>
                  </FormItem>
                  <FormItem>
                    <Button  type="primary" @click="handleUpdatePasswordClick('passwordData')">确认修改</Button>
                  </FormItem>
                </Form>
              </p>
          </Panel>
          <Panel name="4">
              所属公司
              <div slot="content">
                <div style="display:flex">
                  <div class="company-cards">{{firstText}}</div>
                  <div>
                    <div class="company-name-cards">{{ userInfo.companyName }}</div>
                    <div class="company-time-cards">加入时间: {{ userInfo.createTime }}</div>
                  </div>
                </div>
                <Button @click="leaveConfirmModal = true" type="error" ghost>离职退出公司</Button>
              </div>
          </Panel>
        </Collapse>
      </div>
    </TabPane>
  </Tabs>
  <Modal v-model="leaveConfirmModal" width="360">
    <p slot="header" style="color:#f60;text-align:center">
      <Icon type="ios-information-circle"></Icon>
      <span>退出公司确认</span>
    </p>
    <div style="text-align:center">
      <p>退出公司后, 将无法登录系统.</p>
      <p>您确认退出公司?</p>
    </div>
    <div slot="footer">
      <Button type="error" size="large" long :loading="modal_loading" @click="handleConfirmClick">退出</Button>
    </div>
  </Modal>
  </Card>
</template>
<script>
import { mapActions } from 'vuex'
import { ValidatePhone, validateEmail, ValidatePass } from '../../../libs/util'

export default {
  data () {
    const validateUserEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('新邮箱不能为空'))
      } else if (!validateEmail(value)) {
        callback(new Error('邮箱格式不正确'))
      } else if (value === this.userInfo.email) {
        callback(new Error('新邮箱不能与当前邮箱一致'))
      } else {
        callback()
      }
    }
    const validatePhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('手机号码不能为空'))
      } else if (!ValidatePhone(value)) {
        callback(new Error('手机号码格式不正确'))
      } else if (value === this.userInfo.phone) {
        callback(new Error('新手机号不能与当前手机号一致'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('新密码不能为空'))
      } else if (this.passwordData.oldPassword === value) {
        callback(new Error('新密码与原密码相同'))
      } else if (value.length < 8) {
        callback(new Error('密码至少8位'))
      } else if (ValidatePass(value) < 2) {
        callback(new Error('至少8位，包含大小写字母、数字、符号中至少2种'))
      } else {
        callback()
      }
    }
    const validatePassCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('新密码两次输入不一致'))
      } else if (this.passwordData.newPassword !== value) {
        callback(new Error('新密码两次输入不一致'))
      } else {
        callback()
      }
    }
    return {
      userInfo: {
        userName: '',
        gender: 1,
        roleName: '',
        email: '',
        companyName: '',
        phone: '',
        createTime: ''
      },
      accountSecurityShow: true,
      accountSecurity: {
        password: ''
      },
      emailData: {
        email: '',
        code: ''
      },
      ruleEmailValidate: {
        email: [
          { required: true, validator: validateUserEmail, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      },
      auth_time: 0,
      phoneData: {
        phone: '',
        code: ''
      },
      rulePhoneValidate: {
        phone: [
          { required: true, validator: validatePhone, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      },
      phone_auth_time: 0,
      passwordData: {
        oldPassword: '',
        newPassword: '',
        newPasswordAgain: ''
      },
      rulePasswordValidate: {
        oldPassword: [
          { required: true, message: '原密码不能为空', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
        newPasswordAgain: [
          { required: true, validator: validatePassCheck, trigger: 'blur' }
        ]
      },
      collapseShow: '',
      leaveConfirmModal: false,
      modal_loading: false
    }
  },
  computed: {
    firstText: function () {
      return this.userInfo.companyName.slice(0, 1)
    },
    checkEmail: function () {
      if (validateEmail(this.emailData.email)) {
        if (this.emailData.email !== this.userInfo.email) {
          if (this.auth_time === 0) {
            return false
          }
        }
      }
      return true
    },
    checkPhone: function () {
      if (ValidatePhone(this.phoneData.phone)) {
        if (this.phoneData.phone !== this.userInfo.phone) {
          if (this.phone_auth_time === 0) {
            return false
          }
        }
      }
      return true
    }
  },
  mounted () {
    this.getUserInformation().then(res => {
      if (res && res.status === 200 && res.data.code === 10000) {
        this.userInfo = res.data.data
      }
    })
  },
  methods: {
    ...mapActions([
      'getUserInformation',
      'updateUserInformation',
      'checkUserPassword',
      'getEmailVCode',
      'updateUserEmail',
      'checkedRegisterEmail',
      'checkedRegisterPhone',
      'getVCode',
      'updateUserPhone',
      'updateUserPassword',
      'userLeaveCompany',
      'handleLogOut'
    ]),
    handleUserInfoClick () {
      this.updateUserInformation(this.userInfo).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Modal.success({
            title: '提示',
            content: '修改成功'
          })
          this.$router.push({
            name: 'user-setting'
          })
        }
      })
    },
    handleShowClick () {
      this.checkUserPassword(this.accountSecurity).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.accountSecurityShow = false
          this.accountSecurity.password = ''
        }
      })
    },
    setInTime () {
      this.auth_time = 60
      var auth_timetimer = setInterval(() => {
        this.auth_time--
        if (this.auth_time <= 0) {
          clearInterval(auth_timetimer)
        }
      }, 1000)
    },
    sendEmailCode () {
      this.checkedRegisterEmail({ email: this.emailData.email }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          if (res.data.data) {
            this.emailData.email = ''
            this.$Modal.error({
              title: '提示',
              content: '邮箱已注册'
            })
          } else {
            this.getEmailVCode({ email: this.emailData.email, userName: this.userInfo.userName }).then(res => {
              if (res && res.status === 200 && res.data.code === 10000) {
                this.setInTime()
              }
            })
          }
        }
      })
    },
    handleUpdateEmailClick (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.updateUserEmail(this.emailData).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              // this.userInfo.email = this.emailData.email
              // this.emailData.email = ''
              // this.emailData.code = ''
              // this.$Modal.success({
              //   title: '提示',
              //   content: '修改成功'
              // })
              this.handleRedirect()
            }
          })
        }
      })
    },
    setPhoneInTime () {
      this.phone_auth_time = 60
      var auth_timetimer = setInterval(() => {
        this.phone_auth_time--
        if (this.phone_auth_time <= 0) {
          clearInterval(auth_timetimer)
        }
      }, 1000)
    },
    sendPhoneCode () {
      if (ValidatePhone(this.phoneData.phone)) {
        this.checkedRegisterPhone({ phone: this.phoneData.phone }).then(res => {
          if (res && res.status === 200 && res.data.code === 10000) {
            if (res.data.data.isRegister) {
              this.phoneData.phone = ''
              this.$Modal.error({
                title: '提示',
                content: '手机号已注册'
              })
            } else {
              this.getVCode({ phone: this.phoneData.phone, smsType: '3' }).then(res => {
                if (res && res.status === 200 && res.data.code === 10000) {
                  this.setPhoneInTime()
                }
              })
            }
          }
        })
      }
    },
    handleUpdatePhoneClick (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.updateUserPhone(this.phoneData).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              // this.userInfo.phone = this.phoneData.phone
              // this.phoneData.phone = ''
              // this.phoneData.code = ''
              // this.$Modal.success({
              //   title: '提示',
              //   content: '修改成功'
              // })
              this.handleRedirect()
            }
          })
        }
      })
    },
    handleUpdatePasswordClick (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.updateUserPassword(this.passwordData).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.passwordData.oldPassword = ''
              this.passwordData.newPassword = ''
              this.passwordData.newPasswordAgain = ''
              this.$Modal.success({
                title: '提示',
                content: '修改成功'
              })
            }
          })
        }
      })
    },
    handleConfirmClick () {
      this.modal_loading = true
      this.userLeaveCompany().then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          setTimeout(() => {
            this.modal_loading = false
            this.leaveConfirmModal = false
            this.$Message.success('退出成功')
            this.$router.push({
              name: 'login'
            })
          }, 1000)
        }
      })
    },
    handleRedirect () {
      const title = '操作通知'
      const content = '<p>操作成功</p><p>即将跳转登录页</p>'
      this.$Modal.info({
        title: title,
        content: content,
        onOk: () => {
          this.handleLogOut().then(() => {
            this.$router.push({
              name: 'login'
            })
          })
        }
      })
    }
  }
}
</script>
