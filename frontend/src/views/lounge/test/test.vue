<template>
  <video id="video" width="72" height="56" autoplay muted></video>
  <button @click="startBtn">시작!!!!!!!!</button>
</template>

<script setup>
import { useStore } from 'vuex'

const store = useStore();

const startBtn = () => {
    store.dispatch("gameModule/startVideo", "video");
}
</script>

<style>

</style>