import './App.css'
import {Navigate, Route, Routes} from "react-router-dom";

import Register from './pages/Register'
import Login from "./pages/Login.tsx";
import Home from "./pages/Home.tsx";

function ProtectedPage({children}: { children: JSX.Element }) {
  return children;
}

function App() {
  return (
    <Routes>
      <Route path="/register" element={<Register/>}/>
      <Route path="/login" element={<Login/>}/>
      <Route path="/home"
             element={
               <ProtectedPage>
                 <Home/>
               </ProtectedPage>
             }/>
      <Route path="*" element={<Navigate to="/login" replace/>}/>
    </Routes>
  )
}

export default App
