import { useState, useRef, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

function ResetPassword() {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const navigate = useNavigate();

    const modalRef = useRef(null);
    const [newPassword, setNewPassword] = useState('');
    const [confirm, setConfirm] = useState('');
    const [message, setMessage] = useState('');

    const closeModal = (e) => {
        if (modalRef.current === e.target) {
            navigate('/');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (newPassword !== confirm) {
            console.log("hello")
            setMessage('Passwords do not match');
            return;
        }

        try {
            const res = await axios.post('/users/reset-password', {
                token,
                newPassword,
            });
            setMessage(res.data);
            setTimeout(() => navigate('/'), 2000);
        } catch (err) {
            console.log(err)
            setMessage(err.response?.data || 'Something went wrong');
        }
    };

    return (
        <div
            className="fixed inset-0 shadow-sm bg-opacity-25 backdrop-blur-sm flex justify-center z-50 items-center"
            onClick={closeModal}
            ref={modalRef}
        >
            <div className="w-[300px] md:w-[400px] bg-white p-6 rounded shadow-lg">
                <h2 className="text-xl font-semibold mb-4 text-center">Reset Your Password</h2>
                {message && <p className="text-center mb-4 text-blue-600">{message}</p>}
                <form onSubmit={handleSubmit} className="flex flex-col space-y-4">
                    <input
                        type="password"
                        placeholder="New Password"
                        className="bg-gray-200 px-4 py-2 rounded"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Confirm Password"
                        className="bg-gray-200 px-4 py-2 rounded"
                        value={confirm}
                        onChange={(e) => setConfirm(e.target.value)}
                        required
                    />
                    <button
                        type="submit"
                        className="bg-blue-600 text-white py-2 rounded hover:bg-blue-700 cursor-pointer" 
                    >
                        Reset Password
                    </button>
                </form>
            </div>
        </div>
    );
}

export default ResetPassword;
