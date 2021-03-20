<template>
  <div class="admins-certificate">
    <admin-navbar></admin-navbar>
    <h1>This page should show certificates issued by logged admin</h1>
  </div>
</template>

<script>
import AdminNavbar from "../../components/navbars/AdminNavbar.vue";

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
    computed: {

    }
}
</script>
