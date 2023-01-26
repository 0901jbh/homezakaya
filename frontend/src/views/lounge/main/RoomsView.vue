<template>
  <header>
    <UserHeader />
  </header>
  <div class="container">
    <div class="title-rooms">
      <div class="title">
        <div class="content">
          Table
        </div>
      </div>
      <div class="rooms">
        <RoomsIndexItem />
      </div>
    </div>
    <FriendsItem />
  </div>
  <footer>
    <FooterItem />
  </footer>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
import UserHeader from '../menu/UserHeader.vue'
import FooterItem from '../menu/FooterItem.vue'
import RoomsIndexItem from './components/RoomsIndexItem.vue'
import FriendsItem from './components/FriendsItem.vue'
import InfoItem from '../menu/InfoItem.vue'

</script>

<style scoped>
.container {
  height: 90vh;
  display: grid;
  grid-template-columns: 5fr 2fr;
  margin: 0 4%;
}

.title-rooms {
  display: grid;
  grid-template-rows: 1fr 5fr;
  height: 90vh;
  padding-right: 4vh;
}

.title {
  display: flex;
  align-items: center;
  padding: 0 4%;
}

.content {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 15px 35px;
  gap: 10px;

  width: 10%;
  height: 5vh;
  left: 52px;
  top: 105px;

  color: white;
  font-size: 2rem;
  font-weight: 700;
  background: #000000;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 53px;
}

.rooms {
  background: #FFFFFF;
  border: 0.3rem solid #000000;
  border-radius: 30px;
  padding: 4vh;
  margin-bottom: 4vh;
}
</style>