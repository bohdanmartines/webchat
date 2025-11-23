import {useEffect, useMemo, useState} from "react";
import Navbar from "./Navbar.tsx";
import * as chatApi from '../api/chat'
import ChatList from "./ChatList.tsx";
import {useAuth} from "../api/AuthContext.tsx";
import '../css/HomePage.css';
import CreateChatModal from "./CreateChatModal.tsx";

function Home() {

  const [chats, setChats] = useState([])
  const [searchQuery, setSearchQuery] = useState('')

  const [modalOpen, setModalOpen] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const auth = useAuth()

  useEffect(() => {
    loadChats()
  }, [])

  const filteredChats = useMemo(() => {
    if (!searchQuery.trim()) {
      return chats;
    }

    const query = searchQuery.toLowerCase();
    return chats.filter(chat =>
      chat.name.toLowerCase().includes(query)
    );
  }, [chats, searchQuery]);

  async function loadChats() {
    try {
      setLoading(true)
      setError('')
      const chatsData = await chatApi.getChats();
      setChats(chatsData)
    } catch (err: any) {
      setError(err.message || 'Failed to load chats');
      if (err.response?.status === 401) {
        setError('Invalid credentials')
        auth.signOut()
      }
    } finally {
      setLoading(false)
    }
  }

  function handleChatClick(chatId: number) {
    // TODO Implement me
    console.log('Chat clicked: ' + chatId)
  }

  async function handleCreateChat(name: string) {
    const newChat = await chatApi.createChat(name);
    setChats([...chats, newChat]);
    setModalOpen(false)
  }

  return (
    <div className="home-page">
      <Navbar/>
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

        <div className="search-container">
          <input
            type="text"
            className="search-input"
            placeholder="🔍 Search chats..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>

        {error && <div className="error-banner">{error}</div>}

        {loading ?
          <div className="loading-banner">Loading...</div>
          :
          <ChatList chats={filteredChats} onChatClick={handleChatClick}/>
        }
      </main>
      <CreateChatModal
        isOpen={modalOpen}
        onClose={() => {setModalOpen(false)}}
        onSave={handleCreateChat}
      />
    </div>
  )
}

export default Home