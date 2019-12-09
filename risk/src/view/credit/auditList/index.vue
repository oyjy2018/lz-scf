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
                placeholder="选择流程状态搜索"
                clearable
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
      <div class="buttons">
        <Tabs value="name2"
              @on-click="onClickTab">
          <TabPane label="所有"
                   name="name1"></TabPane>
          <TabPane label="我的待办"
                   name="name2"></TabPane>
        </Tabs>
      </div>
      <Table ref="selection"
             border
             stripe
             :loading="tableLoading"
             :columns="columns"
             :data="list"
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
                <DropdownItem>
                  <Button :disabled="row.status === 1"
                          @click="audit(row)"
                          type="text">审核</Button>
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
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'auditList',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        flowCode: '',
        customerName: '',
        businessId: '',
        itemName: '',
        isWait: true, // 是否查询待办 (查所有：false  查待办：true)
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
          title: '',
          width: 50,
          align: 'center',
          slot: 'icon'
        },
        {
          title: '申请ID',
          align: 'center',
          key: 'id',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.view(params.row)
                }
              }
            }, params.row.id)
          }
        },
        {
          title: '申请人',
          align: 'center',
          key: 'customerName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.customerName)
          }
        },
        {
          title: '授信项目',
          align: 'center',
          key: 'itemNames',
          render: (h, params) => {
            let html = ''
            if (params.row.itemNames) {
              params.row.itemNames.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${item}</p>`
              })
            }
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              },
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '申请业务种类（授信成功后）',
          align: 'center',
          key: 'applyCreditBusiness',
          render: (h, params) => {
            let html = ''
            if (params.row.applyCreditBusiness) {
              params.row.applyCreditBusiness.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${item}</p>`
              })
            }
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              },
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
          render: (h, params) => {
            let html = ''
            if (params.row.applyCreditMoney) {
              params.row.applyCreditMoney.split('、').forEach(item => {
                html += `<p style="line-height: 24px;">${absMoney(item)}</p>`
              })
            }
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              },
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '手机号',
          align: 'center',
          key: 'mobile',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.mobile)
          }
        },
        {
          title: '申请日期',
          align: 'center',
          key: 'applyTime',
          width: 150,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.applyTime)
          }
        },
        {
          title: '流程状态',
          align: 'center',
          key: 'flowName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.flowName)
          }
        },
        {
          title: '当前处理用户组',
          align: 'center',
          key: 'disposeRoleNames',
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
  mounted () {
    this.getData()
    this.getFlowCodeList1()
  },
  methods: {
    ...mapActions([
      'auditList',
      'getFlowCodeList'
    ]),
    getData () {
      this.tableLoading = true
      this.auditList(this.form).then(res => {
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
    view (e) {
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
    audit (e) {
      const route = {
        name: 'applyaudit',
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
    onClickTab (isWait) {
      this.form.isWait = isWait === 'name2'
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
