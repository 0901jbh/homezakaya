<template>
  <div class="head">
    <div class="title">
      <div style="margin-right: 4vw; font-family: 'dodum';">[{{ props.category }}]</div>
      <div>{{ props.title }}</div>
    </div>
    <div @click="buttonClick" style="color:white; cursor:pointer;">
      뿌리기
    </div>
    <div class="info">
      {{ headCount }} / {{ headCountMax }}
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits(["clickDrop"])

const props = defineProps({
  title: String,
  category: String,
  headCount: Number,
  headCountMax: Number,
})

function buttonClick() {
  emit("clickDrop");
}
</script>

<style scoped>
@font-face {
  font-family: 'dokdo';
  src: url('@/assets/fonts/Dokdo-Regular.ttf')
}

@font-face {
  font-family: 'eastsea';
  src: url('@/assets/fonts/EastSeaDokdo-Regular.ttf')
}

@font-face {
  font-family: 'hansans';
  src: url('@/assets/fonts/BlackHanSans-Regular.ttf')
}

@font-face {
  font-family: "dodum";
  src: url("@/assets/fonts/GowunDodum-Regular.ttf");
}

.head {
  background-color: black;
  height: 10vh;
  /* border-bottom: solid 2vh #6E0000; */
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-family: 'hansans';
  color: white;
  font-size: 5vh;
  /* font-weight: 600; */
  padding-left: 1vw;
  display: flex;
  align-items: center;

}

.info {
  font-family: 'dodum';
  color: white;
  font-size: 4vh;
  font-weight: 600;
  margin-right: 1vw;
}
</style>