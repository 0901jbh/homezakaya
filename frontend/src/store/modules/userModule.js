import axios from "axios"
import jwtDecode from "jwt-decode"

export const userModule = {
  namespaced: true,
  state: () => ({
    user: {
      userId: "korealdw",
      nickname: "동우",
      mannerPoint: 3,
      alcoholPoint: 7,
    }, // user 전체 정보

    isLogin: false, // 로그인상태
    isLoginError: false,
    user: {},
    isValidToken: false, // accessToken 정보
  }),
  getters: {
    checkUserInfo: function (state) {
      return state.user
    },
    checkToken: function (state) {
      return state.isValidToken
    },
  },
  mutations: {
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError
    },
    SET_IS_VALID_TOKEN: (state, isValidToken) => {
      state.isValidToken = isValidToken
    },
    SET_USER_INFO: (state, user) => {
      state.isLogin = true
      state.user = user
    },
  },

  actions: {
    // 회원 가입 - ok
    async createUser(context, payload) {
      await axios
        .post(`/api/users`, payload)
        .then(({ status, data }) => {
          if (status == 201) {
            console.log("회원가입 성공")
          }
        })
        .catch((err) => {
          if (err.response.status === 409) {
            console.log("이미 존재하는 유저입니다.") // 콘솔에
          }
        })
    },
    // id 중복확인 - ok
    async idcheck(context, payload) {
      await axios
        .get(`/api/users/id/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("사용 가능한 id 입니다.")
            return true
          }
        })
        .catch((err) => {
          console.log(err)
          if (err.response.status == 409) {
            console.log("중복된 id 입니다.")
          }
          return false
        })
    },

    // nickname 중복확인 - ok
    async nicknameCheck(context, payload) {
      await axios
        .get(`/api/users/nickname/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("사용 가능한 nickname 입니다.")
            return true
          }
        })
        .catch((err) => {
          if (err.response.status == 409) {
            console.log("중복된 닉네임 입니다.")
          }
          return false
        })
    },

    // email 인증, id 찾기, pw 찾기
    sendEmail(context, payload) {
      axios
        .post(`/api/users/login/mailConfirm`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data)
            context.commit("SET_USER_INFO", data) // 인증번호 확인용
            console.log("인증메일 발송 완료")
          } else {
            console.log("이미 가입된 이메일 입니다.")
          }
        })
        .catch((err) => {
          // 필요한가?
          if (err.response.status == 404) {
            console.log("발송 실패")
          }
        })
    },

    // 회원 가입 - ok
    async createUser(context, payload) {
      await axios
        .post(`/api/users`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("회원가입 성공") // console 출력 안됨
            context.commit("SET_USER", data)
          }
        })
        .catch((err) => {
          if (err.response.status == 500) {
            console.log("회원가입 실패")
          }
        })
    },

    // 회원 정보 수정 - ok
    updateUser(context, payload) {
      axios
        .put(`/api/users`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log("정보 수정 완료")
            context.commit("SET_USER_INFO", data)
          }
        })
        .catch((err) => {
          if (err.response.status == 404) {
            console.log("유저없음")
          }
        })
    },

    // 로그인 - ok
    async userLogin(context, payload) {
      await axios
        .post(`/api/users/login`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            let accessToken = data["access-token"]
            let refreshToken = data["refresh-token"]

            context.commit("SET_IS_LOGIN", true)
            context.commit("SET_IS_LOGIN_ERROR", false)
            context.commit("SET_IS_VALID_TOKEN", true)

            sessionStorage.setItem("access-token", accessToken)
            sessionStorage.setItem("refresh-token", refreshToken)

            console.log("로그인 성공")
          } else {
            commit("SET_IS_LOGIN", false)
            commit("SET_IS_LOGIN_ERROR", true)
            commit("SET_IS_VALID_TOKEN", false)
          }
        })
        .catch((err) => {
          if (err.response.status == 401) {
            console.log("로그인 실패")
          }
        })
    },

    // 로그아웃 (세션 날리기) -  ok
    async userLogout(context, payload) {
      await axios
        .get(`/api/users/logout/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            context.commit("SET_IS_LOGIN", false)
            context.commit("SET_USER_INFO", {})
            context.commit("SET_IS_VALID_TOKEN", false)

            sessionStorage.clear()

            console.log("로그아웃 완료")
          } else {
            console.log("유저 정보 없음")
          }
        })
        .catch((err) => {
          if (err.response.status == 401) {
            console.log("로그아웃 실패")
          }
        })
    },

    // 회원 정보 조회 (전체 정보) - ok
    async getUserInfo(context, payload) {
      if (payload) {
        let decodedToken = jwtDecode(payload) // 토큰 정보
        console.log("getUserInfo() decodeToken :: ", decodedToken)
        await axios
          .get(`/api/users/${decodedToken.userId}`)
          .then(({ status, data }) => {
            if (status == 200) {
              // console.log("data: ", data);
              context.commit("SET_USER_INFO", data) //user에 저장
            } else {
              console.error("Failed to retrieve user information")
            }
          })
          .catch((err) => {
            console.log("토큰이 만료되어 사용 불가")
            // commit("SET_IS_VALID_TOKEN", false);
          })
      } else {
        console.log("토큰 null")
      }
    },

    // token refresh
    tokenRegeneration(context, payload) {
      console.log(
        "토큰 재발급 >> 기존 토큰 정보 : {}",
        sessionStorage.getItem("access-token")
      )
      axios
        .post(`/api/users/refresh`, payload)
        .then(({ status, data }) => {
          console.log(status)
          if (status == 200) {
            let accessToken = data["access-token"]
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken)
            sessionStorage.setItem("access-token", accessToken)
            context.commit("SET_IS_VALID_TOKEN", true)
          }
        })
        .catch((err) => {
          if (err.status == 401) {
            console.log("토큰 갱신 실패")
            userLogout(
              state.user.userId,
              ({ data }) => {
                if (data.message === 200) {
                  console.log("리프레시 토큰 제거 성공")
                } else {
                  console.log("리프레시 토큰 제거 실패")
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.")
                context.commit("SET_IS_LOGIN", false)
                context.commit("SET_USER_INFO", {})
                context.commit("SET_IS_VALID_TOKEN", false)
                router.push({ name: "login" })
              },
              (error) => {
                console.log(error)
                context.commit("SET_IS_LOGIN", false)
                context.commit("SET_USER_INFO", {})
              }
            )
          }
        })
    },

    // 매너도수
    updateMannerPoint(context, payload) {
      return axios
        .put(`/api/users/point/${payload.userId}`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data)
            console.log("매너도수 평가 완료")
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },

    // 회원 탈퇴 (db에서 삭제) - ok
    removeUser(context, payload) {
      axios
        .delete(`/api/users/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            context.commit("SET_IS_LOGIN", false)
            context.commit("SET_USER_INFO", {})
            context.commit("SET_IS_VALID_TOKEN", false)

            sessionStorage.clear()

            console.log("회원 탈퇴 성공")
          }
        })
        .catch((err) => {
          // 필요한가?
          if (err.response.status == 404) {
            console.log("존재하지 않는 ID")
          }
        })
    },
  },
}
