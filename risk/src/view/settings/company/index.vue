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
               placeholder="输入公司全称搜索"
               class="search-input"
               v-model="form.companyName"
               style="width: 200px" />
        <CheckboxGroup class="checkbox"
                       v-model="form.status">
          <Checkbox :label="0">待激活</Checkbox>
          <Checkbox :label="1">已启用</Checkbox>
          <Checkbox :label="2">已禁用</Checkbox>
        </CheckboxGroup>
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <div class="buttons">
        <!-- <Button style="margin-right: 10px"
                type="primary"
                icon="md-add"
                to="./company/add"
                ghost>添加公司</Button> -->
        <Button type="primary"
                icon="md-create"
                to="./company/audit-table"
                ghost>公司审核</Button>
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
                <!-- <DropdownItem>
                  <Button type="text">修改</Button>
                </DropdownItem> -->
                <DropdownItem>
                  <Button :disabled="row.status === 1"
                          @click="editStatus(row.companyId, 1)"
                          type="text">启用</Button>
                </DropdownItem>
                <DropdownItem>
                  <Button :disabled="row.status === 2"
                          @click="editStatus(row.companyId, 2)"
                          type="text">禁用</Button>
                </DropdownItem>
                <DropdownItem>
                  <Button @click="deleteCompany(row.companyName, row.companyId)"
                          type="text">删除</Button>
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </template>
        <template slot-scope="{ row, index }"
                  slot="status">
          <div @mouseenter="show(index)">{{row.status | statusText}}</div>
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

export default {
  name: 'company',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        companyName: '',
        status: [],
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
          width: 60,
          align: 'center',
          slot: 'icon'
        },
        {
          title: '公司全称',
          width: 250,
          align: 'center',
          key: 'companyName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  this.viewDetail(params.row)
                }
              }
            }, [
              h('Tooltip', {
                props: { placement: 'top' }
              }, [
                h('a', {
                  style: {
                    display: 'inline-block',
                    overflow: 'hidden',
                    width: params.column._width * 0.8 + 'px',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }
                }, params.row.companyName),
                h('span', {
                  slot: 'content',
                  style: { whiteSpace: 'normal', wordBreak: 'break-all' }
                }, params.row.companyName)
              ])
            ])
          }
        },
        {
          title: '统一社会信用代码',
          width: 200,
          align: 'center',
          key: 'creditCode',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.creditCode)
          }
        },
        {
          title: '法人',
          width: 100,
          align: 'center',
          key: 'legalPersonName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.legalPersonName)
          }
        },
        {
          title: '风控平台版本',
          width: 120,
          align: 'center',
          key: 'riskSystemVersionName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.riskSystemVersionName)
          }
        },
        {
          title: '票据交易平台版本',
          width: 140,
          align: 'center',
          key: 'ticketSystemVersionName',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.ticketSystemVersionName)
          }
        },
        {
          title: '成员数量',
          width: 100,
          align: 'center',
          key: 'userCount',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.userCount)
          }
        },
        {
          title: '状态',
          key: 'status',
          slot: 'status',
          width: 80,
          align: 'center'
        },
        {
          title: '京东注册状态',
          width: 120,
          align: 'center',
          key: 'isJdRegister',
          render: (h, params) => {
            // （0：待激活；1：已启用；2：已禁用）
            let text = '待激活'
            if (params.row.isJdRegister === 1) {
              text = '已启用'
            }
            if (params.row.isJdRegister === 2) {
              text = '已禁用'
            }
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, text)
          }
        },
        {
          title: '京东企业认证',
          width: 120,
          align: 'center',
          key: 'jdVerified',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.jdVerified ? '已认证' : '未认证')
          }
        },
        {
          title: '京东收款银行',
          width: 120,
          align: 'center',
          key: 'isRepayAccount',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.isRepayAccount ? '已设置' : '未设置')
          }
        },
        {
          title: '京东收票银行（平安）',
          width: 160,
          align: 'center',
          key: 'isReceiptAccount',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.isReceiptAccount ? '已设置' : '未设置')
          }
        },
        {
          title: 'e签宝企业注册',
          width: 120,
          align: 'center',
          key: 'esignVerified',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, params.row.esignVerified ? '已认证' : '未认证')
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
    statusText: value => {
      // 状态0未启用1已启用2禁用
      let text = '未启用'
      switch (value) {
        case 1:
          text = '已启用'
          break
        case 2:
          text = '禁用'
          break
      }
      return text
    }
  },
  mounted () {
    this.getData()
  },
  methods: {
    ...mapActions([
      'getCompanyData',
      'editCompanyStatus',
      'companyDelete'
    ]),
    getData () {
      this.tableLoading = true
      this.getCompanyData(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total, pages } = res.data.data
          this.list = records
          this.total = total
          this.pages = pages
        }
      })
    },
    editStatus (id, status) {
      this.editCompanyStatus({ id, status }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success(status === 1 ? '启用成功' : '禁用成功')
          this.getData()
        }
      })
    },
    deleteCompany (companyName, id) {
      this.$Modal.confirm({
        title: '提示',
        content: `确定删除【${companyName}】吗？`,
        onOk: () => {
          this.companyDelete({ id }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success('删除成功')
              this.getData()
            }
          })
        },
        onCancel: () => {
          this.$Message.info('取消删除')
        }
      })
    },
    show (index) {
      this.activeIndex = index
    },
    viewDetail (row) {
      const route = {
        name: 'company-detail',
        query: {
          companyId: row.companyId,
          companyName: row.companyName
        }
      }
      this.$router.push(route)
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
