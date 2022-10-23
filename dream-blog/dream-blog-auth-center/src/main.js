import Vue from 'vue'
import App from './App.vue'
//路由配置
import router from "./router";
//状态管理
import store from "./store"

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
