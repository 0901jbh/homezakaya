<template>
  <!-- <header>
    <HeaderItem :isEnter="true"/>
  </header> -->
  <div class="container">
    <div class="wrapper">
      <div class="id-title">비밀번호 찾기</div>
      <div class="id-form">
        <el-form label-width="0px">

          <div v-if="data.idNull" style="color: red;">아이디를 입력해주세요.</div>
          <div v-else>아이디</div>
          <el-form-item>
            <el-input v-model="data.id" placeholder="아이디" clearable>
            </el-input>
          </el-form-item>

          <div v-if="data.emailNull" style="color: red;">이메일을 입력해주세요.</div>
          <div v-else>이메일</div>
          <el-form-item>
            <el-input v-model="data.email" placeholder="이메일" clearable>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="info" size="large" @click="emailsend" style="width: 300px;">
              비밀번호 찾기
            </el-button>
          </el-form-item>

        </el-form>
      </div>
      <div v-if="data.emailErr" class="id-result" style="color: red;">
        아이디와 이메일 정보가 일치하지 않습니다.
      </div>
      <div v-if="data.newPass" class="id-result">
        새로운 비밀번호가 당신의 메일로 전송되었습니다.
      </div>
      <el-form-item>
        <el-button type="info" size="large" style="width: 300px;">
          <RouterLink to="/homeview" style="color:black">뒤로 가기</RouterLink>
        </el-button>
      </el-form-item>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import HeaderItem from "../menu/HeaderItem.vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();

const data = ref({

  // id 관련 data
  idNull: false,

  // email 관련 data
  emailNull: false,
  emailErr: false,
  emailChecked: false,

  newPass: false,

  id: "",
  email: "",
  emailCode: "",
});

watch(() => data.value.id, (newValue, oldValue) => {
  console.log('id changed')
  data.value.idNull = false
  data.value.idErr = false
  data.value.idChecked = false
});

watch(() => data.value.email, (newValue, oldValue) => {
  console.log('email changed')
  data.value.emailNull = false
  data.value.emailChecked = false
});

const emailsend = async () => {
  data.value.emailUnchecked = false
  if (data.value.email == "") {
    data.value.emailNull = true
  }
  if (data.value.id == "") {
    data.value.idNull = true
  }
  const form = new FormData();
  form.append("email", data.value.email);
  form.append("userId", data.value.id);
  await store.dispatch("userModule/findPassword", form);
  data.value.emailErr = store.state.userModule.emailErr
  data.value.newPass = store.state.userModule.newPass
  console.log("email send!");
};

</script>

<style scoped>
@font-face {
  font-family: "dodum";
  src: url("@/assets/fonts/GowunDodum-Regular.ttf");
}

a {
  text-decoration: none;
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url("@/assets/images/main.png");
  background-size: cover;
}

.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: hsl(229.41, 18.68%, 17.84%, 80%);
  height: 80%;
  width: 1000px;
  color: #cbcbcb;
}

.id-title {
  font-family: 'dodum';
  font-style: normal;
  font-weight: 800;
  font-size: 8vh;
  color: #E27B66;
  margin-bottom: 2vh;
}

.id-form {
  width: 40vw;
  /* padding-top: 50px; */
}

.el-button {
  background-color: #E27B66 !important;
  border: none;
  color: white;
}

.el-button:hover {
  opacity: 0.75;
  cursor: pointer;
}

.el-radio-button__inner:hover {
  color: black;
}

.el-form {
  width: 300px;
  margin: 0 auto;
}

/* @media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
} */
</style>
