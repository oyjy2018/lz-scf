import axios from '@/libs/api.request'

// 首页
export const getIndex = () => {
  return axios.request({
    url: 'index',
    method: 'get'
  })
}

// 获取公司京东实名属性
export const getCompanyJdProperty = data => {
  return axios.request({
    url: 'businessTicket/getCompanyJdProperty',
    data,
    method: 'get'
  })
}

// 发布列表
export const getInquireList = data => {
  return axios.request({
    url: 'businessTicket/inquireList',
    data,
    method: 'post'
  })
}

// 我的发布列表
export const getMyInquireList = data => {
  return axios.request({
    url: 'businessTicket/myInquireList',
    data,
    method: 'post'
  })
}

// 询价详情
export const getInquireDetail = data => {
  return axios.request({
    url: 'businessTicket/inquireDetail',
    data,
    method: 'post'
  })
}

// 撤销发布
export const cancelInquire = data => {
  return axios.request({
    url: 'businessTicket/cancelInquire',
    data,
    method: 'post'
  })
}

// 发布票据
export const inquireBusinessTicket = data => {
  return axios.request({
    url: 'businessTicket/inquire',
    data,
    method: 'post'
  })
}

// ocr识别票据
export const ocrRecognize = data => {
  return axios.request({
    url: 'businessTicket/ocrRecognize',
    data,
    method: 'post'
  })
}

// 报价
export const quoteTicket = data => {
  return axios.request({
    url: 'businessTicket/quote',
    data,
    method: 'post'
  })
}

// 票方-报价单列表
export const getTicketQuotationList = data => {
  return axios.request({
    url: 'quotation/ticket',
    data,
    method: 'post'
  })
}

// 票方-接受报价
export const acceptedQuotation = data => {
  return axios.request({
    url: 'businessTicket/acceptedQuotation',
    data,
    method: 'post'
  })
}

// 票方-拒绝报价
export const rejectQuotation = data => {
  return axios.request({
    url: 'businessTicket/rejectQuotation',
    data,
    method: 'post'
  })
}

// 票方-我的订单 资方-我的订单 userType 用户类型（1：票方；2：资方
export const findOrderList = data => {
  return axios.request({
    url: 'businessTicket/findOrderList',
    data,
    method: 'post'
  })
}

// 通用-订单详情
export const findOrderDetails = data => {
  return axios.request({
    url: 'businessTicket/findOrderDetails',
    data,
    method: 'post'
  })
}

// buy资产
// 资产-我的报价列表
export const getMyQuotationList = data => {
  return axios.request({
    url: 'quotation/owner',
    data,
    method: 'post'
  })
}

// 报价单详情
export const getQuotationDetail = ({ quotationId }) => {
  return axios.request({
    url: `quotation/${quotationId}/detail`,
    method: 'get'
  })
}

// 资产-撤销报价
export const cancelQuotation = ({ quotationId }) => {
  return axios.request({
    url: `/quotation/owner/${quotationId}`,
    method: 'delete'
  })
}

// 获取合同内容
export const getContractContent = data => {
  return axios.request({
    url: 'sign/getContractContent',
    data,
    method: 'post'
  })
}

// 发短信验证码
export const sendCode = data => {
  return axios.request({
    url: 'sign/sendCode',
    data,
    method: 'post'
  })
}

// 签署合同
export const signContract = data => {
  return axios.request({
    url: 'sign/signContract',
    data,
    method: 'post'
  })
}

// 发起订单支付
export const payOrder = data => {
  return axios.request({
    url: 'businessTicket/payOrder',
    data,
    method: 'post'
  })
}

// 撤销订单
export const revokeOrder = data => {
  return axios.request({
    url: 'businessTicket/revokeOrder',
    data,
    method: 'post'
  })
}

// 更换合同
export const changeContract = data => {
  return axios.request({
    url: 'sign/changeContract',
    data,
    method: 'post'
  })
}

// 更换合同
export const isOnlyKeyWord = data => {
  return axios.request({
    url: 'sign/isOnlyKeyWord',
    data,
    method: 'post'
  })
}

