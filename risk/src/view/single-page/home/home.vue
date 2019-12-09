<template>
  <div>
    <!-- <Button type="primary"
            size="large"
            @click="modal = true"
            icon="md-add">邀请成员</Button> -->

    <Modal v-model="modal"
           :mask-closable="false"
           scrollable
           footer-hide
           title="邀请成员加入公司">
      <Form ref="formDynamic"
            :model="formDynamic"
            :label-width="80"
            style="width: 300px">
        <FormItem v-for="(item, index) in formDynamic.items"
                  v-if="item.status"
                  :key="index"
                  :prop="'items.' + index + '.value'"
                  :rules="[
                        { required: true, message: '邮箱不能为空', trigger: 'blur' },
                        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
                    ]">
          <Row>
            <Col span="18">
            <Input type="text"
                   v-model="item.value"
                   placeholder="请输入成员邮箱"></Input>
            </Col>
            <Col span="4"
                 offset="1">
            <Button @click="handleRemove(index)" :disabled="handleDisabled">删除</Button>
            </Col>
          </Row>
        </FormItem>
        <FormItem>
          <Row>
            <Col span="12">
            <Button type="dashed"
                    long
                    @click="handleAdd"
                    icon="md-add">添加下一位</Button>
            </Col>
          </Row>
        </FormItem>
        <FormItem>
          <Button @click="handleCancel"
                  type="text"
                  style="margin-right: 18px">暂不邀请</Button>
          <Button type="primary"
                  size="large"
                  @click="handleSubmit('formDynamic')">邀请</Button>

        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'home',
  components: {
  },
  data () {
    return {
      modal: false,
      index: 1,
      formDynamic: {
        items: [
          {
            value: '',
            index: 1,
            status: 1
          }
        ]
      }
    }
  },
  mounted () {
    this.getIsInvite().then(res => {
      if (res && res.status === 200 && res.data.code === 10000) {
        this.modal = res.data.data.isInvite
      }
    })
  },
  computed: {
    handleDisabled: function () {
      return this.formDynamic.items.filter(item => item.status === 1).length === 1
    }
  },
  methods: {
    ...mapActions([
      'getIsInvite',
      'inviteUsers'
    ]),
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let emails = []
          this.formDynamic.items.forEach(item => {
            if (item.status === 1) {
              emails.push(item.value)
            }
          })
          this.inviteUsers({ emails: emails.join(',') }).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.modal = false
              this.$Modal.success({
                title: '提示',
                content: '已向成员邮箱发送邀请邮件'
              })
              this.formDynamic = {
                items: [
                  {
                    value: '',
                    index: 1,
                    status: 1
                  }
                ]
              }
            }
          })
        } else {
          this.$Message.error({
            content: '请填写邮箱',
            duration: 5,
            closable: true
          })
        }
      })
    },
    handleCancel () {
      this.modal = false
    },
    handleReset () {
      this.$refs[name].resetFields()
    },
    handleAdd () {
      this.index++
      this.formDynamic.items.push({
        value: '',
        index: this.index,
        status: 1
      })
    },
    handleRemove (index) {
      this.formDynamic.items[index].status = 0
    }
  }
}
</script>

<style lang="less">
.count-style {
  font-size: 50px;
}
</style>
