<template>
  <header>
    <RoomHeader :title="title" :category="category" :headCount="headCount" :headCountMax="headCountMax" @clickDrop="clickDrop"/>
    <Drop :isDrop="this.isDrop" :dropIdx="this.dropIdx"/>
    <!-- <Drop :isDrop="this.isDrop" :dropIdx="this.dropIdx"/> -->
  </header>
  <div id="main-container" class="container">
    <div id="session">
      <div id="container" style="display: flex;">
        <div id="video-btns">

          <div id="video-container" :class="{
            'under-one': this.headCount == 1,
            'under-two': this.headCount == 2,
            'under-four': this.headCount == 3 || this.headCount == 4,
            'under-six': this.headCount == 5 || this.headCount == 6,
            'under-eight': this.headCount == 7 || this.headCount == 8,
          }">
            <user-video class="video" :streamManager="publisher" :myVideo="true" :isHostView="hostId == myUserId"
              :hostId="hostId" :highLightUserName="highLightUserName" @checkDrunk="checkDrunk" />
            <user-video class="video" v-for="sub in subscribers" :key="sub.stream.connection.connectionId"
              :streamManager="sub" :myVideo="false" :isHostView="hostId == myUserId" :hostId="hostId" :friends="friends"
              :highLightUserName="highLightUserName" @kickUser="kickUser" @changeHost="changeHost"
              @friendRequest="friendRequest" @checkDrunk="checkDrunk" />
          </div>

          <div id="btns-container">
            <div id="mute">
              <div class="onoff" @click="clickMuteVideo">
                <img v-if="videoActive" src="@/assets/images/video_on.png" alt="video on img" />
                <img v-else src="@/assets/images/video_off.png" alt="video on img" />
              </div>
              <div class="onoff" @click="clickMuteAudio">
                <img v-if="audioActive" src="@/assets/images/audio_on.png" alt="audio on img" />
                <img v-else src="@/assets/images/audio_off.png" alt="audio on img" />
              </div>
              <div class="onoff" @click="infoOpen">
                <img class="option-footer-btn" src="@/assets/images/info.png" alt="게임정보">
              </div>
            </div>
            <div id="btns">
              <img v-if="hostId == myUserId" class="option-footer-btn" src="@/assets/images/laughter.png" alt="웃음참기"
                @click="startBtn(1)">
              <img v-if="hostId == myUserId" class="option-footer-btn" src="@/assets/images/random_topic.png" alt="랜덤주제"
                @click="startBtn(3)">
            </div>
          </div>

        </div>
        <div id="chat-btns">

          <div class="game-chatting-container">
            <Transition>
              <div class="game-container" v-if="this.gameStart">
                <div class="stop-btn" v-if="hostId == myUserId">
                  <div class="cancel" type="button" @click="closeSignal">X</div>
                </div>
                <div class="game-wrapper">
                  <div></div>
                  <div class="game-title">{{ this.gameTitle }}</div>
                  <div class="game-content">{{ this.gameContent }}</div>
                  <div></div>
                  <div></div>
                </div>
              </div>
            </Transition>
            <div id="chatting-container">
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
                <img src="@/assets/images/message.png" alt="message img" @click="sendMessage"
                  style="width:30px; height:30px; cursor: pointer">
              </form>
            </div>
          </div>

          <div id="option-footer">
            <el-popover :width="300" popper-style="background: #E27B66; border: none; padding: 15px;" trigger="click">
              <template #reference>
                <div @click="refreshInviteBtn()" class="footer-btn">초대하기</div>
              </template>
              <template #default>
                <div v-if="friends.length == 0">
                  초대 가능한 친구가 없어요
                </div>
                <div class="online_friend" v-for="friend in friends" :key="friend"
                  style="display: flex; justify-content: space-evenly; align-items: center; margin: 10px;">
                  <p class="friend_nickname" align="left"
                    style="width: 80%; margin: 0; margin-right: 10px; font-size: 20px; color: white; align-self:center;">
                    {{ friend.nickname }}
                  </p>
                  <div class="invite-btn" @click="inviteFriend(friend.userId)" style="">
                    초대하기
                  </div>
                </div>
              </template>
            </el-popover>
            <div class="footer-btn" @click="exitBtn">나가기</div>
          </div>

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
        <div class="game-info-title"> 방 내부 안내 </div>
        <div class="game-info-sentence1">
          <ul>
            <li><img src="@/assets/images/video_on.png" alt="video on img" /> : 카메라 on/off</li>
            <li><img src="@/assets/images/audio_on.png" alt="audio on img" /> : 오디오 on/off</li>
            <li><img class="info_img" src="@/assets/images/info.png" alt="info on img" /> : 방 내부와 게임 설명</li>
            <li>
              <div class="footer-btn" style="display:inline; padding: 5px 0px; margin-right:0px;">초대하기</div> : 온라인 상태인
              친구를 초대할 수 있어요
            </li>
            <li>
              <div class="footer-btn" style="display:inline; padding: 5px 0px; margin-right:0px;">나가기</div> : 방 나가기
            </li>
          </ul>
        </div>
      </div>
      <div class="page2">
        <div class="game-info-title"> 웃음 참기 게임 </div>
        <div class="game-info-sentence">
          <br>
          <p><img class="info_img" src="@/assets/images/laughter.png" alt="game on img" /> : 게임 시작 하기 (방장에게만 보여요)</p>
          <br>
          <p>① 이가 보이지 않게 입술을 안으로
            말아 넣어서 할머니 입을 만든다</p><br />
          <p>② 랜덤으로 주제가 선정된다</p><br />
          <p>③ 한 명씩 돌아가면서 할머니 입 상태로 주제에 맞는 단어를 외친다</p><br />
          <p>④ 웃음이 감지되면 탈락!!</p><br />

        </div>
      </div>
      <div class="page3">
        <div class="game-info-title"> 나 진짜 안취했어! </div>
        <div class="game-info-sentence">
          <br>
          <p>게임시작 : 방장이 다른 유저의 화면을 클릭하여 진행할 수 있어요</p><br>
          <p>술 취했나 안 취했나 확인해보자!!</p><br />
          <p>① 참여자가 술을 너무 많이 마신 것 같으면 방장이 지목하여 발음 테스트를 시작한다</p><br />
          <p>② 지목 받은 사람은 랜덤으로 주어지는 발음 테스트 문장을 읽는다 (ex. 강 공장장이고, 된장 공장 공장장은 공 공장장이다)</p><br />
          <p>③ 정확도를 통해 혀가 꼬였는지 확인 가능!</p><br />
        </div>
      </div>
      <div class="page4">
        <div class="game-info-title"> 랜덤 대화 주제 </div>
        <div class="game-info-sentence">
          <br>
          <p><img class="info_img" src="@/assets/images/random_topic.png" alt="game on img" /> : 게임 시작 하기 (방장에게만 보여요)
          </p><br>
          <p>처음 만난 사람들과 어색하다면?? </p><br />
          <p>랜덤 대화 주제를 통해 친해져보자!</p><br />
          <p>start 버튼을 클릭하면 랜덤으로 선택된 주제가 화면에 제시된다 (ex. 좋아하는 영화는 무엇인가요?)</p><br />
          <p>제시된 주제에 대한 이야기를 나누며 자연스럽게 무르익는 술자리~~</p><br />
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
import RoomHeader from '../menu/RoomHeader.vue';
import UserVideo from "./components/UserVideo.vue";
import Drop from "./components/Drop.vue";
import { OpenVidu } from "openvidu-browser";
import { useStore } from 'vuex';
import axios from "axios";

