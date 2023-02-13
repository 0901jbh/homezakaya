<template>
  <div class="drop">
  </div>
</template>

<script>
export default {
  name: 'Drop',
  props: {
    isDrop: Boolean,
    dropIdx: Number,
  },
  watch: {
    isDrop(value) {
      this.dropping = value;
      this.drop();
    }
  },
  data(){
    return {
      dropping: this.isDrop,
      dropItem: ["snowflake", "maple", "rain", "sakura"],
    }
  },
  computed: {
    
  },
  methods: {
    makeDrop(tagValue) {
      if (!this.dropping) return;
      const dropObject = document.createElement("div");
      const MIN_DURATION = 5;
      const delay = Math.random() * 5;
      const initialOpacity = Math.random();
      let duration = Math.random() * 20 + MIN_DURATION;
      if (this.dropIdx == 2) {
        duration = Math.random() * MIN_DURATION;
      }

      dropObject.classList.add(`${this.dropItem[this.dropIdx]}`);
      dropObject.style.left = `${Math.random() * 97}%`;
      dropObject.style.animationDelay = `${delay}s`;
      dropObject.style.opacity = initialOpacity;
      dropObject.style.animation = `fall ${duration}s linear`;
      if (this.dropIdx == 1 || this.dropIdx == 3) {
        dropObject.style.transition = `all ${duration}s linear`
        dropObject.style.transform = `rotate(${Math.random() * 1440}deg)`;
        // dropObject.style.transform = `rotateX(${Math.random() * 7200}deg)`;
        // dropObject.style.transform = `skewY(${Math.random() * 60}deg)`;
      }
      

      tagValue.appendChild(dropObject);
  
      setTimeout(() => {
        if (!this.dropping) return;
        tagValue.removeChild(dropObject);
        this.makeDrop(tagValue)
      }, (duration + delay) * 1000);
    },

    drop() {
      if (this.dropping) {
        const head = document.getElementsByClassName('head')[0];
        for (let index = 0; index < 150; index++) {
          if (!this.dropping) return;
          setTimeout(this.makeDrop(head), 500 * index);
        };
      }
    },
  },
}
</script>

<style scoped>
</style>