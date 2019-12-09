import Cookies from 'js-cookie'
// cookie保存的天数
import config from '@/config'
import { forEach, hasOneOf, objEqual } from '@/libs/tools'
const { title, cookieExpires, useI18n } = config

export const TOKEN_KEY = 'token'
export const ACCESS_KEY = 'access'
export const PERMISSION = 'permission'
export const ROLETYPE = 'roleType'
export const USERNAME = 'userName'

export const setToken = (token) => {
  Cookies.set(TOKEN_KEY, token, { expires: cookieExpires || 1 })
}

export const getToken = () => {
  const token = Cookies.get(TOKEN_KEY)
  if (token) return token
  else return false
}

export const setAccess = (access) => {
  Cookies.set(ACCESS_KEY, access, { expires: cookieExpires || 1 })
}

export const getAccess = () => {
  const access = Cookies.get(ACCESS_KEY)
  if (access) return access.split(',')
  else return ''
}

export const setPermission = (permission) => {
  Cookies.set(PERMISSION, permission, { expires: cookieExpires || 1 })
}

export const getPermission = () => {
  const permission = Cookies.get(PERMISSION)
  if (permission) return permission.split(',')
  else return ''
}

export const setUserName = (name) => {
  Cookies.set(USERNAME, name, { expires: cookieExpires || 1 })
}

export const getUserName = () => {
  const name = Cookies.get(USERNAME)
  if (name) return name
  else return ''
}

export const setRoleType = (type) => {
  Cookies.set(ROLETYPE, type, { expires: cookieExpires || 1 })
}

export const getRoleType = () => {
  const type = Cookies.get(ROLETYPE)
  if (type) return type
  else return ''
}

export const hasChild = (item) => {
  return item.children && item.children.length !== 0
}

const showThisMenuEle = (item, access) => {
  if (item.meta && item.meta.access && item.meta.access.length) {
    if (hasOneOf(item.meta.access, access)) return true
    else return false
  } else return true
}
/**
 * @param {Array} list 通过路由列表得到菜单列表
 * @returns {Array}
 */
export const getMenuByRouter = (list, access) => {
  let res = []
  forEach(list, item => {
    if (!item.meta || (item.meta && !item.meta.hideInMenu)) {
      let obj = {
        icon: (item.meta && item.meta.icon) || '',
        name: item.name,
        meta: item.meta
      }
      if ((hasChild(item) || (item.meta && item.meta.showAlways)) && showThisMenuEle(item, access)) {
        obj.children = getMenuByRouter(item.children, access)
      }
      if (item.meta && item.meta.href) obj.href = item.meta.href
      if (showThisMenuEle(item, access)) res.push(obj)
    }
  })
  return res
}

/**
 * @param {Array} routeMetched 当前路由metched
 * @returns {Array}
 */
export const getBreadCrumbList = (route, homeRoute) => {
  let homeItem = { ...homeRoute, icon: homeRoute.meta.icon }
  let routeMetched = route.matched
  if (routeMetched.some(item => item.name === homeRoute.name)) return [homeItem]
  let res = routeMetched.filter(item => {
    return item.meta === undefined || !item.meta.hideInBread
  }).map(item => {
    let meta = { ...item.meta }
    if (meta.title && typeof meta.title === 'function') {
      meta.__titleIsFunction__ = true
      meta.title = meta.title(route)
    }
    let obj = {
      icon: (item.meta && item.meta.icon) || '',
      name: item.name,
      meta: meta
    }
    return obj
  })
  res = res.filter(item => {
    return !item.meta.hideInMenu
  })
  return [{ ...homeItem, to: homeRoute.path }, ...res]
}

export const getRouteTitleHandled = (route) => {
  let router = { ...route }
  let meta = { ...route.meta }
  let title = ''
  if (meta.title) {
    if (typeof meta.title === 'function') {
      meta.__titleIsFunction__ = true
      title = meta.title(router)
    } else title = meta.title
  }
  meta.title = title
  router.meta = meta
  return router
}

export const showTitle = (item, vm) => {
  let { title, __titleIsFunction__ } = item.meta
  if (!title) return
  if (useI18n) {
    if (title.includes('{{') && title.includes('}}') && useI18n) title = title.replace(/({{[\s\S]+?}})/, (m, str) => str.replace(/{{([\s\S]*)}}/, (m, _) => vm.$t(_.trim())))
    else if (__titleIsFunction__) title = item.meta.title
    else title = vm.$t(item.name)
  } else title = (item.meta && item.meta.title) || item.name
  return title
}

/**
 * @description 本地存储和获取标签导航列表
 */
export const setTagNavListInLocalstorage = list => {
  list.length = 1
  localStorage.tagNaveList = JSON.stringify(list)
}
/**
 * @returns {Array} 其中的每个元素只包含路由原信息中的name, path, meta三项
 */
