import axios from "axios";

export const userModule = {
  namespaced: true,
  state: () => ({
    // users: [], // 전체 유저 필요 X (친구에서 필요,, )
    user: {
      userId: "userAId",
      nickname: "태형",
      mannerPoint: 3,
      alcoholPoint: 7,
    }, // user 전체 정보
    loginUser: {}, // loginUser 정보
    token: null, // accessToken 정보
    tokenExpired: null, // token 만료 시간
    isLogin: false, // 로그인상태
  }),
  mutations: {
    SET_USER(state, payload) {
      state.user = payload;
    },
    // 토큰 설정
    SET_TOKEN(state, payload) {
      state.token = payload;
    },

    // 로그인
    LOGIN(state, payload) {
      state.isLogin = true;
      // state.loginUser = {
      //   nickname: sessionStorage.getItem("nickname"),
      //   mannerPoint: sessionStorage.getItem("mannerPoint"),
      //   alcoholPoint: sessionStorage.getItem("alcoholPoint"),
      // };
      // state.user = state.loginUser;
      state.user = user;
    },

    // 로그아웃
    LOGOUT(state) {
      // state.loginUser = null;
      // state.user = state.loginUser;
      state.isLogin = false;
      state.user = user;
    },

    // 탈퇴
    DELETE_USER(state) {
      // state.isLogin = true;
      state.loginUser = null;
      state.user = state.loginUser;
    },

    // 회원정보 수정(본인)
    UPDATE_USER(state, payload) {
      state.user = payload;
      state.loginUser = {
        nickname: sessionStorage.getItem("nickname"),
        email: sessionStorage.getItem("email"),
        birthDate: sessionStorage.getItem("birthDate"),
        alcoholPoint: sessionStorage.getItem("alcoholPoint"),
      };
    },
  },
  getters: {
    // 호출해야될 변수 : token, userInfo
    // GET_TOKEN(state, token) {
    //   state.token = token;
    // },
    GET_USER(state, user) {
      state.user = user;
    },

    GET_TOKEN(state, token) {
      state.token = token;
    },
  },

  actions: {
    // id 중복확인 - ok
    idcheck(context, payload) {
      return axios
        .get(`/api/users/id/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("사용 가능한 id 입니다.");
            return true;
          }
        })
        .catch((err) => {
          if (err.response.status == 409) {
            console.log("중복된 id 입니다.");
          }
          return false;
        });
    },

    // nickname 중복확인 - ok
    nicknameCheck(context, payload) {
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

    // 회원 가입 - ok
    createUser(context, payload) {
      return axios.post(`/api/users`, payload).then(({ status, data }) => {
        if (status == 200) {
          console.log("회원가입 성공"); // console 출력 안됨
          context.commit("SET_USER", data);
        }
      });
    },

    // 회원 정보 조회 (내정보) - ok
    getUser(context, payload) {
      axios
        .get(`/api/users/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data);
            context.commit("SET_USER", data);
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("존재하지 않는 회원 입니다.");
          }
        });
    },

    // 회원 정보 수정 - ok
    updateUser(context, payload) {
      axios
        .put(`/api/users`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            sessionStorage.setItem("nickname", payload.nickname);
            sessionStorage.setItem("password", payload.password);
            sessionStorage.setItem("email", payload.email); // 표시할건지?
            sessionStorage.setItem("mannerPoint", payload.mannerPoint);
            sessionStorage.setItem("alcoholPoint", payload.alcoholPoint);

            console.log("정보 수정 완료");
            context.commit("UPDATE_USER", data);
          }
        })
        .catch((err) => {
          if (err.status == 404) {
            console.log("유저없음");
          }
        });
    },

    // 로그인 (세션에 토큰 정보 받아오기) - ok
    userLogin(context, payload) {
      axios
        .post(`/api/users/login`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            sessionStorage.setItem("userId", payload.userId);
            sessionStorage.setItem("nickname", data.nickname);
            sessionStorage.setItem("mannerPoint", data.mannerPoint);
            sessionStorage.setItem("alcoholPoint", data.alcoholPoint);
            sessionStorage.setItem("access-token", data.accessToken);

            console.log("로그인 성공");
            context.commit("LOGIN");
          }
        })
        .catch((err) => {
          if (err.status == 401) {
            console.log("로그인 실패");
          }
        });
    },

    // 로그아웃 (세션 날리기) -  ok
    userLogout(context, payload) {
      axios.get(`/api/users/logout/${payload}`).then(({ status, data }) => {
        if (status == 200) {
          sessionStorage.clear();

          context.commit("DELETE_USER");
          console.log("로그아웃 완료");
        }
      });
    },

    // 로그인 토큰 갱신 (새로고침 시 검증)
    tokenRefresh(context, payload) {
      axios.get(`/api/users/refresh`);
    },

    // 회원 탈퇴 (db에서 삭제) - ok
    removeUser(context, payload) {
      axios
        .delete(`/api/users/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            sessionStorage.clear;

            console.log("회원 탈퇴 성공");
            context.commit("DELETE_USER");
          }
        })
        .catch((err) => {
          // 필요한가?
          if (err.response.status == 404) {
            console.log("존재하지 않는 ID");
          }
        });
    },

    // 매너도수 로직 고치기 : from_user, to_user 필요?
    // 매너도수 평가 : 타인
    // 매너도수 표시 : 나

    // 매너도수 - userId : 평가받는(나) 유저
    updateMannerPoint(context, payload) {
      axios
        .put(`/api/point/${payload.userId}`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data);
            context.commit("EVALUATE", data); // userId, mannerPoint
          }
        })
        .catch((err) => {
          // 임시
          console.log("err");
        });
    },
  },
};
