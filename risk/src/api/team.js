import axios from '@/libs/api.request'

// 公司部门树
export const getCompanyDepTree = () => {
  return axios.request({
    url: 'dept/find/tree',
    method: 'post'
  })
}

// 公司成员列表
export const getUserList = (data) => {
  return axios.request({
    url: 'user/find/list',
    data,
    method: 'post'
  })
}

export const addDept = (data) => {
  return axios.request({
    url: 'dept/add',
    data,
    method: 'post'
  })
}

export const editDept = (data) => {
  return axios.request({
    url: 'dept/edit',
    data,
    method: 'post'
  })
}

export const deleteDept = ({ id }) => {
  const data = { id }
  return axios.request({
    url: 'dept/delete',
    data,
    method: 'post'
  })
}

export const getDeptUserTree = ({ companyId }) => {
  const data = { companyId }
  return axios.request({
    url: `dept/user?companyId=${companyId}`,
    data,
    method: 'get'
  })
}

// 添加用户（邀请多个）
export const addUserList = (data) => {
  return axios.request({
    url: 'user/add/list',
    data,
    method: 'post'
  })
}

export const getUserEidtInfo = (data) => {
  return axios.request({
    url: 'user/edit/find/info',
    data,
    method: 'post'
  })
}

export const editUserInfo = (data) => {
  return axios.request({
    url: 'user/edit/info/save',
    data,
    method: 'post'
  })
}

export const deleteUserInfo = (data) => {
  return axios.request({
    url: 'user/delete',
    data,
    method: 'post'
  })
}

export const editUserStatus = (data) => {
  return axios.request({
    url: 'user/edit/status',
    data,
    method: 'post'
  })
}
