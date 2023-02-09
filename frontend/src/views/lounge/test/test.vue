<template>
  <video id="video" width="72" height="56" autoplay muted></video>
  <button @click="startBtn">시작!!!!!!!!</button>

  <div class="words" contenteditable></div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

const form = new FormData();
form.append("email", "bestsjj@naver.com");

const startBtn = () => {
  // store.dispatch("gameModule/updateUser", "video");
  // store.dispatch("userModule/userLogout", "suz.dev33");
  // store.dispatch("roomModule/quitRoom", 83)
  // .then((result) => {
  //   if (result) {
  //     store.dispatch("roomModule/removeUserInRoom", store.state.userModule.user.userId)
  //   }
  // })
   store.dispatch("roomModule/removeInvite", {
    fromUserId: "ssafy",
    toUserId : "sss"
   });
};
</script>

<style></style>
