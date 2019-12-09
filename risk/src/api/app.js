import axios from '@/libs/api.request'

// 行业分类
export const getNatureSelectData = ({ key }) => {
  const data = { key }
  return axios.request({
    url: 'api/nature/select',
    data,
    method: 'post'
  })
}

// 公司规模
export const getStaffsizeSelectData = () => {
  return axios.request({
    url: 'api/staffsize/select',
    method: 'post'
  })
}

// 系统版本
export const getSystemVersionList = () => {
  return axios.request({
    url: 'api/getSystemVersionList',
    method: 'post'
  })
}

// 银行
export const getBankList = () => {
  return axios.request({
    url: 'api/bank/select/list',
    method: 'post'
  })
}

// 金融产品
export const getProductList = () => {
  return axios.request({
    url: 'api/product/select/list',
    method: 'post'
  })
}

export const getTableData = () => {
  return axios.request({
    url: 'get_table_data',
    method: 'get'
  })
}

export const getDragList = () => {
  return axios.request({
    url: 'get_drag_list',
    method: 'get'
  })
}

export const errorReq = () => {
  return axios.request({
    url: 'error_url',
    method: 'post'
  })
}

export const saveErrorLogger = info => {
  return axios.request({
    url: 'save_error_logger',
    data: info,
    method: 'post'
  })
}

export const uploadImg = formData => {
  return axios.request({
    url: 'image/upload',
    data: formData
  })
}

export const getOrgData = () => {
  return axios.request({
    url: 'get_org_data',
    method: 'get'
  })
}

export const getTreeSelectData = () => {
  return axios.request({
    url: 'get_tree_select_data',
    method: 'get'
  })
}
