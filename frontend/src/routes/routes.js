import VueRouter from "vue-router";

const routes = [
    {
        path: "/app", component: MainView
    },
    {
        path: "/test", component: TEST
    },
];

export default new VueRouter({
    mode: "history",
    routes,
});