import {
  getRoleGroupList,
  getUserRoleList,
  addRole,
  editRole,
  deleteRole,
  getRoleCompanyUser,
  getSystemFunctionVersionList,
  getRolePrivilege,
  editRolePrivilege,
  addRoleUser,
  deleteRoleUser,
  findCompanyRoleList
} from '@/api/role'

export default {
  state: {
    systemFunctionVersionList: []
  },
  mutations: {
  },
  getters: {

  },
  actions: {
    getRoleGroupList ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getRoleGroupList().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUserRoleList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getUserRoleList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addRole ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addRole(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editRole ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        editRole(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteRole ({ state, commit }, { id }) {
      return new Promise((resolve, reject) => {
        deleteRole({ id }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getRoleCompanyUser ({ state, commit }, { companyId }) {
      return new Promise((resolve, reject) => {
        getRoleCompanyUser({ companyId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getSystemFunctionVersionList ({ state, commit }, { systemId, companyId }) {
      if (state.systemFunctionVersionList.length) {
        return
      }
      return new Promise((resolve, reject) => {
        getSystemFunctionVersionList({ systemId, companyId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getRolePrivilege ({ state, commit }, { id }) {
      return new Promise((resolve, reject) => {
        getRolePrivilege({ id }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editRolePrivilege ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        editRolePrivilege(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addRoleUser ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addRoleUser(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteRoleUser ({ state, commit }, { roleUserId }) {
      return new Promise((resolve, reject) => {
        deleteRoleUser({ roleUserId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCompanyRoleList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCompanyRoleList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
