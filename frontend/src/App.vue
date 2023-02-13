<template>
  <div class="router-view">
    <ErrorPopUp />
    <RouterView />
  </div>
</template>

<script>
import { RouterLink, RouterView } from "vue-router";
import ErrorPopUp from "@/views/ErrorPopUp.vue";
import { useStore } from "vuex";
export default{
  components:{
    ErrorPopUp,
  },
  data() {
    return {
      store: useStore(),
      roomId : null,
      roomout : false,
    };
  },
  methods: {
   
    unLoadEvent: function (event) {
      
        
         
      //this.store.dispatch("roomModule/removeUserInRoom", this.store.state.userModule.user.userId);
      this.store.dispatch("roomModule/getRoomId", this.store.state.userModule.user.userId).then((result)=>{
        this.roomId = result;
      });

      //get new  host Id => changeHost

      this.store.dispatch("roomModule/removeUserInRoom", this.store.state.userModule.user.userId) //userinroom remove
        .then((result) => {
          if (result) {
            console.log("quit api 직전");
            this.store.dispatch("roomModule/quitRoom", this.roomId);  //room personcount -=1
            console.log("minus 사람수");
          } 
        })
        this.store.dispatch("roomModule/isClosed");
        this.store.dispatch("userModule/userLogout", this.store.state.userModule.user.userId);
        this.roomout = true;
        return;
        
        event.preventDefault();
         event.returnValue = '';
    },
},
  mounted() {
    window.addEventListener('beforeunload', this.unLoadEvent);
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.unLoadEvent);
  },
}

</script>

<style scoped>
.router-view {
  height: 100%;
}
</style>
