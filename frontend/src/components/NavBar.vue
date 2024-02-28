<script setup>

import { computed } from 'vue';


const props = defineProps({
    userCard: {
    type: Object,
        default(rawProps) {
            return { 
                userName: 'anonymous',
                roles: []
            }
        }    
    }
})

const userLink = computed(() => '/user/'+ props.userCard.userName)

</script>

<template>
    <nav class="right-aligned nav-list">
        <a v-if="userCard.roles.length > 0" href="/recipe/newRecipe">Rezept Erstellen</a>
        <a v-if="userCard.roles.includes('ROLE_ADMIN')">Users</a>
        <a v-if="userCard.roles.length > 0" :href="userLink"> {{ userCard.userName }}</a>
        <a v-if="userCard.roles.length == 0" href="/login">Login</a>
        <a v-if="userCard.roles.length > 0" href="/logout">Logout</a>
    </nav>
</template>