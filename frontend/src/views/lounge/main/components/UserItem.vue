<template>
  <div class="wrapper" v-if="props.user.userId != store.state.userModule.user.userId">
    <div class="first-line">
      <div class="user-state"></div>
      <div class="name-and-cancel">
        <div>{{ props.user.nickname }}</div>
      </div>
    </div>
    <div class="second-line">
      <img src="@/assets/images/manner_w.png" alt="manner_w img" style="height:25px;">
      <div>{{ parseFloat(props.user.mannerPoint.toFixed(2)) }}</div>
      <img src="@/assets/images/alcohol_w.png" alt="alcohol_w img" style="height:25px;">
      <div>{{ props.user.alcoholPoint }}잔</div>
      <div v-if="!isFriend" class="request-friend" type="button" @click="sendRequest">친구요청</div>
      <div v-if="isFriend" class="request-friend-none"></div>
    </div>
  </div>
  <div class="request-friend-modal-bg" @click="requestFriendClose"></div>
  <div class="request-friend-modal-wrap">
    <div class="request-popup">
      <!-- <div class="popup-header">
        <div class="popup-header-title">Send the Request</div>
      </div> -->
      <div class="popup-content">
        <div>친구요청을 보냈습니다.</div>
        <div class="btn-wrapper">
          <div class="btn">
            <RouterLink to="/rooms" style="text-decoration:none;">
              <el-button type="info" size="large" @click="requestFriendClose">확인</el-button>
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div></div>
</template>

<script setup>
import { ref, onMounted, onUpdated } from "vue";
import { useStore } from 'vuex'

const props = defineProps({
  user: Object,
  friends: Array,
  idx: Number,
  renderKey: Number,
})
const store = useStore()

function userState() {
  let userStateTag = document.getElementsByClassName("user-state")[props.idx]
  if (props.user.state == "offline") {
    userStateTag.style.background = "radial-gradient(50% 50% at 50% 50%, #EE1818 0%, rgba(208, 106, 106, 0) 100%)";
  } else {
    userStateTag.style.background = "radial-gradient(50% 50% at 50% 50%, #1CEE18 0%, rgba(108, 208, 106, 0) 100%)";
  }
}

const isFriend = ref(false)

const setFriend = () => {
  props.friends.some(function (element) {
    if (props.user.userId == element.userId) {
      isFriend.value = true
      return true
    }
  });
}

onMounted(() => {
  userState()
  setFriend()
})

onUpdated(() => {
  userState()
  setFriend()
})


const sendRequest = () => {
  console.log('send the Request!')
  store.dispatch('friendModule/sendRequest', {
    userAId: store.state.userModule.user.userId,
    userBId: props.user.userId
  })
  requestFriendOpen()
}

// 친구 요청 완료창
const requestFriendOpen = () => {
  document.getElementsByClassName("request-friend-modal-wrap")[props.idx].style.display = 'block';
  document.getElementsByClassName("request-friend-modal-bg")[props.idx].style.display = 'block';
}
const requestFriendClose = () => {
  document.getElementsByClassName("request-friend-modal-wrap")[props.idx].style.display = 'none';
  document.getElementsByClassName("request-friend-modal-bg")[props.idx].style.display = 'none';
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

.user-state {
  width: 1rem;
  height: 1rem;
  /* 온라인 표시 */
  background: radial-gradient(50% 50% at 50% 50%, #1CEE18 0%, rgba(108, 208, 106, 0) 100%);
  /* 오프라인 표시
  background: radial-gradient(50% 50% at 50% 50%, #EE1818 0%, rgba(208, 106, 106, 0) 100%); */
}

.name-and-cancel {
  display: flex;
  justify-content: space-between;
}

.second-line {
  padding-top: 2%;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}

.request-friend {
  display: flex;
  justify-content: center;
  align-items: center;
  /* height: 30%; */
  width: 30%;
  background: #e27b66;
  box-shadow: 0px 0px 15px #e27b66;
  border-radius: 20px;
  cursor: pointer;
  color: black;

  height: 1.3rem;
  font-size: 0.8rem;
}

.request-friend-none {
  width: 30%;
}

.request-friend:hover {
  opacity: 0.75;
  cursor: pointer;
}

/* 친구 요청 확인창 */
.request-friend-modal-bg {
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

.request-friend-modal-wrap {
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

.request-popup {
  display: flex;
  align-items: center;
  height: 100%;
  width: 100%;
  /* grid-template-rows: 1fr 11fr; */
  transition: 0.5s ease-out;
  color: white;
}

/* .popup-header {
  background-color: #E27B66;
  height: 100%;
  width: 100%;
  border-bottom: solid 0.5rem #6E0000;
  border-radius: 1.6rem 1.6rem 0 0;
} */

.popup-content {
  /* display: flex;
  justify-content: center;
	align-items: center;
	flex-direction: column; */
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

/* .popup-header-title {
  color: black;
  font-size: 1.3rem;
  padding: 0 5%;
  padding-top: 1%;
} */

.el-button {
  background-color: #e27b66 !important;
  color: black !important;
  border: none;
}

.el-button:hover {
  opacity: 0.75;
  cursor: pointer;
}
</style>