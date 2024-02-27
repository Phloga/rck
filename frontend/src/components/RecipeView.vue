<script setup>
import ItemSelector from "./ItemSelector.vue"
import UnitsDropdown from "./UnitsDropdown.vue"
import NavBar from "./NavBar.vue"

import {Form,Field, ErrorMessage} from "vee-validate"

import {ref, onMounted, useAttrs } from 'vue'

import * as yup from 'yup'


import EditorJS from "@editorjs/editorjs"
import Header from "@editorjs/header"
import List from "@editorjs/list"
import Warning from "@editorjs/warning"
import Table from "@editorjs/table"


const unitListUri = "/api/units/getAll"
const currentUserUri = "/api/user/self"

const availableItems = ref(new Map())
const remainingItems = ref(new Map())
const availableUnits = ref(new Array())
const searchString = ref("");

const recipeName = ref("");
const ingredients = ref(new Array())
const products = ref(new Array())

const currentUserCard = ref({userName: "", roles: []})

const validateAmount = yup.number().min(0)

const validateTitle = yup.string().required();

var editor

function setupEditor(recipeObj){
  if (recipeObj != null){
    recipeName.value = recipeObj.name;
    ingredients.value = recipeObj.ingredients;
    products.value = recipeObj.products;
  }

  editor = new EditorJS({
    readOnly: true,
    holder: 'editorjs',
    minHeight: 600,
    ...(recipeObj != null && recipeObj.content.length > 0) && {data: JSON.parse(recipeObj.content)},
    /**
     * Common Inline Toolbar settings
     * - if true (or not specified), the order from 'tool' property will be used
     * - if an array of tool names, this order will be used
     */
    // inlineToolbar: ['link', 'marker', 'bold', 'italic'],
    // inlineToolbar: true,
    tools: {
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
    // defaultBlock: 'paragraph'
    onReady: function(){
      //saveButton.click();
    },
    onChange: function(api, event) {
      //console.log('something changed', event);
    }
  });

}

function addIngredient(item){
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

onMounted(() => {
    fetch(unitListUri)
    .then(response => response.json())
    .then(data => { 
    availableUnits.value = data})
    .catch(error => console.error('Unable to get unit list.', error)); //TODO replace this with an error message for the user 

    fetch(currentUserUri)
    .then(response => response.json())
    .then(data => { currentUserCard.value = data})
    .catch(error => console.error('Unable to get user information.', error)); //TODO replace this with an error message for the user 

    let defaultValues = JSON.parse(document.getElementById("app").getAttribute("data-init")); //attrs doesn't work
    setupEditor(defaultValues)
})


</script>

<template>
    <NavBar :userCard="currentUserCard"></NavBar>
    <h1><span class="block">Rezepte Editor</span></h1>
    <main>
    <div>
      <br>
      <div class="block">
        <div name="recipe-title" class="title borderless-field">{{ recipeName }}</div>
      </div><br>
      <hr class="divider">
      <h2><span class="block">Zutatenliste</span></h2>
      <p class="text-centered" v-if="ingredients.length === 0">
        Keine Zutaten
      </p>

      <table class="item-table">
        <tr v-for="listing,i in ingredients" :key="i">
          <td class="row-label">{{listing.itemName}}</td>
          <td>
            <div> {{ listing.amount }} </div>
          </td>
          <td><div> {{ listing.unit }}</div></td>
        </tr>
      </table>
    </div>
    <hr class="divider">
    <h2><span class="block">Beschreibung</span></h2>
    <div class="section-container">
        <div class="editor" id="editorjs"></div>
    </div>
    </main>
</template>