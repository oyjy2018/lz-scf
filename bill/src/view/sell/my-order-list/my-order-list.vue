<style lang="less" scoped>
.ivu-dropdown-item:hover {
  background-color: #ffffff;
}
</style>

<template>
  <div class="list-table">
    <Card :bordered="false">
      <div class="search-con-top">
        <Input clearable
               placeholder="输入买方（资方）"
               class="search-input"
               v-model="form.quotationCompanyName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入订单ID"
               class="search-input"
               v-model="form.orderId"
               @on-blur="(event) => form.orderId = isNaN(parseInt(event.target.value)) ? '' : parseInt(event.target.value) + ''"
               style="width: 200px" />
        <Select clearable
                v-model="form.orderStatus"
                placeholder="选择订单状态"
                style="width:200px">
          <Option v-for="item in orderStatusArr"
                  :value="item.value"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
        <Select clearable
                v-model="form.billStatus"
                placeholder="选择商票状态"
                style="width:200px">
          <Option v-for="item in billStatusArr"
                  :value="item.value"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
        <Select clearable
                v-model="form.payStatus"
                placeholder="选择支付状态"
                style="width:200px">
          <Option v-for="item in payStatusArr"
                  :value="item.value"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
      </div>
      <div class="search-con-top"
           style="position:relative;">
        成交时间 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择成交时间"
                    @on-change="(value) =>form.dealStartDate = value"
                    format="yyyy-MM-dd HH:mm:ss"
                    v-model="form.dealStartDate"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择成交时间"
                    format="yyyy-MM-dd HH:mm:ss"
                    @on-change="(value) => form.dealEndDate = value"
                    v-model="form.dealEndDate"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="handleSubimt"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
        <span class="orderProcess"
              style="position:absolute; right:0;color:#2C8EF3;cursor: pointer;"
              @click="handleViewOrderProcess()"
              icon="">订单操作流程</span>
      </div>
      <Table ref="selection"
             :columns="columns"
             :data="list"
             :highlight-row="true">
        <template slot-scope="{ row, index }"
                  slot="icon">
          <div @mouseenter="show(index)">
            <Dropdown placement="right">
              <a href="javascript:void(0)">
                <Icon v-show="index === activeIndex"
                      size="20"
                      type="ios-settings"
                      class="table-icon-setting"></Icon>
              </a>
              <DropdownMenu slot="list">
                <DropdownItem v-if="row.orderStatus === 2 && !row.hasUserSign">
                  <Button @click="handleSignContract(row, 1, row.contractFileUrlView)"
                          type="text">授权人签署合同</Button>
                </DropdownItem>
                <DropdownItem v-if="row.orderStatus === 2 && !row.hasCompanySign">
                  <Button @click="handleSignContract(row, 0, row.contractFileUrlView)"
                          type="text">企业签署合同</Button>
                </DropdownItem>
                <DropdownItem v-if="row.orderStatus !== 1 && row.orderStatus !== 97 && row.orderStatus !== 98">
                  <Button @click="handleViewContractFile(row)"
                          type="text">查看合同</Button>
                </DropdownItem>
                <DropdownItem v-if="row.orderStatus === 1 || row.orderStatus === 2 || row.orderStatus === 3 || row.orderStatus === 4 || row.orderStatus === 5">
                  <Button @click="handleRevoke(row)"
                          type="text">撤单</Button>
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </template>
      </Table>
      <div class="pagination">
        <Page :total="total"
              :current="form.current"
              :page-size="form.size"
              @on-change="changePage"
              show-elevator
              show-total></Page>
      </div>
    </Card>
    <ticketTransferAgreement ref="agreement"
                             :modal="showModal"
                             party="a"
                             @on-click="handleClose" />
    <Modal v-model="visible"
           width="800px"
           :footer-hide="true"
           :title="title">
      <iframe :src="viewUrl"
              frameborder="0"
              width="100%"
              height="600px"></iframe>
    </Modal>
    <Modal v-model="orderProcessVisible"
           width="800px"
           :footer-hide="true"
           title="订单流程">
      <iframe :src="orderProcessUrl"
              frameborder="0"
              width="100%"
              height="200px"
              style="padding:67px 0 0 27px;"></iframe>
    </Modal>
  </div>
</template>

<script>
import { absMoney } from '@/libs/tools'
import { mapState, mapActions } from 'vuex'
import { orderStatusArr, payStatusArr, billStatusArr } from '@/libs/status'
import ticketTransferAgreement from '../../ticket-transfer-agreement/ticket-transfer-agreement'
import { setTimeout } from 'timers'

