<script setup>
import ItemSelector from "./ItemSelector.vue"
import UnitsDropdown from "./UnitsDropdown.vue"

import {Form,Field, ErrorMessage} from "vee-validate"

import {ref, onMounted} from 'vue'

import * as yup from 'yup'

//const itemListUri = "/api/items/allItems";
const itemListUri = "/api/items/commonIngredients";
const unitListUri = "/api/units/getAll"
const currentUserUri = "/api/user/currentUser"

const availableItems = ref(new Map())
const availableUnits = ref(new Array())
const searchString = ref("");

const recipeName = ref("");
const ingredients = ref(new Array())

const currentUserCard = ref({userName: ""})

const validateAmount = yup.number().min(0)

const validateTitle = yup.string().required();

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
  const recipeData = {
    name: recipeName,
    content: "",
    ingredients: ingredients.value,
    products: [{itemName: recipeName, isBaseIngredient : false, isOptional : false, unit:"none", amount:1.0}],
  }
  let event = new CustomEvent("saveRecipe", { bubbles: true, detail: recipeData });
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
  .catch(error => console.error('Unable to get unit list.', error)); //TODO replace this with an error message for the user 

  fetch(currentUserUri)
  .then(response => response.json())
  .then(data => { 
    currentUserCard.value = data})
  .catch(error => console.error('Unable to get user information.', error)); //TODO replace this with an error message for the user 
})


</script>

<template>
    <Form @submit="saveRecipe">
      <br>
      <div class="block">
        <Field name="recipe-title" v-model="recipeName" :rules="validateTitle" class="title borderless-field" placeholder="Titel hier eingeben"/>
        <ErrorMessage as="div" name='recipe-title' class="note error"/>
      </div><br>
      <hr class="divider">
      <h2><span class="block">Zutatenliste</span></h2>
      <div class="section-container">
        <input v-model="searchString" class="item-selector-search borderless-field largetext" placeholder="Zutaten Suche...">
        <br>
        <div class="item-selector-innerbox">
          <ItemSelector :item-map="availableItems" :searchString="searchString" :enableCreateNew="true" @itemSelected="addIngredient"/>
        </div>
      </div>
      <p class="text-centered" v-if="ingredients.length === 0">
        Keine Zutaten Ausgewählt
      </p>

      <table class="item-table">
        <tr v-for="listing,i in ingredients" :key="i">
          <td class="row-label">{{listing.item.name}}</td>
          <td>
            <Field :id='"amount-"+i' :name='"amount-"+i' type='number' :rules='validateAmount' v-model='listing.amount' class='borderless-field'/>
            <ErrorMessage as="div" :name='"amount-"+i' class="note error"/>
          </td>
          <td><UnitsDropdown v-model="listing.unit" :unitList="availableUnits"></UnitsDropdown></td>
          <td><button type="button" @click="removeIngredient(i)" class="round-corners control">
            <i class="icon-remove"></i>
          </button></td>
        </tr>
      </table>
      <nav class="bottom-bar">
        <button type='submit' class="round-corners critical z-top">Speichern</button>
      </nav>
    </Form>
</template>