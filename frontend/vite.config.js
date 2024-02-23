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
    outDir: "../backend/src/main/resources/frontend",
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'templates/index.html'),
        register: resolve(__dirname, 'templates/register.html'),
        registerSuccess: resolve(__dirname, 'templates/registerSuccess.html'),
        items: resolve(__dirname, 'templates/item/index.html'),
        recipeEditor: resolve(__dirname, 'templates/recipeEditor.html')
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
