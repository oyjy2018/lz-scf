import axios from '@/libs/api.request'

// 检测是否邀请成员
export const getIsInvite = () => {
  return axios.request({
    url: 'company/isInvite',
    method: 'post'
  })
}

// 公司管理
export const getCompanyData = (data) => {
  return axios.request({
    url: 'company/find/list',
    data,
    method: 'post'
  })
}

export const getCompanyAuditData = (data) => {
  return axios.request({
    url: 'company/audit',
    data,
    method: 'post'
  })
}

export const getCompanyAuditBasicInfo = ({ companyAuditId }) => {
  return axios.request({
    url: `/company/audit/${companyAuditId}/detail`,
    method: 'get'
  })
}

export const getCompanyDetail = ({ companyId }) => {
  return axios.request({
    url: `/company/${companyId}`,
    method: 'get'
  })
}

export const companyAudit = (data) => {
  return axios.request({
    url: `company/audit/${data.companyAuditId}`,
    data,
    method: 'post'
  })
}

export const companyDelete = ({ id }) => {
  const data = { id }
  return axios.request({
    url: 'company/delete',
    data,
    method: 'post'
  })
}

export const editCompanyStatus = (data) => {
  return axios.request({
    url: 'company/edit/status',
    data,
    method: 'post'
  })
}

// 获取用户有权限的公司
export const getCompanyUser = () => {
  return axios.request({
    url: 'user/findUserDataPermissionList',
    method: 'post'
  })
}

// 获取公司所有成员
export const getCompanyUserList = (data) => {
  return axios.request({
    url: 'company/find/user/list',
    data,
    method: 'post'
  })
}

// 部门管理
export const getDeptOption = ({ companyId }) => {
  const data = { companyId }
  return axios.request({
    url: 'dept/find/option',
    data,
    method: 'post'
  })
}

export const getDeptList = ({ companyId }) => {
  const data = { companyId }
  return axios.request({
    url: 'dept/find/list',
    data,
    method: 'post'
  })
}

// 获取用户有权限的部门
export const getDeptUserList = ({ companyId }) => {
  const data = { companyId }
  return axios.request({
    url: 'dept/find/user/list',
    data,
    method: 'post'
  })
}

export const getDeptSelectList = ({ companyId }) => {
  const data = { companyId }
  return axios.request({
    url: 'dept/find/select/list',
    data,
    method: 'post'
  })
}

// 公司基本信息
export const getCompanyInfo = () => {
  return axios.request({
    url: 'company/information',
    method: 'get'
  })
}

// 实名认证链接
export const getCompanyRealUrl = () => {
  return axios.request({
    url: 'company/realUrl',
    method: 'post'
  })
}

// 查询公司是否已实名认证成功
export const getCompanyIsJdReal = () => {
  return axios.request({
    url: 'company/jd/isReal',
    method: 'get'
  })
}

// 查询公司银行列表
export const getCompanyBanks = (data) => {
  return axios.request({
    url: `company/bank`,
    params: data,
    method: 'get'
  })
}

// 查询支持小额打款银行列表
export const getJdBanks = () => {
  return axios.request({
    url: 'jd/banks',
    method: 'get'
  })
}

// 查询开户行省
export const getJdBankProvinces = () => {
  return axios.request({
    url: 'jd/bank/provinces',
    method: 'get'
  })
}

// 查询开户行市
export const getJdBankCitys = ({ provinceId }) => {
  return axios.request({
    url: `/jd/bank/${provinceId}/citys`,
    method: 'get'
  })
}

// 查询开户行支行列表
export const getJdBankCode = ({ cnapBankCode, cityId }) => {
  return axios.request({
    url: `/jd/${cnapBankCode}/${cityId}`,
    method: 'get'
  })
}

// 新增银行卡
export const addCompanyBank = (data) => {
  return axios.request({
    url: 'company/bank',
    data,
    method: 'post'
  })
}

// 小额打款认证
export const verifyCompanyBank = (data) => {
  return axios.request({
    url: 'company/bank/verify',
    data,
    method: 'post'
  })
}

// 设为默认账户
export const defaultCompanyBank = ({ companyBankId, accountType }) => {
  const data = { accountType }
  return axios.request({
    url: `company/bank/${companyBankId}/default`,
    data,
    method: 'post'
  })
}

// 删除账户类型
export const deleteCompanyAccountType = ({ companyBankId, accountType }) => {
  const data = { accountType }
  return axios.request({
    url: `company/bank/${companyBankId}`,
    data,
    method: 'delete'
  })
}

// 重新发起打款
export const sendBankPayment = ({ companyBankId }) => {
  return axios.request({
    url: `company/bank/${companyBankId}/payment`,
    method: 'post'
  })
}

// 查询公司银行列表-收票
export const getCompanyBanksReceipt = (data) => {
  return axios.request({
    url: `company/bank/receipt`,
    params: data,
    method: 'get'
  })
}

// 小额打款认证-收票
export const verifyCompanyBankReceipt = (data) => {
  return axios.request({
    url: 'company/bank/verify/receipt',
    data,
    method: 'post'
  })
}

// 设为默认账户-收票
export const defaultCompanyBankReceipt = ({ companyBankId, accountType }) => {
  const data = { accountType }
  return axios.request({
    url: `company/bank/${companyBankId}/default/receipt`,
    data,
    method: 'post'
  })
}

// 删除账户类型-收票
export const deleteCompanyAccountTypeReceipt = ({ companyBankId, accountType }) => {
  const data = { accountType }
  return axios.request({
    url: `company/bank/${companyBankId}/receipt`,
    data,
    method: 'delete'
  })
}

// 重新发起打款-收票
export const sendBankPaymentReceipt = ({ companyBankId }) => {
  return axios.request({
    url: `company/bank/${companyBankId}/payment/receipt`,
    method: 'post'
  })
}

// ECDS授权
export const verifyEcdsCompanyBank = ({ companyBankId }) => {
  return axios.request({
    url: `company/bank/${companyBankId}/verify/ecds`,
    method: 'post'
  })
}

// 异常待处理订单
export const queryUserCenterUrl = () => {
  return axios.request({
    url: 'jdOrder/queryUserCenterUrl',
    method: 'get'
  })
}

// 获取用户基本信息
export const getUserInformation = () => {
  return axios.request({
    url: '/company/user',
    method: 'get'
  })
}
// 修改用户基本信息
export const updateUserInformation = (data) => {
  return axios.request({
    url: 'company/user',
    data,
    method: 'post'
  })
}
// 校验账户安全
export const checkUserPassword = (data) => {
  return axios.request({
    url: 'company/user/checkPassword',
    data,
    method: 'post'
  })
}
// 修改用户邮箱
export const updateUserEmail = (data) => {
  return axios.request({
    url: 'company/user/email',
    data,
    method: 'post'
  })
}
// 修改用户手机
export const updateUserPhone = (data) => {
  return axios.request({
    url: 'company/user/phone',
    data,
    method: 'post'
  })
}
// 修改账户密码
export const updateUserPassword = (data) => {
  return axios.request({
    url: 'company/user/password',
    data,
    method: 'post'
  })
}
// 离职退出公司
export const userLeaveCompany = (data) => {
  return axios.request({
    url: '/company/user/leave',
    data,
    method: 'post'
  })
}
