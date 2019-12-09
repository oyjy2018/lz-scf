import axios from '@/libs/api.request'

// 模型列表
export const modelList = (data) => {
  return axios.request({
    url: 'risk/model/list',
    data,
    method: 'post'
  })
}

// 启用禁用模型
export const updateModelHasEnabled = (data) => {
  return axios.request({
    url: 'risk/model/updateHasEnabled',
    data,
    method: 'post'
  })
}

// 删除模型
export const deleteModel = (data) => {
  return axios.request({
    url: 'risk/model/delete',
    data,
    method: 'post'
  })
}
// 添加模型
export const insertModel = (data) => {
  return axios.request({
    url: 'risk/model/insert',
    data,
    method: 'post'
  })
}
// 添加评分项列表
export const addScoreItem = (data) => {
  return axios.request({
    url: '/scoreItem',
    data,
    method: 'post'
  })
}
// 获取评分变量
export const scoreVariable = (modelId) => {
  return axios.request({
    url: '/scoreItem/variable',
    params: { 'modelId': modelId },
    method: 'get'
  })
}
// 模型详情-获取评分项列表
export const modelDetailItem = (modelId) => {
  return axios.request({
    url: '/modelDetail/scoreItem',
    params: { 'modelId': modelId },
    method: 'get'
  })
}
// 获取模型列表
export const modelItem = (modelId) => {
  return axios.request({
    url: '/scoreItem',
    params: { 'modelId': modelId },
    method: 'get'
  })
}
// 评分项详情
export const scoreDetail = (data) => {
  return axios.request({
    url: `/scoreItem/${data}`,
    data,
    method: 'get'
  })
}
// 修改评分项详情
export const editScore = (data) => {
  return axios.request({
    url: '/scoreItem',
    data,
    method: 'put'
  })
}
// 删除评分项
export const removeScore = (data) => {
  return axios.request({
    url: `/scoreItem/${data}`,
    data,
    method: 'delete'
  })
}
// 获取模型公式
export const getModelFormula = (data) => {
  return axios.request({
    url: '/risk/model/getModelFormula',
    data,
    method: 'post'
  })
}
// 获取模型详情
export const getModelDetail = (data) => {
  return axios.request({
    url: '/risk/model/detail',
    data,
    method: 'post'
  })
}
// 修改模型详情
export const editModel = (data) => {
  return axios.request({
    url: '/risk/model/update',
    data,
    method: 'post'
  })
}
// 修改模型详情
export const updateModelFormula = (data) => {
  return axios.request({
    url: '/risk/model/updateModelFormula',
    data,
    method: 'post'
  })
}
// 修改评分项满分值
export const updatafullMark = ({ riskScoreItemId, fullMark }) => {
  const data = { fullMark }
  return axios.request({
    url: `/scoreItem/${riskScoreItemId}`,
    data,
    method: 'put'
  })
}

// 获取公司标准业务名称和id
export const getStandardBusinessTypeList = (data) => {
  return axios.request({
    url: `/risk/model/getBusinessTypeList`,
    data,
    method: 'Post'
  })
}
