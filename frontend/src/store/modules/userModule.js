import axios from 'axios'

export const userModule = {
  namespaced: true,
  state: () => ({
    // users: [], // 전체 유저 필요 X (친구에서 필요,, )
    user: {}, // user 전체 정보
    loginUser: {}, // loginUser 정보
    token: null, // accessToken 정보
    tokenExpired: null, // token 만료 시간
    isLogin: false, // 로그인상태
  }),
  mutations: {

  },
  getters: {

  },
  actions: {
    
  }
};
 