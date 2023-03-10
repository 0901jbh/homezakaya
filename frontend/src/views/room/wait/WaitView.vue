<template>
  <header>
    <RoomWaitHeader :title="title" :category="category" :headCount="headCount" :headCountMax="headCountMax" />
  </header>
  <div id="main-container" class="container">
    <div id="session">
      <div id="out">
        <div id="out_btn">
          <div @click="leaveSession">
            X
          </div>
        </div>
      </div>
      <div id="txt">입장전 신분증 검사가 있겠습니다.</div>
      <div id="minz">
        <div id="container_txt">
          <div style="font-size: 3rem;">
            주민등록증
          </div>
          <div style="font-size: 2rem; width: 90%; text-align: center; ">
            {{ myUserName }}({{ myUserId }})
          </div>
          <div style="font-size: 1.5rem;">
            매너도수 {{ mannerPoint }}
            <br>
            알콜도수 {{ alcoholPoint }}
          </div>
        </div>
        <div id="container_pic">
          <user-video :streamManager="publisher" />
        </div>
        <div id="container_in">
          <div>홈자카야</div>
          <img src="@/assets/images/in.png" alt="in img" style="height: 50%">
        </div>
      </div>
      <div id="option-footer">
        <div id="mute">
          <div class="onoff" @click="clickMuteVideo">
            <img v-if="videoActive" src="@/assets/images/video_on.png" alt="video on img" />
            <img v-else src="@/assets/images/video_off.png" alt="video on img" />
          </div>
          <div class="onoff" @click="clickMuteAudio">
            <img v-if="audioActive" src="@/assets/images/audio_on.png" alt="audio on img" />
            <img v-else src="@/assets/images/audio_off.png" alt="audio on img" />
          </div>
        </div>
        <div id="enter">
          <div style=" margin: auto 30px auto 0px; color: white;" class="enter_content">{{ title }} 방에 입장하시겠습니까?</div>
          <div class="content enter_content" @click="enterRoom">입장하기</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RoomWaitHeader from '../menu/RoomWaitHeader.vue'
import { OpenVidu } from "openvidu-browser";
import axios from "axios";
import UserVideo from "./components/UserVideo.vue";
import { useStore } from 'vuex';

axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL = 'http://localhost:5000';
const OPENVIDU_SERVER_URL = 'https://i8a606.p.ssafy.io:8443';
const OPENVIDU_SERVER_SECRET = 'ssafy';

