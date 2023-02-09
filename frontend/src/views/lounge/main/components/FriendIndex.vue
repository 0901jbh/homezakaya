<template>
  <div class="tabs">
    <input id="friend" type="radio" name="tab_item" checked>
    <label class="tab_item" id="tab_first" for="friend">친구 목록</label>
    <input id="user" type="radio" name="tab_item">
    <label class="tab_item" id="tab_second" for="user">친구 찾기</label>
    <div class="tab_content" id="friend_content">
      <FriendInviteItem v-for="(invite, idx) in data.invites " :key="idx" :invite="invite"/>
      <FriendRequestItem v-for="(request, idx) in data.requests" :key="idx" :idx="idx" :request="request" />
      <FriendItem v-for="(friend, idx) in data.friends" :key="idx" :idx="idx" :friend="friend"
        @followFriend="userInRoom" />
    </div>
    <div class="tab_content" id="user_content">
      <input class="search-bar" type="text" v-model="data.userInput" @keyup.enter="searchUser"
        placeholder="press enter key for search">
      <UserItem v-for="(user, idx) in data.searchUsers" :key="idx" :idx="idx" :user="user" :friends="data.friends"
        :renderKey="data.renderKey" />
    </div>
  </div>
  <!-- 따라가기 거부됨 팝업창 -->
  <div class="user-not-in-modal-bg" @click="notInRoomClose"></div>
  <div class="user-not-in-modal-wrap">
    <div class="user-not-in-popup">
      <!-- <div class="user-not-in-popup-header">
        <div class="user-not-in-popup-header-title">Refuse Follow Friend</div>
      </div> -->
      <div class="user-not-in-popup-content">
        <div>해당 친구는 참여중인 방이 없습니다.</div>
        <div class="btn-wrapper">
          <div class="btn">
            <RouterLink to="/rooms" style="text-decoration:none;">
              <el-button type="info" size="large" @click="notInRoomClose">확인</el-button>
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 비공개방 비밀번호 입력창 -->
  <div class="follow-private-modal-bg" @click="followPrivatePopClose"></div>
  <div class="follow-private-modal-wrap">
    <div class="follow-private-popup">
      <!-- <div class="follow-private-popup-header">
        <div class="follow-private-popup-header-title">비밀번호 입력</div>
      </div> -->
      <div class="follow-private-popup-content">
        <div style="margin-bottom:3%;">해당 방은 비공개방입니다.</div>
        <div style="margin-bottom:5%;">비밀번호를 입력해주세요.</div>
        <div>
          <el-form-item label="비밀번호">
            <el-input v-model="data.passwordInput" placeholder="비밀번호를 입력해주세요" show-password />
          </el-form-item>
        </div>
        <div>
          <el-button type="info" size="large" @click="followEnterPrivateRoom">Enter</el-button>
          <el-button type="info" size="large" @click="followPrivatePopClose">Cancel</el-button>
        </div>
      </div>
    </div>
  </div>
  <!-- 비밀번호 입력 실패 팝업창 -->
  <div class="follow-error-modal-bg" @click="followErrorClose"></div>
  <div class="follow-error-modal-wrap">
    <div class="follow-error-popup">
      <!-- <div class="follow-error-popup-header">
        <div class="follow-error-popup-header-title">Wrong Password</div>
      </div> -->
      <div class="follow-error-popup-content">
        <div class="sentence">비밀번호가 틀렸습니다.</div>
        <div class="follow-error-btn-wrapper">
          <div class="btn">
            <el-button type="info" size="large" @click="followErrorClose">확인</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div></div>
</template>

<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import FriendInviteItem from './FriendInviteItem.vue'
import UserItem from './UserItem.vue'
import FriendItem from './FriendItem.vue'
import FriendRequestItem from './FriendRequestItem.vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

const router = useRouter()

const data = ref({
  userInput: '',
  invites: [],
  requests: [],
  friends: [],
  searchUsers: [],
  passwordInput: '',
  followRoomId: -1,
  renderKey: 0,
})

const store = useStore()
const emit = defineEmits(['followFriend'])

data.value.requests = computed(() => store.getters["friendModule/getRequests"])
data.value.friends = computed(() => store.getters["friendModule/getFriends"])
data.value.searchUsers = computed(() => store.getters["friendModule/getSearchUsers"])
data.value.invites = computed(() => store.getters["roomModule/getRequests"])

const getRequests = onBeforeMount(() => {
  store.dispatch("friendModule/getRequests", store.state.userModule.user.userId)
})

