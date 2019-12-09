import axios from '@/libs/api.request'

// business相关接口

// 查询正向流程列表
export const getBusinessFlowForward = (data) => {
  return axios.request({
    url: 'business/getBusinessFlowForward',
    data,
    method: 'post'
  })
}

// 查询业务类型列表
export const getBusinessTypeList = (data) => {
  return axios.request({
    url: 'business/getBusinessTypeList',
    data,
    method: 'post'
  })
}

// 查询全部流程扭转配置
export const getBusinessFlowExtendAll = (data) => {
  return axios.request({
    url: 'business/getBusinessFlowExtendAll',
    data,
    method: 'post'
  })
}

// 查询所选流程扭转节点是否已存在配置
export const workFlowExists = (data) => {
  return axios.request({
    url: 'business/workFlowExists',
    data,
    method: 'post'
  })
}

// 编辑业务流程扭转配置
export const editWorkFlow = (data) => {
  return axios.request({
    url: 'business/editWorkFlow',
    data,
    method: 'post'
  })
}

// 查询流程扭转配置信息
export const findFlowExtendSettingInfo = (data) => {
  return axios.request({
    url: 'business/findFlowExtendSettingInfo',
    data,
    method: 'post'
  })
}

// 查询流程扭转配置信息
export const savePower = (data) => {
  return axios.request({
    url: 'business/savePower',
    data,
    method: 'post'
  })
}
