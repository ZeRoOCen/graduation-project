import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import http from './utils/http'
//导入全局css样式
import './assets/css/global.css'

//login登录页面图标导入
import './assets/login-icon/iconfont.css'

//通过修改原型链，来更方便的使用，不修改也是可以的
//Vue.prototype.$http = axios
Vue.prototype.$http = http

//阻止显示生产模式的消息。因为在开发环境下，Vue 会提供很多警告来帮你对付常见的错误与陷阱。而在生产环境下，这些警告语句却没有用
Vue.config.productionTip = false 

//定义主入口App.vue
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
