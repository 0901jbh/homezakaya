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
	max-height: 100%;
	max-width: 100%;
	/* cursor: pointer; */
}
</style>
