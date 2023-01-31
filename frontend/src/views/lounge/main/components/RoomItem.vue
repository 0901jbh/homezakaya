<template>
  <div class="room-state" @click="enterRoom">
    <div>{{ props.room.title }}</div>
    <div>{{ props.room.category }}</div>
    <div>{{ props.room.personCount }} / {{ props.room.personLimit }}</div>
  </div>
  <div class="private-modal-bg" @click="privatePopClose"></div>
	<div class="private-modal-wrap">
		<div class="private-popup">
			<div class="private-popup-header">
				<div class="private-popup-header-title">Private Room</div>
			</div>
			<div class="popup-content">
				<div>해당 방은 비공개방입니다.</div>
        <div>비밀번호를 입력해주세요.</div>
        <div><input class="password-input" type="text" v-model="userInput"></div>
        <div>
          <div @click="enterRoom">Enter</div>
          <div>Cancel</div>
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
  check: true,
})
const store = useStore()

const isPrivate = onBeforeMount(() => {
  if (props.room.password != '') {
    data.value.check = false
  }
})

const enterRoom = () => {
  if (data.value.check) {
    store.dispatch('roomModule/enterRoom', props.room.roomId)
  } else {
    privatePopOpen()
  }
}



const privatePopOpen = () => {
	document.getElementsByClassName("private-modal-wrap")[0].style.display ='block';
	document.getElementsByClassName("private-modal-bg")[0].style.display ='block';
}

const privatePopClose = () => {
    document.getElementsByClassName("private-modal-wrap")[0].style.display ='none';
    document.getElementsByClassName("private-modal-bg")[0].style.display ='none';
}

</script>

<style scoped>
.room-state{
  font-size: 1rem;
  cursor: pointer;
}

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
	width:35%;
	height:60%;
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

.password-input{
  height: 5%;
  width: 100%;
  font-size: 1.3rem;
  padding: 2% 5%;
  margin-bottom: 2.5%;
  border: 0;
  border-radius: 15px;
  outline: none;
  background-color: rgb(233, 233, 233);
}
</style>