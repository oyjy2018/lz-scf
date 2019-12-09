import Vue from 'vue'
import Vuex from 'vuex'

import user from './module/user'
import app from './module/app'
import setting from './module/setting'
import team from './module/team'
import role from './module/role'
import credit from './module/credit'
import ticket from './module/ticket'
import risk from './module/risk'
import operation from './module/operation'
import business from './module/business'
import lzProject from './module/lz-project'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //
  },
  mutations: {
    //
  },
  actions: {
    //
  },
  modules: {
    user,
    app,
    setting,
    team,
    role,
    credit,
    ticket,
    operation,
    business,
    risk,
    lzProject
  }
})
