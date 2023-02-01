<template>
  <div class="room-state" @click="enterRoom">
    <div>{{ props.room.title }}</div>
    <div>{{ props.room.category }}</div>
    <div>{{ props.room.personCount }} / {{ props.room.personLimit }}</div>
  </div>
	<!-- 비공개방 비밀번호 입력창 -->
  <div class="private-modal-bg" @click="privatePopClose"></div>
	<div class="private-modal-wrap">
		<div class="private-popup">
			<div class="private-popup-header">
				<div class="private-popup-header-title">Private Room</div>
			</div>
			<div class="popup-content">
				<div>해당 방은 비공개방입니다.</div>
        <div>비밀번호를 입력해주세요.</div>
        <div>
					<el-form-item label="비밀번호">
						<el-input v-model="data.userInput" placeholder="비밀번호를 입력해주세요" show-password />
					</el-form-item>
				</div>
        <div>
          <el-button type="info" size="large" @click="enterRoom">Enter</el-button>
          <el-button type="info" size="large" @click="privatePopClose">Cancel</el-button>
        </div>
			</div>
		</div>
	</div>
	<!-- 비밀번호 입력 실패 팝업창 -->
	<div class="error-modal-bg" @click="requestFriendClose"></div>
	<div class="error-modal-wrap">
		<div class="error-popup">
			<div class="error-popup-header">
				<div class="error-popup-header-title">Wrong Password</div>
			</div>
			<div class="error-popup-content">
				<div>비밀번호가 틀렸습니다.</div>
        <div class="error-btn-wrapper">
          <div class="btn">
						<el-button type="info" size="large" @click="requestFriendClose">확인</el-button>
          </div>
        </div>
			</div>
		</div>
	</div>
  <div></div>
</template>

<script setup>
import { defineProps, onBeforeMount, ref } from 'vue';
import { useStore } from 'vuex';


const props = defineProps({
  room: Object
})
const data = ref({
  userInput: '',
  private: false,
})
const store = useStore()

const isPrivate = onBeforeMount(() => {
  if (props.room.password != '') {
    data.value.private = true;
  }
})

const enterRoom = () => {
  if (!data.value.private) {
    store.dispatch('roomModule/enterRoom', props.room.roomId)
	} else if (data.value.userInput == '') {
		privatePopOpen()
	} else {
		if (store.dispatch('roomModule/checkPassword',{
			roomId: props.room.roomId,
			password: data.value.userInput
		})) {
			store.dispatch('roomModule/enterRoom', props.room.roomId)
		} else {
			errorOpen()
		}
	}
}

const privatePopOpen = () => {
	document.getElementsByClassName("private-modal-wrap")[0].style.display ='block';
	document.getElementsByClassName("private-modal-bg")[0].style.display ='block';
}

const privatePopClose = () => {
    document.getElementsByClassName("private-modal-wrap")[0].style.display ='none';
    document.getElementsByClassName("private-modal-bg")[0].style.display ='none';
		data.value.userInput = '';
}

const errorOpen = () => {
	document.getElementsByClassName("error-modal-wrap")[0].style.display ='block';
	document.getElementsByClassName("error-modal-bg")[0].style.display ='block';
}
const errorClose = () => {
    document.getElementsByClassName("error-modal-wrap")[0].style.display ='none';
    document.getElementsByClassName("error-modal-bg")[0].style.display ='none';
}
</script>

<style scoped>
.room-state{
  font-size: 1rem;
  cursor: pointer;
}

/* 비공개방 비밀번호 입력 */
.private-modal-bg {
	display:none;
	width:100%;
	height:100%;
	position:fixed;
	top:0;
	left:0;
	right:0;
	z-index:997;
}
.private-modal-wrap {
	display:none;
	position:absolute;
	top:50%;
	left:50%;
	transform:translate(-50%,-50%);
	width:35%;
	height:60%;
	background:white;
	border: solid 5px black;
	border-radius: 2rem;
	z-index:998;
}
.private-popup {
	display: grid;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 11fr;
}
.private-popup-header {
	background-color: black;
  height: 100%;
	width: 100%;
  border-bottom: solid 0.5rem #6E0000;
	border-radius: 1rem 1rem 0 0;
}
.private-popup-content {
	display: flex;
	align-items: start;
	flex-direction: column;
	width: 80%;
	margin: 0% 10%;
	margin-top: 10%;
}
.private-popup-header-title {
	color:white;
	font-size: 1.3rem;
	padding: 0 5%;
	padding-top: 1%;
}



/* 비밀번호 오류 팝업창 */
.error-modal-bg {
	display:none;
	width:100%;
	height:100%;
	position:fixed;
	top:0;
	left:0;
	right:0;
	z-index:999;
	transition: 0.5s ease-out;
}
.error-modal-wrap {
	display:none;
	position:absolute;
	top:50%;
	left:50%;
	transform:translate(-50%,-50%);
	width:30%;
	height:30%;
	background:white;
	border: solid 5px black;
	border-radius: 2rem;
	z-index:1000;
}
.error-popup {
	display: grid;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 11fr;
	transition: 0.5s ease-out;
}
.error-popup-header {
	background-color: black;
  height: 100%;
	width: 100%;
  border-bottom: solid 0.5rem #6E0000;
	border-radius: 1rem 1rem 0 0;
}
.error-popup-content {
  display: grid;
  text-align: center;
  grid-template-rows: 2fr 1fr;
  height: 50%;
	width: 80%;
	margin: 10%;
}
.error-btn-wrapper{
  display: flex;
  justify-content: space-around;
}
.error-popup-header-title {
	color:white;
	font-size: 1.3rem;
	padding: 0 5%;
	padding-top: 1%;
}
.el-button {
  background-color: black !important;
  color: white !important;
}
.el-button:hover {
  background-color: rgb(118, 118, 118) !important;
  transition: 0.2s;
}
</style>