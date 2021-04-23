import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

import AdminAddCertificate from "../views/admin/AdminAddCertificate.vue";
import AdminsCertificates from "../views/admin/AdminsCertificates.vue";
import AllCertificates from "../views/admin/AllCertificates.vue";
import Register from "../views/Register.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/register",
    name: "Register",
    component: Register
  },
  {
    path: "/admin/add-certificate",
    name: "AdminAddCertificate",
    component: AdminAddCertificate
  },
  {
    path: "/admin/issued-certificates",
    name: "AdminsCertificates",
    component: AdminsCertificates
  },
  {
    path: "/admin/all-certificates",
    name: "AllCertificates",
    component: AllCertificates
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  }
];

const router = new VueRouter({
  routes
});

export default router;
