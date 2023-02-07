<template>
	<div class="room-state" @click="clickRoomIcon">
		<img v-if="props.room.category == '자유'" src="../../../../assets/free.png" alt="free img"
			style="height: 50%; align-self: center;">
		<img v-if="props.room.category == '맥주'" src="../../../../assets/beer.png" alt="beer img"
			style="height: 50%; align-self: center;">
		<img v-if="props.room.category == '소주'" src="../../../../assets/soju.png" alt="soju img"
			style="height: 50%; align-self: center;">
		<img v-if="props.room.category == '양주'" src="../../../../assets/whisky.png" alt="whisky img"
			style="height: 50%; align-self: center;">
		<img v-if="props.room.category == '사케'" src="../../../../assets/sake.png" alt="sake img"
			style="height: 50%; align-self: center;">
		<div class="title">{{ props.room.title }}</div>
		<div class="content">
			<div>{{ props.room.category }}</div>
			<div>{{ props.room.personCount }} / {{ props.room.personLimit }}</div>
			<div v-if="secret">
				<img src="../../../../assets/unlock.png" alt="unlock img" style="height: 80%;">
			</div>
			<div v-else>
				<img src="../../../../assets/lock.png" alt="lock img" style="height: 80%;">
			</div>
		</div>
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
					<el-button type="info" size="large" @click="clickEnterBtn">Enter</el-button>
					<el-button type="info" size="large" @click="privatePopClose">Cancel</el-button>
				</div>
			</div>
		</div>
	</div>
	<!-- 비밀번호 입력 실패 팝업창 -->
	<div class="error-modal-bg" @click="errorClose"></div>
	<div class="error-modal-wrap">
		<div class="error-popup">
			<!-- <div class="error-popup-header">
				<div class="error-popup-header-title">Wrong Password</div>
			</div> -->
			<div class="error-popup-content">
				<div class="error-sentence">비밀번호가 틀렸습니다.</div>
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
import { onMounted, computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const router = useRouter()
const store = useStore()

const props = defineProps({
	room: Object,
	idx: Number
})

const data = ref({
	userInput: '',
})

const clickRoomIcon = () => {
	store.dispatch("roomModule/checkPassword", {
		roomId: props.room.roomId,
		password: data.value.userInput,
	}).then((result) => {
		if (result) {
			console.log(`${props.room.roomId}번에 입장`)
			enterRoom()
		}
		else {
			privatePopOpen()
		}
	})
}

const clickEnterBtn = () => {
	store.dispatch("roomModule/checkPassword", {
		roomId: props.room.roomId,
		password: data.value.userInput,
	}).then((result) => {
		if (result) {
			enterRoom()
		}
		else {
			errorOpen(1)
		}
	})
}

const enterRoom = () => {
	store.dispatch('roomModule/createUserInRoom', {
		userId: store.state.userModule.user.userId,
		roomId: props.room.roomId,
	}).then((result) => {
		if (result) {
			store.dispatch('roomModule/enterRoom', props.room.roomId).then((result) => {
				if (result) {
					router.push({ name: 'room', params: { roomId: props.room.roomId } })
				}
				else {
					errorOpen(2)
				}
			})
		}
		else {
			errorOpen(2)
		}
	})
}

const privatePopOpen = () => {
	document.getElementsByClassName("private-modal-wrap")[props.idx].style.display = 'block';
	document.getElementsByClassName("private-modal-bg")[props.idx].style.display = 'block';
}

const privatePopClose = () => {
	document.getElementsByClassName("private-modal-wrap")[props.idx].style.display = 'none';
	document.getElementsByClassName("private-modal-bg")[props.idx].style.display = 'none';
	data.value.userInput = '';
}

const errorOpen = (state) => {
	// let titleTag = document.getElementsByClassName("error-popup-header-title")[props.idx];
	let sentenceTag = document.getElementsByClassName("error-sentence")[props.idx];
	if (state == 1) {
		// titleTag.innerHTML = "Wrong Password";
		sentenceTag.innerHTML = "비밀번호가 틀렸습니다.";
	} else {
		// titleTag.innerHTML = "Refuse Enter Room";
		sentenceTag.innerHTML = "입장에 실패했습니다.";
	}
	document.getElementsByClassName("error-modal-wrap")[props.idx].style.display = 'block';
	document.getElementsByClassName("error-modal-bg")[props.idx].style.display = 'block';
}

const errorClose = () => {
	document.getElementsByClassName("error-modal-wrap")[props.idx].style.display = 'none';
	document.getElementsByClassName("error-modal-bg")[props.idx].style.display = 'none';
	data.value.userInput = '';
}

const secret = ref()

onMounted(() => {
	store.dispatch("roomModule/checkPassword", {
		roomId: props.room.roomId,
		password: "",
	}).then((result) => {
		secret.value = result
	});
});

</script>

<style scoped>
.room-state {
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	align-items: center;
	background: linear-gradient(#252836 20%, #1f1d2b 40%);
	width: 100%;
	height: 100%;
	font-size: 1rem;
	cursor: pointer;
	border-radius: 10px;
	/* background-image: url('../../../../assets/table.png');
	background-position: center;
	background-size: contain;
	background-repeat: no-repeat; */
}

.room-state .title {
	display: flex;
	justify-content: center;
	align-items: center;
	color: white;
	width: 80%;
}

.room-state .content {
	display: flex;
	height: 20%;
	width: 100%;
	justify-content: center;
	text-align: center;
	color: white;
	padding-bottom: 20px;
	font-size: small;
}

.content div {
	display: flex;
	height: 100%;
	width: 25%;
	background: #252836;
	border-radius: 10px;
	padding: 3px;
	margin: 0px 5px;
	justify-content: center;
	align-items: center;
}

.room-state:hover {
	transform: scale(1.1, 1.1);
	transition: 0.3s ease-out;
	cursor: pointer;
}

/* 비공개방 비밀번호 입력 */
.private-modal-bg {
	display: none;
	width: 100%;
	height: 100%;
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 999;
}

.private-modal-wrap {
	display: none;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 30%;
	height: 40%;
	background: #252836;
	border: solid 2px #e27b66;
	border-radius: 2rem;
	z-index: 1000;
}

.private-popup {
	display: grid;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 11fr;
	transition: 0.5s ease-out;
	color: white;
}

.private-popup-header {
	background-color: #E27B66;
	height: 100%;
	width: 100%;
	/* border-bottom: solid 0.5rem #6E0000; */
	border-radius: 1.6rem 1.6rem 0 0;
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
	/* display: grid;
	text-align: center;
	grid-template-rows: 2fr 1fr;
	height: 30%;
	width: 80%;
	margin: 10%; */
}

.private-popup-header-title {
	color: black;
	font-size: 1.3rem;
	padding: 0 5%;
	padding-top: 1%;
}


/* 비밀번호 오류 팝업창 */
.error-modal-bg {
	display: none;
	width: 100%;
	height: 100%;
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 999;
	transition: 0.5s ease-out;
}

.error-modal-wrap {
	display: none;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 30%;
	height: 30%;
	background: #252836;
	border: solid 2px #e27b66;
	border-radius: 2rem;
	z-index: 1000;
}

.error-popup {
	display: flex;
	align-items: center;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 11fr;
	transition: 0.5s ease-out;
	color: white;
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
	height: 30%;
	width: 80%;
	margin: 10%;
}

.error-btn-wrapper {
	display: flex;
	justify-content: space-around;
}

.error-popup-header-title {
	color: white;
	font-size: 1.3rem;
	padding: 0 5%;
	padding-top: 1%;
}

.el-button {
	background-color: #e27b66 !important;
	color: black !important;
}

.el-button:hover {
	opacity: 0.75;
	cursor: pointer;
}
</style>