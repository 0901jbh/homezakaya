import axios from 'axios'

export const roomModule = {
  namespaced: true,
  state: () => ({
    rooms: [],
    room: {},
    checkPasswordResult: false,
  }),
  mutations: {
    SET_ROOMS(state, payload) {
      state.rooms = payload;
    },
    SET_ROOM(state, payload) {
      state.room = payload;
    },
    // SET_ROOM_ID(state, payload){
    //   state.roomId = payload;
    // },
    // SET_CHECK_PASSWORD_RESULT(state, payload) {
    //   if (payload == "success") {
    //     state.checkPasswordResult = true;
    //     console.log('vuex true')
    //   } else {
    //     state.checkPasswordResult = false;
    //     console.log('vuex false')
    //   }
    // },
  },
  getters: {
    getCheckPasswordResult(state){
      return state.checkPasswordResult
    }
  },
  actions: {
    // 방 만들기
    createRoom(context, payload){
      axios.post(`/api/rooms`, payload).then(({ status, data }) => {
        if(status == 201){
          console.log(data);
          context.commit("SET_ROOM", data);
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
      });
    },
    // 방 목록 조회
    getRooms(context, payload){
      axios.get(`/api/rooms`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          context.commit("SET_ROOMS", data);
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
      });
    },
    // 특정 방 조회
    getRoom(context, payload){
      axios.get(`/api/rooms/${payload}`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          context.commit("SET_ROOM", data);
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
      });
    },
    // 비공개방 비밀번호 확인
    checkPassword(context , payload){
      return axios.post(`/api/rooms/password`, payload).then(({ status, data }) => {
        if(status == 200){
          // context.commit("SET_CHECK_PASSWORD_RESULT", data.message)
          console.log("비밀번호 일치")
          return true
        }
      }).catch(err => {
        if(err.response.status == 401){
          // context.commit("SET_CHECK_PASSWORD_RESULT", err.response.message)
          console.log("비밀번호 불일치")
        }
        else if(err.response.status == 404){
          // context.commit("SET_CHECK_PASSWORD_RESULT", err.response.message)
          console.log("노방")
        }
        return false
      });
    },
    // 방 입장
    enterRoom(context, payload){
      return axios.put(`api/rooms/enter/${payload}`).then(({ status, data }) => {
        console.log(payload)
        if(status == 200){
          console.log("입장 성공");
          console.log(data.personCount);
        }
        return true
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        else if(err.response.status == 409){
          console.log("풀방");
        }
        return false
      });
    },
    // 방 퇴장
    quitRoom(context, payload){
      return axios.put(`api/rooms/quit/${payload}`).then(({ status, data }) => {
        if(status == 200){
          console.log("퇴장 성공");
          console.log(data);
          if(data.personCount == 0){
            context.dispatch("removeRoom", payload);
          }
          return true
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        return false
      });
    },
    // 방 삭제
    removeRoom(context, payload){
      axios.delete(`api/rooms/${payload}`).then(({ status }) => {
        if(status == 204){
          console.log("방 삭제 성공");
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
      });
    },
    createUserInRoom(context, payload){
      return axios.post(`api/userinroom`, payload).then(({ status, data }) => {
        if(status == 201){
          console.log("유저인룸 생성 성공");
        }
        return true
      }).catch(err => {
        if(err.response.status == 404){
          console.log(err.response.data);
          console.log("노방");
        }
        else if(err.response.status == 409){
          console.log("이미 참여중인 유저입니다.");
        }
        return false
      });
    },
    removeUserInRoom(context, payload){
      axios.delete(`api/userinroom/${payload}`).then(({ status, data }) => {
        if(status == 204){
          console.log("유저인룸 삭제 성공");
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
      });
    },
    getRoomId(context, payload){
      return axios.get(`api/userinroom/${payload}`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          return data;
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        return -1;
      });
    },
  }
};
 