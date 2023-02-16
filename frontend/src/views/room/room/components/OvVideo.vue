<template>
	<video autoplay />
</template>

<script>
export default {
	name: 'OvVideo',

	props: {
		streamManager: Object,
	},

	mounted() {
		this.streamManager.addVideoElement(this.$el);
	},
};
</script>

<style scoped>
video {
	max-height: 100%;
	max-width: 100%;
	border: solid 1.5px white;
	overflow: hidden;
	border-radius: 20px;
	/* cursor: pointer; */
}
</style>
