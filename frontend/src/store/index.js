import { createStore } from 'vuex';
import { userModule } from "@/store/modules/userModule.js";
import { friendModule } from "@/store/modules/friendModule.js";
import { roomModule } from "@/store/modules/roomModule.js";
import { gameModule } from "@/store/modules/gameModule.js";
import { errorModule } from "@/store/modules/errorModule.js";
import createPersistedState from 'vuex-persistedstate';

export default createStore({
  state: { // 변수들의 집합
  },
  getters: { // state의 변수들을 get 호출
  },
  mutations: { // 변수들을 조작하는 함수들
  },
  actions: { // 비동기 처리를 하는 함수들
  },
  modules: {
    userModule,
    friendModule,
    roomModule,
    gameModule,
    errorModule,
  },
  plugins: [
    createPersistedState({
      // 브라우저 종료시 제거하기 위해 localStorage가 아닌 sessionStorage로 변경. (default: localStorage)
      storage: sessionStorage,
    }),
  ],
})