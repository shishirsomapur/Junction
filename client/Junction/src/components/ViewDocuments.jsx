import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import axios from 'axios';
import { setDocuments } from '../slices/documentsSlice';
import { Link } from 'react-router-dom';

function ViewDocuments() {
    const dispatch = useDispatch();
    const documents = useSelector((state) => state.documents.documents || []);
    const [searchTerm, setSearchTerm] = useState('');

    const email = localStorage.getItem('email');
    const token = localStorage.getItem('token');

    useEffect(() => {
        if (documents.length === 0 && email && token) {
            const fetchDocuments = async () => {
                try {
                    const res = await axios.get(`/documents/${email}`, {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });
                    dispatch(setDocuments(res.data));
                } catch (error) {
                    console.error('Error fetching documents:', error);
                }
            };

            fetchDocuments();
        }
    }, [dispatch, documents.length, email, token]);

    const filteredDocuments = documents.filter((doc) =>
        doc.title.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="p-6 pt-24 max-w-4xl mx-auto">
            <h2 className="text-2xl font-bold mb-6">Your Documents</h2>

            <input
                type="text"
                placeholder="Search by title..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="mb-6 px-4 py-2 border rounded w-full md:w-1/2"
            />

            {filteredDocuments.length === 0 ? (
                <p className="text-gray-600">No documents to display.</p>
            ) : (
                <div className="space-y-4">
                    {filteredDocuments.map((doc) => (
                        <Link to={`/edit-document/${doc.id}`} key={doc.id}>
                            <div className="p-4 border rounded shadow-sm bg-white hover:shadow-md transition cursor-pointer mb-4">
                                <h3 className="text-xl font-semibold">{doc.title}</h3>
                                <p className="text-gray-600">Visibility: {doc.visibility}</p>
                                <p className="text-gray-600">
                                    Shared With:{' '}
                                    {doc.sharedWith?.length > 0
                                        ? doc.sharedWith.map((u, idx) => (
                                            <span
                                                key={idx}
                                                className="inline-block mr-1 px-2 py-0.5 bg-blue-100 text-blue-800 text-sm rounded"
                                            >
                                                {typeof u === 'string' ? u : u.email}
                                            </span>
                                        ))
                                        : 'None'}
                                </p>
                            </div>
                        </Link>
                    ))}

                </div>
            )}
        </div>
    );
}

export default ViewDocuments;
