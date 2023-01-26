<template>
	<div class="rooms-container">
		<div class="create-room">
			<div class="create-room-wrapper btn-open" @click="popOpen">
				<div>+</div>
			</div>
		</div>
		<div class="room" v-for="i in 6" :key="i">
			<RoomItem class="room-wrapper" />
		</div>
	</div>
	<div class="modal-bg" @click="popClose"></div>
	<div class="modal-wrap">
		<div class="create-room-popup">
			<div style="background-color:black;"></div>
			modal sample
			<button class="modal-close" @click="popClose">닫기</button>
		</div>
	</div>
</template>

<script setup>
import RoomItem from './RoomItem.vue'
const hi = () => {
	console.log('hi')
}
const popOpen = () => {
	document.getElementsByClassName("modal-wrap")[0].style.display ='block';
	document.getElementsByClassName("modal-bg")[0].style.display ='block';
}

const popClose = () => {
    document.getElementsByClassName("modal-wrap")[0].style.display ='none';
    document.getElementsByClassName("modal-bg")[0].style.display ='none';
}
</script>

<style scoped>
.rooms-container {
	display: flex;
	width: 100%;
	height: 100%;
	flex-direction: row;
	flex-wrap: wrap;
	overflow-y: scroll;
}

.rooms-container::-webkit-scrollbar {
	display: none;
}

.create-room {
	width: 30%;
	height: 40%;
	background-image: url('../../../../assets/table.png');
	background-position: center;
	background-size: contain;
	background-repeat: no-repeat;
	margin-right: 2%;
	margin-bottom: 2%;
}

.create-room-wrapper {
	display: flex;
	width: 100%;
	height: 100%;
	font-size: 3rem;
	justify-content: center;
	align-items: center;
}

.room {
	width: 30%;
	height: 40%;
	background-image: url('../../../../assets/table.png');
	background-position: center;
	background-size: contain;
	background-repeat: no-repeat;
	margin-right: 2%;
	margin-bottom: 2%;
}

.room-wrapper {
	display: flex;
	width: 100%;
	height: 100%;
	font-size: 3rem;
	justify-content: center;
	align-items: center;
}

.create-room .room {
	transition: 0.5s ease-out;
}

.create-room:hover {
	transform: scale(1, 1.2);
}

.room:hover {
	transform: scale(1, 1.2);
}

/* test---------------------- */
.modal-bg {
	display:none;
	width:100%;
	height:100%;
	position:fixed;
	top:0;
	left:0;
	right:0;
	z-index:999;
}
.modal-wrap {
	display:none;
	position:absolute;
	top:50%;
	left:50%;
	transform:translate(-50%,-50%);
	width:500px;
	height:500px;
	background:white;
	border: solid 5px black;
	border-radius: 20px;
	z-index:1000;
}
.create-room-popup {
	display: grid;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 5fr;
}
</style>