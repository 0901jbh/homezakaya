<template>
  <div class="head">
    <RouterLink to="/rooms" style="text-decoration: none">
      <div class="title">Homezakaya</div>
    </RouterLink>
    <el-popover :width="300" popper-style="background: #252836; border: solid 2px #e27b66; padding: 20px;"
      trigger="click">
      <template #reference>
        <!-- <img src="@/assets/images/profile.png" alt="profile img"
          style="width:60px; height:60px; padding-right: 1vw; cursor:pointer;"> -->
        <div style="color: white; padding-right: 8vw; font-size: 30px; font-family: hansans; cursor: pointer;" @click="refreshData">
          오늘도 즐거운 술자리, {{ data.nickname }}님!
        </div>
      </template>
      <template #default>
        <div class="MyInfo" style="display: flex; gap: 32px; flex-direction: column">
          <!-- <p class="MyInfo_nickname" style="margin: 0; font-size: 30px; color: white; align-self:center;">
            {{ data.nickname }}
          </p> -->
          <div class="MyInfo_manner"
            style="margin: 0; display: flex; gap: 16px; flex-direction: row; justify-content: center;">
            <img src="@/assets/images/manner_w.png" alt="manner_w img" style="width:40px; height:40px;">
            <p style="margin: 0; font-size: 20px; color: white; align-self:center;">매너 도수는 {{ data.mannerPoint }}점</p>
          </div>
          <div class="MyInfo_alcohol"
            style="margin: 0; display: flex; gap: 16px; flex-direction: row; justify-content: center;">
            <img src="@/assets/images/alcohol_w.png" alt="alcohol_w img" style="width:40px; height:40px;">
            <p style="margin: 0; font-size: 20px; color: white; align-self:center;">알콜 도수는 {{ data.alcoholPoint }}잔</p>
          </div>
          <div class="MyInfo_edit content" @click="userPrivatePopOpen">
            정보수정
          </div>
          <div class="MyInfo_logout content" @click="logout">
            로그아웃
          </div>
        </div>
      </template>
    </el-popover>
  </div>
  <!-- 회원정보수정 비밀번호 입력창 -->
	<div class="user-private-modal-bg" @click="userPrivatePopClose"></div>
	<div class="user-private-modal-wrap">
		<div class="user-private-popup">
			<div class="user-private-popup-content">
				<div style="margin-bottom:0;">[ 비밀번호를 입력해주세요. ]</div>
				<div>
					<el-form-item>
						<el-input v-model="data.userInput" placeholder="비밀번호를 입력해주세요" show-password />
					</el-form-item>
				</div>
				<div>
					<el-button type="info" size="large" @click="edit">Enter</el-button>
					<el-button type="info" size="large" @click="userPrivatePopClose">Cancel</el-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

const store = useStore();
const router = useRouter();

const data = ref({
  userId: store.state.userModule.user.userId,
  nickname: store.state.userModule.user.nickname,
  alcoholPoint: store.state.userModule.user.alcoholPoint,
  mannerPoint : parseFloat(store.state.userModule.user.mannerPoint.toFixed(2)),
  userInput: '',
});

const edit = async () => {
  if (data.value.userInput == store.state.userModule.user.password) {
    router.push({ name: "edit" });
  } else {
    userPrivatePopClose()
    store.commit('errorModule/SET_STATUS', 401);
  }
};

const logout = async () => {
  await store.dispatch(
    "userModule/userLogout",
    store.state.userModule.user.userId
  );
  console.log(store.state.userModule.isLogin)
  router.push({ name: "home" });
};
const refreshData = () =>{
  console.log("갱신 시작");
  store.dispatch("userModule/getUserPoint",data.value.userId).then((response) =>{
				data.value.mannerPoint = parseFloat(response.mannerPoint.toFixed(2));
				data.value.alcoholPoint = response.alcoholPoint;
			});
};

const userPrivatePopOpen = () => {
	document.getElementsByClassName("user-private-modal-wrap")[0].style.display = 'block';
	document.getElementsByClassName("user-private-modal-bg")[0].style.display = 'block';
}

const userPrivatePopClose = () => {
	document.getElementsByClassName("user-private-modal-wrap")[0].style.display = 'none';
	document.getElementsByClassName("user-private-modal-bg")[0].style.display = 'none';
	data.value.userInput = '';
}	
</script>

<style scoped>
@font-face {
  font-family: "dokdo";
  src: url("@/assets/fonts/Dokdo-Regular.ttf");
}

@font-face {
  font-family: "eastsea";
  src: url("@/assets/fonts/EastSeaDokdo-Regular.ttf");
}

@font-face {
  font-family: 'hansans';
  src: url('@/assets/fonts/BlackHanSans-Regular.ttf')
}

.head {
  padding-top: 1vw;
  background-color: #252836;
  height: 8vh;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-family: "dokdo";
  color: white;
  font-size: 2.5rem;
  font-weight: 600;
  padding-left: 11vw;
}

.content {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  align-self: center;
  padding: 15px 35px;
  gap: 10px;

  width: 100px;
  height: 10px;
  left: 52px;
  top: 105px;

  color: black;
  font-size: 1rem;
  font-weight: 70;
  background: #e27b66;
  box-shadow: 0px -0x 15px #e27b66;
  border-radius: 53px;

  cursor: pointer;
}

/* 회원정보수정 접근 비밀번호 입력창 */
.user-private-modal-bg {
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

.user-private-modal-wrap {
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

.user-private-popup {
	display: grid;
	height: 100%;
	width: 100%;
	transition: 0.5s ease-out;
	color: white;
}

.user-private-popup-content {
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: center;
	height: 80%;
	width: 80%;
	margin: 10%;
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
</style>