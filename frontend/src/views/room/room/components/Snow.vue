<template>
  <div class="snow">
  </div>
</template>

<script>
export default {
  name: 'Snow',
  props: {
    isSnowing: Boolean,
  },
  watch: {
    isSnowing() {
      this.snowing = this.isSnowing;
      this.snow();
    }
  },
  data(){
    return {
      snowing: this.isSnowing,
    }
  },
  computed: {

  },
  methods: {
    makeSnowflake(tagValue) {
      if (!this.snowing) return;
      const snowflake = document.createElement("div");
      const MIN_DURATION = 5;
      const delay = Math.random() * 5;
      const initialOpacity = Math.random();
      const duration = Math.random() * 20 + MIN_DURATION;
  
      snowflake.classList.add("snowflake");
      snowflake.style.left = `${Math.random() * 98}%`;
      snowflake.style.animationDelay = `${delay}s`;
      snowflake.style.opacity = initialOpacity;
      snowflake.style.animation = `fall ${duration}s linear`;
  
      tagValue.appendChild(snowflake);
  
      setTimeout(() => {
        if (!this.snowing) return;
        console.log(this.snowing)
        tagValue.removeChild(snowflake);
        this.makeSnowflake(tagValue)
      }, (duration + delay) * 1000);
    },

    snow() {
      if (this.snowing) {
        const head = document.getElementsByClassName('head')[0];
        for (let index = 0; index < 150; index++) {
          if (!this.snowing) return;
          console.log(this.snowing)
          setTimeout(this.makeSnowflake(head), 500 * index);
        };
      }
    },
  },
}
</script>

<style scoped>
</style>