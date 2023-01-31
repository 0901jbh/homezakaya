<template>
  <div class="wrapper">
    <div class="first-line">
      <div>{{ props.request.nickname }} 님께서</div>
      <button class="cancel" type="button" @click="deleteRequestOpen">X</button>
    </div>
    <div class="second-line">친구요청을 보냈습니다.</div>
    <div class="third-line">
      <img src="../../../../assets/beer.png" alt="맥주 이미지" style="width:10%; height:15%; ">
      <div>{{ props.request.mannerPoint }}</div>
      <img src="../../../../assets/dokuri.png" alt="사케 이미지" style="width:10%; height:15%; ">
      <div>{{ props.request.alcoholPoint }}잔</div>
      <div class="approve" type="button" @click="approveRequest">수락</div>
    </div>
  </div>
  <!-- popup 창 -->
  <div class="delete-request-modal-bg" @click="deleteRequestClose"></div>
	<div class="delete-request-modal-wrap">
		<div class="delete-popup">
			<div class="popup-header">
				<div class="popup-header-title">Refuese Request</div>
			</div>
			<div class="popup-content">
				<div>친구요청을 거절하시겠습니까?</div>
        <div class="btn-wrapper">
          <div class="btn">
            <RouterLink to="/rooms" style="text-decoration:none;">
              <el-button type="info" size="large" @click="deleteRequest">예</el-button>
            </RouterLink>
          </div>
          <div class="btn">
            <RouterLink to="/rooms" style="text-decoration:none;">
              <el-button type="info" size="large" @click="deleteRequestClose">아니요</el-button>
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
import { useStore } from 'vuex';

const props = defineProps({
  request: Object
})

const store = useStore()

const deleteRequest = () => {
  console.log('deleteRequest!')
  store.dispatch('friendModule/deleteRequest', {
    userAId: store.state.userModule.userId,
    userBId: props.request.userId
  })
  deleteRequestClose()
}

const approveRequest = () => {
  console.log('approveRequest!')
  store.dispatch('friendModule/approveRequest', {
    userAId: store.state.userModule.userId,
    userBId: props.request.userId
  })
}

const deleteRequestOpen = () => {
	document.getElementsByClassName("delete-request-modal-wrap")[0].style.display ='block';
	document.getElementsByClassName("delete-request-modal-bg")[0].style.display ='block';
}

const deleteRequestClose = () => {
    document.getElementsByClassName("delete-request-modal-wrap")[0].style.display ='none';
    document.getElementsByClassName("delete-request-modal-bg")[0].style.display ='none';
}
</script>

<style scoped>
.wrapper {
  width: 100%;
  height: 15%;
  color: white;
  background: linear-gradient(180deg, #A44242 202.91%, rgba(0, 0, 0, 0.709847) 260.73%, rgba(84, 84, 84, 0) 302.91%);
  border-radius: 20px;
  padding: 7%;
  font-size: 1rem;
  margin: 2.5% 0;
}
.first-line{
  display: flex;
  justify-content: space-between;
}
.cancel{
  color:white;
  border: none;
  text-decoration: none;
  background:none;
  cursor:pointer;
}
.cancel:hover {
  color: red;
}
.third-line{
  padding-top: 2%;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}
.approve{
  display: flex;
  justify-content: center;
  align-items: center;
  width:30%;
  background: #823D3D;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  cursor:pointer;
} 
.approve:hover{
  background: grey;
}

/* 친구 요청 삭제 팝업창 */
.delete-request-modal-bg {
	display:none;
	width:100%;
	height:100%;
	position:fixed;
	top:0;
	left:0;
	right:0;
	z-index:999;
}
.delete-request-modal-wrap {
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
.delete-popup {
	display: grid;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 11fr;
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
/* 친구삭제 팝업창 end */
</style>