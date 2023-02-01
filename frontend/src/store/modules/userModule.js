import axios from "axios";
import { colorPickerEmits } from "element-plus";

export const userModule = {
  namespaced: true,
  state: () => ({
    users: [],
    user: {},
    token: colorPickerEmits.get("access-token"),
    // userId: "taehyoung",
  }),
  mutations: {
    LOGIN(state, item) {
      state.token = item.headers["access-token"];
    },

    GET_USER(state, payload) {
      state.user = payload;
    },

    GET_USERS(state, payload) {
      state.users = payload;
    },

    SET_USER(state, payload) {
      state.user = payload;
      state.loginUser = {};
    },

    // 토큰 설정
    SET_TOKEN(state, token) {
      state.token = token;
    },

    SET_USER(state, payload) {
      state.user = payload;
    },
  },
  getters: {
    getId(state) {
      if (state.token) {
        return userId;
      } else {
        return false;
      }
    },

    config: (state) => ({ headers: { "X-access-token": state.token } }),
  },
  actions: {
    // 회원 가입
    createUser(context, payload) {
      axios
        .post(`/api/users`, payload)
        .then(({ status, data }) => {
          if (status == 201) {
            console.log("회원가입 성공");
            context.dipatch("getUser", data.userId);
          }
        })
        .catch((err) => {
          console.log("회원가입 실패");
        });
    },

    // id 중복확인
    checkUserId(context, payload) {
      return axios
        .get(`/api/users/id/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("사용 가능한 id 입니다.");
            return true;
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("중복된 id 입니다.");
          }
          return false;
        });
    },

    // nickname 중복확인
    checkNickName(context, payload) {
      return axios
        .get(`/api/users/nickname/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("사용 가능한 nickname 입니다.");
            return true;
          }
        })
        .catch((err) => {
          if (err.response.status == 409) {
            console.log("중복된 닉네임 입니다.");
          }
          return false;
        });
    },

    // 회원 정보 조회
    getUser(context, payload) {
      axios
        .get(`/api/users/${payload}`)
        .then(({ staus, data }) => {
          if (staus) {
            console.log(data);
            context.commit("GET_USER", data);
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("존재하지 않는 id 입니다.");
          }
        });
    },

    // 회원 정보 수정
    updateUser(context, payload) {
      axios
        .put(`/api/users/${payload}`)
        .then(({ staus, data }) => {
          if (staus == 200) {
            console.log(data);
            context.commit("UPDATE_USER", data);
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("유저없음");
          }
        });
    },

    // 회원 탈퇴 (db에서 삭제)
    removeUser(context, payload) {
      axios
        .delete(`/api/users/${payload}`)
        .then(({ staus }) => {
          if (staus == 204) {
            console.log("회원 탈퇴 성공");
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("존재하지 않는 ID");
          }
        });
    },

    // 매너도수 계산
    updateMannerPoint(context, payload) {
      axios.put(`/api/point/${payload}`).then(({ status, data }) => {
        if (status == 200) {
          console.log(data);
          context.commit("GET_MANNER_POINT", data);
        }
      });
    },

    // 로그인 (세션에 정보 받아오기)
    userLogin(context, payload) {
      axios
        .get(`/api/users/login`)
        .then(({ status, data }) => {
          if (status == 200) {
            // sessionStorage.setItem("access-token");

            context.commit("GET_TOKEN", data.userId);
            return true;
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("로그인 실패");
          }
          return false;
        });
    },

    // 로그아웃 (세션 날리기)
    userLogout() {
      axios.get(`/api/users/logout`).then(({ status, data }) => {
        if (status == 200) {
          sessionStorage.removeItem("access-token");
          context.commit("LOGOUT");
        }
      });
    },

    // 로그인 토큰 갱신
    tokenRefresh() {},
  },
};
