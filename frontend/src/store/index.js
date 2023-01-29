import { createStore } from 'vuex';

const store = createStore({
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
  }
})

export default store;