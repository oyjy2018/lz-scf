<template>
  <Card :bordered="false">
    <Form class="form"
          style="width: 600px"
          ref="loginForm"
          :model="form"
          :label-width="160"
          @keydown.enter.native="handleSubmit">
      <FormItem label="登录手机号">
        <p>{{ form.phone }}</p>
      </FormItem>
      <FormItem label="登录邮箱">
        <p>{{ form.email }}</p>
      </FormItem>
      <FormItem label="真实姓名"
                prop="userName">
        <Input v-model.trim="form.userName"
               size="large"
               placeholder="请输入真实姓名"></Input>
      </FormItem>
      <FormItem label="所属用户组"
                :rules="{ required: true, type: 'array', min: 1, message: '选择人员加入的用户组', trigger: 'change' }"
                prop="roleIds">
        <Select v-model="form.roleIds"
                size="large"
                multiple
                placeholder="请选择用户组(多选)">
          <Option v-for="item in roleList"
                  :value="item.id"
                  :key="'roleName'+item.id">{{ item.roleName }}</Option>
        </Select>
      </FormItem>
      <FormItem label="并将以上人员加入部门"
                :rules="[{ required: true, type: 'array', min: 1, message: '选择人员加入的部门', trigger: 'change' },
                  { type: 'array', max: 1, message: '一个成员只能有一个部门', trigger: 'change' }]"
                prop="deptIds">
        <tree-select v-model="form.deptIds"
                     :data="deptList"></tree-select>
      </FormItem>
      <FormItem label="数据权限类型"
                prop="permissionType">
        <RadioGroup v-model="form.permissionType">
          <Radio :label="1"
                 :disabled="roleType === '1'">个人</Radio>
          <Radio :label="2"
                 :disabled="roleType === '1'">机构</Radio>
        </RadioGroup>
      </FormItem>
      <FormItem v-if="form.permissionType === 2"
                label="数据权限"
                :rules="{ required: true, type: 'array', min: 1, message: '选择人员的数据权限', trigger: 'change' }"
                prop="permissionOrgIds">
        <Select v-model="form.permissionOrgIds"
                size="large"
                multiple
                placeholder="请选择数据权限(多选)">
          <Option v-for="item in companyList"
                  :value="item.id"
                  :key="item.companyName+item.id">{{ item.companyName }}</Option>
        </Select>
      </FormItem>
      <FormItem>
        <Button @click="handleSubmit"
                type="primary"
                style="width: 150px">保存</Button>
      </FormItem>
    </Form>
  </card>
</template>
<script>
import { mapState, mapMutations, mapActions } from 'vuex'
import { menuToTree, getRoleType } from '@/libs/util'
import TreeSelect from './tree-select.vue'

export default {
  name: 'team-edit',
  components: {
    TreeSelect
  },
  data () {
    return {
      roleType: getRoleType(),
      form: {
        id: '',
        userName: '',
        companyId: '',
        permissionOrgIds: [],
        deptIds: [],
        email: '',
        permissionType: 1,
        roleIds: []
      },
      companyList: [],
      roleList: [],
      deptList: []
    }
  },
  computed: {
    ...mapState({
      permission: state => state.user.permission
    })
  },
  mounted () {
    const query = this.$route.query
    if (query.companyId) {
      const data = { companyId: query.companyId, userId: query.userId }
      this.getUserEidtInfo(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.form = res.data.data
          if (!this.form.permissionType) {
            this.form.permissionType = 1
          }
          this.getData(query.companyId)
        }
      })
    }
  },
  methods: {
    ...mapActions([
      'getCompanyUser',
      'getRoleCompanyUser',
      'getUserEidtInfo',
      'editUserInfo',
      'getDeptSelectList'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getData (companyId) {
      this.getCompanyUser().then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.companyList = res.data.data
        }
      })
      this.getRoleCompanyUser({ companyId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.roleList = res.data.data
        }
      })
      this.getDeptSelectList({ companyId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.deptList = menuToTree(res.data.data)
        }
      })
    },
    handleSubmit () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          const data = Object.assign({}, this.form)
          data.companyId = this.$route.query.companyId
          this.editUserInfo(data).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.closeTag({
                name: 'team-edit',
                query: {
                  companyId: this.$route.query.companyId,
                  userId: this.$route.query.userId,
                  email: this.$route.query.email
                }
              })
              this.$Message.success({
                content: '修改成功',
                duration: 3
              })
              this.$router.push({ name: 'team' })
            }
          })
        }
      })
    }
  }
}
</script>
