<template>
  <div class="wrapper">
    <div class="first-line">
      <div>전병현 님께서</div>
      <button class="cancel" type="button" @click="deleteRequest">X</button>
    </div>
    <div class="second-line">친구요청을 보냈습니다.</div>
    <div class="third-line">
      <img src="../../../../assets/beer.png" alt="맥주 이미지" style="width:10%; height:15%; ">
      <div>4.5</div>
      <img src="../../../../assets/dokuri.png" alt="사케 이미지" style="width:10%; height:15%; ">
      <div>0.5잔</div>
      <div class="approve" type="button" @click="approveRequest">수락</div>
    </div>
  </div>

</template>

<script setup>
const deleteRequest = ()=>{
  console.log('deleteRequest!')
}
</script>

<style scoped>
.wrapper {
  /* border: solid 5px black; */
  width: 100%;
  height: 15%;
  color: white;
  background: linear-gradient(180deg, #A44242 202.91%, rgba(0, 0, 0, 0.709847) 260.73%, rgba(84, 84, 84, 0) 302.91%);
  border-radius: 30px;
  padding: 7%;
  font-size: 1rem;
}
.first-line{
  display: flex;
  justify-content: space-between;
}
.cancel{
  color:white;
  border: none;
  text-decoration: none;
  background:none;
  cursor:pointer;
}
.cancel:hover {
  color: red;
}
.third-line{
  padding-top: 2%;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}
.approve{
  display: flex;
  justify-content: center;
  align-items: center;
  width:30%;
  background: #823D3D;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  cursor:pointer;
} 
.approve:hover{
  background: grey;
}
</style>