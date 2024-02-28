import { fileURLToPath, URL } from 'node:url'

import { resolve } from 'path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'


// https://vitejs.dev/config/
export default defineConfig({
  server: {
    proxy: {
      
      '^(/api|/login|/item)': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,      
          ws: true,
          configure: (proxy, _options) => {
            proxy.on('error', (err, _req, _res) => {
              console.log('proxy error', err);
            });
            proxy.on('proxyReq', (proxyReq, req, _res) => {
              console.log('Sending Request to the Target:', req.method, req.url);
            });
            proxy.on('proxyRes', (proxyRes, req, _res) => {
              console.log('Received Response from the Target:', proxyRes.statusCode, req.url);
            });
          }
      },
    }
  },
  build: {
    //outDir: "../backend/src/main/resources/frontend",
    emptyOutDir: true,
    /*
    lib : {
            // Could also be a dictionary or array of multiple entry points
            entry: resolve(__dirname, 'src/page/recipeEditor.js'),
            name: 'recipeEditor',
            // the proper extensions will be added
            fileName: 'recipeEditor',
            formats: ["umd"]
    },
    
    rollupOptions: {
      // make sure to externalize deps that shouldn't be bundled
      // into your library
      
      external: ['vue'],
      output: {
        // Provide global variables to use in the UMD build
        // for externalized deps
        globals: {
          vue: 'Vue',
        },
      },
    },*/
    
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'templates/index.html'),
        register: resolve(__dirname, 'templates/register.html'),
        registerSuccess: resolve(__dirname, 'templates/registerSuccess.html'),
        items: resolve(__dirname, 'templates/item/index.html'),
        recipeEditor: resolve(__dirname, 'templates/recipeEditor.html'),
        recipeView: resolve(__dirname, 'templates/recipeView.html'),
        usersView: resolve(__dirname, 'templates/usersView.html')
      },
    },
  },
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
