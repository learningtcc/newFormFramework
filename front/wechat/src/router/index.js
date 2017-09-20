import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/in',
      name: 'index',
      component: resolve => require(['../components/website/index/index'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '首页';
        next()
      }
    },
    {
      path: '/fillOrder',
      name: 'fillOrder',
      component: resolve => require(['../components/mall/order/fillOrder'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '订单填写';
        next()
      }
    },
    {
      path: '/orderSucc',
      name: 'orderSucc',
      component: resolve => require(['../components/mall/order/orderSucc'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '交易记录';
        next()
      }
    },
    {
      path: '/onlineshop/list',
      name: 'onlineshop',
      component: resolve => require(['../components/mall/onlineShop/list'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '网上商铺';
        next()
      }
    },
    {
      path: '/userCenter',
      name: 'userCenter',
      component: resolve => require(['../components/user/userCenter'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '个人中心';
        next()
      }
    },
    {
      path: '/myMessage',
      name: 'myMessage',
      component: resolve => require(['../components/user/message'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '我的消息';
        next()
      }
    },
    {
      path: '/messageDetail',
      name: 'messageDetail',
      component: resolve => require(['../components/user/messageDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '消息详情';
        next()
      }
    },
    {
      path: '/userInfo',
      name: 'userInfo',
      component: resolve => require(['../components/user/info'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '个人信息';
        next()
      }
    },
    {
      path: '/addressList',
      name: 'addressList',
      component: resolve => require(['../components/user/addressList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '收货地址';
        next()
      }
    },
    {
      path: '/addressNew',
      name: 'addressNew',
      component: resolve => require(['../components/user/addressNew'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '收货地址';
        next()
      }
    },
    {
      path: '/myFavor',
      name: 'myFavor',
      component: resolve => require(['../components/user/myFavor'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '我的收藏';
        next()
      }
    },
    {
      path: '/myorder_detail',
      name: 'payType',
      component: resolve => require(['../components/user/myorder_detail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '订单详情';
        next()
      }
    },
    {
      path: '/myorder_payType',
      name: 'payType',
      component: resolve => require(['../components/user/myorder_payType'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '付款方式';
        next()
      }
    },
    {
      path: '/myorder_evaluate',
      name: 'myorder_evaluate',
      component: resolve => require(['../components/user/myorder_evaluate'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '评价';
        next()
      }
    },
    {
      path: '/streetService',
      name: 'streetService',
      component: resolve => require(['../components/streetConsult/streetService'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '街区服务';
        next()
      }
    },
    {
      path: '/tenementList',
      name: 'tenementList',
      component: resolve => require(['../components/streetConsult/tenementList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '旺铺招租';
        next()
      }
    },
    {
      path: '/tenementDetail',
      name: 'tenementDetail',
      component: resolve => require(['../components/streetConsult/tenementDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '旺铺招租详情';
        next()
      }
    },
    {
      path: '/activityList',
      name: 'activityList',
      component: resolve => require(['../components/streetConsult/activityList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '街区文化活动';
        next()
      }
    },
    {
      path: '/activityDetail',
      name: 'activityDetail',
      component: resolve => require(['../components/streetConsult/activityDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '街区文化活动详情';
        next()
      }
    },
    {
      path: '/storeDiscounts',
      name: 'storeDiscounts',
      component: resolve => require(['../components/streetConsult/storeDiscounts'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '店铺优惠';
        next()
      }
    },
    {
      path: '/storeDetail',
      name: 'storeDetail',
      component: resolve => require(['../components/streetConsult/storeDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '店铺优惠详情';
        next()
      }
    },
    {
      path: '/theme',
      name: 'theme',
      component: resolve => require(['../components/themeActivity/theme'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '主题活动';
        next()
      }
    },
    {
      path: '/themeDetail',
      name: 'themeDetail',
      component: resolve => require(['../components/themeActivity/themeDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '主题活动详情';
        next()
      }
    },
    {
      path: '/handShop',
      name: 'handShop',
      component: resolve => require(['../components/mall/handShop'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '掌上商城';
        next()
      }
    },
    {
      path: '/teaList',
      name: 'teaList',
      component: resolve => require(['../components/mall/tea/teaList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '茶文化';
        next()
      }
    },
    {
      path: '/teaDetail',
      name: 'teaDetail',
      component: resolve => require(['../components/mall/tea/teaDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '茶文化详情';
        next()
      }
    },
    {
      path: '/guide',
      name: 'guide',
      component: resolve => require(['../components/mall/onlineShop/guide'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '导购指南';
        next()
      }
    },
    {
      path: '/specialtyShop',
      name: 'specialtyShop',
      component: resolve => require(['../components/mall/onlineShop/specialtyShop'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '特色商品';
        next()
      }
    },
    {
      path: '/virtualShop',
      name: 'virtualShop',
      component: resolve => require(['../components/mall/onlineShop/virtualShop'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '网上商铺';
        next()
      }
    },
    {
      path: '/hotShop',
      name: 'hotShop',
      component: resolve => require(['../components/mall/onlineShop/hotShop'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '热门商铺';
        next()
      }
    },
    {
      path: '/interaction/index',
      name: 'interaction',
      component: resolve => require(['../components/mall/interaction/index'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '商家互动';
        next()
      }
    },
    {
      path: '/interactionList',
      name: 'interactionList',
      component: resolve => require(['../components/mall/interaction/interactionList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '互动列表';
        next()
      }
    },
    {
      path: '/interactionDetail',
      name: 'interactionDetail',
      component: resolve => require(['../components/mall/interaction/interactionDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '互动详情';
        next()
      }
    },
    {
      path: '/interactionRelease',
      name: 'interactionRelease',
      component: resolve => require(['../components/mall/interaction/interactionRelease'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '互动发布';
        next()
      }
    },
    {
      path: '/myInteraction',
      name: 'myInteraction',
      component: resolve => require(['../components/user/myInteraction'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '我的互动';
        next()
      }
    },
    {
      path: '/myInteractionR',
      name: 'myInteractionR',
      component: resolve => require(['../components/user/myInteractionR'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '我的互动发布';
        next()
      }
    },
    {
      path: '/myorder_logistics',
      name: 'myorder_logistics',
      component: resolve => require(['../components/user/myorder_logistics'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '物流详情';
        next()
      }
    },
    {
      path: '/myOrder',
      name: 'myOrder',
      component: resolve => require(['../components/user/myOrder'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '我的订单';
        next()
      }
    },
    {
      path: '/map',
      name: 'map',
      component: resolve => require(['../components/map/map'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '服务地图';
        next()
      }
    },
    {
      path: '/mapSearchList',
      name: 'mapSearchList',
      component: resolve => require(['../components/map/mapSearchList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '搜索';
        next()
      }
    },
    {
      path: '/mapAroundList',
      name: 'mapAroundList',
      component: resolve => require(['../components/map/mapAroundList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '搜周边';
        next()
      }
    },
    {
      path: '/mapAroundDetail',
      name: 'mapAroundDetail',
      component: resolve => require(['../components/map/mapAroundDetail'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '服务地图';
        next()
      }
    },
    {
      path: '/mapAroundNameList',
      name: 'mapAroundNameList',
      component: resolve => require(['../components/map/mapAroundNameList'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '名称选择';
        next()
      }
    },
    {
      path: '/mapAroundRoute',
      name: 'mapAroundRoute',
      component: resolve => require(['../components/map/mapAroundRoute'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '线路';
        next()
      }
    },
    {
      path: '/mapAroundType',
      name: 'mapAroundType',
      component: resolve => require(['../components/map/mapAroundType'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '线路';
        next()
      }
    },
    {
      path: '/cart',
      name: 'cart',
      component: resolve => require(['../components/cart/cart'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '我的购物车';
        next()
      }
    },
    {
      path: '/productDetail',
      name: 'productDetail',
      component: resolve => require(['../components/mall/details/details'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '商品详情';
        next()
      }
    },
    {
      path: '/userInfoName',
      name: 'userInfoName',
      component: resolve => require(['../components/user/info_name'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '设置昵称';
        next()
      }
    },
    {
      path: '/userInfoTel',
      name: 'userInfoTel',
      component: resolve => require(['../components/user/info_tel'], resolve),
      beforeEnter: (to, from, next) => {
        document.title = '设置电话';
        next()
      }
    },
  ]
})
