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
        <span>项目合同编号：</span>
        <Input clearable
               class="search-input"
               v-model="form.contractNo"
               style="width: 150px"/>&nbsp;
        <span>项目名称：</span>
        <Input clearable
               class="search-input"
               v-model="form.projectName"
               style="width: 150px" />&nbsp;
        <span>项目经理：</span>
        <Input clearable
               class="search-input"
               v-model="form.businessManager"
               style="width: 150px" />&nbsp;
        <span>项目状态：</span>
        <Input clearable
               class="search-input"
               v-model="form.projectStatus"
               style="width: 150px" />&nbsp;
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
  name: 'LzProjectList',
  data () {
    return {
      tableLoading: true,
      form: {
        contractNo: '',
        projectName: '',
        businessManager: '',
        projectStatus: '',
        size: 10,
        current: 1
      },
      total: 0,
      pages: 0,
      list: [],
      columns: [
        {
          title: '项目合同编号',
          align: 'center',
          key: 'contractNo',
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  if (this.permission.indexOf('common:lzProject:basicInfo') !== -1) {
                    this.goToDetail(params.row.contractNo)
                  }
                }
              }
            }, params.row.contractNo)
          }
        },
        {
          title: '项目名称',
          align: 'center',
          key: 'projectName'
        },
        {
          title: '项目类型',
          align: 'center',
          key: 'projectType'
        },
        {
          title: '甲方全称',
          align: 'center',
          key: 'firstPartyFullName'
        },
        {
          title: '乙方全称',
          align: 'center',
          key: 'secondPartyFullName'
        },
        {
          title: '项目经理',
          align: 'center',
          key: 'businessManager'
        },
        {
          title: '经营模式',
          align: 'center',
          key: 'businessModel'
        },
        {
          title: '计划开工日期',
          align: 'center',
          key: 'planCommenceDate'
        },
        {
          title: '项目状态',
          align: 'center',
          key: 'projectStatus'
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
      'lzProjectList'
    ]),
    getData () {
      this.tableLoading = true
      this.lzProjectList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total } = res.data.data
          this.list = records
          this.total = total
        }
      })
    },
    goToDetail (value) {
      const route = {
        name: 'riskDataLzProjectDetail',
        query: {
          contractNo: value
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
