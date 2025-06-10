import axios from "axios";

const server = "http://localhost:8080/api";

export const api = axios.create({
  baseURL: server,
  withCredentials: true,
});

// api.interceptors.request.use(async (config) => {
//   const token = await fetchCsrfToken();
//   if (token) {
//     config.headers["X-CSRF-Token"] = token;
//   }
//   return config;
// });
