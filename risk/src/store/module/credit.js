import {
  findBusinessAttrCfg,
  saveDraft,
  commitApply,
  allApplyList,
  myApplyList,
  getDetail,
  allAuditList,
  auditList,
  myAuditList,
  getTrailingWorkFlowCfg,
  auditCommit,
  fileCommit,
  getCommentList,
  findAuditLogList,
  findOperateLogList,
  getFlowCodeList,
  deleteCreditItem,
  getCreditDocument,
  importRecord,
  deleteRecord,
  getCreditApplyDetails,
  findCreditApprovalCourse
} from '@/api/credit'

export default {
  state: {
  },
  mutations: {
  },
  getters: {
  },
  actions: {
    findBusinessAttrCfg ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findBusinessAttrCfg(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    saveDraft ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        saveDraft(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    commitApply ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        commitApply(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    allApplyList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        allApplyList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    myApplyList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        myApplyList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getDetail ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    allAuditList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        allAuditList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    auditList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        auditList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    myAuditList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        myAuditList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getTrailingWorkFlowCfg ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getTrailingWorkFlowCfg(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    fileCommit ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        fileCommit(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    auditCommit ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        auditCommit(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getCommentList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCommentList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findAuditLogList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findAuditLogList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findOperateLogList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findOperateLogList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getFlowCodeList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getFlowCodeList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteCreditItem ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        deleteCreditItem(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getCreditDocument ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCreditDocument(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    importRecord ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        importRecord(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteRecord ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        deleteRecord(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getCreditApplyDetails ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCreditApplyDetails(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditApprovalCourse ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditApprovalCourse(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
