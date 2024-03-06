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

const userLink = computed(() => '/users/u/'+ props.userCard.userName)

</script>

<template>
    <nav class="nav-list">
        <a v-if="userCard.roles.length > 0" href="/recipe/new">Rezept Erstellen</a>
        <div v-if="userCard.roles.includes('ROLE_ADMIN')" href="#" class="dropdown">
            <button>Admin Tools</button>
            <div class="dropdown__content">
                <a href="/users/index">Users</a>
                <a href="/items/index">Items</a>
            </div>
        </div>
        <a v-if="userCard.roles.length > 0" :href="userLink"> {{ userCard.userName }}</a>
        <a v-if="userCard.roles.length == 0" href="/login">Login</a>
        <a v-if="userCard.roles.length > 0" href="/logout">Logout</a>
    </nav>
</template>

<style>

.nav-list {
  padding-top: 10px;
  overflow: hidden;
}

.nav-list > * {
  padding: 0.3rem 0.3rem 0.3rem 0.3rem;
  background: var(--color-bg-4);
  color: inherit;
  float: right;
  text-align: center;
}

.nav-list a {
    text-decoration: none;
}

.dropdown {
    overflow: hidden;
}

.dropdown button {
  font-size: 1rem;
  border: none;
  outline: none;
  color: var(--color-text);
  background-color: inherit;
  font-family: inherit; /* Important for vertical align on mobile phones */
  margin: 0; /* Important for vertical align on mobile phones */
}

.dropdown__content {
    float: none;
    position: absolute;
    background-color: var(--color-bg-5);
    color: var(--color-text);
    display: none;
    width: 10em;
    z-index: 1;
}

.dropdown__content a {
    float: none;
    color: var(--color-text);
    display: block;
    text-align: left;
}



.dropdown:hover .dropdown__content{
    display: block;
}


</style>