<template>
  <header>
    <RoomHeader
      :title="title"
      :category="category"
      :headCount="headCount"
      :headCountMax="headCountMax"
    />
  </header>
  <div id="main-container" class="container">

    <div id="session">
      <div id="container" style="display: flex;">
        <div id="video-container" :class="{
          'under-one': this.headCount == 1,
          'under-two': this.headCount == 2,
          'under-four': this.headCount == 3 || this.headCount == 4,
          'under-six': this.headCount == 5 || this.headCount == 6,
          'under-eight': this.headCount == 7 || this.headCount == 8,
        }">
          <user-video class="video" :stream-manager="publisher" my-video="true" />
          <user-video class="video" v-for="sub in subscribers" :key="sub.stream.connection.connectionId"
            :stream-manager="sub" my-video="false" @kickUser="kickUser" @changeHost="changeHost"/>
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
              style="width:30px; height:30px; cursor: pointer">
          </form>
        </div>
      </div>
      <div id="option-footer">
        <div id="mute">
          <div class="onoff" @click="clickMuteVideo">
            <img v-if="videoActive" src="../../../assets/video_on.png" alt="video on img" />
            <img v-else src="../../../assets/video_off.png" alt="video on img" />
          </div>
          <div class="onoff" @click="clickMuteAudio">
            <img v-if="audioActive" src="../../../assets/audio_on.png" alt="audio on img" />
            <img v-else src="../../../assets/audio_off.png" alt="audio on img" />
          </div>
        </div>
        <div id="btns">
          <div class="content" @click="infoOpen">Info</div>
          <el-popover v-if="myUserId == hostId" :width="300"
            popper-style="background: rgb(235 153 153); border: rgb(235 153 153); box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 15px;"
            trigger="click">
            <template #reference>
              <div class="content">game</div>
            </template>
            <template #default>
              <div class="game" v-for="idx in 3" :key="idx"
                style="display: flex; justify-content: space-evenly; align-items: center; margin: 10px;">
                <p class="game_name" align="right"
                  style="width: 80%; margin: 0; margin-right: 10px; font-size: 20px; color: white; align-self:center;">
                  {{ games[idx] }}
                </p>
                <div @click="startBtn(idx)" class="content_inside" style="width: 20%; text-decoration:none;">
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
              <div v-if=" friends.length == 0 ">
                초대 가능한 친구가 없어요
              </div>
              <div class="online_friend" v-for="friend in friends" :key="friend"
                style="display: flex; justify-content: space-evenly; align-items: center; margin: 10px;">
                <p class="friend_nickname" align="right"
                  style="width: 80%; margin: 0; margin-right: 10px; font-size: 20px; color: white; align-self:center;">
                  {{ friend.nickname }}
                </p>
                <div class="content_inside" style="width: 20%; text-decoration:none;">
                  Invite
                </div>
              </div>
            </template>
          </el-popover>
          <div class="content" @click="exitBtn">Exit</div>
        </div>
      </div>
    </div>
  </div>
<!-- gameInfo popup -->
  <div class="game-info-modal-bg" @click="infoClose"></div>
  <div class="game-info-modal-wrap">
    <div class="left-arrow" @click="leftClick()"></div>
    <div class="game-info-popup">
      <div class="page1">
        <div class="game-info-title"> 웃음 참기 게임 </div>
        <div class="game-info-sentence">
          내용1
        </div>
      </div>
      <div class="page2">
        <div class="game-info-title"> 나 진짜 안취했어! </div>
        <div class="game-info-sentence">
          내용2
        </div>
      </div>
      <div class="page3">
        <div class="game-info-title"> 랜덤 대화 주제 </div>
        <div class="game-info-sentence">
          내용3
        </div>
      </div>
    </div>
    <div class="game-info-btn-wrapper">
      <div class="btn" @click="infoClose">닫기</div>
    </div>
    <div class="right-arrow" @click="rightClick()"></div>
  </div>
  <div></div>
</template>

