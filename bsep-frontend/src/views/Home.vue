<template>
  <div class="home">
    <admin-navbar></admin-navbar>
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Log In" />
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";
import AdminNavbar from "../components/navbars/AdminNavbar.vue"

export default {
  name: "Home",
  components: {
    HelloWorld,
    AdminNavbar
  }
};
</script>
