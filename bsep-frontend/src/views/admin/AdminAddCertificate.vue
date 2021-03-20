<template>
  <div class="admin-add-certificate">
    <admin-navbar></admin-navbar>
    <h1>This page should have a form for creating a new certificate.</h1>
    <h1>This page should also contains templates for creating new certificates.</h1>
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

      }
    },
    methods: {

    },
    computed: {

    }
  };
</script>
