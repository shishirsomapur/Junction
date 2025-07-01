import { useEffect, useState } from 'react';
import Register from './Register';
import Login from './Login';
import ForgotPassword from './ForgotPassword';

function Navbar() {
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const [showForgotPassword, setShowForgotPassword] = useState(false);
  const [loggedInUser, setLoggedInUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userEmail = localStorage.getItem('email');
    if (token && userEmail) {
      setLoggedInUser(userEmail);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    setLoggedInUser(null);
  };

  return (
    <nav className="w-full px-6 py-4 bg-white shadow-md flex justify-between items-center">
      <h1 className="text-2xl font-bold text-black-600">junction</h1>

      {/* Right side of navbar */}
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

      {/* Modals */}
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

      {showRegister && (
        <Register onClose={() => setShowRegister(false)} />
      )}

      {showForgotPassword && (
        <ForgotPassword onClose={() => setShowForgotPassword(false)} />
      )}
    </nav>
  );
}

export default Navbar;
