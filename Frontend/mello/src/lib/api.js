// lib/api.js
const API_URL = "http://localhost:8080/api";

const getToken = () =>
  typeof window !== "undefined" ? localStorage.getItem("jwtToken") : null;

export async function apiFetch(path, options = {}) {
  const token = getToken();

  const res = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
      ...options.headers,
    },
  });

  if (!res.ok) {
    let errorMessage = `API error ${res.status}`;
    try {
      const err = await res.json();
      errorMessage = err.message || errorMessage;
    } catch {}
    throw new Error(errorMessage);
  }

  return res.status === 204 ? null : res.json();
}
