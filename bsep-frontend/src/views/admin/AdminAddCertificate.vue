<template>
  <div class="admin-add-certificate">
    <admin-navbar></admin-navbar>
    <add-certificate-form></add-certificate-form>
  </div>
</template>

<script>
import AdminNavbar from '../../components/navbars/AdminNavbar.vue';
import AddCertificateForm from '../../components/AddCertificateForm.vue';
import axios from "axios";

  export default  {
    components: { AdminNavbar, AddCertificateForm},
    name: 'AdminAddCertificate',
    props: [],
    created(){
      var token = JSON.parse(localStorage.getItem('userInfo')).accessToken;
       axios
        .get(" https://localhost:8443/api/user/whoami", {
          headers: {
            'Authorization': `Bearer ${token}` 
          }
        })
        .then(response => {
          if(!response.data.userRoles.includes("ADMIN"))
            this.$router.push('/forbidden-access');
        });
    },
    mounted () {},
    computed: {

    }
  };
</script>
