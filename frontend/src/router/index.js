import { createRouter, createWebHistory } from "vue-router"
import HomeView from "../views/lounge/login/HomeView.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/signup",
      name: "signup",
      component: () => import("../views/lounge/signup/SignUpView.vue"),
    },
    {
      path: "/rooms",
      name: "rooms",
      component: () => import("../views/lounge/main/RoomsView.vue"),
    },
    {
      path: "/edit",
      name: "edit",
      component: () => import("../views/lounge/edit/EditView.vue"),
    },
    {
      path: "/room/:roomId",
      name: "room",
      component: () => import("../views/room/room/RoomView.vue"),
    },
    {
      path: "/room/:roomId/wait",
      name: "wait",
      component: () => import("../views/room/wait/WaitView.vue"),
    },
    {
      path: "/test",
      name: "test",
      component: () => import("../views/lounge/test/test.vue"),
    },
  ],
})

export default router
