<script setup>

import {ref, onMounted } from 'vue';
import NavBar from './NavBar.vue'
import {fetchActiveUser} from '../serverApi';
import {fetchAllUsers, fetchAllRoles} from '../serverSecuredApiMock'

const userCard = ref({
        userName: "anonymous",
        roles: []
})

const roles = ref(["USER"])

const users = ref([]);

onMounted(() => {
    fetchActiveUser().then((data) => userCard.value = data)
    fetchAllRoles().then((data) => roles.value = data)
    fetchAllUsers().then((data) => users.value = data)
})
</script>

<template>
    <NavBar :userCard="userCard"></NavBar>
    <div>Benutzer Liste</div>
    <ul>
        <li class="card"><a href="/users/new" class="card__link">
            <div>Benutzer Erstellen</div></a></li>
        <li class="card" v-for="user in users" :key="user.userName">
            <a :href="'/users/p/' + user.userName" class="card__link">
                <div>{{ user.userName }}</div>
            </a>
        </li>
    </ul>
</template>

<style>

</style>