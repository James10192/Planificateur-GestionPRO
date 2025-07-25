/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        // Primary Colors
        primary: {
          DEFAULT: "#005BAA", // Primary Blue
          light: "#3D7EBB",
          lighter: "#7AA6D1",
          lightest: "#E6EFF8",
        },
        secondary: {
          DEFAULT: "#1A75FF", // Secondary Blue
          light: "#4D94FF",
          lighter: "#80B3FF",
          lightest: "#E5F0FF",
        },
        // Accent Colors
        accent: {
          DEFAULT: "#FF8C00", // Accent Orange
          light: "#FFA533",
          lighter: "#FFBE66",
          lightest: "#FFF2E5",
        },
        // Neutral Colors
        neutral: {
          dark: "#2D3748",
          medium: "#718096",
          light: "#E2E8F0",
          lightest: "#F7FAFC",
        },
        // Semantic Colors
        success: "#48BB78",
        warning: "#ECC94B",
        error: "#F56565",
        info: "#4299E1",
      },
      fontFamily: {
        sans: [
          "Inter",
          "Roboto",
          "system-ui",
          "-apple-system",
          "BlinkMacSystemFont",
          "sans-serif",
        ],
      },
      spacing: {
        0: "0",
        1: "4px",
        2: "8px",
        3: "12px",
        4: "16px",
        5: "20px",
        6: "24px",
        8: "32px",
        10: "40px",
        12: "48px",
        16: "64px",
        20: "80px",
      },
      borderRadius: {
        none: "0",
        sm: "2px",
        md: "4px",
        lg: "8px",
        xl: "16px",
        round: "9999px",
      },
      boxShadow: {
        sm: "0 1px 3px rgba(0, 0, 0, 0.1)",
        md: "0 4px 6px rgba(0, 0, 0, 0.1)",
        lg: "0 10px 15px rgba(0, 0, 0, 0.1)",
        xl: "0 20px 25px rgba(0, 0, 0, 0.1)",
      },
    },
  },
  plugins: [],
};
