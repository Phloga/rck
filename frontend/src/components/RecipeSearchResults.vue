<script setup>
import {defineProps} from "vue"

const props = defineProps(["results"])

</script>
<template>
    <div v-for="result,i in results" :key="i">
        <a :href="'/recipe/d/' + result.recipeId">
            <img src="/assets/img/placeholder-preview.jpg">
            <div>{{result.recipeName}}</div>
            <div>{{result.ingredientMatches}}/{{result.ingredientCount}}</div>
        </a>
    </div>
</template>