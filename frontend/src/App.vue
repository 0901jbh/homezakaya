<template>
  <div class='router-view'>
    <RouterView/>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<style scoped>
.router-view {
  height: 100%;
}
</style>
