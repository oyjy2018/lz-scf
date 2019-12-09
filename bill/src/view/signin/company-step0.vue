<style lang="less" scoped>
.products {
  display: flex;
  justify-content: center;
  width: 100%;
  height: 600px;
  &-item {
    position: relative;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 436px;
    height: 505px;
    background: linear-gradient(
      180deg,
      rgba(236, 240, 246, 1) 0%,
      rgba(255, 255, 255, 0.3) 100%
    );
    border-radius: 8px 8px 0px 8px;
    border: 1px solid rgba(236, 240, 246, 1);
    img {
      margin-top: 20px;
      width: 244px;
      height: 160px;
    }
    .h3 {
      margin-top: 15px;
      font-size: 20px;
      font-weight: bold;
      color: #555555;
    }
    .border {
      margin-top: 10px;
      width: 300px;
      height: 1px;
      border-bottom: 1px dashed #9bcdff;
    }
    .h4 {
      margin-top: 32px;
      font-size: 20px;
      color: #555555;
    }
    .h5 {
      font-size: 16px;
      color: #555555;
      img {
        width: 16px;
        height: 14px;
        margin-right: 7px;
      }
    }
    .bottom-img {
      position: absolute;
      z-index: 3;
      bottom: 0;
      right: 0;
      width: 50px;
      height: 50px;
    }
    &:hover {
      box-shadow: 1px 2px 4px 0px rgba(0, 0, 0, 0.4);
      &::after {
        position: absolute;
        z-index: 2;
        bottom: 0;
        right: 0;
        width: 50px;
        height: 50px;
        content: "";
        background: url("../../assets/images/front/g3.png") no-repeat;
        background-size: 50px;
      }
    }
  }
}
</style>

<template>
  <div>
    <div class="products">
      <div class="products-item"
           style="margin-right: 40px"
           @click="systemId1 = !systemId1">
        <img src="../../assets/images/front/pd1.png">
        <div class="h3">票据融资平台</div>
        <div class="border"></div>
        <div class="h4">产品功能</div>
        <div class="h5"><img src="../../assets/images/front/g1.png">商票融资</div>
        <div class="h5"><img src="../../assets/images/front/g1.png">交易见证</div>
        <div class="h5"><img src="../../assets/images/front/g1.png">账户管理</div>
        <img v-show="systemId1"
             class="bottom-img"
             src="../../assets/images/front/g2.png">
      </div>
      <div class="products-item"
           style="margin-right: 40px"
           @click="systemId2 = !systemId2">
        <img src="../../assets/images/front/pd2.png">
        <div class="h3">供应链金融风控平台</div>
        <div class="border"></div>
        <div class="h4">产品功能</div>
        <div class="h5"><img src="../../assets/images/front/g1.png">授信管理</div>
        <div class="h5"><img src="../../assets/images/front/g1.png">风控引擎</div>
        <div class="h5"><img src="../../assets/images/front/g1.png">用信管理</div>
        <div class="h5"><img src="../../assets/images/front/g1.png">账户管理</div>
        <img v-show="systemId2"
             class="bottom-img"
             src="../../assets/images/front/g2.png">
      </div>
    </div>
    <Form class="form"
          style="width: 100%">
      <FormItem style="text-align: center;">
        <Button @click="handleSubmit"
                type="primary"
                long
                style="font-size: 14px; width: 180px;">
          免费试用
        </Button>
      </FormItem>
      <FormItem style="text-align: center;">
        <a style="font-size: 14px;"
           @click="$router.back(-1)">使用已有账户登录</a>
      </FormItem>
    </Form>
  </div>
</template>
<script>
export default {
  name: 'CompanyStep0',
  data () {
    return {
      systemId1: false,
      systemId2: false
    }
  },
  mounted () {
    this.$ba.trackPageview('/bill/signin/company')
  },
  methods: {
    handleSubmit () {
      if (this.systemId1 || this.systemId2) {
        const systemIdList = [] // 1:风控 2:票据
        if (this.systemId2) {
          systemIdList.push(1)
        }
        if (this.systemId1) {
          systemIdList.push(2)
        }
        this.$emit('on-success-valid', {
          systemIdList: systemIdList.join(',')
        })
      } else {
        this.$Message.warning({
          content: '请最少选择一个产品',
          duration: 5,
          closable: true
        })
      }
    }
  }
}
</script>
