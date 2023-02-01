<template>
  <header>
    <RoomHeader />
  </header>
  <div id="main-container" class="container">
    <!-- <div id="join" v-if="!session">
      <div id="join-dialog" class="jumbotron vertical-center">
        <h1>Join a video session</h1>
        <div class="form-group">
          <p>
            <label>Participant</label>
            <input v-model="myUserName" class="form-control" type="text" required />
          </p>
          <p>
            <label>Session</label>
            <input v-model="mySessionId" class="form-control" type="text" required />
          </p>
          <p class="text-center">
            <button class="btn btn-lg btn-success" @click="joinSession()">
              Join!
            </button>
          </p>
        </div>
      </div>
    </div> -->

    <!-- <div id="session" v-if="session"> -->
    <div id="session">
      <!-- <div id="main-video" class="col-md-6">
        <user-video :stream-manager="mainStreamManager" />
      </div> -->
      <div id="container" style="display: flex;">
        <div id="video-container" :class="{
          'under-one': this.headCount == 1,
          'under-two': this.headCount == 2,
          'under-four': this.headCount == 3 || this.headCount == 4,
          'under-six': this.headCount == 5 || this.headCount == 6,
          'under-eight': this.headCount == 7 || this.headCount == 8,
        }">
          <user-video class="video" :stream-manager="publisher"
            @click.native="updateMainVideoStreamManager(publisher)" />
          <user-video class="video" v-for="sub in subscribers" :key="sub.stream.connection.connectionId"
            :stream-manager="sub" @click.native="updateMainVideoStreamManager(sub)" />
        </div>
        <div id="chatting-container" class="col-md-4">
          <div id="chats" ref="message_scroll">
            <div v-for="message in messages" :key="message.id">
              <!-- 내 채팅 -->
              <div id="msg_mine" class="msg_box" v-if="message.username == myUserName">
                <div class="username">
                  {{ message.username }}
                </div>
                <div class="msg">
                  {{ message.text }}
                </div>
              </div>
              <!-- 남의 채팅 -->
              <div id="msg_not_mine" class="msg_box" v-else>
                <div class="username">
                  {{ message.username }}
                </div>
                <div class="msg">
                  {{ message.text }}
                </div>
              </div>
            </div>
          </div>
          <form id="send-form" @submit.prevent="sendMessage">
            <input v-model="newMessage" placeholder="Type your message here" />
            <img src="../../../assets/message.png" alt="message img" @click="sendMessage"
              style="width:30px; height:30px;">
          </form>
        </div>
      </div>
      <div id="option-footer">
        <!-- <h1 id="session-title">{{ mySessionId }}</h1> -->
        <!-- <input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession"
          value="Leave session" /> -->
        <div class="content" @click="clickMuteVideo">video.M</div>
        <div class="content" @click="clickMuteAudio">audio.M</div>
        <el-popover :width="300"
          popper-style="background: rgb(235 153 153); border: rgb(235 153 153); box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 15px;"
          trigger="click">
          <template #reference>
            <div class="content">game</div>
          </template>
          <template #default>
            <div class="game" v-for="game in games"
              style="display: flex; justify-content: space-evenly; align-items: center; margin: 10px;">
              <p class="game_name" align="right"
                style="width: 80%; margin: 0; margin-right: 10px; font-size: 20px; color: white; align-self:center;">
                {{ game }}
              </p>
              <div class="content" style="width: 20%; text-decoration:none;">
                start
              </div>
            </div>
          </template>
        </el-popover>
        <el-popover :width="300"
          popper-style="background: rgb(235 153 153); border: rgb(235 153 153); box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 15px;"
          trigger="click">
          <template #reference>
            <div class="content">Invite</div>
          </template>
          <template #default>
            <div class="online_friend" v-for="friend in friends"
              style="display: flex; justify-content: space-evenly; align-items: center; margin: 10px;">
              <p class="friend_nickname" align="right"
                style="width: 80%; margin: 0; margin-right: 10px; font-size: 20px; color: white; align-self:center;">
                {{ friend }}
              </p>
              <div class="content" style="width: 20%; text-decoration:none;">
                Invite
              </div>
            </div>
          </template>
        </el-popover>
        <div class="content" @click="leaveSession">Exit</div>
      </div>
    </div>
  </div>
</template>

<script>
import { RouterLink, RouterView } from 'vue-router'
import RoomHeader from '../menu/RoomHeader.vue'
import { OpenVidu } from "openvidu-browser";
import axios from "axios";
import UserVideo from "./components/UserVideo.vue";

axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL = "http://localhost:5000/";

export default {
  name: "RoomView",

  components: {
    UserVideo,
    RoomHeader,
  },

  data() {
    return {
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      // Join form
      mySessionId: this.$route.params.roomId,
      myUserName: "nickname" + Math.floor(Math.random() * 100),

      newMessage: null,
      messages: [],
      messageData: null,
      eventData: null,

      headCountMax: 8,
      headCount: 1,

      games: ['할머니 게임', '나 안취했어', '랜덤 대화주제'],
      friends: ['친구1', '친구2', '친구3', '이름이긴친구우우'],
    };
  },

  methods: {

    sendMessage() {
      if (this.newMessage) {
        this.messageData = {
          content: this.newMessage,
          username: this.myUserName
        }
        this.session.signal({
          data: JSON.stringify(this.messageData),  // Any string (optional)
          to: [],                     // Array of Connection objects (optional. Broadcast to everyone if empty)
          type: 'my-chat'             // The type of message (optional)
        })
          .then(() => {
            console.log('Message successfully sent');
          })
          .catch(error => {
            console.error(error);
          })
        this.messageData = null
        this.newMessage = null
      }
    },

    clickMuteVideo() {
      if (this.publisher.stream.videoActive) {
        this.publisher.publishVideo(false)
      } else {
        this.publisher.publishVideo(true)
      }
    },

    clickMuteAudio() {
      if (this.publisher.stream.audioActive) {
        this.publisher.publishAudio(false)
      } else {
        this.publisher.publishAudio(true)
      }
    },

    joinSession() {
      // --- 1) Get an OpenVidu object ---
      this.OV = new OpenVidu();

      // --- 2) Init a session ---
      this.session = this.OV.initSession();

      // --- 3) Specify the actions when events take place in the session ---

      // On every new Stream received...
      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
        this.headCount++;
      });

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
        this.headCount--;
      });

      // On every asynchronous exception...
      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // --- 4) Connect to the session with a valid user token ---

      // Get a token from the OpenVidu deployment
      this.getToken(this.mySessionId).then((token) => {

        // Receiver of the message (usually before calling 'session.connect')

        this.session.on('signal:my-chat', (event) => {
          console.log(event.data); // Message
          console.log(event.from); // Connection object of the sender
          console.log(event.type); // The type of message ("my-chat")
          this.eventData = JSON.parse(event.data)
          this.messages.push({
            id: Date.now(),
            username: this.eventData.username,
            text: this.eventData.content,
          });
          this.$nextTick(() => {
            let msgscr = this.$refs.message_scroll;
            msgscr.scrollTo({ top: msgscr.scrollHeight, behavior: 'smooth' });
          });
        });

        // First param is the token. Second param can be retrieved by every user on event
        // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
        this.session.connect(token, { clientData: this.myUserName })
          .then(() => {

            // --- 5) Get your own camera stream with the desired properties ---

            // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
            // element: we will manage it on our own) and with the desired properties
            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            // Set the main video in the page to display our webcam and store our Publisher
            this.mainStreamManager = publisher;
            this.publisher = publisher;

            // --- 6) Publish your stream ---

            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.log("There was an error connecting to the session:", error.code, error.message);
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      // Empty all properties...
      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      // Remove beforeunload listener
      window.removeEventListener("beforeunload", this.leaveSession);

      this.$router.push({ name: 'rooms' });
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },

    /**
     * --------------------------------------------
     * GETTING A TOKEN FROM YOUR APPLICATION SERVER
     * --------------------------------------------
     * The methods below request the creation of a Session and a Token to
     * your application server. This keeps your OpenVidu deployment secure.
     * 
     * In this sample code, there is no user control at all. Anybody could
     * access your application server endpoints! In a real production
     * environment, your application server must identify the user to allow
     * access to the endpoints.
     * 
     * Visit https://docs.openvidu.io/en/stable/application-server to learn
     * more about the integration of OpenVidu in your application server.
     */
    async getToken(mySessionId) {
      const sessionId = await this.createSession(mySessionId);
      return await this.createToken(sessionId);
    },

    async createSession(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
        headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The sessionId
    },

    async createToken(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
        headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The token
    },
  },

  created() {
    this.joinSession()
  },
};

</script>

<style scoped>
#main-container {
  background: #121212;
  height: 90vh;
}

#container {
  height: 80vh;
}

#container>div {
  margin: 20px;
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

#session img {
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

#option-footer {
  display: flex;
  justify-content: space-evenly;
}

.content {
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