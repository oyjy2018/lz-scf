<template>
  <Select ref="select"
          class="tree-select"
          :value="value"
          size="large">
    <Option style="display: none;"
            v-for="item in list"
            :value="item.value"
            :key="item.value">{{ item.label }}</Option>
    <Tree :data="data"
          @on-select-change="onSelectChange"></Tree>
  </Select>
</template>
<script>
import { returnItem } from '@/libs/util'

export default {
  props: {
    value: {
      type: Array,
      default () {
        return []
      }
    },
    data: {
      type: Array,
      default () {
        return []
      }
    }
  },
  data () {
    return {
      list: []
    }
  },
  watch: {
    data (newData, oldVal) {
      if (newData && newData.length && this.value.length) {
        const id = this.value[0]
        const item = returnItem(newData, id)
        if (item) {
          this.list = [item]
        }
      }
    }
  },
  methods: {
    onSelectChange (select) {
      if (select && select.length) {
        this.list = [{
          value: select[0].id,
          label: select[0].title
        }]
        this.$emit('input', [select[0].id])
      } else {
        this.list = []
        this.$emit('input', [])
      }
    }
  }
}
</script>
