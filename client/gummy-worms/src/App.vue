<template>
  <v-app>
    <header>
      <AppHeader />
    </header>
    <v-main>
      <router-view />
    </v-main>
    <v-footer>
      <AppFooter />
    </v-footer>
  </v-app>
</template>

<script setup>
  import AppHeader from './components/AppHeader.vue'
  import AppFooter from './components/AppFooter.vue'
</script>

