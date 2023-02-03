import axios from 'axios'
import * as faceapi from 'face-api.js'

export const gameModule = {
  namespaced: true,
  state: () => ({
    interval: Object,
    sentence: String,
    topic: String,
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
  },
  getters: {
  },
  actions: {
    //문장 가져오기
    getSentence(context, payload){
      axios.get(`api/games/sentence`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          context.commit("SET_SENTENCE", data.content);
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노문장");
        }
      });
    },
    //주제 가져오기
    getTopic(context, payload){
      axios.get(`api/games/topic`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          context.commit("SET_TOPIC", data.content);
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
      const videos = document.getElementsByTagName("video");
      console.log(videos);
      const video = videos[0];

      Promise.all([
          faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
          faceapi.nets.faceExpressionNet.loadFromUri('/models')
      ]);
      
      video.addEventListener('play', context.dispatch("startDetect", video));
    },
    startDetect(context, payload){
      context.dispatch("stopDetect");
      context.commit('SET_INTERVAL',setInterval(async () => {
          const detections = await faceapi.detectAllFaces(payload, new faceapi.TinyFaceDetectorOptions()).withFaceExpressions();
          if(detections.length > 0){
              console.log(detections[0].expressions);
              if(detections[0].expressions.happy > 0.3){
                console.log("웃었땈ㅋ");
                context.dispatch("stopDetect");
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
        console.log("감지 시작");
      };

      recognition.onend = function() {
        console.log("감지 끝");
      };

      recognition.onresult = function(e) {
        console.log(e);
        let texts = Array.from(e.results).map(results => results[0].transcript).join("");
        console.log(texts);
      }
    }
  }
};
 