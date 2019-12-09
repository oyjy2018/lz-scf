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
        <span>名称：</span>
        <Input clearable
               placeholder="输入名称搜索"
               class="search-input"
               v-model="form.bannerName"
               @on-keyup="trimSpace()"
               style="width: 200px" />
        <span style="margin-left: 20px">状态：</span>
        <CheckboxGroup v-model="enabled">
          <Checkbox label="1"><span>已启用</span></Checkbox>
          <Checkbox label="0"><span>已禁用</span></Checkbox>
        </CheckboxGroup>
        <Button @click="handleSubmit"
                class="search-btn"
                type="primary"
                style="margin-left: 20px"
                icon="ios-search">搜索</Button>
      </div>
      <div class="buttons">
        <Button v-if="functionControl.add"
                type="primary"
                icon="md-add"
                :to="{name:'addBanner'}"
                ghost>添加Banner</Button>
      </div>
      <Table ref="selection"
             border
             stripe
             @mouseleave.native="activeIndex = -1"
             :loading="tableLoading"
             :columns="columns"
             :data="list"
             :draggable="true"
             @on-drag-drop="reSort"
             :highlight-row="true">
        <template slot-scope="{ row, index }"
                  slot="icon">
          <div @mouseenter="showIcon(index)">
            <Dropdown placement="bottom-start">
              <Icon type="md-menu" />
              <a href="javascript:void(0)" style="margin-left: 10px">
                <Icon v-show="index === activeIndex"
                      size="20"
                      type="ios-settings"
                      class="table-icon-setting"></Icon>
              </a>
              <DropdownMenu slot="list">
                <DropdownItem v-if="functionControl.update">
                  <Button @click="edit(row)"
                          type="text">修改</Button>
                </DropdownItem>
                <DropdownItem v-if="functionControl.updateUseStatus && row.useStatus === 0">
                  <Button @click="handleUpdateBannerUseStatus(row.id,1)"
                          type="text">启用</Button>
                </DropdownItem>
                <DropdownItem v-if="functionControl.updateUseStatus && row.useStatus === 1">
                  <Button @click="handleUpdateBannerUseStatus(row.id,0)"
                          type="text">禁用</Button>
                </DropdownItem>
                <DropdownItem v-if="functionControl.delete">
                  <Button @click.native="handleDeleteBanner(row.id)"
                          type="text">删除</Button>
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </template>
      </Table>
      <div class="pagination">
         共{{total}}条数据
      </div>
    </Card>
    <viewer>
      <div class="detailed">
        <img class="viewerImg"
             id="viewerImgId"
             src=""
             style="display: none;width: 1px;height: 1px"
             alt="">
      </div>
    </viewer>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { trimSpace } from '@/libs/tools'

export default {
  name: 'bannerPage',
  components: {},
  data () {
    return {
      tableLoading: true,
      enabled: [],
      functionControl: {
        list: false,
        add: false,
        delete: false,
        update: false,
        updateUseStatus: false,
        sort: false
      },
      form: {
        useStatus: '',
        bannerName: '',
        size: 10,
        current: 1
      },
      total: 0,
      list: [],
      activeIndex: -1,
      idsSort: '',
      columns: [
        {
          title: '',
          width: 80,
          align: 'left',
          slot: 'icon'
        },
        {
          title: '名称',
          align: 'center',
          key: 'bannerName'
        },
        {
          title: '点击Banner后跳转链接',
          align: 'center',
          key: 'jumpUrl'
        },
        {
          title: '图片缩略图',
          align: 'center',
          key: 'bannerFileUrl',
          render: (h, params) => {
            let html = `<img style="width: 20px;height: 20px" src="${params.row.bannerFileUrl}"/>`
            return h('div', {
              on: {
                click: () => {
                  document.getElementById('viewerImgId').src = params.row.bannerFileUrl
                  document.getElementById('viewerImgId').click()
                }
              },
              domProps: {
                innerHTML: html
              }
            })
          }
        },
        {
          title: '启用状态',
          align: 'center',
          key: 'useStatus',
          render: (h, params) => {
            return h('div', params.row.useStatus === 1 ? '已启用' : '已禁用')
          }
        },
        {
          title: '创建日期',
          align: 'center',
          key: 'createTime'
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
    this.getFunctionControl()
    this.getData()
  },
  methods: {
    ...mapActions([
      'findBannerList',
      'deleteBanner',
      'updateBannerUseStatus',
      'bannerSort'
    ]),
    getFunctionControl () {
      // 列表的权限
      if (this.permission.indexOf('operationsMgt:banner:list') !== -1) {
        this.functionControl.list = true
      }
      // 新增的权限
      if (this.permission.indexOf('operationsMgt:banner:add') !== -1) {
        this.functionControl.add = true
      }
      // 删除的权限
      if (this.permission.indexOf('operationsMgt:banner:delete') !== -1) {
        this.functionControl.delete = true
      }
      // 修改的权限
      if (this.permission.indexOf('operationsMgt:banner:update') !== -1) {
        this.functionControl.update = true
      }
      // 排序的权限
      if (this.permission.indexOf('operationsMgt:banner:sort') !== -1) {
        this.functionControl.sort = true
      }
      // 启用/禁用的权限
      if (this.permission.indexOf('operationsMgt:banner:updateUseStatus') !== -1) {
        this.functionControl.updateUseStatus = true
      }
    },
    getData () {
      // 无权限
      if (!this.functionControl.list) {
        return
      }
      this.tableLoading = true
      this.form.useStatus = this.enabled.length === 1 ? this.enabled[0] : ''
      this.findBannerList(this.form).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          this.list = res.data.data
          this.total = this.list.length
        }
      })
    },
    handleUpdateBannerUseStatus (id, value) {
      const data = {
        id: id,
        value: value
      }
      this.$Modal.confirm({
        title: '提示',
        content: value === 1 ? '是否确认启用？' : '是否确认禁用？',
        onOk: () => {
          this.updateBannerUseStatus(data).then(res => {
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
    handleDeleteBanner (id) {
      this.$Modal.confirm({
        title: '提示',
        content: '是否确认删除？',
        onOk: () => {
          const data = { id: id }
          this.deleteBanner(data).then(res => {
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
        name: 'addBanner',
        query: {
          id: e.id
        }
      }
      this.$router.push(route)
    },
    // 列表重新排序  index是当前元素下标，toIndex是拖动到的位置下标
    reSort (index, toIndex) {
      console.log(index, toIndex)
      console.log(this.functionControl.sort)
      // 无权限
      if (!this.functionControl.sort) {
        return
      }
      let id = this.list[index].id
      let toId = this.list[toIndex].id
      let direction = ''
      if (index > toIndex) { // 上移
        direction = 'up'
        let temp = this.list[index]
        this.list.splice(index, 1) // 先删除当前元素
        this.list.splice(toIndex, 0, temp) // 再把当前元素放到目标位置
      } else { // 下移
        direction = 'down'
        this.list.splice(toIndex + 1, 0, this.list[index]) // 先将当前元素放在目标位置的后一位
        this.list.splice(index, 1) // 再删除当前元素
      }
      // 后端排序
      let data = {
        id: id,
        toId: toId,
        direction: direction
      }
      this.bannerSort(data).then(res => {
        // console.log(res)
      })
    },
    trimSpace () {
      this.form.bannerName = trimSpace(this.form.bannerName)
    },
    showIcon (index) {
      this.activeIndex = index
    },
    handleSubmit () {
      this.form.current = 1
      this.getData()
    }
  }
}
</script>
