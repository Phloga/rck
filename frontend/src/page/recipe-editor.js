//import RecipeEditor from './components/RecipeEditor.vue'

//import { createApp } from 'vue'

//let app = createApp(RecipeEditor)
//app.mount("#app")

import '/assets/main.css'

import { createApp } from "vue"
import RecipeEditor from "../components/RecipeEditor.vue"

let app = createApp(RecipeEditor)
app.mount("#app")