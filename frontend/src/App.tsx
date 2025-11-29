import './css/App.css'
import {Navigate, Route, Routes} from "react-router-dom";

import Register from './pages/Register'
import Login from "./pages/Login.tsx";
import Home from "./pages/Home.tsx";
import {useAuth} from "./api/AuthContext.tsx";
import Chat from "./pages/Chat.tsx";

function ProtectedPage({children}: { children: JSX.Element }) {
  const auth = useAuth()
  if (!auth.token) {
    return <Navigate to="/login" replace/>;
  }
  return children;
}

function App() {
  return (
    <Routes>
      <Route path="/register" element={<Register/>}/>
      <Route path="/login" element={<Login/>}/>
      <Route path="/"
             element={
               <ProtectedPage>
                 <Home/>
               </ProtectedPage>
             }/>
      <Route path="/chat/:chatId"
             element={
               <ProtectedPage>
                 <Chat/>
               </ProtectedPage>
             }/>
      <Route path="*" element={<Navigate to="/" replace/>}/>
    </Routes>
  )
}

export default App
