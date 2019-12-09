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
               placeholder="输入授信申请ID搜索"
               class="search-input"
               v-model="form.applyId"
               style="width: 200px" />
      </div>
      <div class="search-con-top">
        授信开始日 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择授信开始日"
                    @on-change="(value) => onDateChang('creditStartDateStart', value)"
                    format="yyyy-MM-dd"
                    v-model="form.creditStartDateStart"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择授信开始日"
                    @on-change="(value) => onDateChang('creditStartDateEnd', value)"
                    v-model="form.creditStartDateEnd"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        授信截止日 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择授信截止日"
                    @on-change="(value) => onDateChang('creditEndDateStart', value)"
                    v-model="form.creditEndDateStart"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择授信截止日"
                    @on-change="(value) => onDateChang('creditEndDateEnd', value)"
                    v-model="form.creditEndDateEnd"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <!-- <div class="buttons">
        <Tabs value="name2"
              @on-click="onClickTab">
          <TabPane label="所有"
                   name="name1"></TabPane>
          <TabPane label="我的待办"
                   name="name2"></TabPane>
        </Tabs>
      </div> -->
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
                          @click="applyTickt(row)"
                          type="text">申请开商票</Button>
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
  name: 'myAudit',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        id: '',
        customerName: '',
        itemName: '',
        applyId: '',
        creditStartDateStart: '',
        creditStartDateEnd: '',
        creditEndDateStart: '',
        creditEndDateEnd: '',
        size: 10,
        current: 1
      },
      total: 0,
      pages: 0,
      list: [],
      activeIndex: -1,
      columns: [
        {
          title: '',
          width: 50,
          align: 'center',
          slot: 'icon'
        },
        {
          title: 'ID',
          key: 'id',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
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
          key: 'itemName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.itemName)
          }
        },
        {
          title: '授信金额（元）',
          key: 'creditAmount',
          align: 'right',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.creditAmount))
          }
        },
        {
          title: '已用信(元)',
          key: 'usedCreditAmount',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.viewTicketUse(params.row)
                }
              }
            }, absMoney(params.row.usedCreditAmount))
          }
        },
        {
          key: 'remainCreditAmount',
          width: 150,
          align: 'right',
          renderHeader: (h, params) => {
            return h('Tooltip', {
              props: {
                content: '授信金额-已用信金额',
                placement: 'top-end'
              },
              style: {
                lineHeight: '43px'
              }
            }, [
              h('span', {
                style: {
                  marginRight: '10px'
                }
              }, '授信余额(元)'),
              h('a', {
                style: {
                  position: 'absolute',
                  top: '5px',
                  right: '-10px',
                  border: '1px solid #2c8ef3',
                  borderRadius: '50%',
                  width: '16px',
                  height: '16px',
                  textAlign: 'center',
                  lineHeight: '16px'
                }
              }, '?')
            ])
          },
          render: (h, params) => {
            return h('span', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, absMoney(params.row.remainCreditAmount))
          }
        },
        {
          key: 'creditDays',
          align: 'center',
          width: 150,
          renderHeader: (h, params) => {
            return h('Tooltip', {
              props: {
                content: '授信截止日 - 授信开始日',
                placement: 'top-end'
              },
              style: {
                lineHeight: '43px'
              }
            }, [
              h('span', {
                style: {
                  marginRight: '10px'
                }
              }, '授信期限(天)'),
              h('a', {
                style: {
                  position: 'absolute',
                  top: '5px',
                  right: '-10px',
                  border: '1px solid #2c8ef3',
                  borderRadius: '50%',
                  width: '16px',
                  height: '16px',
                  textAlign: 'center',
                  lineHeight: '16px'
                }
              }, '?')
            ])
          },
          render: (h, params) => {
            return h('span', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.creditDays)
          }
        },
        {
          title: '授信开始日期',
          align: 'center',
          key: 'creditStart',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.creditStart)
          }
        },
        {
          title: '授信截止日期',
          align: 'center',
          key: 'creditEnd',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.creditEnd)
          }
        },
        {
          title: '关联授信申请ID',
          key: 'applyId',
          align: 'center',
          width: 150,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.view(params.row)
                }
              }
            }, params.row.applyId)
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
    const { id } = this.$route.query
    if (id) {
      this.form.id = id
    }
    this.getData()
  },
  methods: {
    ...mapActions([
      'myAuditList'
    ]),
    getData () {
      this.tableLoading = true
      this.myAuditList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { list, total } = res.data.data
          this.list = list
          this.total = total
          // this.pages = pages
        }
      })
    },
    view (e) {
      const route = {
        name: 'viewapply',
        query: {
          companyId: e.companyId,
          businessTypeId: e.businessTypeId,
          creditApplyId: e.applyId,
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
    onDateChang (name, value) {
      this.form[name] = value
    },
    handleSubmit () {
      this.form.current = 1
      this.getData()
    },
    applyTickt (row) {
      const route = {
        name: 'ticketapply',
        query: {
          companyId: row.companyId,
          creditRecordId: row.id,
          businessTypeId: row.businessTypeId,
          creditTicketApplyId: '',
          flowCode: row.flowCode
        }
      }
      this.$router.push(route)
    },
    viewTicketUse (row) {
      const route = {
        name: 'myticketuse',
        query: {
          creditId: row.id
        }
      }
      this.$router.push(route)
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
