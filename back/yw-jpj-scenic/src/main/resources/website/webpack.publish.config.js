var path = require('path')
var node_modules = path.resolve(__dirname, 'node_modules')
var CleanPlugin = require('clean-webpack-plugin')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var webpack = require('webpack')
var HtmlWebpackPlugin = require('html-webpack-plugin')
module.exports = {
  entry: path.resolve(__dirname, 'src/app.js'),
  output: {
    path: path.resolve(__dirname, 'build'),
    filename: '[name].[hash:8].js',
    chunkFilename: '[name].chunk.js'
  },
    // ...
  plugins: [
    new CleanPlugin(['dist']),
        // 分离第三方应用插件,name属性会自动指向entry中vendros属性，filename属性中的文件会自动构建到output中的path属性下面
    new webpack.optimize.CommonsChunkPlugin({name: 'vendors', filename: 'vendors.js'}),
        // 可以新建多个抽离样式的文件，这样就可以有多个css文件了。
    new ExtractTextPlugin('app.css'),
    new webpack.DefinePlugin({
            // 去掉react中的警告，react会自己判断
      'process.env': {
        NODE_ENV: '"production"'
      }
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      htmlWebpackPlugin: {
        'files': {
          'css': ['app.css'],
          'js': ['vendors.js', 'bundle.js']
        }
      },
      // 压缩 html 文档
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeAttributeQuotes: false
      }
    }),
    new webpack.optimize.UglifyJsPlugin({ // 压缩
      compress: {
        warnings: false,
        drop_console: true
         // drop_debugger: true,
      }
    })
  ],
  module: {
    loaders: [{
      test: /\.vue$/,
      loader: 'vue'
    }, {
      test: /\.css$/,
      loader: 'style!css'
    }, {
      test: /\.less$/,
      loader: 'style!css!less'
    }, {
      test: /\.(png|jpg|gif|jpeg)$/,
      loader: 'url?limit=25000'
    }, {
      test: /\.(eot|woff|ttf|woff2|svg)$/,
      loader: 'url?limit=25000'
    }, {
      test: /\.(eot|svg|ttf|woff|woff2|png)\w*/,
      loader: 'file'
    }, {
      test: /\.jsx?$/,
      loader: 'babel'
    }]
  },
  babel: {
    presets: ['es2015']
  },
  resolve: {
		// require时省略的扩展名，如：require('app') 不需要app.js
    extensions: ['', '.js', '.vue'],
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
