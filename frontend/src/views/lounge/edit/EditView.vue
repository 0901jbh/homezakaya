<template>
  <header>
    <UserHeader />
  </header>
  <div class="container">
    <div class="signup-title">회원 정보 수정</div>
    <div class="signup-form">
      <el-form :model="form" label-width="150px">
        <el-form-item label="아이디">
          <el-input v-model="data.id" disabled> </el-input>
        </el-form-item>
        <el-form-item label="비밀번호">
          <el-input
            v-model="data.password"
            placeholder="비밀번호를 입력해주세요"
            show-password
          />
        </el-form-item>
        <el-form-item label="재입력">
          <el-input
            v-model="data.password2"
            placeholder="비밀번호를 재입력해주세요"
            show-password
          >
            <template #append>
              <el-button @click="isCorrect">비밀번호확인</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="닉네임">
          <el-input
            v-model="data.nickname"
            placeholder="닉네임을 입력해주세요"
            clearable
          >
            <template #append>
              <el-button @click="nicknamecheck">중복확인</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="이름">
          <el-input v-model="data.name" disabled />
        </el-form-item>
        <el-form-item label="성별">
          <!-- <el-input v-model="form.gender" placeholder="성별선택으로 바꿀예정" clearable /> -->
          <el-radio-group
            v-model="data.gender"
            fill="black"
            size="large"
            disabled
          >
            <el-radio-button label="남" />
            <el-radio-button label="녀" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="생년월일">
          <!-- <el-input v-model="form.birth" placeholder="생년월일을 입력해주세요 ex)1996-05-18" clearable /> -->
          <el-date-picker
            v-model="data.birth"
            type="date"
            style="width: 100%"
            disabled
          />
        </el-form-item>
        <el-form-item label="이메일">
          <el-input
            v-model="data.email"
            placeholder="이메일을 입력해주세요"
            clearable
          >
            <template #append>
              <el-button @click="emailsend">인증</el-button>
            </template>
          </el-input>
        </el-form-item>
        <!-- <el-form-item label="인증번호">
          <el-input v-model="form.emailcheck" placeholder="인증번호을 입력해주세요" clearable>
            <template #append>
              <el-button @click="emailconfirm">인증확인</el-button>
            </template>
          </el-input>
        </el-form-item> -->
        <el-form-item label="주량">
          <!-- <el-input v-model="form.alcohol" placeholder="주량을 입력해주세요 (소주 기준)" clearable /> -->
          <el-input-number
            v-model="data.alcohol"
            size="large"
            :precision="1"
            :step="0.5"
            :min="0"
          />
          <span class="el-form-item__label">&nbsp;잔 (소주)</span>
        </el-form-item>
        <el-form-item>
          <div class="btn">
            <RouterLink to="/rooms" style="text-decoration: none"
              ><el-button type="info" size="large" @click="onSubmit"
                >수정 완료</el-button
              ></RouterLink
            >
          </div>
          <div class="btn">
            <RouterLink to="/rooms" style="text-decoration: none"
              ><el-button type="info" size="large">돌아가기</el-button>
            </RouterLink>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
  <footer>
    <FooterItem />
  </footer>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import UserHeader from "../menu/UserHeader.vue";
import FooterItem from "../menu/FooterItem.vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();

const data = ref({
  id: store.state.userModule.user.userId,
  password: store.state.userModule.user.password,
  password2: "",
  nickname: store.state.userModule.user.nickname,
  name: store.state.userModule.user.username,
  gender: store.state.userModule.user.gender,
  birth: store.state.userModule.user.birthDate,
  email: store.state.userModule.user.email,
  emailCode: "",
  alcohol: store.state.userModule.user.alcoholPoint,
});

let flag = ref(false);

// 막힌 form 정보 빈값으로 들어옴 (update 가능 값, 유지될 값) getUserInfo로 한번 뿌려줘야 됨
const onSubmit = async () => {
  await store.dispatch("userModule/updateUser", {
    userId: data.value.id,
    password: data.value.password,
    nickname: data.value.nickname,
    username: data.value.name,
    gender: data.value.gender,
    birthDate: data.value.birth,
    email: data.value.email,
    alcoholPoint: data.value.alcohol,
  });

  console.log("submit!");
  router.push({ name: "rooms" });
};

const idcheck = async () => {
  await store.dispatch("userModule/idcheck", data.value.id);
  console.log("id check!");
};

const nicknamecheck = async () => {
  await store.dispatch("userModule/nicknameCheck", data.value.nickname);
  console.log("nickname check!");
};

const emailsend = async () => {
  const form = new FormData();
  form.append("email", data.value.email);
  await store.dispatch("userModule/sendEmail", form);
  console.log("email send!");
  ElMessageBox.prompt("인증번호를 입력해주세요.", "메일인증", {
    confirmButtonText: "OK",
    cancelButtonText: "Cancel",
  })
    .then(({ value }) => {
      if (value == store.state.userModule.user.emailCode) {
        console.log("email confirm!");
        ElMessage({
          type: "success",
          message: `인증되었습니다.`,
        });
      } else {
        ElMessage({
          type: "error",
          message: "인증이 실패했습니다.",
        });
      }
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "입력이 취소되었습니다.",
      });
    });
};

function isCorrect() {
  if (data.value.password == data.value.password2) {
    flag = true;
    console.log("right password!");
  } else {
    flag = false;
    console.log("wrong password!");
  }
}
</script>

<style scoped>
.container {
  display: flex;
  height: 90vh;
  align-items: center;
  flex-direction: column;
}

.signup-title {
  font-family: "Inter";
  font-style: normal;
  font-weight: 800;
  font-size: 8vh;
  color: #9d0101;
  margin-top: 5vh;
}

.signup-form {
  width: 40vw;
  margin-top: 5vh;
}

.el-button {
  background-color: black !important;
  color: white !important;
}

.el-button:hover {
  background-color: rgb(118, 118, 118) !important;
  transition: 0.2s;
}

.el-radio-button__inner:hover {
  color: black;
}

.el-form-item__label {
  justify-content: right !important;
  font-size: 3vh;
}

/* @media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
} */
</style>
