<template>
  <div>
    <div>
      <Button type="primary"
              ghost
              style="margin-top: 16px;margin-right: 16px;"
              @click="editModel()">编辑</Button>
      <Button type="primary"
              ghost
              style="margin-top: 16px;"
              @click="goBackList()">返回模型列表</Button>
    </div>
    <p style="margin:20px 0;">模型公式：{{modelFormula}}</p>
    <Table border
           stripe
           :loading="tableLoading"
           :columns="columns1"
           :data="data1">
    </Table>
    <!-- <div class="pagination">
      <Page :total="total"
            :current="form.current"
            :page-size="form.size"
            @on-change="changePage"
            show-elevator
            show-total></Page>
    </div> -->
  </div>

</template>
<script>
import { mapActions, mapMutations } from 'vuex'
export default {
  data () {
    return {
      modelId: '',
      modelFormula: '暂无',
      tableLoading: true,
      inputShow: -1,
      total: 0,
      nameIndex: -1,
      changeMark: 0,
      columns1: [
        {
          title: '评分项',
          width: '200',
          key: 'itemName',
          align: 'center'
        },
        {
          title: '满分分值',
          width: '200',
          key: 'fullMark',
          align: 'center'
        },
        {
          title: '公式',
          key: 'formulaDesc',
          resizable: true,
          align: 'left',
          render: (h, params) => {
            var _html = ''
            var _list = params.row.formulaDesc.split(',')
            _list.forEach(item => {
              var _h2 = item.split('{')
              _html += '<label>' + _h2[0] + '{' + '</label>'
              _html += '<br><label>&nbsp;&nbsp;&nbsp;&nbsp;' + _h2[1].split('}')[0] + '' + '</label>'
              _html += '<br><label>}</label></br>'
            })
            return h('div', {
              domProps: {
                innerHTML: _html
              }
            })
          }
        },
        {
          title: '公式说明',
          key: 'itemExplain',
          render: (h, params) => {
            var _html = ''
            var _data = params.row.itemExplain.split(/\n|\r\n/g)
            _data.forEach(e => {
              _html += '<span>' + e + '</span>'
              _html += '<br>'
            })
            return h('div', {
              domProps: {
                innerHTML: _html
              }
            })
          }
        }
      ],
      data1: []
    }
  },
  mounted () {
    this.modelId = this.$route.query.id
    this.getModelItemData()
    this.handleGetModelFormula()
  },
  methods: {
    ...mapActions([
      'modelDetailItem',
      'removeScore',
      'updatafullMark',
      'getModelFormula'
    ]),
    ...mapMutations([
      'closeTag'
    ]),
    getModelItemData: function () {
      this.modelDetailItem(this.modelId).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          this.data1 = res.data.data
        }
      })
    },
    // 获取模型公式
    handleGetModelFormula: function () {
      const data = {
        id: this.modelId
      }
      this.getModelFormula(data).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.modelFormula = res.data.data.modelFormulaCH === null ? '暂无' : res.data.data.modelFormulaCH
        }
      })
    },
    editModel () {
      this.closeTag({
        name: 'modelDetail',
        query: {
          id: this.modelId
        }
      })
      this.$router.push({ path: '/riskModel/riskControl/model/edit-model', query: { id: this.modelId, current: 0 } })
    },
    goBackList () {
      this.closeTag({
        name: 'modelDetail',
        query: {
          id: this.modelId
        }
      })
      this.$router.push({ name: 'riskControlModelList' })
    },
    editFmark (num, index) {
      this.changeMark = 0
      this.inputShow = index
    }
  }
}
</script>
