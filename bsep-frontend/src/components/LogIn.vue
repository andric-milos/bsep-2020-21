<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <form>
      <h3>Email</h3>
      <input type="text" id="email" v-model="email">
      <h3>Password</h3>
      <input type="password" id="password" v-model="password">
      <br/>
      <button type="submit" class="btn btn-success" style="margin-top:15px" v-on:click.prevent="login">Log In</button>

    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "LogIn",
  props: {
    msg: String
  },
  data () {
    return {
      email: undefined,
      password: undefined
    }
  },
    methods: {
      login() {
        var loginData = {
          email: this.email,
          password: this.password
        };
        axios
          .post(" https://localhost:8443/api/auth/login/", loginData)
          .then(response => {
              var userInfo = response.data;
              //saving the token
              localStorage.setItem('userInfo', JSON.stringify(userInfo));
              //accessing the token
              console.log(JSON.parse(localStorage.getItem('userInfo')));
              this.$router.push('/home');
            })
            .catch(function(){
                alert("Error.");
            });
      }
    }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
