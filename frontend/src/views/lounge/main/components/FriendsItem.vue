<template>
	<div class="tabs">
		<input id="friend" type="radio" name="tab_item" checked>
		<label class="tab_item" id="tab_first" for="friend">Friend</label>
		<input id="user" type="radio" name="tab_item">
		<label class="tab_item" id="tab_second" for="user">User</label>
    <div class="tab_content" id="friend_content">
			<MyFriendRequestItem/>
			<div v-for="i in 10	" :key="i">
				<MyFriendItem/>
			</div>
    </div>
    <div class="tab_content" id="user_content">
			<form action="">
      	<input type="text">
				<button value="submit">검색</button>
			</form>
			<div v-for="i in 10	" :key="i">
				<MyFriendItem/>
			</div>
		</div>
	</div>
</template>

<script setup>
import MyFriendItem from './MyFriendItem.vue'
import MyFriendRequestItem from './MyFriendRequestItem.vue'

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
  padding: 40px 40px 0;
  clear: both;
  overflow: hidden;
	height: 79%;
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
</style>