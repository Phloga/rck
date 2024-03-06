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
    <div class="panel">
        <div class="large-text">Benutzer Liste</div>
        <ul>
            <li class="card" v-for="user in users" :key="user.userName">
                <a :href="'/users/p/' + user.userName">
                    <div>{{ user.userName }}</div>
                </a>
            </li>

            <a href="/users/new" class="card-body inline">
                <div class="card">
                    <i class="icon-add"></i>
                </div>
            </a>
        </ul>
    </div>
</template>

<style>

.faded-text {
    color: var(--color-text-faded)
}

.card-body {
    display: flex;
    justify-content: flex-start;
    align-items: center;
}

.icon-add {
    filter: invert(99%) sepia(0%) saturate(7457%) hue-rotate(197deg) brightness(121%) contrast(80%);
}

.inline > * {
    display: inline-block
}

.panel {
    padding: 4px
}

.panel>.large-text {
    margin: 8px;
    padding: 4px;
}

.panel .card {
    margin: 8px;
    padding: 4px;
}

</style>