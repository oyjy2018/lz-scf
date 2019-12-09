<template>
  <div class="team-leftmenu">
    <Tree ref="tree"
          :data="list"
          :render="render"></Tree>
  </div>
</template>

<script>
export default {
  name: 'TeamLeft',
  props: {
    list: {
      type: Array,
      default () {
        return []
      }
    },
    render: {
      type: Function,
      default () {
        return function () { }
      }
    }
  }
}
</script>
