<script setup>
import ItemSelector from "./ItemSelector.vue"
import UnitsDropdown from "./UnitsDropdown.vue"

import {ref, onMounted} from 'vue'

//const itemListUri = "/api/items/allItems";
const itemListUri = "/api/items/commonIngredients";
const unitListUri = "/api/units/getAll"

const availableItems = ref(new Map())
const availableUnits = ref(new Array())
const searchString = ref("");

const ingredients = ref(new Array())


function addIngredient(item){
  ingredients.value.push({
    item: item,
    unit: "",
    amount: 0.0
  })
  availableItems.value.delete(item.id)
}

function removeIngredient(index){
  let element = ingredients.value[index]
  ingredients.value.splice(index,1)
  if (element.item.id == null){
    return
  }
  availableItems.value.set(element.item.id, element.item)
}


function saveRecipe(){
  let event = new CustomEvent("saveRecipe", { bubbles: true, detail: {} });
  document.dispatchEvent(event);
}


onMounted(() => {
  fetch(itemListUri)
  .then(response => response.json())
  .then(data => { 
    availableItems.value = new Map(data.map(rsp => [rsp.id, rsp]))
  })
  .catch(error => console.error('Unable to get items.', error)); //TODO replace this with an error message for the user 

  fetch(unitListUri)
  .then(response => response.json())
  .then(data => { 
    availableUnits.value = data})
  .catch(error => console.error('Unable to get items.', error)); //TODO replace this with an error message for the user 
})

</script>

<template>
    <ItemSelector :item-map="availableItems" :search-string="searchString" @itemSelected="addIngredient"/>
    <button @click="saveRecipe">Speichern</button>
    <table>
      <tr v-for="listing,i in ingredients" :key="i">
        <td>{{listing.item.name}}</td>
        <td><input v-model=listing.amount></td>
        <td><UnitsDropdown v-model="listing.unit" :unitList="availableUnits"></UnitsDropdown></td>
        <td><button @click="removeIngredient(i)">Entfernen</button></td>
      </tr>
    </table>
</template>