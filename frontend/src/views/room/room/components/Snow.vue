<template>
  <div class="snow">
  </div>
</template>

<script>
export default {
  name: 'Snow',
  props: {
    isSnowing:Boolean,
  },
  data(){
    return {

    }
  },
  computed: {

  },
  watch: {
    isSnowing(value) {
      if (value) {
        console.log("눈이 내리고 있어요.")
        const head = document.getElementsByClassName('head')[0];
        function makeSnowflake() {
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
      
          head.appendChild(snowflake);
      
          setTimeout(() => {
            head.removeChild(snowflake);
            makeSnowflake()
          }, (duration + delay) * 1000);
        };

        for (let index = 0; index < 150; index++) {
          setTimeout(makeSnowflake(), 500 * index);
        };
      }
    }
  },
  methods: {

  },
}
</script>

<style scoped>
</style>