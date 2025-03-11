import axios from "axios";

const API_URL = "http://localhost:8080/api/auth";

export const loginUser = async (credentials: { email: string; password: string }) => {
  const response = await axios.post(`${API_URL}/login`, credentials);
  return response.data;
};

export const registerUser = async (userData: { firstName: string; lastName: string; email: string; password: string }) => {
  const response = await axios.post(`${API_URL}/register-admin`, userData);
  return response.data;
};
