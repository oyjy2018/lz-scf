<style lang="less">
@import "../../../index.less";
@import "../company/company.less";
</style>

<template>
  <div class="list-table">
    <Card :bordered="false"
          title="收款账户">
      <div class="buttons">
        <Button style="margin-right: 10px"
                type="primary"
                icon="md-add"
                @click="routerTo"
                ghost>添加银行账号</Button>
      </div>
      <Table ref="selection"
             border
             stripe
             :loading="tableLoading"
             :columns="columns"
             :data="bankList"
             :highlight-row="true">
        <template slot-scope="{ row, index }"
                  slot="icon">
          <div @mouseenter="show(index)">
            <Dropdown placement="bottom-start">
              <a href="javascript:void(0)">
                <Icon v-show="index === activeIndex"
                      size="20"
                      type="ios-settings"
                      class="table-icon-setting"></Icon>
              </a>
              <DropdownMenu slot="list">
                <DropdownItem v-if="row.subStatus === '13' || row.subStatus === '23'">
                  <Button @click="handleVerify(row)"
                          type="text">输入打款金额验证</Button>
                </DropdownItem>
                <!-- // 账号类型(1:收票账户 2:收款账户) -->
                <DropdownItem v-if="row.accountStatus === 2">
                  <Button :disabled="row.isRepayDefault ? true : false"
                          @click="handleSetDefault(row.id, '2')"
                          type="text">设为默认收款账户</Button>
                </DropdownItem>
                <DropdownItem v-if="row.accountStatus === 3 || row.accountStatus === 4">
                  <Button @click="handleSendBankPayment(row.id)"
                          type="text">重新申请打款认证</Button>
                </DropdownItem>
                <DropdownItem>
                  <Button @click="handleDelete(row.id)"
                          type="text">删除</Button>
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
    <Modal v-model="modal"
           width="800"
           draggable
           scrollable
           @on-visible-change="handleVerifyVisible"
           @on-ok="handleVerifySubmit()">
      <Steps slot="header"
             :current="1"
             size="small">
        <Step title="提交"
              content="提交收款银行账号"></Step>
        <Step title="验证"
              content="小额打款验证"></Step>
        <Step title="成功"
              content="添加成功（收款账户）"></Step>
      </Steps>
      <div>
        <p>平台已委托京东金融已于{{ paySuccessTime }}提交一笔0.5元以下的打款申请，预计到账时间为1个工作日，请于对公账户收到金额后，在此页面回填到账金额</p>
        <br />
        <p>对公账户：{{ bankAccountNo }}</p>
        <p><Input v-model="amount"
                 style="width: 100px;margin-right: 10px" />输入金额必须与实际收到金额一致</p>
      </div>
    </Modal>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

// 账户状态(1:认证中 2:认证成功 3:认证失败 4:认证过期) accountStatus
const ACCOUNTSTATUS = {
  1: '认证中',
  2: '认证成功',
  3: '认证失败',
  4: '认证过期'
}
// 打款认证状态 subStatus
const SUBSTATUS = {
  11: '待发起代付',
  12: '待打款',
  13: '打款成功',
  21: '代付失败',
  22: '打款失败',
  23: '金额确认失败',
  24: '打款申请失败次数超限',
  25: '金额验证失败次数超限',
  30: '退票',
  40: '金额确认成功',
  50: '认证过期'
}

export default {
  name: 'pay-banks',
  data () {
    return {
      tableLoading: true,
      modal: false,
      companyBankId: '',
      amount: '',
      bankAccountNo: '',
      paySuccessTime: '',
      ACCOUNTSTATUS: ACCOUNTSTATUS,
      bankList: [],
      form: {
        accountType: 2,
        size: 10,
        current: 1
      },
      total: 0,
      pages: 0,
      activeIndex: -1,
      columns: [
        {
          title: '',
          width: 60,
          align: 'center',
          slot: 'icon'
        },
        {
          title: '银行账号',
          key: 'bankAccountNo',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.isRepayDefault === 0
              ? params.row.bankAccountNo
              : [h('span', params.row.bankAccountNo), h('icon', { props: { type: 'ios-checkmark', size: 19 }, style: { color: 'red' } }), h('span', '默认')]
            )
          },
          width: 260
        },
        {
          title: '户名',
          key: 'bankAccountName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.bankAccountName)
          }
        },
        {
          title: '银行名称',
          key: 'bankName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.bankName)
          },
          width: 150
        },
        {
          title: '银行省市',
          key: 'provinceName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.provinceName + params.row.cityName)
          }
        },
        {
          title: '银行支行',
          key: 'bankChildName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.bankChildName)
          }
        },
        {
          title: '账户状态',
          key: 'accountStatus',
          width: '90px',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, ACCOUNTSTATUS[params.row.accountStatus])
          }
        },
        {
          title: '打款认证状态',
          key: 'subStatus',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, SUBSTATUS[params.row.subStatus])
          }
        }
      ]
    }
  },
  mounted () {
    this.getData()
  },
  methods: {
    ...mapActions([
      'getCompanyBanks',
      'defaultCompanyBank',
      'verifyCompanyBank',
      'sendBankPayment',
      'deleteCompanyAccountType'
    ]),
    getData () {
      this.tableLoading = true
      this.getCompanyBanks(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total, pages } = res.data.data
          this.bankList = records
          this.total = total
          this.pages = pages
        }
      })
    },
    show (index) {
      this.activeIndex = index
    },
    handleVerify (row) {
      this.companyBankId = row.id
      this.bankAccountNo = row.bankAccountNo
      this.paySuccessTime = row.paySuccessTime
      this.modal = true
    },
    handleVerifyVisible (value) {
      if (!value) {
        this.companyBankId = ''
        this.bankAccountNo = ''
        this.paySuccessTime = ''
        this.amount = ''
      }
    },
    handleVerifySubmit () {
      if (this.amount !== '') {
        const data = {
          companyBankId: this.companyBankId,
          amount: this.amount
        }
        this.verifyCompanyBank(data).then(res => {
          if (res && res.status === 200 && res.data.code === 10000) {
            this.$Message.info('提交成功，5分钟后回到此页面查看验证结果')
          }
        })
      } else {
        this.$Message.warning({
          content: '金额不能为空',
          duration: 5,
          closable: true
        })
      }
    },
    handleSetDefault (companyBankId, accountType) {
      this.defaultCompanyBank({ companyBankId, accountType }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.info('设置默认成功')
          this.getData()
        }
      })
    },
    handleSendBankPayment (companyBankId) {
      this.sendBankPayment({ companyBankId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.info('重新发起打款成功')
          this.getData()
        }
      })
    },
    handleDelete (companyBankId) {
      const _this = this
      this.$Modal.confirm({
        title: '删除确认',
        content: '<p>是否确认删除？</p>',
        onOk: () => {
          _this.deleteCompanyAccountType({ companyBankId, accountType: 2 }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              _this.$Message.info('删除成功')
              _this.getData()
            }
          })
        },
        onCancel: () => {
          _this.getData()
        }
      })
    },
    routerTo () {
      this.$router.push({ path: './add', query: { accountType: 2 } })
    },
    changePage (current) {
      this.form.current = current
      this.getData()
    }
  }
}
</script>
