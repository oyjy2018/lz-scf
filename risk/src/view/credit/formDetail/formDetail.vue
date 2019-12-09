<style lang="less" scoped>
@import "../../../index.less";
.form-steps-context {
  margin-left: 15px;
  border-left: 1px dashed #ccc;
}
.exp-step-nav {
  position: fixed;
  top: 230px;
  left: 532px;
}
.form-label {
  flex: 1;
  text-align: right;
}
.form-value {
  flex: 1;
  margin-left: 20px;
}
</style>

<template>
  <div class="sention-context">
    <div class="form-steps"
         style="width: 700px;">
      <div class="form-steps-item sention-sub"
           v-for="(item, index) in detail"
           :key="index">
        <Affixs :offset-top="index * 30 + 151"
                v-show="scrollTop < (sention + (index * 50) - 600)"
                container=".content-wrapper">
          <div class="form-steps-title"
               @click="collapse(index)">
            <div class="form-steps-title-num">
              <div><span :class="{'span-active': index === subStep[0]}">{{index+1}}</span></div>
            </div>
            <div class="form-steps-title-text">
              <div style="line-height: 30px;"><span>{{item.classifyName}}</span></div>
            </div>
          </div>
        </Affixs>
        <div class="form-steps-context">
          <template v-for="(subItem, subIndex) in item.attrList">
            <div :key="subItem.columnName+subIndex"
                 style="padding-left: 150px;display:flex;line-height: 34px;">

              <div class="form-label">{{subItem.columnCh}}</div>
              <font v-if="subItem.required"
                    color="red">*</font>
              <div class="form-value"
                   v-if="subItem.attrValObj">
                {{subItem.attrValObj['attrValMap'][subItem.columnVal] && subItem.attrValObj['attrValMap'][subItem.columnVal]['valueCh'] || subItem.columnVa}} {{subItem.columnUnit}}
              </div>
              <div class="form-value"
                   v-else>
                <span v-if="subItem.columnName === 'creditApply_familyAddressProvinceCode'">{{subItem.columnVal | province}}</span>
                <span v-else-if="subItem.columnName === 'creditApply_familyAddressCityCode'">{{subItem.columnVal | city}}</span>
                <span v-else-if="subItem.columnName === 'creditApply_familyAddressRegionCode'">{{subItem.columnVal | region}}</span>
                <span v-else>{{subItem.columnVal}} {{subItem.columnUnit}}</span>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Affixs from '_c/affixs'
import { CITYS } from '@/libs/citys.js'

export default {
  name: 'FormDetail',
  components: {
    Affixs
  },
  props: {
    detail: null,
    scrollTop: null,
    sention: null,
    subStep: null
  },
  filters: {
    province (value) {
      for (let i = 0, len = CITYS.length; i < len; i++) {
        if (CITYS[i].value === value) {
          return CITYS[i].label
        }
      }
    },
    city (value) {
      for (let i = 0, len = CITYS.length; i < len; i++) {
        for (let j = 0, lenj = CITYS[i].children.length; j < lenj; j++) {
          if (CITYS[i].children[j].value === value) {
            return CITYS[i].children[j].label
          }
        }
      }
    },
    region (value) {
      if (value) {
        for (let i = 0, len = CITYS.length; i < len; i++) {
          for (let j = 0, lenj = CITYS[i].children.length; j < lenj; j++) {
            for (let k = 0, lenk = CITYS[i].children[j].children.length; k < lenk; k++) {
              if (CITYS[i].children[j].children[k].value === value) {
                return CITYS[i].children[j].children[k].label
              }
            }
          }
        }
      } else {
        return ''
      }
    }
  },
  methods: {
    collapse (index) {
      this.$emit('on-collapse', 0, index)
    }
  }
}
</script>
