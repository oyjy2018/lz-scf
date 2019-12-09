import {
  getAllOrderList,
  reStartTicketStatusQuery,
  findOrderDetails,
  getQuotationDetail,
  getInquireDetail
} from '@/api/operation'

export default {
  state: {},
  mutations: {},
  getters: {},
  actions: {
    // 所有商票订单
    getAllOrderList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getAllOrderList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 重新发起商票状态轮询
    reStartTicketStatusQuery ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        reStartTicketStatusQuery(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 重新发起商票状态轮询
    findOrderDetails ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findOrderDetails(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getQuotationDetail ({ state, commit }, { quotationId }) {
      return new Promise((resolve, reject) => {
        getQuotationDetail({ quotationId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getInquireDetail ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getInquireDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
