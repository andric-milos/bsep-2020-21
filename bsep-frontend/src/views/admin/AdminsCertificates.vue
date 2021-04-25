<template>
  <div class="admins-certificate">
    <admin-navbar></admin-navbar>
    <h1>This page should show certificates issued by logged admin</h1>
  </div>
</template>

<script>
import AdminNavbar from "../../components/navbars/AdminNavbar.vue";
import axios from "axios";

  export default  {
    components: { AdminNavbar },
    name: 'AdminsCertificates',
    props: [],
    mounted () {

    },
    data () {
      return {

      }
    },
    methods: {

    },
    created() {
      var token = JSON.parse(localStorage.getItem('userInfo')).accessToken;
      var vm = this;
     
      axios
      .get(" https://localhost:8443/api/certificate", {
            headers: {
              'Authorization': `Bearer ${token}` 
            }
          })
        .then(response => {
          console.log(response);
        }) 
        .catch(function(error){
            if(error.response.status === 403){
              //vm.msg="Forbidden access! You don't have permission to view this page!"
              vm.$router.push('/forbidden-access');
            } else {
              alert("Error.");
            }
        });
      
    },
    computed: {

    }
}
</script>
