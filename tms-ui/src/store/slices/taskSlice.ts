import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';

interface Comment {
  userId: string;
  message: string;
  timestamp: string;
}

interface Task {
  id: string;
  projectId: string;
  title: string;
  description: string;
  assignee: string;
  status: string;
  clientCode: string;
  comments: Comment[];
  createdAt: string;
  updatedAt: string;
}

interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
}

const initialState: TaskState = {
  tasks: [],
  loading: false,
  error: null,
};

export const fetchTasksByProject = createAsyncThunk(
  'tasks/fetchByProject',
  async (projectId: string) => {
    const response = await axios.get(`http://localhost:8080/api/tasks/project/${projectId}`);
    return response.data;
  }
);

export const updateTaskStatus = createAsyncThunk(
  'tasks/updateStatus',
  async ({ taskId, status }: { taskId: string; status: string }) => {
    const response = await axios.put(`http://localhost:8080/api/tasks/${taskId}/status?status=${status}`);
    return response.data;
  }
);

const taskSlice = createSlice({
  name: 'tasks',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchTasksByProject.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchTasksByProject.fulfilled, (state, action: PayloadAction<Task[]>) => {
        state.loading = false;
        state.tasks = action.payload;
      })
      .addCase(updateTaskStatus.fulfilled, (state, action: PayloadAction<Task>) => {
        const index = state.tasks.findIndex(t => t.id === action.payload.id);
        if (index !== -1) {
          state.tasks[index] = action.payload;
        }
      });
  },
});

export default taskSlice.reducer;