export const getTagNavListFromLocalstorage = () => {
  const list = localStorage.tagNaveList
  return list ? JSON.parse(list) : []
}

/**
 * @param {Array} routers 路由列表数组
 * @description 用于找到路由列表中name为home的对象
 */
export const getHomeRoute = (routers, homeName = 'home') => {
  let i = -1
  let len = routers.length
  let homeRoute = {}
  while (++i < len) {
    let item = routers[i]
    if (item.children && item.children.length) {
      let res = getHomeRoute(item.children, homeName)
      if (res.name) return res
    } else {
      if (item.name === homeName) homeRoute = item
    }
  }
  return homeRoute
}

/**
 * @param {*} list 现有标签导航列表
 * @param {*} newRoute 新添加的路由原信息对象
 * @description 如果该newRoute已经存在则不再添加
 */
export const getNewTagList = (list, newRoute) => {
  const { name, path, meta } = newRoute
  let newList = [...list]
  if (newList.findIndex(item => item.name === name) >= 0) return newList
  else newList.push({ name, path, meta })
  return newList
}

/**
 * @param {*} access 用户权限数组，如 ['super_admin', 'admin']
 * @param {*} route 路由列表
 */
const hasAccess = (access, route) => {
  if (route.meta && route.meta.access) return hasOneOf(access, route.meta.access)
  else return true
}

/**
 * 权鉴
 * @param {*} name 即将跳转的路由name
 * @param {*} access 用户权限数组
 * @param {*} routes 路由列表
 * @description 用户是否可跳转到该页
 */
export const canTurnTo = (name, access, routes) => {
  const routePermissionJudge = (list) => {
    return list.some(item => {
      if (item.children && item.children.length) {
        return routePermissionJudge(item.children)
      } else if (item.name === name) {
        return hasAccess(access, item)
      }
    })
  }

  return routePermissionJudge(routes)
}

/**
 * @param {String} url
 * @description 从URL中解析参数
 */
export const getParams = url => {
  const keyValueArr = url.split('?')[1].split('&')
  let paramObj = {}
  keyValueArr.forEach(item => {
    const keyValue = item.split('=')
    paramObj[keyValue[0]] = keyValue[1]
  })
  return paramObj
}

/**
 * @param {Array} list 标签列表
 * @param {String} name 当前关闭的标签的name
 */
export const getNextRoute = (list, route) => {
  let res = {}
  if (list.length === 2) {
    res = getHomeRoute(list)
  } else {
    const index = list.findIndex(item => routeEqual(item, route))
    if (index === list.length - 1) res = list[list.length - 2]
    else res = list[index + 1]
  }
  return res
}

/**
 * @param {Number} times 回调函数需要执行的次数
 * @param {Function} callback 回调函数
 */
export const doCustomTimes = (times, callback) => {
  let i = -1
  while (++i < times) {
    callback(i)
  }
}

/**
 * @param {Object} file 从上传组件得到的文件对象
 * @returns {Promise} resolve参数是解析后的二维数组
 * @description 从Csv文件中解析出表格，解析成二维数组
 */
export const getArrayFromFile = (file) => {
  let nameSplit = file.name.split('.')
  let format = nameSplit[nameSplit.length - 1]
  return new Promise((resolve, reject) => {
    let reader = new FileReader()
    reader.readAsText(file) // 以文本格式读取
    let arr = []
    reader.onload = function (evt) {
      let data = evt.target.result // 读到的数据
      let pasteData = data.trim()
      arr = pasteData.split((/[\n\u0085\u2028\u2029]|\r\n?/g)).map(row => {
        return row.split('\t')
      }).map(item => {
        return item[0].split(',')
      })
      if (format === 'csv') resolve(arr)
      else reject(new Error('[Format Error]:你上传的不是Csv文件'))
    }
  })
}

/**
 * @param {Array} array 表格数据二维数组
 * @returns {Object} { columns, tableData }
 * @description 从二维数组中获取表头和表格数据，将第一行作为表头，用于在iView的表格中展示数据
 */
export const getTableDataFromArray = (array) => {
  let columns = []
  let tableData = []
  if (array.length > 1) {
    let titles = array.shift()
    columns = titles.map(item => {
      return {
        title: item,
        key: item
      }
    })
    tableData = array.map(item => {
      let res = {}
      item.forEach((col, i) => {
        res[titles[i]] = col
      })
      return res
    })
  }
  return {
    columns,
    tableData
  }
}

export const findNodeUpper = (ele, tag) => {
  if (ele.parentNode) {
    if (ele.parentNode.tagName === tag.toUpperCase()) {
      return ele.parentNode
    } else {
      return findNodeUpper(ele.parentNode, tag)
    }
  }
}

