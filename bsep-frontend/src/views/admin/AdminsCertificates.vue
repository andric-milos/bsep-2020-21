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
      axios
        .get(" https://localhost:8443/api/certificate", {
              headers: {
                'Authorization': `Bearer ${token}` 
              }
            })
          .then(response => {
            console.log(response);
          })
          .catch(function(){
              alert("Error.");
          });
      
    },
    computed: {

    }
}
</script>
