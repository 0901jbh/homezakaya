<template>
  <div class="wrapper">
    <div class="first-line">
      <div>{{ data.nickname }} 님께서</div>
      <button class="cancel" type="button" @click="deleteInvite">X</button>
    </div>
    <div class="second-line">초대 요청을 보내셨습니다.</div>
    <div class="third-line">
      <div class="approve" type="button" @click="approveInvite">수락</div>
    </div>
  </div>
  <div></div>
</template>

<script setup>
import { onMounted } from '@vue/runtime-core';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const props = defineProps({
  invite: Object,
})

const store = useStore()
const router = useRouter()

const data = ref({
  nickname: '',
})

onMounted(async () => {
  const friends = await store.getters["friendModule/getFriends"]
  friends.forEach(friend => {
    if (friend.userId == props.invite.fromUserId) {
      data.value.nickname = friend.nickname;
    }
  });
})

const deleteInvite = () => {
  store.dispatch('roomModule/removeInvite', {
    fromUserId: props.invite.fromUserId,
    toUserId: store.state.userModule.user.userId
  })
}

const approveInvite = async () => {
  const fromUserId = props.invite.fromUserId;
  const roomId = await store.dispatch('roomModule/getRoomId', fromUserId)
  .then((result) => {
    if (result == -1) {
      store.commit("errorModule/SET_STATUS", 403);
    }
    else {
      store.dispatch("roomModule/checkValidRoom", result).then((status) => {
        if(status != 200){
          store.commit("errorModule/SET_STATUS", status);
        }
        else{
          router.push({ name: 'wait', params: { roomId: result } });
        }
      })
    }
    deleteInvite()
  })
}

</script>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  width: 86%;
  height: 15%;
  color: white;
  background: #252836;
  border: solid 2px #e27b66;
  border-radius: 20px;
  padding: 7%;
  font-size: 1rem;
  margin: 2.5% 0;
  transition: 0.1s ease-in;
}

.wrapper:hover {
  transform: scale(1.05, 1.05);
}

.first-line {
  display: flex;
  justify-content: space-between;
}

.cancel {
  color: white;
  border: none;
  text-decoration: none;
  background: none;
  cursor: pointer;
}

.cancel:hover {
  color: #e27b66;
}

.third-line {
  padding-top: 2%;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}

.approve {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30%;
  background: #e27b66;
  box-shadow: 0px 0px 15px #e27b66;
  border-radius: 20px;
  cursor: pointer;
  color: black;
}

.approve:hover {
  opacity: 0.75;
  cursor: pointer;
}
</style>