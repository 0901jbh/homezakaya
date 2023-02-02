<template>
  <video id="video" width="72" height="56" autoplay muted></video>
  <button @click="startBtn">시작!!!!!!!!</button>
<<<<<<< HEAD
=======
  <div class="words" contenteditable></div>
>>>>>>> fee3f6c49d53c3d6ae7d0f144920c87685c992ca
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

const startBtn = () => {
<<<<<<< HEAD
    // store.dispatch("gameModule/startVideo", "video");
    store.dispatch("gameModule/getSpeech");
}
=======
  // store.dispatch("gameModule/updateUser", "video");
  store.dispatch("userModule/removeUser", "ssafy");
};
>>>>>>> fee3f6c49d53c3d6ae7d0f144920c87685c992ca
</script>

<style></style>