export default {
  name: 'myorderlist',
  components: {
    ticketTransferAgreement
  },
  data () {
    return {
      visible: false,
      viewUrl: '',
      orderProcessVisible: false,
      orderProcessUrl: require('@/assets/images/orderlst.png'),
      title: '',
      showModal: false,
      orderStatusArr: orderStatusArr,
      payStatusArr: payStatusArr,
      billStatusArr: billStatusArr,
      form: {
        current: 1,
        size: 10,
        userType: 1, // 用户类型（1：票方；2：资方）
        orderId: '',
        orderStatus: '',
        billStatus: '',
        payStatus: '',
        dealStartDate: '',
        dealEndDate: '',
        quotationCompanyName: ''
      },
      activeIndex: -1,
      total: 0,
      list: [],
      columns: [
        {
          title: '',
          width: 60,
          align: 'center',
          slot: 'icon'
        },
        {
          title: '订单ID',
          key: 'id',
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.handleView(params.row)
                }
              }
            }, [
              h('span', params.row.id)
            ])
          }
        },
        {
          title: '订单状态',
          key: 'orderStatusCH',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.orderStatusCH)
            ])
          }
        },
        {
          title: '商票状态',
          key: 'billStatusCH',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.billStatusCH)
            ])
          }
        },
        {
          title: '支付状态',
          key: 'payStatusCH',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.payStatusCH)
            ])
          }
        },
        {
          title: '资方（买方）',
          key: 'quotationCompanyName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.quotationCompanyName)
            ])
          }
        },

        {
          title: '承兑人',
          key: 'accepterName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.accepterName)
            ])
          }
        },
        {
          title: '票面金额（元）',
          key: 'billAmt',
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.billAmt))
          }
        },
        {
          title: '到期日',
          key: 'maturityDate',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.maturityDate)
            ])
          }
        },
        {
          title: '剩余天数',
          key: 'surplusValidDays',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.surplusValidDays)
            ])
          }
        },
        {
          title: '年化率（%）',
          key: 'annualRate',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.annualRate)
            ])
          }
        },
        {
          title: '每10万扣款（元）',
          align: 'right',
          key: 'discountFee',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.discountFee))
          }
        },
        {
          title: '瑕疵',
          key: 'flaws',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.flaws)
            ])
          }
        },
        {
          title: '签约操作人',
          key: 'publishPerson',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.publishPerson)
            ])
          }
        },
        {
          title: '成交时间',
          key: 'dealTime',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.dealTime)
            ])
          }
        },
        {
          title: '关联询价单',
          key: 'inquireId',
          width: 80,
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.handleViewInquireDetail(params.row)
                }
              }
            }, [
              h('span', params.row.inquireId)
            ])
          }
        },
        {
          title: '关联报价单',
          key: 'quotationId',
          width: 80,
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.handleViewQuotationDetail(params.row)
                }
              }
            }, [
              h('span', params.row.quotationId)
            ])
          }
        }
      ]
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    this.getOrderList()
  },
  methods: {
    ...mapActions([
      'findOrderList',
      'acceptedQuotation',
      'rejectQuotation',
      'revokeOrder'
    ]),
    getOrderList () {
      this.findOrderList(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.total
          this.list = res.data.data.list
        }
      })
    },
    handleView (row) {
      const route = {
        name: 'sellorderdetail',
        query: {
          orderId: row.id
        }
      }
      this.$router.push(route)
    },
    handleViewInquireDetail (row) {
      const route = {
        name: 'inquiredetail',
        query: {
          id: row.inquireId
        }
      }
      this.$router.push(route)
    },
    handleViewQuotationDetail (row) {
      const route = {
        name: 'sellquotationdetail',
        query: {
          quotationId: row.quotationId
        }
      }
      this.$router.push(route)
    },
    handleViewContractFile (row) {
      if (row.contractFileUrlView) {
        // window.location.href = row.contractFileUrlView
        this.visible = true
        this.title = '查看文件'
        this.viewUrl = row.contractFileUrlView
      }
    },
    handleViewOrderProcess () {
      if (this.orderProcessUrl) {
        this.orderProcessVisible = true
      }
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getOrderList()
    },
    handleSubimt () {
      this.form.current = 1
      this.getOrderList()
    },
    handleSignContract (row, type, contractFileUrlView) {
      this.showModal = true
      this.$refs.agreement.getContent(row.id, type, row.contractType, contractFileUrlView)
    },
    handleClose (src) {
      this.showModal = false
      this.form.current = 1
      this.getOrderList()
      if (src) {
        setTimeout(() => {
          this.visible = true
          this.viewUrl = src
        }, 500)
      }
    },
    handleAcceptedQuotation (row) {
      this.$Modal.confirm({
        title: '是否确定接受报价',
        content: '系统将会生成一笔订单，状态为【待买家签订合同】,如有疑问，请咨询0755-86571036',
        onOk: () => {
          const data = { quotationId: row.quotationId }
          this.acceptedQuotation(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              setTimeout(() => {
                this.$Message.info('接受报价成功')
              }, 500)
              this.form.current = 1
              this.getOrderList()
            }
          })
        }
      })
    },
    handleRejectQuotation (row) {
      this.$Modal.confirm({
        title: '是否确定拒绝报价',
        content: '取消将会扣除您公司在平台的信用分,如有疑问，请咨询0755-86571036',
        onOk: () => {
          const data = { quotationId: row.quotationId }
          this.rejectQuotation(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              setTimeout(() => {
                this.$Message.info('拒绝报价成功')
              }, 500)
              this.form.current = 1
              this.getOrderList()
            }
          })
        }
      })
    },
    handleRevoke (row) {
      this.$Modal.confirm({
        title: '提示',
        content: '是否确定撤单',
        onOk: () => {
          const data = { orderId: row.id, userType: 1 }
          this.revokeOrder(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              setTimeout(() => {
                this.$Message.info('撤单成功')
              }, 500)
              this.form.current = 1
              this.getOrderList()
            }
          })
        }
      })
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