const APPLICATION_SERVER_URL = 'http://localhost:5000';
const OPENVIDU_SERVER_URL = 'https://i8a606.p.ssafy.io:8443';
const OPENVIDU_SERVER_SECRET = 'ssafy';

export default {
  name: "RoomView",

  components: {
    UserVideo,
    RoomHeader,
    Drop,
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

      newMessage: "",
      messages: [],
      messageData: "",
      eventData: {},

      title: "",
      category: "",
      headCountMax: 8,
      headCount: 1,
      hostId: "",
      highLightUserName: "",

      myUserId: "",
      myUserName: "",
      friends: [],
      validFriends: [],

      videoActive: JSON.parse(this.$route.query.video),
      audioActive: JSON.parse(this.$route.query.audio),
      // videoActive: true,
      // audioActive: true,

      gameStatus: 0,
      games: ['일반', '할머니 게임', '나 안취했어', '랜덤 대화주제'],  // 이거 건들지 마세여

      // 도움말 팝업창 페이지
      infoPage: 1,

      // 게임화면 띄우기
      gameStart: false,
      gameTitle: "",
      gameContent: "",

      // 배경효과
      isDrop: false,
      dropIdx: -1,
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
    isSmile(value) {
      if (value) {
        this.session.signal({
          data: JSON.stringify({
            username: this.myUserName
          }),
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
    isFinished(value) {
      if (value) {
        this.session.signal({
          //말할 문장, 말한 문장 담아서 보내기
          data: JSON.stringify({
            content: this.gameContent,
            strPerson: this.store.state.gameModule.texts
          }),
          type: 'detect-audio'
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
      if (this.publisher.stream.videoActive) {
        console.log('on')
        this.publisher.publishVideo(false)
        this.videoActive = false
      } else {
        console.log('off')
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
        this.headCount = this.subscribers.length + 1;

        // 게임 중 유저 입장 시 처리 signal
        if (this.hostId == this.myUserId && this.gameStart) {
          this.session.signal({
            data: JSON.stringify({
              gameTitle: this.gameTitle,
              gameContent: this.gameContent,
              gameStatus: this.gameStatus
            }),
            type: 'enter-user-in-game'
          })
        }
      });

      this.session.on("signal:enter-user-in-game", async (event) => {
        if (!this.gameStart) {
          this.eventData = await JSON.parse(event.data);
          this.gameStatus = this.eventData.gameStatus;
          this.gameScreenOpen(this.eventData.gameStatus);
          this.gameTitle = this.eventData.gameTitle;
          this.gameContent = this.eventData.gameContent;
          if (this.gameStatus == 1) {
            this.store.dispatch("gameModule/startSmileGame");
          }
        }
      })

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
        this.headCount = this.subscribers.length + 1;
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

      this.session.on('signal:random-keyword', (event) => {
        this.gameStatus = 1
        this.gameScreenOpen(1);
        this.eventData = JSON.parse(event.data);
        this.gameContent = this.eventData.keyword;
      })

      this.session.on('signal:detect-smile', (event) => {
        // 웃참 인식 시작
        this.store.dispatch("gameModule/startSmileGame");
      })

      this.session.on('signal:smile', (event) => {
        this.store.dispatch("gameModule/stopDetect");
        this.eventData = JSON.parse(event.data);
        this.highLightUserName = this.eventData.username;
        this.gameContent = `${this.eventData.username}님이 웃으셨습니다 !`;
        setTimeout(() => { this.gameScreenClose() }, 5000);
      })

      this.session.on('signal:check-drunk', async (event) => {
        this.gameStatus = 2
        this.gameScreenOpen(2)
        this.eventData = JSON.parse(event.data);
        this.gameContent = `${this.eventData.username}님 말 할 준비!`;
        this.highLightUserName = this.eventData.username;
        setTimeout(() => {
          this.gameContent = this.eventData.sentence
          if (this.eventData.username == this.myUserName) {
            this.store.dispatch("gameModule/getSpeech");
          }
        }, 3000)
      })

      this.session.on('signal:detect-audio', async (event) => {
        this.eventData = JSON.parse(event.data);
        this.gameContent = `기준 문장 : ${this.eventData.content}

        발음 문장 : ${this.eventData.strPerson}`;
        setTimeout(() => {
          this.store.dispatch("gameModule/getAccuracy", this.eventData).then((response) => {
            this.gameContent = `정확도 : ${response}`;
          });
        }, 6000)
        setTimeout(() => { this.gameScreenClose() }, 9000);
      })

      this.session.on('signal:random-topic', (event) => {
        this.gameStatus = 3
        this.gameScreenOpen(3)
        this.eventData = JSON.parse(event.data);
        this.gameContent = this.eventData.topic;
      })

      this.session.on('signal:kick', (event) => {
        this.eventData = JSON.parse(event.data);
        if (this.eventData.userId == this.myUserId) {
          this.exitBtn();
          this.store.commit("errorModule/SET_STATUS", 405);
        }
      })

      this.session.on('signal:change-host', (event) => {
        this.eventData = JSON.parse(event.data);
        this.hostId = this.eventData.userId;
        this.store.dispatch("roomModule/changeHost", {
          roomId: this.mySessionId,
          hostId: this.hostId,
        });
      })

      this.session.on('signal:close', (event) => {
        this.gameScreenClose();
      })

      // --- 4) Connect to the session with a valid user token ---

      // Get a token from the OpenVidu deployment
      this.getToken(this.mySessionId).then((token) => {
        // First param is the token. Second param can be retrieved by every user on event
        // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
        this.session.connect(token, { userId: this.myUserId, username: this.myUserName })
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
              mirror: true, // Whether to mirror your local video or not
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
    },

    leaveSession() {
      if (this.hostId == this.myUserId && this.subscribers.length > 0) {
        let data = JSON.parse(this.subscribers[0].stream.connection.data);
        this.changeHost(data.userId);
      }

      this.store.dispatch("roomModule/removeUserInRoom", this.store.state.userModule.user.userId)
        .then((result) => {
          if (result) {
            this.store.dispatch("roomModule/quitRoom", this.mySessionId)
          }
        })

      // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      // Empty all properties...
      this.session = undefined;
      // this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

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

    // async createSession(sessionId) {
    //   const response = await axios.post(APPLICATION_SERVER_URL + '/api/sessions', { customSessionId: sessionId });
    //   return response.data; // The sessionId
    // },

    // async createToken(sessionId) {
    //   const response = await axios.post(APPLICATION_SERVER_URL + '/api/sessions/' + sessionId + '/connections');
    //   return response.data; // The token
    // },

    // 초대 가능 친구 갱신
    async refreshInviteBtn() {
      await this.store.dispatch("roomModule/inviteValidFriend", this.myUserId);
      const friends = this.store.state.roomModule.inviteValidFriends;
      const parseFriends = JSON.parse(JSON.stringify(friends));
      this.validFriends = parseFriends;
    },

    async getFriends() {
      await this.store.dispatch("friendModule/getFriends", this.myUserId);
      const friends = this.store.state.friendModule.friends;
      const parseFriends = JSON.parse(JSON.stringify(friends));
      this.friends = parseFriends;
    },

    async getRoom() {
      const roomData = await this.store.dispatch("roomModule/getRoom", this.$route.params.roomId);
      const room = JSON.parse(JSON.stringify(roomData));
      this.title = room.title;
      this.category = room.category;
      this.headCount = room.personCount;
      this.headCountMax = room.personLimit;
      this.hostId = room.hostId;
    },

    getUser() {
      const user = this.store.state.userModule.user;
      this.myUserId = user.userId;
      this.myUserName = user.nickname;
    },

    //게임 기능
    startBtn(idx) {
      this.gameStatus = idx;
      switch (this.gameStatus) {
        case 1:
          console.log('웃음참기 버튼 클릭');
          this.store.dispatch("gameModule/getKeyword")
            .then(async () => {
              this.gameContent = await this.store.state.gameModule.keyword;
              this.session.signal({
                type: 'detect-smile'
              })
                .then(() => {
                  this.session.signal({
                    data: JSON.stringify({ keyword: this.store.state.gameModule.keyword }),
                    type: 'random-keyword'
                  })
                })
            })
          break;
        // case 2:
        //   this.session.signal({
        //     //체크할 닉네임 보내기
        //     data: JSON.stringify({ username: this.myUserName }),
        //     type: 'not-drunk'
        //   })
        //     .then(() => {
        //       console.log('나안취했어 시작');
        //     })
        //     .catch(error => {
        //       console.error(error);
        //     })
        //   break;
        case 3:
          console.log('랜덤주제 버튼 클릭');
          this.store.dispatch("gameModule/getTopic")
            .then(async () => {
              this.gameContent = await this.store.state.gameModule.topic
              this.session.signal({
                data: JSON.stringify({
                  topic: this.store.state.gameModule.topic
                }),
                type: 'random-topic'
              })
            })
          break;
      }
    },

    exitBtn() {
      this.$router.push({ name: 'rooms' });
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
      const number1 = 1 / 4 * 100
      const number2 = 2 / 4 * 100
      const number3 = 3 / 4 * 100
      if (this.infoPage == 1) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number3}%)`;
        this.infoPage = 4
      }
      else if (this.infoPage == 3) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number1}%)`;
        this.infoPage = 2
      }
      else if (this.infoPage == 2) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = "translateX(0%)";
        this.infoPage = 1
      }
      else {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number2}%)`;
        this.infoPage = 3
      }
    },

    rightClick() {
      const number1 = 1 / 4 * 100
      const number2 = 2 / 4 * 100
      const number3 = 3 / 4 * 100
      if (this.infoPage == 1) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number1}%)`;
        this.infoPage = 2
      }
      else if (this.infoPage == 2) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number2}%)`;
        this.infoPage = 3
      }
      else if (this.infoPage == 3) {
        document.getElementsByClassName("game-info-popup")[0].style.transform = `translateX(${-number3}%)`;
        this.infoPage = 4
      }
      else {
        document.getElementsByClassName("game-info-popup")[0].style.transform = "translateX(0%)";
        this.infoPage = 1
      }
    },

    checkDrunk(username) {
      if (this.hostId == this.myUserId) {
        console.log("나안취했어 버튼 클릭")
        this.store.dispatch("gameModule/getSentence").then(() => {
          this.session.signal({
            data: JSON.stringify({
              username: username,
              sentence: this.store.state.gameModule.sentence
            }),
            type: 'check-drunk'
          })
        })
      }
    },

    kickUser(userId) {
      this.session.signal({
        data: JSON.stringify({ userId: userId }),
        type: 'kick'
      })
        .then(() => {
          console.log('강퇴!');
        })
        .catch(error => {
          console.error(error);
        })
    },

    changeHost(userId) {
      this.session.signal({
        data: JSON.stringify({ userId: userId }),
        type: 'change-host'
      })
        .then(() => {
          console.log('방장변경!');
        })
        .catch(error => {
          console.error(error);
        })
    },

    gameScreenOpen(idx) {
      if (!this.gameStart) {
        this.gameStart = true;
        document.getElementById("chatting-container").id = "chatting-container-small";
      }
      this.gameStatus = idx;
      this.gameTitle = this.games[idx];
    },

    gameScreenErase() {
      this.gameTitle = '';
      this.gameContent = '';
      this.gameStatus = 0;
      this.highLightUserName = '';
    },

    async gameScreenClose() {
      await this.gameScreenErase();
      if (this.gameStart) {
        this.gameStart = false;
        document.getElementById("chatting-container-small").id = "chatting-container";
      }
    },

    closeSignal() {
      this.session.signal({
        type: 'close'
      })
        .then(() => {
          console.log('창닫기');
        })
        .catch(error => {
          console.error(error);
        })
    },

    friendRequest(userId) {
      this.store.dispatch("friendModule/sendRequest", {
        userAId: this.myUserId,
        userBId: userId
      });
    },

    // 초대 요청 보내기
    inviteFriend(toUserId) {
      this.store.dispatch("roomModule/inviteFriend", {
        fromUserId: this.myUserId,
        toUserId: toUserId
      }).then((response) => {
        if (response == 200) {
          this.store.commit("errorModule/SET_STATUS", 205);
        }
        else if (response == 409) {
          this.store.commit("errorModule/SET_STATUS", 406);
        }
      });
    },

    // 뿌리기 효과
    clickDrop(){
      if (this.dropIdx == 3) {
        this.isDrop = false;
        this.dropIdx = -1;
      } else {
        this.isDrop = false;
        this.dropIdx += 1;
        this.isDrop = true;
      }
    }
  },

  created() {
  },


  // check point

  async mounted() {
    this.getUser();
    await this.getRoom();
    await this.getFriends(); 

    this.joinSession();
  },

  beforeRouteLeave(to, from, next) {
    this.leaveSession();
    next();
  },
};

</script>

<style scoped>
@import url('https://cdn.jsdelivr.net/npm/galmuri@latest/dist/galmuri.css');

#main-container {
  /* background: ; */
  background: #121212;
  height: 90vh;
}

#container {
  height: 90vh;
}

/* #container>div {
  margin: 20px;
} */

#chatting-container {
  display: flex;
  flex-direction: column;
  background: rgb(3, 3, 3);
  border-radius: 30px;
  height: 100%;
  width: 20vw;
  transition: all .5s ease;
}

#chatting-container-small {
  display: flex;
  flex-direction: column;
  background: black;
  border-radius: 30px;
  height: 55%;
  width: 20vw;
  transition: all .5s ease;
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
  background: #e27b66;
  color: white;
  margin-left: auto;
  border-top-right-radius: 0;
}

#msg_not_mine {
  background: #c7c5c5;
  border-top-left-radius: 0;
}

.username {
  font-size: 12px;
}

#send-form {
  display: flex;
  background: #c7c5c5;
  border-radius: 10px;
  align-self: flex-end;
  width: 90%;
  height: 40px;
  justify-content: space-evenly;
  align-items: center;
  margin: 5%;
}

#send-form input {
  width: 70%;
  margin: auto;
  background: #c7c5c5;
  color: black;
  border: 0;
}

#send-form input:focus {
  outline: none;
}

#send-form input::placeholder {
  font-size: 17px;
  opacity: 0.7;
  color: rgb(95, 92, 92);
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

#video-btns {
  display: flex;
  flex-direction: column;
  width: 75vw;
  height: 90vh;
}

#chat-btns {
  display: flex;
  flex-direction: column;
  width: 25vw;
  height: 90vh;
}

#video-container {
  /* border-style: solid;
  border-width: 5px; */
  display: flex;
  justify-content: space-evenly;
  align-content: center;
  align-items: center;
  align-self: center;
  width: 75vw;
  height: 75vh;
  flex-wrap: wrap;
  /* margin: 2.5vh; */
}

#btns-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 75vw;
  height: 15vh;
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

/* #session img {
  width: 100%;
  height: auto;
  display: inline-block;
  object-fit: contain;
  vertical-align: baseline;
} */

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
  align-items: center;
  width: 25vw;
  height: 15vh;
}

#mute {
  display: flex;
  justify-content: space-evenly;
}

.onoff {
  margin-left: 100px;
  cursor: pointer;
  transition: all .1s ease-in;
}

.onoff>img {
  width: 60px;
  height: 60px;
}

#btns {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}

#btns>img {
  width: 60px;
  height: 60px;
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
  display: none;
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 998;
  transition: 0.5s ease-out;
  backdrop-filter: blur(4px) brightness(60%);
}

.game-info-modal-wrap {
  display: none;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60%;
  height: 70%;
  background: #2E303F;
  border: 2px solid #CBCBCB;
  border-radius: 30px;
  z-index: 999;
  overflow: hidden;
}

.game-info-popup {
  display: flex;
  flex-wrap: nowrap;
  height: 100%;
  width: 400%;
  transition: all .3s ease-in;
}

.game-info-popup>div[class*="page"] {
  height: 100%;
  width: 100%;
  display: grid;
  grid-template-rows: 2fr 5fr 2.5fr;
  justify-content: center;
  align-items: center;
  padding: 0 4%;
}

.game-info-title {
  font-size: 2rem;
  text-align: center;
  color: white;
  margin-bottom: 40px;
  margin-bottom: 30px;
}

.game-info-sentence {
  text-align: center;
  color: white;
  height: 120%;
  overflow: auto;
  font-size: 20px;
}

.game-info-sentence1 {
  text-align: left;
  color: white;
  height: 120%;
  overflow: auto;
  font-size: 20px;
}

.game-info-sentence1::-webkit-scrollbar {
  display: none;
}

.game-info-sentence::-webkit-scrollbar {
  display: none;
}

li {
  margin: 5px 0;
}

.game-info-btn-wrapper {
  width: auto;
  height: auto;
  position: absolute;
  bottom: 10%;
  left: 50%;
  transform: translateX(-50%);
  color: white;
  text-align: center;
  z-index: 1001;
}

.btn {
  height: 2.5rem;
  width: 4rem;
  line-height: 2.5rem;
  background: #E27B66;
  border-radius: 20px;
  transition: all .1s ease-in;
}

.btn:hover {
  transform: scale(1.2, 1.2);
  cursor: pointer;
}

.footer-btn {
  width: 5rem;
  height: 3rem;
  color: white;
  text-align: center;
  line-height: 3rem;

  border: solid 3px #E27B66;
  border-radius: 40px;
  transition: all .1s ease-in;
}

.footer-btn:hover {
  transform: scale(1.2, 1.2);
  cursor: pointer;
}

.left-arrow,
.right-arrow {
  width: 5vw;
  height: 5vw;
  top: 42%;
  position: absolute;
  vertical-align: middle;
  transition: all .05s ease-in;
  z-index: 1001;
}

.left-arrow {
  left: 5%;
  background: url('@/assets/images/left_arrow.png') center center /100% no-repeat;
}

.right-arrow {
  right: 5%;
  background: url('@/assets/images//right_arrow.png') center center /100% no-repeat;
}

.left-arrow:hover,
.right-arrow:hover {
  transform: scale(1.2, 1.2);
  cursor: pointer;
}

.game-chatting-container {
  width: 20vw;
  height: 72.5vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-self: center;
  margin-top: 2.5vh;
}

.v-enter-active,
.v-leave-active {
  transition: all 0.5s linear;
  animation: open-game-screen .5s linear;
  animation-delay: 0.7s;
  margin-top: 0px;
}

.v-enter-from,
.v-leave-to {
  animation: open-game-screen 0.5s linear reverse;
}

@keyframes open-game-screen {
  0% {
    height: 0%;
  }

  50% {
    height: 20%;
  }

  100% {
    height: 40%;
  }
}

.game-container {
  background-image: url('@/assets/images/game_screen.png');
  background-repeat: no-repeat;
  background-size: cover;
  border: solid white;
  height: 40%;
  background-color: #2E303F;
  border: 2px solid #CBCBCB;
  border-radius: 30px;
  padding: 1rem;
  margin-bottom: 3vh;
  display: grid;
  grid-template-rows: 1fr 9fr;
}

.stop-btn {
  display: flex;
  justify-content: flex-end;
}

.game-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}

.game-title {
  font-family: 'Galmuri11';
  color: white;
  font-size: 1.5rem;
}

.game-content {
  font-family: 'Galmuri9';
  color: white;
  font-size: 1rem;
}

.cancel {
  color: white;
  border: none;
  text-decoration: none;
  background: none;
  cursor: pointer;
  margin-right: 0px;
}

.cancel:hover {
  color: #e27b66;
}

.info_img {
  height: 50px;
  width: auto;
}

ul {
  list-style: none;
}

p {
  margin: 0px;
}

.option-footer-btn {
  margin-right: 100px;
}

.option-footer-btn:hover {
  cursor: pointer;
}

.invite-btn {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  align-self: center;
  padding: 15px 20px;

  width: 6rem;
  height: 0.8rem;
  color: white;
  font-size: 1rem;
  font-weight: 70;
  background: #121212;
  /* box-shadow: -4px -4px 15px rgba(255, 255, 255, 0.5), 4px 4px 15px rgba(0, 0, 0, 0.5), inset 4px 4px 15px rgba(255, 255, 255, 0.5); */
  border-radius: 50px;

  cursor: pointer;
}
</style>