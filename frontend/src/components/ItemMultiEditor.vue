<script setup>
import ItemSelector from "./ItemSelector.vue"
import MessageOverlay from "./MessageOverlay.vue";
import {onMounted, ref, reactive} from "vue"
import NavBar from "./NavBar.vue";
import { fetchActiveUser, placeholderUserCard } from "@/serverApi";
import { fetchAllItems } from "@/serverSecuredApi";


const searchString =  ref("")

const availableItems = ref(new Map())
const modifiedItems = reactive(new Array())
const modalMessages = reactive(new Array())

const currentUserCard = ref(placeholderUserCard())

function updateItemName(event, i) {
    var item = modifiedItems[i]
    item.name = event.target.value
}

function updateItemFlag(event, i){
    var item = modifiedItems[i]
    item.isBaseIngredient = event.target.checked
}

function createNewItem(item){
    item = {
        name : "new item",
        id : null,
        isBaseIngredient : true
    }
    modifiedItems.push(item)
}

function itemAdd(item) {
    modifiedItems.push(item)
    availableItems.value.delete(item.id)
}

function saveChanges(){
    const putItemChangesUri = "/api/item/modified"
    //const csrf_token = document.querySelector('meta[name="_csrf"]').content
    
    //const changesSubmission = Array.from(modifiedItems, ([key, value]) =>  value )
    //TODO add newly created item to changesSubmission

    fetch(putItemChangesUri, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            //"X-CSRF-TOKEN": csrf_token
        },
        body: JSON.stringify(modifiedItems), // body data type must match "Content-Type" header
    })
    .then(response => {
        if (response.status != 200){
            return Promise.reject("Item-Modifikationanfrage fehlgeschlagen!\nServer-Antwort: " + response.status)
        }
    })
    .catch(error => modalMessages.push(error))
}

onMounted(()=> {
        fetchAllItems()
        .then(data => { 
            availableItems.value = new Map(data.map(rsp => [rsp.id, rsp]))
        })
        .catch(error => console.error('Unable to get items.', error)); //TODO replace this with an error message for the user 
        
        fetchActiveUser().then(data => {currentUserCard.value = data})
    })

</script>

<template>
    <NavBar :userCard="currentUserCard"></NavBar>
    <div class="panel item-search item-search--wide">
        <MessageOverlay v-model="modalMessages"></MessageOverlay>
        <div class="item-search__header_box">
            <div class="panel__item-search_label">Item Auswahl</div>
            <input type='text' v-model="searchString" placeholder="Suche" class="borderless-field largetext panel__item-search panel__item-search--float-start">
        </div>
        <div class="item-selector">
            <div class="item-selector__selection">
                <ItemSelector :searchString="searchString" :item-map="availableItems" @item-selected="itemAdd"></ItemSelector>
            </div>
        </div>
    </div>    
    <div class="panel">
        <button @click="saveChanges" class="round-corners critical">Änderungen Speichern</button>
        <div class="item-grid">
            <template v-for="item,i in modifiedItems" :key="item.id">
                <div class="card item-edit-box">
                    <div v-if="item.id == null" class="item-edit-box__header"> New Item </div>
                    <div v-else class="item-edit-box__header"> Item {{ item.id }}</div>
                    <label v-bind:for="'item-name-'+i">Name:</label>
                    <input type="text" @change="updateItemName($event, i)" v-bind:id="'item-name-'+i" :value="item.name" class="borderless-field">
                    <label v-bind:for="'item-base-'+i">Basis Zutat:</label>
                    <input type="checkbox" @change="updateItemFlag($event, i)" v-bind:id="'item-base-'+i" :checked="item.isBaseIngredient">
                </div>
            </template>
            <button @click="createNewItem" class="round-corners control">Neues Item</button>
        </div>
    </div>
</template>

<style>
.panel .panel__item-search.panel__item-search--float-start {
    float: inline-start
}

.item-search.item-search--wide {
  width: auto;
}


.item-edit-box {
    display: grid;
    grid-template: 
    "t t" auto
    "l e" auto / 1fr 1fr;
    width: 20rem;
}

.item-edit-box .item-edit-box__header {
    grid-area: t;
    color: var(--color-text-faded)
}


.item-grid {
    display: flex;
    flex-flow: row wrap; 
}

</style>