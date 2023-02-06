<template>
  <div class="head">
    <div style="height: 100%; width: 100%;">
      <div class="title" @click="clickTitle">Homezakaya</div>
    </div>
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

.head {
  position: absolute;
  top: 0px;
  height: 90vh;
  width: 100%;
  background: black;
  border-bottom: solid 10vh #6E0000;
  transition: all ease-in 1s;
}

.title {
  margin: 0 33vw;
  text-align: center;
  padding-top: 33vh;
  font-family: 'dokdo';
  color: white;
  font-size: 6rem;
  font-weight: 600;
  transition: all ease-in 1s;
  /* transition: transform ease-in 0.5s; */
}

.title:hover {
  color: rgb(247, 225, 197);
  cursor: pointer;
  /* transform: scale(1.1, 1.1); */
}

.head-change {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: black;
  height: 8vh;
  border-bottom: solid 2vh #6E0000;
  transition: all ease-in 1.5s;
  width: 100%;
}

.title-change {
  font-family: 'dokdo';
  color: white;
  font-size: 2.5rem;
  font-weight: 600;
  padding-left: 1vw;
  transition: all ease-in 1.5s;
}
</style>