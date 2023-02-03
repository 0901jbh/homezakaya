<template>
  <div class='router-view'>
    <RouterView/>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { useStore } from "vuex";

const store = useStore();
window.onunload = function (event) {
  event.preventDefault();
  store.dispatch("userModule/userLogout", sessionStorage.getItem("userId"));
}
</script>

<style scoped>
.router-view {
  height: 100%;
}
</style>