<template>
  <!-- <div class="intro" v-if="!data.isEnter">
    <HeaderItem :isEnter="data.isEnter" @clickTitle="clickTitle" />
  </div>
  <div class="login" v-if="data.isEnter">
  <div class="login">
    <header>
      <HeaderItem :isEnter="data.isEnter" />
    </header> -->
  <div class="container">
    <div class="wrapper">
      <div class="box">
        <div class="title">Homezakaya</div>
        <div class="comment">좋은 사람들과 새로운 술자리</div>
      </div>
      <div>
        <div class="box">
          <el-form margin-top="10vh">
            <el-form-item size="large">
              <el-input v-model="data.id" placeholder="아이디" clearable />
            </el-form-item>
            <el-form-item size="large">
              <el-input v-model="data.password" placeholder="비밀번호" show-password />
            </el-form-item>
            <el-form-item>
              <div class="btn">
                <el-button type="info" size="large" @click="onSubmit">로그인</el-button>
              </div>
            </el-form-item>
            <div v-if="data.loginFail" style="margin-bottom: 18px; color: red;">
              아이디와 비밀번호를 확인하세요.
            </div>
            <div>
              계정이 없으신가요?
              <RouterLink to="/signup" style="text-decoration: none; color: #e27b66">회원가입</RouterLink>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
  <footer>
    <FooterItem />
  </footer>
  <!-- </div> -->
</template>

<script setup>
import HeaderItem from "../menu/HeaderItem.vue";
import FooterItem from "../menu/FooterItem.vue";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();

const data = ref({
  id: "",
  password: "",
  isEnter: false,
  loginFail: false,
});

const onSubmit = async () => {
  console.log("Log-In!");
  await store.dispatch("userModule/userLogin", {
    userId: data.value.id,
    password: data.value.password,
  });
  await store.dispatch(
    "userModule/getUserInfo",
    sessionStorage.getItem("access-token")
  );
  if (store.getters['userModule/checkToken']) {
    router.push({ name: "rooms" });
  } else {
    data.value.loginFail = true
  }
};

const clickTitle = () => {
  data.value.isEnter = !data.value.isEnter;
};
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

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  /* grid-template-columns: 2fr 3fr; */
  height: 100vh;
  background-image: url("@/assets/images/main.png");
  background-size: cover;
}

.wrapper {
  display: flex;
  flex-direction: column;
  /* justify-content: center; */
  align-items: center;
  background: hsl(229.41, 18.68%, 17.84%, 80%);
  height: 80%;
  width: 1000px;
  color: #cbcbcb;
}

.title {
  font-family: "dokdo";
  color: #e27b66;
  font-style: normal;
  font-weight: 700;
  font-size: 12vh;
  text-align: center;
  /* padding-top: 5vh; */
}

.comment {
  /* align-self: flex-end; */
  font-family: "dokdo";
  font-weight: 600;
  font-size: 4vh;
  /* padding-top: 3vh; */
  text-align: center;
}

.box {
  margin-top: 10%;
  /* padding-left: 5vw; */
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  flex-direction: column;
}

.el-button {
  background-color: #e27b66;
  width: 300px;
  border: none;
}

.el-button:hover {
  opacity: 0.75;
  cursor: pointer;
}

.el-form {
  display: flex;
  align-items: center;
  flex-direction: column;
  padding-top: 50px;
}

.el-input {
  background-color: #252836;
  width: 300px;
}
</style>
