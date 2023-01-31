<template>
  <div class="wrapper">
    <div class="first-line">
      <div class="user-state"></div>
      <div class="name-and-cancel">
        <div>{{ props.user.nickname }}</div>
      </div>
    </div>
    <div class="second-line">
      <img src="../../../../assets/beer.png" alt="맥주 이미지" style="width:10%; height:15%; ">
      <div>{{ props.user.mannerPoint }}</div>
      <img src="../../../../assets/dokuri.png" alt="사케 이미지" style="width:10%; height:15%; ">
      <div>{{ props.user.alcoholPoint }}잔</div>
      <div class="request-friend" type="button" @click="sendRequest">친구요청</div>
    </div>
  </div>
  <div class="request-friend-modal-bg" @click="requestFriendClose"></div>
	<div class="request-friend-modal-wrap">
		<div class="request-popup">
			<div class="popup-header">
				<div class="popup-header-title">Send the Request</div>
			</div>
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
import { defineProps } from "vue";
import { useStore } from 'vuex'

const props = defineProps({
  user: Object
})
const store = useStore()

const sendRequest = () => {
  console.log('send the Request!')
  store.dispatch('friendModule/sendRequest', {
    userAId: store.state.userModule.userId,
    userBId: props.user.userId
  })
  requestFriendOpen()
}

// 친구 요청 완료창
const requestFriendOpen = () => {
	document.getElementsByClassName("request-friend-modal-wrap")[0].style.display ='block';
	document.getElementsByClassName("request-friend-modal-bg")[0].style.display ='block';
}
const requestFriendClose = () => {
    document.getElementsByClassName("request-friend-modal-wrap")[0].style.display ='none';
    document.getElementsByClassName("request-friend-modal-bg")[0].style.display ='none';
}
</script>

<style scoped>
.wrapper {
  width: 100%;
  height: 10%;
  color: white;
  background: linear-gradient(180deg, #959595 202.91%, rgba(0, 0, 0, 0.709847) 260.73%, rgba(84, 84, 84, 0) 302.91%);
  border-radius: 20px;
  padding: 7%;
  font-size: 1rem;
  margin: 2.5% 0;
}
.first-line{
  display: grid;
  grid-template-columns: 1fr 8fr;
  align-items: center;
}
.user-state{
  width: 1rem;
  height: 1rem;
  /* 온라인 표시 */
  background: radial-gradient(50% 50% at 50% 50%, #1CEE18 0%, rgba(108, 208, 106, 0) 100%);
  /* 오프라인 표시
  background: radial-gradient(50% 50% at 50% 50%, #EE1818 0%, rgba(208, 106, 106, 0) 100%); */
}

.name-and-cancel{
  display: flex;
  justify-content: space-between;
}
.second-line{
  padding-top: 2%;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}
.request-friend{
  display: flex;
  justify-content: center;
  align-items: center;
  width:30%;
  background: grey;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  cursor:pointer;
} 
.request-friend:hover{
  background: #823D3D;
}
/* 친구 요청 확인창 */
.request-friend-modal-bg {
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
.request-friend-modal-wrap {
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
.request-popup {
	display: grid;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 11fr;
	transition: 0.5s ease-out;
}
.popup-header {
	background-color: black;
  height: 100%;
	width: 100%;
  border-bottom: solid 0.5rem #6E0000;
	border-radius: 1rem 1rem 0 0;
}
.popup-content {
	/* display: flex;
  justify-content: center;
	align-items: center;
	flex-direction: column; */
  display: grid;
  text-align: center;
  grid-template-rows: 2fr 1fr;
  height: 50%;
	width: 80%;
	margin: 10%;
}
.btn-wrapper{
  display: flex;
  justify-content: space-around;
}
.popup-header-title {
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