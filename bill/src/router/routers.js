import Center from '@/view/center/center.vue'
import config from '@/config'
// import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
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
    path: '/index',
    name: 'index',
    meta: {
      title: '首页',
      hideInMenu: true,
      notAuth: true
    },
    component: () => import('@/view/index/index.vue')
  },
  {
    path: '/',
    name: '_index',
    redirect: '/index',
    meta: {
      title: '首页',
      hideInMenu: true,
      notAuth: true
    }
  },
  {
    path: '/billhall',
    name: 'billhall',
    meta: {
      title: '交易大厅',
      hideInMenu: true,
      notAuth: true
    },
    component: () => import('@/view/bill-hall/bill-hall.vue')
  },
  {
    path: '/rulestatement',
    name: 'rule-statement',
    meta: {
      title: '领筑数科商票交易平台信息撮合规则',
      hideInMenu: true,
      notAuth: true
    },
    component: () => import('@/view/publish-ticket/rule-statement.vue')
  },
  {
    path: '/publishticket',
    name: 'publishticket',
    meta: {
      title: '发布票据',
      hideInMenu: true,
      notAuth: true
    },
    component: () => import('@/view/publish-ticket/publish-ticket.vue')
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录',
      hideInMenu: true
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
    path: '/sell',
    name: '_sell',
    // redirect: '/sell/myinquirelist',
    component: Center,
    meta: {
      title: '我是票方',
      notCache: true,
      hideInMenu: false,
      notAuth: true,
      icon: 'md-home'
    },
    children: [
      {
        path: '/sell/_publishticket',
        name: '_publishticket',
        redirect: '/publishticket',
        meta: {
          title: '发布票据',
          hideInMenu: false,
          icon: 'icon-fabu'
        }
      },
      {
        path: '/sell/myinquirelist',
        name: 'myinquirelist',
        meta: {
          title: '我的发布',
          hideInMenu: false,
          icon: 'icon-bianzu'
        },
        component: () => import('@/view/sell/my-inquire-list/my-inquire-list.vue')
      },
      {
        path: '/sell/quotationlist',
        name: 'quotationlist',
        meta: {
          title: '报价列表',
          hideInMenu: false,
          icon: 'icon-liebiao'
        },
        component: () => import('@/view/sell/quotation-list/quotation-list.vue')
      },
      {
        path: '/sell/myorderlist',
        name: 'myorderlist',
        meta: {
          title: '我的订单',
          hideInMenu: false,
          icon: 'icon-wodedingdan'
        },
        component: () => import('@/view/sell/my-order-list/my-order-list.vue')
      },
      {
        path: '/sell/inquiredetail',
        name: 'inquiredetail',
        meta: {
          icon: 'icon-xunjia',
          title: '询价单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/inquire-detail/inquire-detail.vue')
      },
      {
        path: '/sell/quotationdetail',
        name: 'sellquotationdetail',
        meta: {
          icon: 'md-bookmarks',
          title: '报价单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/quotation-detail/quotation-detail.vue')
      },
      {
        path: '/sell/orderdetail',
        name: 'sellorderdetail',
        meta: {
          icon: 'icon-dingdan',
          title: '订单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/order-detail/order-detail.vue')
      }
    ]
  },
  {
    path: '/operationsMgt',
    name: '_operationsMgt',
    // redirect: '/operationsMgt/companyBanks',
    component: Center,
    meta: {
      title: '运营管理',
      notCache: true,
      hideInMenu: false,
      icon: 'md-home'
    },
    children: [
      {
        path: '/operationsMgt/companyBanks',
        name: 'companyBanks',
        meta: {
          title: '银行账户',
          hideInMenu: false,
          icon: 'icon-yinhangka',
          parentMenu: '_operationsMgt'
        },
        component: () => import('@/view/operation/bank/company-banks.vue')
      },
      {
        path: '/operationsMgt/allInquire',
        name: 'allInquire',
        meta: {
          title: '所有询价',
          hideInMenu: false,
          icon: 'icon-xunjia',
          parentMenu: '_operationsMgt'
        },
        component: () => import('@/view/operation/all-inquire/all-inquire.vue')
      },
      {
        path: '/operationsMgt/allQuotation',
        name: 'allQuotation',
        meta: {
          title: '所有报价',
          hideInMenu: false,
          icon: 'icon-liebiao',
          parentMenu: '_operationsMgt'
        },
        component: () => import('@/view/operation/all-quotation/all-quotation.vue')
      },
      {
        path: '/operationsMgt/allOrder',
        name: 'allOrder',
        meta: {
          title: '所有订单',
          hideInMenu: false,
          icon: 'icon-dingdan',
          parentMenu: '_operationsMgt'
        },
        component: () => import('@/view/operation/all-order/all-order.vue')
      },
      {
        path: '/operationsMgt/inquiredetail',
        name: 'operInquiredetail',
        meta: {
          icon: 'md-bookmarks',
          title: '询价单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/inquire-detail/inquire-detail.vue')
      },
      {
        path: '/operationsMgt/quotationdetail',
        name: 'operQuotationdetail',
        meta: {
          icon: 'md-bookmarks',
          title: '报价单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/quotation-detail/quotation-detail.vue')
      },
      {
        path: '/operationsMgt/orderdetail',
        name: 'operOrderdetail',
        meta: {
          icon: 'md-bookmarks',
          title: '订单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/order-detail/order-detail.vue')
      },
      {
        path: '/operationsMgt/serviceRate',
        name: 'serviceRate',
        meta: {
          title: '平台服务费',
          hideInMenu: false,
          icon: 'icon-yubeifeiyong',
          parentMenu: '_operationsMgt'
        },
        component: () => import('@/view/operation/service-rate/service-rate.vue')
      },
      {
        path: '/operationsMgt/bannerList',
        name: 'bannerList',
        meta: {
          title: 'Banner配置',
          hideInMenu: false,
          icon: 'icon-yubeifeiyong',
          parentMenu: '_operationsMgt'
        },
        component: () => import('@/view/operation/banner/list.vue')
      },
      {
        path: '/operationsMgt/addBanner',
        name: 'addBanner',
        meta: {
          title: '添加banner',
          hideInMenu: false,
          icon: 'icon-yubeifeiyong',
          parentMenu: '_operationsMgt'
        },
        component: () => import('@/view/operation/banner/save.vue')
      }
    ]
  },
  {
    path: '/buy',
    name: '_buy',
    // redirect: '/buy/myquotationlist',
    component: Center,
    meta: {
      title: '我是资方',
      notCache: true,
      hideInMenu: false,
      icon: 'md-home'
    },
    children: [
      {
        path: '/buy/myquotationlist',
        name: 'myquotationlist',
        meta: {
          title: '我的报价',
          hideInMenu: false,
          icon: 'icon-qian'
        },
        component: () => import('@/view/buy/my-quotation-list/my-quotation-list.vue')
      },
      {
        path: '/buy/myorderlist',
        name: 'orderlist',
        meta: {
          title: '我的订单',
          hideInMenu: false,
          icon: 'icon-wodedingdan'
        },
        component: () => import('@/view/buy/order-list/order-list.vue')
      },
      {
        path: '/buy/myassignlist',
        name: 'myassignlist',
        meta: {
          title: '给我的询价',
          hideInMenu: false,
          icon: 'icon-xunjia'
        },
        component: () => import('@/view/buy/my-assign-list/my-assign-list.vue')
      },
      {
        path: '/buy/quotationdetail',
        name: 'buyquotationdetail',
        meta: {
          icon: 'md-bookmarks',
          title: '询价单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/quotation-detail/quotation-detail.vue')
      },
      {
        path: '/buy/orderdetail',
        name: 'buyorderdetail',
        meta: {
          icon: 'md-bookmarks',
          title: '订单详情',
          hideInMenu: true
        },
        component: () => import('@/view/sell/order-detail/order-detail.vue')
      },
      {
        path: '/buy/selectcontract',
        name: 'buyselectcontract',
        meta: {
          icon: 'md-bookmarks',
          title: '选择合同',
          hideInMenu: true
        },
        component: () => import('@/view/buy/select-contract/select-contract.vue')
      },
      {
        path: '/buy/addacceptor/',
        name: 'buyaddacceptor',
        meta: {
          icon: 'icon-zengjia',
          title: '新增承兑方',
          hideInMenu: false
        },
        component: () => import('@/view/buy/acceptor/add-acceptor.vue')
      },
      {
        path: '/buy/acceptorlist',
        name: 'buyacceptorlist',
        meta: {
          icon: 'icon-liebiao',
          title: '承兑方列表',
          hideInMenu: false
        },
        component: () => import('@/view/buy/acceptor/acceptor-list.vue')
      },
      {
        path: '/buy/updateacceptor',
        name: 'buyupdateacceptor',
        meta: {
          icon: 'md-list',
          title: '修改承兑方',
          hideInMenu: true
        },
        component: () => import('@/view/buy/acceptor/update-acceptor.vue')
      }
    ]
  },
  {
    path: '',
    name: 'doc',
    meta: {
      title: '设置',
      href: config.settingUrl,
      hideInMenu: false,
      icon: 'md-settings'
    }
  },
  {
    path: '/about',
    name: 'about',
    meta: {
      title: '关于我们',
      notCache: true,
      hideInMenu: true,
      icon: 'md-home',
      notAuth: true
    },
    component: () => import('@/view/introduction/about.vue')
  },
  {
    path: '/model',
    name: 'model',
    meta: {
      title: '平台模式',
      notCache: true,
      hideInMenu: true,
      icon: 'md-home',
      notAuth: true
    },
    component: () => import('@/view/introduction/model.vue')
  },
  {
    path: '/safety',
    name: 'safety',
    meta: {
      title: '安全保障',
      notCache: true,
      hideInMenu: true,
      icon: 'md-home',
      notAuth: true
    },
    component: () => import('@/view/introduction/safety.vue')
  },
  {
    path: '/agreement',
    name: 'agreement',
    meta: {
      title: '规则协议',
      notCache: true,
      hideInMenu: true,
      icon: 'md-home',
      notAuth: true
    },
    component: () => import('@/view/introduction/agreement.vue')
  },
  {
    path: '/contact',
    name: 'contact',
    meta: {
      title: '联系我们',
      notCache: true,
      hideInMenu: true,
      icon: 'md-home',
      notAuth: true
    },
    component: () => import('@/view/introduction/contact.vue')
  },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]
