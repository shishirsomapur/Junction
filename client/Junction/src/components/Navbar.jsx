import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Register from './Register';
import Login from './Login';
import ForgotPassword from './ForgotPassword';
import { FiMenu, FiX } from 'react-icons/fi';
import { isTokenExpired } from '../utils/auth';
import { jwtDecode } from 'jwt-decode';

function Navbar() {
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const [showForgotPassword, setShowForgotPassword] = useState(false);
  const [loggedInUser, setLoggedInUser] = useState(null);
  const [menuOpen, setMenuOpen] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userEmail = localStorage.getItem('email');

    if (token && userEmail && !isTokenExpired(token)) {
      setLoggedInUser(userEmail);
    } else {
      localStorage.removeItem('token');
      localStorage.removeItem('email');
      setLoggedInUser(null);
    }
  }, []);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const { exp } = jwtDecode(token);
      const timeout = exp * 1000 - Date.now();

      if (timeout > 0) {
        const timer = setTimeout(() => {
          localStorage.removeItem('token');
          localStorage.removeItem('email');
          setLoggedInUser(null);
        }, timeout);

        return () => clearTimeout(timer);
      }
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    setLoggedInUser(null);
  };

  const handleProtectedNav = (route) => {
    if (!loggedInUser) {
      setShowLogin(true);
    } else {
      navigate(route);
      setMenuOpen(false);
    }
  };

  return (
    <nav className="w-full px-6 py-4 bg-white shadow-md flex justify-between items-center fixed z-10">
      <h1 className="text-2xl font-bold text-black-600">junction</h1>

      <div className="hidden md:flex items-center gap-6">
        <button
          className="hover:underline"
          onClick={() => handleProtectedNav('/create-document')}
        >
          Create Document
        </button>
        <button
          className="hover:underline"
          onClick={() => handleProtectedNav('/view-documents')}
        >
          View Documents
        </button>
      </div>

      <div className="hidden md:flex items-center gap-6">
        {loggedInUser ? (
          <div className="flex items-center space-x-4">
            <div
              className="bg-blue-600 text-white font-bold w-10 h-10 rounded-full flex items-center justify-center"
              title={loggedInUser}
            >
              {loggedInUser.charAt(0).toUpperCase()}
            </div>
            <button
              onClick={handleLogout}
              className="text-sm text-blue-600 hover:underline"
            >
              Logout
            </button>
          </div>
        ) : (
          <button
            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 cursor-pointer"
            onClick={() => setShowLogin(true)}
          >
            Login
          </button>
        )}
      </div>

      <div className="md:hidden">
        <button onClick={() => setMenuOpen(!menuOpen)}>
          {menuOpen ? <FiX size={24} /> : <FiMenu size={24} />}
        </button>
      </div>

      {menuOpen && (
        <div className="absolute top-16 left-0 w-full bg-white shadow-md p-4 flex flex-col gap-4 md:hidden z-40">
          <button onClick={() => handleProtectedNav('/create-document')}>
            Create Document
          </button>
          <button onClick={() => handleProtectedNav('/view-documents')}>
            View Documents
          </button>
          {loggedInUser ? (
            <>
              <span className="font-semibold text-gray-600 flex items-center justify-center">
                <div
                  className="bg-blue-600 text-white font-bold w-10 h-10 rounded-full flex items-center justify-center"
                  title={loggedInUser}>
                  {loggedInUser.charAt(0).toUpperCase()}
                </div>
              </span>
              <button
                onClick={() => {
                  handleLogout();
                  setMenuOpen(false);
                }}
                className="text-blue-600 hover:underline"
              >
                Logout
              </button>
            </>
          ) : (
            <button
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
              onClick={() => {
                setShowLogin(true);
                setMenuOpen(false);
              }}
            >
              Login
            </button>
          )}
        </div>
      )}

      {showLogin && (
        <Login
          onClose={() => setShowLogin(false)}
          onSwitchToRegister={() => {
            setShowLogin(false);
            setShowRegister(true);
          }}
          onForgotPassword={() => {
            setShowLogin(false);
            setShowForgotPassword(true);
          }}
          onLoginSuccess={(email) => {
            setLoggedInUser(email);
            setShowLogin(false);
          }}
        />
      )}

      {showRegister && <Register onClose={() => setShowRegister(false)} />}
      {showForgotPassword && (
        <ForgotPassword onClose={() => setShowForgotPassword(false)} />
      )}
    </nav>
  );
}

export default Navbar;
