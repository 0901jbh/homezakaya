<template>
  <!-- <header>
    <HeaderItem :isEnter="true"/>
  </header> -->
  <div class="container">
    <div class="wrapper">
      <div class="signup-title">회원가입</div>
      <div class="signup-form">
        <el-form :model="form" label-width="0px">
          <el-form-item label="">
            <el-input v-model="form.id" placeholder="아이디를 입력해주세요" clearable>
              <template #append>
                <el-button @click="idcheck" style="color:white;">중복확인</el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item _label="비밀번호">
            <el-input v-model="form.password" placeholder="비밀번호를 입력해주세요" show-password />
          </el-form-item>
          <el-form-item _label="재입력">
            <el-input v-model="form.password2" placeholder="비밀번호를 재입력해주세요" show-password>
              <!-- <template #append>
                <el-button @click="isCorrect" style="color:white;">비밀번호확인</el-button>
              </template> -->
            </el-input>
          </el-form-item>
          <el-form-item _label="닉네임">
            <el-input v-model="form.nickname" placeholder="닉네임을 입력해주세요" clearable>
              <template #append>
                <el-button @click="nicknamecheck" style="color:white;">중복확인</el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item _label="이름">
            <el-input v-model="form.name" placeholder="이름을 입력해주세요" clearable />
          </el-form-item>
          <el-form-item _label="성별">
            <!-- <el-input v-model="form.gender" placeholder="성별선택으로 바꿀예정" clearable /> -->
            <el-radio-group v-model="form.gender" size="large">
              <el-radio-button label="남" />
              <el-radio-button label="녀" />
            </el-radio-group>
          </el-form-item>
          <el-form-item _label="생년월일">
            <!-- <el-input v-model="form.birth" placeholder="생년월일을 입력해주세요 ex)1996-05-18" clearable /> -->
            <el-date-picker v-model="form.birth" type="date" placeholder="생년월일을 입력해주세요" style="width: 100%" />
          </el-form-item>
          <el-form-item _label="이메일">
            <el-input v-model="form.email" placeholder="이메일을 입력해주세요" clearable>
              <template #append>
                <el-button @click="emailsend" style="color:white;">인증</el-button>
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
          <el-form-item _label="주량">
            <!-- <el-input v-model="form.alcohol" placeholder="주량을 입력해주세요 (소주 기준)" clearable /> -->
            <el-input-number v-model="form.alcohol" size="large" :precision="1" :step="0.5" :min="0" />
            <span>&nbsp;잔 (주량)</span>
          </el-form-item>
          <el-form-item>
            <div class="btn" style="margin-inline: auto;">
              <RouterLink to="/rooms" style="text-decoration: none"><el-button type="info" size="large"
                  @click="onSubmit">가입 완료</el-button></RouterLink>
            </div>
            <div class="btn" style="margin-inline: auto;">
              <RouterLink to="/" style="text-decoration: none"><el-button type="info" size="large">돌아가기</el-button>
              </RouterLink>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
  <footer>
    <FooterItem />
  </footer>
</template>

<script setup>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import HeaderItem from "../menu/HeaderItem.vue";
import FooterItem from "../menu/FooterItem.vue";

const form = reactive({
  id: "",
  password: "",
  password2: "",
  nickname: "",
  name: "",
  gender: "",
  birth: "",
  email: "",
  emailcheck: "",
  alcohol: 0,
});

let flag = ref(false);

const onSubmit = () => {
  console.log("submit!");
};

const idcheck = () => {
  console.log("id check!");
};

const nicknamecheck = () => {
  console.log("nickname check!");
};

const emailsend = () => {
  console.log("email send!");
  ElMessageBox.prompt("인증번호를 입력해주세요.", "메일인증", {
    confirmButtonText: "OK",
    cancelButtonText: "Cancel",
  })
    .then(({ }) => {
      console.log("email confirm!");
      ElMessage({
        type: "success",
        message: `인증되었습니다.`,
      });
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "입력이 취소되었습니다.",
      });
    });
};

// const emailconfirm = () => {
//   console.log('email confirm!')
// }

function isCorrect() {
  if (form.password == form.password2) {
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
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('@/assets/images/main.png');
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
  color: white;
}

.signup-title {
  font-family: 'eastsea';
  font-style: normal;
  font-weight: 800;
  font-size: 10vh;
  color: #E27B66;
}

.signup-form {
  width: 40vw;
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

.el-form-item__label {
  justify-content: right !important;
  font-size: 3vh;
}

.el-form {
  width: 60%;
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
