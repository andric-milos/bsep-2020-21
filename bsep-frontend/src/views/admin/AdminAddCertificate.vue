<template>
  <div class="admin-add-certificate">
    <admin-navbar></admin-navbar>

    <div class="d-flex p-2 justify-content-center">
      <form accept-charset="UTF-8" class="d-flex flex-column col-sm-4">
        <h1 class="p-2">Issue new certificate</h1>

        <h3 clas="p-2">Certificate type</h3>
        <select class="p-2" name="type" id="type" v-model="type">
          <option value="ca">CA</option>
          <option value="endUser">End-user</option>
        </select>

        <br><br>
        <h3 clas="p-2">Date</h3>
        <label class="p-2">Valid from</label>
        <input type="date" class="p-2" id="validFrom" v-model="validFrom">
        <label class="p-2">Valid to</label>
        <input type="date" class="p-2" id="validTo" v-model="validTo">

        <br><br>
        <h3 class="p-2">Subject's data</h3>
        <label class="p-2">Firstname</label>
        <input type="text" class="p-2" id="subjectsFirstname" v-model="subjectsFirstname">
        <label class="p-2">Lastname</label>
        <input type="text" class="p-2" id="subjectsLastname" v-model="subjectsLastname">
        <label class="p-2">Organization</label>
        <input type="text" class="p-2" id="subjectsOrganization" v-model="subjectsOrganization">
        <label class="p-2">Country code</label>
        <input type="text" class="p-2" id="subjectsCountryCode" v-model="subjectsCountryCode">
        <label class="p-2">City</label>
        <input type="text" class="p-2" id="subjectsCity" v-model="subjectsCity">
        <label class="p-2">E-mail</label>
        <input type="text" class="p-2" id="subjectsEmail" v-model="subjectsEmail">
        
        <br><br>
        <h3 class="p-2">Issuer's data</h3>
        <label class="p-2">Firstname</label>
        <input type="text" class="p-2" id="issuersFirstname" v-model="issuersFirstname">
        <label class="p-2">Lastname</label>
        <input type="text" class="p-2" id="issuersLastname" v-model="issuersLastname">
        <label class="p-2">Organization</label>
        <input type="text" class="p-2" id="issuersOrganization" v-model="issuersOrganization">
        <label class="p-2">Country code</label>
        <input type="text" class="p-2" id="issuersCountryCode" v-model="issuertsCountryCode">
        <label class="p-2">City</label>
        <input type="text" class="p-2" id="issuersCity" v-model="issuersCity">
        <label class="p-2">E-mail</label>
        <input type="text" class="p-2" id="issuersEmail" v-model="issuersEmail">
                
        <br><br>
        <button type="submit" class="btn btn-primary" v-on:click.prevent="issue">Confirm</button>
      </form>
    </div>

  </div>
</template>

<script>
import AdminNavbar from '../../components/navbars/AdminNavbar.vue';

  export default  {
    components: { AdminNavbar },
    name: 'AdminAddCertificate',
    props: [],
    mounted () {

    },
    data () {
      return {
        subjectsFirstname: undefined,
        subjectsLastname: undefined,
        subjectsOrganization: undefined,
        subjectsCountryCode: undefined,
        subjectsCity: undefined,
        subjectsEmail: undefined,
        issuersFirstname: undefined,
        issuersLastname: undefined,
        issuersOrganization: undefined,
        issuertsCountryCode: undefined,
        issuersCity: undefined,
        issuersEmail: undefined,
        type: undefined,
        validFrom: undefined,
        validTo: undefined
      }
    },
    methods: {
      issue() {

      }
    },
    computed: {

    }
  };
</script>
