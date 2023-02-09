<template>
  <div class="head">
    <RouterLink to="/rooms" style="text-decoration: none">
      <div class="title">Homezakaya</div>
    </RouterLink>
    <el-popover
      :width="300"
      popper-style="background: #252836; border: solid 2px #e27b66; padding: 20px;"
      trigger="click"
    >
      <template #reference>
        <!-- <img src="@/assets/images/profile.png" alt="profile img"
          style="width:60px; height:60px; padding-right: 1vw; cursor:pointer;"> -->
        <div style="color: white; padding-right: 2vw; font-size: 30px; font-family: hansans; cursor: pointer;">
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
          <RouterLink
            class="MyInfo_edit content"
            to="/edit"
            style="text-decoration: none"
          >
            정보수정
          </RouterLink>
          <RouterLink
            class="MyInfo_logout content"
            to="/"
            style="text-decoration: none"
          >
            <el-button @click="logout">로그아웃</el-button>
          </RouterLink>
        </div>
      </template>
    </el-popover>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useStore } from "vuex";
const store = useStore();
const data = ref({
  nickname: store.state.userModule.user.nickname,
  mannerPoint: store.state.userModule.user.mannerPoint,
  alcoholPoint: store.state.userModule.user.alcoholPoint,
});
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
  background-color: #252836;
  height: 8vh;
  /* border-bottom: solid 2vh #6E0000; */
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-family: "dokdo";
  color: white;
  font-size: 2.5rem;
  font-weight: 600;
  padding-left: 2vw;
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
}
</style>