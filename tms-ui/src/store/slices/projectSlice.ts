import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';

interface Project {
  id: string;
  name: string;
  description: string;
  managerId: string;
  clientCode: string;
  createdAt: string;
  updatedAt: string;
}

interface ProjectState {
  projects: Project[];
  loading: boolean;
  error: string | null;
}

const initialState: ProjectState = {
  projects: [],
  loading: false,
  error: null,
};

export const fetchProjects = createAsyncThunk(
  'projects/fetchAll',
  async (clientCode: string) => {
    const response = await axios.get(`http://localhost:8080/api/projects?clientCode=${clientCode}`);
    return response.data;
  }
);

export const createProject = createAsyncThunk(
  'projects/create',
  async (project: Partial<Project>) => {
    const response = await axios.post('http://localhost:8080/api/projects/create', project);
    return response.data;
  }
);

const projectSlice = createSlice({
  name: 'projects',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchProjects.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchProjects.fulfilled, (state, action: PayloadAction<Project[]>) => {
        state.loading = false;
        state.projects = action.payload;
      })
      .addCase(fetchProjects.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch projects';
      })
      .addCase(createProject.fulfilled, (state, action: PayloadAction<Project>) => {
        state.projects.push(action.payload);
      });
  },
});

export default projectSlice.reducer;
