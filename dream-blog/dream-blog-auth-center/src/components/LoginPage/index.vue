<template>
  <div>
    <header-view></header-view>
    <div class="g-main">
      <!--   主区域组件渲染出口   -->
      <router-view></router-view>
    </div>
    <footer-view></footer-view>
  </div>
</template>

<script>
  import HeaderView from '@/components/LoginPage/HeaderView'
  import FooterView from '@/components/LoginPage/FooterView'
  export default {
    components: {
      HeaderView,
      FooterView,
    }
  }

</script>

<style scoped>
  .g-main{
    min-height: 700px;
    width: 100%;
  }
</style>