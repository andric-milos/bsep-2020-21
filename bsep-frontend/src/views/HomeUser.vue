<template>
  <div class="home">
    <user-navbar></user-navbar>
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Welcome (logged in as user)" />
    <button v-on:click.prevent="openSqlInjectionPage">Test SQL Injection</button>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";
import UserNavbar from "../components/navbars/UserNavbar.vue"

export default {
  name: "Home",
  components: {
    HelloWorld,
    UserNavbar
  },
    methods: {
      openSqlInjectionPage(){
        this.$router.push("/sql-injection");
      }
    }
}
</script>
