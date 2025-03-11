/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'brand-primary': '#2C5282',
        'brand-secondary': '#4299E1',
        'brand-accent': '#F56565'
      }
    },
  },
  plugins: [],
}