<template>
  <div class="all-certificates">
    <user-navbar></user-navbar>
    <table class="table p-2 mt-2 ml-2 mr-4 table-dark table-striped table-hover">
      <thead>
        <tr>
          <th scope="col">Serial number</th>
          <th scope="col">Subject</th>
          <th scope="col">Issued by</th>
          <th scope="col">Not before</th>
          <th scope="col">Not after</th>
          <th scope="col">Public key</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>1111</td>
          <td>Pera</td>
          <td>Pera</td>
          <td>01.01.2021.</td>
          <td>01.01.2022.</td>
          <td>jkasjkfhasf</td>
        </tr>
        <tr>
          <td>1112</td>
          <td>Mika</td>
          <td>Pera</td>
          <td>01.01.2018.</td>
          <td>01.01.2025.</td>
          <td>aafasfasfaf</td>
        </tr>
        <tr>
          <td>1113</td>
          <td>Mara</td>
          <td>Mika</td>
          <td>01.01.2021.</td>
          <td>01.01.2022.</td>
          <td>dghfghfgfgh</td>
        </tr>
        <tr>
          <td>1114</td>
          <td>Dara</td>
          <td>Pera</td>
          <td>01.01.2021.</td>
          <td>01.01.2022.</td>
          <td>qwesdgdsggd</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import UserNavbar from '../../components/navbars/UserNavbar.vue';

  export default  {
    components: { UserNavbar },
    name: "UserCertificates",
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
