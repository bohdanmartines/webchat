import './App.css'
import {Navigate, Route} from "react-router-dom";
import Register from './pages/Register'

function App() {
  return (
      <div className="min-h-screen flex flex-col">
          <Route path="/register" element={<Register />} />
          <Route path="*" element={<Navigate to="/register" replace />} />
      </div>
  )
}

export default App
