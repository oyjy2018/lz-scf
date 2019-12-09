<template>
  <div v-if="isShow">
    <MyFormItem v-if="(item.columnType === 'text' || item.columnType === 'number')"
                :label="item.columnCh"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <template v-if="disabled">
        {{value}}{{item.columnUnit}}
      </template>
      <template v-else>
        <Tooltip v-if="item.columnUnit === '元'"
                 trigger="focus"
                 placement="top-start">
          <Input v-model="value"
                 :placeholder="item.columnTip"
                 :name="item.columnName"
                 type="text"
                 :disabled="disabled || !!item.hasReadOnly"
                 @on-blur="(event) => onBlur(event, index)">
          <span slot="append"
                v-if="item.columnUnit">{{item.columnUnit}}</span>
          </Input>
          <div slot="content">{{ formatNumber }}</div>
        </Tooltip>

        <Input v-else
               v-model="value"
               :placeholder="item.columnTip"
               :name="item.columnName"
               type="text"
               :disabled="disabled || !!item.hasReadOnly"
               @on-blur="(event) => onBlur(event, index)">
        <span slot="append"
              v-if="item.columnUnit">{{item.columnUnit}}</span>
        </Input>
      </template>

    </MyFormItem>
    <MyFormItem v-if="item.columnType === 'textarea'"
                :label="item.columnCh"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <template v-if="disabled">
        {{value}}{{item.columnUnit}}
      </template>
      <template v-else>
        <Input v-model="value"
               :placeholder="item.columnTip"
               :name="item.columnName"
               :type="item.columnType"
               :disabled="disabled || !!item.hasReadOnly"
               :autosize="{minRows: 2,maxRows: 5}"
               @on-blur="(event) => onBlur(event, index)">
        <span slot="append"
              v-if="item.columnUnit">{{item.columnUnit}}</span>
        </Input>
      </template>
    </MyFormItem>
    <MyFormItem v-if="(item.columnType === 'id-card')"
                :label="item.columnCh"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <template v-if="disabled">
        {{value}}{{item.columnUnit}}
      </template>
      <template v-else>
        <Input v-model="value"
               :placeholder="item.columnTip"
               :name="item.columnName"
               type="text"
               :disabled="disabled || !!item.hasReadOnly"
               @on-blur="(event) => onBlur(event, index)">
        <span slot="append"
              v-if="item.columnUnit">{{item.columnUnit}}</span>
        </Input>
      </template>
    </MyFormItem>
    <MyFormItem v-if="(item.columnType === 'one-decimal' || item.columnType === 'two-decimal')"
                :label="item.columnCh"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <template v-if="disabled">
        {{value}}{{item.columnUnit}}
      </template>
      <template v-else>
        <Row>
          <Col span="12">
          <Tooltip v-if="item.columnUnit === '元'"
                   trigger="focus"
                   placement="top-start">
            <Input v-model="value"
                   :placeholder="item.columnTip"
                   :name="item.columnName"
                   type="text"
                   :disabled="disabled || !!item.hasReadOnly"
                   @on-blur="(event) => onBlurDecimal(event, index, item.columnType)">
            <span slot="append"
                  v-if="item.columnUnit">{{item.columnUnit}}</span>
            </Input>
            <div slot="content">{{ formatNumber }}</div>
          </Tooltip>
          <Input v-else
                 v-model="value"
                 :placeholder="item.columnTip"
                 :name="item.columnName"
                 type="text"
                 :disabled="disabled || !!item.hasReadOnly"
                 @on-blur="(event) => onBlurDecimal(event, index, item.columnType)">
          <span slot="append"
                v-if="item.columnUnit">{{item.columnUnit}}</span>
          </Input>
          </Col>
          <Col span="12"
               style="text-align: right">
          <span v-if="item.columnName === 'creditTicketApply_applyMoney'"
                style="">剩余可开商票金额：{{useAbleMoney}}元</span>
          </Col>
        </Row>
      </template>
    </MyFormItem>
    <MyFormItem v-if="item.columnType === 'radio'"
                :label="item.columnCh"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <RadioGroup v-model="value"
                  :placeholder="item.columnTip"
                  :name="item.columnName"
                  @on-change="(value) => onRadioChange(item.columnName, value, index)">
        <Radio v-for="l in item.list"
               :key="l.valueCh"
               :disabled="disabled || !!item.hasReadOnly"
               :label="l.id+''">{{l.valueCh}}
        </Radio>
      </RadioGroup>
    </MyFormItem>
    <MyFormItem v-if="item.columnType === 'select'"
                :label="item.columnCh"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <Select v-model="value"
              :placeholder="item.columnTip"
              :name="item.columnName"
              :disabled="disabled || !!item.hasReadOnly"
              @on-change="(value) => onRadioChange(item.columnName, value, index)">
        <Option v-for="l in item.list"
                :key="l.valueCh"
                :value="l.id+''">{{l.valueCh}}
        </Option>
      </Select>
    </MyFormItem>
    <MyFormItem v-if="item.columnType === 'date'"
                :label="item.columnCh"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <template v-if="disabled">
        {{value}}{{item.columnUnit}}
      </template>
      <template v-else>
        <DatePicker :editable="false"
                    type="date"
                    style="width: 300px"
                    :placeholder="item.columnTip"
                    v-model="value"
                    :disabled="disabled || !!item.hasReadOnly"
                    @on-change="(value) => onRadioChange(item.columnName, value, index)"></DatePicker>
      </template>
    </MyFormItem>
    <MyFormItem v-if="item.columnType === 'select-province'"
                :label="`${item.columnCh}${cityText}`"
                :prop="group ? `${group}[${index}][${item.columnName}]` : `${item.columnName}`"
                :rules="rules">
      <Cascader :data="citys"
                size="large"
                trigger="hover"
                :placeholder="item.columnTip"
                v-model="arr"
                :disabled="disabled || !!item.hasReadOnly"
                @on-change="(value) => onCascaderChange(item.columnName, value, index)"></Cascader>
    </MyFormItem>
  </div>
