<template>
    <div class="game-container">
        여기는 게임화면
    </div>
</template>

<script setup>
const props = defineProps({
    gameStart: Boolean,
    renderKey: Number,
    gameStatus: Number,
})

</script>

<style scoped>
.game-container{
    border: solid white;
    height: 40%;
    background-color: #2E303F;
    border: 2px solid #CBCBCB;
    border-radius: 30px;
    margin-bottom: 3vh;
}
</style>