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
        <span>模型名称：</span>
        <Input clearable
               placeholder="输入模型名称搜索"
               class="search-input"
               v-model="form.modelName"
               @on-keyup="enter()"
               style="width: 200px" />
        <span>状态：</span>
        <CheckboxGroup v-model="enabled">
          <Checkbox label="1"><span>已启用</span></Checkbox>
          <Checkbox label="0"><span>已禁用</span></Checkbox>
        </CheckboxGroup>
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                icon="ios-search">搜索</Button>
      </div>
      <div class="buttons" v-if="permission.indexOf('common:riskModel:insert') !== -1">
        <Button style="margin-right: 10px"
                type="primary"
                icon="md-add"
                :to="{path:'./add-model',query: {current: 0}}"
                ghost>添加模型</Button>
      </div>
      <div @mouseleave="activeIndex = -1">
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
                <DropdownMenu slot="list"
                              v-if="permission.indexOf('common:riskModel:update') !== -1
                                  || permission.indexOf('common:riskModel:enabled') !== -1
                                  || permission.indexOf('common:riskModel:delete') !== -1">

                  <DropdownItem v-if="permission.indexOf('common:riskModel:update') !== -1">
                    <Button @click="edit(row)"
                            type="text">修改</Button>
                  </DropdownItem>
                  <DropdownItem v-if="permission.indexOf('common:riskModel:enabled') !== -1">
                    <Button :disabled="!(row.hasEnabled === 0)"
                            @click="handleUpdateModelHasEnabled(row.id,1)"
                            type="text">启用</Button>
                  </DropdownItem>
                  <DropdownItem v-if="permission.indexOf('common:riskModel:enabled') !== -1">
                    <Button :disabled="!(row.hasEnabled === 1)"
                            @click="handleUpdateModelHasEnabled(row.id,0)"
                            type="text">禁用</Button>
                  </DropdownItem>
                  <DropdownItem v-if="permission.indexOf('common:riskModel:delete') !== -1">
                    <Button @click="handleDeleteModel(row.id)"
                            type="text">删除</Button>
                  </DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </div>
          </template>
        </Table>
      </div>
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
import { trimSpace } from '@/libs/tools'

export default {
  name: 'modelList',
  components: {},
  data () {
    return {
      tableLoading: true,
      enabled: [],
      form: {
        hasEnabled: '',
        modelName: '',
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
          width: 200,
          align: 'center',
          key: 'id',
          render: (h, params) => {
            return h('a', {
              on: {
                mouseenter: () => {
                  this.show(params.index)
                },
                click: () => {
                  if (this.permission.indexOf('common:riskModel:detail') !== -1) {
                    this.view(params.row.id)
                  }
                }
              }
            }, params.row.id)
          }
        },
        {
          title: '模型名称',
          align: 'center',
          key: 'modelName'
        },
        {
          title: '所属公司',
          align: 'center',
          key: 'companyName'
        },
        {
          title: '所属平台',
          align: 'center',
          key: 'systemName'
        },
        {
          title: '所属业务',
          align: 'center',
          key: 'businessTypeName'
        },
        {
          title: '状态',
          align: 'center',
          key: 'hasEnabledCH'
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
      'modelList',
      'updateModelHasEnabled',
      'deleteModel'
    ]),
    getData () {
      this.tableLoading = true
      this.form.hasEnabled = this.enabled.length === 1 ? this.enabled[0] : ''
      this.modelList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          const { records, total } = res.data.data
          this.list = records
          this.total = total
        }
      })
    },
    handleUpdateModelHasEnabled (id, value) {
      const data = {
        id: id,
        value: value
      }
      this.$Modal.confirm({
        title: '提示',
        content: value === 1 ? '是否确认启用？' : '是否确认禁用？',
        onOk: () => {
          this.updateModelHasEnabled(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              setTimeout(() => {
                this.$Message.success('操作成功')
              }, 500)
              this.form.current = 1
              this.getData()
            }
          })
        }
      })
    },
    handleDeleteModel (id) {
      this.$Modal.confirm({
        title: '提示',
        content: '是否确认删除？',
        onOk: () => {
          const data = { id: id }
          this.deleteModel(data).then(res => {
            if (res && res.status === 200 && res.data && res.data.code === 10000) {
              setTimeout(() => {
                this.$Message.success('删除成功')
              }, 500)
              this.form.current = 1
              this.getData()
            }
          })
        }
      })
    },
    edit (e) {
      const route = {
        path: '/riskModel/riskControl/model/edit-model',
        query: {
          id: e.id,
          current: 0
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
    },
    view (id) {
      const route = {
        name: 'modelDetail',
        query: {
          id: id
        }
      }
      this.$router.push(route)
    },
    enter () {
      this.form.modelName = trimSpace(this.form.modelName)
    }
  }
}
</script>

<style>
.ivu-menu-vertical.ivu-menu-light:after {
  background: #ffffff;
}
</style>