</template>

<script>
import MyFormItem from '../my-form-item'
import { CITYS } from '@/libs/citys.js'
import { deepCopy, returnRules, idCardValid } from '@/libs/util.js'
import { convertCurrency } from '@/libs/tools'

export default {
  name: 'ApplayForm',
  components: {
    MyFormItem
  },
  data () {
    return {
      value: '',
      arr: [],
      citys: deepCopy(CITYS),
      cityText: '市区'
    }
  },
  props: {
    useAbleMoney: {
      type: Number,
      default: 0
    },
    form: null,
    attrList: {
      type: Array,
      default () {
        return []
      }
    },
    personList: {
      type: Array,
      default () {
        return []
      }
    },
    model: null,
    citysNum: {
      type: Number,
      default: 3
    },
    isRules: {
      type: Boolean,
      default: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    group: {
      type: String,
      default: ''
    },
    index: {
      type: Number,
      default: -1
    },
    item: {
      type: Object,
      default () {
        return {
          businessAttrId: 1,
          businessTypeId: 1,
          classifyName: '',
          columnCh: '',
          columnName: '',
          columnTip: '',
          columnType: 'text',
          companyId: 1,
          hasAuditVisible: 1,
          hasBr: 1,
          hasReadOnly: 0,
          columnErrMsg: '',
          hasCreateNeed: 1,
          hasCreateVisible: 1,
          hasEditVisible: 1,
          hasPlurality: 0,
          hasViewVisible: 1,
          required: 1,
          status: 1,
          verifyFormula: '',
          list: []
        }
      }
    }
  },
  mounted () {
    this.value = this.model
    this.arr = this.model
    if (this.citysNum === 2 && this.item.columnType === 'select-province') {
      this.citys = this.citys.map(item => {
        item.children.map(subItem => {
          delete subItem.children
          return subItem
        })
        return item
      })
      this.cityText = '市 '
    }
  },
  computed: {
    rules () {
      return this.isRules ? returnRules(this.item, this.citysNum) : []
    },
    isShow () {
      let re = true
      if (this.item.preColumnId && this.attrList.length) {
        this.attrList.forEach(item => {
          if (item.businessAttrId === this.item.preColumnId) {
            if (this.group) {
              re = this.form[this.group][this.index][item.columnName] === this.item.preColumnValue
            } else {
              re = this.form[item.columnName] === this.item.preColumnValue
            }
          }
        })
        this.personList.forEach(element => {
          if (element.businessAttrId === this.item.preColumnId) {
            re = this.form[element.columnName] === this.item.preColumnValue
          }
        })
      }
      return re
    },
    formatNumber () {
      if (this.value === '') return `请输入${this.item.columnCh}`
      return convertCurrency(this.value)
    }
  },
  watch: {
    model () {
      this.value = this.model
      this.arr = this.model
    }
  },
  methods: {
    onBlur (event, index) {
      let data = {
        group: this.group,
        tableName: event.target.name,
        value: event.target.value,
        index: index,
        valid: true
      }
      if (this.item.verifyFormula) {
        if (event.target.value === '' && !this.required) {
          data.valid = true
        } else {
          let verifyFormula = new RegExp(this.item.verifyFormula)
          data.valid = verifyFormula.test(event.target.value)
        }
      } else if (this.item.columnType === 'id-card') {
        data.valid = idCardValid(event.target.value)
      }
      this.$emit('on-save', data)
    },
    onBlurDecimal (event, index, columnType) {
      let n = columnType === 'two-decimal' ? 2 : 1
      if (event.target.value !== '') {
        let value = parseFloat(event.target.value).toFixed(n).toString()
        let data = {
          group: this.group,
          tableName: event.target.name,
          value,
          index: index,
          valid: true
        }
        this.value = value
        this.$emit('on-save', data)
      }
    },
    onRadioChange (name, value, index) {
      let data = {
        group: this.group,
        tableName: name,
        value: value,
        index: index,
        valid: true
      }
      this.$emit('on-save', data)
    },
    onCascaderChange (name, value, index) {
      let data = {
        group: this.group,
        tableName: name,
        value: value,
        index: index,
        valid: true
      }
      this.$emit('on-save', data)
    }
  }
}
</script>
