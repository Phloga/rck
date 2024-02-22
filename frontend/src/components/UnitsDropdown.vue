<script setup>
import {ref, computed} from 'vue'

const props = defineProps(["unitList","modelValue"])
const emit = defineEmits(["update:modelValue"])

const dropdownRoot = ref(null)
const isOpen = ref(false)


const filteredUnitList = computed(() => {
  let newFilteredList = []
    props.unitList.forEach((unit) => {
        if (unit.name.toUpperCase().indexOf(props.modelValue.toUpperCase()) != -1) {
          newFilteredList.push(unit)
        }
    })
    return newFilteredList 
})

function closeWhenInside(e){	
    if (!dropdownRoot.value.contains(e.target)){
        closeDropdown();
    }
}

function closeDropdown(){
    isOpen.value = false
    window.removeEventListener('focusin', closeWhenInside)
}

function openDropdown(){
    isOpen.value = true
    window.addEventListener('focusin', closeWhenInside)
}

function selectItem(index){
    let unit = filteredUnitList.value[index]
    closeDropdown()
    emit("update:modelValue", unit.name)
}
</script>

<template>
    <div ref="dropdownRoot" @focusin="openDropdown">
        <input type="text" :value="props.modelValue" @input="emit('update:modelValue', $event.target.value)" placeholder="Einheit" class="borderless-field">
        <div v-show="isOpen" class="dropdown">
            <button v-for="unit,i in filteredUnitList" :key="i" @click="selectItem(i)">
                {{ unit.name }} [{{ unit.symbol }}]
            </button>
        </div> 
    </div>
</template>