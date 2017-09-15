// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import axios from 'axios';
import VueRouter from "vue-router";
import VueAxios from 'vue-axios';
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
// import AMap from 'vue-amap';
import router from './router';


Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(VueRouter);
Vue.use(VueAxios,axios);
// Vue.use(AMap);

var store = {
  debug: true,
  state: {
  },
  write (key, value) {
    if (this.debug) console.log('writeStore key=', key, ',value=', value);
    this.state[key] = value;
  },
  read (key) {
    if (this.debug) console.log('readStore key=', key, ',value=', this.state[key])
    return this.state[key];
  },
  clear(key) {
  	if (this.debug) console.log('clearStore key=', key, ',value=', this.state[key])
    this.state[key] = null;
  }
}

// 初始化vue-amap
// AMap.initAMapApiLoader({
//   // 申请的高德key
//   key: 'YOUR_KEY',
//   // 插件集合
//   plugin: ['']
// });

/* eslint-disable no-new */
const app = new Vue({
  router,
  render:h => h(App),
  data: {
    store: store
  }
}).$mount("#app");
