<template>
	<div class="tabs">
		<input id="friend" type="radio" name="tab_item" checked>
		<label class="tab_item" id="tab_first" for="friend">Friend</label>
		<input id="user" type="radio" name="tab_item">
		<label class="tab_item" id="tab_second" for="user">User</label>
    <div class="tab_content" id="friend_content">
			<FriendRequestItem/>
      <UserItem v-for="i in 10" :key="i"/>
    </div>
    <div class="tab_content" id="user_content">
      <!-- <div class="search-wrapper"> -->
      <input 
        class="search-bar"
        type="text" 
        v-model="search.userInput" 
        @keyup.enter="searchUser"
        placeholder="press enter key for search">
        <!-- <div class="search-btn">검색</div> -->
      <!-- </div> -->
      <UserItem v-for="i in 10" :key="i"/>
		</div>
	</div>
</template>

<script setup>
import { ref } from 'vue' 
import UserItem from './UserItem.vue'
import FriendRequestItem from './FriendRequestItem.vue'

const search = ref({
  userInput: '',
})

const searchUser = () => {
  console.log("searchUser !!")
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
  border-bottom: 3px solid #333333;
	border-radius: 30px;
  background-color: #f8f8f8;
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
	flex-direction: row;
	flex-wrap: wrap;
	overflow-y: scroll;
}

/* 스크롤 가리기 */
.tab_content::-webkit-scrollbar {
	display: none;
}

/* 선택된 탭 스타일 */
.tabs input:checked + .tab_item {
  background-color: #333333;
  color: #fff;
}
/* .search-wrapper{
  width: 100%;
  display: grid;
  grid-template-columns: 8fr 3fr;
} */
.search-bar{
  /* height: 70%; */
  width: 100%;
  font-size: 1.3rem;
  padding: 2% 5%;
  margin-bottom: 2.5%;
  border: 0;
  border-radius: 15px;
  outline: none;
  background-color: rgb(233, 233, 233);
}
/* .search-btn{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 70%;
  width: 100%;  
  border: solid black 0.2rem;
  border-radius: 10px;
  text-decoration: none;
  color: white;
  background:black;
  cursor:pointer;
} */
</style>