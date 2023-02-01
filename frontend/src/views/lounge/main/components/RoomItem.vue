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
			<div class="private-popup-content">
				<div style="margin-bottom:3%;">해당 방은 비공개방입니다.</div>
        <div style="margin-bottom:5%;">비밀번호를 입력해주세요.</div>
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
	<div class="error-modal-bg" @click="errorClose"></div>
	<div class="error-modal-wrap">
		<div class="error-popup">
			<div class="error-popup-header">
				<div class="error-popup-header-title">Wrong Password</div>
			</div>
			<div class="error-popup-content">
				<div>비밀번호가 틀렸습니다.</div>
        <div class="error-btn-wrapper">
          <div class="btn">
						<el-button type="info" size="large" @click="errorClose">확인</el-button>
          </div>
        </div>
			</div>
		</div>
	</div>
  <div></div>
</template>

<script setup>
import { defineProps, onBeforeMount, ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const router = useRouter()

const props = defineProps({
  room: Object
})
const data = ref({
  userInput: '',
  private: false,
	room: {},
})
const store = useStore()

const room = onBeforeMount(() => {
	data.value.room = props.room
})

const enterRoom = () => {
	console.log(`${data.value.room.roomId}번 방입니다.`)

	console.log(`${data.value.room.roomId}번방은 비밀번호가 있을까?`)

	console.log(props.room)
  if (data.value.room.password != '') {
    data.value.private = true;
		console.log('비밀번호가 필요한 비공개방입니다.')
  }

  if (!data.value.private) {
		console.log('비밀번호가 필요없는 공개방!')
    store.dispatch('roomModule/enterRoom', data.value.room.roomId)
		router.push({ name: 'room', params: { roomId: data.value.room.roomId } })
	} else if (data.value.userInput == '') {
		console.log('비밀번호가 필요한 비공개방이기 때문에 비밀번호 입력 필요 !')
		privatePopOpen()
	} else {
		if (store.dispatch('roomModule/checkPassword',{
			roomId: data.value.room.roomId,
			password: data.value.userInput
		})) {
			console.log("axios 결과 비밀번호가 일치함")
			store.dispatch('roomModule/enterRoom', data.value.room.roomId)
			router.push({ name: 'room', params: { roomId: data.value.room.roomId } })
		} else {
			console.log("axios 결과 비밀번호가 불일치함")
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
	width: 100%;
	height: 100%;
  font-size: 1rem;
  cursor: pointer;
	background-image: url('../../../../assets/table.png');
	background-position: center;
	background-size: contain;
	background-repeat: no-repeat;
}

.room-state:hover {
	transform: scale(1.1, 1.1);
	transition: 0.3s ease-out;
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
	z-index:999;
}
.private-modal-wrap {
	display:none;
	position:absolute;
	top:50%;
	left:50%;
	transform:translate(-50%,-50%);
	width:30%;
	height:40%;
	background:white;
	border: solid 5px black;
	border-radius: 2rem;
	z-index:1000;
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
	/* display: grid;
	text-align: center;
	grid-template-rows: 1fr 1fr 1fr 1fr; */
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: center;
	height: 65%;
	width: 80%;
	margin: 10%;
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