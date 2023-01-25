<template>
  <header>
    <HeaderItem/>
  </header>
  <section>
    <div class='wrapper'>
      <RouterView/>
    </div>
  </section>
  <footer>
    <FooterItem/>
  </footer>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HeaderItem from './components/HeaderItem.vue'
import FooterItem from './components/FooterItem.vue'
</script>

<style scoped>
</style>
