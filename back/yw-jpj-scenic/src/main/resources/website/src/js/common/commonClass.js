import { loginUser } from 'common/method'

// 所有的树里面的类配置
export const treeClass = {
  all: {
    i: ['fa'],
    b: ['fa']
  },
  root: ['ibox-content', 'col-max-height'],
  0: {
    li: ['tree-first-item'],
    a: ['tree-first-block'],
    i: ['tree-first-folder'],
    b: ['tree-first-caretr'],
    span: ['tree-first-name']
  },
  1: {
    ul: ['tree-second-list'],
    li: ['tree-second-item'],
    a: ['tree-second-block'],
    i: ['tree-second-folder'],
    b: ['tree-second-caretr'],
    span: ['tree-second-name']
  },
  2: {
    ul: ['tree-third-list'],
    li: ['tree-third-item'],
    a: ['tree-third-block'],
    i: ['tree-third-folder'],
    b: ['tree-third-caretr'],
    span: ['tree-third-name']
  },
  3: {
    ul: ['tree-forth-list'],
    li: ['tree-forth-item'],
    a: ['tree-forth-block'],
    i: ['tree-forth-folder'],
    b: ['tree-forth-caretr'],
    span: ['tree-forth-name']
  },
  4: {
    ul: ['tree-fifth-list'],
    li: ['tree-fifth-item'],
    a: ['tree-fifth-block'],
    i: ['tree-fifth-folder'],
    b: ['tree-fifth-caretr'],
    span: ['tree-fifth-name']
  },
  5: {
    ul: ['tree-sixth-list'],
    li: ['tree-sixth-item'],
    a: ['tree-sixth-block'],
    i: ['tree-sixth-folder'],
    b: ['tree-sixth-caretr'],
    span: ['tree-sixth-name']
  },
  6: {
    ul: ['tree-seventh-list'],
    li: ['tree-seventh-item'],
    a: ['tree-seventh-block'],
    i: ['tree-seventh-folder'],
    b: ['tree-seventh-caretr'],
    span: ['tree-seventh-name']
  },
  7: {
    ul: ['tree-eighth-list'],
    li: ['tree-eighth-item'],
    a: ['tree-eighth-block'],
    i: ['tree-eighth-folder'],
    b: ['tree-eighth-caretr'],
    span: ['tree-eighth-name']
  },
  8: {
    ul: ['tree-ninth-list'],
    li: ['tree-ninth-item'],
    a: ['tree-ninth-block'],
    i: ['tree-ninth-folder'],
    b: ['tree-ninth-caretr'],
    span: ['tree-ninth-name']
  },
  9: {
    ul: ['tree-tenth-list'],
    li: ['tree-tenth-item'],
    a: ['tree-tenth-block'],
    i: ['tree-tenth-folder'],
    b: ['tree-tenth-caretr'],
    span: ['tree-tenth-name']
  }
}

export const WINDOW_CENTER = 'J_center' // 标准格式
export const windowCenter = 'J_center' // 用到的地方太多，标准格式暂时不替换menu中的文件

export const btnPlusClass = ['fa', 'fa-plus-square', 'font-primary']
export const btnMinusClass = ['fa', 'fa-minus-square', 'font-info']
export const btnReloadClass = ['fa', 'fa-refresh', 'font-success']
export const btnEditClass = ['fa', 'fa-check-square', 'font-info']

// style常量   BTN_EDI
export const Z_INDEX_HIGH = '1111'
export const Z_INDEX_LOW = '0'

export function setConfig (opt) {
  opt.config.ajaxType = opt.config.type
  opt.config.ajaxUrl = opt.config.url //防止后台字段重名导致错误

  var json = {}
  for (var attr in opt.opt) {
    json[attr] = opt.opt[attr]
  }
  if (opt.id) {
    json.id = json.className = opt.id
  } else if (opt.fnName) {
    json.id = json.className = opt.fnName
  } else {
    json.id = json.className = opt.config.xtype
  }
  if (opt.config) {
    for (var i in opt.config) {
      json[i] = opt.config[i]
    }
  }

  if (opt.opt.btns) {
    for (let btn in opt.btns) {
      if (opt.btns[btn] && opt.opt.btns[btn] !== true) {
        delete opt.btns[btn]
      }
    }
    for (let btn in opt.opt.btns) {
      if (!opt.btns[btn] && opt.opt.btns[btn] && opt.opt.btns[btn] !== true) {
        opt.btns[btn] = opt.opt.btns[btn]
      }
    }
  }
  if (opt.opt.operationBtns) {
    for (let btn in opt.operationBtns) {
      if (opt.operationBtns[btn] && opt.opt.operationBtns[btn] !== true) {
        delete opt.operationBtns[btn]
      }
    }
    for (var btn in opt.opt.operationBtns) {
      if (!opt.operationBtns[btn] && opt.opt.operationBtns[btn]) {
        opt.operationBtns[btn] = opt.opt.operationBtns[btn]
      }
    }
  }

  if (opt.opt.fields && opt.opt.fields.length) {
    json.fields = opt.config.fields = opt.opt.fields
  }

  var loginHasProporty = false
  for (var method in loginUser.allMethod) {
    if (method) {
      loginHasProporty = true
      break
    }
  }

  json.btns = {}
  json.operationBtns = {}
  if (loginUser.isBasisAuthority) {
    if (loginUser && loginHasProporty) {
      if (loginUser.allMethod[json.id]) {
        for (let btn in loginUser.allMethod[json.id]) {
          if (btn) {
            if (loginUser.allMethod[json.id][btn]) {
              if (opt.btns && opt.btns[btn]) {
                json.btns[btn] = opt.btns[btn]
              }
              if (opt.operationBtns && opt.operationBtns[btn]) {
                json.operationBtns[btn] = opt.operationBtns[btn]
              }
            }
          }
        }
      }
    }

    if (opt.btns && opt.btns.reload) { json.btns.reload = opt.btns.reload }
  } else {
    for (let btn in opt.btns) {
      if (btn && opt.btns[btn]) {
        json.btns[btn] = opt.btns[btn]
      }
    }
    for (let btn in opt.operationBtns) {
      if (btn && opt.operationBtns[btn]) {
        json.operationBtns[btn] = opt.operationBtns[btn]
      }
    }
  }

  if (opt.treeEvents) {
    json.treeEvents = {}
    for (let btn in opt.treeEvents) {
      json.treeEvents[btn] = opt.treeEvents[btn]
    }
  }
  if (json.important) {
    for (var defineBtns in json.important) {
      if (opt[json.important[defineBtns]]) {
        json[json.important[defineBtns]] = {}
        for (let btn in opt[json.important[defineBtns]]) {
          json[json.important[defineBtns]][btn] = opt[json.important[defineBtns]][btn]
        }
      }
    }
  }
  var classGroup = opt.classGroup || opt.config.xtype
  if (classGroup) {
    try {
      if (eval('set' + classGroup + 'Class')) {
        eval('set' + classGroup + 'Class')({
          opt: opt,
          json: json
        })
      }
    } catch (e) {
      console.log('function is not defined')
    }
  }
  if (json.title) { json.title.extraClass = ['ibox-title'] }
  return json
}

