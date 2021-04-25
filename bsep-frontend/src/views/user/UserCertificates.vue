<template>
  <div class="all-certificates">
    <user-navbar></user-navbar>
    <table class="table p-2 mt-2 ml-2 mr-4 table-dark table-striped table-hover">
      <thead>
       <tr>
            <th scope="col">Status</th>
            <th scope="col">Certificate Role</th>
            <th scope="col">City Issuer</th>
            <th scope="col">Country Issuer</th>
            <th scope="col">Email Issuer</th>
            <th scope="col">Start Date</th>
            <th scope="col">Expire Date</th>
            <th scope="col">First Name Issuer</th>
            <th scope="col">Last Name Issuer</th>
            <th scope="col">Organization Issuer</th>
            <th scope="col">Type Of Entity</th>
          </tr>
        </thead>
        <tbody>

        <tr v-for="certificate in certificates" :key="certificate.id">
            <td>{{ certificate.certificateStatus }}</td>
            <td>{{ certificate.certificateRole }}</td>
            <td>{{ certificate.cityIssuer }}</td>
            <td>{{ certificate.countryIssuer }}</td>
            <td>{{ certificate.emailIssuer }}</td>
            <td>{{ certificate.startDate }}</td>
            <td>{{ certificate.expiringDate }}</td>
            <td>{{ certificate.firstNameIssuer }}</td>
            <td>{{ certificate.lastNameIssuer }}</td>
            <td>{{ certificate.organizationIssuer }}</td>
            <td>{{ certificate.typeOfEntity }}</td>
        </tr>

        </tbody> 
    </table>
  </div>
</template>

<script>
import UserNavbar from '../../components/navbars/UserNavbar.vue';
import axios from "axios";

  export default  {
    components: { UserNavbar },
    name: "UserCertificates",
    props: [],
    mounted () {

    },
    data () {
      return {
        certificates:[]
      }
    },
    created() {
      var token = JSON.parse(localStorage.getItem('userInfo')).accessToken;
      var vm = this;

      axios
        .get(" https://localhost:8443/api/certificate/children", {
              headers: {
                'Authorization': `Bearer ${token}` 
              }
            })
          .then(response => {
            console.log(response);
            this.certificates = response.data;
          })
          .catch(function(error){
            if(error.response)
              if(error.response.status === 403){
                //vm.msg="Forbidden access! You don't have permission to view this page!"
                vm.$router.push('/forbidden-access');
              } else {
                alert("Error.");
              }
          });
      
    },

    methods: {

    },
    computed: {

    }
  };
</script>
