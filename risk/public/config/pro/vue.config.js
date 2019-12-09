const path = require('path')
// gzip压缩插件
const CompressionWebpackPlugin = require('compression-webpack-plugin')

const resolve = dir => {
  return path.join(__dirname, dir)
}

function getProdExternals () {
  return {
    'vue': 'Vue',
    'vue-router': 'VueRouter',
    'iview': 'iview',
    'axios': 'axios'
  }
}

const prourl = 'http://www.lingzhufin.com/risk/'

// 项目部署基础
// 默认情况下，我们假设你的应用将被部署在域的根目录下,
// 例如：https://www.my-app.com/
// 默认：'/'
// 如果您的应用程序部署在子路径中，则需要在这指定子路径
// 例如：https://www.foobar.com/my-app/
// 需要将它改为'/my-app/'
// iview-admin线上演示打包路径： https://file.iviewui.com/admin-dist/

const BASE_URL = process.env.NODE_ENV === 'production'
  ? prourl
  : '/'

module.exports = {
  // webpack配置
  configureWebpack: config => {
    let plugins = [
      new CompressionWebpackPlugin({
        filename: '[path].gz[query]',
        algorithm: 'gzip',
        test: new RegExp(
          '\\.(' +
          ['js', 'css'].join('|') +
          ')$'
        ),
        threshold: 10240,
        minRatio: 0.8
      })
    ]
    if (process.env.NODE_ENV !== 'development') {
      config.plugins = [...config.plugins, ...plugins]
      // 把webpack的配置写在这里 会自动合并
      // 以下库使用cdn，不会被打包
      config.externals = getProdExternals()
    }
  },
  // Project deployment base
  // By default we assume your app will be deployed at the root of a domain,
  // e.g. https://www.my-app.com/
  // If your app is deployed at a sub-path, you will need to specify that
  // sub-path here. For example, if your app is deployed at
  // https://www.foobar.com/my-app/
  // then change this to '/my-app/'
  publicPath: BASE_URL,
  // tweak internal webpack configuration.
  // see https://github.com/vuejs/vue-cli/blob/dev/docs/webpack.md
  // 如果你不需要使用eslint，把lintOnSave设为false即可
  lintOnSave: true,
  chainWebpack: config => {
    config.entry('polyfill').add('@babel/polyfill')
    config.resolve.alias
      .set('@', resolve('src')) // key,value自行定义，比如.set('@@', resolve('src/components'))
      .set('_c', resolve('src/components'))
  },
  transpileDependencies: ['iview'],
  // 设为false打包时不生成.map文件
  productionSourceMap: false,
  // 这里写你调用接口的基础路径，来解决跨域，如果设置了代理，那你本地开发环境的axios的baseUrl要写为 '' ，即空字符串
  devServer: {
    proxy: prourl
    // proxy: 'http://192.168.1.31:21000/'
  }
}
