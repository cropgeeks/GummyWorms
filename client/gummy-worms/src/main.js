/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'
import Axios from 'axios'
import router from './router'

Axios.defaults.baseURL = '/api'

// Composables
import { createApp } from 'vue'
import vuetify from './plugins/vuetify'

const app = createApp(App)

registerPlugins(app)

app.use(vuetify).use(router).mount('#app')
