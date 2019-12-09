import {
  getCompanyDepTree,
  getUserList,
  addDept,
  editDept,
  deleteDept,
  addUserList,
  getUserEidtInfo,
  editUserInfo,
  deleteUserInfo,
  editUserStatus,
  getDeptUserTree
} from '@/api/team'

export default {
  state: {
  },
  mutations: {
  },
  getters: {
  },
  actions: {
    getCompanyDepTree ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getCompanyDepTree().then(res => {
          // const data = res.data
          // commit('setCompanyDeptTreeList', data.data)
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUserList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getUserList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addDept ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addDept(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editDept ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        editDept(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteDept ({ state, commit }, { id }) {
      return new Promise((resolve, reject) => {
        deleteDept({ id }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addUserList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addUserList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUserEidtInfo ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getUserEidtInfo(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editUserInfo ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        editUserInfo(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteUserInfo ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        deleteUserInfo(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editUserStatus ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        editUserStatus(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getDeptUserTree ({ state, commit }, { companyId }) {
      return new Promise((resolve, reject) => {
        getDeptUserTree({ companyId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
