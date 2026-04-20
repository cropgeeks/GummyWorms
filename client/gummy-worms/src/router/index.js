/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../pages/index.vue'),
    },
    {
      path: '/results',
      name: 'results',
      component: () => import('../pages/results.vue'),
    }
    
  ]
})

export default router
