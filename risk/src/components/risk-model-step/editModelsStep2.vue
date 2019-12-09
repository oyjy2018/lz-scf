<template>
  <div>
    <p style="margin:20px 0;">评分项基本定义</p>
    <Button icon="md-add"
            type="primary"
            ghost
            style="margin-bottom: 16px;"
            :to="{path:'./add-score',query:{id:this.modelId,type:'editModel'}}">添加评分项
    </Button>
    <div @mouseleave="activeIndex = -1">
      <Table border
             stripe
             :loading="tableLoading"
             :columns="columns1"
             :data="data1">
        <template slot-scope="{ row, index }"
                  slot="icon">
          <div>
            <Dropdown placement="bottom-start">
              <a href="javascript:void(0)">
                <Icon size="20"
                      type="ios-create-outline"
                      class="table-icon-setting"
                      @click="edit(row.id)"></Icon>
              </a>
              <a href="javascript:void(0)">
                <Icon size="20"
                      type="ios-close"
                      class="table-icon-setting"
                      @click="remove(row.id)"></Icon>
              </a>
            </Dropdown>
          </div>
        </template>
        <template slot-scope="{ row, index }"
                  slot="fullMark">
          <div @mouseenter="show(index)">
            <Dropdown placement="bottom-start">
              <div style="line-height: 50px;">
                <span style="margin-right:20px;">{{data1[index].fullMark}}</span>
                <a style="vertical-align: top;line-height: 45px;"
                   href="javascript:void(0)">
                  <span>
                    <Icon v-show="index === activeIndex"
                          size="20"
                          type="ios-create-outline"
                          class="table-icon-setting"
                          @click="editFmark(data1[index].fullMark,index)"></Icon>
                  </span>
                </a>
              </div>
              <input v-show="inputShow === index"
                     style="position: absolute;width: 125px;top: 13px;left: -12px;"
                     type="text"
                     @input="handleInput"
                     v-model='changeMark'
                     @blur="handleUpdateFullMark(row.id)">
            </Dropdown>
          </div>
        </template>
      </Table>
    </div>
    <Button style="margin-top: 16px;margin-right: 16px;"
            type="primary"
            @click="next()">下一步</Button>
    <Button style="margin-top: 16px;"
            @click="cancel()">取消</Button>
  </div>

</template>
<script>
import { mapActions, mapMutations } from 'vuex'
export default {
  data () {
    return {
      modelId: '',
      tableLoading: true,
      activeIndex: -1,
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
          slot: 'fullMark'
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
        },
        {
          title: '操作',
          width: '200',
          align: 'center',
          slot: 'icon'
        }
      ],
      data1: []
    }
  },
  mounted () {
    let Id = this.$route.query.id
    this.modelId = Id
    this.getModelItemnData()
  },
  methods: {
    ...mapActions(['modelDetailItem', 'removeScore', 'updatafullMark']),
    ...mapMutations([
      'closeTag'
    ]),
    handleInput (event) {
      this.changeMark = event.target.value.replace(/[^\d.]/g, '')
    },
    getModelItemnData: function () {
      this.modelDetailItem(this.modelId).then(res => {
        this.tableLoading = false
        if (res && res.status === 200 && res.data.code === 10000) {
          this.data1 = res.data.data
        }
      })
    },
    show (index) {
      this.activeIndex = index
    },
    editFmark (num, index) {
      this.changeMark = this.data1[index].fullMark
      this.inputShow = index
      this.activeIndex = -1
    },
    handleUpdateFullMark (riskScoreItemId) {
      this.inputShow = -1
      this.updatafullMark({ riskScoreItemId, fullMark: this.changeMark }).then(res => {
        if (res && res.status === 200 && res.data.code === 10000) {
          this.getModelItemnData()
          this.inputShow = -1
        }
      })
    },
    edit: function (id) {
      this.$router.push({ path: '/riskModel/riskControl/model/edit-score', query: { scoreId: id, id: this.modelId } })
    },
    remove (scoreId) {
      this.$Modal.confirm({
        title: '提示',
        content: `是否确认删除？`,
        onOk: () => {
          this.removeScore(scoreId).then(res => {
            if (res && res.status === 200 && res.data.code === 10000) {
              this.$Message.success('删除成功!')
              this.getModelItemnData()
            }
          })
        },
        onCancel: () => {
          this.$Message.info('取消删除')
        }
      })
    },
    next () {
      this.cancel()
      setTimeout(() => {
        this.$router.push({ path: '/riskModel/riskControl/model/edit-model', query: { id: this.modelId, current: 2 } })
      }, 10)
    },
    cancel () {
      this.closeTag({
        name: this.$route.name,
        query: {
          id: this.modelId,
          current: 1
        }
      })
    }
  }
}
</script>
<style lang="less">
</style>
