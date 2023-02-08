<template>
	<div id="video" v-if="streamManager">
		<ov-video :streamManager="streamManager" />
		<div id="nametag">
			<!-- <img v-if="isHost" src="@/assets/images/crown.png" alt="crown img"
				style="display: inline-block; width:20px; height:20px; padding-right: 5px;" /> -->
			<p>{{ username }}</p>
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
		username() {
			const { username } = this.getConnectionData();
			return username;
		},
	},

	methods: {
		getConnectionData() {
			const { connection } = this.streamManager.stream;
			return JSON.parse(connection.data);
		},
		// userInfo() {
		// 	console.log("클릭")
		// }
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