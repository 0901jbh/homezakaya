<template>
  <div class="head">
    <RouterLink to="/rooms" style="text-decoration:none;">
      <div class="title">Homezakaya</div>
    </RouterLink>
    <!-- <div class="info">
      <img src="../../../assets/profile.png" alt="profile img" style="width:60px; height:60px;">
    </div> -->
    <el-popover :width="300"
      popper-style="background: rgb(235 153 153); border: rgb(235 153 153); box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
      trigger="click">
      <template #reference>
        <img src="../../../assets/profile.png" alt="profile img" style="width:60px; height:60px;">
      </template>
      <template #default>
        <div class="MyInfo" style="display: flex; gap: 32px; flex-direction: column">
          <p class="MyInfo_nickname" style="margin: 0; font-size: 30px; color: white; align-self:center;">
            팀장은역시고진석
          </p>
          <div class="MyInfo_manner"
            style="margin: 0; display: flex; gap: 16px; flex-direction: row; justify-content: center;">
            <img src="../../../assets/beer.png" alt="beer img" style="width:40px; height:40px;">
            <p style="margin: 0; font-size: 30px; color: white; align-self:center;">3.5</p>
          </div>
          <div class="MyInfo_alcohol"
            style="margin: 0; display: flex; gap: 16px; flex-direction: row; justify-content: center;">
            <img src="../../../assets/dokuri.png" alt="dokuri img" style="width:40px; height:40px;">
            <p style="margin: 0; font-size: 30px; color: white; align-self:center;">14잔</p>
          </div>
          <RouterLink class="MyInfo_edit content" to="/edit" style="text-decoration:none;">
            정보수정
          </RouterLink>
          <RouterLink class="MyInfo_logout content" to="/" style="text-decoration:none;">
            로그아웃
          </RouterLink>
        </div>
      </template>
    </el-popover>
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

.content {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  align-self: center;
  padding: 15px 35px;
  gap: 10px;

  width: 100px;
  height: 10px;
  left: 52px;
  top: 105px;

  color: white;
  font-size: 1rem;
  font-weight: 70;
  background: rgb(121 65 65);
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 53px;
}
</style>