const getFriends = onBeforeMount(() => {
  store.dispatch("friendModule/getFriends", store.state.userModule.user.userId)
})

const getInvites = onBeforeMount(() => {
  store.dispatch("roomModule/getInvitesList", store.state.userModule.user.userId)
})

const searchUser = () => {
  store.dispatch("friendModule/searchUser", {
    userInput: data.value.userInput,
    userId: store.state.userModule.user.userId,
  })
  data.value.renderKey += 1
  console.log(data.value.renderKey)
}

const notInRoomOpen = () => {
  document.getElementsByClassName("user-not-in-modal-wrap")[0].style.display = 'block';
  document.getElementsByClassName("user-not-in-modal-bg")[0].style.display = 'block';
}

const notInRoomClose = () => {
  document.getElementsByClassName("user-not-in-modal-wrap")[0].style.display = 'none';
  document.getElementsByClassName("user-not-in-modal-bg")[0].style.display = 'none';
}

const userInRoom = (userId) => {
  store.dispatch("roomModule/getRoomId", userId).then((result) => {
    if (result == -1) {
      notInRoomOpen()
    }
    else {
      store.dispatch("roomModule/checkValidRoom", result).then((status) => {
        if(status != 200){
          errorOpen(status);
        }
        else{
          const room = store.state.roomModule.room.find(r => r.roomId == result);
          if(room.private){
            followPrivatePopOpen();
          }
          else{
            router.push({ name: 'wait', params: { roomId: result } });
          }
        }
      });
    }
  })
}

const followEnterPrivateRoom = () => {
  store.dispatch("roomModule/checkPassword", {
    roomId: data.value.followRoomId,
    password: data.value.passwordInput,
  }).then((result) => {
    if (result) {
      console.log(`${data.value.followRoomId}번에 입장시도`)
      enterRoom(data.value.followRoomId)
    }
    else {
      followErrorOpen(1)
    }
  })
}

// const enterRoom = (roomId) => {
//   store.dispatch('roomModule/createUserInRoom', {
//     userId: store.state.userModule.user.userId,
//     roomId: roomId,
//   })
//     .then((result) => {
//       if (result) {
//         store.dispatch('roomModule/enterRoom', roomId)
//           .then((result) => {
//             if (result) {
//               router.push({ name: 'wait', params: { roomId: roomId } })
//             }
//             else {
//               followErrorOpen(2)
//             }
//           })
//       }
//       else {
//         followErrorOpen(2)
//       }
//     })
// }

const followPrivatePopOpen = () => {
  document.getElementsByClassName("follow-private-modal-wrap")[0].style.display = 'block';
  document.getElementsByClassName("follow-private-modal-bg")[0].style.display = 'block';
}

const followPrivatePopClose = () => {
  document.getElementsByClassName("follow-private-modal-wrap")[0].style.display = 'none';
  document.getElementsByClassName("follow-private-modal-bg")[0].style.display = 'none';
  data.value.passwordInput = '';
}

const followErrorOpen = (state) => {
  // let titleTag = document.querySelector(".follow-error-popup-header-title");
  let sentenceTag = document.querySelector(".sentence");
  if (state == 1) {
    // titleTag.innerHTML = "Wrong Password";
    sentenceTag.innerHTML = "비밀번호가 틀렸습니다.";
  } else {
    // titleTag.innerHTML = "Refuse Enter Room";
    sentenceTag.innerHTML = "입장에 실패했습니다.";
  }
  document.getElementsByClassName("follow-error-modal-wrap")[0].style.display = 'block';
  document.getElementsByClassName("follow-error-modal-bg")[0].style.display = 'block';
}
const followErrorClose = () => {
  document.getElementsByClassName("follow-error-modal-wrap")[0].style.display = 'none';
  document.getElementsByClassName("follow-error-modal-bg")[0].style.display = 'none';
  data.value.passwordInput = '';
}
</script>

<style scoped>
/* 탭 전체 스타일 */
.tabs {
  width: 100%;
  height: 85vh;
}

/* 탭 스타일 */
.tab_item {
  width: 40%;
  height: 8%;
  font-size: 1.3rem;
  font-weight: 700;
  border-radius: 30px;
  background-color: #1f1d2b;
  /* box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5); */
  box-shadow: 0px 0px 15px #1f1d2b;
  line-height: 50px;
  font-size: 16px;
  text-align: center;
  color: white;
  display: block;
  float: left;
  text-align: center;
  font-weight: bold;
  transition: all 0.2s ease;
  margin: 5%;

  display: flex;
  justify-content: center;
  align-items: center;
}

