import React, { useState } from 'react';
import Editor from './Editor';

function Home() {
  const [content, setContent] = useState('');

  const handleSubmit = () => {
    console.log("Content:", content);
  };

  return (
    <div className="p-4 max-w-3xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Create a Post</h1>
      <Editor value={content} onChange={setContent} />
      <button
        className="mt-4 px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        onClick={handleSubmit}
      >
        Submit
      </button>
    </div>
  );
}

export default Home;
