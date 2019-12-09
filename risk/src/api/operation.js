import axios from '@/libs/api.request'

// 所有商票订单
export const getAllOrderList = (data) => {
  return axios.request({
    url: 'ticketOrder/getAllOrderList',
    data,
    method: 'post'
  })
}

// 重新发起商票状态轮询
export const reStartTicketStatusQuery = (data) => {
  return axios.request({
    url: 'ticketOrder/reStartTicketStatusQuery',
    data,
    method: 'post'
  })
}

// 通用-订单详情
export const findOrderDetails = data => {
  return axios.request({
    url: 'ticketOrder/findOrderDetails',
    data,
    method: 'post'
  })
}

// 询价单详情
export const getInquireDetail = data => {
  return axios.request({
    url: 'ticketOrder/inquireDetail',
    data,
    method: 'post'
  })
}

// 报价单详情
export const getQuotationDetail = ({ quotationId }) => {
  return axios.request({
    url: `ticketOrder/quotationDetail/${quotationId}`,
    method: 'get'
  })
}
