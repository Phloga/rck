<script setup>
import {ref, computed, defineProps, defineEmits, defineModel, onMounted, onUnmounted} from 'vue'


const props = defineProps(["unitList","modelValue"])
const emit = defineEmits(["update:modelValue"])

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

function closeDropdown(){
    isOpen.value = false
}

function openDropdown(){
    isOpen.value = true
}

function selectItem(index){
    let unit = filteredUnitList.value[index]
    closeDropdown()
    emit("update:modelValue", unit.name)
}
</script>

<template>
    <div @focusin="openDropdown">
        <input type="text" :value="props.modelValue" @input="emit('update:modelValue', $event.target.value)" placeholder="Einheit">
        <div v-show="isOpen">
            <a href="#" v-for="unit,i in filteredUnitList" :key="i" @click="selectItem(i)">
                <div>{{ unit.name }} [{{ unit.symbol }}]</div>
            </a>
        </div> 
    </div>
</template>