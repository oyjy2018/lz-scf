<style lang="less" scoped>
.title {
  border-bottom: 1px dashed #e9e9e9;
  padding-bottom: 6px;
  margin-bottom: 16px;
  margin-top: 10px;
}
</style>

<template>
  <div>
    <div class="title">
      <b>{{tree.deptName}}</b>
    </div>
    <Checkbox v-for="user in tree.userList"
              :key="user.userId"
              :disabled="user.disabled"
              :label="user.userId">{{user.userName}}({{user.userEmail}})</Checkbox>
    <div style="color: #999"
         v-if="tree.userList && tree.userList.length === 0">暂无数据</div>
    <template v-if="tree.childrenList && tree.childrenList.length">
      <div v-for="item in tree.childrenList"
           :key="item.deptId"
           style="padding-left: 10px">
        <user-role :tree="item"></user-role>
      </div>
    </template>
  </div>
</template>

<script>

export default {
  name: 'UserRole',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    tree: {
      type: Object,
      default () {
        return {}
      }
    }
  },
  methods: {
    handleCheck (e) {
      // console.log(e)
    }
  }
}
</script>
