<template>
  <div class="admin-add-certificate">
    <admin-navbar></admin-navbar>
    <add-certificate-form></add-certificate-form>
  </div>
</template>

<script>
import AdminNavbar from '../../components/navbars/AdminNavbar.vue';
import AddCertificateForm from '../../components/AddCertificateForm.vue';

  export default  {
    components: { AdminNavbar, AddCertificateForm},
    name: 'AdminAddCertificate',
    props: [],
    mounted () {

    },
    computed: {

    }
  };
</script>
