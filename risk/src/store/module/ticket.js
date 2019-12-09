import {
  findCreditTicketCfg,
  saveDraftTicket,
  applyCommitTicket,
  findCreditUseApplyAllList,
  findCreditUseApplyMyList,
  findCreditUseAuditList,
  addCreditUseRecord,
  findCreditUseApplyDetails,
  findCreditUseMyList,
  findCreditUseAllList,
  updateRefundStatus,
  findCreditUseDetail,
  findCreditList,
  ticketDetail,
  ticketUsesaveFile,
  deleteFileById,
  auditTicketCommit,
  ocrTicket,
  getTicketDocument,
  getTicketOperateLogList,
  getTicketAuditLogList,
  getAuditFinishList,
  getCreditUseAbleMoney,
  updateTicketStatus
} from '@/api/ticket'

export default {
  state: {
  },
  mutations: {
  },
  getters: {
  },
  actions: {
    findCreditTicketCfg ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditTicketCfg(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    saveDraftTicket ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        saveDraftTicket(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    applyCommitTicket ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        applyCommitTicket(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditUseApplyAllList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditUseApplyAllList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditUseApplyMyList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditUseApplyMyList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditUseAuditList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditUseAuditList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addCreditUseRecord ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addCreditUseRecord(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditUseApplyDetails ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditUseApplyDetails(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditUseMyList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditUseMyList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditUseAllList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditUseAllList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateRefundStatus ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateRefundStatus(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditUseDetail ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditUseDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findCreditList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findCreditList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    ticketDetail ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        ticketDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    ticketUsesaveFile ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        ticketUsesaveFile(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteFileById ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        deleteFileById(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    auditTicketCommit ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        auditTicketCommit(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    ocrTicket ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        ocrTicket(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getTicketDocument ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getTicketDocument(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getTicketAuditLogList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getTicketAuditLogList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getTicketOperateLogList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getTicketOperateLogList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getAuditFinishList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getAuditFinishList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getCreditUseAbleMoney ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getCreditUseAbleMoney(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateTicketStatus ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateTicketStatus(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
