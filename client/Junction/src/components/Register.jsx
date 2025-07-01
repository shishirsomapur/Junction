import axios from 'axios';
import React, { useEffect, useRef, useState } from 'react';
import { BsCheck2Circle } from "react-icons/bs";

const Register = ({ onClose }) => {
  const modalRef = useRef(null);
  const [form, setForm] = useState({ email: '', password: '', fullName: '', username: '' });
  const [registered, setRegistered] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);

  const closeModal = (event) => {
    if (modalRef.current === event.target) {
      setRegistered(false);
      onClose();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitted(true);

    const { email, password, fullName, username } = form;
    if (!email || !password || !username || !fullName) return;

    try {
      const response = await axios.post('http://localhost:8080/users/register', form);
      if (response.status === 200) setRegistered(true);
    } catch (err) {
      console.log(err);
    }
  };

  const handleChange = (e) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  return (
    <div className="fixed inset-0 bg-opacity-25 backdrop-blur-sm flex justify-center z-50 items-center" ref={modalRef} onClick={closeModal}>
      <div className="w-[500px] flex flex-col">
        <div className="bg-white p-4 rounded shadow-lg">
          {registered ? (
            <div className="flex flex-col items-center">
              <BsCheck2Circle className="w-24 h-24 text-green-600" />
              <p>Registered Successfully!</p>
            </div>
          ) : (
            <form className="flex flex-col" onSubmit={handleSubmit}>
              <label>Full Name</label>
              <input className="bg-slate-200 my-2 h-9 p-2" type="text" name="fullName" value={form.fullName} onChange={handleChange} />
              {isSubmitted && !form.fullName && <p className="text-red-500 text-xs font-bold mb-2">*Full name is required</p>}

              <label>Username</label>
              <input className="bg-slate-200 my-2 h-9 p-2" type="text" name="username" value={form.username} onChange={handleChange} />
              {isSubmitted && !form.username && <p className="text-red-500 text-xs font-bold mb-2">*Username is required</p>}

              <label>Email</label>
              <input className="bg-slate-200 my-2 h-9 p-2" type="email" name="email" value={form.email} onChange={handleChange} />
              {isSubmitted && !form.email && <p className="text-red-500 text-xs font-bold mb-2">*Email is required</p>}

              <label>Password</label>
              <input className="bg-slate-200 my-2 h-9 p-2" type="password" name="password" value={form.password} onChange={handleChange} />
              {isSubmitted && !form.password && <p className="text-red-500 text-xs font-bold mb-2">*Password is required</p>}

              <input className="w-28 bg-blue-600 text-white py-1 rounded mt-3 cursor-pointer hover:bg-blue-700" type="submit" value="Register" />
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default Register;
