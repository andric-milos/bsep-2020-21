<template>
  <div>
    <admin-navbar></admin-navbar>
    <register-form></register-form>
  </div>
</template>

<script>
import AdminNavbar from "../components/navbars/AdminNavbar";
import RegisterForm from "../components/RegisterForm";

    export default  {
        name: 'Register',
        components: {
            AdminNavbar,
            RegisterForm
        },
        data () {
            return {

            }
        },
        methods: {

        }
    }

</script>
