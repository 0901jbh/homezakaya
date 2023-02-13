<template>
  <header>
    <UserHeader />
  </header>
  <div class="container">
    <!-- <div class="title">
        <div class="content">
          Table
        </div>
      </div> -->
    <div class="rooms">
      <RoomsIndexItem :key="renderKey" />
    </div>
    <FriendIndex :key="renderKey" />
  </div>
  <div class="reload-btn-wrapper" @click="reload">
    <div class="reload-btn">
      <img src="@/assets/images/circle_arrow.png" alt="circle_arrow">
    </div>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
import UserHeader from '../menu/UserHeader.vue'
import RoomsIndexItem from './components/RoomsIndexItem.vue'
import FriendIndex from './components/FriendIndex.vue'
import { ref } from 'vue'

const renderKey = ref(0);

const reload = () => {
  renderKey.value += 1
  console.log("-- start Rerender !! --")
}

defineExpose(renderKey)
</script>

<style scoped>
.container {
  height: 88vh;
  display: grid;
  grid-template-columns: 5fr 1.5fr;
  margin: 1vh 8vw;
}

/* .title {
  display: flex;
  align-items: center;
  padding: 0 4%;
} */

/* .content {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 15px 35px;
  gap: 10px;

  width: 10%;
  height: 5vh;
  left: 52px;
  top: 105px;

  color: white;
  font-size: 2rem;
  font-weight: 700;
  background: #000000;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 53px;
} */

.rooms {
  background: #252836;
  /* border: 0.3rem solid #000000; */
  border-radius: 30px;
  padding: 4vh;
  /* margin-bottom: 4vh; */
  height: 80vh;
}

/* 새로고침 버튼 */

.reload-btn-wrapper {
  position: fixed;
  bottom: 5vh;
  right: 2vw;

  height: 4rem;
  width: 4rem;
  background: #e27b66;
  border: 0.3rem solid black;
  border-radius: 50%;
  transition: rotate 1s;
}

.reload-btn {
  width: 100%;
  height: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
}

.reload-btn>img {
  width: 65%;
  height: 65%;
}

.reload-btn-wrapper:hover {
  background-color: #e27b66;
  rotate: 360deg;
  cursor: pointer;
}
</style>