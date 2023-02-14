<template>
  <!-- <header>
    <HeaderItem :isEnter="true"/>
  </header> -->
  <div class="container">
    <div class="wrapper">
      <div class="signup-title">회원가입</div>
      <div class="signup-form">
        <el-form label-width="0px">

          <div v-if="data.idNull" style="color: red;">아이디를 입력해주세요.</div>
          <div v-else-if="data.idErr" style="color: red;">아이디를 사용할 수 없습니다.</div>
          <div v-else-if="data.idUnchecked" style="color: red;">아이디 확인을 해주세요.</div>
          <div v-else-if="data.idChecked">아이디 사용 가능합니다.</div>
          <div v-else>아이디</div>
          <el-form-item>
            <el-input v-model="data.id" placeholder="아이디" clearable>
              <template #append>
                <el-button @click="idcheck" style="color:white;">중복확인</el-button>
              </template>
            </el-input>
          </el-form-item>

          <div v-if="data.passwordNull" style="color: red;">비밀번호를 입력해주세요.</div>
          <div v-else>비밀번호</div>
          <el-form-item>
            <el-input v-model="data.password" placeholder="비밀번호" show-password />
          </el-form-item>

          <div v-if="data.password2Null" style="color: red;">비밀번호 확인을 입력해주세요.</div>
          <div v-else-if="data.passwordErr" style="color: red;">비밀번호가 일치하지 않습니다.</div>
          <div v-else-if="data.passwordUnchecked" style="color: red;">비밀번호 확인을 해주세요.</div>
          <div v-else-if="data.passwordChecked">비밀번호 사용 가능합니다.</div>
          <div v-else>비밀번호 확인</div>
          <el-form-item>
            <el-input v-model="data.password2" placeholder="비밀번호 확인" show-password>
              <template #append>
                <el-button @click="isCorrect" style="color:white;">비번확인</el-button>
              </template>
            </el-input>
          </el-form-item>

          <div v-if="data.nicknameNull" style="color: red;">닉네임을 입력해주세요.</div>
          <div v-else-if="data.nicknameErr" style="color: red;">닉네임을 사용할 수 없습니다.</div>
          <div v-else-if="data.nicknameUnchecked" style="color: red;">닉네임 확인을 해주세요.</div>
          <div v-else-if="data.nicknameChecked">닉네임 사용 가능합니다.</div>
          <div v-else>닉네임</div>
          <el-form-item>
            <el-input v-model="data.nickname" placeholder="닉네임" clearable>
              <template #append>
                <el-button @click="nicknamecheck" style="color:white;">중복확인</el-button>
              </template>
            </el-input>
          </el-form-item>

          <!-- <el-form-item label="이름">
            <el-input v-model="data.name" placeholder="이름을 입력해주세요" clearable />
          </el-form-item>
          <el-form-item _label="성별">
            <el-input v-model="form.gender" placeholder="성별선택으로 바꿀예정" clearable />
            <el-radio-group v-model="data.gender" fill="black" size="large">
              <el-radio-button label="남" />
              <el-radio-button label="녀" />
            </el-radio-group>
          </el-form-item>
          <el-form-item _label="생년월일">
            <el-input v-model="form.birth" placeholder="생년월일을 입력해주세요 ex)1996-05-18" clearable />
            <el-date-picker v-model="data.birth" type="date" placeholder="생년월일을 입력해주세요" style="width: 100%" />
          </el-form-item> -->

          <div v-if="data.emailNull" style="color: red;">이메일을 입력해주세요.</div>
          <div v-else-if="data.emailErr" style="color: red;">이메일을 사용할 수 없습니다.</div>
          <div v-else-if="data.emailUnchecked" style="color: red;">이메일 인증을 해주세요.</div>
          <div v-else>이메일</div>
          <el-form-item>
            <el-input v-model="data.email" placeholder="이메일" clearable>
              <template #append>
                <el-button @click="emailsend" style="color:white;">메일인증</el-button>
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
          <div>주량 (소주 기준)</div>
          <el-form-item>
            <!-- <el-input v-model="form.alcohol" placeholder="주량을 입력해주세요 (소주 기준)" clearable /> -->
            <el-input-number v-model="data.alcohol" size="large" :precision="1" :step="0.5" :min="0" />
            <span>&nbsp;잔</span>
          </el-form-item>

          <el-form-item>
            <el-button type="info" size="large" @click="onSubmit" style="width: 300px;">
              가입 완료
            </el-button>
          </el-form-item>

          <!-- <el-form-item>
            <RouterLink to="/homeview" style="color: white; text-decoration: none;">
              <el-button type="info" size="large"
                style="width: 300px; background-color: hsl(229.41, 18.68%, 17.84%) !important; color: white !important; border: 1px solid #E27B66">
                뒤로 가기
              </el-button>
            </RouterLink>
          </el-form-item> -->

        </el-form>
      </div>
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
  idErr: false,
  idChecked: false,
  idUnchecked: false,

  // password 관련 data
  passwordNull: false,
  password2Null: false,
  passwordErr: false,
  passwordChecked: false,
  passwordUnchecked: false,

  // nickname 관련 data
  nicknameNull: false,
  nicknameErr: false,
  nicknameChecked: false,
  nicknameUnchecked: false,

  // email 관련 data
  emailNull: false,
  emailErr: false,
  emailChecked: false,
  emailUnchecked: false,

  id: "",
  password: "",
  password2: "",
  nickname: "",
  name: "",
  gender: "",
  birth: "",
  email: "",
  emailCode: "",
  alcohol: 0,
});

