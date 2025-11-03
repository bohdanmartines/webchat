import './App.css'
import {Navigate, Route, Routes} from "react-router-dom";
import Register from './pages/Register'
import Login from "./pages/Login.tsx";

function App() {
  return (
    <Routes>
      <Route path="/register" element={<Register/>}/>
      <Route path="/login" element={<Login/>}/>
      <Route path="*" element={<Navigate to="/login" replace/>}/>
    </Routes>
  )
}

export default App
