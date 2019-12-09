export const getRecentlyMonth = (mDigital) => {
  // 创建现在的时间
  var date = new Date()
  // 获取年
  var year = date.getFullYear()
  // 获取月
  var mon = date.getMonth() + 1
  var arry = []
  for (var i = 0; i < mDigital; i++) {
    if (mon <= 0) {
      year = year - 1
      mon = mon + 12
    }
    if (mon < 10) {
      mon = '0' + mon
    }

    arry[i] = year + '-' + mon

    mon = mon - 1
  }

  return arry
}

// 获取指定时间近mDigital个月的时间（单位：月）
export const getDateMonth = (date, mDigital) => {
  // 获取年
  let year = date.getFullYear()
  // 获取月
  let mon = date.getMonth() + 1
  let arry = []
  for (let i = 0; i < mDigital; i++) {
    if (mon > 12) {
      year = year + 1
      mon = 1
    }
    if (mon < 10) {
      mon = '0' + mon
    }

    arry[i] = year + '-' + mon

    mon = parseInt(mon) + 1
  }

  return arry
}

export const monthsBetw = (date1, date2) => {
  // 获取年,月数
  let year1 = parseInt(date1.getFullYear())
  let month1 = parseInt(date1.getMonth())
  let year2 = parseInt(date2.getFullYear())
  let month2 = parseInt(date2.getMonth())
  // 通过年,月差计算月份差
  let months = (year2 - year1) * 12 + (month2 - month1) + 1
  return months
}

// 将时间戳格式化
export const getMyDate = (time) => {
  if (typeof (time) === 'undefined') {
    return ''
  }
  let oDate = new Date(time)
  let oYear = oDate.getFullYear()
  let oMonth = oDate.getMonth() + 1
  let oDay = oDate.getDate()
  let oHour = oDate.getHours()
  let oMin = oDate.getMinutes()
  let oSen = oDate.getSeconds()
  // 最后拼接时间
  oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay) + ' ' + getzf(oHour) + ':' + getzf(oMin) + ':' + getzf(oSen)

  return oTime
}

// 补0操作,当时间数据小于10的时候，给该数据前面加一个0
function getzf (num) {
  if (parseInt(num) < 10) {
    num = '0' + num
  }
  return num
}

// 获取当前时间，格式YYYY-MM-DD
export const formatDateDay = (d) => {
  let date = new Date(d)
  let seperator1 = '-'
  let year = date.getFullYear()
  let month = date.getMonth() + 1
  let strDate = date.getDate()
  if (month >= 1 && month <= 9) {
    month = '0' + month
  }
  if (strDate >= 0 && strDate <= 9) {
    strDate = '0' + strDate
  }
  let currentdate = year + seperator1 + month + seperator1 + strDate
  return currentdate
}

// 获取当前时间，格式YYYY-MM
export const formatDateMonth = (d) => {
  let date = new Date(d)
  let seperator1 = '-'
  let year = date.getFullYear()
  let month = date.getMonth() + 1
  if (month >= 1 && month <= 9) {
    month = '0' + month
  }
  let currentdate = year + seperator1 + month
  return currentdate
}

export default {
  getRecentlyMonth,
  getDateMonth,
  monthsBetw,
  getMyDate,
  formatDateDay,
  formatDateMonth
}
