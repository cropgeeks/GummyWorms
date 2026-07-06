<template>
  <v-app>
    <header>
      <AppHeader />
    </header>
    <v-main>
      <div class="pa-4">
      <router-view />
      </div>
    </v-main>
    <!--<v-footer>
      <AppFooter />
    </v-footer>-->
  </v-app>
</template>

<script>
  import AppHeader from './components/AppHeader.vue'
  import AppFooter from './components/AppFooter.vue'
</script>

