import Main from '@/components/main'
// import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 *  notAuth: (false) 是否需要登录
 * }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/forget',
    name: 'forget',
    meta: {
      title: '忘记密码',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/forget/forget.vue')
  },
  {
    path: '/signin/company',
    name: 'signin-company',
    meta: {
      title: '注册',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/signin/signin-company.vue')
  },
  {
    path: '/signin/person',
    name: 'signin-person',
    meta: {
      title: '注册',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/signin/signin-person.vue')
  },
  {
    path: '/company/active',
    name: 'company-active',
    meta: {
      title: '公司激活',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/signin/company-active.vue')
  },
  {
    path: '/signin/privacy-statement',
    name: 'privacy-statement',
    meta: {
      title: '隐私权声明',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/signin/privacy-statement.vue')
  },
  {
    path: '/signin/service-agreement',
    name: 'service-agreement',
    meta: {
      title: '用户服务协议',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/signin/service-agreement.vue')
  },
  {
    path: '/signin/signature-statement',
    name: 'signature-statement',
    meta: {
      title: '电子签名数字证书用户使用须知',
      hideInMenu: true,
      notCache: true,
      notAuth: true
    },
    component: () => import('@/view/signin/signature-statement.vue')
  },
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: '/home',
        name: 'home',
        meta: {
          hideInMenu: true,
          title: '首页',
          notCache: true,
          icon: 'md-home'
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  {
    path: '/credit',
    name: 'credit',
    meta: {
      icon: 'md-bookmark',
      title: '授信管理'
    },
    component: Main,
    children: [
      {
        path: 'allapply',
        name: 'allapply',
        meta: {
          icon: 'md-albums',
          title: '所有申请',
          notCache: true,
          access: ['授信管理-所有申请']
        },
        component: () => import('@/view/credit/allApply/index.vue')
      },
      {
        path: 'viewapply',
        name: 'viewapply',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `授信审批详情-${route.query.creditApplyId}`,
          hideInMenu: true
        },
        component: () => import('@/view/credit/viewApply/index.vue')
      },
      {
        path: 'myapply',
        name: 'myapply',
        meta: {
          icon: 'ios-albums',
          title: '我的申请',
          notCache: true,
          access: ['授信管理-我的申请']
        },
        component: () => import('@/view/credit/myApply/index.vue')
      },
      {
        path: 'apply',
        name: 'apply',
        meta: {
          icon: 'md-add-circle',
          title: '申请授信',
          access: ['授信管理-申请授信']
        },
        component: () => import('@/view/credit/apply/apply.vue')
      },
      // {
      //   path: 'apply2',
      //   name: 'apply2',
      //   meta: {
      //     icon: 'md-add-circle',
      //     title: '申请授信2',
      //     access: ['授信管理-申请授信']
      //   },
      //   component: () => import('@/view/credit/apply/apply2.vue')
      // },
      {
        path: 'auditlist',
        name: 'auditlist',
        meta: {
          icon: 'md-brush',
          title: '授信审批',
          notCache: true,
          access: ['授信管理-授信审批']
        },
        component: () => import('@/view/credit/auditList/index.vue')
      },
      {
        path: 'myapplyaudit',
        name: 'myapplyaudit',
        meta: {
          icon: 'md-bookmarks',
          notCache: true,
          query: true,
          title: route => `审批-${route.query.creditApplyId}`,
          hideInMenu: true
        },
        component: () => import('@/view/credit/myApplyAudit/myApplyAudit.vue')
      },
      {
        path: 'applyaudit',
        name: 'applyaudit',
        meta: {
          icon: 'md-bookmarks',
          notCache: true,
          query: true,
          title: route => `审批-${route.query.creditApplyId}`,
          hideInMenu: true
        },
        component: () => import('@/view/credit/applyAudit/applyAudit.vue')
      },
      {
        path: 'allaudit',
        name: 'allaudit',
        meta: {
          icon: 'ios-paper',
          notCache: true,
          title: '所有授信',
          access: ['授信管理-所有授信']
        },
        component: () => import('@/view/credit/allAudit/index.vue')
      },
      {
        path: 'exportxlsx',
        name: 'exportxlsx',
        meta: {
          icon: 'ios-paper',
          title: '导入授信',
          hideInMenu: true
        },
        component: () => import('@/view/credit/exportXlsx/exportXlsx.vue')
      },
      {
        path: 'myaudit',
        name: 'myaudit',
        meta: {
          icon: 'ios-paper-outline',
          title: '我的授信',
          notCache: true,
          access: ['授信管理-我的授信']
        },
        component: () => import('@/view/credit/myAudit/index.vue')
      },
      {
        path: 'editapply',
        name: 'editapply',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `修改申请ID-${route.query.creditApplyId}`,
          hideInMenu: true
        },
        component: () => import('@/view/credit/edit/edit.vue')
      }
    ]
  },
  {
    path: '/ticketUse',
    name: 'ticketUse',
    meta: {
      icon: 'md-card',
      title: '用信管理'
    },
    component: Main,
    children: [
      {
        path: 'ticketapply',
        name: 'ticketapply',
        meta: {
          icon: 'md-bookmarks',
          title: '申请开商票',
          notCache: true,
          access: ['用信管理-申请开商票']
        },
        component: () => import('@/view/ticketUse/ticketApply/ticketApply.vue')
      },
      {
        path: 'allticketapply',
        name: 'allticketapply',
        meta: {
          icon: 'md-albums',
          title: '所有申请',
          notCache: true,
          access: ['用信管理-所有申请']
        },
        component: () => import('@/view/ticketUse/allTicketApply/index.vue')
      },
      {
        path: 'myticketapply',
        name: 'myticketapply',
        meta: {
          icon: 'ios-albums',
          title: '我的申请',
          notCache: true,
          access: ['用信管理-我的申请']
        },
        component: () => import('@/view/ticketUse/myTicketApply/index.vue')
      },
      {
        path: 'addticketuse',
        name: 'addticketuse',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `录入用信-${route.query.useApplyId || route.query.creditId}`,
          hideInMenu: true
        },
        component: () => import('@/view/ticketUse/addTicketUse/index.vue')
      },
      {
        path: 'editticketapply',
        name: 'editticketapply',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `编辑申请开商票-${route.query.creditApplyId}`,
          hideInMenu: true
        },
        component: () => import('@/view/ticketUse/editTicketApply/index.vue')
      },
      {
        path: 'viewticketapply',
        name: 'viewticketapply',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `用信申请详情-${route.query.creditApplyId}`,
          hideInMenu: true
        },
        component: () => import('@/view/ticketUse/viewTicketApply/index.vue')
      },
      {
        path: 'credituseaudit',
        name: 'credituseaudit',
        meta: {
          icon: 'md-brush',
          title: '用信审批',
          notCache: true,
          access: ['用信管理-用信审批']
        },
        component: () => import('@/view/ticketUse/creditUseAudit/index.vue')
      },
      {
        path: 'auditfinishlist',
        name: 'auditfinishlist',
        meta: {
          icon: 'md-clipboard',
          title: '录入用信',
          notCache: true,
          access: ['用信管理-录入用信']
        },
        component: () => import('@/view/ticketUse/auditFinishList/index.vue')
      },
      {
        path: 'auditticket',
        name: 'auditticket',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `开商票审批-${route.query.creditApplyId}`,
          hideInMenu: true
        },
        component: () => import('@/view/ticketUse/auditTicket/index.vue')
      },
      {
        path: 'allticketuse',
        name: 'allticketuse',
        meta: {
          icon: 'md-list-box',
          title: '所有用信',
          notCache: true,
          access: ['用信管理-所有用信']
        },
        component: () => import('@/view/ticketUse/allTicketUse/index.vue')
      },
      {
        path: 'myticketuse',
        name: 'myticketuse',
        meta: {
          icon: 'ios-list-box-outline',
          title: '我的用信',
          notCache: true,
          access: ['用信管理-我的用信']
        },
        component: () => import('@/view/ticketUse/myTicketUse/index.vue')
      },
      {
        path: 'ticketusedetail',
        name: 'ticketusedetail',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `用信详情-${route.query.id}`,
          hideInMenu: true
        },
        component: () => import('@/view/ticketUse/ticketUseDetail/index.vue')
      },
      {
        path: 'ticketlist',
        name: 'ticketlist',
        meta: {
          icon: 'md-list',
          title: '商票列表',
          notCache: true,
          access: ['用信管理-商票列表']
        },
        component: () => import('@/view/ticketUse/ticketList/index.vue')
      },
      {
        path: 'ticketdetail',
        name: 'ticketdetail',
        meta: {
          icon: 'md-bookmarks',
          query: true,
          notCache: true,
          title: route => `商票详情-${route.query.id}`,
          hideInMenu: true
        },
        component: () => import('@/view/ticketUse/ticketDetail/index.vue')
      }
    ]
  },
  // {
  //   path: '/operation',
  //   name: 'operation',
  //   meta: {
  //     icon: 'md-stats',
  //     title: '运营管理',
  //     access: ['运营管理-所有订单']
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'allorder',
  //       name: 'operation-allorder',
  //       meta: {
  //         icon: 'md-list',
  //         title: '所有订单',
  //         notCache: true,
  //         access: ['运营管理-所有订单']
  //       },
  //       component: () => import('@/view/operation/allOrder')
  //     },
  //     {
  //       path: 'orderdetail',
  //       name: 'operation-orderdetail',
  //       meta: {
  //         title: route => `订单详情-${route.query.orderId}`,
  //         query: true,
  //         notCache: true,
  //         hideInMenu: true
  //       },
  //       component: () => import('@/view/operation/orderDetail/orderDetail.vue')
  //     },
  //     {
  //       path: 'inquiredetail',
  //       name: 'operation-inquiredetail',
  //       meta: {
  //         title: route => `询价单详情-${route.query.id}`,
  //         query: true,
  //         notCache: true,
  //         hideInMenu: true
  //       },
  //       component: () => import('@/view/operation/inquire-detail/inquire-detail.vue')
  //     },
  //     {
  //       path: 'quotationdetail',
  //       name: 'operation-quotationdetail',
  //       meta: {
  //         title: route => `报价单详情-${route.query.quotationId}`,
  //         query: true,
  //         notCache: true,
  //         hideInMenu: true
  //       },
  //       component: () => import('@/view/operation/quotation-detail/quotation-detail.vue')
  //     }
  //   ]
  // },
  {
    path: '/riskModel',
    name: 'riskModel',
    meta: {
      icon: 'md-cog',
      title: '风控模型',
      access: ['风控模型-风控模型']
    },
    component: Main,
    children: [

      {
        path: 'riskControl/model/add-model',
        name: 'addModel',
        meta: {
          icon: 'md-calculator',
          title: '添加模型',
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/risk-control/model/addRiskModel.vue')
      },
      {
        path: 'riskControl/model/edit-model',
        name: 'editModel',
        meta: {
          icon: 'md-calculator',
          title: '编辑模型',
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/risk-control/model/editRiskModel.vue')
      },
      {
        path: 'riskControl/model/add-score',
        name: 'addScore',
        meta: {
          icon: 'md-calculator',
          title: '添加评分项',
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/risk-control/add-score/addScore.vue')
      },
      {
        path: 'riskControl/model/edit-score',
        name: 'editScore',
        meta: {
          icon: 'md-calculator',
          title: '编辑评分项',
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/risk-control/add-score/editScore.vue')
      },
      {
        path: 'riskControl/model/model-detail',
        name: 'modelDetail',
        meta: {
          icon: 'md-calculator',
          title: '查看模型',
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/risk-control/add-score/modelDetail.vue')
      },
      {
        path: 'riskControl/model/list',
        name: 'riskControlModelList',
        meta: {
          icon: 'md-calculator',
          title: '风控模型',
          notCache: true,
          hideInMenu: false
        },
        component: () => import('@/view/risk-control/model/list.vue')
      }
    ]
  },
  {
    path: '/riskData',
    name: 'riskData',
    meta: {
      icon: 'md-cog',
      title: '风控数据'
    },
    component: Main,
    children: [
      {
        path: 'lzProject/list',
        name: 'riskDataLzProjectList',
        meta: {
          icon: 'md-calculator',
          title: '深华项目',
          notCache: true,
          hideInMenu: false,
          access: ['风控数据-深华项目']
        },
        component: () => import('@/view/risk-data/lz-project/list.vue')
      },
      {
        path: 'lzProject/detail',
        name: 'riskDataLzProjectDetail',
        meta: {
          title: '深华项目详情',
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/risk-data/lz-project/detail.vue')
      }
    ]
  },
  {
    path: '/settings',
    name: 'settings',
    meta: {
      icon: 'md-settings',
      title: '设置'
    },
    component: Main,
    children: [
      {
        path: 'company/info',
        name: 'company-info',
        meta: {
          icon: 'md-information-circle',
          title: '公司信息',
          notCache: true,
          access: ['设置-公司信息']
        },
        component: () => import('@/view/settings/company/info.vue')
      },
      {
        path: 'company',
        name: 'company',
        meta: {
          icon: 'md-bookmarks',
          title: '公司管理',
          notCache: true,
          access: ['设置-公司管理']
        },
        component: () => import('@/view/settings/company/index.vue')
      },
      {
        path: 'company/add',
        name: 'company-add',
        meta: {
          icon: 'md-arrow-dropdown-circle',
          title: '添加公司',
          hideInMenu: true
        },
        component: () => import('@/view/settings/company/add.vue')
      },
      {
        path: 'company/audit',
        name: 'company-audit',
        meta: {
          icon: 'md-arrow-dropdown-circle',
          title: route => `审核公司-${route.query.name}`,
          query: true,
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/settings/company/audit.vue')
      },
      {
        path: 'company/auditinfo',
        name: 'company-auditinfo',
        meta: {
          icon: 'md-arrow-dropdown-circle',
          title: route => `公司详情-${route.query.name}`,
          query: true,
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/settings/company/audit-info.vue')
      },
      {
        path: 'company/detail',
        name: 'company-detail',
        meta: {
          icon: 'md-arrow-dropdown-circle',
          title: route => `公司详情-${route.query.companyName}`,
          query: true,
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/settings/company/company-detail.vue')
      },
      {
        path: 'company/audit-table',
        name: 'company-audit-table',
        meta: {
          icon: 'md-arrow-dropdown-circle',
          title: '公司审核',
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/settings/company/audit-table.vue')
      },
      {
        path: 'team',
        name: 'team',
        meta: {
          icon: 'md-people',
          title: '成员管理',
          notCache: true,
          access: ['设置-成员管理']
        },
        component: () => import('@/view/settings/team/team.vue')
      },
      {
        path: 'team/add',
        name: 'team-add',
        meta: {
          icon: 'md-arrow-dropdown-circle',
          title: route => `添加成员-${route.query.companyName}`,
          query: true,
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/settings/team/add.vue')
      },
      {
        path: 'team/edit',
        name: 'team-edit',
        meta: {
          icon: 'md-arrow-dropdown-circle',
          title: route => `修改成员-${route.query.email}`,
          query: true,
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/settings/team/edit.vue')
      },
      {
        path: 'role',
        name: 'role',
        meta: {
          icon: 'md-unlock',
          title: '权限管理',
          notCache: true,
          access: ['设置-权限管理']
        },
        component: () => import('@/view/settings/role/role.vue')
      },
      {
        path: 'company/real',
        name: 'company-real',
        meta: {
          icon: 'md-checkmark-circle-outline',
          title: '企业认证',
          notCache: true,
          access: ['设置-企业认证']
        },
        component: () => import('@/view/settings/company/real.vue')
      },
      {
        path: 'banks/pay',
        name: 'banks-pay',
        meta: {
          icon: 'md-card',
          title: '收款账户',
          notCache: true,
          access: ['设置-收款账户']
        },
        component: () => import('@/view/settings/banks/payBank.vue')
      },
      {
        path: 'banks/receipt',
        name: 'banks-receipt',
        meta: {
          icon: 'ios-card-outline',
          title: '收票账户',
          notCache: true,
          access: ['设置-收票账户']
        },
        component: () => import('@/view/settings/banks/receiptBank.vue')
      },
      {
        path: 'banks/add',
        name: 'banks-add',
        meta: {
          icon: 'md-bookmarks',
          title: '添加银行账户',
          hideInMenu: true
        },
        component: () => import('@/view/settings/banks/addBank.vue')
      },
      {
        path: 'company/exceptionalorder',
        name: 'company-exceptionalorder',
        meta: {
          icon: 'md-checkmark-circle-outline',
          title: '异常待处理订单',
          notCache: true,
          access: ['设置-异常待处理订单']
        },
        component: () => import('@/view/settings/company/exceptional-order.vue')
      },
      {
        path: 'user/setting',
        name: 'user-setting',
        meta: {
          icon: 'md-person',
          title: '个人设置',
          notCache: true
        },
        component: () => import('@/view/settings/user/information.vue')
      },
      {
        path: 'business',
        name: 'setting-business',
        meta: {
          icon: 'ios-git-compare',
          title: '业务设置',
          notCache: true,
          access: ['设置-业务设置']
        },
        component: () => import('@/view/settings/business/business.vue')
      },
      {
        path: 'business/flowExtend',
        name: 'setting-business-flowExtend',
        meta: {
          icon: 'md-person',
          title: '工作流设置',
          query: true,
          notCache: true,
          hideInMenu: true
        },
        component: () => import('@/view/settings/business/flowExtend.vue')
      }
    ]
  },
  // {
  //   path: '/error_store',
  //   name: 'error_store',
  //   meta: {
  //     hideInBread: true
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'error_store_page',
  //       name: 'error_store_page',
  //       meta: {
  //         icon: 'ios-bug',
  //         title: '错误收集'
  //       },
  //       component: () => import('@/view/error-store/error-store.vue')
  //     }
  //   ]
  // },
  // {
  //   path: '/error_logger',
  //   name: 'error_logger',
  //   meta: {
  //     hideInBread: true,
  //     hideInMenu: true
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'error_logger_page',
  //       name: 'error_logger_page',
  //       meta: {
  //         icon: 'ios-bug',
  //         title: '错误收集'
  //       },
  //       component: () => import('@/view/single-page/error-logger.vue')
  //     }
  //   ]
  // },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true,
      notAuth: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true,
      notAuth: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true,
      notAuth: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]
