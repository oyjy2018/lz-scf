import {
  getIndex,
  getInquireList,
  getMyInquireList,
  getInquireDetail,
  cancelInquire,
  inquireBusinessTicket,
  ocrRecognize,
  quoteTicket,
  getMyQuotationList,
  getQuotationDetail,
  cancelQuotation,
  getTicketQuotationList,
  acceptedQuotation,
  rejectQuotation,
  findOrderList,
  findOrderDetails,
  getContractContent,
  sendCode,
  signContract,
  getCompanyJdProperty,
  payOrder,
  revokeOrder,
  changeContract,
  isOnlyKeyWord,
  getInitContractViewUrl,
  quotationCompanyList,
  myAssignInquireList,
  acceptorList,
  deleteAcceptor,
  updateAcceptor,
  addAcceptor,
  acceptorDetail,
  updateAcceptorLimit,
  isLimit,
  allInquire,
  allOrder,
  allQuotation,
  companyBanks,
  reStartTicketStatusQuery,
  platformServiceRateSave,
  platformServiceRateQuery,
  findBannerList,
  addBanner,
  deleteBanner,
  updateBannerUseStatus,
  bannerSort,
  bannerDetail,
  updateBanner,
  getBannerCarousel
} from '@/api/front'
// import router from '@/router'
// import routers from '@/router/routers'
// import config from '@/config'
// const { homeName } = config

// const closePage = (state, route) => {
//   const nextRoute = getNextRoute(state.tagNavList, route)
//   state.tagNavList = state.tagNavList.filter(item => {
//     return !routeEqual(item, route)
//   })
//   router.push(nextRoute)
// }

export default {
  state: {
    // breadCrumbList: [],
    // tagNavList: [],
    // homeRoute: {},
    // local: localRead('local'),
    // errorList: [],
    // hasReadErrorPage: false
  },
  getters: {
    // menuList: (state, getters, rootState) => getMenuByRouter(routers, rootState.user.access),
    // errorCount: state => state.errorList.length
  },
  mutations: {
    // setBreadCrumb1 (state, route) {
    //   state.breadCrumbList = getBreadCrumbList(route, state.homeRoute)
    // }
  },
  actions: {
    getCompanyJdProperty () {
      return new Promise((resolve, reject) => {
        getCompanyJdProperty().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getIndex () {
      return new Promise((resolve, reject) => {
        getIndex().then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    inquireBusinessTicket ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        inquireBusinessTicket(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getInquireList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getInquireList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getMyInquireList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getMyInquireList(data).then(res => {
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
    },
    cancelInquire ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        cancelInquire(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    ocrRecognize ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        ocrRecognize(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    quoteTicket ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        quoteTicket(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getTicketQuotationList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getTicketQuotationList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getMyQuotationList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getMyQuotationList(data).then(res => {
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
    cancelQuotation ({ state, commit }, { quotationId }) {
      return new Promise((resolve, reject) => {
        cancelQuotation({ quotationId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    acceptedQuotation ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        acceptedQuotation(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    rejectQuotation ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        rejectQuotation(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findOrderList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findOrderList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findOrderDetails ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findOrderDetails(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getContractContent ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getContractContent(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    sendCode ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        sendCode(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    signContract ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        signContract(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    payOrder ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        payOrder(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    revokeOrder ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        revokeOrder(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    changeContract ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        changeContract(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    isOnlyKeyWord ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        isOnlyKeyWord(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getInitContractViewUrl ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getInitContractViewUrl(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    quotationCompanyList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        quotationCompanyList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    myAssignInquireList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        myAssignInquireList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    acceptorList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        acceptorList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteAcceptor ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        deleteAcceptor(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateAcceptor ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateAcceptor(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addAcceptor ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addAcceptor(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    acceptorDetail ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        acceptorDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateAcceptorLimit ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateAcceptorLimit(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    isLimit ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        isLimit(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    allInquire ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        allInquire(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    allOrder ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        allOrder(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    allQuotation ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        allQuotation(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    companyBanks ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        companyBanks(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    reStartTicketStatusQuery ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        reStartTicketStatusQuery(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    platformServiceRateSave ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        platformServiceRateSave(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    platformServiceRateQuery ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        platformServiceRateQuery(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    findBannerList ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        findBannerList(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    addBanner ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        addBanner(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    deleteBanner ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        deleteBanner(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateBannerUseStatus ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateBannerUseStatus(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    bannerSort ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        bannerSort(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    bannerDetail ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        bannerDetail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateBanner ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        updateBanner(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getBannerCarousel ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getBannerCarousel(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
