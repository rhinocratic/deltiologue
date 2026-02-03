import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
// import { playwright } from '@vitest/browser-playwright'
import { UserConfig } from 'vite'

export default defineConfig({
  plugins: [react()],
  test: {
    // browser: {
    //   enabled: true,
    //   provider: playwright(),
    //   instances: [
    //     { browser: 'chromium' },
    //   ],
    // },
    environment: 'jsdom',
    globals: true,
    setupFiles: './test/setup.js',
  }
} as UserConfig)