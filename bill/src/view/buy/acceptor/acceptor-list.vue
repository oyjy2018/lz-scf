<style lang="less" scoped>
  .ivu-dropdown-item:hover {
    background-color: #ffffff;
  }
</style>

<template>
  <Row :gutter="16">
  <div class="list-table">
    <Card :bordered="false">
      <div class="search-con-top">
          <Col span="7">
            <Input clearable
                   placeholder="输入承兑方"
                   class="search-input"
                   v-model="form.acceptorName"
                   style="width: 200px"/>
            <Button @click="handleSubmit"
                    class="search-btn"
                    type="primary"
                    icon="ios-search">搜索
            </Button>
          </Col>
          <Col span="12">
            <RadioGroup v-model="limit">
              <span>限制承兑方：</span>
              <span @click="handleUpdateAcceptorLimit(1)">
                <Radio label="只允许对列表中的承兑方的票据进行报价">
                  <span>只允许对列表中的承兑方的票据进行报价&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                  <Tooltip :transfer="transfer"  max-width="300" placement="bottom" offset="-10" content="如果限制承兑方，则贵公司只能对列表中的承兑方进行报价，且 对承兑方的贴现额度需要控制在设置的贴现授信额度中；">
                    <a style='position: absolute; top: -12px; right: 0px; border: 1px solid #2c8ef3; borderRadius: 50%; width: 16px; height: 16px; textAlign: center; lineHeight: 16px'>?</a>
                  </Tooltip>
                </Radio>

              </span>
              <span @click="handleUpdateAcceptorLimit(0)"><Radio label="不做限制"></Radio></span>
            </RadioGroup>
          </Col>

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
                <DropdownItem>
                  <Button @click="handleDeleteAcceptor(row)"
                          type="text">删除
                  </Button>
                </DropdownItem>
                <DropdownItem>
                  <Button @click="handleUpdate(row)"
                          type="text">修改
                  </Button>
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
                             party="b"
                             @on-click="handleClose"/>
  </div>
  </Row>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'
import ticketTransferAgreement from '../../ticket-transfer-agreement/ticket-transfer-agreement'

export default {
  name: 'acceptorlist',
  components: {
    ticketTransferAgreement
  },
  data () {
    return {
      limit: '',
      visible: false,
      viewUrl: '',
      showModal: false,
      transfer: true,
      myOrderAccess: false,
      form: {
        current: 1,
        size: 10,
        acceptorName: ''
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
          title: '承兑方ID',
          key: 'id',
          render: (h, params) => {
            return h('div', params.row.id)
          }
        },
        {
          title: '承兑方',
          key: 'acceptorName',
          render: (h, params) => {
            return h('div', params.row.acceptorName)
          }
        },
        {
          title: '贴现授信额度（元）',
          key: 'limitMoney',
          align: 'right',
          render: (h, params) => {
            return h('div', absMoney(params.row.limitMoney))
          }
        },
        {
          title: '已贴现金额（元）',
          key: 'useLimitMoney',
          align: 'right',
          render: (h, params) => {
            if (this.myOrderAccess && params.row.useLimitMoney !== 0) {
              return h('a', {
                on: {
                  click: () => {
                    this.goToBuyOrderList(params.row)
                  }
                }
              }, absMoney(params.row.useLimitMoney))
            } else {
              return h('div', absMoney(params.row.useLimitMoney))
            }
          }
        },
        {
          title: '剩余贴现额度（元）',
          key: 'remainLimitMoney',
          align: 'right',
          render: (h, params) => {
            return h('div', absMoney(params.row.remainLimitMoney))
          }
        },
        {
          title: '录入日期',
          key: 'maturityDate',
          align: 'center',
          render: (h, params) => {
            return h('div', params.row.createTime)
          }
        },
        {
          title: '备注',
          align: 'center',
          key: 'remark',
          render: (h, params) => {
            return h('Tooltip',
              [
                this.handleShowRemark(params.row.remark), // 显示文字
                h('div', {
                  slot: 'content',
                  style: {
                    whiteSpace: 'normal'
                  }
                }, params.row.remark)// 文字提示内容
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
    this.accessControl()
    this.getAcceptorList()
    this.handleIsLimit()
  },
  methods: {
    ...mapActions([
      'acceptorList',
      'deleteAcceptor',
      'isLimit',
      'updateAcceptorLimit'
    ]),
    // 页面功能控制
    accessControl () {
      // 有我是买方-我的订单的权限
      if (this.permission.indexOf('ticket:order:list') !== -1) {
        this.myOrderAccess = true
      }
      console.log(this.myOrderAccess)
    },
    getAcceptorList () {
      this.acceptorList(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.total
          this.list = res.data.data.records
        }
      })
    },
    handleShowRemark (value) {
      if (!value) return value
      if (value.length <= 10) return value
      return value.substr(0, 10) + '...'
    },
    handleDeleteAcceptor (row) {
      this.$Modal.confirm({
        title: '提示',
        content: '是否确认删除？',
        onOk: () => {
          const data = { id: row.id }
          this.deleteAcceptor(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              setTimeout(() => {
                this.$Message.info('删除成功')
              }, 500)
              this.form.current = 1
              this.getAcceptorList()
            }
          })
        }
      })
    },
    handleUpdate (row) {
      const route = {
        path: '/buy/updateacceptor',
        name: 'buyupdateacceptor',
        query: {
          id: row.id
        }
      }
      this.$router.push(route)
    },
    handleIsLimit () {
      this.isLimit().then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.limit = res.data.data === 1 ? '只允许对列表中的承兑方的票据进行报价' : '不做限制'
        }
      })
    },
    handleUpdateAcceptorLimit (value) {
      this.$Modal.confirm({
        title: '提示',
        content: value === 1 ? '是否确认限制承兑方？' : '是否确认解除限制承兑方？',
        onOk: () => {
          const data = {
            value: value
          }
          this.updateAcceptorLimit(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              setTimeout(() => {
                this.$Message.success('保存成功')
              }, 500)
              this.handleIsLimit()
              // this.form.current = 1
            }
          })
        },
        onCancel: () => { this.handleIsLimit() }
      })
    },
    goToBuyOrderList (row) {
      const route = {
        name: 'orderlist',
        query: {
          acceptorName: row.acceptorName,
          orderStatus: '99',
          isTicketMaturity: 'true'
        }
      }
      this.$router.push(route)
    },

    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getAcceptorList()
    },
    handleSubmit () {
      this.form.current = 1
      this.getAcceptorList()
    },
    handleClose (src) {
      this.showModal = false
      this.form.current = 1
      this.getAcceptorList()
      if (src) {
        setTimeout(() => {
          this.visible = true
          this.viewUrl = src
        }, 500)
      }
    }
  }
}
</script>

<style>
  .ivu-menu-vertical.ivu-menu-light:after {
    background: #ffffff;
  }
</style>
