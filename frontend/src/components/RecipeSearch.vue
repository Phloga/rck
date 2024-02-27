<script setup>
//import RecipeSearchResults from "./RecipeSearchResults.vue"
import ItemSelector from "./ItemSelector.vue"
import RecipeSearchResults from "./RecipeSearchResults.vue"
import NavBar from "./NavBar.vue";
import { ref, onMounted } from "vue";

const initialIngredientsUri = "/api/items/commonIngredients";
const recipesFindByIngredientsUri = "/api/recipes/findByIngredients"
const currentUserUri = "/api/user/self"

const itemListUri = ref(initialIngredientsUri)

const availableItems = ref(new Map())
const selectedItems = ref(new Map())

const searchString =  ref("")
const queryResults = ref([])

const userCard = ref({userName:"", roles:[]});

function itemAdd(item) {
    selectedItems.value.set(item.id,item)
    availableItems.value.delete(item.id)
}

function itemRemove(item) {
    availableItems.value.set(item.id, item)
    selectedItems.value.delete(item.id)
}

       
function search() {
    console.log("start search")
    let items = []
    selectedItems.value.forEach((item) => {
        items.push(item.id)
    })
    let query = {itemIds: items}

    fetch(recipesFindByIngredientsUri, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(query)})
        .then(response => response.json())
        .then(data => {
            queryResults.value = data
        })
        .catch(error => console.error('Unable to get items.', error)) //TODO replace this with an error message for the user
}

onMounted(() => {
    //responseJson = [{"id":1,"name":"Zucker","isBaseIngredient":true},{"id":2,"name":"Salz","isBaseIngredient":true},{"id":3,"name":"Milch","isBaseIngredient":true},{"id":4,"name":"Mehl","isBaseIngredient":true},{"id":5,"name":"Butter","isBaseIngredient":true},{"id":6,"name":"Wasser","isBaseIngredient":true},{"id":7,"name":"Tomate","isBaseIngredient":true},{"id":8,"name":"Eier","isBaseIngredient":true},{"id":9,"name":"Zwiebel","isBaseIngredient":true},{"id":10,"name":"Karotte","isBaseIngredient":true},{"id":11,"name":"Knoblauch","isBaseIngredient":true},{"id":12,"name":"Wasabi","isBaseIngredient":true},{"id":13,"name":"Sonnenblumen Öl","isBaseIngredient":true},{"id":14,"name":"Tomatenmark","isBaseIngredient":true},{"id":15,"name":"Tomatenstücke","isBaseIngredient":true},{"id":16,"name":"Lorbeerblätter","isBaseIngredient":true},{"id":17,"name":"Italienische Kräuter","isBaseIngredient":true},{"id":18,"name":"Spaghetti","isBaseIngredient":true},{"id":19,"name":"Staudensellerie","isBaseIngredient":true},{"id":20,"name":"Parmesan","isBaseIngredient":true},{"id":21,"name":"Rinderhackfleisch","isBaseIngredient":true},{"id":22,"name":"Banane","isBaseIngredient":true},{"id":23,"name":"Backpulver","isBaseIngredient":true},{"id":24,"name":"Kakao","isBaseIngredient":true},{"id":25,"name":"Vanille Zucker","isBaseIngredient":true},{"id":26,"name":"Puderzucker","isBaseIngredient":true}]
    const uri = itemListUri.value;
    fetch(uri)
    .then(response => response.json())
    .then(data => { 
        availableItems.value = new Map(data.map(rsp => [rsp.id, rsp]))
    })
    .catch(error => console.error('Unable to get items.', error)); //TODO replace this with an error message for the user 

    fetch(currentUserUri)
    .then(response => response.json())
    .then(data => {
        userCard.value = data
    })
    .catch(error => console.error('Unable to get user Information.', error)) //TODO replace this with an error message for the user
})

</script>

<template>
    <NavBar :userCard="userCard"></NavBar>
    <header>
        <h1>Reverse Chefkoch</h1>
    </header>
    <main>
        <input type='text' v-model="searchString">
        <ItemSelector :itemMap="availableItems" :searchString="searchString" @itemSelected="itemAdd"></ItemSelector>
        <br>
        <ItemSelector v-bind:itemMap="selectedItems" searchString="" @itemSelected="itemRemove"></ItemSelector>
        <button @click="search">Suche</button>
        <RecipeSearchResults :results="queryResults"></RecipeSearchResults>
    </main>
</template>