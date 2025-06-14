import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  id: null,
  name: "",
  isEnabled: false,
  steps: [],
};

export const workflowSlice = createSlice({
  name: "Untitled Workflow",
  initialState,
  reducers: {
    setWorkflow: (state, action) => {
      state.id = action.payload.id;
      state.name = action.payload.name;
      state.isEnabled = action.payload.isEnabled;
      state.steps = action.payload.steps;
    },
    toggleWorkflowEnabled: (state) => {
      state.isEnabled = !state.isEnabled;
    },
    setWorkflowName: (state, action) => {
      state.name = action.payload;
    },
    addStep: (state, action) => {
      const { newStep, type } = action.payload;
      if (type === "trigger") {
        if (state.steps.length > 0 && state.steps[0].type === "trigger") {
          state.steps[0] = newStep;
        } else {
          state.steps.unshift(newStep);
        }
      } else {
        state.steps.push(newStep);
      }
    },
    deleteStep: (state, action) => {
      const stepIdToDelete = action.payload;
      state.steps = state.steps.filter((step) => step.id !== stepIdToDelete);
    },
    updateStep: (state, action) => {
      const { stepId, field, value } = action.payload;
      const stepToUpdate = state.steps.find((step) => step.id === stepId);
      if (stepToUpdate) {
        stepToUpdate[field] = value;
      }
    },
    reorderSteps: (state, action) => {
      state.steps = action.payload;
    },
  },
});

export const {
  setWorkflow,
  toggleWorkflowEnabled,
  setWorkflowName,
  addStep,
  deleteStep,
  updateStep,
  reorderSteps,
} = workflowSlice.actions;

export default workflowSlice.reducer;
