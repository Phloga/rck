<script setup>
import ItemSelector from "./ItemSelector.vue"
import ModalMessageBox from "./ModalMessageBox.vue"
import {onMounted, ref, reactive} from "vue"

const props = defineProps(["itemListUri"])

const searchString =  ref("")

const availableItems = ref(new Map())
const modifiedItems = reactive(new Array())
const modalMessages = reactive(new Array())


function messageDismiss(index) {
    modalMessages.splice(index,1)
}

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
    const postItemChangesUri = "/api/items/modify"
    //const csrf_token = document.querySelector('meta[name="_csrf"]').content
    
    //const changesSubmission = Array.from(modifiedItems, ([key, value]) =>  value )
    //TODO add newly created item to changesSubmission

    fetch(postItemChangesUri, {
        method: "POST",
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
        fetch(props.itemListUri)
        .then(response => response.json())
        .then(data => { 
            availableItems.value = new Map(data.map(rsp => [rsp.id, rsp]))
        })
        .catch(error => console.error('Unable to get items.', error)); //TODO replace this with an error message for the user 
    })

</script>

<template>
    <div>
        <ModalMessageBox v-for="message,i in modalMessages" v-bind:key="i" 
            @dismiss="messageDismiss" v-bind:message="message" v-bind:index="i"/>
        <input type='text' v-model="searchString">
        <ItemSelector :searchString="searchString" :item-map="availableItems" @item-selected="itemAdd"></ItemSelector>
        <button @click="saveChanges">Änderungen Speichern</button>
        <div>
            <template v-for="item,i in modifiedItems" :key="item.id">
                <div>
                    <label v-bind:for="'item-name-'+i">Name:</label><br>
                    <input type="text" @change="updateItemName($event, i)" v-bind:id="'item-name-'+i" :value="item.name">
                    <label v-bind:for="'item-base-'+i">Basis Zutat:</label><br>
                    <input type="checkbox" @change="updateItemFlag($event, i)" v-bind:id="'item-base-'+i" :checked="item.isBaseIngredient">
                </div>
            </template>
            <button @click="createNewItem">Neues Item</button>
        </div>
    </div>
</template>