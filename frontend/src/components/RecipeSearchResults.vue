<script setup>
import {defineProps} from "vue"

const props = defineProps(["results"])

</script>
<template>
    <template v-for="result,i in results" :key="i">
        <a :href="'/recipe/d/' + result.recipeId" class="card card--recipe">
            <img src="/assets/img/placeholder-preview.jpg" class="card__recipe_image">
            <div class="recipe_info_box">
                <div class="recipe_info_box__key_value_pair">
                    <div>Übereinstimmende Zutaten:</div>
                    <div>{{result.ingredientMatches}}/{{result.ingredientCount}}</div>
                </div>
            </div>
            <div class="card__title">{{result.recipeName}}</div>
        </a>
    </template>
</template>


<style>

.recipe_info_box {
    display: flex;
    flex-flow: row wrap;
    justify-content: flex-start;
}

.recipe_info_box__key_value_pair {
    display: flex;
    flex-flow: row nowrap;
    justify-content: flex-start;
    padding: 0 1rem 0 0;
}

.recipe_info_box__key_value_pair > div:nth-child(1){
    color: var(--color-text-faded);
    padding: 0 1rem 0 0;
}

.recipe_info_box__key_value_pair > div:nth-child(2){
    color: var(--color-text-lightly-faded)
}

.recipe_info_box > div {
    margin-right: 1rem;
}

.card__title {
    font-size: large;
}

.card__recipe_image {
    width: 100%
}

.card--recipe {
    display: flex;
    flex-flow: column nowrap;
}


</style>