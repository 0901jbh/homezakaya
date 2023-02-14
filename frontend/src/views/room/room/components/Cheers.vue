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
      left: -100%;
    }
    50% {
      left: 35vw;
    }
    65% {
      left: 28vw;
    }
    70% {
      left: 30vw;
      opacity: 100;
    }
    100% {
      left: 30vw;
      opacity: 0;
    }
}
</style>