import axios from '@/libs/api.request'

// 角色管理
export const getRoleGroupList = () => {
  return axios.request({
    url: 'company/role',
    method: 'get'
  })
}

export const getUserRoleList = (data) => {
  const roleId = data.id
  return axios.request({
    url: `/role/${roleId}/user`,
    data,
    method: 'get'
  })
}

export const addRole = (data) => {
  const companyId = data.companyId
  return axios.request({
    url: `company/${companyId}/role`,
    data,
    method: 'post'
  })
}

export const editRole = (data) => {
  return axios.request({
    url: 'role/edit',
    data,
    method: 'post'
  })
}

export const deleteRole = ({ id }) => {
  const roleId = id
  return axios.request({
    url: `/role/${roleId}`,
    method: 'delete'
  })
}

// 获取用户有权限的角色
export const getRoleCompanyUser = ({ companyId }) => {
  const data = { companyId }
  return axios.request({
    url: 'role/find/company/user',
    data,
    method: 'post'
  })
}

export const getSystemFunctionVersionList = ({ systemId, companyId }) => {
  return axios.request({
    url: `system/${systemId}/company/${companyId}/function`,
    method: 'get'
  })
}

// 获取角色权限数据
export const getRolePrivilege = ({ id }) => {
  const data = { id }
  return axios.request({
    url: 'role/find/privilege',
    data,
    method: 'post'
  })
}

export const editRolePrivilege = (data) => {
  return axios.request({
    url: 'role/edit/privilege',
    data,
    method: 'post'
  })
}

export const addRoleUser = (data) => {
  const roleId = data.roleId
  return axios.request({
    url: `/role/${roleId}/user`,
    data,
    method: 'post'
  })
}

export const deleteRoleUser = ({ roleUserId }) => {
  return axios.request({
    url: `/role/user/${roleUserId}`,
    method: 'delete'
  })
}

export const findCompanyRoleList = (data) => {
  return axios.request({
    url: `/role/findCompanyRoleList`,
    data,
    method: 'post'
  })
}
