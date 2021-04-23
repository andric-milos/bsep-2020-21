<template>

    <div class="d-flex p-2 justify-content-center">
        <form id="login" accept-charset="UTF-8" class="d-flex flex-column col-sm-4">
        <h1 class="p-2">Register</h1>
        <br/>

        <div class="d-flex justify-content-between">
            <label class="p-2">First name</label>
            <label for="text" class="p-2 err">{{firstNameMessage}}</label>
        </div>
        <input type="text" class="p-2" id="firstName" v-model="firstName"/>

        <div class="d-flex justify-content-between">
            <label class="p-2">Last name</label>
            <label for="text" class="p-2 err">{{lastNameMessage}}</label>
        </div>
        <input type="text" class="p-2" id="lastName" v-model="lastName"/>

        <div class="d-flex justify-content-between">
            <label class="p-2">Country</label>
            <label for="text" class="p-2 err">{{countryMessage}}</label>
        </div>
        <select name="country" class="p-2" id="country" v-model="country">
            <option value="Serbia">Serbia</option>
            <option value="United States">United States</option>
            <option value="United Kingdom">United Kingdom</option>
            <option value="Switzerland">Switzerland</option>
            <option value="Italy">Italy</option>
        </select>

        <div class="d-flex justify-content-between">
            <label class="p-2">City</label>
            <label for="text" class="p-2 err">{{cityMessage}}</label>
        </div>
        <input type="text" class="p-2" id="city" v-model="city"/>

        <div class="d-flex justify-content-between">
            <label class="p-2">Organization</label>
            <label for="text" class="p-2 err">{{organizationMessage}}</label>
        </div>
        <input type="text" class="p-2" id="organization" v-model="organization"/>

        <br/><br/>

        <div class="d-flex justify-content-between">
            <label for="staticEmail" class="p-2">E-mail</label>
            <label for="text" class="p-2 err">{{emailMessage}}</label>
        </div>
        <input
            type="email"
            class="p-2"
            id="email"
            v-model="email"
            placeholder="Enter email"
        />

        <div class="d-flex justify-content-between">
            <label class="p-2" for="password">Password</label>
            <label for="text" class="p-2 err">{{passwordMessage}}</label>
        </div>
        <input
            type="password"
            class="p-2"
            id="password"
            v-model="password"
            placeholder="Password"
        />
        <label style="text-align: left;"><b>Note: </b>Password must contain at least 10 characters (uppercase and lowercase letters, digits, and one of special characters: !, #, $, %)</label>

        <div class="d-flex justify-content-between">
            <label class="p-2" for="password">Confirm password</label>
        </div>
        <input
            type="password"
            class="p-2"
            id="confirmPassword"
            v-model="confirmPassword"
            placeholder="Confirm password"
        />
        <label class="p-2" for="text" id="matching">{{matching}}</label>

        <br />
        <button class="btn btn-primary p-2" v-on:click.prevent="register">Submit</button>
        </form>
    </div>

</template>

<script>
    export default  {
        name: 'RegisterForm',
        props: [],
        data () {
        return {
            firstName: undefined,
            lastName: undefined,
            country: undefined,
            city: undefined,
            organization: undefined,
            email: undefined,
            password: undefined,
            confirmPassword: undefined,

            firstNameMessage: "",
            lastNameMessage: "",
            countryMessage: "",
            cityMessage: "",
            organizationMessage: "",
            emailMessage: "",
            passwordMessage: "",
            matching: "",
            
            fnErr: true,
            lnErr: true,
            cntryErr: true,
            cityErr: true,
            orgErr: true,
            emErr: true,
            pwErr: true,
            pwMErr: true
        }
        },
        methods: {
            register: function() {
                let thereAreEmptyFields = this.areFieldsEmpty();
                let passwordIsValid = this.isPasswordValid();
                let emailIsValid = this.isEmailValid();

                if (thereAreEmptyFields || !passwordIsValid || !emailIsValid)
                    alert("ne valja");
                else
                    alert("ok je");
            },
            areFieldsEmpty: function() {
                let firstNameInput = document.getElementById("firstName");
                let lastNameInput = document.getElementById("lastName");
                let countryInput = document.getElementById("country");
                let cityInput = document.getElementById("city");
                let organizationInput = document.getElementById("organization");
                //let emailInput = document.getElementById("email");
                //let passwordInput = document.getElementById("password");

                let errorCounter = 0;

                if (!this.firstName) {
                    this.firstNameMessage = "First name cannot be empty!";
                    firstNameInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.firstNameMessage = "";
                    firstNameInput.style.border = null;
                }

                if (!this.lastName) {
                    this.lastNameMessage = "Last name cannot be empty!";
                    lastNameInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.lastNameMessage = "";
                    lastNameInput.style.border = null;
                }

                if (!this.country) {
                    this.countryMessage = "Country cannot be empty!";
                    countryInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.countryMessage = "";
                    countryInput.style.border = null;
                }

                if (!this.city) {
                    this.cityMessage = "City cannot be empty!";
                    cityInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.cityMessage = "";
                    cityInput.style.border = null;
                }

                if (!this.organization) {
                    this.organizationMessage = "Organization cannot be empty!";
                    organizationInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.organizationMessage = "";
                    organizationInput.style.border = null;
                }

                /*
                if (!this.email) {
                    this.emailMessage = "Email cannot be empty!";
                    emailInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.emailMessage = "";
                    emailInput.style.border = null;
                }

                if (!this.password) {
                    this.passwordMessage = "Password cannot be empty!";
                    passwordInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.passwordMessage = "";
                    passwordInput.style.border = null;
                }

                if (!this.confirmPassword) {
                    passwordInput.style.border = '2px solid red';
                    ++errorCounter;
                } else {
                    this.passwordMessage = "";
                    passwordInput.style.border = null;
                }
                */

                if (errorCounter)
                    return true;
                else
                    return false;
            },
            isPasswordValid: function() {
                let passwordInput = document.getElementById("password");
                let confirmPasswordInput = document.getElementById("confirmPassword");

                // password field is empty
                if (!this.password) {
                    this.matching = "Password cannot be empty!";
                    passwordInput.style.border = '2px solid red';
                    confirmPasswordInput.style.border = '2px solid red';
                    return false;
                }

                // TODO: pasword doesn't match the requirments
                const regex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%&]).{10,}/;
                if (!this.password.match(regex)) {
                    this.matching = "Password does not match the requirements!";
                    passwordInput.style.border = '2px solid red';
                    confirmPasswordInput.style.border = '2px solid red';
                    return false;
                }

                // password input isn't empty, but input for confirming password is
                if (this.password && !this.confirmPassword) {
                    this.matching = "You must confirm your password!";
                    passwordInput.style.border = '2px solid red';
                    confirmPasswordInput.style.border = '2px solid red';
                    return false;
                }

                // values inside of inputs for password and confirming password don't match
                if (this.password != this.confirmPassword) {
                    this.matching = "Passwords aren't matching!";
                    passwordInput.style.border = '2px solid red';
                    confirmPasswordInput.style.border = '2px solid red';
                    return false;
                }

                this.matching = "";
                passwordInput.style.border = null;
                confirmPasswordInput.style.border = null;
                return true;
            },
            isEmailValid: function() {
                let emailInput = document.getElementById("email");

                // email input field is empty
                if (!this.email) {
                    this.emailMessage = "Email cannot be empty!";
                    emailInput.style.border = '2px solid red';
                    return false;
                }

                // email is not valid
                const regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                if (!this.email.match(regex)) {
                   this.emailMessage = "Email is not valid!";
                    emailInput.style.border = '2px solid red';
                    return false;
                }

                // valid
                this.emailMessage = "";
                emailInput.style.border = null;
                return true;
            }
        }
    }

</script>

<style scoped>
#matching, .err {
  color: red;
}
</style>
