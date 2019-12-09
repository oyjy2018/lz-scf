import {
  modelList,
  updateModelHasEnabled,
  deleteModel,
  insertModel,
  addScoreItem,
  scoreVariable,
  getModelFormula,
  modelDetailItem,
  modelItem,
  scoreDetail,
  editScore,
  removeScore,
  getModelDetail,
  editModel,
  updateModelFormula,
  updatafullMark,
  getStandardBusinessTypeList
} from '@/api/risk'

export default {
  state: {
  },
  mutations: {
  },
  getters: {
  },
  actions: {
    modelList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        modelList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateModelHasEnabled ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateModelHasEnabled(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteModel ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        deleteModel(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    insertModel ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        insertModel(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addScoreItem ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        addScoreItem(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    scoreVariable ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        scoreVariable(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getModelFormula ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        getModelFormula(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    modelDetailItem ({ state, commot }, modelId) {
      return new Promise((resolve, reject) => {
        modelDetailItem(modelId).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    modelItem ({ state, commot }, modelId) {
      return new Promise((resolve, reject) => {
        modelItem(modelId).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    scoreDetail ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        scoreDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editScore ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        editScore(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    removeScore ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        removeScore(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getModelDetail ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        getModelDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    editModel ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        editModel(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateModelFormula ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        updateModelFormula(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updatafullMark ({ state, commit }, { riskScoreItemId, fullMark }) {
      return new Promise((resolve, reject) => {
        updatafullMark({ riskScoreItemId, fullMark }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getStandardBusinessTypeList ({ state, commot }, data) {
      return new Promise((resolve, reject) => {
        getStandardBusinessTypeList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
