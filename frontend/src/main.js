import { createApp } from "vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from "axios";
import "./assets/main.css";

axios.defaults.baseURL = "https://i8a606.p.ssafy.io:8081"; // axios baseURL

const app = createApp(App);

app.config.globalProperties.$axios = axios; // axios 전역화

app.use(router);
app.use(store);
app.use(ElementPlus);
app.mount("#app");
