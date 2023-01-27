<template>
  <div class="head">
    <div class="title">Homezakaya</div>
    <!-- <div class="info">내 정보</div> -->
  </div>
</template>

<script setup>
</script>

<style scoped>
@font-face {
  font-family: 'dokdo';
  src: url('../../../assets/fonts/Dokdo-Regular.ttf')
}

@font-face {
  font-family: 'eastsea';
  src: url('../../../assets/fonts/EastSeaDokdo-Regular.ttf')
}

.head {
  background-color: black;
  height: 8vh;
  border-bottom: solid 2vh #6E0000;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-family: 'dokdo';
  color: white;
  font-size: 5vh;
  font-weight: 600;
  padding-left: 1vw;
}

.info {
  color: white;
  font-size: 4vh;
  font-weight: 600;
  margin-right: 1vw;
}
</style>