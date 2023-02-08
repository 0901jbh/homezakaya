<template>
  <div class="wrapper">
    <div class="first-line">
      <div class="friend-state"></div>
      <div class="name-and-cancel">
        <div>{{ props.friend.nickname }}</div>
        <button class="delete-friend" type="button" @click="deleteFriendOpen">X</button>
      </div>
    </div>
    <div class="second-line">
      <img src="@/assets/images/manner_w.png" alt="manner_w img" style="height:25px;">
      <div>{{ props.friend.mannerPoint }}</div>
      <img src="@/assets/images/alcohol_w.png" alt="alcohol_w img" style="height:25px;">
      <div>{{ props.friend.alcoholPoint }}잔</div>
      <div v-if="props.friend.state == 'online'" class="follow-friend" type="button" @click="followFriend">따라가기</div>
      <div v-else class="follow-friend"></div>
    </div>
  </div>

  <!-- 친구 삭제 팝업창 -->
  <div class="delete-friend-modal-bg" @click="deleteFriendClose"></div>
  <div class="delete-friend-modal-wrap">
    <div class="delete-popup">
      <!-- <div class="popup-header">
        <div class="popup-header-title">Delete Friend</div>
      </div> -->
      <div class="popup-content">
        <div>친구를 삭제하시겠습니까?</div>
        <div class="btn-wrapper">
          <el-button type="info" size="large" @click="deleteFriend">
            <RouterLink to="/rooms" style="text-decoration:none; color: black;">
              예
            </RouterLink>
          </el-button>
          <el-button type="info" size="large" @click="deleteFriendClose">
            <RouterLink to="/rooms" style="text-decoration:none; color: black;">
              아니요
            </RouterLink>
          </el-button>
        </div>
      </div>
    </div>
  </div>
  <div></div>
</template>

<script setup>
import { onMounted, onUpdated } from 'vue';
import { useStore } from 'vuex';

const props = defineProps({
  friend: Object,
  idx: Number,
})

const emit = defineEmits(['followFriend'])
const store = useStore()


const userState = () => {
  let friendStateTag = document.getElementsByClassName("friend-state")[props.idx]
  if (props.friend.state == "offline") {
    friendStateTag.style.background = "radial-gradient(50% 50% at 50% 50%, #EE1818 0%, rgba(208, 106, 106, 0) 100%)";
    let isOnline = false;
  } else {
    friendStateTag.style.background = "radial-gradient(50% 50% at 50% 50%, #1CEE18 0%, rgba(108, 208, 106, 0) 100%)";
    let isOnline = true;
  }
}

onMounted(() => {
  userState()
})

onUpdated(() => {
  userState()
})

const deleteFriend = () => {
  console.log('deleteFriend!')
  store.dispatch("friendModule/deleteFriend", {
    userAId: store.state.userModule.user.userId,
    userBId: props.friend.userId
  })
  deleteFriendClose()
}

const followFriend = () => {
  emit('followFriend', props.friend.userId)
}

const deleteFriendOpen = () => {
  document.getElementsByClassName("delete-friend-modal-wrap")[props.idx].style.display = 'block';
  document.getElementsByClassName("delete-friend-modal-bg")[props.idx].style.display = 'block';
}

const deleteFriendClose = () => {
  document.getElementsByClassName("delete-friend-modal-wrap")[props.idx].style.display = 'none';
  document.getElementsByClassName("delete-friend-modal-bg")[props.idx].style.display = 'none';
}
</script>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  width: 86%;
  height: 10%;
  color: white;
  /* background: linear-gradient(180deg, #959595 202.91%, rgba(0, 0, 0, 0.709847) 260.73%, rgba(84, 84, 84, 0) 302.91%); */
  background: #252836;
  border-radius: 20px;
  padding: 7%;
  font-size: 1rem;
  margin: 2.5% 0;
  transition: 0.1s ease-in;
}

.wrapper:hover {
  transform: scale(1.05, 1.05);
}

.first-line {
  display: grid;
  grid-template-columns: 1fr 8fr;
  align-items: center;
}

.friend-state {
  width: 1rem;
  height: 1rem;
  background: radial-gradient(50% 50% at 50% 50%, #EE1818 0%, rgba(208, 106, 106, 0) 100%);
}

.name-and-cancel {
  display: flex;
  justify-content: space-between;
}

.delete-friend {
  color: white;
  border: none;
  text-decoration: none;
  background: none;
  cursor: pointer;
}

.delete-friend:hover {
  color: #e27b66;
}

.second-line {
  padding-top: 2%;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}

.follow-friend {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30%;
  /* background: grey; */
  background: #e27b66;
  box-shadow: 0px 0px 15px #e27b66;
  border-radius: 20px;
  cursor: pointer;
  color: black;
}

.follow-friend:hover {
  opacity: 0.75;
  cursor: pointer;
}

/* 친구 삭제 팝업창 */
.delete-friend-modal-bg {
  display: none;
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
  transition: 0.5s ease-out;
  backdrop-filter: blur(4px) brightness(60%);
}

.delete-friend-modal-wrap {
  display: none;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 30%;
  height: 30%;
  background: #252836;
  border: solid 2px #e27b66;
  border-radius: 2rem;
  z-index: 1000;
}

.delete-popup {
  display: flex;
  align-items: center;
  height: 100%;
  width: 100%;
  /* grid-template-rows: 1fr 11fr; */
  transition: 0.5s ease-out;
  color: white;
}

/* .popup-header {
  background-color: black;
  height: 100%;
  width: 100%;
  border-bottom: solid 0.5rem #6E0000;
  border-radius: 1rem 1rem 0 0;
} */

.popup-content {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 70%;
  width: 80%;
  margin: 10%;
}

.btn-wrapper {
  display: flex;
  justify-content: space-around;
}

.popup-header-title {
  color: white;
  font-size: 1.3rem;
  padding: 0 5%;
  padding-top: 1%;
}

.el-button {
  background-color: #e27b66 !important;
  color: black !important;
  border: none;
}

.el-button:hover {
  opacity: 0.75;
  cursor: pointer;
}

/* 친구삭제 팝업창 end */
</style>