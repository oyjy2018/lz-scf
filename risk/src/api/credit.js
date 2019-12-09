import axios from '@/libs/api.request'

// apply

// 所有申请
export const allApplyList = (data) => {
  return axios.request({
    url: 'creditApply/allApplyList',
    data,
    method: 'post'
  })
}

// 我的申请
export const myApplyList = (data) => {
  return axios.request({
    url: 'creditApply/myApplyList',
    data,
    method: 'post'
  })
}

// 申请详情
export const getDetail = (data) => {
  return axios.request({
    url: 'creditApply/getDetail',
    data,
    method: 'post'
  })
}

// 授信审批列表
export const auditList = (data) => {
  return axios.request({
    url: 'creditApply/auditList',
    data,
    method: 'post'
  })
}

// 所有授信列表
export const allAuditList = (data) => {
  return axios.request({
    url: 'creditRecord/allList',
    data,
    method: 'post'
  })
}

// 我的授信列表
export const myAuditList = (data) => {
  return axios.request({
    url: 'creditRecord/myList',
    data,
    method: 'post'
  })
}

// 查询公司指定业务表单配置信息
export const findBusinessAttrCfg = (data) => {
  return axios.request({
    url: 'business/findBusinessAttrCfg',
    data,
    method: 'post'
  })
}

// 授信申请保存草稿（每次保存单个字段）
export const saveDraft = (data) => {
  return axios.request({
    url: 'creditApply/saveDraft',
    data,
    method: 'post'
  })
}

// 申请授信提交
export const commitApply = (data) => {
  return axios.request({
    url: 'creditApply/commit',
    data,
    method: 'post'
  })
}

// 审核页面信息查询（授信详情、审核配置）
export const getTrailingWorkFlowCfg = (data) => {
  return axios.request({
    url: 'creditApply/getTrailingWorkFlowCfg',
    data,
    method: 'post'
  })
}

// 授信审核提交
export const fileCommit = (data) => {
  return axios.request({
    url: 'creditApply/fileCommit',
    data,
    method: 'post'
  })
}

// 授信审核提交
export const auditCommit = (data) => {
  return axios.request({
    url: 'creditApply/auditCommit',
    data,
    method: 'post'
  })
}

// 评论列表
export const getCommentList = (data) => {
  return axios.request({
    url: 'comment/findList',
    data,
    method: 'post'
  })
}

// 审核日志列表
export const findAuditLogList = (data) => {
  return axios.request({
    url: 'creditApply/findAuditLogList',
    data,
    method: 'post'
  })
}

// 操作日志列表
export const findOperateLogList = (data) => {
  return axios.request({
    url: 'creditApply/findOperateLogList',
    data,
    method: 'post'
  })
}

// 获取业务的流程状态集合
export const getFlowCodeList = (data) => {
  return axios.request({
    url: 'business/getFlowCodeList',
    data,
    method: 'post'
  })
}

// 获取业务的流程状态集合
export const deleteCreditItem = (data) => {
  return axios.request({
    url: 'creditApply/deleteCreditItem',
    data,
    method: 'post'
  })
}

// 申请授信详情-文件资料
export const getCreditDocument = (data) => {
  return axios.request({
    url: 'creditApply/document',
    data,
    method: 'post'
  })
}

// 导入授信
export const importRecord = (data) => {
  return axios.request({
    url: 'creditRecord/importRecord',
    data,
    method: 'post'
  })
}

// 删除授信
export const deleteRecord = (data) => {
  return axios.request({
    url: 'creditRecord/deleteRecord',
    data,
    method: 'post'
  })
}

// 查询授信申请详情（最新）
export const getCreditApplyDetails = (data) => {
  return axios.request({
    url: 'creditApply/getDetails',
    data,
    method: 'post'
  })
}

// 查询审核过程数据
export const findCreditApprovalCourse = (data) => {
  return axios.request({
    url: 'creditApply/findApprovalCourse',
    data,
    method: 'post'
  })
}
