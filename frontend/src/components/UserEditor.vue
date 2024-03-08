<script setup>
import {onMounted, ref, computed} from 'vue'
import {Form, Field, ErrorMessage} from 'vee-validate'
import {fetchActiveUser} from '../serverApi'
import {sendUser, fetchAllRoles} from '../serverSecuredApi'
import NavBar from "./NavBar.vue"
import * as yup from 'yup'

const rolePrefix = "ROLE_"

const userName = ref("")
const userEmail = ref("")
const userRoles = ref(new Set())
userRoles.value.add("USER")
const userEnabled = ref(false)

const userRoleSelection = ref(null);

const currentUserCard = ref({userName: "", roles: []})

const prevUserName = ref("")

const newPassword = ref("")

const isNewUser = ref(true)

const availableRoles = ref([])

const isAdmin = computed(() => {
    return userRoles.value.has("ADMIN")
})

onMounted(() => {
    const value = document.getElementById("app").getAttribute("data-init")        
    if (value.length > 0) {
        const user = JSON.parse(document.getElementById("app").getAttribute("data-init"));
        prevUserName.value = user.userName
        isNewUser.value = false;
        userName.value = user.userName
        userEmail.value = user.email
        userEnabled.value = user.enabled
        userRoles.value = new Set(user.roles.map(authName => authName.substring(rolePrefix.length)))
    }
    fetchAllRoles()
    .then(data => {
        availableRoles.value = data.map(authName => authName.substring(rolePrefix.length))
    })
    
    fetchActiveUser()
    .then(data => { 
      currentUserCard.value = data})
})


const emailRule = yup.string().email();

const nameRule = yup.string().matches(/[a-zA-Z0-9]/);

function removeRole(role){
    userRoles.value.delete(role)
}

function addRole(){
    userRoles.value.add(availableRoles.value[userRoleSelection.value.selectedIndex])
}


async function saveChanges() {
    const userDataUpdateRequest = {
        userName : userName.value,
        email: userEmail.value,
        roles: Array.from(userRoles.value).map(roleName => rolePrefix + roleName),
        enabled: userEnabled.value,
        password: isNewUser.value ? newPassword.value : null
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
        password: newPassword.value
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
    <div class="panel">
        <div class="large-text panel__title">Benutzer Informationen Bearbeiten</div>
        <Form @submit="saveChanges">
            <div class="panel_body--flex">
                <div class="card single-column">
                    <label for="user-name" class="label">User Name</label>
                    <Field name="user-name" id="user-name" v-model='userName' :rules="nameRule" class="borderless-field large-text"/>
                    <ErrorMessage as='div' name='user-name' class='note error'/>
                    <label for="email" class="label">Email</label>
                    <Field name="email" v-model="userEmail" :rules="emailRule" class="borderless-field large-text"/>
                    <ErrorMessage as="div" name='email' class="note error"/>
                    <input type="checkbox" id="user-enabled" name="enabled" v-model="userEnabled" class="round-corners inline"/>
                    <label for="user-enabled" class="label inline">Enabled</label>
                </div>
                <div class="card">
                    <div>Rollen</div>
                    <table v-if="isAdmin" class="item-table">
                        <tr v-for="role,i in userRoles" :key="i" class="role_card">
                            <td class="row-label">{{role}}</td>
                            <td>
                                <button type="button" @click="removeRole(role)" class="round-corners control">
                                <i class="icon-remove"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <select ref="userRoleSelection" class="round-corners">
                                <option v-for="role in availableRoles" :key="role">{{ role }}</option>
                            </select>
                            <button type="button" @click="addRole()" class="round-corners control">Hinzufügen</button>
                        </tr>
                    </table>
                    <table v-else class="item-table">
                        <tr v-for="role,i in userRoles" :key="i" class="role_card">
                            <td class="row-label">{{role}}</td>
                        </tr>
                    </table>

                </div>

                <Field v-if="isNewUser" name="password" id="password" v-model="newPassword" placeholder="Neues Passwort" class="borderless-field large-text inline"/>

            
            </div>
            <div class="panel_footer">
                <button class="round-corners control">Speichern</button>
            </div>
        </Form>
    </div>


    <div class="panel" v-if="!isNewUser">
        <Form @submit="changePassword" class="single-column">
            <div class="large-text">Passwort</div>
            <Field name="password" id="password" v-model="newPassword" placeholder="Neues Passwort" class="borderless-field large-text inline"/>
            <button class="round-corners control inline">Password Ändern</button>
        </Form>
    </div>
</template>

<style>


.panel_body--flex {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-items: flex-start;
}

.role_card {
    background: var(--color-bg-4);
    margin: 8px;
    padding: 4px;
    box-shadow: 0 1px 3px 0 rgb(0, 0, 0), 0 1px 2px -1px rgb(0,0,0);
}

.panel_footer {
    background: var(--color-bg-3);

}

.panel .card {
    background: var(--color-bg-6);
    margin: 8px;
    padding: 4px;
}

.panel button {
    margin: 2px 8px 2px 8px;
} 

.panel__title {
    margin: 2px 8px 2px 8px;
}

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