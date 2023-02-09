<template>
  <div class="router-view">
    <ErrorPopUp />
    <RouterView />
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import ErrorPopUp from "@/views/ErrorPopUp.vue";
import { useStore } from "vuex";

const store = useStore();
window.onunload = function (event) {
  if (self.screenTop > 9000) {
    // 브라우저 닫힘
    //alert('닫힘');
    event.preventDefault();
    store.dispatch("userModule/userLogout", sessionStorage.getItem("userId"));
  }
  // else {
  //     if(document.readyState == "complete") {
  //      // 새로고침
  //      //alert('새로고침');
  //     }
  //     else if(document.readyState == "loading") {
  //       // 다른 사이트로 이동
  //       //alert('이동');

  //     }
  // }
};
</script>

<style scoped>
.router-view {
  height: 100%;
}
</style>
