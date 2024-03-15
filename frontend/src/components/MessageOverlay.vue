<script setup>

const messages = defineModel()

const props = defineProps(
    {
        modelValue : []
    }
)

function onDismiss() {
    messages.value.splice(0,messages.value.length)
}

</script>

<template>

<div v-show="messages.length > 0" class="overlay">
    <div v-for="message,i in props.modelValue" :key="i">
        <div>{{message}}</div>
    </div>
    <button @click="onDismiss()" class="rounded-corners control">Ok</button>
</div>

</template>

<style>

.overlay{
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-flow: column nowrap;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0,0,0,0.5);
    z-index: 200;
    cursor: pointer;
}

</style>