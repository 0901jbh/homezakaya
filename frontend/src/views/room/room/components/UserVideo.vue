<template>
	<div id="video" v-if="streamManager">
		<ov-video :stream-manager="streamManager" />
		<div id="nametag">
			<p>{{ clientData }}</p>
		</div>
	</div>
</template>

<script>
import OvVideo from './OvVideo.vue';

export default {
	name: 'UserVideo',

	components: {
		OvVideo,
	},

	props: {
		streamManager: Object,
	},

	computed: {
		clientData() {
			const { clientData } = this.getConnectionData();
			return clientData;
		},
	},

	methods: {
		getConnectionData() {
			const { connection } = this.streamManager.stream;
			return JSON.parse(connection.data);
		},
	},
};
</script>

<style scoped>
#video {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 10px;
}

#nametag p {
	display: inline-block;
	background: #f8f8f8;
	padding-left: 5px;
	padding-right: 5px;
	color: #777777;
	font-weight: bold;
	border-bottom-right-radius: 4px;
	border-bottom-left-radius: 4px;
	margin: 0;
}
</style>