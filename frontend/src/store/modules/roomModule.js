import axios from "axios"

export const roomModule = {
  namespaced: true,
  state: () => ({
    rooms: [],
    room: {},
    checkPasswordResult: false,
    requests: [], // 방초대 리스트
    inviteValidFriends:[],  // 초대 가능 친구 리스트
  }),
  mutations: {
    SET_ROOMS(state, payload) {
      state.rooms = payload
    },
    SET_ROOM(state, payload) {
      state.room = payload
    },
    // SET_ROOM_ID(state, payload){
    //   state.roomId = payload;
    // },
    SET_REQUESTS(state, payload) {
      state.requests = payload
    },
    SET_INVITEVALID_FRIENDS(state, payload){
      state.inviteValidFriends = payload;
    }
  },
  getters: {
    getCheckPasswordResult(state) {
      return state.checkPasswordResult
    },
    getRoom(state) {
      return state.room
    },
    getRequests(state) {
      return state.requests
    },
  },
  actions: {
    // 방 만들기
    createRoom(context, payload) {
      return axios
        .post(`/api/rooms`, payload)
        .then(({ status, data }) => {
          if (status == 201) {
            context.commit("SET_ROOM", data)
          }
          return data
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("방 만들기 실패")
          }
          return { roomId: -1 }
        })
    },
    // 방 목록 조회
    getRooms(context, payload) {
      axios
        .get(`/api/rooms`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("getRooms Success")
            context.commit("SET_ROOMS", data)
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("노방")
          }
        })
    },
    // 특정 방 조회
    getRoom(context, payload) {
      return axios
        .get(`/api/rooms/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            context.commit("SET_ROOM", data)
          }
          return data
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("노방")
          }
          return null
        })
    },
    // 비공개방 비밀번호 확인
    checkPassword(context, payload) {
      return axios
        .post(`/api/rooms/password`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("비밀번호 일치")
            return true
          }
        })
        .catch((err) => {
          if (err.response.status == 401) {
            console.log("비밀번호 불일치")
          } else if (err.response.status == 404) {
            console.log("노방")
          }
          return false
        })
    },
    // 방 입장
    enterRoom(context, payload) {
      return axios
        .put(`api/rooms/${payload}/enter`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("입장 성공")
          }
          return status
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("노방")
          } else if (err.response.status == 409) {
            console.log("풀방")
          }
          return err.response.status
        })
    },
    // 방 퇴장
    quitRoom(context, payload){
      return axios.put(`api/rooms/${payload}/quit`).then( async ({ status, data }) => {
        if(status == 200){
          console.log("방 나가기 성공");
          if(data.personCount <= 0){
            context.dispatch("removeRoom", payload)
          }
          else{
            context.dispatch("getRooms")
          }
          return true
        }
      }).catch(err => {
        if(err.response.status == 404){
          console.log("노방");
        }
        console.log("방 나가기 실패");
        return false
      });
    },
    changeHost(context, payload){
      axios.put(`api/rooms/${payload.roomId}/host`, payload).then(({ status }) => {
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
    removeRoom(context, payload) {
      axios
        .delete(`api/rooms/${payload}`)
        .then(({ status }) => {
          if (status == 204) {
            console.log("방 삭제 성공")
            context.dispatch("getRooms")
          }
          
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("노방")
          }
          
        })
    },
    createUserInRoom(context, payload) {
      return axios
        .post(`api/userinroom`, payload)
        .then(({ status, data }) => {
          if (status == 201) {
            console.log("createUserInRoom Success")
            return status
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("노방")
          } else if (err.response.status == 409) {
            console.log("이미 참여중인 유저입니다.")
          }
          return err.response.status
        })
    },
    removeUserInRoom(context, payload){
      return axios.delete(`api/userinroom/${payload}`).then(({ status, data }) => {
        if(status == 204){
          console.log("유저인룸 삭제 성공");
        }
        return true;
      }).catch(err => {
        if(err.response.status == 404){
          console.log("이 유저는 현재 참여중인 방이 없습니다.");
        }
        console.log("유저인룸 삭제 실패");
        return false;
      });
    },
    getRoomId(context, payload) {
      return axios
        .get(`api/userinroom/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data)
            return data
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("노방")
          }
          return -1
        })
    },

    checkValidRoom(context, payload) {
      return context.dispatch("getRoom", payload).then((room) => {
        if (room) {
          if (room.personCount >= room.personLimit) {
            return 409
          }
          return 200
        }
        return 404
      })
    },

    doEnterRoom(context, payload) {
      return context
        .dispatch("checkValidRoom", payload.roomId)
        .then((status) => {
          if (status != 200) {
            return status
          } else {
            return context
              .dispatch("createUserInRoom", payload)
              .then((result) => {
                if (result == 201) {
                  return context
                    .dispatch("enterRoom", payload.roomId)
                    .then((result) => {
                      if (result != 200) {
                        context.dispatch("deleteUserInRoom", payload.userId)
                      }
                      return result
                    })
                } else if (result == 409) {
                  return 402
                }
                return 404
              })
          }
        })
    },

    // 초대 가능 친구 조회 - ok
    inviteValidFriend(context, payload){
      axios.get(`/api/userinroom/invite/valid/${payload}`).then(({status, data})=>{
        if(status == 200){
          context.commit("SET_INVITEVALID_FRIENDS", data)
          console.log("초대 가능한 친구 목록 : ", data)
        }
      }).catch((err)=>{
        console.log(err)
        if (err.response.status == 404) {
          console.log("userId가 존재하지 않습니다.")
        }
      })
    },

    // 방으로 친구 초대 -ok
    inviteFriend(context, payload) {
      return axios.post(`/api/userinroom/invite`, payload).then(({ status, data }) => {
          if (status == 200) {
            console.log("초대 요청을 보냈습니다.")
            return status;
          }
        })
        .catch((err) => {
          console.log(err)
          if (err.response.status == 409) {
            console.log("이미 보낸 초대.")
          } else if (err.response.status == 500) {
            console.log("그 외 서버 관련 에러")
          }
          return err.response.status;
        })
    },

    // 초대한 유저 id만 조회 (fromUser) "xx님이 초대 요청을 보냈습니다." - ok
    getInvitesList(context, payload){
      axios.get(`api/userinroom/invite/${payload}`).then(({ status, data }) => {
        if(status == 200){
          context.commit('SET_REQUESTS', data)
          console.log("getInvites Success");
        }else if (status == 204){
          context.commit('SET_REQUESTS', [])
          console.log("받은 초대 없음.")
        }
        
      }).catch((err) => {
        console.log(err)
        if (err.response.status == 500){
          console.log("그 외 서버 관련 에러")
        }
      });
    },

    // 방으로 친구 초대 거절(초대 정보 삭제) - ok
    removeInvite(context, payload) {
      axios.delete(`api/userinroom/invite/${payload.fromUserId}/${payload.toUserId}`).then(({ status, data }) => {
          if (status == 204) {
            console.log("방초대 삭제 완료")
            context.dispatch("getInvitesList", payload.toUserId);
          }
        })
        .catch((err) => {
          console.log(err);
          if (err.response.status == 404) {
            console.log("방 초대가 없습니다.")
          }
        })
    },
  },
}
