<template>
  <div class="container">
    <div class="signup-title">회원가입</div>
    <div class="signup-form">
      <el-form :model="form" label-width="120px">
        <el-form-item label="아이디">
          <el-input v-model="form.id" placeholder="아이디를 입력해주세요" clearable/>
        </el-form-item>
        <el-form-item label="비밀번호">
          <el-input v-model="form.password" placeholder="비밀번호를 입력해주세요" show-password/>
        </el-form-item>
        <el-form-item label="재입력">
          <el-input v-model="form.password2" placeholder="비밀번호를 재입력해주세요" show-password/>
        </el-form-item>
        <el-form-item>
          <div class="btn">
            <el-button type="info" size="large" @click="onSubmit">가입 완료</el-button>
          </div>
          <div class="btn" @click="SignUpRouter">
            <RouterLink to="/" style="text-decoration:none;"><el-button type="info" size="large">돌아가기</el-button></RouterLink>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const form = reactive({
  id:'',
  password:'',
  password2:'',
})

let flag = ref(false)

const onSubmit = () => {
  console.log('submit!')
}

function isCorrect() {
  if (form.password == form.password2) {
    flag = true
  } else {
    flag = false
  }
}

</script>

<style scope>
.container {
  display: flex;
  align-items: center;
  flex-direction: column;
  height: 90vh;
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
}
.el-button:hover {
  background-color: rgb(118, 118, 118) !important;
  transition: 0.2s;
}
.el-form-item__label {
  justify-content: center !important;
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
