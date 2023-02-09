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
  },
  getters: {
    getCheckPasswordResult(state){
      return state.checkPasswordResult
    },
    getRoom(state){
      return state.room
    }
  },
  actions: {
    // 방 만들기
    createRoom(context, payload){
      return axios.post(`/api/rooms`, payload).then(({ status, data }) => {
        if(status == 201){
          context.commit("SET_ROOM", data);
        }
        return data
      }).catch(err => {
        if(err.response.status == 404){
          console.log("방 만들기 실패");
        }
        return { roomId: -1 }
      });
    },
    // 방 목록 조회
    getRooms(context, payload){
      axios.get(`/api/rooms`).then(({ status, data }) => {
        if(status == 200){
          console.log("getRooms Success");
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
      return axios.get(`/api/rooms/${payload}`).then(({ status, data }) => {
        if(status == 200){
          context.commit("SET_ROOM", data);
        }
        return data;
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        return null;
      });
    },
    // 비공개방 비밀번호 확인
    checkPassword(context , payload){
      return axios.post(`/api/rooms/password`, payload).then(({ status, data }) => {
        if(status == 200){
          console.log("비밀번호 일치")
          return true
        }
      }).catch(err => {
        if(err.response.status == 401){
          console.log("비밀번호 불일치")
        }
        else if(err.response.status == 404){
          console.log("노방")
        }
        return false
      });
    },
    // 방 입장
    enterRoom(context, payload){
      return axios.put(`api/rooms/${payload}/enter`).then(({ status, data }) => {
        if(status == 200){
          console.log("입장 성공");
        }
        return status;
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        else if(err.response.status == 409){
          console.log("풀방");
        }
        return err.response.status;
      });
    },
    // 방 퇴장
    quitRoom(context, payload){
      return axios.put(`api/rooms/${payload}/quit`).then(({ status, data }) => {
        if(status == 200){
          console.log("quitRoom Success");
          if(data.personCount == 0){
            context.dispatch("removeRoom", payload)
          }
          // else {
          //   context.dispatch("getRooms");
          // }
          return true
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        return false
      });
    },
    changeHost(context, payload){
      axios.put(`api/rooms/${payload.roomId}/host`, payload.hostId).then(({ status }) => {
        if(status == 200){
          console.log("방장 변경 성공!");
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("방이 없거나 유저가 없다.");
        }
      });
    },
    // 방 삭제
    removeRoom(context, payload){
      axios.delete(`api/rooms/${payload}`).then(({ status }) => {
        if(status == 204){
          console.log("방 삭제 성공");
          context.dispatch("getRooms")
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
          console.log("createUserInRoom Success");
          return status;
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        else if(err.response.status == 409){
          console.log("이미 참여중인 유저입니다.");
        }
        return err.response.status;
      });
    },
    removeUserInRoom(context, payload){
      axios.delete(`api/userinroom/${payload}`).then(({ status, data }) => {
        if(status == 204){
          console.log("유저인룸 삭제 성공");
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("이 유저는 현재 참여중인 방이 없습니다.");
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

    checkValidRoom(context, payload){
      const room = context.dispatch("getRoom", payload);
      if(room){
        if(room.personCount >= room.personLimit){
          return 409;
        }
        return 200;
      }
      return 404;
    },

    doEnterRoom(context, payload){
      return context.dispatch("checkValidRoom", payload.roomId).then((status) => {
        if(status != 200){
          return status;
        }
        else{
          return context.dispatch('createUserInRoom', payload).then((result) => {
            if (result == 201) {
              return context.dispatch('enterRoom', payload.roomId).then((result)=>{
                if(result != 200){
                  context.dispatch('deleteUserInRoom', payload.userId);
                }
                return result;
              });
            }
            else if(result == 409){
              return 401;
            }
            return 404;
          });
        }
      });
    },
  }
};
