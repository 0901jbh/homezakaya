<template>
	<div class="rooms-container">
		<div class="create-room">
			<div class="create-room-wrapper btn-open" @click="popOpen">
				<img src="@/assets/images/plus.png" alt="plus img" style="width: 50px;">
			</div>
		</div>
		<div class="room" v-for="(room, idx) in form.rooms" :key="idx">
			<RoomItem :room="room" :idx="idx" />
		</div>
	</div>
	<!-- 방 생성 팝업창 -->
	<div class="modal-bg" @click="popClose"></div>
	<div class="modal-wrap">
		<div class="create-room-popup">
			<div class="popup-header">
				<div class="popup-header-title">방 만들기</div>
			</div>
			<div class="popup-content">
				<el-form class="create-room-form" :model="form" label-width="20%" label-position="left">
					<el-form-item label="방 제목">
						<el-input v-model="form.title" placeholder="방 제목을 입력해주세요" />
					</el-form-item>
					<div>
						<el-form-item label="주종">
							<el-select v-model="form.category" placeholder="주종을 선택해주세요">
								<el-option label="자유" value="자유" />
								<el-option label="소주" value="소주" />
								<el-option label="맥주" value="맥주" />
								<el-option label="양주" value="양주" />
								<el-option label="사케" value="사케" />
							</el-select>
						</el-form-item>
						<el-form-item label="인원">
							<el-select v-model="form.personLimit" placeholder="인원 수를 선택해주세요">
								<el-option label="2" value=2 />
								<el-option label="3" value=3 />
								<el-option label="4" value=4 />
								<el-option label="5" value=5 />
								<el-option label="6" value=6 />
								<el-option label="7" value=7 />
								<el-option label="8" value=8 />
							</el-select>
						</el-form-item>
					</div>
					<el-form-item label="공개여부">
						<el-radio-group v-model="form.private" fill="#e27b66">
							<el-radio-button label="공개" @click="passwordClose" />
							<el-radio-button label="비공개" @click="passwordOpen" />
						</el-radio-group>
					</el-form-item>
					<el-form-item class="passwordInput" label="비밀번호">
						<el-input v-model="form.password" placeholder="비밀번호를 입력해주세요" show-password />
					</el-form-item>
					<el-form-item style="width:100%;">
						<el-button type="info" size="large" @click="createRoom">Create</el-button>
						<el-button type="info" size="large" @click="popClose">
							<RouterLink to="/rooms" style="text-decoration:none; color: black;">
								Cancel
							</RouterLink>
						</el-button>
					</el-form-item>
				</el-form>
			</div>
		</div>
	</div>
	<div></div>
	<!-- / 방 생성 팝업창  -->
</template>

<script setup>
import RoomItem from './RoomItem.vue'
import { computed, onBeforeMount, onMounted, onUpdated, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

const store = useStore()
const router = useRouter()

const form = ref({
	title: '',
	category: '자유',
	personLimit: 2,
	password: '',
	private: "공개",
	hostId: store.state.userModule.user.userId,
	rooms: [],
	roomId: -1,
})

const createRoom = () => {
	let isPrivate = true;
	if (form.value.private == "공개") {
		isPrivate = false;
		form.value.password = '';
	}
	store.dispatch("roomModule/createRoom", {
		title: form.value.title,
		password: form.value.password,
		category: form.value.category,
		hostId: form.value.hostId,
		personLimit: form.value.personLimit,
		private: isPrivate,
	}).then((result) => {
		form.value.roomId = result.roomId
		store.dispatch('roomModule/doEnterRoom', {
			userId: store.state.userModule.user.userId,
			roomId: result.roomId,
		}).then((status) => {
			if (status == 200) {
				router.push({ name: 'room', params: { roomId: form.value.roomId }, query: { video: false, audio: false } })
			}
			else {
				popClose();
				store.commit("errorModule/SET_STATUS", status);
			}
		})
	});
}

form.value.rooms = computed(() => store.state.roomModule.rooms)

onBeforeMount(() => {
	store.dispatch("roomModule/getRooms")
})

const popOpen = () => {
	document.getElementsByClassName("modal-wrap")[0].style.display = 'block';
	document.getElementsByClassName("modal-bg")[0].style.display = 'block';
}

const popClose = () => {
	document.getElementsByClassName("modal-wrap")[0].style.display = 'none';
	document.getElementsByClassName("modal-bg")[0].style.display = 'none';
}

const passwordOpen = () => {
	document.getElementsByClassName("passwordInput")[0].style.display = 'flex';
	form.value.password = '';
}

const passwordClose = () => {
	document.getElementsByClassName("passwordInput")[0].style.display = 'none';
}

</script>

<style scoped>
@font-face {
	font-family: 'hansans';
	src: url('@/assets/fonts/BlackHanSans-Regular.ttf')
}

@font-face {
	font-family: "dodum";
	src: url("@/assets/fonts/GowunDodum-Regular.ttf");
}

.rooms-container {
	display: flex;
	width: 100%;
	height: 100%;
	flex-direction: row;
	flex-wrap: wrap;
	overflow-y: scroll;
	scroll-snap-type: y mandatory;
}

.rooms-container::-webkit-scrollbar {
	display: none;
}

.create-room {
	width: 30%;
	height: 35%;
	padding: 5% 1.5%;
	scroll-snap-align: start;
}

.create-room-wrapper {
	display: flex;
	width: 100%;
	height: 100%;
	font-size: 3rem;
	justify-content: center;
	align-items: center;
	background: linear-gradient(#252836 10%, #1f1d2b 40%);
	border-radius: 10px;
}

.room {
	width: 30%;
	height: 35%;
	padding: 5% 1.5%;
	scroll-snap-align: start;
}

.room-wrapper {
	display: flex;
	width: 100%;
	height: 100%;
	font-size: 3rem;
	justify-content: center;
	align-items: center;
}

.create-room:hover {
	transform: scale(1.1, 1.1);
	transition: 0.3s ease-out;
	cursor: pointer;
}

/* 방만들기 */
.modal-bg {
	display: none;
	width: 100%;
	height: 100%;
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 999;
	transition: 0.5s ease-out;
	backdrop-filter: blur(4px) brightness(60%);
}

.modal-wrap {
	display: none;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 35%;
	height: 55%;
	background: #252836;
	border: solid 2px #e27b66;
	border-radius: 2rem;
	z-index: 1000;
}

.create-room-popup {
	display: grid;
	height: 100%;
	width: 100%;
	grid-template-rows: 1fr 11fr;
	transition: 0.5s ease-out;
	color: white;
}

.popup-header {
	background-color: #E27B66;
	height: 100%;
	width: 100%;
	/* border-bottom: solid 0.5rem #6E0000; */
	border-radius: 1.6rem 1.6rem 0 0;
}

.popup-content {
	display: flex;
	align-items: start;
	flex-direction: column;
	width: 80%;
	margin: 10%;
	/* display: grid;
	text-align: center;
	grid-template-rows: 2fr 1fr;
	height: 30%;
	width: 80%;
	margin: 10%; */
}

.popup-header-title {
	color: black;
	font-size: 1.5rem;
	font-family: 'dodum';
	padding: 1% 5%;
}

.passwordInput {
	display: none;
}

.create-room-form {
	width: 100%;
}

.el-button {
	background-color: #e27b66 !important;
	color: black !important;
	border: none;
}

.el-button:hover {
	opacity: 0.75;
	cursor: pointer;
}

.el-radio-button__inner:hover {
	color: black;
}

.el-input {
	width: 300px;
}

.el-select {
	width: 300px;
}
</style>