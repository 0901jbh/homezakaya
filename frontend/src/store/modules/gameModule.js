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
    startVideo(context, payload) {
      console.log("시작");
      const video = document.getElementById(payload);
      Promise.all([
          faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
          faceapi.nets.faceExpressionNet.loadFromUri('/models')
      ]).then(() => {
          navigator.mediaDevices.getUserMedia({ video: true })
              .then(function (stream) { video.srcObject = stream; })
              .catch(function (err) { console.log(err); });
      });
      
      video.addEventListener('play', context.dispatch("detect", video));
    },
    detect(context, payload){
      clearInterval(context.state.interval);
      context.commit('SET_INTERVAL',setInterval(async () => {
          const detections = await faceapi.detectAllFaces(payload, new faceapi.TinyFaceDetectorOptions()).withFaceExpressions();
          if(detections.length > 0){
              console.log(detections[0].expressions.happy);
              if(detections[0].expressions.happy > 0.3)
                  console.log("웃었땈ㅋ");
          }
      }, 500));
  },
  }
};
 