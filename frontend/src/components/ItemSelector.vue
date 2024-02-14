<script setup>

import {computed} from "vue"

const props = defineProps(["itemMap","searchString"]) // maps int -> object

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

function select(item){
    emit('itemSelected', item)
}
</script>


<template>
    <div>
        <div>
            <button v-for="item in filtereditems" :key="item.id"  @click="select(item)" class="item-selection-button">
            {{item.name}}
            </button>
        </div>
    </div>
</template>