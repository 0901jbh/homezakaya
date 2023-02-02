<template>
	<div id="video" v-if="streamManager">
		<ov-video :stream-manager="streamManager" @click="userInfo" />
		<div id="nametag">
			<img v-if="isHost" src="../../../../assets/crown.png" alt="crown img"
				style="display: inline-block; width:20px; height:20px; padding-right: 5px;" />
			<p>{{ clientId }}</p>
			<p>{{ clientNick }}</p>
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
		clientId() {
			const { clientId } = this.getConnectionData();
			return clientId;
		},
		clientNick() {
			const { clientNick } = this.getConnectionData();
			return clientNick;
		},
		isHost() {
			const { isHost } = this.getConnectionData();
			return isHost;
		},
	},

	methods: {
		getConnectionData() {
			const { connection } = this.streamManager.stream;
			return JSON.parse(connection.data);
		},
		userInfo() {
			console.log("클릭")
		}
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

#nametag {
	display: flex;
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