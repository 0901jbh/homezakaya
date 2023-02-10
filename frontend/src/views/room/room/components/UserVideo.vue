<template>
	<div id="video" v-if="streamManager !== undefined">
		<el-popover :width="250"
			popper-style="background: rgb(235 153 153); border: rgb(235 153 153); box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 15px;"
			trigger="click" placement="right">
			<template #reference>
				<div id="cam">
					<ov-video :streamManager="streamManager" @click="userInfo" />
				</div>
			</template>
			<template #default>
				<div id="user_setting" style="display: flex; gap: 16px; flex-direction: column;">
					<p class="user_nickname" style="margin: 0; font-size: 20px; color: white; align-self:center;">
						{{ username }}
					</p>
					<div class="user_manner_alcohol"
						style="margin: 0; display: flex; gap: 16px; flex-direction: row; justify-content: center;">
						<img src="@/assets/images/manner_w.png" alt="manner_w img" style="width:28px; height:28px;">
						<p style="margin: 0; font-size: 20px; color: white; align-self:center;">{{ mannerPoint }}</p>
						<img src="@/assets/images/alcohol_w.png" alt="alcohol_w img" style="width:28px; height:28px;">
						<p style="margin: 0; font-size: 20px; color: white; align-self:center;">{{alcoholPoint}}잔</p>
					</div>
					<!-- <div class="content" style="width: 60%; text-decoration:none;">
						별점
					</div> -->
					<el-rate v-if="!myVideo" v-model="manner_rate" size="large" allow-half
						style="justify-content: center;" />
					<div v-if="!myVideo && !isFriend" @click="friend" class="content" style="width: 60%; text-decoration:none;">
						친구 추가
					</div>
					<div v-if="!myVideo && isHostView" @click="host" class="content" style="width: 60%; text-decoration:none;">
						방장 변경
					</div>
					<div v-if="!myVideo && isHostView" @click="kick" class="content"
						style="width: 60%; text-decoration:none;">
						강제 퇴장
					</div>
				</div>
			</template>
		</el-popover>
		<!-- <div id="cam">
			<ov-video :stream-manager="streamManager" @click="userInfo" />
		</div> -->
		<div id="nametag">
			<img v-if="isHost" src="@/assets/images/crown.png" alt="crown img"
				style="display: inline-block; width:20px; height:20px; padding-right: 5px;" />
			<p>{{ username }}</p>
		</div>
	</div>
</template>

<script>
import OvVideo from './OvVideo.vue';
import { useStore } from 'vuex';
import axios from "axios";
export default {
	name: 'UserVideo',

	components: {
		OvVideo,
	},

	props: {
		streamManager: Object,
		myVideo: Boolean,
		isHostView: Boolean,
	},

	data() {
		return {
			store: useStore(),
			manner_rate:null,
			mannerPoint: 0,
			alcoholPoint : 0,
			
		}
	},

	computed: {
		username() {
			const { username } = this.getConnectionData();
			return username;
		},
		userId() {
			const { userId } = this.getConnectionData();
			return userId;
		},
		isHost() {
			const clientData = this.getConnectionData();
			return clientData.hostId == clientData.userId;
		},
		isFriend() {
			const clientData = this.getConnectionData();
			if(clientData.friends != undefined)
				return clientData.friends.includes(clientData.userId);
			else
				return false;
		}
		// isHost 값을 주는 것 보다는 hostId와 clientId가 일치하는지 직접 비교하는게 나을듯
	
	},

	methods: {
		getConnectionData() {
			const { connection } = this.streamManager.stream;
			console.log(JSON.parse(connection.data));
			return JSON.parse(connection.data);
		},
		userInfo() {
			console.log("userInfo");
			this.store.dispatch("userModule/getUserPoint",this.userId).then((response) =>{
				this.mannerPoint = response.mannerPoint;
				this.alcoholPoint = response.alcoholPoint;
			});
		},
		kick() {
			this.$emit('kickUser', this.username);
		},
		host() {
			this.$emit('changeHost', this.userId);
		},
		friend(){
			this.$emit('friendRequest', this.userId);
		},
		
	},
	beforeDestroy(){
		console.log("before destory");
	}
};
</script>

<style scoped>
#cam {
	max-height: 90%;
	max-width: 100%;
	cursor: pointer;
}

/* .content {
	display: flex;
	flex-direction: row;
	justify-content: center;
	align-items: center;
	padding: 15px 35px;
	gap: 10px;

	width: 5%;
	height: 3vh;

	color: black;
	font-size: 2rem;
	font-weight: 700;
	background: white;
	box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5);
	border-radius: 53px;

	cursor: pointer;
} */

.content {
	display: flex;
	flex-direction: row;
	justify-content: center;
	align-items: center;
	align-self: center;
	padding: 15px 20px;
	gap: 10px;

	width: 100px;
	height: 10px;
	left: 52px;
	top: 105px;

	color: white;
	font-size: 1rem;
	font-weight: 70;
	background: rgb(121 65 65);
	box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5);
	border-radius: 53px;

	cursor: pointer;
}

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