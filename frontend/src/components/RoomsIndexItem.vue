<template>
	<div class="rooms-container">
		<div class="create-room">
			<div class="create-room-wrapper">
				<div>+</div>
			</div>
		</div>
		<div class="room" v-for="i in 6" :key="i">
			<RoomItem class="room-wrapper"/>
		</div>
	</div>
</template>

<script setup>
import RoomItem from './RoomItem.vue'
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
	background-image: url('../assets/table.png');
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
	background-image: url('../assets/table.png');
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
</style>