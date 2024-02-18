//import RecipeEditor from './components/RecipeEditor.vue'

//import { createApp } from 'vue'

//let app = createApp(RecipeEditor)
//app.mount("#app")



import EditorJS from "@editorjs/editorjs"
import Header from "@editorjs/header"
import List from "@editorjs/list"
import Warning from "@editorjs/warning"
import Table from "@editorjs/table"

var editor = new EditorJS({
    /**
     * Enable/Disable the read only mode
     */
    readOnly: false,

    /**
     * Wrapper of Editor
     */
    holder: 'editorjs',

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

document.addEventListener("saveRecipe", function(event) {
  //TODO save
})


import { createApp } from "vue"
import RecipeEditor from "../components/RecipeEditor.vue"

let app = createApp(RecipeEditor)
app.mount("#app")