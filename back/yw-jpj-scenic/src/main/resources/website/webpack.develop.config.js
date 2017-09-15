// webpack 的开发配置文件
// 编写配置文件，要有最基本的文件入口和输出文件配置信息等
// 里面还可以加loader和各种插件配置使用
var path = require('path')
var webpack = require('webpack')
var OpenBrowserPlugin = require('open-browser-webpack-plugin')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var FriendlyErrorsPlugin = require('friendly-errors-webpack-plugin')
module.exports = {
	// 单页面 SPA 的入口文件
  entry: [
    'webpack/hot/only-dev-server',
    'webpack-dev-server/client?http://localhost:8067',
    path.resolve(__dirname, 'src/app.js')
  ],
	// 构建之后的文件输出位置配置
  externals: {
    'ol': 'window.ol',
    'Cache': 'window.Cache',
    'API': 'window.API',
    '$': 'window.Jquery'
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'bundle.js',
    chunkFilename: '[name].chunk.js'
  },
  devServer: {
    port: 8067,
    proxy: [
      {
        context: ['/user', '/dict', '/authority', '/role', '/common', '/api', '/cms', '/offerVoucher', '/thematicActivities', '/interactive', '/advertising', '/commodity', '/store', '/statistical', '/lease', '/teaSpecies','/order','/commodityEvaluation','/auditManagement'],
        target: 'http://192.168.11.113:6880/'
        // secure: false,
        // changeOrigin: true
      }
    ]
  },
  noParse: [path.join(__dirname, 'node_modules/openlayers/dist/ol.js')],
  plugins: [
    new webpack.HotModuleReplacementPlugin(),
//		new OpenBrowserPlugin({
//			url: 'http://localhost:8080'
//		}),
    new webpack.NoErrorsPlugin(),
    new FriendlyErrorsPlugin(),
    new ExtractTextPlugin('app.css')
		//		, new webpack.optimize.UglifyJsPlugin({ //压缩
		//			compress: {
		//				warnings: false
		//			}
		//		})

  ],
  module: {
    loaders: [{
      test: /\.(js|vue)$/,
      loader: 'eslint-loader',
      enforce: 'pre',
      include: [path.join(__dirname, '.', 'src/js/code'), path.join(__dirname, '.', 'src/js/common'), path.join(__dirname, '.', 'src/js/component')], // path.join(__dirname, '.', 'src/App.vue')
      options: {
        formatter: require('eslint-friendly-formatter')
      }
    }, {
      test: /\.css$/,
      loader: 'style!css'
    }, {
      test: /\.less$/,
      loader: 'style!css!less'
    }, {
      test: /\.(png|jpg|gif|jpeg)$/,
      loader: 'url?limit=125000'
    }, {
      test: /\.(eot|woff|ttf|woff2|svg)$/,
      loader: 'url?limit=125000'
    }, {
      test: /\.(eot|svg|ttf|woff|woff2)\w*/,
      loader: 'file'
    }, {
      test: /\.jsx?$/,
      loader: 'babel'
    }]
  },
  babel: {
    presets: ['es2015', 'stage-2']
  },
  resolve: {
		// require时省略的扩展名，如：require('app') 不需要app.js
    extensions: ['', '.js'],
		// 别名，可以直接使用别名来代表设定的路径以及其他
    alias: {
      nodeModule: path.join(__dirname, './node_modules/'),
    	img: path.join(__dirname, './src/img/'),
      static: path.join(__dirname, './src/static/'),
    	cssPath: path.join(__dirname, './src/css/'),
    	lessPath: path.join(__dirname, './src/less/'),
      jsPath: path.join(__dirname, './src/js/'),
      jsonPath: path.join(__dirname, './src/json/'),
    	code: path.join(__dirname, './src/js/code/'),
    	common: path.join(__dirname, './src/js/common/'),
      component: path.join(__dirname, './src/js/component/')
    }
  }

}
