import { createSlice } from '@reduxjs/toolkit';

const documentsSlice = createSlice({
  name: 'documents',
  initialState: {
    documents: [],
  },
  reducers: {
    setDocuments: (state, action) => {
      state.documents = action.payload;
    },
    clearDocuments: (state) => {
      state.documents = [];
    },
    updateDocument: (state, action) => {
      const { id, updates } = action.payload;
      const index = state.documents.findIndex(doc => doc.id === id);
      if (index !== -1) {
        state.documents[index] = {
          ...state.documents[index],
          ...updates
        };
      }
    },
  },
});

export const { setDocuments, clearDocuments, updateDocument } = documentsSlice.actions;

export default documentsSlice.reducer;
