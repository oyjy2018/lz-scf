import axios from '@/libs/api.request'

// 收款列表
export const gatheringList = (data) => {
  return axios.request({
    url: 'lz/project/gatheringList',
    params: data,
    method: 'get'
  })
}

// 深华项目列表
export const lzProjectList = (data) => {
  return axios.request({
    url: '/lz/project/list',
    data,
    method: 'post'
  })
}

// 深华项目基本信息
export const lzProjectBasicInfo = (data) => {
  return axios.request({
    url: '/lz/project/basicInfo',
    params: data,
    method: 'get'
  })
}

// 深华项目信息
export const lzProjectItemInfo = (data) => {
  return axios.request({
    url: '/lz/project/itemInfo',
    params: data,
    method: 'get'
  })
}

// 深华合同信息
export const lzProjectContractInfo = (data) => {
  return axios.request({
    url: '/lz/project/contract',
    params: data,
    method: 'get'
  })
}

// 深华付款信息
export const lzProjectPayList = (data) => {
  return axios.request({
    url: '/lz/project/payList',
    params: data,
    method: 'get'
  })
}

// 查询项目成本费用明细（经营利润分析B）
export const findCostDetails = (data) => {
  return axios.request({
    url: '/lz/project/findCostDetails',
    params: data,
    method: 'get'
  })
}

// 查询项目采购物流信息
export const getPurchaseLogistics = (data) => {
  return axios.request({
    url: '/lz/project/purchaseLogistics',
    params: data,
    method: 'get'
  })
}

// 查询项目资产数据
export const findProjectAsset = (data) => {
  return axios.request({
    url: '/lz/project/findProjectAsset',
    data,
    method: 'post'
  })
}

// 查询项目借款信息
export const getLoanList = (data) => {
  return axios.request({
    url: '/lz/project/loanList',
    params: data,
    method: 'get'
  })
}

// 查询项目还款信息
export const getRefundList = (data) => {
  return axios.request({
    url: '/lz/project/refundList',
    params: data,
    method: 'get'
  })
}

// 查询项目文件
export const getFileList = (data) => {
  return axios.request({
    url: '/lz/project/fileList',
    params: data,
    method: 'get'
  })
}

// 查询项目负债、利润
export const findLiabilities = (data) => {
  return axios.request({
    url: '/lz/project/findLiabilities',
    data,
    method: 'post'
  })
}

// 项目看板卡片数据
export const getBoardCard = (data) => {
  return axios.request({
    url: '/lz/project/board/card',
    params: data,
    method: 'get'
  })
}

// 项目看板折线图数据
export const getBoardReport = (data) => {
  return axios.request({
    url: '/lz/project/board/report',
    data,
    method: 'post'
  })
}

// 查询押金缴纳记录
export const getPledgeCashPayList = (data) => {
  return axios.request({
    url: '/lz/project/getPledgeCashPayList',
    params: data,
    method: 'get'
  })
}

// 查询押金缴纳记录
export const getPledgeCashExtractList = (data) => {
  return axios.request({
    url: '/lz/project/getPledgeCashExtractList',
    params: data,
    method: 'get'
  })
}

// 保险信息
export const getInsuranceList = (data) => {
  return axios.request({
    url: '/lz/project/insuranceList',
    params: data,
    method: 'get'
  })
}

// 发票信息
export const getInvoiceList = (data) => {
  return axios.request({
    url: '/lz/project/invoiceList',
    params: data,
    method: 'get'
  })
}

// 保证金记录
export const findMarginsList = (data) => {
  return axios.request({
    url: '/lz/project/findMarginsList',
    params: data,
    method: 'get'
  })
}
