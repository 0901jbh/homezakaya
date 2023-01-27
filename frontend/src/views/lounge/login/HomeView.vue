<template>
  <header>
    <HeaderItem />
  </header>
  <div class="container">
    <div>
      <img src="../../../assets/HomeLogin.jpg" alt="izakaya img" style="width: 30vw; height:90vh;">
    </div>
    <div class="wrapper">
      <div class="title">
        <div>Homezakaya</div>
      </div>
      <div>
        <div class="comment">좋은 사람들과 새로운 술자리</div>
        <div class="box">
          <div>
            <el-form :model="form" label-width="10vw" margin-top="10vh">
              <el-form-item label="아이디" size='large'>
                <el-input v-model="form.id" placeholder="아이디를 입력해주세요" clearable />
              </el-form-item>
              <br>
              <el-form-item label="비밀번호" size="large">
                <el-input v-model="form.password" placeholder="비밀번호를 입력해주세요" show-password />
              </el-form-item>
              <br>
              <br>
              <el-form-item>
                <div class="btn">
                  <el-button type="info" size="large" @click="onSubmit">로그인</el-button>
                </div>
                <div class="btn" @click="SignUpRouter">
                  <RouterLink to="/signup" style="text-decoration:none;"><el-button type="info"
                      size="large">회원가입</el-button></RouterLink>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
  <footer>
    <FooterItem />
  </footer>
</template>

<script setup>
import HeaderItem from '../menu/HeaderItem.vue'
import FooterItem from '../menu/FooterItem.vue'
import { reactive } from 'vue'

const form = reactive({
  id: '',
  password: '',
})

const onSubmit = () => {
  console.log('submit!')
}
</script>

<style scoped>
.container {
  display: grid;
  grid-template-columns: 2fr 3fr;
  height: 90vh;
}

.title {
  font-family: 'Inter';
  color: #9D0101;
  font-style: normal;
  font-weight: 700;
  font-size: 12vh;
  text-align: center;
  padding-top: 5vh;
}

.comment {
  font-weight: 600;
  font-size: 4vh;
  padding-top: 3vh;
  padding-left: 23vw;
}

.box {
  margin-top: 14vh;
  padding-left: 5vw;
  display: flex;
  justify-content: center;
}

.btn {
  margin-left: 4vw;
}

.el-button {
  background-color: black !important;
}

.el-button:hover {
  background-color: rgb(118, 118, 118) !important;
  transition: 0.2s;
}
</style>