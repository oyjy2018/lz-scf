import axios from '@/libs/api.request'

export const login = ({ loginAccount, password }) => {
  const data = {
    loginAccount,
    password
  }
  return axios.request({
    url: 'login',
    data,
    method: 'post'
  })
}

// 手机号码是否存在
export const phoneCheck = ({ phone }) => {
  const data = { phone }
  return axios.request({
    url: 'api/phoneCheck',
    data,
    method: 'post'
  })
}

// 获取短信验证码
export const getVCode = (data) => {
  return axios.request({
    url: 'api/vcode/phone/get',
    data,
    method: 'post'
  })
}

// 校验短信验证码
export const checkVCode = ({ phone, vcode }) => {
  const data = { phone, vcode }
  return axios.request({
    url: 'api/vcode/phone/check',
    data,
    method: 'post'
  })
}

// 获取邮箱短信验证码
export const getEmailVCode = ({ email, userName }) => {
  const data = { email, userName }
  return axios.request({
    url: 'api/getEmailVCode',
    data,
    method: 'post'
  })
}

// 公司注册
export const reg = (data) => {
  return axios.request({
    url: 'company/register',
    data,
    method: 'post'
  })
}

// 用户接受邀请并注册（新用户）
export const acceptInviteAndRegister = (data) => {
  return axios.request({
    url: 'user/acceptInviteAndRegister',
    data,
    method: 'post'
  })
}

// 用户接受邀请并注册（老用户）
export const acceptInvite = (data) => {
  return axios.request({
    url: 'user/acceptInvite',
    data,
    method: 'post'
  })
}

// 重发激活邮件
export const reSendInvite = ({ email }) => {
  const data = { email }
  return axios.request({
    url: 'user/reSendInvite',
    data,
    method: 'post'
  })
}

// 公司激活
export const companyActive = ({ companyAuditId }) => {
  return axios.request({
    url: `company/active/${companyAuditId}`,
    method: 'post'
  })
}

// 重发公司激活邮件
export const sendCompanyActiveEmail = ({ companyAuditId }) => {
  return axios.request({
    url: `company/active/${companyAuditId}/email`,
    method: 'post'
  })
}

// 查询公司成员(主要用于下拉选择)
export const getUserByCompanyId = ({ companyId }) => {
  const data = { companyId }
  return axios.request({
    url: 'user/findByCompanyId',
    data,
    method: 'post'
  })
}

export const getUserInfo = (token) => {
  return axios.request({
    url: 'get_info',
    params: {
      token
    },
    method: 'get'
  })
}

export const logout = (token) => {
  return axios.request({
    url: 'logout',
    method: 'post'
  })
}

export const checkedRegisterPhone = ({ phone }) => {
  const data = { phone }
  return axios.request({
    url: 'user/isRegisterPhone',
    data,
    method: 'post'
  })
}

// 邮箱是否已注册
export const checkedRegisterEmail = ({ email }) => {
  return axios.request({
    url: `user/${email}/isRegister`,
    method: 'get'
  })
}

export const updatePassword = (data) => {
  return axios.request({
    url: 'user/updatePassword',
    data,
    method: 'post'
  })
}

export const inviteUsers = ({ emails }) => {
  const data = { emails }
  return axios.request({
    url: 'user/inviteUsers',
    data,
    method: 'post'
  })
}

export const getUnreadCount = () => {
  return axios.request({
    url: 'message/count',
    method: 'get'
  })
}

export const getMessage = () => {
  return axios.request({
    url: 'message/init',
    method: 'get'
  })
}

export const getContentByMsgId = msg_id => {
  return axios.request({
    url: 'message/content',
    method: 'get',
    params: {
      msg_id
    }
  })
}

export const hasRead = msg_id => {
  return axios.request({
    url: 'message/has_read',
    method: 'post',
    data: {
      msg_id
    }
  })
}

export const removeReaded = msg_id => {
  return axios.request({
    url: 'message/remove_readed',
    method: 'post',
    data: {
      msg_id
    }
  })
}

export const restoreTrash = msg_id => {
  return axios.request({
    url: 'message/restore',
    method: 'post',
    data: {
      msg_id
    }
  })
}

// 查询用户所在公司成员
export const findCompanyUser = (data) => {
  return axios.request({
    url: '/user/findCompanyUser',
    data,
    method: 'post'
  })
}

// 检查是否邮箱验证通过
export const checkIsNewUser = (data) => {
  return axios.request({
    url: 'user/checkIsNewUser',
    params: data,
    method: 'get'
  })
}
