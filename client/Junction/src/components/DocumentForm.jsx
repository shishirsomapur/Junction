import React, { useState } from 'react';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { updateDocument } from '../slices/documentsSlice';
import { useNavigate } from 'react-router-dom';
import Editor from './Editor';

function DocumentForm({ mode = 'create', initialData = {} }) {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [title, setTitle] = useState(initialData.title || '');
  const [content, setContent] = useState(initialData.content || '');
  const [visibility, setVisibility] = useState(initialData.visibility || 'PRIVATE');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('email');

    if (!token || !user) {
      alert("You are not logged in");
      return;
    }

    const payload = { title, content, visibility };

    try {
      if (mode === 'edit') {
        await axios.put(`/documents/${initialData.id}`, payload, {
          headers: { Authorization: `Bearer ${token}` },
        });

        dispatch(updateDocument({ id: initialData.id, updates: payload }));
        navigate('/view-documents');

      } else {
        // create document
        const res = await axios.post('/documents', { ...payload, user }, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          }
        });

        alert(res.data);
        setTitle('');
        setContent('');
        setVisibility('PRIVATE');
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Operation failed");
    }
  };

  return (
    <div className="p-8 pt-24 max-w-3xl mx-auto">
      <h2 className="text-2xl font-bold mb-6">
        {mode === 'edit' ? 'Edit Document' : 'Create Document'}
      </h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Title"
          className="w-full border px-4 py-2 rounded"
          required
        />

        <Editor value={content} onChange={setContent} />

        <select
          value={visibility}
          onChange={(e) => setVisibility(e.target.value)}
          className="w-full border px-4 py-2 rounded"
        >
          <option value="PRIVATE">Private</option>
          <option value="PUBLIC">Public</option>
        </select>

        <button
          type="submit"
          className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700"
        >
          {mode === 'edit' ? 'Save Changes' : 'Save'}
        </button>
      </form>
    </div>
  );
}

export default DocumentForm;
    