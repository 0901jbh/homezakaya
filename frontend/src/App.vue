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
  data() {
    return {
      store: useStore(),
    };
  },
  methods: {
    unLoadEvent: function (event) {
      this.store.dispatch("userModule/userLogout", this.store.state.userModule.user.userId);
      return; 

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
