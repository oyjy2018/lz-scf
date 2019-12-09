import {
  getBusinessFlowForward,
  getBusinessTypeList,
  getBusinessFlowExtendAll,
  workFlowExists,
  editWorkFlow,
  findFlowExtendSettingInfo,
  savePower
} from '@/api/business'

export default {
  state: {
  },
  mutations: {
  },
  getters: {
  },
  actions: {
    getBusinessFlowForward ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getBusinessFlowForward(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getBusinessTypeList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getBusinessTypeList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getBusinessFlowExtendAll ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getBusinessFlowExtendAll(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    workFlowExists ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        workFlowExists(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editWorkFlow ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        editWorkFlow(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findFlowExtendSettingInfo ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findFlowExtendSettingInfo(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    savePower ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        savePower(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