<script>
import RoomHeader from '../menu/RoomHeader.vue'
import { OpenVidu } from "openvidu-browser";
import axios from "axios";
import UserVideo from "./components/UserVideo.vue";
import { useStore } from 'vuex';

axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL = "http://localhost:5000/";
const OPENVIDU_SERVER_SECRET = 'ssafy';

export default {
  name: "RoomView",

  components: {
    UserVideo,
    RoomHeader,
  },

  data() {
    return {
      store: useStore(),

      // OpenVidu objects
      OV: undefined,
      session: undefined,
      // mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      // Join form
      mySessionId: this.$route.params.roomId,

      newMessage: null,
      messages: [],
      messageData: null,
      eventData: null,

      title: "",
      category: "",
      headCountMax: 8,
      headCount: 1,
      hostId: "",

      myUserId: "",
      myUserName: "",
      friends: [],

      // videoActive: JSON.parse(this.$route.query.video),
      // audioActive: JSON.parse(this.$route.query.audio),
      videoActive: true,
      audioActive: true,

      gameStatus: 0,
      games: ['일반', '할머니 게임', '나 안취했어', '랜덤 대화주제'],

      infoPage: 1,
    };
  },

  computed: {
    isSmile() {
      return this.store.state.gameModule.isSmile;
    },
    isFinished() {
      return this.store.state.gameModule.isFinished;
    },
  },
  watch: {
    isSmile(value){
      console.log("감지지지지지ㅣㅈ" + value);
      if(value){
        this.session.signal({
            data: JSON.stringify(this.myUserName),
            type: 'smile'
          })
            .then(() => {
              console.log('웃탐!!');
            })
            .catch(error => {
              console.error(error);
            })
      }
    },
    isFinished(value){
      console.log("감감감감감감감감지" + value);
      if(value){
        this.session.signal({
          //말할 문장, 말한 문장 담아서 보내기
          data: JSON.stringify({sentence : this.store.state.gameModule.sentence, speech : this.store.state.gameModule.texts}),
          type: 'detect-audio'
        })
          .then(() => {
            console.log('끝!!!!');
            this.store.dispatch("gameModule/getSentence");
          })
          .catch(error => {
            console.error(error);
          })
      }
    },
  },
  methods: {

    sendMessage() {
      if (this.newMessage) {
        this.messageData = {
          content: this.newMessage,
          username: this.myUserName
        }
        this.session.signal({
          data: JSON.stringify(this.messageData),
          type: 'my-chat'
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
      console.log(this.videoActive);
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

      this.session.on('signal:detect-smile', (event) => {
        this.store.dispatch("gameModule/startSmileGame");
      })

      this.session.on('signal:smile', (event) => {
        this.store.dispatch("gameModule/stopDetect");
        console.log(event.data + "님이 웃으셨습니다!!");
      })

      this.session.on('signal:not-drunk', (event) => {
        this.eventData = JSON.parse(event.data);
        console.log(this.eventData.username + "님 말 할 준비 하세용");
        console.log(this.store.state.gameModule.sentence);
        if(this.eventData.username == this.myUserName){
          this.store.dispatch("gameModule/getSpeech");
        }
      })

      this.session.on('signal:detect-audio', (event) => {
        this.eventData = JSON.parse(event.data);
        console.log(this.eventData.sentence + "이 문장을 발음한 결과");
        console.log(this.eventData.speech + "이것입니다.");
      })

      this.session.on('signal:random-topic', (event) => {
        this.eventData = JSON.parse(event.data);
        console.log(this.eventData.topic);
      })

      this.session.on('signal:kick', (event) => {
        this.eventData = JSON.parse(event.data);
        console.log(this.eventData.username);
        if(this.eventData.username == this.myUserName){
          alert("강퇴당함");
          this.leaveSession();
        }
      })
      
      this.session.on('signal:change-host', (event) => {
        this.eventData = JSON.parse(event.data);
        this.hostId = this.eventData.userId;
        this.store.dispatch("roomModule/changeHost",{
          roomId: this.mySessionId,
          hostId: this.hostId,
        });
      })

      // --- 4) Connect to the session with a valid user token ---

      // Get a token from the OpenVidu deployment
      this.getToken(this.mySessionId).then((token) => {
        // First param is the token. Second param can be retrieved by every user on event
        // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
        this.session.connect(token, { userId: this.myUserId, username: this.myUserName, hostId: this.hostId, friends: this.friends })
          .then(() => {

            // --- 5) Get your own camera stream with the desired properties ---

            // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
            // element: we will manage it on our own) and with the desired properties
            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: this.audioActive, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: this.videoActive, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            // Set the main video in the page to display our webcam and store our Publisher
            // this.mainStreamManager = publisher;
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
      // this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      // Remove beforeunload listener
      window.removeEventListener("beforeunload", this.leaveSession);
      
      this.$router.push({ name: 'rooms' });
      this.store.dispatch("roomModule/quitRoom", this.mySessionId)
      .then((result) => {
        if (result) {
          this.store.dispatch("roomModule/removeUserInRoom",this.store.state.userModule.user.userId)
        }
      })
    },

    // updateMainVideoStreamManager(stream) {
    //   if (this.mainStreamManager === stream) return;
    //   this.mainStreamManager = stream;
    // },

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
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId });
      return response.data; // The sessionId
    },

    async createToken(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections');
      return response.data; // The token
    },

    async getFriends() {
      const friends = await this.store.state.friendModule.friends;
      const parseFriends = JSON.parse(JSON.stringify(friends));
      this.friends = parseFriends.filter(friend => friend.state === "online");
    },

    async getRoom() {
      const roomData = await this.store.dispatch("roomModule/getRoom", this.$route.params.roomId);
      const room = JSON.parse(JSON.stringify(roomData));
      this.title = room.title;
      this.category = room.category;
      // this.headCount = room.personCount;
      this.headCount = 1;
      this.headCountMax = room.personLimit;
      this.hostId = room.hostId;
    },

    getUser() {
      const user = this.store.state.userModule.user;
      this.myUserId = user.userId;
      this.myUserName = user.nickname;
    },

    exitBtn() {
      if(this.hostId == this.myUserId && this.headCount > 1){
        this.changeHost();
      }
      this.leaveSession();
    },

    //게임 기능
    startBtn(idx) {
      this.gameStatus = idx;
      switch(this.gameStatus){
        case 1:
          //게임화면 켜지고 게임 룰 설명하고
          //웃음탐지 시그널 보내고
          this.session.signal({
            type: 'detect-smile'
          })
            .then(() => {
              console.log('웃탐시작');
              // 각 게임에 맞는 화면 시작
            })
            .catch(error => {
              console.error(error);
            })
          break;
        case 2:
          this.session.signal({
            //체크할 닉네임 보내기
            data: JSON.stringify({username : this.myUserName}),
            type: 'not-drunk'
          })
            .then(() => {
              console.log('나안취했어 시작');
            })
            .catch(error => {
              console.error(error);
            })
          break;
        case 3:
          this.session.signal({
            data: JSON.stringify({topic : this.store.state.gameModule.topic}),
            type: 'random-topic'
          })
            .then(() => {
              console.log('랜덤주제');
              this.store.dispatch("gameModule/getTopic");
            })
            .catch(error => {
              console.error(error);
            })

          break;

      }
    },
    
    infoOpen() {
      document.getElementsByClassName("game-info-modal-wrap")[0].style.display = "block";
      document.getElementsByClassName("game-info-modal-bg")[0].style.display = "block";
    },

    infoClose() {
      document.getElementsByClassName("game-info-modal-wrap")[0].style.display = "none";
      document.getElementsByClassName("game-info-modal-bg")[0].style.display = "none";
      this.infoPage = 1;
      document.getElementsByClassName("game-info-popup")[0].style.transform = "translateX(0%)";
    },

    leftClick() {
      const number1 = 1/3 * 100
      const number2 = 2/3 * 100
      if (this.infoPage == 1) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number2}%)`;
        this.infoPage = 3
      }
      else if (this.infoPage == 2) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = "translateX(0%)";
        this.infoPage = 1
      }
      else{
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number1}%)`;
        this.infoPage = 2
      }
    },

    rightClick() {
      const number1 = 1/3 * 100
      const number2 = 2/3 * 100
      if (this.infoPage == 1) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number1}%)`;
        this.infoPage = 2
      }
      else if (this.infoPage == 2) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number2}%)`;
        this.infoPage = 3
      }
      else{
        document.getElementsByClassName("game-info-popup")[0].style.transform = "translateX(0%)";
        this.infoPage = 1
      }
    },

    kickUser(username){
      this.session.signal({
        data: JSON.stringify({username : username}),
        type: 'kick'
      })
        .then(() => {
          console.log('강퇴!');
        })
        .catch(error => {
          console.error(error);
        })
    },

    changeHost(userId){
      this.session.signal({
        data: JSON.stringify({userId : userId}),
        type: 'change-host'
      })
        .then(() => {
          console.log('방장변경!');
        })
        .catch(error => {
          console.error(error);
        })
    }
  },

  created() {
    this.joinSession();
    this.getFriends();
    this.getRoom();
    this.getUser();
    console.log(this.$route.query.video);
    this.store.dispatch("gameModule/getSentence");
    this.store.dispatch("gameModule/getTopic");
  },

  mounted() {
  },

  updated() {

  }
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
  justify-content: space-between;
}

