import {
  gatheringList,
  lzProjectList,
  lzProjectBasicInfo,
  lzProjectItemInfo,
  lzProjectContractInfo,
  lzProjectPayList,
  findCostDetails,
  getPurchaseLogistics,
  findProjectAsset,
  getLoanList,
  getRefundList,
  getFileList,
  findLiabilities,
  getBoardCard,
  getBoardReport,
  getPledgeCashPayList,
  getPledgeCashExtractList,
  getInsuranceList,
  getInvoiceList,
  findMarginsList
} from '@/api/lz-project'

export default {
  state: {
  },
  mutations: {
  },
  getters: {
  },
  actions: {
    gatheringList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        gatheringList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    lzProjectList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        lzProjectList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    lzProjectBasicInfo ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        lzProjectBasicInfo(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    lzProjectItemInfo ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        lzProjectItemInfo(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    lzProjectContractInfo ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        lzProjectContractInfo(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    lzProjectPayList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        lzProjectPayList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCostDetails ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCostDetails(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getPurchaseLogistics ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getPurchaseLogistics(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findProjectAsset ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findProjectAsset(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getLoanList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getLoanList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getRefundList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getRefundList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getFileList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getFileList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findLiabilities ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findLiabilities(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getBoardCard ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getBoardCard(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getBoardReport ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getBoardReport(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getPledgeCashPayList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getPledgeCashPayList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getPledgeCashExtractList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getPledgeCashExtractList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getInsuranceList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getInsuranceList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getInvoiceList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getInvoiceList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findMarginsList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findMarginsList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
