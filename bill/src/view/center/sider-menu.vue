<template>
  <div>
    <Menu ref="menu"
          :active-name="activeName"
          theme="light"
          width="auto"
          @on-select="handleSelect">
      <template v-for="item in list">
        <template v-if="item.children && item.children.length ">
          <MenuGroup :name="item.name"
                     :key="`menu-${item.name}`"
                     :title="item.meta.title">
            <template slot="title">
              <Icon :type="item.meta.icon"></Icon>
              {{item.meta.title}}
            </template>
            <template v-for="child in item.children">
              <MenuItem v-if="!child.meta.hideInMenu && clistArry.indexOf(child.name) !== -1"
                        :to="child.path ? child.path : child.meta.href"
                        :name="child.name"
                        :key="`menu-${child.name}`">
              <Icon :custom="`iconfont ${child.meta.icon}`"
                    :key="`menu-${child.name}`"></Icon>
              {{child.meta.title}}
              </MenuItem>
            </template>
          </MenuGroup>
        </template>
        <template v-else>
          <MenuItem :to="item.path ? item.path : item.meta.href"
                    :target="item.path ? '_self' : '_blank'"
                    :name="item.name"
                    :key="`menu-${item.name}`">
          <Icon :type="item.meta.icon"
                :key="`menu-${item.name}`"></Icon>
          {{item.meta.title}}
          </MenuItem>
        </template>
      </template>
    </Menu>
  </div>
</template>
<script>
import routers from '@/router/routers'
import { getAccess } from '@/libs/util'
export default {
  name: 'SiderMenu',
  data () {
    return {
      clistArry: [],
      menuName: '',
      firstMenuName: ''
    }
  },
  mounted () {
    this.activeFirstMenu()
  },
  created () {
    // console.log(111)
  },
  methods: {
    handleSelect (name) {
      this.$emit('on-select', name)
    },
    Menulistn () {
      this.clistArry = []
      // const mpath = this.$route.matched[0].path
      const access = getAccess()
      this.menuList.forEach((mlist, i) => {
        // if (mlist.name.substring(1) === mpath.substring(1)) {
        //   access.forEach((alist, m) => {
        //     mlist.children.forEach((clist, i) => {
        //       this.clistArry.push(clist.name)
        //       return alist
        //     })
        //   })
        // }
        access.forEach((alist, m) => {
          if (mlist.meta.title === alist.split('-')[0]) {
            if (mlist.children) {
              mlist.children.forEach((clist, i) => {
                if (clist.meta.title === alist.split('-')[1]) {
                  this.clistArry.push(clist.name)
                }
              })
            }
          }
        })
      })
    },
    activeFirstMenu () {
      if (this.clistArry.length > 0 && this.list.length > 0) {
        for (let item of this.list) {
          if (item.children && item.children.length > 0) {
            for (let child of item.children) {
              console.log(child.name.substring(0, 1))
              if (this.firstMenuName === '' && !child.meta.hideInMenu && child.name.substring(0, 1) !== '_' && this.clistArry.indexOf(child.name) !== -1) {
                this.$router.push({ name: child.name })
                return
              }
            }
          } else {
            if (this.firstMenuName === '' && !item.meta.hideInMenu) {
              if (item.name === 'doc') {
                this.$router.push({ name: child.name })
                return
              } else {
                if (this.clistArry.indexOf(item.name) !== -1) {
                  this.$router.push({ name: child.name })
                  return
                }
              }
            }
          }
        }
      }
    }
  },
  computed: {
    menuList () {
      const menuList = this.$store.getters.menuList
      // const access = getAccess()
      return menuList
    },
    activeName () {
      return this.menuName
    },
    list () {
      this.Menulistn()
      const mpath = this.$route.matched[0].path
      return routers.filter(item => {
        if ((mpath === item.path || item.name === 'doc') && !item.meta.hideInMenu) {
          // console.log(item)
          return item
        }
      })
    }
  },
  watch: {
    activeName (value, oldValue) {
      if (!value) {
        this.$nextTick(() => {
          this.$refs.menu.updateActiveName()
        })
      }
    },
    $route (to, from) {
      console.log(to.name)
      if (!to.meta.hideInMenu) {
        this.menuName = to.name
      }
    }
  }
}
</script>
