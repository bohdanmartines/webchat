import {useAuth} from "../api/AuthContext.tsx";
import '../css/Navbar.css';

export default function Navbar() {

  const { user, signOut } = useAuth()

  return(
    <header className="navbar">
      <div className="navbar-content page-container">
        <div className="navbar-logo">WebChat</div>
        <div className="navbar-actions">
          <div className="navbar-user">{user}</div>
          <button onClick={signOut} className="btn-signout">Sign out</button>
        </div>
      </div>
    </header>
  )
}