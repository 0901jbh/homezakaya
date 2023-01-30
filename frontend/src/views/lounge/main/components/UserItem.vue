<template>
  <div class="wrapper">
    <div class="first-line">
      <div class="user-state"></div>
      <div class="name-and-cancel">
        <div>내가진짜전병현임</div>
        <button class="delete-friend" type="button" @click="deleteFriend">X</button>
      </div>
    </div>
    <div class="second-line">
      <img src="../../../../assets/beer.png" alt="맥주 이미지" style="width:10%; height:15%; ">
      <div>4.5</div>
      <img src="../../../../assets/dokuri.png" alt="사케 이미지" style="width:10%; height:15%; ">
      <div>0.5잔</div>
      <div class="follow-friend" type="button" @click="followFriend">따라가기</div>
      <!-- <div class="request-frined" type="button" @click="requestFriend">친구요청</div> -->
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.wrapper {
  width: 100%;
  height: 10%;
  color: white;
  background: linear-gradient(180deg, #959595 202.91%, rgba(0, 0, 0, 0.709847) 260.73%, rgba(84, 84, 84, 0) 302.91%);
  border-radius: 20px;
  padding: 7%;
  font-size: 1rem;
  margin: 2.5% 0;
}
.first-line{
  display: grid;
  grid-template-columns: 1fr 8fr;
  align-items: center;
}
.user-state{
  width: 1rem;
  height: 1rem;
  /* 온라인 표시 */
  background: radial-gradient(50% 50% at 50% 50%, #1CEE18 0%, rgba(108, 208, 106, 0) 100%);
  /* 오프라인 표시
  background: radial-gradient(50% 50% at 50% 50%, #EE1818 0%, rgba(208, 106, 106, 0) 100%); */
}

.name-and-cancel{
  display: flex;
  justify-content: space-between;
}
.delete-friend{
  color:white;
  border: none;
  text-decoration: none;
  background:none;
  cursor:pointer;
}
.delete-friend:hover {
  color: red;
}
.second-line{
  padding-top: 2%;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}
.follow-friend{
  display: flex;
  justify-content: center;
  align-items: center;
  width:30%;
  background: grey;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  cursor:pointer;
} 
.follow-friend:hover{
  background: #823D3D;
}
</style>