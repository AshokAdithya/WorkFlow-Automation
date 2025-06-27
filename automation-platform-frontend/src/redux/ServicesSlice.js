import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../api/Api";

const initialState = {
  apps: [],
  loading: false,
  error: null,
};

export const fetchServices = createAsyncThunk(
  "services/fetchServices",
  async (_, { rejectWithValue }) => {
    try {
      const response = await api.get("/integrations");
      const apps = Array.isArray(response.data)
        ? response.data
        : response.data.apps;
      return Array.isArray(apps) ? apps : [];
    } catch (error) {
      return rejectWithValue(error.response?.data || error.message);
    }
  }
);

export const servicesSlice = createSlice({
  name: "services",
  initialState,
  extraReducers: (builder) => {
    builder
      .addCase(fetchServices.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchServices.fulfilled, (state, action) => {
        state.apps = action.payload;
        state.loading = false;
      })
      .addCase(fetchServices.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export default servicesSlice.reducer;
