import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'


export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/users': 'http://localhost:8080',
      '/documents': 'http://localhost:8080'
    },
  }
})
