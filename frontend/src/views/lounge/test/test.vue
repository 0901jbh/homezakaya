<template>
  <video id="video" width="72" height="56" autoplay muted></video>
  <button @click="startBtn">시작!!!!!!!!</button>

  <div class="words" contenteditable></div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

const startBtn = () => {
  // store.dispatch("gameModule/updateUser", "video");
  // store.dispatch("userModule/userLogout", "suz.dev33");
  store.dispatch("roomModule/quitRoom", 1)
  .then((result) => {
    if (result) {
      store.dispatch("roomModule/removeUserInRoom", store.state.userModule.user.userId)
    }
  })
};
</script>

<style></style>
