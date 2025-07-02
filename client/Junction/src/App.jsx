import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Register from './components/Register'
import Login from './components/Login'
import Navbar from './components/Navbar'
import Home from './components/Home'
import ResetPassword from './components/ResetPassword'
import CreateDocument from './components/CreateDocument'
import ViewDocuments from './components/ViewDocuments'
import EditDocument from './components/EditDocument';

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/create-document" element={<CreateDocument />} />
        <Route path="/view-documents" element={<ViewDocuments />} />
        <Route path="/edit-document/:id" element={<EditDocument />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
