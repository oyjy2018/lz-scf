import {
  login,
  logout,
  reg,
  regPersonEmail,
  getVCode,
  checkVCode,
  getUserInfo,
  checkedRegisterPhone,
  updatePassword,
  reSendInvite,
  getUserByCompanyId,
  getMessage,
  getContentByMsgId,
  hasRead,
  removeReaded,
  restoreTrash,
  getUnreadCount
} from '@/api/user'
import { setToken, getToken, setAccess, getAccess, setPermission, getPermission, setUserName, getUserName, setRoleType, getRoleType } from '@/libs/util'

export default {
  state: {
    userName: getUserName(),
    userId: '',
    avatarImgPath: '',
    token: getToken(),
    access: getAccess(),
    permission: getPermission(),
    roleType: getRoleType(),
    hasGetInfo: false,
    unreadCount: 0,
    messageUnreadList: [],
    messageReadedList: [],
    messageTrashList: [],
    messageContentStore: {}
  },
  mutations: {
    setAvatar (state, avatarPath) {
      state.avatarImgPath = avatarPath
    },
    setUserId (state, id) {
      state.userId = id
    },
    setUserName (state, name) {
      state.userName = name
      setUserName(name)
    },
    setAccess (state, access) {
      state.access = access.split(',')
      setAccess(access)
    },
    setToken (state, token) {
      state.token = token
      setToken(token)
    },
    setPermission (state, permission) {
      state.permission = permission
      setPermission(permission)
    },
    setRoleType (state, type) {
      state.roleType = type
      setRoleType(type)
    },
    setHasGetInfo (state, status) {
      state.hasGetInfo = status
    },
    setMessageCount (state, count) {
      state.unreadCount = count
    },
    setMessageUnreadList (state, list) {
      state.messageUnreadList = list
    },
    setMessageReadedList (state, list) {
      state.messageReadedList = list
    },
    setMessageTrashList (state, list) {
      state.messageTrashList = list
    },
    updateMessageContentStore (state, { msg_id, content }) {
      state.messageContentStore[msg_id] = content
    },
    moveMsg (state, { from, to, msg_id }) {
      const index = state[from].findIndex(_ => _.msg_id === msg_id)
      const msgItem = state[from].splice(index, 1)[0]
      msgItem.loading = false
      state[to].unshift(msgItem)
    }
  },
  getters: {
    messageUnreadCount: state => state.messageUnreadList.length,
    messageReadedCount: state => state.messageReadedList.length,
    messageTrashCount: state => state.messageTrashList.length
  },
  actions: {
    // 登录
    handleLogin ({ commit }, { loginAccount, password }) {
      loginAccount = loginAccount.trim()
      return new Promise((resolve, reject) => {
        login({
          loginAccount,
          password
        }).then(res => {
          const data = res.data
          if (data.code === 10000) {
            commit('setToken', data.data.AuthenticationToken)
            commit('setUserName', `${data.data.companyName}-${data.data.roleNames}-${data.data.userName}`)
            commit('setAccess', data.data.menuList)
            commit('setPermission', data.data.permissionList)
            commit('setRoleType', data.data.permissionType)
          }
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    removeLogin ({ state, commit }) {
      commit('setToken', '')
      commit('setAccess', '')
      commit('setPermission', '')
      commit('setUserName', '')
    },
    // 退出登录
    handleLogOut ({ state, commit }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(res => {
          commit('setToken', '')
          commit('setAccess', '')
          commit('setPermission', '')
          commit('setUserName', '')
          resolve(res)
        }).catch(err => {
          reject(err)
        })
        // 如果你的退出登录无需请求接口，则可以直接使用下面三行代码而无需使用logout调用接口
        // commit('setToken', '')
        // commit('setAccess', [])
        // resolve()
      })
    },
    // 公司注册
    handleReg ({ commit }, data) {
      return new Promise((resolve, reject) => {
        reg(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 个人邮件受邀注册
    regPersonEmail ({ commit }, data) {
      return new Promise((resolve, reject) => {
        regPersonEmail(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取验证码
    getVCode ({ state, commit }, data) {
      return new Promise((resolve, reject) => {
        getVCode(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 验证验证码
    checkVCode ({ state, commit }, { phone, vcode }) {
      return new Promise((resolve, reject) => {
        checkVCode({ phone, vcode }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 验证手机是否注册
    checkedRegisterPhone ({ state, commit }, { phone }) {
      return new Promise((resolve, reject) => {
        checkedRegisterPhone({ phone }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 修改密码
    updatePassword ({ commit }, data) {
      return new Promise((resolve, reject) => {
        updatePassword(data).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    reSendInvite ({ state, commit }, { email }) {
      return new Promise((resolve, reject) => {
        reSendInvite({ email }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUserByCompanyId ({ state, commit }, { companyId }) {
      return new Promise((resolve, reject) => {
        getUserByCompanyId({ companyId }).then(res => {
          resolve(res)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取用户相关信息
    getUserInfo ({ state, commit }) {
      return new Promise((resolve, reject) => {
        try {
          getUserInfo(state.token).then(res => {
            const data = res.data
            commit('setAvatar', data.avatar)
            commit('setUserName', data.name)
            commit('setUserId', data.user_id)
            commit('setAccess', data.access)
            commit('setHasGetInfo', true)
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
    // 此方法用来获取未读消息条数，接口只返回数值，不返回消息列表
    getUnreadMessageCount ({ state, commit }) {
      getUnreadCount().then(res => {
        const { data } = res
        commit('setMessageCount', data)
      })
    },
    // 获取消息列表，其中包含未读、已读、回收站三个列表
    getMessageList ({ state, commit }) {
      return new Promise((resolve, reject) => {
        getMessage().then(res => {
          const { unread, readed, trash } = res.data
          commit('setMessageUnreadList', unread.sort((a, b) => new Date(b.create_time) - new Date(a.create_time)))
          commit('setMessageReadedList', readed.map(_ => {
            _.loading = false
            return _
          }).sort((a, b) => new Date(b.create_time) - new Date(a.create_time)))
          commit('setMessageTrashList', trash.map(_ => {
            _.loading = false
            return _
          }).sort((a, b) => new Date(b.create_time) - new Date(a.create_time)))
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 根据当前点击的消息的id获取内容
    getContentByMsgId ({ state, commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        let contentItem = state.messageContentStore[msg_id]
        if (contentItem) {
          resolve(contentItem)
        } else {
          getContentByMsgId(msg_id).then(res => {
            const content = res.data
            commit('updateMessageContentStore', { msg_id, content })
            resolve(content)
          })
        }
      })
    },
    // 把一个未读消息标记为已读
    hasRead ({ state, commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        hasRead(msg_id).then(() => {
          commit('moveMsg', {
            from: 'messageUnreadList',
            to: 'messageReadedList',
            msg_id
          })
          commit('setMessageCount', state.unreadCount - 1)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 删除一个已读消息到回收站
    removeReaded ({ commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        removeReaded(msg_id).then(() => {
          commit('moveMsg', {
            from: 'messageReadedList',
            to: 'messageTrashList',
            msg_id
          })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 还原一个已删除消息到已读消息
    restoreTrash ({ commit }, { msg_id }) {
      return new Promise((resolve, reject) => {
        restoreTrash(msg_id).then(() => {
          commit('moveMsg', {
            from: 'messageTrashList',
            to: 'messageReadedList',
            msg_id
          })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}