export function setTreePanelClass (opt) {
  var defaultClass = {
    extraClass: ['ibox'],
    defineBtnsExtraClass: ['ibox-tools'],
    boxExtraClass: ['col-sm-5'],
    treeClass: treeClass,
    extend: {
      true: {
        b: 'fa-caret-down',
        i: 'fa-folder-open'
      },
      false: {
        b: 'fa-caret-right',
        i: 'fa-folder'
      }
    }
  }
  for (var i in defaultClass) {
    if (!opt.json[i]) { opt.json[i] = defaultClass[i] }
  }
}
export function setGridPanelClass (opt) {
  var defaultClass = {
    extraClass: ['ibox', 'componentSet'],
    gridBoxExtraClass: ['ibox-content', 'clearfix'],
    defineBtnsExtraClass: ['ibox-tools'],
    boxExtraClass: ['col-sm-12'],
    tableExtraClass: {
      table: ['table', 'table-bordered', 'table-condensed', 'text-center'],
      thead: [],
      theadTr: []
    },
    pageExtraClass: {
      pages: ['pagination'],
      allPage: ['page-total-number'],
      pageSizeNumber: ['page-size-number'],
      pageInputContainer: ['input-group'],
      pageNoInput: ['form-control', 'page-total-input'],
      pageSizeInput: ['form-control'],
      pageBtn: ['input-group-addon', 'page-search']
    }
  }
  for (let i in defaultClass) {
    if (!opt.json[i]) { opt.json[i] = defaultClass[i] }
  }
  if (opt.opt.config.search) {
    opt.opt.config.search.extraClass = opt.opt.config.search.extraClass || ['form-group', 'input-group']
    for (let i in opt.opt.config.search.fields) {
      opt.opt.config.search.fields[i].extraClass = opt.opt.config.search.fields[i].extraClass || ['form-control', 'page-total-input']
    }
    if (!opt.opt.config.search.btn) { opt.opt.config.search.btn = {} }
    opt.opt.config.search.btn.extraClass = opt.opt.config.search.btn.extraClass || ['fa', 'fa-search', 'input-group-addon', 'page-search']
  }
  for (let i in opt.opt.config.fields) {
    if (opt.opt.config.fields[i].type === 'operations') { opt.opt.config.fields[i].operationBtns = opt.json.operationBtns }
  }
}
export function setFormPanelClass (opt) {
  var defaultClass = {
    formExtraClass: ['form-horizontal', 'panel-body'],
    fieldExtraClass: {
      boxDiv: ['form-group'],
      label: ['col-sm-4', 'control-label'],
      div: ['col-sm-8'],
      inputText: ['form-control'],
      select: ['form-control'],
      textarea: ['form-control'],
      inputRadioLabel: ['radio-inline'],
      treeRoot: ['dropdown-menu', 'show'],
      column: ['col-sm-12']
    }
  }
  var noColumn = true
  for (var i in opt.json.fields) {
    if (opt.json.fields[i].column) {
      noColumn = false
      break
    }
  }
  if (noColumn) {
    defaultClass.fieldExtraClass.label = ['col-sm-3', 'control-label']
  }
  for (let i in defaultClass) {
    if (!opt.json[i]) { opt.json[i] = defaultClass[i] }
  }
}
export function setChartPanelClass (opt) {
  var defaultClass = {
    extraClass: ['ibox', 'componentSet'],
    gridBoxExtraClass: ['ibox-content', 'clearfix'],
    defineBtnsExtraClass: ['ibox-tools'],
    boxExtraClass: ['col-sm-12']
  }
  for (let i in defaultClass) {
    if (!opt.json[i]) { opt.json[i] = defaultClass[i] }
  }
  if (opt.opt.config.search) {
    opt.opt.config.search.extraClass = opt.opt.config.search.extraClass || ['form-group', 'input-group']
    for (let i in opt.opt.config.search.fields) {
      opt.opt.config.search.fields[i].extraClass = opt.opt.config.search.fields[i].extraClass || ['form-control', 'page-total-input']
    }
    if (!opt.opt.config.search.btn) { opt.opt.config.search.btn = {} }
    opt.opt.config.search.btn.extraClass = opt.opt.config.search.btn.extraClass || ['fa', 'fa-search', 'input-group-addon', 'page-search']
  }
}
