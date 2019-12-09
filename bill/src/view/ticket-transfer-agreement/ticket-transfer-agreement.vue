<style lang="less" scoped>
.center {
  padding: 10px 15px;
  h2 {
    text-align: center;
  }
}
.bottom {
  margin: 20px 0 40px 0;
  text-align: center;
  button {
    width: 116px;
    height: 38px;
    font-size: 14px;
  }
}
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1100;
  background-color: rgba(55, 55, 55, 0.6);
}
.card {
  width: 520px;
  .buttons {
    text-align: right;
  }
}
</style>

<template>
  <div>
    <Modal v-model="modal"
           width="980px"
           scrollable
           title="签署合同"
           @on-cancel="handleClose">
      <Icon class="close"
            slot="close"
            type="md-close"
            size="20"
            @click="handleClose" />
      <div class="center"
           v-if="contractFileUrlView">
        <iframe :src="contractFileUrlView"
                frameborder="0"
                width="100%"
                height="600px"></iframe>
      </div>
      <div class="bottom"
           slot="footer">
        <div class="bottom-buttons">
          <Button type="primary"
                  style="margin-left: 40px;"
                  @click="handleOk">签署</Button>
        </div>
      </div>
    </Modal>
    <Modal v-model="modalKeyWord"
           draggable
           scrollable>
      <div slot="header"
           class="title">请输入合同中盖章的位置的关键字</div>
      <div class="modal-content">
        <Input v-model="keyWord"
               :maxlength="10"
               placeholder="请输入1~10个字且在合同中只出现了一次"
               style="width: 350px">
        <Button slot="append"
                type="text"
                @click="handleView">查看示例</Button>
        </Input>
      </div>
      <div class="buttons"
           slot="footer">
        <Button @click="handleOnlyKeyWord"
                :loading="loading"
                :disabled="keyWord === ''"
                type="primary">确定</Button>
        <Button @click="modalKeyWord = false"
                style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
    <Modal v-model="modalCode"
           draggable
           scrollable>
      <template v-if="signType === 0">
        <div slot="header"
             class="title">请输入短信验证码，完成企业盖章签署</div>
        <div class="modal-content">
          <Input v-model="code"
                 :maxlength="6"
                 style="width: 250px">
          <Button slot="append"
                  :disabled="auth_time !== 0"
                  type="text"
                  @click="handelSendCode('2')">{{auth_time ? '重新发送('+auth_time+'s)' : '获取短信验证码'}}</Button>
          </Input>
          <br>
          <p>如有疑问，请咨询平台客服</p>
        </div>
        <div class="buttons"
             slot="footer">
          <Button @click="handleOk2"
                  :loading="loading"
                  type="primary"><span v-if="!loading">确定</span>
            <span v-else>Loading...</span></Button>
          <Button @click="modalCode = false"
                  style="margin-left: 8px">取消</Button>
        </div>
      </template>
      <template v-else>
        <div slot="header"
             class="title">请输入短信验证码，完成授权人签署</div>
        <div class="modal-content">
          <Input v-model="code"
                 :maxlength="6"
                 style="width: 250px">
          <Button slot="append"
                  :disabled="auth_time !== 0"
                  type="text"
                  @click="handelSendCode('1')">{{auth_time ? '重新发送('+auth_time+'s)' : '获取短信验证码'}}</Button>
          </Input>
          <br>
          <p>如有疑问，请咨询平台客服</p>
        </div>
        <div class="buttons"
             slot="footer">
          <Button @click="handleOk1"
                  :loading="loading"
                  type="primary"><span v-if="!loading">确定</span>
            <span v-else>Loading...</span></Button>
          <Button @click="modalCode = false"
                  style="margin-left: 8px">取消</Button>
        </div>
      </template>
    </Modal>
    <Modal draggable
           scrollable
           :zIndex="1100"
           v-model="visible"
           width="980px"
           :footer-hide="true"
           title="查看示例">
      <img :src="viewUrl"
           v-if="visible"
           style="width: 100%; height: 90%">
    </Modal>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import ht from '@/assets/images/example/ht.png'

