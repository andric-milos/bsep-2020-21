<template>
  <div class="home">
    <user-navbar></user-navbar>
    <h1>Testing SQL Injection</h1>
    <label>Type an input value for</label>
    <br/>
    <input type="text" id="inputValue">
      <br/>
    <button>Submit</button>
  </div>
</template>
<script>
import UserNavbar from "../../components/navbars/UserNavbar.vue"

export default {
  name: "SqlInjection",
  components: {
    UserNavbar
  }
};
</script>