// 获取默认合同预览地址
export const getInitContractViewUrl = data => {
  return axios.request({
    url: 'sign/getInitContractViewUrl',
    data,
    method: 'post'
  })
}

// 获取可报价公司列表
export const quotationCompanyList = data => {
  return axios.request({
    url: 'businessTicket/quotationCompanyList',
    data,
    method: 'post'
  })
}

// 给我的询价
export const myAssignInquireList = data => {
  return axios.request({
    url: 'businessTicket/myAssignInquireList',
    data,
    method: 'post'
  })
}

// 承兑方列表
export const acceptorList = data => {
  return axios.request({
    url: 'acceptor/list',
    data,
    method: 'post'
  })
}

// 删除承兑方
export const deleteAcceptor = data => {
  return axios.request({
    url: 'acceptor/delete',
    data,
    method: 'post'
  })
}

// 修改承兑方
export const updateAcceptor = data => {
  return axios.request({
    url: 'acceptor/update',
    data,
    method: 'post'
  })
}
// 新增承兑方
export const addAcceptor = data => {
  return axios.request({
    url: 'acceptor/add',
    data,
    method: 'post'
  })
}
// 承兑方详情
export const acceptorDetail = data => {
  return axios.request({
    url: 'acceptor/detail',
    data,
    method: 'post'
  })
}

// 修改承兑方限制
export const updateAcceptorLimit = data => {
  return axios.request({
    url: 'acceptor/updateLimit',
    data,
    method: 'post'
  })
}

// 查看是否限制
export const isLimit = data => {
  return axios.request({
    url: 'acceptor/isLimit',
    data,
    method: 'post'
  })
}

// 所有询价
export const allInquire = data => {
  return axios.request({
    url: 'operation/allInquire',
    data,
    method: 'post'
  })
}

// 所有订单
export const allOrder = data => {
  return axios.request({
    url: 'operation/allOrder',
    data,
    method: 'post'
  })
}

// 重新发起商票状态轮询
export const reStartTicketStatusQuery = data => {
  return axios.request({
    url: 'operation/reStartTicketStatusQuery',
    data,
    method: 'post'
  })
}

// 所有报价
export const allQuotation = data => {
  return axios.request({
    url: 'quotation/ticket/all',
    params: data,
    method: 'get'
  })
}

// 所有报价
export const companyBanks = data => {
  return axios.request({
    url: 'company/bank',
    params: data,
    method: 'get'
  })
}

// 保存服务费
export const platformServiceRateSave = data => {
  return axios.request({
    url: 'operation/platformServiceRateSave',
    data,
    method: 'post'
  })
}

// 查询服务费
export const platformServiceRateQuery = data => {
  return axios.request({
    url: 'operation/platformServiceRateQuery',
    params: data,
    method: 'post'
  })
}

// 查询banner列表
export const findBannerList = data => {
  return axios.request({
    url: 'banner/list',
    data,
    method: 'post'
  })
}

// 新增banner
export const addBanner = data => {
  return axios.request({
    url: 'banner/add',
    data,
    method: 'post'
  })
}

// 删除banner
export const deleteBanner = data => {
  return axios.request({
    url: 'banner/delete',
    data,
    method: 'post'
  })
}

// 修改banner启用状态
export const updateBannerUseStatus = data => {
  return axios.request({
    url: 'banner/updateUseStatus',
    data,
    method: 'post'
  })
}

// 修改banner排序
export const bannerSort = data => {
  return axios.request({
    url: 'banner/sort',
    data,
    method: 'post'
  })
}

// banner详情
export const bannerDetail = data => {
  return axios.request({
    url: 'banner/detail',
    params: data,
    method: 'get'
  })
}

// 修改banner
export const updateBanner = data => {
  return axios.request({
    url: 'banner/update',
    data,
    method: 'post'
  })
}

// 获取首页banner轮播
export const getBannerCarousel = data => {
  return axios.request({
    url: 'banner/getCarousel',
    params: data,
    method: 'get'
  })
}