export default {
  name: 'tickettransferagreement',
  props: {
    modal: {
      type: Boolean,
      default: false
    },
    party: {
      type: String,
      default: 'a'
    }
  },
  data () {
    return {
      viewUrl: '',
      visible: false,
      contractType: null,
      keyWord: '',
      modalKeyWord: false,
      loading: false,
      auth_time: 0,
      auth_timetimer: null,
      modalCode: false,
      signType: '',
      id: '',
      code: '',
      signParty: 'a1', // (a1:甲方个人,a2:甲方企业,b1:乙方个人,b2:乙方企业)
      contractFileUrlView: ''
    }
  },
  destroyed () {
    clearInterval(this.auth_timetimer)
  },
  methods: {
    ...mapActions([
      'getContractContent',
      'sendCode',
      'signContract',
      'isOnlyKeyWord'
    ]),
    getContent (id, type, contractType, contractFileUrlView) {
      if (id) {
        this.id = id
        this.signType = type
        this.contractType = contractType
        this.code = ''
        this.contractFileUrlView = contractFileUrlView
        // const data = {
        //   id
        // }
        // this.getContractContent(data).then(res => {
        //   if (res && res.status === 200 && res.data && res.data.code === 10000) {
        //     this.form = res.data.data
        //   }
        // })
      }
    },
    handleView () {
      this.viewUrl = ht
      this.visible = true
    },
    handleClose () {
      this.$emit('on-click')
    },
    setInTime () {
      this.auth_time = 60
      this.auth_timetimer = setInterval(() => {
        this.auth_time--
        if (this.auth_time <= 0) {
          clearInterval(this.auth_timetimer)
        }
      }, 1000)
    },
    handleOnlyKeyWord () {
      const data = {
        orderId: this.id,
        keyWord: this.keyWord
      }
      this.isOnlyKeyWord(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.modalKeyWord = false
          this.modalCode = true
        }
      })
    },
    handleOk () {
      if (this.contractType === '0') {
        this.modalCode = true
      } else {
        this.modalKeyWord = true
      }
    },
    handelSendCode (type) {
      const data = {
        orderId: this.id,
        signParty: this.party + type
      }
      this.sendCode(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.$Message.success('短信发送成功')
          this.setInTime()
        } else {
          // this.modal = false
          this.modalCode = false
          this.$emit('on-click')
        }
      })
    },
    handleOk1 () {
      if (this.code === '') {
        return this.$Message.warning({
          content: '请输入验证码',
          duration: 5,
          closable: true
        })
      }
      this.loading = true
      const data = {
        orderId: this.id,
        signParty: this.party + '1',
        code: this.code,
        keyWord: this.keyWord
      }
      this.signContract(data).then(res => {
        this.loading = false
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.auth_time = 0
          // this.modal = false
          this.modalCode = false
          this.code = ''
          this.keyWord = ''
          this.$Message.success({
            content: '完成授权人签署',
            duration: 10,
            closable: true
          })
          clearInterval(this.auth_timetimer)
          this.$emit('on-click', res.data.data)
        } else {
          // this.modal = false
          this.modalCode = false
          this.$emit('on-click')
        }
      })
    },
    handleOk2 () {
      if (this.code === '') {
        return this.$Message.warning({
          content: '请输入验证码',
          duration: 5,
          closable: true
        })
      }
      this.loading = true
      const data = {
        orderId: this.id,
        signParty: this.party + '2',
        code: this.code,
        keyWord: this.keyWord
      }
      this.signContract(data).then(res => {
        if (res && res.status === 200 && res.data && res.data.code === 10000) {
          this.auth_time = 0
          this.$Message.success({
            content: '完成企业盖章签署',
            duration: 10,
            closable: true
          })
          // this.modal = false
          this.modalCode = false
          this.code = ''
          this.keyWord = ''
          this.$emit('on-click', res.data.data)
          clearInterval(this.auth_timetimer)
        } else {
          // this.modal = false
          this.modalCode = false
          this.$emit('on-click')
        }
        this.loading = false
      })
    }
  }
}
</script>
