import axios from 'axios'

export const roomModule = {
  namespaced: true,
  state: () => ({
    rooms: [],
    room: {},
    // roomId: Number
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
  },
  getters: {
  },
  actions: {
    // 방 만들기
    createRoom(context, payload){
      axios.post(`/api/rooms`, payload).then(({ status, data }) => {
        if(status == 201){
          console.log(data);
          context.dispatch("getRoom", data.roomId);
        }
        else if(status == 404){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    // 방 목록 조회
    getRooms(context, payload){
      axios.get(`/api/rooms`).then(({ status, data }) => {
        if(status == 200){
          console.log(data.roomList);
          context.commit("SET_ROOMS", data.roomList);
        }
        else if(status == 404){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    // 특정 방 조회
    getRoom(context, payload){
      axios.get(`/api/rooms/${payload}`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          context.commit("SET_ROOM", data);
        }
        else if(status == 404){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    // 비공개방 비밀번호 확인
    checkPassword(context , payload){
      return axios.post(`/api/rooms/password`, payload).then(({ status, data }) => {
        if(status == 200){
          console.log("비밀번호 일치");
          return true;
        }
        else{
          if(status == 401){
            console.log("비밀번호 불일치");
          }
          else if(status == 404){
            console.log("방이 없음.");
          }
          return false;
        }
      }).catch(err => {
        console.log(err);
      });
    },
    // 방 입장
    enterRoom(context, payload){
      axios.put(`api/rooms/enter/${payload}`).then(({ status, data }) => {
        if(status == 200){
          console.log("입장 성공");
          console.log(data);
        }
        else if(status == 404){
          console.log("방이 없음");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    // 방 퇴장
    quitRoom(context, payload){
      axios.put(`api/rooms/quit/${payload}`).then(({ status, data }) => {
        if(status == 200){
          console.log("퇴장 성공");
          console.log(data);
          if(data.personCount == 0){
            context.dispatch("removeRoom", payload);
          }
        }
        else if(status == 404){
          console.log("방이 없음");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    // 방 삭제
    removeRoom(context, payload){
      axios.delete(`api/rooms/${payload}`).then(({ status }) => {
        if(status == 204){
          console.log("방 삭제 성공");
        }
        else if(status == 404){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    
    createUserInRoom(context, payload){
      axios.post(`api/userinroom`, payload).then(({ status, data }) => {
        if(status == 201){
          console.log("유저인룸 생성 성공");
        }
        else if(status == 404){
          console.log("방이 없음.");
        }
        else if(status == 409){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    removeUserInRoom(context, payload){
      axios.delete(`api/userinroom/${payload}`).then(({ status, data }) => {
        if(status == 204){
          console.log("유저인룸 삭제 성공");
        }
        else if(status == 404){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    getRoomId(context, payload){
      return axios.get(`api/userinroom/${payload}`).then(({ status, data }) => {
        if(status == 201){
          console.log(data);
          return data;
        }
        else if(status == 404){
          console.log("방이 없음.");
        }
        return -1;
      }).catch(err => {
        console.log(err);
      });
    },
  }
};
 