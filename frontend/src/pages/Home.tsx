import Navbar from "./Navbar.tsx";
import {useState} from "react";

function Home() {

  const [chats, setChats] = useState([])
  const [modalOpen, setModalOpen] = useState(false)
  const [loading, setLoading] = useState(null)
  const [error, setError] = useState('')

  return(
    <div className="home-page">
      <Navbar />
      <main className="home-content">
        <div className="content-header">
          <h2>Your Chats</h2>
          <button
            className="btn-new-chat"
            onClick={() => setModalOpen(true)}
          >
            + New Chat
          </button>
        </div>

        {error && <div className="error-banner">{error}</div>}

        {loading ?
          <div className="loading-banner">Loading...</div>
          :
          <div className="chats-list">{chats.map(chat => <div className="chat-item">{chat.name}</div>)}</div>
        }

      </main>
    </div>
  )
}

export default Home