// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import vuetify from './plugins/vuetify'
import VueSession from 'vue-session';

var sessionOptions = {
  persist: true
}
Vue.config.productionTip = false
Vue.use(VueSession, sessionOptions)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  vuetify,
  router,
  components: { App },
  template: '<App/>'
})