export default {
  name: "WaitView",

  components: {
    UserVideo,
    RoomWaitHeader,
  },

  data() {
    return {
      store: useStore(),
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      publisher: undefined,
      // subscribers: [],

      // Join form
      roomId: this.$route.params.roomId,
      title: this.$route.params.title,
      myUserId: "",
      myUserName: "",

      mannerPoint: null,
      alcoholPoint: null,

      title: "",
      category: "",
      headCountMax: 8,
      headCount: 1,

      videoActive: false,
      audioActive: false,
    };
  },

  methods: {

    clickMuteVideo() {
      if (this.publisher.stream.videoActive) {
        this.publisher.publishVideo(false)
        this.videoActive = false
      } else {
        this.publisher.publishVideo(true)
        this.videoActive = true
      }
    },

    clickMuteAudio() {
      if (this.publisher.stream.audioActive) {
        this.publisher.publishAudio(false)
        this.audioActive = false
      } else {
        this.publisher.publishAudio(true)
        this.audioActive = true
      }
    },

    joinSession() {
      this.OV = new OpenVidu();

      this.session = this.OV.initSession();

      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      this.getToken(`${this.roomId}wait`).then((token) => {
        this.session.connect(token, { username: this.myUserName })
          .then(() => {

            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined,
              videoSource: undefined,
              publishAudio: this.videoActive,
              publishVideo: this.audioActive,
              resolution: "640x480",
              frameRate: 30,
              insertMode: "APPEND",
              mirror: true,
            });

            this.publisher = publisher;

            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.log("There was an error connecting to the session:", error.code, error.message);
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      if (this.session) this.session.disconnect();

      this.session = undefined;
      this.publisher = undefined;
      this.OV = undefined;

      window.removeEventListener("beforeunload", this.leaveSession);

      this.$router.push({ name: 'rooms' });
    },

    async getToken(mySessionId) {
      console.log(mySessionId);
      const sessionId = await this.createSession(mySessionId);
      return await this.createToken(sessionId);
    },

    createSession(sessionId) {
      return new Promise((resolve, reject) => {
        let data = JSON.stringify({ customSessionId: sessionId });
        axios
          .post(`${OPENVIDU_SERVER_URL}/openvidu/api/sessions`, data, {
            headers: {
              Authorization: `Basic ${btoa(
                `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
              )}`,
              'Content-Type': 'application/json',
            },
          })
          .then((response) => {
            resolve(response.data.id);
          })
          .catch((response) => {
            let error = { ...response };
            if (error?.response?.status === 409) {
              resolve(sessionId);
            } else if (
              window.confirm(
                `No connection to OpenVidu Server. This may be a certificate error at "${OPENVIDU_SERVER_URL}"\n\nClick OK to navigate and accept it. ` +
                `If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`
              )
            ) {

              window.location.assign(`${OPENVIDU_SERVER_URL}/accept-certificate`);
            }
          });
      });
    },

    createToken(sessionId) {
      return new Promise((resolve, reject) => {
        let data = {};
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
            data,
            {
              headers: {
                Authorization: `Basic ${btoa(
                  `OPENVIDUAPP:${OPENVIDU_SERVER_SECRET}`
                )}`,
                'Content-Type': 'application/json',
              },
            }
          )
          .then((response) => {
            resolve(response.data.token);
          })
          .catch((error) => reject(error));
      });
    },

    async getRoom() {
      const roomData = await this.store.dispatch("roomModule/getRoom", this.$route.params.roomId);
      const room = JSON.parse(JSON.stringify(roomData));
      this.title = room.title;
      this.category = room.category;
      this.headCount = room.personCount;
      this.headCountMax = room.personLimit;
    },

    getUser() {
      const user = this.store.state.userModule.user;
      this.myUserId = user.userId;
      this.myUserName = user.nickname;
    },

    userInfo() {
      console.log("userInfo");
      this.store.dispatch("userModule/getUserPoint", this.myUserId).then((response) => {
        this.mannerPoint = parseFloat(response.mannerPoint.toFixed(2));
        this.alcoholPoint = response.alcoholPoint;
      });
    },

    enterRoom() {
      this.store.dispatch('roomModule/doEnterRoom', {
        userId: this.myUserId,
        roomId: this.roomId,
      }).then((status) => {
        if (status == 200) {
          this.$router.push({ name: 'room', params: { roomId: this.roomId }, query: { video: this.videoActive, audio: this.audioActive } }).then(() => {
            if (this.session) this.session.disconnect();
            this.session = undefined;
            this.publisher = undefined;
            this.OV = undefined;

            window.removeEventListener("beforeunload", this.leaveSession);
          })
        }
        else {
          this.store.commit("errorModule/SET_STATUS", status);
        }
      });
    },
  },

  async mounted() {
    await this.getRoom();
    this.getUser();
    this.userInfo();
    this.joinSession();
  },
};

</script>

<style scoped>
@font-face {
  font-family: 'dokdo';
  src: url('@/assets/fonts/Dokdo-Regular.ttf')
}

@font-face {
  font-family: 'eastsea';
  src: url('@/assets/fonts/EastSeaDokdo-Regular.ttf')
}

@font-face {
  font-family: 'hansans';
  src: url('@/assets/fonts/BlackHanSans-Regular.ttf')
}

@font-face {
  font-family: "dodum";
  src: url("@/assets/fonts/GowunDodum-Regular.ttf");
}

#main-container {
  display: flex;
  background: #121212;
  height: 90vh;
}

#session {
  display: flex;
  flex-direction: column;
  align-content: center;
  align-items: center;
  background: #2F2F2F;
  margin: 5vh auto;
  height: 70vh;
  width: 80%;
}

#chatting-container {
  display: flex;
  flex-direction: column;
  background: #e9e9e9;
  border-radius: 30px;
  width: 20vw;
}

#chats {
  height: 90%;
  overflow-y: scroll;
  margin: 5%;
  margin-bottom: 0;
}

#chats::-webkit-scrollbar {
  display: none;
}

.msg_box {
  width: 60%;
  margin-top: 5%;
  padding: 5%;
  border-radius: 10px;
}

#msg_mine {
  background: #eb9999;
  margin-left: auto;
  border-top-right-radius: 0;
}

#msg_not_mine {
  background: #272727;
  color: white;
  border-top-left-radius: 0;
}

.username {
  font-size: 12px;
}

#send-form {
  display: flex;
  background: #282828;
  border-radius: 10px;
  align-self: flex-end;
  width: 90%;
  height: 10%;
  justify-content: space-evenly;
  align-items: center;
  margin: 5%;
}

#send-form input {
  width: 70%;
  margin: auto;
  background: #3b3b3b;
  color: white;
}

#send-form img {
  margin: auto;
}

html {
  position: relative;
  min-height: 100%;
}

nav {
  height: 50px;
  width: 100%;
  z-index: 1;
  background-color: #4d4d4d !important;
  border-color: #4d4d4d !important;
  border-top-right-radius: 0 !important;
  border-top-left-radius: 0 !important;
}

.navbar-header {
  width: 100%;
}

.nav-icon {
  padding: 5px 15px 5px 15px;
  float: right;
}

nav a {
  color: #ccc !important;
}

nav i.fa {
  font-size: 40px;
  color: #ccc;
}

nav a:hover {
  color: #a9a9a9 !important;
}

nav i.fa:hover {
  color: #a9a9a9;
}


/*vertical-center {
	position: relative;
	top: 30%;
	left: 50%;
	transform: translate(-50%, -50%);
}*/

.horizontal-center {
  margin: 0 auto;
}

.form-control {
  color: #0088aa;
  font-weight: bold;
}

.form-control:focus {
  border-color: #0088aa;
  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
    0 0 8px rgba(0, 136, 170, 0.6);
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
    0 0 8px rgba(0, 136, 170, 0.6);
}

.footer {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 60px;
  background-color: #4d4d4d;
}

.footer .text-muted {
  margin: 20px 0;
  float: left;
  color: #ccc;
}

.openvidu-logo {
  height: 35px;
  float: right;
  margin: 12px 0;
  -webkit-transition: all 0.1s ease-in-out;
  -moz-transition: all 0.1s ease-in-out;
  -o-transition: all 0.1s ease-in-out;
  transition: all 0.1s ease-in-out;
}

.openvidu-logo:hover {
  -webkit-filter: grayscale(0.5);
  filter: grayscale(0.5);
}

.demo-logo {
  margin: 0;
  height: 22px;
  float: left;
  padding-right: 8px;
}

a:hover .demo-logo {
  -webkit-filter: brightness(0.7);
  filter: brightness(0.7);
}

#join-dialog {
  margin-left: auto;
  margin-right: auto;
  max-width: 70%;
}

#join-dialog h1 {
  color: #4d4d4d;
  font-weight: bold;
  text-align: center;
}

#img-div {
  text-align: center;
  margin-top: 3em;
  margin-bottom: 3em;
  /*position: relative;
	top: 20%;
	left: 50%;
	transform: translate(-50%, -50%);*/
}

#img-div img {
  height: 15%;
}

#join-dialog label {
  color: #0088aa;
}

#join-dialog input.btn {
  margin-top: 15px;
}

#session-title {
  display: inline-block;
}

#buttonLeaveSession {
  float: right;
  margin-top: 20px;
}

#video-container {
  /* border-style: solid;
  border-width: 5px; */
  display: flex;
  justify-content: space-evenly;
  align-content: center;
  align-items: center;
  align-self: center;
  width: 80vw;
  height: 75vh;
  flex-wrap: wrap;
}

#video-container .video {
  /* border-style: solid;
  border-width: 5px; */
  float: left;
  /* cursor: pointer; */
}

.under-one {
  justify-content: center !important;
}

.under-one .video {
  max-width: 80%;
  max-height: 90%;
}

.under-two .video {
  max-width: 40%;
  max-height: 90%;
}

.under-four .video {
  max-width: 40%;
  max-height: 45%;
}

.under-six .video {
  max-width: 30%;
  max-height: 45%;
}

.under-eight .video {
  max-width: 23%;
  max-height: 45%;
}

/* #video-container video+div {
  float: left;
  width: 50%;
  position: relative;
  margin-left: -50%;
} */

/* #video-container p {
  display: inline-block;
  background: #f8f8f8;
  padding-left: 5px;
  padding-right: 5px;
  color: #777777;
  font-weight: bold;
  border-bottom-right-radius: 4px;
  border-bottom-left-radius: 4px;
  margin: 0;
} */

.video {
  width: 100%;
  height: 100%;
}

#main-video p {
  position: absolute;
  display: inline-block;
  background: #f8f8f8;
  padding-left: 5px;
  padding-right: 5px;
  font-size: 22px;
  color: #777777;
  font-weight: bold;
  border-bottom-right-radius: 4px;
  border-bottom-left-radius: 4px;
}

#mute img {
  width: 100%;
  height: auto;
  display: inline-block;
  object-fit: contain;
  vertical-align: baseline;
}

/* #session #video-container img {
  position: relative;
  float: left;
  width: 50%;
  cursor: pointer;
  object-fit: cover;
  height: 180px;
} */

/* xs ans md screen resolutions*/

@media screen and (max-width: 991px) and (orientation: portrait) {
  #join-dialog {
    max-width: inherit;
  }

  #img-div img {
    height: 10%;
  }

  #img-div {
    margin-top: 2em;
    margin-bottom: 2em;
  }

  .container-fluid>.navbar-collapse,
  .container-fluid>.navbar-header,
  .container>.navbar-collapse,
  .container>.navbar-header {
    margin-right: 0;
    margin-left: 0;
  }

  .navbar-header i.fa {
    font-size: 30px;
  }

  .navbar-header a.nav-icon {
    padding: 7px 3px 7px 3px;
  }
}

@media only screen and (max-height: 767px) and (orientation: landscape) {
  #img-div {
    margin-top: 1em;
    margin-bottom: 1em;
  }

  #join-dialog {
    max-width: inherit;
  }
}


#out {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 5%;
  width: 98%;
  color: white;
  border: none;
  text-decoration: none;
  background: none;
}

#out_btn {
  cursor: pointer;
}

#out_btn:hover {
  color: #e27b66;
}

#txt {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 10%;
  color: white;

  font-family: 'dodum';
  font-size: 2rem;
}

#minz {
  display: flex;
  flex-wrap: wrap;
  height: 50vh;
  background: #FFFFF4;
  width: 80vh;
  border-radius: 20px;
}

#container_txt {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  height: 80%;
  width: 40%;

  font-family: 'dodum';
}

#container_pic {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80%;
  width: 60%
}

#container_in {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 20%;
  width: 100%;

  font-family: 'hansans';
  font-size: 2rem;
}

#option-footer {
  height: 20%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}


#mute {
  display: flex;
  justify-content: space-evenly;
}

.onoff {
  margin-left: 100px;
  cursor: pointer;
}

#enter {
  display: flex;
  justify-content: space-evenly;
}

.content {
  /* font-family: 'dokdo'; */

  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 15px 35px;

  width: 30px;
  height: 30px;

  background: #E27B66;
  /* box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5); */
  border-radius: 53px;

  cursor: pointer;

  width: 5rem;
  /* height: 3rem; */
  color: white;
  text-align: center;
  line-height: 3rem;

  border-radius: 40px;
  transition: all .1s ease-in;
}

.enter_content {
  margin-right: 100px;
}

.online_friend .content {
  font-size: 1rem;
  font-weight: 500;
  width: 30%;
  height: 2vh;
  padding: 5px 10px;
}

.game .content {
  font-size: 1rem;
  font-weight: 500;
  width: 30%;
  height: 2vh;
  padding: 5px 10px;
}
</style>