<template>
  <header>
    <HeaderItem />
  </header>
  <div class="container">
    <div class="signup-title">회원가입</div>
    <div class="signup-form">
      <el-form :model="form" label-width="150px">
        <el-form-item label="아이디">
          <el-input v-model="form.id" placeholder="아이디를 입력해주세요" clearable>
            <template #append>
              <el-button @click="idcheck" type="info">중복확인</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="비밀번호">
          <el-input v-model="form.password" placeholder="비밀번호를 입력해주세요" show-password />
        </el-form-item>
        <el-form-item label="재입력">
          <el-input v-model="form.password2" placeholder="비밀번호를 재입력해주세요" show-password>
            <template #append>
              <el-button @click="isCorrect">비밀번호확인</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="닉네임">
          <el-input v-model="form.nickname" placeholder="닉네임을 입력해주세요" clearable>
            <template #append>
              <el-button @click="nicknamecheck">중복확인</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="이름">
          <el-input v-model="form.name" placeholder="이름을 입력해주세요" clearable />
        </el-form-item>
        <el-form-item label="성별">
          <!-- <el-input v-model="form.gender" placeholder="성별선택으로 바꿀예정" clearable /> -->
          <el-radio-group v-model="form.gender" fill="black" size="large">
            <el-radio-button label="남" />
            <el-radio-button label="녀" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="생년월일">
          <!-- <el-input v-model="form.birth" placeholder="생년월일을 입력해주세요 ex)1996-05-18" clearable /> -->
          <el-date-picker v-model="form.birth" type="date" placeholder="생년월일을 입력해주세요" style="width: 100%" />
        </el-form-item>
        <el-form-item label="이메일">
          <el-input v-model="form.email" placeholder="이메일을 입력해주세요" clearable>
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
          <el-input-number v-model="form.alcohol" size=large :precision="1" :step="0.5" :min="0" />
          <span class="el-form-item__label">&nbsp;잔 (소주)</span>
        </el-form-item>
        <el-form-item>
          <div class="btn">
            <RouterLink to="/rooms" style="text-decoration:none;"><el-button type="info" size="large"
                @click="onSubmit">가입 완료</el-button></RouterLink>
          </div>
          <div class="btn">
            <RouterLink to="/" style="text-decoration:none;"><el-button type="info" size="large">돌아가기</el-button>
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
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import HeaderItem from '../menu/HeaderItem.vue'
import FooterItem from '../menu/FooterItem.vue'

const form = reactive({
  id: '',
  password: '',
  password2: '',
  nickname: '',
  name: '',
  gender: '',
  birth: '',
  email: '',
  emailcheck: '',
  alcohol: 0,
})

let flag = ref(false)

const onSubmit = () => {
  console.log('submit!')
}

const idcheck = () => {
  console.log('id check!')
}

const nicknamecheck = () => {
  console.log('nickname check!')
}

const emailsend = () => {
  console.log('email send!')
  ElMessageBox.prompt('인증번호를 입력해주세요.', '메일인증', {
    confirmButtonText: 'OK',
    cancelButtonText: 'Cancel',
  })
    .then(({ }) => {
      console.log('email confirm!')
      ElMessage({
        type: 'success',
        message: `인증되었습니다.`,
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '입력이 취소되었습니다.',
      })
    })
}

// const emailconfirm = () => {
//   console.log('email confirm!')
// }

function isCorrect() {
  if (form.password == form.password2) {
    flag = true
    console.log('right password!')
  } else {
    flag = false
    console.log('wrong password!')
  }
}

</script>

<style scope>
.container {
  display: flex;
  height: 90vh;
  align-items: center;
  flex-direction: column;
}

.signup-title {
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 8vh;
  color: #9D0101;
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
  color: black
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
