import axios from "axios";
import jwtDecode from "jwt-decode";

export const userModule = {
  namespaced: true,
  state: () => ({
    // user: {
    //   userId: "power916",
    //   nickname: "태형",
    //   mannerPoint: 3,
    //   alcoholPoint: 7,
    // }, // user 전체 정보

    isLogin: false, // 로그인상태
    isLoginError: false,
    user: null,
    isValidToken: false, // accessToken 정보
  }),
  getters: {
    checkUserInfo: function (state) {
      return state.user;
    },
    checkToken: function (state) {
      return state.isValidToken;
    },
  },
  mutations: {
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    SET_IS_VALID_TOKEN: (state, isValidToken) => {
      state.isValidToken = isValidToken;
    },
    SET_USER_INFO: (state, user) => {
      state.isLogin = true;
      state.user = user;
    },
  },

  actions: {
    // 회원 가입 - ok
    createUser(context, payload) {
      return axios
        .post(`/api/users`, payload)
        .then(({ status, data }) => {
          if (status == 201) {
            console.log("회원가입 성공");
          }
        })
        .catch((err) => {
          if (err.response.status === 409) {
            console.log("이미 존재하는 유저입니다."); // 콘솔에
          }
        });
    },
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
            console.log("정보 수정 완료");
            context.commit("SET_USER_INFO", data);
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
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
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];

            // console.log(data);

            context.commit("SET_IS_LOGIN", true);
            context.commit("SET_IS_LOGIN_ERROR", false);
            context.commit("SET_IS_VALID_TOKEN", true);

            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);

            // 토큰 decode
            let decodedToken = jwtDecode(
              sessionStorage.getItem("access-token")
            );
            // console.log("getUserInfo() decodeToken :: ", decodedToken);
            context.commit("SET_USER_INFO", decodedToken); //user에 저장

            console.log("로그인 성공");
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
          }
        })
        .catch((err) => {
          if (err.response.status == 401) {
            console.log("로그인 실패");
          }
        });
    },

    // 로그아웃 (세션 날리기) -  ok
    userLogout(context, payload) {
      axios
        .get(`/api/users/logout/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            context.commit("SET_IS_LOGIN", false);
            context.commit("SET_USER_INFO", null);
            context.commit("SET_IS_VALID_TOKEN", false);

            sessionStorage.clear();

            console.log("로그아웃 완료");
          } else {
            console.log("유저 정보 없음");
          }
        })
        .catch((err) => {
          if (err.response.status == 401) {
            console.log("로그아웃 실패");
          }
        });
    },

    // 회원 정보 조회 (전체 정보) - ok
    getUserInfo(context, payload) {
      let decodedToken = jwtDecode(payload); // 토큰 정보
      console.log("getUserInfo() decodeToken :: ", decodedToken);
      axios
        .get(`/api/users/${decodedToken.userId}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("data: ", data);
            context.commit("SET_USER_INFO", response.data.user); //user에 저장
          } else {
            // console.log(response.data);
            console.error("Failed to retrieve user information");
          }
        })
        .catch((err) => {
          if (status == 401) {
            console.log("인증되지 않은 토큰");
          }
        });
    },

    // token refresh
    tokenRegeneration(context, payload) {
      console.log(
        "토큰 재발급 >> 기존 토큰 정보 : {}",
        sessionStorage.getItem("access-token")
      );
      axios
        .post(`/api/users/refresh`, payload)
        .then(({ status, data }) => {
          console.log(status);
          if (status == 200) {
            let accessToken = data["access-token"];
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            context.commit("SET_IS_VALID_TOKEN", true);
          }
        })
        .catch((err) => {
          if (err.status == 401) {
            console.log("토큰 갱신 실패");
            userLogout(
              state.user.userId,
              ({ data }) => {
                if (data.message === 200) {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                context.commit("SET_IS_LOGIN", false);
                context.commit("SET_USER_INFO", null);
                context.commit("SET_IS_VALID_TOKEN", false);
                router.push({ name: "login" });
              },
              (error) => {
                console.log(error);
                context.commit("SET_IS_LOGIN", false);
                context.commit("SET_USER_INFO", null);
              }
            );
          }
        });
    },

    // 매너도수
    updateMannerPoint(context, payload) {
      return axios
        .put(`/api/users/point/${payload.userId}`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data);
            console.log("매너도수 평가 완료");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    // 회원 탈퇴 (db에서 삭제) - ok
    removeUser(context, payload) {
      axios
        .delete(`/api/users/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            context.commit("SET_IS_LOGIN", false);
            context.commit("SET_USER_INFO", null);
            context.commit("SET_IS_VALID_TOKEN", false);

            sessionStorage.clear();

            console.log("회원 탈퇴 성공");
          }
        })
        .catch((err) => {
          // 필요한가?
          if (err.response.status == 404) {
            console.log("존재하지 않는 ID");
          }
        });
    },
  },

  // email 인증, id 찾기, pw 찾기
  sendEmail(context, payload) {},
};
