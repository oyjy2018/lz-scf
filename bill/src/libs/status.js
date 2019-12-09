// 报价状态(1:待接受报价;2:接受报价;3:不接受报价;4:已撤回)
export const quotationStatusArr = [{
  value: 1,
  label: '待接受报价'
}, {
  value: 2,
  label: '接受报价'
}, {
  value: 3,
  label: '不接受报价'
}, {
  value: 4,
  label: '已撤回'
}]

// 询价状态(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
export const inquireStatusArr = [{
  value: 1,
  label: '待报价'
}, {
  value: 2,
  label: '竞价中'
}, {
  value: 3,
  label: '询价成功'
}, {
  value: 4,
  label: '询价已截止'
}, {
  value: 5,
  label: '询价已撤销'
}]

// 票据状态（0：未背书；1：背书待签收；2：背书已签收）
export const billStatusArr = [{
  value: 0,
  label: '未背书'
}, {
  value: 1,
  label: '背书待签收'
}, {
  value: 2,
  label: '背书已签收'
}]

// payStatus 支付状态（1：支付中；97：支付撤销中；98：支付失败；99：支付成功；）
export const payStatusArr = [{
  value: 1,
  label: '支付中'
}, {
  value: 97,
  label: '支付撤销中'
}, {
  value: 98,
  label: '支付失败'
}, {
  value: 99,
  label: '支付成功'
}]

/*
  订单状态（1：待买家签合同；2：待卖家签合同；3：双方已签合同-待支付；
  4：支付成功-待背书；5：已背书-待签收；6：已背书-已签收；99：交易成功；
  97：撤销中；98：已撤销）
*/
export const orderStatusArr = [{
  value: 1,
  label: '待买家签合同'
}, {
  value: 2,
  label: '待卖家签合同'
}, {
  value: 3,
  label: '双方已签合同-待支付'
}, {
  value: 4,
  label: '支付成功-待背书'
}, {
  value: 5,
  label: '已背书-待签收'
}, {
  value: 6,
  label: '已背书-已签收'
}, {
  value: 99,
  label: '交易成功'
}, {
  value: 97,
  label: '撤销中'
}, {
  value: 98,
  label: '已撤销'
}]

export const flawsArray = [{
  value: '无瑕疵',
  label: '无瑕疵',
  tip: '无瑕疵'
}, {
  value: '不可转让',
  label: '不可转让',
  tip: '票据不能再继续背书转让权利'
}, {
  value: '同名',
  label: '同名',
  tip: '所有背书人中出现持票人的银行户名相同账号不同的'
}, {
  value: '回头',
  label: '回头',
  tip: '持票人在所背书人中出现两次或两次以上的'
}, {
  value: '上下不一致',
  label: '上下不一致',
  tip: '承兑人与开票人不一致'
}, {
  value: '保理',
  label: '保理',
  tip: '所有背书人中出现保理公司的'
}, {
  value: '质押',
  label: '质押',
  tip: '指以设定质权、供给债款担保为意图而进行的背书'
}, {
  value: '担保',
  label: '担保',
  tip: '所有背书人中出现融资担保公司的'
}, {
  value: '租赁',
  label: '租赁',
  tip: '所有背书人中出现租赁公司的'
}, {
  value: '小贷',
  label: '小贷',
  tip: '所有背书人中出现小贷公司的'
}, {
  value: '供应链',
  label: '供应链',
  tip: '所有背书人中出现供应链公司的'
}, {
  value: '投资',
  label: '投资',
  tip: '所有背书人中出现投资公司的'
}, {
  value: '金融',
  label: '金融',
  tip: '所有背书人中出现金融公司的'
}]
