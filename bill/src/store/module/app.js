import {
  getBreadCrumbList,
  setTagNavListInLocalstorage,
  getMenuByRouter,
  getTagNavListFromLocalstorage,
  getHomeRoute,
  getNextRoute,
  routeHasExist,
  routeEqual,
  getRouteTitleHandled,
  localSave,
  localRead
} from '@/libs/util'
import {
  getNatureSelectData,
  getStaffsizeSelectData,
  getSystemVersionList,
  getBankList,
  getProductList,
  saveErrorLogger
} from '@/api/app'
import router from '@/router'
import routers from '@/router/routers'
import config from '@/config'
const { homeName } = config

const closePage = (state, route) => {
  const nextRoute = getNextRoute(state.tagNavList, route)
  state.tagNavList = state.tagNavList.filter(item => {
    return !routeEqual(item, route)
  })
  router.push(nextRoute)
}

export default {
  state: {
    breadCrumbList: [],
    tagNavList: [],
    homeRoute: {},
    local: localRead('local'),
    errorList: [],
    hasReadErrorPage: false,
    natureList: [],
    staffsizeList: [],
    systemVersionList: [],
    bankList: [],
    productList: []
  },
  getters: {
    menuList: (state, getters, rootState) => getMenuByRouter(routers, rootState.user.access),
    errorCount: state => state.errorList.length
  },
  mutations: {
    setBreadCrumb (state, route) {
      state.breadCrumbList = getBreadCrumbList(route, state.homeRoute)
    },
    setHomeRoute (state, routes) {
      state.homeRoute = getHomeRoute(routes, homeName)
    },
    setTagNavList (state, list) {
      let tagList = []
      if (list) {
        tagList = [...list]
      } else tagList = getTagNavListFromLocalstorage() || []
      if (tagList[0] && tagList[0].name !== homeName) tagList.shift()
      let homeTagIndex = tagList.findIndex(item => item.name === homeName)
      if (homeTagIndex > 0) {
        let homeTag = tagList.splice(homeTagIndex, 1)[0]
        tagList.unshift(homeTag)
      }
      state.tagNavList = tagList
      setTagNavListInLocalstorage([...tagList])
    },
    closeTag (state, route) {
      let tag = state.tagNavList.filter(item => routeEqual(item, route))
      route = tag[0] ? tag[0] : null
      if (!route) return
      closePage(state, route)
    },
    addTag (state, { route, type = 'unshift' }) {
      let router = getRouteTitleHandled(route)
      if (!routeHasExist(state.tagNavList, router)) {
        if (type === 'push') state.tagNavList.push(router)
        else {
          if (router.name === homeName) state.tagNavList.unshift(router)
          else state.tagNavList.splice(1, 0, router)
        }
        setTagNavListInLocalstorage([...state.tagNavList])
      }
    },
    setLocal (state, lang) {
      localSave('local', lang)
      state.local = lang
    },
    addError (state, error) {
      state.errorList.push(error)
    },
    setHasReadErrorLoggerStatus (state, status = true) {
      state.hasReadErrorPage = status
    },
    setStaffsizeList (state, list) {
      state.staffsizeList = list
    },
    setNature (state, list) {
      state.natureList = list
    },
    setSystemVersionList (state, list) {
      state.systemVersionList = list.map(item => {
        item.id = item.id + ''
        return item
      })
    },
    setBankList (state, list) {
      state.bankList = list
    },
    setProductList (state, list) {
      state.productList = list
    }
  },
  actions: {
    // 行业分类
    getNatureSelectData ({ state, commit }, { key }) {
      if (!key && state.natureList.length) {
        return
      }
      return new Promise((resolve, reject) => {
        getNatureSelectData({ key }).then(res => {
          const data = res.data
          if (key) {
            resolve(res)
          } else {
            commit('setNature', data.data)
            resolve()
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 公司规模
    getStaffsizeSelectData ({ state, commit }) {
      if (state.staffsizeList.length) {
        return
      }
      return new Promise((resolve, reject) => {
        getStaffsizeSelectData().then(res => {
          const data = res.data
          commit('setStaffsizeList', data.data)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 系统版本
    getSystemVersionList ({ state, commit }) {
      if (state.systemVersionList.length) {
        return
      }
      return new Promise((resolve, reject) => {
        getSystemVersionList().then(res => {
          const data = res.data
          commit('setSystemVersionList', data.data)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 银行
    getBankList ({ state, commit }) {
      if (state.bankList.length) {
        return
      }
      return new Promise((resolve, reject) => {
        getBankList().then(res => {
          const data = res.data
          commit('setBankList', data.data)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 金融产品
    getProductList ({ state, commit }) {
      if (state.productList.length) {
        return
      }
      return new Promise((resolve, reject) => {
        getProductList().then(res => {
          const data = res.data
          commit('setProductList', data.data)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    addErrorLog ({ commit, rootState }, info) {
      if (!window.location.href.includes('error_logger_page')) commit('setHasReadErrorLoggerStatus', false)
      const { user: { token, userId, userName } } = rootState
      let data = {
        ...info,
        time: Date.parse(new Date()),
        token,
        userId,
        userName
      }
      saveErrorLogger(info).then(() => {
        commit('addError', data)
      })
    }
  }
}
