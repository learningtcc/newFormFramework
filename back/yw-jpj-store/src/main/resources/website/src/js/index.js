import layer from 'static/layer/layer'
import $ from 'jquery'
import { WINDOW_CENTER } from 'common/commonClass'
import NavPanel from 'component/navPanel'
import TreePanel from 'component/treePanel'
import { getMenuAuthority } from 'common/method'

import * as types from 'jsPath/middleware'

var leftMenu = {
  fn: function (opt) {
    var mainCotainer = document.getElementById(WINDOW_CENTER)
    if (mainCotainer) {
      mainCotainer.innerHTML = ''
    }
    if (opt && opt.panel) {
      opt.panel.items[opt.btn.getAttribute('href').substring(1)] = types[opt.btn.getAttribute('href').substring(1)]({
        owner: opt.panel,
        ownerBtn: opt.panel // 其余配置有的加
      })
    }
  },
  btns: [
    {
      name: ' 商家信息',
      href: '#businessForm',
      icon: ['fa', 'fa-user-o'],
      events: {
        click: function (opt) {
          leftMenu.fn(opt)
        }
      }
    },
    {
      name: ' 商品管理',
      href: '#goodsGrid',
      icon: ['fa', 'fa-shopping-basket'],
      events: {
        click: function (opt) {
          leftMenu.fn(opt)
        }
      }
    },
    {
      name: ' 优惠活动',
      href: '#salesGrid',
      icon: ['fa', 'fa-street-view'],
      events: {
        click: function (opt) {
          leftMenu.fn(opt)
        }
      }
    },
    {
      name: ' 招租管理',
      href: '#leaseGrid',
      icon: ['fa', 'fa-street-view'],
      events: {
        click: function (opt) {
          leftMenu.fn(opt)
        }
      }
    },
    {
      name: ' 订单管理',
      href: '#orderGrid',
      icon: ['fa', 'fa-street-view'],
      events: {
        click: function (opt) {
          leftMenu.fn(opt)
        }
      }
    },
    {
      name: ' 评价管理',
      href: '#commentGrid',
      icon: ['fa', 'fa-shopping-basket'],
      events: {
        click: function (opt) {
          leftMenu.fn(opt)
        }
      }
    },
    {
      name: ' 用户管理',
      href: '#userManageList',
      icon: ['fa', 'fa-cog'],
      events: {
        click: function (opt) {
        }
      },
      children: [{
        name: ' 权限管理',
        href: '#powerTree', // icon:['fa','fa-cog'],
        events: {
          click: function (opt) {
            leftMenu.fn(opt)
          }
        }
      }, {
        name: ' 角色管理',
        href: '#roleGrid', // icon:['fa','fa-cog'],
        events: {
          click: function (opt) {
            leftMenu.fn(opt)
          }
        }
      }, {
        name: ' 用户管理',
        href: '#userGrid', // icon:['fa','fa-cog'],
        events: {
          click: function (opt) {
            leftMenu.fn(opt)
          }
        }
      }, {
        name: ' 日志管理',
        href: '#logGrid', // icon:['fa','fa-cog'],
        events: {
          click: function (opt) {
            leftMenu.fn(opt)
          }
        }
      }
      ]
    }]
}

// 获取用户信息与权限
var finalObj = getMenuAuthority({
  isBasisAuthority: false, // 是否根据后台接口分配权限,获取最终展示的菜单以及菜单显示的顺序,false即全部功能都展现
  sendMenu: leftMenu,
  ajaxConfig: {
    url: 'user/getLoginUserInfo',
    type: 'get'
  }
})

// 导航栏配置
/* eslint-disable no-unused-vars */
var nav = new NavPanel({
  setBox: true,
  id: 'nav',
  className: 'nav',
  boxExtraClass: ['drore-top'],
  extraClass: ['navbar', 'navbar-inverse'],
  btnsPanelExtraClass: ['nav', 'navbar-top-links', 'navbar-right'],
  logo: {
    html: '<i class="fa fa-home"></i> 义乌精品街管理系统--商家端',
    extraClass: ['navbar-header', 'drore-logo', 'container-fluid']
  },
  btns: [{
    text: '欢迎登录，' + finalObj.userName,
    className: 'welcome'
  },
  {
    text: '|',
    className: 'clear'
  },
  {
    text: '修改密码',
    className: 'editPassword',
    events: {
      click: function (opt) {
        types.mdfPasswordForm({
          owner: opt.panel
        })
      }
    }
  }, {
    text: '|',
    className: 'clear'
  }, {
    text: '退出',
    className: 'logout',
    events: {
      click: function () {
        layer.confirm('你确定要退出登录吗？', {
          btn: ['确定', '取消'] // 按钮
        }, function () {
          var loading = layer.load(1)
          $.ajax({
            type: 'get',
            url: '/user/logout',
            success: function (response) {
              if (response.isSuccess) {
                location.href = 'login.html'
              } else {
                layer.msg(response.errorMessage, function () {})
              }
              layer.close(loading)
            },
            error: function (response) {
              layer.close(loading)
              layer.alert('错误代码：' + response.status + '，请联系管理员', {title: '错误提示', shadeClose: true})
            }
          })
          layer.closeAll() // 关闭所有层
        }, function () {
          layer.closeAll() // 关闭所有层
        })
      }
    }
  }]
})

// 侧边栏配置
/* eslint-disable no-unused-vars */
var menu = new TreePanel({
  id: 'menu',
  extraClass: ['drore-left'],
  className: 'menu',
  treeClass: {
    all: {
      b: ['fa', 'arrow']
    },
    root: ['nav', 'left-nav', 'drore-left-nav'],
    1: {
      ul: ['nav', 'nav-second-level']
    },
    2: {
      ul: ['nav', 'nav nav-third-level']
    }
  },
  isMenu: true,
  data: finalObj.isBasisAuthority ? finalObj.btns : leftMenu.btns
})
