<script setup>
import {onMounted, ref} from 'vue'
import {Form, Field, ErrorMessage} from 'vee-validate'
import {fetchActiveUser} from '../serverApi'
import {sendUser, fetchAllRoles} from '../serverSecuredApiMock'
import NavBar from "./NavBar.vue"
import * as yup from 'yup'

//TODO load this information from data tag
const user = ref({
    userName : "",
    password : "",
    email: "",
    roles: [],
    enabled: false
})

const currentUserCard = ref({userName: "", roles: []})

const prevUserName = ref("")

const newPassword = ref("")

const isNewUser = ref(true)

const availableRols = ref([])

onMounted(() => {
    const value = document.getElementById("app").getAttribute("data-init")        
    if (value.length > 0) {
        user.value = JSON.parse(document.getElementById("app").getAttribute("data-init"));
        prevUserName.value = user.value.userName
        isNewUser.value = false;
    }
    fetchAllRoles()
    .then(data => {
        availableRols.value = data
    })
    
    fetchActiveUser()
    .then(data => { 
      currentUserCard.value = data})
})


const emailRule = yup.string().email();

const nameRule = yup.string().matches(/[a-zA-Z0-9]/);

async function saveChanges() {
    const userDataUpdateRequest = {
        userName : user.value.userName,
        email: user.value.email,
        roles: user.value.roles,
        enabled: user.value.enabled,
        password: isNewUser.value ? user.value.password : null
    }

    const name = isNewUser.value ? userDataUpdateRequest.userName : prevUserName.value

    const response = await sendUser(userDataUpdateRequest, name)
    if (response.status == 201){
        window.location.href = response.headers.get("Location");
    } else if (!response.ok) {
        //todo display error
    }
}

async function changePassword() {
    const userDataUpdateRequest = {
        password: newPassword
    }
    const response = await sendUser(userDataUpdateRequest, prevUserName.value)
    if (response.status == 201){
        window.location.href = response.headers.get("Location");
    } else if (!response.ok) {
        //todo display error
    }
}

</script>


<template>
    <NavBar :userCard="currentUserCard"></NavBar>
    <div class="card">
    <Form @submit="saveChanges" class="single-column">
        <div class="large-text">Benutzer Informationen</div>
        <label for="user-name" class="label">User Name</label>
        <Field name="user-name" id="user-name" v-model='user.userName' :rules="nameRule" class="borderless-field large-text"/>
        <ErrorMessage as='div' name='user-name' class='note error'/>
        <label for="email" class="label">Email</label>
        <Field name="email" v-model="user.email" :rules="emailRule" class="borderless-field large-text"/>
        <ErrorMessage as="div" name='email' class="note error"/>
        <Field type="checkbox" id="user-enabled" name="enabled" v-model="user.enabled" class="round-corners inline"/>
        <label for="user-enabled" class="label inline">Enabled</label>

        <table class="item-table">
        <tr v-for="role,i in user.roles" :key="i">
            <td class="row-label">{{role}}</td>
            <td>
                <button type="button" @click="removeRole(i)" class="round-corners control">
                    <i class="icon-remove"></i>
                </button>
            </td>
        </tr>
        <tr>
            <select>
                <option v-for="role,i in availableRols" :key="i">
                    {{ role }}
                </option>
            </select>
            <button type="button" @click="addRole(i)" class="round-corners control">
                Hinzufügen
            </button>
        </tr>
        </table>

        <Field v-if="isNewUser" name="password" id="password" v-model="user.password" placeholder="Neues Passwort" class="borderless-field large-text inline"/>
        <button class="round-corners control">Speichern</button>
    </Form>
    </div>
    <div class="card" v-if="!isNewUser">
        <Form @submit="changePassword" class="single-column">
            <Field name="password" id="password" v-model="newPassword" placeholder="Neues Passwort" class="borderless-field large-text inline"/>
            <button class="round-corners control inline">Password Ändern</button>
        </Form>
    </div>
</template>

<style>
.single-column {
    columns: 1;
    margin: 0 8px 0 8px;
}

.single-column > * {
    display: block;
}

.single-column > .inline {
    display: inline-block;
    vertical-align: middle
}

.label {
  padding: 2px 1rem 2px 1rem
}

.small-font {
    font-size: 0.7em;
}

</style>