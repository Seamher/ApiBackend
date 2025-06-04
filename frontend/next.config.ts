import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  env: {
    NEXT_PUBLIC_NGINX_URL:
      process.env.NEXT_PUBLIC_NGINX_URL || "http://localhost:65/",
    PUBLIC_URL:
      process.env.NEXT_PUBLIC_API_URL ||
      "http://127.0.0.1:4523/m1/6503504-6203961-default",
  },
};

export default nextConfig;
