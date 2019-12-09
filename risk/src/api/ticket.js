import axios from '@/libs/api.request'

// 查询开票申请流程相关信息
export const findCreditTicketCfg = (data) => {
  return axios.request({
    url: 'creditTicketApply/findCreditTicketCfg',
    data,
    method: 'post'
  })
}

// 查询开票申请流程相关信息
export const saveDraftTicket = (data) => {
  return axios.request({
    url: 'creditTicketApply/saveDraft',
    data,
    method: 'post'
  })
}

// 开票申请提交
export const applyCommitTicket = (data) => {
  return axios.request({
    url: 'creditTicketApply/applyCommit',
    data,
    method: 'post'
  })
}

// 所有用信申请
export const findCreditUseApplyAllList = (data) => {
  return axios.request({
    url: 'creditUse/findCreditUseApplyAllList',
    data,
    method: 'post'
  })
}

// 我的用信申请
export const findCreditUseApplyMyList = (data) => {
  return axios.request({
    url: 'creditUse/findCreditUseApplyMyList',
    data,
    method: 'post'
  })
}

// 用信审批列表
export const findCreditUseAuditList = (data) => {
  return axios.request({
    url: 'creditUse/findCreditUseAuditList',
    data,
    method: 'post'
  })
}

// 录入用信记录
export const addCreditUseRecord = (data) => {
  return axios.request({
    url: 'creditUse/addCreditUseRecord',
    data,
    method: 'post'
  })
}

// 商票正面ocr识别
export const ocrTicket = (data) => {
  return axios.request({
    url: 'creditUse/ocrTicket',
    data,
    method: 'post'
  })
}

// 查看用信申请详情
export const findCreditUseApplyDetails = (data) => {
  return axios.request({
    url: 'creditUse/findCreditUseApplyDetails',
    data,
    method: 'post'
  })
}

// 我的用信列表
export const findCreditUseMyList = (data) => {
  return axios.request({
    url: 'creditUse/myList',
    data,
    method: 'post'
  })
}

// 所有用信列表
export const findCreditUseAllList = (data) => {
  return axios.request({
    url: 'creditUse/allList',
    data,
    method: 'post'
  })
}

// 修改还款状态
export const updateRefundStatus = (data) => {
  return axios.request({
    url: 'creditUse/updateRefundStatus',
    data,
    method: 'post'
  })
}

// 用信详情（适用于开商票）
export const findCreditUseDetail = (data) => {
  return axios.request({
    url: 'creditUse/detail',
    data,
    method: 'post'
  })
}

// 商票列表
export const findCreditList = (data) => {
  return axios.request({
    url: 'creditUse/ticketList',
    data,
    method: 'post'
  })
}

// 商票详情
export const ticketDetail = (data) => {
  return axios.request({
    url: 'creditUse/ticketDetail',
    data,
    method: 'post'
  })
}

// 开票申请存文件
export const ticketUsesaveFile = (data) => {
  return axios.request({
    url: 'creditTicketApply/saveFile',
    data,
    method: 'post'
  })
}

// 删除文件
export const deleteFileById = (data) => {
  return axios.request({
    url: '/file/deleteById',
    data,
    method: 'post'
  })
}

// 开商票审核提交
export const auditTicketCommit = (data) => {
  return axios.request({
    url: 'creditTicketApply/auditCommit',
    data,
    method: 'post'
  })
}

// 用信申请详情-审核日志列表
export const getTicketAuditLogList = (data) => {
  return axios.request({
    url: 'creditUse/auditLog',
    data,
    method: 'post'
  })
}

// 用信申请详情-操作日志列表
export const getTicketOperateLogList = (data) => {
  return axios.request({
    url: 'creditUse/operateLog',
    data,
    method: 'post'
  })
}

// 用信申请详情-文件资料
export const getTicketDocument = (data) => {
  return axios.request({
    url: 'creditUse/document',
    data,
    method: 'post'
  })
}

// 录入用信（审批完成列表）
export const getAuditFinishList = (data) => {
  return axios.request({
    url: 'creditTicketApply/getAuditFinishList',
    data,
    method: 'post'
  })
}

// 查询授信可用额度
export const getCreditUseAbleMoney = (data) => {
  return axios.request({
    url: '/creditRecord/findBalance',
    data,
    method: 'post'
  })
}

// 修改商票状态
export const updateTicketStatus = (data) => {
  return axios.request({
    url: '/creditUse/updateTicketStatus',
    data,
    method: 'post'
  })
}
