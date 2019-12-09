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
               v-model="form.applyUserName"
               style="width: 200px" />
        <!-- <Select v-model="form.businessTypeId"
                placeholder="选择业务类型"
                clearable
                style="width:200px">
          <Option :value="2">开商票</Option>
        </Select> -->
        <Input clearable
               placeholder="输入授信项目搜索"
               class="search-input"
               v-model="form.creditItemName"
               style="width: 200px" />
        <Input clearable
               placeholder="输入用信申请ID搜索"
               class="search-input"
               v-model="form.id"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联授信ID搜索"
               class="search-input"
               v-model="form.creditId"
               style="width: 200px" />
        <Input clearable
               placeholder="输入关联授信申请ID搜索"
               class="search-input"
               v-model="form.creditApplyId"
               style="width: 200px" />
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
                  <Button :disabled="!(row.flowName == '草稿')"
                          @click="editticketapply(row)"
                          type="text">修改</Button>
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
  name: 'myticketapply',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        businessTypeId: 2,
        creditId: '',
        creditItemName: '',
        applyUserName: '',
        creditApplyId: '',
        id: '',
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
          title: '申请ID',
          key: 'id',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewticketapply(params.row)
                }
              }
            }, params.row.id)
          }
        },
        {
          title: '申请人',
          width: 150,
          align: 'center',
          key: 'applyUserName'
        },
        {
          title: '业务类型',
          width: 100,
          align: 'center',
          key: 'applyBusiness'
        },
        {
          title: '申请用信项目',
          width: 200,
          align: 'center',
          key: 'creditItemName'
        },
        {
          title: '申请用信金额（元）',
          key: 'applyMoney',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('span', absMoney(params.row.applyMoney))
          }
        },
        {
          title: '申请用信期限',
          width: 120,
          align: 'center',
          key: 'applyDeadline'
        },
        {
          title: '审批用信金额（元）',
          key: 'auditMoney',
          width: 150,
          align: 'right',
          render: (h, params) => {
            return h('span', absMoney(params.row.auditMoney))
          }
        },
        {
          title: '审批用信期限',
          width: 120,
          align: 'center',
          key: 'auditDeadline'
        },
        {
          title: '申请日期',
          align: 'center',
          key: 'createTime',
          width: 150
        },
        {
          title: '流程状态',
          align: 'center',
          width: 100,
          key: 'flowName'
        },
        {
          title: '关联授信金额（元）',
          key: 'creditMoney',
          align: 'right',
          width: 150,
          render: (h, params) => {
            return h('span', absMoney(params.row.creditMoney))
          }
        },
        {
          title: '关联授信ID',
          align: 'center',
          key: 'creditId',
          width: 100,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.myaudit(params.row)
                }
              }
            }, params.row.creditId)
          }
        },
        {
          title: '关联授信申请ID',
          key: 'creditApplyId',
          align: 'center',
          width: 150,
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.view(params.row.creditApplyId, params.row)
                }
              }
            }, params.row.creditApplyId)
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
  },
  methods: {
    ...mapActions([
      'findCreditUseApplyMyList'
    ]),
    getData () {
      this.tableLoading = true
      this.findCreditUseApplyMyList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total } = res.data.data
          this.list = records
          this.total = total
          // this.pages = pages
        }
      })
    },
    view (id, e) {
      const route = {
        name: 'viewapply',
        query: {
          companyId: e.companyId,
          businessTypeId: e.businessTypeId,
          creditApplyId: id,
          flowCode: null
        }
      }
      this.$router.push(route)
    },
    myaudit (row) {
      const route = {
        name: 'myaudit',
        query: {
          id: row.creditId
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
    },
    viewticketapply (row) {
      let route = {
        name: 'viewticketapply',
        query: {
          companyId: row.companyId,
          businessTypeId: row.businessTypeId,
          creditApplyId: row.id,
          flowCode: row.flowCode
        }
      }
      this.$router.push(route)
    },

    editticketapply (row) {
      let route = {
        name: 'editticketapply',
        query: {
          companyId: row.companyId,
          businessTypeId: row.businessTypeId,
          creditApplyId: row.id,
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
