import { createRouter, createWebHistory } from "vue-router"
import store from "@/store/index"
import HomeView from "../views/lounge/login/HomeView.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
      beforeEnter(to, from, next) {
        if (store.state.userModule.isLogin) {
          alert("이미 로그인 하셧습니다.")
          next({ name: "rooms" })
        } else {
          next()
        }
      },
    },
    {
      path: "/signup",
      name: "signup",
      component: () => import("../views/lounge/signup/SignUpView.vue"),
      beforeEnter(to, from, next) {
        if (store.state.userModule.isLogin) {
          alert("이미 로그인 하셧습니다.")
          next({ name: "rooms" })
        } else {
          next()
        }
      },
    },
    {
      path: "/findid",
      name: "findid",
      component: () => import("../views/lounge/signup/FindIdView.vue"),
      beforeEnter(to, from, next) {
        if (store.state.userModule.isLogin) {
          alert("이미 로그인 하셧습니다.")
          next({ name: "rooms" })
        } else {
          next()
        }
      },
    },
    {
      path: "/findpassword",
      name: "findpassword",
      component: () => import("../views/lounge/signup/FindPasswordView.vue"),
      beforeEnter(to, from, next) {
        if (store.state.userModule.isLogin) {
          alert("이미 로그인 하셧습니다.")
          next({ name: "rooms" })
        } else {
          next()
        }
      },
    },
    {
      path: "/rooms",
      name: "rooms",
      component: () => import("../views/lounge/main/RoomsView.vue"),
      beforeEnter(to, from, next) {
        if (!store.state.userModule.isLogin) {
          alert("로그인을 하셔야 합니다.")
          next({ name: "home" })
        } else {
          next()
        }
      },
    },
    {
      path: "/edit",
      name: "edit",
      component: () => import("../views/lounge/edit/EditView.vue"),
      beforeEnter(to, from, next) {
        if (!store.state.userModule.isLogin) {
          alert("로그인을 하셔야 합니다.")
          next({ name: "home" })
        } else {
          next()
        }
      },
    },
    {
      path: "/room/:roomId",
      name: "room",
      component: () => import("../views/room/room/RoomView.vue"),
      beforeEnter(to, from, next) {
        if (!store.state.userModule.isLogin) {
          alert("로그인을 하셔야 합니다.")
          next({ name: "home" })
        } else {
          next()
        }
      },
    },
    {
      path: "/room/:roomId/wait",
      name: "wait",
      component: () => import("../views/room/wait/WaitView.vue"),
      beforeEnter(to, from, next) {
        if (!store.state.userModule.isLogin) {
          alert("로그인을 하셔야 합니다.")
          next({ name: "home" })
        } else {
          next()
        }
      },
    },
    {
      path: "/test",
      name: "test",
      component: () => import("../views/lounge/test/test.vue"),
    },
  ],
})

export default router
