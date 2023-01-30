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
    getRooms(state){
      return state.rooms;
    },
    getRoom(state){
      return state.room;
    },
    // getRoomId(state){
    //   return state.roomId;
    // },
  },
  actions: {
    createRoom({ dispatch }, payload){
      axios.post(`/api/rooms`, payload).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          dispatch("getRoom", data.roomId);
        }
        else if(status == 204){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    getRooms({ commit }){
      axios.get(`/api/rooms`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          commit("SET_ROOMS", data);
        }
        else if(status == 204){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    getRoom({ commit }, payload){
      axios.get(`/api/rooms/${payload}`).then(({ status, data }) => {
        if(status == 200){
          console.log(data);
          commit("SET_ROOM", data);
        }
        else if(status == 204){
          console.log("방이 없음.");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    checkPassword(state , payload){
      axios.post(`/api/rooms/password`, payload).then(({ status, data }) => {
        if(status == 200){
          console.log("비밀번호 일치");
        }
        else if(status == 204){
          console.log("비밀번호 불일치");
        }
      }).catch(err => {
        console.log(err);
      });
    },
    // getRooms({ commit }){
    //   axios.get(`/api/rooms`).then(({ status, data }) => {
    //     if(status == 200){
    //       console.log(data);
    //       commit("SET_ROOMS", data);
    //     }
    //     else if(status == 204){
    //       console.log("방이 없음.");
    //     }
    //   }).catch(err => {
    //     console.log(err);
    //   });
    // },
    // getRooms({ commit }){
    //   axios.get(`/api/rooms`).then(({ status, data }) => {
    //     if(status == 200){
    //       console.log(data);
    //       commit("SET_ROOMS", data);
    //     }
    //     else if(status == 204){
    //       console.log("방이 없음.");
    //     }
    //   }).catch(err => {
    //     console.log(err);
    //   });
    // },
    // getRooms({ commit }){
    //   axios.get(`/api/rooms`).then(({ status, data }) => {
    //     if(status == 200){
    //       console.log(data);
    //       commit("SET_ROOMS", data);
    //     }
    //     else if(status == 204){
    //       console.log("방이 없음.");
    //     }
    //   }).catch(err => {
    //     console.log(err);
    //   });
    // },
  }
};
 