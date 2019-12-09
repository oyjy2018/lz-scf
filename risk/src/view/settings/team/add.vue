<template>
  <Card :bordered="false">
    <p slot="title">添加成员 - {{companyName}}</p>
    <Form class="form"
          style="width: 600px"
          ref="loginForm"
          :model="form"
          :label-width="160"
          @submit.native.prevent="handleSubmit">
      <FormItem label="邮箱邀请"
                :rules="{ required: true, message: '请输入邮箱', trigger: 'change' }"
                prop="emails">
        <p style="color: #f67373">通过邮箱添加新成员，可输入多个邮箱，空格或回车分隔</p>
        <Input v-model.trim="form.emails"
               type="textarea"
               :rows="4"
               placeholder="请输入邮箱"
               size="large"
               style="text-algin: center" />
      </FormItem>
      <FormItem label="并将以上人员加入用户组"
                :rules="{ required: true, type: 'array', min: 1, message: '选择人员加入的用户组', trigger: 'change' }"
                prop="roleIds">
        <Select v-model="form.roleIds"
                size="large"
                multiple
                placeholder="请选择用户组(多选)">
          <Option v-for="item in roleList"
                  :value="item.id"
                  :key="item.id">{{ item.roleName }}</Option>
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
                label="以上人员的数据权限"
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
                style="width: 150px">发送邮件邀请</Button>
      </FormItem>
    </Form>
  </card>
</template>
<script>
import { mapState, mapMutations, mapActions } from 'vuex'
import { validateEmail, menuToTree, getRoleType } from '@/libs/util'
import TreeSelect from './tree-select.vue'

export default {
  name: 'team-add',
  components: {
    TreeSelect
  },
  data () {
    return {
      roleType: getRoleType(),
      companyId: '',
      companyName: '',
      form: {
        companyId: '',
        permissionOrgIds: [],
        deptIds: [],
        emails: '',
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
    // rules () {
    //   if (this.form.permissionType === 1) {
    //     return {
    //       emails: [
    //         { required: true, message: '邮箱不能为空', trigger: 'blur' }
    //       ],
    //       deptIds: [
    //         { required: true, type: 'array', min: 1, message: '选择以上人员加入的部门', trigger: 'change' },
    //         { type: 'array', max: 1, message: '一个成员只能有一个部门', trigger: 'change' }
    //       ],
    //       // permissionType: [
    //       //   { required: true, message: '请选择数据权限类型', trigger: 'change' }
    //       // ],
    //       roleIds: [
    //         { required: true, type: 'array', min: 1, message: '选择以上人员加入的用户组', trigger: 'change' }
    //       ]
    //     }
    //   } else {
    //     return {
    //       emails: [
    //         { required: true, message: '邮箱不能为空', trigger: 'blur' }
    //       ],
    //       // permissionType: [
    //       //   { required: true, message: '请选择数据权限类型', trigger: 'change' }
    //       // ],
    //       permissionOrgIds: [
    //         { required: true, type: 'array', min: 1, message: '选择人员的数据权限', trigger: 'change' }
    //       ],
    //       deptIds: [
    //         { required: true, type: 'array', min: 1, message: '选择以上人员加入的部门', trigger: 'change' },
    //         { type: 'array', max: 1, message: '一个成员只能有一个部门', trigger: 'change' }
    //       ],
    //       roleIds: [
    //         { required: true, type: 'array', min: 1, message: '选择以上人员加入的用户组', trigger: 'change' }
    //       ]
    //     }
    //   }
    // }
  },
  mounted () {
    const query = this.$route.query
    if (query.companyId) {
      this.companyId = query.companyId
      this.companyName = query.companyName
      if (query.deptId) {
        this.form.deptIds.push(parseInt(query.deptId))
      }
      this.getData()
    }
  },
  methods: {
    ...mapActions([
      'getCompanyUser',
      'getRoleCompanyUser',
      'getDeptSelectList',
      'addUserList'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getData () {
      this.getCompanyUser().then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.companyList = res.data.data
        }
      })
      this.getRoleCompanyUser({ companyId: this.companyId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.roleList = res.data.data
        }
      })
      this.getDeptSelectList({ companyId: this.companyId }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.deptList = menuToTree(res.data.data)
        }
      })
    },
    handleSubmit () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          const data = Object.assign({}, this.form)
          data.emails = data.emails.replace(/\n/g, ' ').split(' ').filter(function (s) {
            return s && s.trim() // 去除空数组 注：IE9(不包含IE9)以下的版本没有trim()方法
          })
          for (let i = 0, len = data.emails.length; i < len; i++) {
            if (!validateEmail(data.emails[i])) {
              return this.$Message.warning({
                content: data.emails[i] + '邮箱格式不正确',
                duration: 5,
                closable: true
              })
            }
          }
          data.companyId = this.companyId
          this.addUserList(data).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success({
                content: '邀请成功',
                duration: 3
              })
              this.form = {
                companyId: '',
                permissionOrgIds: [],
                deptIds: [],
                emails: '',
                permissionType: 0,
                roleIds: []
              }
              this.closeTag({
                name: 'team-add',
                query: {
                  companyId: this.$route.query.companyId,
                  companyName: this.$route.query.companyName,
                  deptId: this.$route.query.deptId
                }
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
