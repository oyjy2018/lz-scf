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
    <Modal width="850"
           :value="show"
           :title="`添加成员`"
           @on-ok="ok"
           @on-cancel="cancel">
      <CheckboxGroup v-model="checkAllGroup">
        <user-role v-if="tree"
                   :tree="tree"></user-role>
      </CheckboxGroup>
    </Modal>
  </div>
</template>

<script>
import UserRole from './userRole.vue'
export default {
  name: 'AddUserRole',
  components: {
    UserRole
  },
  props: {
    show: {
      type: Boolean,
      default: false
    },
    tree: null
  },
  data () {
    return {
      checkAllGroup: []
    }
  },
  methods: {
    ok () {
      this.$emit('handle-ok', { userIds: this.checkAllGroup })
    },
    cancel () {
      this.$emit('handle-cancel')
    }
  }
}
</script>
