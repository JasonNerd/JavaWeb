const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,

  // 默认8080, 以下为配置其他端口号
  // devServer: {
  //   port: 7000,
  // }
})
