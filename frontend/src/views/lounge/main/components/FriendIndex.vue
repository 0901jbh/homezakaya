<template>
	<div class="tabs">
		<input id="friend" type="radio" name="tab_item" checked>
		<label class="tab_item" id="tab_first" for="friend">Friend</label>
		<input id="user" type="radio" name="tab_item">
		<label class="tab_item" id="tab_second" for="user">User</label>
    <div class="tab_content" id="friend_content">
			<FriendRequestItem 
        v-for="request in data.requests" 
        :key="request"
        :request="request"
      />
      <FriendItem 
        v-for="friend in data.friends"
        :key="friend"
        :friend="friend"
      />
    </div>
    <div class="tab_content" id="user_content">
      <input 
        class="search-bar"
        type="text" 
        v-model="data.userInput" 
        @keyup.enter="searchUser"
        placeholder="press enter key for search">
      <UserItem 
        v-for="user in data.searchUsers" 
        :key="user" 
        :user="user"
        :friends="data.friends"
      />
		</div>
	</div>
</template>

<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import UserItem from './UserItem.vue'
import FriendItem from './FriendItem.vue'
import FriendRequestItem from './FriendRequestItem.vue'
import { useStore } from 'vuex'

const data = ref({
  userInput: '',
  requests: [],
  friends: [],
  searchUsers: [],
})

const store = useStore()

data.value.requests = computed(() => store.getters["friendModule/getRequests"])
data.value.friends = computed(() => store.getters["friendModule/getFriends"])
data.value.searchUsers = computed(() => store.getters["friendModule/getSearchUsers"])

const getRequests = onBeforeMount(() => {
  store.dispatch("friendModule/getRequests", store.state.userModule.userId)
})

const getFriends = onBeforeMount(() => {
  store.dispatch("friendModule/getFriends", store.state.userModule.userId)
})

const searchUser = () => {
  store.dispatch("friendModule/searchUser", data.value.userInput)
}
</script>

<style scoped>
/* 탭 전체 스타일 */
.tabs {
  margin-top: 3vh;
	margin-left: 5vh;
	height: 81vh;
}

/* 탭 스타일 */
.tab_item {
  width: 40%;
  height: 8%;
  font-size: 1.3rem;
  font-weight: 700;
	border-radius: 30px;
  background-color: #f8f8f8;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5);
  line-height: 50px;
  font-size: 16px;
  text-align: center;
  color: #333333;
  display: block;
  float: left;
  text-align: center;
  font-weight: bold;
  transition: all 0.2s ease;
	margin: 2vh;
	margin-bottom: 3vh;
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
	background: #FFFFFF;
  border: 0.3rem solid #000000;
  border-radius: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

/* 선택 된 탭 콘텐츠를 표시 */
#friend:checked ~ #friend_content,
#user:checked ~ #user_content {
  display: flex;
	flex-direction: column;
	flex-wrap: wrap;
	overflow-y: scroll;
}

/* 스크롤 가리기 */
.tab_content::-webkit-scrollbar {
	display: none;
}

/* 선택된 탭 스타일 */
.tabs input:checked + .tab_item {
  background-color: black;
  color: #fff;
}
.search-bar{
  height: 5%;
  width: 90%;
  font-size: 1.3rem;
  padding: 2% 5%;
  margin-bottom: 2.5%;
  border: 0;
  border-radius: 15px;
  outline: none;
  background-color: rgb(233, 233, 233);
}
</style>