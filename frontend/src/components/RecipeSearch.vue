<script setup>
//import RecipeSearchResults from "./RecipeSearchResults.vue"
import ItemSelector from "./ItemSelector.vue"
import RecipeSearchResults from "./RecipeSearchResults.vue"
import NavBar from "./NavBar.vue";
import { ref, onMounted } from "vue";
import {fetchActiveUser, fetchCommonIngredients, findByIngredients} from "../serverApi"

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
    //let query = {itemIds: items}

    findByIngredients(items)
        .then(data => {
            queryResults.value = data
        })
        .catch(error => console.error('Unable to get items.', error)) //TODO replace this with an error message for the user
}

onMounted(() => {
    //responseJson = [{"id":1,"name":"Zucker","isBaseIngredient":true},{"id":2,"name":"Salz","isBaseIngredient":true},{"id":3,"name":"Milch","isBaseIngredient":true},{"id":4,"name":"Mehl","isBaseIngredient":true},{"id":5,"name":"Butter","isBaseIngredient":true},{"id":6,"name":"Wasser","isBaseIngredient":true},{"id":7,"name":"Tomate","isBaseIngredient":true},{"id":8,"name":"Eier","isBaseIngredient":true},{"id":9,"name":"Zwiebel","isBaseIngredient":true},{"id":10,"name":"Karotte","isBaseIngredient":true},{"id":11,"name":"Knoblauch","isBaseIngredient":true},{"id":12,"name":"Wasabi","isBaseIngredient":true},{"id":13,"name":"Sonnenblumen Öl","isBaseIngredient":true},{"id":14,"name":"Tomatenmark","isBaseIngredient":true},{"id":15,"name":"Tomatenstücke","isBaseIngredient":true},{"id":16,"name":"Lorbeerblätter","isBaseIngredient":true},{"id":17,"name":"Italienische Kräuter","isBaseIngredient":true},{"id":18,"name":"Spaghetti","isBaseIngredient":true},{"id":19,"name":"Staudensellerie","isBaseIngredient":true},{"id":20,"name":"Parmesan","isBaseIngredient":true},{"id":21,"name":"Rinderhackfleisch","isBaseIngredient":true},{"id":22,"name":"Banane","isBaseIngredient":true},{"id":23,"name":"Backpulver","isBaseIngredient":true},{"id":24,"name":"Kakao","isBaseIngredient":true},{"id":25,"name":"Vanille Zucker","isBaseIngredient":true},{"id":26,"name":"Puderzucker","isBaseIngredient":true}]
    fetchCommonIngredients()
    .then(data => { 
        availableItems.value = new Map(data.map(rsp => [rsp.id, rsp]))
    })
    .catch(error => console.error('Unable to get items.', error)); //TODO replace this with an error message for the user 

    fetchActiveUser()
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
    <main class="responsive-row">
        <div class="panel item-search">
            <div class="item-search__header_box">
                <div class="panel__item-search_label">Zutaten Auswahl</div>
                <input type='text' v-model="searchString" class="borderless-field largetext panel__item-search" placeholder="Suche">
            </div>
            <div class="item-selector">
                <div class="item-selector__selection">
                    <ItemSelector :itemMap="availableItems" :searchString="searchString" @itemSelected="itemAdd"></ItemSelector>
                </div>
            </div>
            <div class="item-selector">
                <div>Ausgewählte Zutaten</div>
                <div v-if="selectedItems.size != 0" class="item-selector__selection">
                    <ItemSelector v-bind:itemMap="selectedItems" searchString="" @itemSelected="itemRemove"></ItemSelector>
                </div>
                <div v-if="selectedItems.size === 0" class="item-selector__no-items-message">keine</div>
            </div>
            <div class="item-search__footer_box">
                <button @click="search" class="round-corners control">Suche</button>
            </div>
        </div>
        <div class="panel item-search-results">
            <RecipeSearchResults :results="queryResults"></RecipeSearchResults>
        </div>
    </main>
</template>

<style>

.item-search__header_box {
    background: var(--color-bg-6);
    display: inline-block;
    margin: 0.3rem 1rem 0.3rem 1rem;
    padding: 0.3rem;
    box-shadow: 0 1px 3px 0 rgb(0, 0, 0), 0 1px 2px -1px rgb(0,0,0);
}

.item-search__footer_box {
    background: var(--color-bg-6);
    margin: 0.3rem 1rem 0.3rem 1rem;
    padding: 0.3rem;
    box-shadow: 0 1px 3px 0 rgb(0, 0, 0), 0 1px 2px -1px rgb(0,0,0);
}


.item-search {
    width: 45rem;
    align-self: flex-start;
    flex-flow: column nowrap;
    display: flex;
}

.item-search-results {
    width: 45rem;
    display: grid;
    grid-template-columns: 1fr 1fr;
}


.item-selector {
    background: var(--color-bg-6);
    color: var(--color-text);
    list-style-type: none;
    margin: 0.3rem 1rem 0.3rem 1rem;
    padding: 0.3rem;
    box-shadow: 0 1px 3px 0 rgb(0, 0, 0), 0 1px 2px -1px rgb(0,0,0);
    flex-flow: row nowrap;
}

.item-selector > * {
    display: block;
}

.item-selector__selection {
    display: inline-block;
    overflow-y: scroll;
    height: 6rem;
    width: 100%;
}


.item-selector__no-items-message {
    font-size: large;
    text-align: center;
    height: 6rem;
    margin: 3px;
    width: 100%;
    display: block;
}


.panel .panel__item-search_label {
    float: inline-start;
}

.panel .panel__item-search {
    margin: 0.3rem 1rem 0.3rem 1rem;
    float: inline-end;
}
</style>