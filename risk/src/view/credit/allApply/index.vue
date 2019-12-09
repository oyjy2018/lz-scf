<style lang="less" scoped>
@import "../../../index.less";
.ivu-dropdown-item:hover {
  background-color: #ffffff;
}
</style>

<template>
  <div class="list-table">
    <Card :bordered="false">
      <div class="search-con-top">
        <Select v-model="form.flowCode"
                clearable
                placeholder="选择流程状态搜索"
                style="width:200px">
          <Option v-for="item in flowCodeList"
                  :value="item.flowCode"
                  :key="item.flowCode">{{ item.flowName }}</Option>
        </Select>
        <Input clearable
               placeholder="输入申请人搜索"
               class="search-input"
               v-model="form.customerName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入授信项目搜索"
               class="search-input"
               v-model="form.itemName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入申请ID搜索"
               class="search-input"
               v-model="form.businessId"
               style="width: 200px" />
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
<!--      <div class="buttons">-->
<!--        <Button style="margin-right: 10px"-->
<!--                type="primary"-->
<!--                icon="md-add"-->
<!--                to="./apply"-->
<!--                ghost>添加项目</Button>-->
<!--      </div>-->
      <Table ref="selection"
             border
             stripe
             :loading="tableLoading"
             :columns="columns"
             :data="list"
             :highlight-row="true">
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
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'allApply',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        flowCode: '',
        customerName: '',
        businessId: '',
        itemName: '',
        size: 10,
        current: 1
      },
      total: 0,
      pages: 0,
      list: [],
      flowCodeList: [],
      activeIndex: -1,
      columns: [
        {
          type: 'index',
          width: 50,
          align: 'center'
        },
        {
          title: '申请ID',
          width: 100,
          key: 'id',
          align: 'center',
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.onCurrentchange(params.row)
                }
              }
            }, params.row.id)
          }
        },
        {
          title: '申请人',
          align: 'center',
          width: 200,
          key: 'customerName'
        },
        {
          title: '授信项目',
          align: 'center',
          width: 200,
          key: 'itemNames',
          render: (h, params) => {
            let html = ''
            if (params.row.itemNames) {
              params.row.itemNames.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${item}</p>`
              })
            }
            return h('div', {
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '申请授信金额（元）',
          key: 'applyCreditMoney',
          align: 'right',
          width: 150,
          render: (h, params) => {
            let html = ''
            if (params.row.applyCreditMoney) {
              params.row.applyCreditMoney.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${absMoney(item)}</p>`
              })
            }
            return h('div', {
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '审批授信金额（元）',
          key: 'auditCreditMoney',
          align: 'right',
          width: 150,
          render: (h, params) => {
            let html = ''
            if (params.row.auditCreditMoney) {
              params.row.auditCreditMoney.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${absMoney(item)}</p>`
              })
            }
            return h('div', {
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '审批授信开始日',
          key: 'auditCreditBeginDate',
          align: 'center',
          width: 150,
          render: (h, params) => {
            let html = ''
            if (params.row.auditCreditBeginDate) {
              params.row.auditCreditBeginDate.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${item}</p>`
              })
            }
            return h('div', {
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '审批授信截止日',
          key: 'auditCreditEndDate',
          align: 'center',
          width: 150,
          render: (h, params) => {
            let html = ''
            if (params.row.auditCreditEndDate) {
              params.row.auditCreditEndDate.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${item}</p>`
              })
            }
            return h('div', {
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '手机号',
          align: 'center',
          width: 150,
          key: 'mobile'
        },
        {
          title: '申请日期',
          align: 'center',
          key: 'applyTime',
          width: 150
        },
        {
          title: '流程状态',
          align: 'center',
          width: 120,
          key: 'flowName'
        },
        {
          title: '当前处理用户组',
          align: 'center',
          key: 'disposeRoleNames',
          width: 150,
          render: (h, params) => {
            return h('Tooltip',
              [
                params.row.showDisposeRoleNames, // 显示文字
                h('div', {
                  slot: 'content',
                  style: {
                    whiteSpace: 'normal'
                  }
                }, params.row.disposeRoleNames)// 文字提示内容
              ])
          }
        },
        {
          title: '当前处理人',
          align: 'center',
          key: 'disposeUserNames',
          width: 150,
          render: (h, params) => {
            return h('Tooltip',
              [
                params.row.showDisposeUserNames, // 显示文字
                h('div', {
                  slot: 'content',
                  style: {
                    whiteSpace: 'normal'
                  }
                }, params.row.disposeUserNames)// 文字提示内容
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
  filters: {
    itemNamesText: value => {
      if (value) {
        let text = ''
        value.split('、').forEach(item => {
          text += `${item}/r/n`
        })
        return text
      } else {
        return value
      }
    }
  },
  mounted () {
    this.getData()
    this.getFlowCodeList1()
  },
  methods: {
    ...mapActions([
      'allApplyList',
      'getFlowCodeList'
    ]),
    getData () {
      this.tableLoading = true

      // console.log(this.form)

      this.allApplyList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { list, total } = res.data.data
          this.list = list
          this.total = total
          // this.pages = pages
        }
      })
    },
    getFlowCodeList1 () {
      const data = {
        param: '申请授信'
      }
      this.getFlowCodeList(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.flowCodeList = res.data.data
        }
      })
    },
    onCurrentchange (e) {
      const route = {
        name: 'viewapply',
        query: {
          companyId: e.companyId,
          businessTypeId: e.businessTypeId,
          creditApplyId: e.id,
          flowCode: e.flowCode
        }
      }
      this.$router.push(route)
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getData()
    },
    handleSubmit () {
      this.form.current = 1
      this.getData()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
