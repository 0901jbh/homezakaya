<template>
  <div class="cheers">
    건 배
  </div>
</template>

<script>
export default {
  name: 'Cheers',
}
</script>

<style scoped>
.cheers{
  position: absolute;
  left: -100%;
  top: 25vh;
  font-size: 15rem;
  text-shadow: -2px 0 black,-2px 0 black,-2px 0 black,-2px 0 black;
  color: white;
  z-index: 1000;
  animation: cheer 2s ease-out;
  overflow: hidden;
}

@keyframes cheer{
    0% {
        transform: translateX(-100%);
    }
    50% {
      transform: translateX(375%);
    }
    65% {
      transform: translateX(335%);
    }
    70% {
        transform: translateX(350%);
        opacity: 100;
    }
    100% {
        transform: translateX(350%);
        opacity: 0;
    }
}


</style>