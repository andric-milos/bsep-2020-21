<template>
  <div>
    <navbar></navbar>
    <register-form></register-form>
  </div>
</template>

<script>
import Navbar from "../components/navbars/Navbar";
import RegisterForm from "../components/RegisterForm";

    export default  {
        name: 'Register',
        components: {
            Navbar,
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
