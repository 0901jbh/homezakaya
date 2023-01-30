import { createStore } from 'vuex';
import { userModule } from "@/store/modules/userModule.js";
import { friendModule } from "@/store/modules/friendModule.js";
import { roomModule } from "@/store/modules/roomModule.js";
import { userInRoomModule } from "@/store/modules/userInRoomModule.js";

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
    userInRoomModule,
  }
})