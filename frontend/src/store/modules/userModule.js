import axios from "axios"
import jwtDecode from "jwt-decode"

export const userModule = {
  namespaced: true,
  state: () => ({
    // user: {
    //   userId: "korealdw",
    //   nickname: "동우",
    //   mannerPoint: 3,
    //   alcoholPoint: 7,
    // }, // user 전체 정보

    isLogin: false, // 로그인상태
    isLoginError: false,
    user: {},
    isValidToken: false, // accessToken 정보

    idErr: null, // id 중복 boolean
    nicknameErr: null, // nickname 중복 boolean
    emailErr: null, // nickname 중복 boolean
    newPass: null, // 새로운 비밀번호 갔는지 boolean
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
      state.user = user
    },
    SET_ID_ERR: (state, idErr) => {
      state.idErr = idErr
    },
    SET_NICKNAME_ERR: (state, nicknameErr) => {
      state.nicknameErr = nicknameErr
    },
    SET_EMAIL_ERR: (state, emailErr) => {
      state.emailErr = emailErr
    },
    SET_NEW_PASSWORD: (state, newPass) => {
      state.newPass = newPass
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
            context.commit("SET_ID_ERR", false)
            return true
          }
        })
        .catch((err) => {
          console.log(err)
          if (err.response.status == 409) {
            console.log("중복된 id 입니다.")
          }
          context.commit("SET_ID_ERR", true)
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
            context.commit("SET_NICKNAME_ERR", false)
            return true
          }
        })
        .catch((err) => {
          if (err.response.status == 409) {
            console.log("중복된 닉네임 입니다.")
          }
          context.commit("SET_NICKNAME_ERR", true)
          return false
        })
    },

    // email 인증
    async sendEmail(context, payload) {
      await axios
        .post(`/api/users/login/mailConfirm`, payload)
        .then(({ status, data }) => {
          console.log(status)
          console.log(data)
          if (status == 200) {
            console.log(data)
            context.commit("SET_USER_INFO", data) // 인증번호 확인용
            console.log("인증메일 발송 완료")
            context.commit("SET_EMAIL_ERR", false)
          } else if(status== 208) {
            console.log("이미 가입된 이메일 입니다.")
            context.commit("SET_EMAIL_ERR", true)
          }else if(status == 500){
            console.log("INTERNAL_SERVER_ERROR")
          }
        })
        .catch((err) => {
          console.log("잘못된 email 형식입니다.")
          context.commit("SET_EMAIL_ERR", true)
        })
    },

    // id 찾기 (email 입력 - id 찾기용 email 인증 같이 써줘야 함) ok
    async findId(context, payload) {
      await axios
        .post(`/api/users/login/findId`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data) // userId, emailCode
            context.commit("SET_USER_INFO", data) //user에 저장(찾아온 id만)
            context.commit("SET_EMAIL_ERR", false)
          } else if(status== 204){
            context.commit("SET_EMAIL_ERR", true)
            console.log("해당 이메일로 가입한 아이디가 존재하지 않습니다.")
          }else if (status == 500) {
            console.log("INTERNAL_SERVER_ERROR")
          }
        })
        .catch((err) => {
          console.log(err)
          context.commit("SET_EMAIL_ERR", true)
        })
    },

    // pw 찾기 (userId, email 입력) - ok
    async findPassword(context, payload) {
      await axios
        .post(`/api/users/login/findPassword`, payload)
        .then(({ status, data }) => {
          if (status == 200) {
            console.log(data)
            console.log("임시 비밀번호 발급 완료!! 비밀번호를 변경해 주세요")
            context.commit("SET_EMAIL_ERR", false)
            context.commit("SET_NEW_PASSWORD", true)
          }else if(status == 204){
            console.log("해당 이메일로 가입한 아이디가 존재하지 않습니다.")
            context.commit("SET_EMAIL_ERR", true)
          }else if(status == 500){
            console.log("INTERNAL_SERVER_ERROR")
          }
        })
        .catch((err) => {
          // console.log("아이디 or 이메일 정보가 일치하지 않습니다")
          console.log(err)
          context.commit("SET_EMAIL_ERR", true)
          context.commit("SET_NEW_PASSWORD", false)
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
        .then(async ({ status, data }) => {
          if (status == 200) {
            await axios
              .delete(`api/userinroom/${payload}`)
              .then(({ status, data }) => {
                if (status == 204) {
                  console.log("유저인룸 삭제 성공")
                }
              })
              .catch((err) => {
                if (err.response.status == 404) {
                  console.log("이 유저는 현재 참여중인 방이 없습니다.")
                }
              })
            context.commit("SET_USER_INFO", {})
            context.commit("SET_IS_LOGIN", false)
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
        ;async (err) => {
          console.log("토큰이 만료되어 사용 불가")
          commit("SET_IS_VALID_TOKEN", false)
          await context.dispatch(
            "tokenRegeneration",
            sessionStorage.getItem("access-token")
          )
        }
      }
    },

    // token refresh
    async tokenRegeneration(context, payload) {
      console.log(
        "토큰 재발급 >> 기존 토큰 정보 : {}",
        sessionStorage.getItem("access-token")
      )
      console.log(payload)
      await axios
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
          if (err.response.status == 401) {
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
                context.commit("SET_USER_INFO", {})
                context.commit("SET_IS_LOGIN", false)
                context.commit("SET_IS_VALID_TOKEN", false)
                router.push({ name: "login" })
              },
              (error) => {
                console.log(error)
                context.commit("SET_USER_INFO", {})
                context.commit("SET_IS_LOGIN", false)
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
    async removeUser(context, payload) {
      await axios
        .delete(`/api/users/${payload}`)
        .then(async ({ status, data }) => {
          if (status == 200) {
            await axios
              .delete(`api/userinroom/${payload}`)
              .then(({ status, data }) => {
                if (status == 204) {
                  console.log("유저인룸 삭제 성공")
                }
              })
              .catch((err) => {
                if (err.response.status == 404) {
                  console.log("이 유저는 현재 참여중인 방이 없습니다.")
                }
              })
            context.commit("SET_USER_INFO", {})
            context.commit("SET_IS_LOGIN", false)
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

    getUserPoint(context, payload) {
      return axios
        .get(`/api/users/${payload}`)
        .then(({ status, data }) => {
          if (status == 200) {
            return data
          } else {
            console.log("status != 200")
          }
        })
        .catch((err) => {
          if (err.response == 401) {
            console.log("유저 정보 획득 실패")
          }
        })
    },
  },
}
