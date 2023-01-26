<template>
  <div class="friend-container">
		<MyFriendRequestItem/>
		<MyFriendItem/>
	</div>
</template>

<script setup>
import MyFriendItem from './MyFriendItem.vue'
import MyFriendRequestItem from './MyFriendRequestItem.vue'

</script>

<style scoped>
.friend-container {
	height: 100%;
	background: #FFFFFF;
  border: 0.3rem solid #000000;
  border-radius: 30px;
}
</style>