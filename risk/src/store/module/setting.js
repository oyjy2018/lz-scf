import {
  getCompanyData,
  getCompanyAuditData,
  getCompanyAuditBasicInfo,
  getCompanyDetail,
  editCompanyStatus,
  companyAudit,
  companyDelete,
  getDeptOption,
  getDeptList,
  getDeptUserList,
  getCompanyUser,
  getCompanyUserList,
  getDeptSelectList,
  getCompanyInfo,
  getCompanyRealUrl,
  getCompanyIsJdReal,
  getCompanyBanks,
  getJdBanks,
  getJdBankProvinces,
  getJdBankCitys,
  getJdBankCode,
  addCompanyBank,
  verifyCompanyBank,
  defaultCompanyBank,
  deleteCompanyAccountType,
  sendBankPayment,
  getCompanyBanksReceipt,
  verifyCompanyBankReceipt,
  defaultCompanyBankReceipt,
  deleteCompanyAccountTypeReceipt,
  sendBankPaymentReceipt,
  verifyEcdsCompanyBank,
  queryUserCenterUrl,
  getUserInformation,
  updateUserInformation,
  checkUserPassword,
  updateUserEmail,
  updateUserPhone,
  updateUserPassword,
  userLeaveCompany
} from '@/api/setting'

export default {
  state: {
    companyData: null,
    userList: [],
    deptOption: [],
    deptList: []
  },
  mutations: {
    setCompanyData (state, data) {
      if (data) {
        state.companyData = data
      }
    },
    setDeptOption (state, list) {
      if (list) {
        state.deptOption = [...list]
      }
    },
    setDeptList (state, list) {
      if (list) {
        state.deptList = [...list]
      }
    }
  },
  getters: {
  },
  actions: {
    getCompanyData ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCompanyData(data).then(res => {
          // const data = res.data
          // commit('setCompanyData', data.data)
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getCompanyAuditData ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCompanyAuditData(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getCompanyAuditBasicInfo ({ state, commit }, { companyAuditId }) {
      return new Promise((resolve, reject) => {
        getCompanyAuditBasicInfo({ companyAuditId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getCompanyDetail ({ state, commit }, { companyId }) {
      return new Promise((resolve, reject) => {
        getCompanyDetail({ companyId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editCompanyStatus ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        editCompanyStatus(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    companyAudit ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        companyAudit(data).then(res => {
          // const data = res.data
          // commit('setCompanyAuditList', data.data)
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    companyDelete ({ state, commit }, { id }) {
      const data = { id }
      return new Promise((resolve, reject) => {
        companyDelete(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getDeptOption ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getDeptOption().then(res => {
          const data = res.data
          commit('setDeptOption', data.data)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    getDeptList ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getDeptList().then(res => {
          const data = res.data
          commit('setDeptList', data.data)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    getDeptSelectList ({ state, commit }, { companyId }) {
      return new Promise((resolve, reject) => {
        getDeptSelectList({ companyId }).then(res => {
          // const data = res.data
          // commit('setDeptList', data.data)
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getDeptUserList ({ state, commit }, { companyId }) {
      return new Promise((resolve, reject) => {
        getDeptUserList({ companyId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取用户有权限的公司
    getCompanyUser ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getCompanyUser().then(res => {
          // const data = res.data
          // commit('setDeptList', data.data)
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取公司所有成员
    getCompanyUserList ({ state, commit }, { companyId }) {
      return new Promise((resolve, reject) => {
        getCompanyUserList({ companyId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 公司基本信息
    getCompanyInfo () {
      return new Promise((resolve, reject) => {
        getCompanyInfo().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 实名认证链接
    getCompanyRealUrl () {
      return new Promise((resolve, reject) => {
        getCompanyRealUrl().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 查询公司是否已实名认证成功
    getCompanyIsJdReal () {
      return new Promise((resolve, reject) => {
        getCompanyIsJdReal().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 查询公司银行列表
    getCompanyBanks ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCompanyBanks(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 查询支持小额打款银行列表
    getJdBanks () {
      return new Promise((resolve, reject) => {
        getJdBanks().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 查询开户行省
    getJdBankProvinces () {
      return new Promise((resolve, reject) => {
        getJdBankProvinces().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 查询开户行市
    getJdBankCitys ({ state, commit }, { provinceId }) {
      return new Promise((resolve, reject) => {
        getJdBankCitys({ provinceId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 查询开户行支行列表
    getJdBankCode ({ state, commit }, { cnapBankCode, cityId }) {
      return new Promise((resolve, reject) => {
        getJdBankCode({ cnapBankCode, cityId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 新增银行卡
    addCompanyBank ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addCompanyBank(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 小额打款认证
    verifyCompanyBank ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        verifyCompanyBank(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 设为默认账户
    defaultCompanyBank ({ state, commit }, { companyBankId, accountType }) {
      return new Promise((resolve, reject) => {
        defaultCompanyBank({ companyBankId, accountType }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 修改账户类型
    deleteCompanyAccountType ({ state, commit }, { companyBankId, accountType }) {
      return new Promise((resolve, reject) => {
        deleteCompanyAccountType({ companyBankId, accountType }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 重新发起打款
    sendBankPayment ({ state, commit }, { companyBankId }) {
      return new Promise((resolve, reject) => {
        sendBankPayment({ companyBankId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 查询公司银行列表-收票
    getCompanyBanksReceipt ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCompanyBanksReceipt(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 小额打款认证-收票
    verifyCompanyBankReceipt ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        verifyCompanyBankReceipt(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 设为默认账户-收票
    defaultCompanyBankReceipt ({ state, commit }, { companyBankId, accountType }) {
      return new Promise((resolve, reject) => {
        defaultCompanyBankReceipt({ companyBankId, accountType }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 删除账户-收票
    deleteCompanyAccountTypeReceipt ({ state, commit }, { companyBankId, accountType }) {
      return new Promise((resolve, reject) => {
        deleteCompanyAccountTypeReceipt({ companyBankId, accountType }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 重新发起打款-收票
    sendBankPaymentReceipt ({ state, commit }, { companyBankId }) {
      return new Promise((resolve, reject) => {
        sendBankPaymentReceipt({ companyBankId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // ECDS授权
    verifyEcdsCompanyBank ({ state, commit }, { companyBankId }) {
      return new Promise((resolve, reject) => {
        verifyEcdsCompanyBank({ companyBankId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    queryUserCenterUrl ({ state, commit }) {
      return new Promise((resolve, reject) => {
        queryUserCenterUrl().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 用户基本信息
    getUserInformation () {
      return new Promise((resolve, reject) => {
        getUserInformation().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 修改用户基本信息
    updateUserInformation ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateUserInformation(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 修改用户基本信息
    checkUserPassword ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        checkUserPassword(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 修改用户邮箱
    updateUserEmail ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateUserEmail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 修改用户手机号
    updateUserPhone ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateUserPhone(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 修改用户手机号
    updateUserPassword ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateUserPassword(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 离职退出公司
    userLeaveCompany ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        userLeaveCompany(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
