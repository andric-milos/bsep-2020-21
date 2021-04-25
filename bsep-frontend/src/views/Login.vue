<template>
  <div class="home">
    <navbar></navbar>
    <img alt="Vue logo" src="../assets/logo.png" />
    <LogIn msg="Log In" />
  </div>
</template>

<script>
// @ is an alias to /src
import LogIn from "@/components/LogIn.vue";
import Navbar from "../components/navbars/Navbar.vue"

export default {
  name: "Login",
  components: {
    LogIn,
    Navbar
  }
};
</script>
