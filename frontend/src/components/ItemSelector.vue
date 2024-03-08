<script setup>

import {computed, ref} from "vue"

//const props = defineProps(["itemMap","searchString"]) // maps int -> object


const props = defineProps({
  itemMap: {
    type: Map,
    required: true
  },
  searchString : {
    type: String,
    default: ""
  },
  enableCreateNew : {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['itemSelected'])

const filtereditems = computed(() => {
    let newFilteredIngredients = []
    props.itemMap.forEach((item) => {
        if (item.name.toUpperCase().indexOf(props.searchString.toUpperCase()) != -1) {
            newFilteredIngredients.push(item)
        }
    })
    return newFilteredIngredients 
})

const createNewVisible = computed(() => {
    return props.enableCreateNew && filtereditems.value.length === 0
})

const resultsEmpty = computed(() => {
    return filtereditems.value.length === 0
})

const newItem = ref(makeItem())

function makeItem(){
    return {
        name: "",
        isBaseIngredient: false
    }
}

function createNewItem(){
    select(newItem.value)
    newItem.value = makeItem();
}

function select(item){
    emit('itemSelected', item)
}

</script>


<template>
    <button v-for="item in filtereditems" :key="item.id"  @click="select(item)" class="round-corners item">
        {{item.name}}
    </button>
    <div v-show="createNewVisible">
        <p>Fall die gesuchte Zutat in der Datenbank nicht vorhanden ist können diese über dieses Feld hier hinzufügen</p>
        <input v-model="newItem.name" class="round-corners item" placeholder="Name">
        <button type="button" @click="createNewItem" class="round-corners control">Hinzufügen</button>
    </div>
</template>