export const findNodeUpperByClasses = (ele, classes) => {
  let parentNode = ele.parentNode
  if (parentNode) {
    let classList = parentNode.classList
    if (classList && classes.every(className => classList.contains(className))) {
      return parentNode
    } else {
      return findNodeUpperByClasses(parentNode, classes)
    }
  }
}

export const findNodeDownward = (ele, tag) => {
  const tagName = tag.toUpperCase()
  if (ele.childNodes.length) {
    let i = -1
    let len = ele.childNodes.length
    while (++i < len) {
      let child = ele.childNodes[i]
      if (child.tagName === tagName) return child
      else return findNodeDownward(child, tag)
    }
  }
}

export const showByAccess = (access, canViewAccess) => {
  return hasOneOf(canViewAccess, access)
}

/**
 * @description 根据name/params/query判断两个路由对象是否相等
 * @param {*} route1 路由对象
 * @param {*} route2 路由对象
 */
export const routeEqual = (route1, route2) => {
  const params1 = route1.params || {}
  const params2 = route2.params || {}
  const query1 = route1.query || {}
  const query2 = route2.query || {}
  return (route1.name === route2.name) && objEqual(params1, params2) && objEqual(query1, query2)
}

/**
 * 判断打开的标签列表里是否已存在这个新添加的路由对象
 */
export const routeHasExist = (tagNavList, routeItem) => {
  let len = tagNavList.length
  let res = false
  doCustomTimes(len, (index) => {
    if (routeEqual(tagNavList[index], routeItem)) res = true
  })
  return res
}

export const localSave = (key, value) => {
  localStorage.setItem(key, value)
}

export const localRead = (key) => {
  return localStorage.getItem(key) || ''
}

// scrollTop animation
export const scrollTop = (el, from = 0, to, duration = 500, endCallback) => {
  if (!window.requestAnimationFrame) {
    window.requestAnimationFrame = (
      window.webkitRequestAnimationFrame ||
      window.mozRequestAnimationFrame ||
      window.msRequestAnimationFrame ||
      function (callback) {
        return window.setTimeout(callback, 1000 / 60)
      }
    )
  }
  const difference = Math.abs(from - to)
  const step = Math.ceil(difference / duration * 50)

  const scroll = (start, end, step) => {
    if (start === end) {
      endCallback && endCallback()
      return
    }

    let d = (start + step > end) ? end : start + step
    if (start > end) {
      d = (start - step < end) ? end : start - step
    }

    if (el === window) {
      window.scrollTo(d, d)
    } else {
      el.scrollTop = d
    }
    window.requestAnimationFrame(() => scroll(d, end, step))
  }
  scroll(from, to, step)
}

/**
 * @description 根据当前跳转的路由设置显示在浏览器标签的title
 * @param {Object} routeItem 路由对象
 * @param {Object} vm Vue实例
 */
export const setTitle = (routeItem, vm) => {
  const handledRoute = getRouteTitleHandled(routeItem)
  const pageTitle = showTitle(handledRoute, vm)
  const resTitle = pageTitle ? `${pageTitle} - ${title}` : title
  window.document.title = resTitle
}

/**
 * @description 校验手机号码
 */
export const ValidatePhone = (value) => {
  const reg = /^[1][2-9][0-9]{9}$/
  if (!reg.test(value)) {
    return false
  } else {
    return true
  }
}

// 校验密码
export const ValidatePass = (value) => {
  const regUpper = /[A-Z]/
  const regLower = /[a-z]/
  const regNum = /[0-9]/
  const regTeShu = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？+-]")
  let complex = 0
  if (regLower.test(value)) {
    ++complex
  }
  if (regUpper.test(value)) {
    ++complex
  }
  if (regNum.test(value)) {
    ++complex
  }
  if (regTeShu.test(value)) {
    ++complex
  }
  return complex
}

// 校验邮箱
export const validateEmail = (value) => {
  const reg = /^[A-Za-z0-9._%-]+@([A-Za-z0-9-]+\.)+[A-Za-z]{2,4}$/
  if (!reg.test(value)) {
    return false
  } else {
    return true
  }
}

