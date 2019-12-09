<style lang="less" scoped>
@import "../../../index.less";
</style>

<template>
  <div class="list-table">
    <Card :bordered="false">
      <div class="buttons">
        <Row>
          <Col span="12">
          <Button type="primary"
                  icon="md-create"
                  :disabled="!selectionData || form.status === 1"
                  @click="auditCompany"
                  ghost>单个处理</Button>
          </Col>
          <Col span="12">
          <RadioGroup v-model="form.status"
                      @on-change="changeStatus"
                      style="float: right;line-height: 30px;">
            <Radio :label="0">待审批</Radio>
            <Radio :label="1">已审批</Radio>
          </RadioGroup>
          </Col>
        </Row>
      </div>
      <Table border
             stripe
             :loading="tableLoading"
             ref="selection"
             :columns="columns"
             :data="list"
             :highlight-row="true"
             @on-selection-change="onSelectionChange"></Table>
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
  name: 'company-audit-table',
  components: {},
  data () {
    return {
      tableLoading: true,
      form: {
        status: 0,
        size: 10,
        current: 1
      },
      total: 0,
      pages: 0,
      list: [],
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '申请公司',
          key: 'companyName',
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.viewDetail(params.row)
                }
              }
            }, params.row.companyName)
          }
        },
        {
          title: '统一社会信用代码',
          key: 'creditCode'
        },
        {
          title: '法人',
          key: 'legalPersonName'
        },
        {
          title: '申请人',
          key: 'porxyPersonName'
        },
        {
          title: '手机号码',
          key: 'porxyPersonPhone'
        },
        {
          title: '风控平台版本',
          key: 'riskSystemVersionName'
        },
        {
          title: '票据交易平台版本',
          key: 'ticketSystemVersionName'
        },
        {
          title: '申请加入时间',
          key: 'createTime'
        },
        {
          title: '状态',
          key: 'status',
          render: (h, params) => {
            const arr = ['待审核', '审核通过', '审核拒绝']
            return h('div', [
              h('span', arr[params.row.status])
            ])
          }
        }
      ],
      selectionData: null
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
      'getCompanyAuditData',
      'companyAudit'
    ]),
    getData () {
      this.tableLoading = true
      this.getCompanyAuditData(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total, pages } = res.data.data
          this.list = records
          this.total = total
          this.pages = pages
        }
      })
    },
    auditCompany () {
      if (this.selectionData) {
        const data = this.selectionData
        this.onCurrentchange(data)
      }
    },
    changeStatus (e) {
      this.getData()
    },
    onCurrentchange (e) {
      const route = {
        name: 'company-audit',
        query: {
          id: e.id,
          name: e.companyName
        }
      }
      this.$router.push(route)
    },
    onSelectionChange (e) {
      // 全选时 不单个处理
      if (e.length === 1) {
        this.selectionData = e[0]
      } else {
        this.selectionData = null
      }
    },
    changePage (current) {
      this.form.current = current
      this.getData()
    },
    viewDetail (row) {
      const route = {
        name: 'company-auditinfo',
        query: {
          id: row.id,
          name: row.companyName
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
