import axios from 'axios'
import store from '..';

export const friendModule = {
  namespaced: true,
  state: () => ({
    friends: [],
    requests: [],
    searchUsers: [],
  }),
  mutations: {
    SET_FRIENDS(state, payload) {
      state.friends = payload;
    },
    SET_REQUESTS(state, payload) {
      state.requests = payload;
    },
    SET_SEARCH_USERS(state, payload) {
      state.searchUsers = payload;
    }
  },
  getters: {
    getFriends(state){
      return state.friends;
    },
    getRequests(state){
      return state.requests;
    },
    getSearchUsers(state){
      return state.searchUsers;
    }
  },
  actions: {
    // 친구 목록 조회
    getFriends(context, payload){
      axios.get(`/api/friends/${payload}`)
      .then(({status, data}) => {
        if(status == 200){
          context.commit("SET_FRIENDS", data)
          console.log("getFriends Success")
        }
        else if(status == 404){
          console.log("userId가 없습니다.")
        }
        else if(status == 500){
          console.log("그 외 서버 관련 에러")
        }
      }).catch(err => {
        console.log(err);
      })
    },
    // 친구 삭제
    deleteFriend(context, payload){
      axios.delete(`/api/friends/${payload.userAId}/${payload.userBId}`)
      .then(({ status, data }) => {
        if (status == 200){
          context.dispatch("getFriends", payload.userAId);
          console.log("deleteFriend Success");
        }
        else if (status == 404){
          console.log("userId 또는 친구가 존재하지 않습니다.")
        }
        else if (status == 500){
          console.log("그 외 서버 관련 에러")
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 친구 요청 신청
    sendRequest(context, payload){
      axios.post(`/api/friends/request`, payload)
      .then(({ status, data }) => {
        if (status == 201){
          context.dispatch("getRequests", payload.userAId);
          console.log("sendRequest Success");
        }
        else if (status == 404){
          console.log("userId가 존재하지 않습니다.")
        }
        else if (status == 500){
          console.log("그 외 서버 관련 에러")
        }
      }).catch(err => {
        console.log(err);
      })
    },
    // 친구 요청 조회
    getRequests(context, payload){
      axios.get(`/api/friends/request/${payload}`)
      .then(({status, data}) => {
        if (status == 200) {
          context.commit('SET_REQUESTS', data)
          console.log("getRequests Success");
        }
        else if (status == 404){
          console.log("userId가 존재하지 않습니다.")
        }
        else if (status == 500){
          console.log("그 외 서버 관련 에러")
        }
      }).catch(err => {
        console.log(err);
      })
    },
    // 친구 요청 수락
    approveRequest(context, payload){
      axios.put(`/api/friends/request`, {
        "userAId": payload.userAId,
        "userBId": payload.userBId,
      })
      .then(({status, data}) => {
        if (status == 201) {
          context.dispatch("getRequests", payload.userAId);
          context.dispatch("getFriends", payload.userAId);
          console.log("approveRequest Success");
        }
        else if (status == 404) {
          console.log("userAId 또는 userBId가 존재하지 않습니다.")
        }
        else if (status == 500) {
          console.log("그 외 서버 관련 에러")
        }
      }).catch(err => {
        console.log(err);
      })
    },
    // 친구 요청 거절
    deleteRequest(context, payload){
      axios.delete(`/api/friends/request/${payload.userBId}/${payload.userAId}`)
      .then(({status, data}) => {
        if (status == 200) {
          context.dispatch("getRequests", payload.userAId);
          console.log("deleteRequest Success");
        }
        else if (status == 404) {
          console.log("userAId 또는 userBId가 존재하지 않거나 친구관계가 존재하지 않습니다.")
        }
      }).catch(err => {
        console.log(err);
      })
    },
    // 유저 검색 조회
    searchUser(context, payload){
      axios.get(`/api/friends/search/${payload.userInput}`)
      .then(({ status, data }) => {
        if(status == 200){
          data.forEach((item, index) => {
            if (item.userId === payload.userId) {
              data.splice(index, 1)
            }
          });
          context.commit("SET_SEARCH_USERS", data)
          console.log("searchUser Success");
        }
      }).catch(err => {
        console.log(err);
      })
    }
  }
};
 