import { createStore } from 'vuex';
import { userModule } from "@/store/modules/userModule.js";
import { friendModule } from "@/store/modules/friendModule.js";
import { roomModule } from "@/store/modules/roomModule.js";
import { userInRoomModule } from "@/store/modules/userInRoomModule.js";

export default createStore({
  state: { // 변수들의 집합
    name:'taehyoung',
  },
  getters: { // state의 변수들을 get 호출
    getNameForm(state){
      return "이름: " + state.name;
    }
  },
  mutations: { // 변수들을 조작하는 함수들
    GET_NAME(state, name){
      state.name = name;
    }
  },
  actions: { // 비동기 처리를 하는 함수들
    getName(context){
      this.axios.get('/name').then(res => {
        context.commit('GET_NAME', res.data)
      })
      .catch(err => {
        console.log(err)
      })
    },
  },
  modules: {
    roomModule,
    friendModule,
    roomModule,
    userInRoomModule,
  }
})