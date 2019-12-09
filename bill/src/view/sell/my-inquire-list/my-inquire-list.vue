<style lang="less" scoped>
.ivu-dropdown-item:hover {
  background-color: #ffffff;
}
</style>

<template>
  <div class="list-table">
    <Card :bordered="false">
      <div class="search-con-top">
        <Select clearable
                v-model="form.status"
                placeholder="选择询价单状态"
                style="width:200px">
          <Option v-for="item in inquireStatusArr"
                  :value="item.value"
                  :key="item.value">{{ item.label }}</Option>
        </Select>
        <Input clearable
               placeholder="输入承兑人"
               class="search-input"
               v-model="form.accepterName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入询价单ID"
               class="search-input"
               v-model="form.id"
               @on-blur="(event) => form.id = isNaN(parseInt(event.target.value)) ? '' : parseInt(event.target.value) + ''"
               style="width: 200px" />
        发布时间 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择发布时间"
                    @on-change="(value) =>form.createTimeBegin = value"
                    v-model="form.createTimeBegin"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="datetime"
                    placeholder="选择发布时间"
                    @on-change="(value) => form.createTimeEnd = value"
                    v-model="form.createTimeEnd"></DatePicker>&nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="handleSubimt"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
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
                  <Button @click="handleViewQuotation(row)"
                          type="text">查看报价</Button>
                </DropdownItem>
                <DropdownItem>
                  <Button :disabled="!(row.status === 1 || row.status === 2)"
                          @click="handleCancelInquire(row)"
                          type="text">撤销发布</Button>
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

// 询价状态(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
const inquireStatusArr = [{
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

export default {
  name: 'myinquirelist',
  components: {},
  data () {
    return {
      inquireStatusArr: inquireStatusArr,
      form: {
        current: 1,
        size: 10,
        accepterName: '',
        hasFlaws: '',
        selectByLogin: true,
        billAmtBegin: '',
        billAmtEnd: '',
        surplusValidDaysBegin: '',
        surplusValidDaysEnd: '',
        id: '',
        status: '',
        createTimeBegin: '',
        createTimeEnd: ''
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
          title: '询价单ID',
          key: 'id',
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: (row) => {
                  this.handleView(params.row)
                }
              }
            }, [
              h('span', params.row.id)
            ])
          }
        },
        {
          title: '询价单状态',
          key: 'statusCH',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.statusCH)
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
          title: '发布操作人',
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
          title: '发布时间',
          key: 'createTime',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('span', params.row.createTime)
            ])
          }
        },
        {
          title: '接受的报价单',
          key: 'relatedQuotationId',
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: (row) => {
                  this.handleViewQuotationDetail(params.row)
                }
              }
            }, [
              h('span', params.row.relatedQuotationId)
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
    if (this.permission.indexOf('ticket:inquire:myList') !== -1) {
      this.getInquireListData()
    }
  },
  methods: {
    ...mapActions([
      'getMyInquireList',
      'cancelInquire'
    ]),
    getInquireListData () {
      this.getMyInquireList(this.form).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.total = res.data.data.total
          this.list = res.data.data.list
        }
      })
    },
    handleView (row) {
      const route = {
        name: 'inquiredetail',
        query: {
          id: row.id
        }
      }
      this.$router.push(route)
    },
    handleViewQuotationDetail (row) {
      const route = {
        name: 'sellquotationdetail',
        query: {
          quotationId: row.relatedQuotationId
        }
      }
      this.$router.push(route)
    },
    handleViewQuotation (row) {
      const route = {
        name: 'quotationlist',
        query: {
          inquireId: row.id
        }
      }
      this.$router.push(route)
    },
    handleCancelInquire (row) {
      this.$Modal.confirm({
        title: '是否确定取消发布',
        content: '取消将会扣除您公司在平台的信用分,如有疑问，请咨询0755-86571036',
        onOk: () => {
          const data = {
            id: row.id
          }
          this.cancelInquire(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              this.$Message.info('撤销发布成功')
              this.form.current = 1
              this.getInquireListData()
            }
          })
        }
      })
    },
    editStatus (id, status) {
      // if (this.permission.indexOf('gsgl:company:status:edit') !== -1) {
      this.editCompanyStatus({ id, status }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.success(status === 1 ? '启用成功' : '禁用成功')
          this.getData()
        }
      })
      // } else {
      //   this.$Message.info('没有权限【编辑公司状态】')
      // }
    },
    show (index) {
      this.activeIndex = index
    },
    changePage (current) {
      this.form.current = current
      this.getInquireListData()
    },
    handleSubimt () {
      this.form.current = 1
      this.getInquireListData()
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