#mute {
  display: flex;
  justify-content: space-evenly;
}

.onoff {
  margin-left: 100px;
  cursor: pointer;
}

#btns {
  display: flex;
  justify-content: space-evenly;
}

.content {
  font-family: 'dokdo';

  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 15px 35px;
  margin-inline: 40px;

  width: 30px;
  height: 30px;

  color: white;
  font-size: 2rem;
  font-weight: 500;
  background: black;
  box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5);
  border-radius: 53px;
  border: white;

  cursor: pointer;
}

.content_inside {
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
/* 게임 시작시 도움말창 */
.game-info-modal-bg {
	display:none;
	width:100%;
	height:100%;
	position:fixed;
	top:0;
	left:0;
	right:0;
	z-index:998;
	transition: 0.5s ease-out;
}
.game-info-modal-wrap {
	display:none;
	position:absolute;
	top:50%;
	left:50%;
	transform:translate(-50%,-50%);
	width:50%;
	height:50%;
	background: #2E303F;
  border: 2px solid #CBCBCB;
  border-radius: 30px;
	z-index:999;
  overflow:hidden;
}
.game-info-popup {
  display: flex;
  flex-wrap: nowrap;
  height: 100%;
  width: 300%;
  transition: all .3s ease-in;
}
.game-info-popup > div[class*="page"]{
  height: 100%;
  width: 100%;
  display: grid;
  grid-template-rows: 2fr 5fr 3fr;
  justify-content: center;
  align-items: center;
  padding: 0 5%;
}
.game-info-title{
  font-size: 2rem;
  color: white;
}
.game-info-sentence{
  text-align: center;
  color: white;
}
.game-info-btn-wrapper{
  width: auto;
  height: auto;
  position: absolute;
  bottom: 10%;
  left: 50%;
  transform: translateX(-50%);
  color:white;
  text-align: center;
  z-index: 1001;
}
.btn{
  height: 2.5rem;
  width: 4rem;
  line-height: 2.5rem;
  background: #E27B66;
  border-radius: 20px;
  transition: all .1s ease-in;
}
.btn:hover{
  transform: scale(1.2, 1.2);
  cursor: pointer;
}
.left-arrow, .right-arrow {
  width: 5vw;
  height: 5vw;
  top: 42%;
  position: absolute;
  vertical-align: middle;
  transition: all .05s ease-in;
  z-index: 1001;
}
.left-arrow{
  left: 5%;
  background: url('../../../assets/left_arrow.png') center center /100% no-repeat;
}
.right-arrow{
  right: 5%;
  background: url('../../../assets/right_arrow.png') center center /100% no-repeat;
}
.left-arrow:hover, .right-arrow:hover {
  transform: scale(1.2, 1.2);
  cursor: pointer;
}
</style>