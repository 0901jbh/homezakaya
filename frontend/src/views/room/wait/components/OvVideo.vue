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

<style>
video {
	max-height: 90%;
	max-width: 90%;
	/* cursor: pointer; */
}
</style>