watch(() => data.value.id, (newValue, oldValue) => {
  console.log('id changed')
  data.value.idNull = false
  data.value.idErr = false
  data.value.idChecked = false
});

watch(() => data.value.password, (newValue, oldValue) => {
  console.log('password changed')
  data.value.passwordNull = false
  data.value.passwordErr = false
  data.value.passwordChecked = false
});

watch(() => data.value.password2, (newValue, oldValue) => {
  console.log('password2 changed')
  data.value.password2Null = false
  data.value.passwordErr = false
  data.value.passwordChecked = false
});

watch(() => data.value.nickname, (newValue, oldValue) => {
  console.log('nickname changed')
  data.value.nicknameNull = false
  data.value.nicknameErr = false
  data.value.nicknameChecked = false
});

watch(() => data.value.email, (newValue, oldValue) => {
  console.log('email changed')
  data.value.emailNull = false
  data.value.emailErr = false
  data.value.emailChecked = false
});

const onSubmit = async () => {
  if (data.value.idChecked == false) {
    data.value.idUnchecked = true
  }
  if (data.value.passwordChecked == false) {
    data.value.passwordUnchecked = true
  }
  if (data.value.nicknameChecked == false) {
    data.value.nicknameUnchecked = true
  }
  if (data.value.emailChecked == false) {
    data.value.emailUnchecked = true
  }
  if (data.value.idChecked && data.value.passwordChecked && data.value.nicknameChecked && data.value.emailChecked) {
    await store.dispatch("userModule/createUser", {
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

    await store.dispatch("userModule/userLogin", {
      userId: data.value.id,
      password: data.value.password,
    });
    await store.dispatch(
      "userModule/getUserInfo",
      sessionStorage.getItem("access-token")
    );
    if (store.state.userModule.isLogin) {
      router.push({ name: "rooms" });
    } else {
      data.value.loginFail = true
    }

    router.push({ name: "rooms" });
  }

};

const idcheck = async () => {
  data.value.idUnchecked = false
  if (data.value.id == "") {
    data.value.idNull = true
  } else {
    data.value.idNull = false
    await store.dispatch("userModule/idcheck", data.value.id);
    data.value.idErr = store.state.userModule.idErr
    if (data.value.idErr == false) {
      data.value.idChecked = true
    }
  }
  console.log("id check!");
};

const nicknamecheck = async () => {
  data.value.nicknameUnchecked = false
  if (data.value.nickname == "") {
    data.value.nicknameNull = true
  } else {
    data.value.nicknameNull = false
    await store.dispatch("userModule/nicknameCheck", data.value.nickname);
    data.value.nicknameErr = store.state.userModule.nicknameErr
    if (data.value.nicknameErr == false) {
      data.value.nicknameChecked = true
    }
    console.log("nickname check!");
  }
};

function isCorrect() {
  data.value.passwordUnchecked = false
  if (data.value.password == "") {
    data.value.passwordNull = true
  } else {
    data.value.passwordNull = false
  }
  if (data.value.password2 == "") {
    data.value.password2Null = true
  } else {
    data.value.password2Null = false
  }
  if (data.value.password != "" && data.value.password2 != "") {
    if (data.value.password == data.value.password2) {
      data.value.passwordChecked = true
      data.value.passwordErr = false
      console.log("right password!");
    } else {
      data.value.passwordChecked = false
      data.value.passwordErr = true
      console.log("wrong password!");
    }
  }
}

const emailsend = async () => {
  data.value.emailUnchecked = false
  if (data.value.email == "") {
    data.value.emailNull = true
  } else {
    data.value.emailNull = false
    const form = new FormData();
    form.append("email", data.value.email);
    console.log(form)
    await store.dispatch("userModule/sendEmail", form);
    data.value.emailErr = store.state.userModule.emailErr
    if (data.value.emailErr == false) {
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
            data.value.emailChecked = true;
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
    }
    console.log("email send!");
  }
};

</script>

<style scoped>
@font-face {
  font-family: "dodum";
  src: url("@/assets/fonts/GowunDodum-Regular.ttf");
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

.signup-title {
  font-family: 'dodum';
  font-style: normal;
  font-weight: 800;
  font-size: 8vh;
  color: #E27B66;
  margin-bottom: 2vh;
}

.signup-form {
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
