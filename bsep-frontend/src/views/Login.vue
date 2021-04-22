<template>
  <div class="home">
    <admin-navbar></admin-navbar>
    <img alt="Vue logo" src="../assets/logo.png" />
    <LogIn msg="Log In" />
  </div>
</template>

<script>
// @ is an alias to /src
import LogIn from "@/components/LogIn.vue";
import AdminNavbar from "../components/navbars/AdminNavbar.vue"

export default {
  name: "Login",
  components: {
    LogIn,
    AdminNavbar
  }
};
</script>
