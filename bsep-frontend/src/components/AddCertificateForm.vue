<template>
  
    <div class="d-flex p-2 justify-content-center">
      <form accept-charset="UTF-8" class="d-flex flex-column col-sm-4">
        <h1 class="p-2">Issue new certificate</h1>

        <!-- Na bekendu izvršiti proveru da li je issuer = subject, jer ako je, onda je self-signed certificate -->
        <!-- Ako je issuer != subject && type = CA, onda smestiti cert u intermediate.jks -->
        <!-- Ako je issuer != subject && type = CA, onda smestiti cert u end-entity.jks -->
        <h3 clas="p-2">Certificate type</h3>
        <select class="p-2" name="type" id="type" v-model="type">
          <option value="CA">Certificate Authority</option>
          <option value="END_ENTITY">End-user</option>
        </select>

        <br><br>
        <h3 class="p-2">Subject</h3>
        <select class="p-2" name="type" id="type" v-model="subject">
          <option v-for="subject in subjects" v-bind:key="subject.id" v-bind:value="subject">
            {{subject | fullName}}
          </option>
        </select>
        
        <br><br>
        <h3 class="p-2">Issuer certificate</h3>
        <select class="p-2" name="type" id="type" v-model="issuer">
          <option v-for="certificate in issuerCertificates" v-bind:key="certificate.id" v-bind:value="certificate">
            Certificate {{certificate.alias}} ({{certificate.certificateRole}} - signed by {{certificate.firstNameIssuer}} {{certificate.lastNameIssuer}})
          </option>
        </select>

        <br><br>
        <h3 clas="p-2">Date</h3>
        <label class="p-2">Valid from</label>
        <input type="date" class="p-2" id="validFrom" v-model="validFrom" min="new Date()">
        <label class="p-2">Valid to</label>
        <input type="date" class="p-2" id="validTo" v-model="validTo">

        <br><br>
        <h3 class="p-2">Keystore Password</h3>
        <input type="text" class="p-2" id="jksPassword" v-model="jksPassword">

        <br><br>
        <button type="submit" class="btn btn-primary" v-on:click.prevent="issue">Confirm</button>
      </form>
    </div>

</template>

<script>
import axios from "axios";

export default {
  name: "AddCertificateForm",
  props:[],
  data () {
    return {
      subjects: [],
      issuerCertificates: [],

      type: undefined,
      subject: undefined,
      issuer: undefined,
      validFrom: undefined,
      validTo: undefined,
      jksPassword: undefined,

      email: undefined
    }
  },
  mounted(){
    var token = JSON.parse(localStorage.getItem('userInfo')).accessToken;
    
    axios
      .get("https://localhost:8443/api/user", {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => {
        this.subjects = response.data;
      })
      .catch(error => {
        alert(error.response.data);
      });
    
    axios
      .get("https://localhost:8443/api/user/getEmailFromJwtToken", {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => {
        this.email = response.data;

        axios
          .get("https://localhost:8443/api/certificate/CAorIntermediate/" + this.email.text, {
            headers: { 
              'Authorization': `Bearer ${token}`
            }
          })
          .then(response => {
            this.issuerCertificates = response.data
          })
          .catch(error => {
            console.log(error);
          });
      })
      .catch(error => {
        //alert(error.response.data);
        console.log(error);
      });

    document.getElementById('validFrom').setAttribute('min', new Date().toISOString().split("T")[0]);
    document.getElementById('validTo').setAttribute('min', new Date().toISOString().split("T")[0]);
  },
  methods: {
    issue() {
      if (this.isFormValid()) {
        // alert("sve je ok!");

        var certificateData = {
          "id": this.issuer.id,
          "subjectID" : this.subject.id,
          "certificateType" : this.type,
          "keyStorePassword" : this.jksPassword,
          "startDate" : this.validFrom,
          "endDate" : this.validTo
        };

        // console.log(certificateData);
        // console.log("issuer: " + this.issuer);
        // console.log("subject: " + this.subject);
        
        var token = JSON.parse(localStorage.getItem('userInfo')).accessToken;

        axios
          .post(" https://localhost:8443/api/certificate/issue", certificateData, {
            headers: {
              'Authorization': `Bearer ${token}` 
            }
          })
          .then(() => {
            alert("Success!");
            location.reload();
          })
          .catch(error => {
            alert(error.response.data);
          });
      }
    },
    isFormValid() {
      if (!this.type || !this.subject || !this.issuer || !this.validFrom || !this.validTo || !this.jksPassword) {
        alert("All fields must be filled!");
        return false;
      }

      let startDate = new Date(this.validFrom);
      let endDate = new Date(this.validTo);
      if (endDate <= startDate) {
        alert("validFrom >= validTo");
        return false;
      }

      return true;
    }
  },
  filters: {
    fullName: function(subject) {
      return subject.firstName + " " + subject.lastName;
    }
  }
};
</script>
