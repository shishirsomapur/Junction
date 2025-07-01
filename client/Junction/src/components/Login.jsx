import axios from 'axios';
import React, { useRef, useState } from 'react';
import { FaRegCircleCheck } from "react-icons/fa6";
import { TbXboxX } from "react-icons/tb";

const Login = ({ onClose, onSwitchToRegister, onForgotPassword, onLoginSuccess  }) => {
  const modalRef = useRef(null);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginStatus, setLoginStatus] = useState(null);
  const [isSubmitted, setIsSubmitted] = useState(false);

  const closeModal = (event) => {
    if (modalRef.current === event.target) {
      setLoginStatus(null);
      onClose();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitted(true);

    if (!username || !password) return;

    try {
      console.log(username, password)
      const response = await axios.post('/users/login', {
        email: username,
        password: password
      });

      const tokenAndUserId = response.data;
      const [token, userId] = tokenAndUserId.split(':');
      localStorage.setItem('token', token);
      localStorage.setItem('email', username);
      setLoginStatus('success');
      onLoginSuccess(username);
    } catch (err) {
      console.error(err);
      console.log("failed to login" + err)
      setLoginStatus('failure');
    }
  };

  return (
    <div
      className="fixed inset-0 shadow-sm bg-opacity-25 backdrop-blur-sm flex justify-center z-50 items-center"
      ref={modalRef}
      onClick={closeModal}
    >
      <div className="w-[300px] md:w-[500px] flex flex-col shadow-lg">
        <div className="bg-white p-4 rounded">
          {loginStatus === null && (
            <form className="flex flex-col" onSubmit={handleSubmit}>
              <label htmlFor="username">Email</label>
              <input
                className="bg-slate-200 my-2 h-9 p-2"
                type="text"
                name="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              {isSubmitted && !username && (
                <p className="text-red-500 text-xs font-bold mb-2">*Email is required</p>
              )}

              <label htmlFor="password">Password</label>
              <input
                className="bg-slate-200 my-2 h-9 p-2"
                type="password"
                name="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              {isSubmitted && !password && (
                <p className="text-red-500 text-xs font-bold mb-2">*Password is required</p>
              )}

              <p className="text-sm text-right">
                <span
                  className="text-blue-600 cursor-pointer"
                  onClick={() => {
                    onClose();             // Close login modal
                    onForgotPassword();   // Trigger forgot password modal
                  }}
                >
                  Forgot Password?
                </span>
              </p>

              <input
                className="w-28 bg-blue-600 text-white py-1 rounded mt-3 cursor-pointer hover:bg-blue-700"
                type="submit"
                value="Login"
              />

              <p className="mt-4 text-sm text-center">
                New user?{' '}
                <span
                  className="text-blue-600 cursor-pointer"
                  onClick={() => {
                    onClose();
                    onSwitchToRegister();
                  }}
                >
                  Register here
                </span>
              </p>
            </form>
          )}

          {loginStatus === 'success' && (
            <div className="flex flex-col items-center">
              <FaRegCircleCheck className="w-24 h-24 mb-3 text-green-600" />
              <p className="text-green-700">Login Successful</p>
            </div>
          )}

          {loginStatus === 'failure' && (
            <div className="flex flex-col items-center">
              <TbXboxX className="w-24 h-24 mb-3 text-red-600" />
              <p className="text-red-700">Login Failed</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Login;
