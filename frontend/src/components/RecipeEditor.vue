<script setup>
import ItemSelector from "./ItemSelector.vue"
import UnitsDropdown from "./UnitsDropdown.vue"

import {Form,Field, ErrorMessage} from "vee-validate"

import {ref, onMounted, useAttrs } from 'vue'

import * as yup from 'yup'


import EditorJS from "@editorjs/editorjs"
import Header from "@editorjs/header"
import List from "@editorjs/list"
import Warning from "@editorjs/warning"
import Table from "@editorjs/table"


//const itemListUri = "/api/items/allItems";
const itemListUri = "/api/items/commonIngredients";
const unitListUri = "/api/units/getAll"
const currentUserUri = "/api/user/currentUser"


const attrs = useAttrs()

const availableItems = ref(new Map())
const remainingItems = ref(new Map())
const availableUnits = ref(new Array())
const searchString = ref("");

const recipeName = ref("");
const ingredients = ref(new Array())
const products = ref(new Array())


const currentUserCard = ref({userName: ""})

const validateAmount = yup.number().min(0)

const validateTitle = yup.string().required();

var editor

function loadRecipe(recipeObj){
  //Fix this
  recipeName.value = recipeObj.name;
  ingredients.value = recipeObj.ingredients;
  products.value = recipeObj.products;

}

function addIngredient(item){
  //TODO make this look more like listing dtos
  //TODO store removed items elsewhere
  ingredients.value.push({
    itemName: item.name,
    itemId: item.id,
    isOptional: false,
    unit: "",
    amount: 0.0
  })
  remainingItems.value.delete(item.id)
}

function removeIngredient(index){
  let element = ingredients.value[index]
  ingredients.value.splice(index,1)
  if (element.itemId == null){
    return
  }
  const item = availableItems.value.get(element.itemId)
  remainingItems.value.set(element.itemId, item)
}


async function saveRecipe(){
  //build a RecipeDetails object
  var editorData = await editor.save();
  const recipeData = {
    name: recipeName,
    content: editorData,
    ingredients: ingredients.value,
    products: [{itemName: recipeName, isBaseIngredient : false, isOptional : false, unit:"none", amount:1.0}],
  }

  //TODO send it to the server

  //let event = new CustomEvent("saveRecipe", { bubbles: true, detail: recipeData });
  //document.dispatchEvent(event);
}


onMounted(() => {
  fetch(itemListUri)
  .then(response => response.json())
  .then(data => { 
    availableItems.value = new Map(data.map(rsp => [rsp.id, rsp]))
    remainingItems.value = new Map(availableItems.value)
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
  
  /*
  let defaultValues = JSON.parse(document.getElementById("app").getAttribute("data-init")); //attrs doesn't work
  if (defaultValues != null){
    loadRecipe(defaultValues)
  }*/

  //setup wysiwyg editor
  editor = new EditorJS({
    /**
     * Enable/Disable the read only mode
     */
    readOnly: false,

    /**
     * Wrapper of Editor
     */
    holder: 'editorjs',
    minHeight: 600,
    /**
     * Common Inline Toolbar settings
     * - if true (or not specified), the order from 'tool' property will be used
     * - if an array of tool names, this order will be used
     */
    // inlineToolbar: ['link', 'marker', 'bold', 'italic'],
    // inlineToolbar: true,

    /**
     * Tools list
     */
    tools: {
      /**
       * Each Tool is a Plugin. Pass them via 'class' option with necessary settings {@link docs/tools.md}
       */
      paragraph: {
        config : {
        placeholder : "Text Hinzufügen",
        preserveBlank : false
        }
      },
      header: {
        class: Header,
        inlineToolbar: ['marker', 'link'],
        config: {
          placeholder: 'Header'
        },
        shortcut: 'CMD+SHIFT+H'
      },
      list: {
        class: List,
        inlineToolbar: true,
        shortcut: 'CMD+SHIFT+L'
      },
      warning: Warning,

      table: {
        class: Table,
        inlineToolbar: true,
        shortcut: 'CMD+ALT+T'
      },

    },

    /**
     * This Tool will be used as default
     */
    // defaultBlock: 'paragraph'
    onReady: function(){
      //saveButton.click();
    },
    onChange: function(api, event) {
      //console.log('something changed', event);
    }
  });
/*
  document.addEventListener("saveRecipe", async function(event) {
    //TODO save
    var editorData = await editor.save();
    //TODO build RecipeDetails object
    const recipe = event.detail;
    recipe.content = editorData    
  })
  */  
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
          <ItemSelector :item-map="remainingItems" :searchString="searchString" :enableCreateNew="true" @itemSelected="addIngredient"/>
        </div>
      </div>
      <p class="text-centered" v-if="ingredients.length === 0">
        Keine Zutaten Ausgewählt
      </p>

      <table class="item-table">
        <tr v-for="listing,i in ingredients" :key="i">
          <td class="row-label">{{listing.itemName}}</td>
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
    <hr class="divider">
    <h2><span class="block">Beschreibung</span></h2>
    <div class="section-container">
        <div class="editor" id="editorjs"></div>
    </div>
</template>