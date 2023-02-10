<template>
  <header>
    <UserHeader />
  </header>
  <div class="container">
    <div class="wrapper">
      <div class="signup-title">회원 정보 수정</div>
      <div class="signup-form">
        <el-form :model="form" label-width="0px">

          <div>아이디</div>
          <el-form-item>
            <el-input v-model="data.id" disabled> </el-input>
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
                <el-button @click="isCorrect">비번확인</el-button>
              </template>
            </el-input>
          </el-form-item>

          <div v-if="data.nicknameNull" style="color: red;">닉네임을 입력해주세요.</div>
          <div v-else-if="data.nicknameErr" style="color: red;">닉네임이 중복됩니다.</div>
          <div v-else-if="data.nicknameUnchecked" style="color: red;">닉네임 중복확인을 해주세요.</div>
          <div v-else-if="data.nicknameChecked">닉네임 사용 가능합니다.</div>
          <div v-else>닉네임</div>
          <el-form-item>
            <el-input v-model="data.nickname" placeholder="닉네임" clearable>
              <template #append>
                <el-button @click="nicknamecheck">중복확인</el-button>
              </template>
            </el-input>
          </el-form-item>

          <!-- <el-form-item label="이름">
          <el-input v-model="data.name" disabled />
        </el-form-item>
        <el-form-item label="성별">
          <el-input v-model="form.gender" placeholder="성별선택으로 바꿀예정" clearable />
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
          <el-input v-model="form.birth" placeholder="생년월일을 입력해주세요 ex)1996-05-18" clearable />
          <el-date-picker
            v-model="data.birth"
            type="date"
            style="width: 100%"
            disabled
          />
        </el-form-item> -->

          <div v-if="data.emailNull" style="color: red;">이메일을 입력해주세요.</div>
          <div v-else-if="data.emailErr" style="color: red;">이메일이 중복됩니다.</div>
          <div v-else-if="data.emailUnchecked" style="color: red;">이메일 인증을 해주세요.</div>
          <div v-else>이메일</div> <el-form-item>
            <el-input v-model="data.email" placeholder="이메일" clearable>
              <template #append>
                <el-button @click="emailsend">메일인증</el-button>
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
            <el-button type="info" size="large" @click="onSubmit" style="width: 192px;">
              수정 완료
            </el-button>
            <el-button type="info" size="large" @click="onDelete"
              style="width: 96px; color: #1F1D2B !important; background-color: red !important;">
              회원 탈퇴
            </el-button>
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
import { ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import UserHeader from "../menu/UserHeader.vue";
import FooterItem from "../menu/FooterItem.vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();

const data = ref({
  id: store.state.userModule.user.userId,
  passwordNow: store.state.userModule.user.password,
  password: store.state.userModule.user.password,
  password2: "",
  nicknameNow: store.state.userModule.user.nickname,
  nickname: store.state.userModule.user.nickname,
  emailNow: store.state.userModule.user.email,
  email: store.state.userModule.user.email,
  emailCode: "",
  alcohol: store.state.userModule.user.alcoholPoint,

  // password 관련 data
  passwordNull: false,
  password2Null: false,
  passwordErr: false,
  passwordChecked: false,
  passwordUnchecked: false,
  passwordSame: true,
  password2Same: false,

  // nickname 관련 data
  nicknameNull: false,
  nicknameErr: false,
  nicknameChecked: false,
  nicknameUnchecked: false,
  nicknameSame: true,

  // email 관련 data
  emailNull: false,
  emailErr: false,
  emailChecked: false,
  emailUnchecked: false,
  emailSame: true,
});

watch(() => data.value.password, (newValue, oldValue) => {
  console.log('password changed')
  data.value.passwordNull = false
  data.value.passwordErr = false
  data.value.passwordChecked = false
  if (newValue == data.value.passwordNow) {
    data.value.passwordSame = true
  } else {
    data.value.passwordSame = false
  }
});

watch(() => data.value.password2, (newValue, oldValue) => {
  console.log('password2 changed')
  data.value.password2Null = false
  data.value.passwordErr = false
  data.value.passwordChecked = false
  if (newValue == data.value.passwordNow) {
    data.value.password2Same = true
  } else {
    data.value.password2Same = false
  }
});

watch(() => data.value.nickname, (newValue, oldValue) => {
  console.log('nickname changed')
  data.value.nicknameNull = false
  data.value.nicknameErr = false
  data.value.nicknameChecked = false
  if (newValue == data.value.nicknameNow) {
    data.value.nicknameSame = true
  } else {
    data.value.nicknameSame = false
  }
});

watch(() => data.value.email, (newValue, oldValue) => {
  console.log('email changed')
  data.value.emailNull = false
  data.value.emailErr = false
  data.value.emailChecked = false
  if (newValue == data.value.emailNow) {
    data.value.emailSame = true
  } else {
    data.value.emailSame = false
  }
});

// 막힌 form 정보 빈값으로 들어옴 (update 가능 값, 유지될 값) getUserInfo로 한번 뿌려줘야 됨
const onSubmit = async () => {
  if (data.value.passwordChecked == false && !(data.value.passwordSame && data.value.password2Same)) {
    data.value.passwordUnchecked = true
  }
  if (data.value.nickname == "") {
    data.value.nickname = data.value.nicknameNow
    data.value.nicknameSame = true
  }
  if (data.value.nicknameChecked == false && !data.value.nicknameSame) {
    data.value.nicknameUnchecked = true
  }
  if (data.value.email == "") {
    data.value.email = data.value.emailNow
    data.value.emailSame = true
  }
  if (data.value.emailChecked == false && !data.value.emailSame) {
    data.value.emailUnchecked = true
  }
  if ((data.value.passwordChecked || (data.value.passwordSame && data.value.password2Same))
    && (data.value.nicknameChecked || data.value.nicknameSame)
    && (data.value.emailChecked || data.value.emailSame)) {
    await store.dispatch("userModule/updateUser", {
      userId: data.value.id,
      password: data.value.password,
      nickname: data.value.nickname,
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
    }
  }
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
  }
  console.log("nickname check!");
};

const emailsend = async () => {
  data.value.emailUnchecked = false
  if (data.value.email == "") {
    data.value.emailNull = true
  } else {
    data.value.emailNull = false
    const form = new FormData();
    form.append("email", data.value.email);
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
};

const onDelete = async () => {
  ElMessageBox.prompt("비밀번호를 입력해주세요.", "비밀번호확인", {
    confirmButtonText: "OK",
    cancelButtonText: "Cancel",
  })
    .then(({ value }) => {
      if (value == data.value.passwordNow) {
        console.log("password confirm!");
        ElMessageBox.confirm("정말로 탈퇴하시겠습니까?", "탈퇴확인", {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
        })
          .then(async () => {
            ElMessage({
              type: "success",
              message: `탈퇴 완료되었습니다..`,
            });
            // store.dispatch("userModule/userLogout", data.value.id);
            await store.dispatch("userModule/removeUser", data.value.id)
            router.push({ name: "home" });
          })
          .catch(() => {
            ElMessage({
              type: "info",
              message: "탈퇴 취소되었습니다.",
            });
          });

      } else {
        ElMessage({
          type: "error",
          message: "비밀번호가 틀렸습니다.",
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
</script>

<style scoped>
@font-face {
  font-family: "dodum";
  src: url("@/assets/fonts/GowunDodum-Regular.ttf");
}

/* .container {
  display: flex;
  height: 90vh;
  align-items: center;
  flex-direction: column;
} */
.container {
  display: flex;
  justify-content: center;
  /* align-items: center; */
  height: 92vh;
  /* background-image: url("@/assets/images/main.png");
  background-size: cover; */
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
  font-family: "dodum";
  font-style: normal;
  font-weight: 800;
  font-size: 8vh;
  color: #E27B66;
  margin-top: 5vh;
}

.signup-form {
  width: 40vw;
  margin-top: 5vh;
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
