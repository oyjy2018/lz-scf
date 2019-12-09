<template>
  <div class="team-leftmenu">
    <Menu theme="light"
          ref="menu1"
          :active-name="roleActiveName"
          :open-names="openname"
          @on-select="onSelectSubmenu"
          accordion
          style="width: 100%;">
      <Submenu v-for="(item, index) in roleGroupList"
               :key="item.id"
               :name="index+''">
        <template slot="title">
          <div style="display:flex;">
            <span style="width: 80%;display:inline-block;overflow:hidden;
          text-overflow: ellipsis;
          white-space: nowrap">{{ item.companyName }}</span>
            <Dropdown :transfer="true">
              <a href="javascript:void(0)"
                 @click.stop.prevent="">
                <Icon type="ios-settings"
                      class="table-icon-setting"></Icon>
              </a>
              <DropdownMenu slot="list">
                <DropdownItem>
                  <div @click.stop.prevent="addModal(item)">添加用户组</div>
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
        </template>
        <div v-for="(dept, dIndex) in item.roles"
             :key="dept.id">
          <MenuItem :name="index + '-' + dIndex">
          <span style="width: 80%;display:inline-block">{{ dept.roleName }}</span>
          <Dropdown :transfer="true"
                    v-if="dept.roleType !== 0">
            <a href="javascript:void(0)"
               @click.stop.prevent="">
              <Icon type="ios-settings"
                    class="table-icon-setting"></Icon>
            </a>
            <DropdownMenu slot="list">
              <DropdownItem v-if="dept.isEditPrivilege === 1">
                <div @click.stop.prevent="editModal(item, dept)">重命名</div>
              </DropdownItem>
              <DropdownItem v-if="dept.isDelete === 1">
                <div @click.stop.prevent="deleteModal(dept)">删除</div>
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
          </MenuItem>
        </div>
      </Submenu>
    </Menu>
  </div>
</template>

<script>

export default {
  name: 'TeamLeft',
  props: {
    openname: {
      type: Array,
      default () {
        return []
      }
    },
    roleActiveName: {
      type: String,
      default: ''
    },
    roleGroupList: {
      type: Array,
      default () {
        return []
      }
    }
  },
  watch: {
    roleActiveName (value, oldVal) {
      if (value !== oldVal) {
        this.$nextTick(() => {
          this.$refs.menu1.updateOpened()
          this.$refs.menu1.updateActiveName()
        })
      }
    }
  },

  methods: {
    init () {
      this.form = {
        id: '',
        companyId: '',
        roleName: ''
      }
    },
    onSelectSubmenu (name) {
      this.$emit('on-select-submenu', name)
    },
    addModal (item) {
      this.$emit('add-modal', item)
    },
    editModal (item, dept) {
      this.$emit('edit-modal', item, dept)
    },
    deleteModal (dept) {
      this.$emit('delete-modal', dept)
    }
  }
}
</script>
