import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';

const ForgotPassword = ({ onClose }) => {
  const modalRef = useRef(null);
  const [email, setEmail] = useState('');
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [emailError, setEmailError] = useState(false);
  const [responseMsg, setResponseMsg] = useState(null);

  const closeModal = (event) => {
    if (modalRef.current === event.target) {
      onClose();
    }
  };

  useEffect(() => {
    if (isSubmitted && email === '') setEmailError(true);
    else setEmailError(false);
  }, [email, isSubmitted]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitted(true);
    if (email === '') return;

    try {
      const res = await axios.post('http://localhost:8080/users/reset-password', null, {
        params: { email },
      });
      setResponseMsg(res.data);
    } catch (error) {
      setResponseMsg(error.response?.data || 'Something went wrong');
    }
  };

  return (
    <div
      className="fixed inset-0 shadow-sm bg-opacity-25 backdrop-blur-sm flex justify-center z-50 items-center"
      ref={modalRef}
      onClick={closeModal}
    >
      <div className="w-[300px] md:w-[400px] bg-white p-6 rounded shadow-lg">
        <h2 className="text-xl font-semibold mb-4">Reset Password</h2>

        {responseMsg ? (
          <p className="text-center text-green-600 font-medium">{responseMsg}</p>
        ) : (
          <form onSubmit={handleSubmit} className="flex flex-col space-y-4">
            <input
              type="email"
              placeholder="Enter your email"
              className="bg-gray-200 px-4 py-2 rounded"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            {emailError && (
              <p className="text-red-500 text-sm font-bold">*Email is required</p>
            )}
            <button
              type="submit"
              className="bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
            >
              Send Reset Link
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ForgotPassword;
