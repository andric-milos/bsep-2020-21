<template>
  <div>
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark justify-content-between">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="#/home-user">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#/login">Log In</a>
            </li>
            <!-- This register nav-link is temporary - it should be inside of HomeNavbar which currently doesn't exist -->
            <li class="nav-item">
                <a class="nav-link" href="#/register">Register</a>
            </li>
            <li>
                <a class="nav-link" href="#/user/add-certificate">Add new certificate</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#/user/issued-certificates">Issued certificates</a>
            </li>
      </ul>
		</nav> 
  </div>
</template>

<script>
  export default  {
    name: 'UserNavbar',
    props: [],
    data () {
    return {
      user: undefined
    }
  },
    created() {
        console.log(JSON.parse(localStorage.getItem('userInfo')));
    },
    methods: {
      getLoggedInUser() {
        console.log(JSON.parse(localStorage.getItem('userInfo')));
      }
    }
}
</script>
