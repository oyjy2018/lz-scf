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
               placeholder="输入出票人搜索"
               class="search-input"
               v-model="form.ticketGiveCompany"
               style="width: 200px" />
        <Input clearable
               placeholder="输入承兑人搜索"
               class="search-input"
               v-model="form.acceptorCompany"
               style="width: 200px" />
        <Input clearable
               placeholder="输入商票号码搜索"
               class="search-input"
               v-model="form.ticketNo"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联用信ID搜索"
               class="search-input"
               v-model="form.creditUseId"
               style="width: 200px" />
      </div>
      <div class="search-con-top">
        出票日期 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择用信开始日"
                    @on-change="(value) => onDateChang('ticketStartBegin', value)"
                    v-model="form.ticketStartBegin"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择用信开始日"
                    @on-change="(value) => onDateChang('ticketStartEnd', value)"
                    v-model="form.ticketStartEnd"></DatePicker> &nbsp;&nbsp;&nbsp;&nbsp;
        汇票到期日 &nbsp;&nbsp;&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择应结清日"
                    @on-change="(value) => onDateChang('ticketEndBegin', value)"
                    v-model="form.ticketEndBegin"></DatePicker>&nbsp;&nbsp;-&nbsp;&nbsp;
        <DatePicker :editable="false"
                    style="width: 150px"
                    type="date"
                    placeholder="选择应结清日"
                    @on-change="(value) => onDateChang('ticketEndEnd', value)"
                    v-model="form.ticketEndEnd"></DatePicker> &nbsp;&nbsp;&nbsp;&nbsp;
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
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
<!--   //可以在此加入其它操作 <DropdownItem><Button @click="addTicketUse(row)" type="text">豆汁儿</Button></DropdownItem>-->
                <Dropdown placement="right-start">
                  <DropdownItem>
                    <Button type="text">修改商票状态</Button>
                    <Icon type="ios-arrow-forward"></Icon>
                  </DropdownItem>
                  <DropdownMenu slot="list">
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,0)" type="text" size="small">已开票</Button></DropdownItem>
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,1)" type="text" size="small">已收票</Button></DropdownItem>
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,2)" type="text" size="small">提示付款中</Button></DropdownItem>
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,3)" type="text" size="small">付款签收中</Button></DropdownItem>
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,4)" type="text" size="small">已解付</Button></DropdownItem>
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,5)" type="text" size="small">贴现申请中</Button></DropdownItem>
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,6)" type="text" size="small">背书签收中</Button></DropdownItem>
                    <DropdownItem><Button @click="handleUpdateTicketStatus(row.id,7)" type="text" size="small">背书已签收</Button></DropdownItem>
                  </DropdownMenu>
                </Dropdown>
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
  name: 'ticketlist',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        creditUseId: '',
        ticketGiveCompany: '',
        acceptorCompany: '',
        ticketNo: '',
        ticketStartBegin: '',
        ticketStartEnd: '',
        ticketEndBegin: '',
        ticketEndEnd: '',
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
          title: '商票号码',
          align: 'center',
          width: 250,
          key: 'ticketNo',
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.ticketdetail(params.row)
                }
              }
            }, params.row.ticketNo)
          }
        },
        {
          title: '出票人',
          align: 'center',
          width: 250,
          key: 'ticketGiveCompany',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('Tooltip', {
                props: { placement: 'top' }
              }, [
                h('span', {
                  style: {
                    display: 'inline-block',
                    overflow: 'hidden',
                    width: params.column._width * 0.8 + 'px',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }
                }, params.row.ticketGiveCompany),
                h('span', {
                  slot: 'content',
                  style: { whiteSpace: 'normal', wordBreak: 'break-all' }
                }, params.row.ticketGiveCompany)
              ])
            ])
          }
        },
        {
          title: '收票人',
          align: 'center',
          width: 250,
          key: 'ticketGetCompany',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('Tooltip', {
                props: { placement: 'top' }
              }, [
                h('span', {
                  style: {
                    display: 'inline-block',
                    overflow: 'hidden',
                    width: params.column._width * 0.8 + 'px',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }
                }, params.row.ticketGetCompany),
                h('span', {
                  slot: 'content',
                  style: { whiteSpace: 'normal', wordBreak: 'break-all' }
                }, params.row.ticketGetCompany)
              ])
            ])
          }
        },
        {
          title: '商票金额（元）',
          width: 150,
          key: 'ticketMoney',
          align: 'right',
          render: (h, params) => {
            return h('span', absMoney(params.row.ticketMoney))
          }
        },
        {
          title: '承兑人',
          align: 'center',
          width: 250,
          key: 'acceptorCompany',
          render: (h, params) => {
            return h('div', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                }
              }
            }, [
              h('Tooltip', {
                props: { placement: 'top' }
              }, [
                h('span', {
                  style: {
                    display: 'inline-block',
                    overflow: 'hidden',
                    width: params.column._width * 0.8 + 'px',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }
                }, params.row.acceptorCompany),
                h('span', {
                  slot: 'content',
                  style: { whiteSpace: 'normal', wordBreak: 'break-all' }
                }, params.row.acceptorCompany)
              ])
            ])
          }
        },
        {
          title: '出票日期',
          align: 'center',
          width: 100,
          key: 'ticketStart'
        },
        {
          title: '汇票到期日',
          align: 'center',
          width: 100,
          key: 'ticketEnd'
        },
        {
          title: '商票状态',
          align: 'center',
          width: 100,
          key: 'ticketStatusName'
        },
        {
          title: '关联用信ID',
          key: 'creditUseId',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewticketDetail(params.row)
                }
              }
            }, params.row.creditUseId)
          }
        },
        {
          title: '关联授信ID',
          key: 'creditId',
          align: 'center',
          width: 100
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
  },
  methods: {
    ...mapActions([
      'findCreditList',
      'updateTicketStatus'
    ]),
    getData () {
      this.tableLoading = true
      this.findCreditList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { list, total } = res.data.data
          this.list = list
          this.total = total
          // this.pages = pages
        }
      })
    },
    handleUpdateTicketStatus (id, value) {
      this.tableLoading = true
      const data = {
        id: id,
        value: value
      }
      this.updateTicketStatus(data).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          this.$Message.info('修改成功')
          this.getData()
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
    ticketdetail (e) {
      const route = {
        name: 'ticketdetail',
        query: {
          id: e.id
        }
      }
      this.$router.push(route)
    },
    viewticketDetail (e) {
      const route = {
        name: 'ticketusedetail',
        query: {
          id: e.id,
          useType: 0
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
          creditRecordId: row.id,
          businessTypeId: row.businessTypeId,
          creditTicketApplyId: '',
          flowCode: row.flowCode
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
