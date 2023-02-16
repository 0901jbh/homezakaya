export const errorModule = {
  namespaced: true,
  state: () => ({
    status: 200,
  }),
  mutations: {
    SET_STATUS(state, payload) {
      state.status = payload;
    },
  },
  actions: {
  }
};