// 身份证校验
export const idCardValid = (code) => {
  var city = { 11: '北京', 12: '天津', 13: '河北', 14: '山西', 15: '内蒙古', 21: '辽宁', 22: '吉林', 23: '黑龙江 ', 31: '上海', 32: '江苏', 33: '浙江', 34: '安徽', 35: '福建', 36: '江西', 37: '山东', 41: '河南', 42: '湖北 ', 43: '湖南', 44: '广东', 45: '广西', 46: '海南', 50: '重庆', 51: '四川', 52: '贵州', 53: '云南', 54: '西藏 ', 61: '陕西', 62: '甘肃', 63: '青海', 64: '宁夏', 65: '新疆', 71: '台湾', 81: '香港', 82: '澳门', 91: '国外 ' }
  // var tip = ''
  var pass = true

  if (!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)) {
    // tip = '身份证号格式错误'
    pass = false
  } else if (!city[code.substr(0, 2)]) {
    // tip = '地址编码错误'
    pass = false
  } else {
    // 18位身份证需要验证最后一位校验位
    if (code.length === 18) {
      code = code.split('')
      // ∑(ai×Wi)(mod 11)//加权因子
      var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
      // 校验位
      var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2]
      var sum = 0
      var ai = 0
      var wi = 0
      for (var i = 0; i < 17; i++) {
        ai = code[i]
        wi = factor[i]
        sum += ai * wi
      }
      var last = parity[sum % 11] + ''
      if (last !== code[17].toUpperCase()) {
        // tip = '校验位错误'
        pass = false
      }
    }
  }
  return pass
}

export const returnRules = (item, num) => {
  let rules = []
  let trigger = 'blur'
  let text = `请输入${item.columnCh}`
  if (item.columnType === 'radio' || item.columnType === 'select' || item.columnType === 'date') {
    trigger = 'change'
    text = `请选择${item.columnCh}`
  }
  let rule = { required: true, message: `${text}`, trigger: trigger }
  if (item.columnType === 'select-province') {
    trigger = 'change'
    text = `请选择${item.columnCh}市区`
    if (num && num === 2) {
      text = `请选择${item.columnCh}市`
    }
    rule = { required: true, type: 'array', message: `${text}`, trigger: trigger }
  }
  if (item.required) {
    rules.push(rule)
    if (item.verifyFormula) {
      let verifyFormula = new RegExp(item.verifyFormula)
      rules.push({
        validator: (rule, value, callback) => {
          if (!verifyFormula.test(value)) {
            const text = item.columnErrMsg
            return callback(new Error(text))
          } else {
            callback()
          }
        },
        trigger: trigger
      })
    }
    if (item.columnType === 'id-card') {
      rules.push({
        validator: ruleIdCardValid, trigger: trigger
      })
    }
  }
  return rules
}

export const ruleIdCardValid = (rule, value, callback) => {
  if (!idCardValid(value)) {
    return callback(new Error('请输入正确的身份证号'))
  } else {
    callback()
  }
}

//  成员管理，菜单数据转换
export const menuToTree = (data, renderTitle) => {
  let arr = []
  data.forEach((item, index) => {
    const obj = Object.assign({}, item)
    obj.children = []
    obj.title = item.deptName
    obj.expand = true
    obj.selected = false
    if (renderTitle) obj.render = renderTitle
    if (item.childs.length) {
      obj.children = menuToTree(item.childs)
    }
    arr.push(obj)
  })
  return arr
}

// 树形结构数据，返回某个子节点
export const returnItem = (arr, id) => {
  var arrRes = null

  let rev = (data, id) => {
    for (var i = 0, length = data.length; i < length; i++) {
      let node = data[i]
      if (node.id === id) {
        arrRes = { value: node.id, label: node.title }
        break
      } else {
        if (node.children) {
          rev(node.children, id)
        }
      }
    }
    return arrRes
  }

  arrRes = rev(arr, id)

  return arrRes
}

// 对象的深拷贝
export const deepCopy = function (obj, isNull) {
  const o = obj instanceof Array ? [] : {}
  for (let k in obj) {
    let val = obj[k]

    if (typeof val === 'object') {
      o[k] = deepCopy(val, isNull)
    } else {
      o[k] = isNull ? '' : val
    }
  }
  return o
}

// 对象的值赋值到目标对象
export const toObjValue = function (obj, targetObj) {
  let arr = ['', '', '']
  for (let k in obj) {
    if (obj[k] instanceof Array) {
      if (!targetObj.hasOwnProperty(k)) {
        targetObj[k] = []
      }
      let len = targetObj[k].length - 1
      obj[k].forEach((item, index) => {
        if (index <= len) {
          for (let l in item) {
            targetObj[k][index][l] = item[l]
          }
        } else {
          targetObj[k].push(deepCopy(targetObj[k][0], true))
          for (let l in item) {
            targetObj[k][index][l] = item[l]
          }
        }
      })
    } else {
      // 处理省市区为数组
      targetObj[k] = obj[k]
      if (k.indexOf('ProvinceCode') !== -1 || k.indexOf('BankProvince') !== -1) {
        arr[0] = obj[k]
        targetObj[k] = arr
      }
      if (k.indexOf('CityCode') !== -1 || k.indexOf('BankCity') !== -1) {
        arr[1] = obj[k]
        targetObj[k] = arr
      }
      if (k.indexOf('RegionCode') !== -1) {
        arr[2] = obj[k]
        targetObj[k] = arr
      }
    }
  }
  return targetObj
}
