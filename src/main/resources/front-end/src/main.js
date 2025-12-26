/**
 * Vue应用入口文件
 * 职责：创建Vue应用实例、注册全局组件和插件、挂载应用
 */

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'
import '@/styles/index.css'

const app = createApp(App)

// 注册所有Element Plus图标为全局组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 注册插件
app.use(createPinia()) // Pinia状态管理
app.use(router) // Vue Router路由
app.use(ElementPlus, { locale: zhCn }) // Element Plus UI库

// 挂载到DOM
app.mount('#app')
