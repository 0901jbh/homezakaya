import axios from 'axios'
import * as faceapi from 'face-api.js'

export const gameModule = {
  namespaced: true,
  state: () => ({
    interval: Object,
    isSmile: false,
    sentence: String,
    topic: String,
    texts: '',
    isFinished: false,
    keyword : String,
  }),
  mutations: {
    SET_INTERVAL(state, payload){
      state.interval = payload;
    },
    SET_SENTENCE(state, payload){
      state.sentence = payload;
    },
    SET_TOPIC(state, payload){
      state.topic = payload;
    },
    SET_KEYWORD(state, payload){
      state.keyword = payload;
    },
    SET_ISSMILE(state, payload){
      state.isSmile = payload;
    },
    SET_TEXTS(state, payload){
      state.texts = payload;
    },
    SET_ISFINISHED(state, payload){
      state.isFinished = payload;
    },
  },
  getters: {
  },
  actions: {
    //문장 가져오기
    async getSentence(context, payload){
      await axios.get(`api/games/sentence`).then(async ({ status, data }) => {
        if(status == 200){
          console.log(data);
          await context.commit("SET_SENTENCE", data.content);
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노문장");
        }
      });
    },
    //주제 가져오기
    async getTopic(context, payload){
      await axios.get(`api/games/topic`).then(async ({ status, data }) => {
        if(status == 200){
          console.log(data);
          await context.commit("SET_TOPIC", data.content);
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노주제");
        }
      });
    },

    // 웃참용 주제 가져오기
    async getKeyword(context, payload){
      await axios.get(`api/games/keyword`).then(async ({ status, data }) => {
        if(status == 200){
          console.log(data);
          await context.commit("SET_KEYWORD", data.content);
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노주제");
        }
      });
    },



    //웃참
    startSmileGame(context, payload) {
      console.log("탐지 시작");
      const video = document.getElementById("local-video-undefined");

      Promise.all([
          faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
          faceapi.nets.faceExpressionNet.loadFromUri('/models')
      ]);
      
      video.addEventListener('play', context.dispatch("startDetect", video));
    },
    startDetect(context, payload){
      context.commit("SET_ISSMILE", false);
      context.commit('SET_INTERVAL',setInterval(async () => {
          const detections = await faceapi.detectAllFaces(payload, new faceapi.TinyFaceDetectorOptions()).withFaceExpressions();
          if(detections.length > 0){
              console.log(detections[0].expressions);
              if(detections[0].expressions.happy > 0.3){
                console.log("웃었땈ㅋ");
                context.commit("SET_ISSMILE", true);
              }
          }
      }, 500));
    },
    stopDetect(context, payload){
      clearInterval(context.state.interval);
      console.log("탐지 끝");
    },
    getSpeech(context, payload){

      window.SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

      let recognition = new SpeechRecognition();
      recognition.interimResults = true;
      recognition.lang = 'ko-KR';

      recognition.start();

      recognition.onstart = function() {
        context.commit("SET_ISFINISHED", false);
        console.log("감지 시작");
      };

      recognition.onend = function() {
        context.commit("SET_ISFINISHED", true);
        console.log("감지 끝");
      };

      recognition.onresult = function(e) {
        let texts = Array.from(e.results).map(results => results[0].transcript).join("");
        console.log(texts);
        context.commit("SET_TEXTS", texts);
      }
    },
    // 정확도 검사
    getAccuracy(context, payload) {
      return axios
      .post(`api/games/accuracy`, payload)
      .then(({ status, data }) => {
        if (status == 200) {
          console.log("정확도:",data.accuracy,"%");
          return data.accuracy + "%";
        }
      })
      .catch((err) => {
        if (err.status == 500) {
          console.log("500 에러");
        }
        return "X";
      });
    },
  }
};
 