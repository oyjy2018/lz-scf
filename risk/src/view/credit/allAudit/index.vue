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
      <div class="buttons">
        <Button style="margin-right: 10px"
                type="primary"
                icon="md-add"
                to="./exportxlsx"
                ghost>导入</Button>
      </div>
      <Table class="all-audit"
             ref="selection"
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
                  <Button :disabled="!(row.remainCreditAmount > 0)"
                          @click="addTicketUse(row)"
                          type="text">录入用信-商票</Button>
                </DropdownItem>
                <DropdownItem v-if="row.ifImport === '1'">
                  <Button @click="deleteRow(row)"
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
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { absMoney } from '@/libs/tools'

export default {
  name: 'allAudit',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        customerName: '',
        itemName: '',
        id: '',
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
          width: 200,
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
          width: 200,
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
          title: '授信金额(元)',
          key: 'creditAmount',
          align: 'right',
          width: 150,
          render: (h, params) => {
            return h('span', {
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
          width: 150,
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
          key: 'creditEnd',
          align: 'center',
          width: 150,
          renderHeader: (h, params) => {
            return h('Tooltip', {
              props: {
                content: '用信的应结清日必须早于授信截止日',
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
              }, '授信截止日期'),
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
          align: 'center',
          key: 'applyId',
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
        },
        {
          title: '是否人工导入',
          align: 'center',
          key: 'ifImport',
          width: 150,
          render: (h, params) => {
            return h('span', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.ifImport === '0' ? '否' : '是')
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
    const { creditApplyId, id } = this.$route.query
    if (creditApplyId) {
      this.form.applyId = creditApplyId
    }
    if (id) {
      this.form.id = id
    }
    this.getData()
  },
  methods: {
    ...mapActions([
      'allAuditList',
      'deleteRecord'
    ]),
    getData () {
      this.tableLoading = true
      this.allAuditList(this.form).then(res => {
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
    deleteRow (row) {
      this.$Modal.confirm({
        title: '提示',
        content: '确定删除【' + row.itemName + '】吗？',
        onOk: () => {
          setTimeout(() => {
            const data = {
              id: row.id
            }
            this.deleteRecord(data).then(res => {
              if (res && res.status === 200 && res.data.code === 10000) {
                this.$Modal.success({
                  title: '提示',
                  content: '删除成功'
                })
                this.form.current = 1
                this.getData()
              }
            })
          }, 500)
        },
        onCancel: () => {
          this.$Message.info('取消删除')
        }
      })
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
    },
    onDateChang (name, value) {
      this.form[name] = value
    },
    addTicketUse (row) {
      const route = {
        name: 'addticketuse',
        query: {
          creditId: row.id,
          enter: 'creditRecord'
        }
      }
      this.$router.push(route)
    },
    viewTicketUse (row) {
      const route = {
        name: 'allticketuse',
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
