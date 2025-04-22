<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>
    <v-footer>
      <AppFooter />
    </v-footer>
  </v-app>
</template>

<script setup>
  import AppFooter from './components/AppFooter.vue'
</script>
