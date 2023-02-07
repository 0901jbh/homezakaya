<template>
  <div class="head">
    <div class="title" @click="clickTitle">Homezakaya</div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";

const props = defineProps({
  isEnter: Boolean
})


const emit = defineEmits([
  'clickTitle'
])
const MIN_DURATION = 5;

onMounted(() => {
  if (!props.isEnter) {
    const head = document.getElementsByClassName('head')[0];
    function makeSnowflake() {
      const snowflake = document.createElement("div");
      const delay = Math.random() * 10;
      const initialOpacity = Math.random();
      const duration = Math.random() * 20 + MIN_DURATION;

      snowflake.classList.add("snowflake");
      snowflake.style.left = `${Math.random() * 98}%`;
      snowflake.style.animationDelay = `${delay}s`;
      snowflake.style.opacity = initialOpacity;
      snowflake.style.animation = `fall ${duration}s linear`;

      head.appendChild(snowflake);

      setTimeout(() => {
        head.removeChild(snowflake);
        makeSnowflake()
      }, (duration + delay) * 1000);
    }

    for (let index = 0; index < 150; index++) {
      setTimeout(makeSnowflake(), 500 * index);
    }
  }
  else {
    const head = document.getElementsByClassName('head')[0];
    const title = document.getElementsByClassName('title')[0];

    head.className = "head-change";
    title.className = "title-change";
  }
})

const clickTitle = () => {
  const head = document.getElementsByClassName('head')[0];
  const title = document.getElementsByClassName('title')[0];

  head.className = "head-change";
  title.className = "title-change";

  setTimeout(function () {
    emit('clickTitle');
  }, 1700)
}

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

.head{
  position: block;
  top: 0px;
  height: 57vh;
  width: 100%;
  background: black;
  border-bottom: solid 10vh #6E0000;
  transition: all ease-in 1s;
  padding-top: 33vh;
}

.title{
  display: block;
  margin-left:35vw;
  /* margin-top:33vh; */
  width: max-content;
  font-family: 'dokdo';
  color: white;
  font-size: 6vw;
  font-weight: 600;
  transition: all ease-in 1s;
  transition: transform ease-in 0.3s;
}

.title:hover {
  color: rgb(247, 225, 197);
  cursor: pointer;
  transform: scale(1.05, 1.05);
}

.head-change {
  background-color: black;
  height: 8vh;
  border-bottom: solid 2vh #6E0000;
  transition: all ease-out 1.5s;
  width: 100%;
}

.title-change {
  font-family: 'dokdo';
  color: white;
  font-size: 2.5rem;
  font-weight: 600;
  padding-top: 0.6vh;
  padding-left: 1vw;
  transition: all ease-out 1.5s;
}
</style>