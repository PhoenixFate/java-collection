<template>
  <div>
    用户管理导航页面 <nuxt-link :to="'/user/one'">进入到one页面</nuxt-link>
    <nuxt-child/>
  </div>
</template>

<script>
export default {
  layout:'test'
}
</script>

<style scoped>

</style>