.tab_item:hover {
  opacity: 0.75;
  cursor: pointer;
}

/* 라디오 버튼 UI삭제*/
input[name="tab_item"] {
  display: none;
}

/* 탭 컨텐츠 스타일 */
.tab_content {
  display: none;
  padding: 20px 20px 0;
  clear: both;
  overflow: hidden;
  height: 83%;
  background: #1f1d2b;
  /* border: 0.3rem solid #000000; */
  border-radius: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

/* 선택 된 탭 콘텐츠를 표시 */
#friend:checked~#friend_content {
  display: flex;
  flex-direction: column;
  overflow-y: scroll;
}

#user:checked~#user_content {
  display: flex;
  flex-direction: column;
  overflow-y: scroll;
}

/* 스크롤 가리기 */
.tab_content::-webkit-scrollbar {
  display: none;
}

/* 선택된 탭 스타일 */
.tabs input:checked+.tab_item {
  background-color: #e27b66;
  box-shadow: 0px 0px 15px #e27b66;
  color: black;
}

/* 검색창 */
.search-bar {
  height: 5%;
  width: 90%;
  font-size: 1.3rem;
  padding: 2% 5%;
  margin-bottom: 2.5%;
  border: 0;
  border-radius: 15px;
  outline: none;
  background-color: #252836;
  color: white;
}

/* .search-bar::placeholder {
  color: #cbcbcb;
} */

/* 친구가 방안에 없음 팝업창 */
.user-not-in-modal-bg {
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

.user-not-in-modal-wrap {
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

.user-not-in-popup {
  display: grid;
  height: 100%;
  width: 100%;
  /* grid-template-rows: 1fr 11fr; */
  transition: 0.5s ease-out;
  color: white;
}

/* .user-not-in-popup-header {
  background-color: black;
  height: 100%;
  width: 100%;
  border-bottom: solid 0.5rem #6E0000;
  border-radius: 1rem 1rem 0 0;
} */

.user-not-in-popup-content {
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

/* .user-not-in-popup-header-title {
  color: white;
  font-size: 1.3rem;
  padding: 0 5%;
  padding-top: 1%;
} */

.el-button {
  background-color: #e27b66 !important;
  color: black !important;
}

.el-button:hover {
  opacity: 0.75;
  cursor: pointer;
}

/* 비공개방 비밀번호 입력 */
.follow-private-modal-bg {
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

.follow-private-modal-wrap {
  display: none;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 30%;
  height: 40%;
  background: #252836;
  border: solid 2px #e27b66;
  border-radius: 2rem;
  z-index: 1000;
}

.follow-private-popup {
  display: grid;
  height: 100%;
  width: 100%;
  /* grid-template-rows: 1fr 11fr; */
  transition: 0.5s ease-out;
  color: white;
}

/* .follow-private-popup-header {
  background-color: #E27B66;
  height: 100%;
  width: 100%;
  border-bottom: solid 0.5rem #6E0000;
  border-radius: 1.6rem 1.6rem 0 0;
} */

.follow-private-popup-content {
  /* display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 65%;
  width: 80%;
  margin: 10%; */
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 80%;
  width: 80%;
  margin: 10%;
}

/* .follow-private-popup-header-title {
  color: black;
  font-size: 1.1rem;
  font-weight: 1000;
  padding: 0 5%;
  padding-top: 1%;
} */


/* 비밀번호 오류 팝업창 */
.follow-error-modal-bg {
  display: none;
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
  transition: 0.5s ease-out;
}

.follow-error-modal-wrap {
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

.follow-error-popup {
  display: flex;
  align-items: center;
  height: 100%;
  width: 100%;
  /* grid-template-rows: 1fr 11fr; */
  transition: 0.5s ease-out;
  color: white;
}

/* .follow-error-popup-header {
  background-color: black;
  height: 100%;
  width: 100%;
  border-bottom: solid 0.5rem #6E0000;
  border-radius: 1rem 1rem 0 0;
} */

.follow-error-popup-content {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 70%;
  width: 80%;
  margin: 10%;
}

.follow-error-btn-wrapper {
  display: flex;
  justify-content: space-around;
}

/* .follow-error-popup-header-title {
  color: white;
  font-size: 1.3rem;
  padding: 0 5%;
  padding-top: 1%;
} */
</style>