<template>
  <div class="admin-add-certificate">
    <user-navbar></user-navbar>
    <add-certificate-form></add-certificate-form>
  </div>
</template>

<script>
import UserNavbar from '../../components/navbars/UserNavbar.vue';
import AddCertificateForm from '../../components/AddCertificateForm.vue';

  export default  {
    components: { UserNavbar, AddCertificateForm},
    name: 'UserAddCertificate',
    props: [],
    mounted () {

    },
    computed: {

    }
  };
</script>
