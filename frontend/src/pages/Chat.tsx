import {useNavigate, useParams} from "react-router-dom";
import {useState} from "react";
import type {Chat} from "../types/Chat.ts";

function Chat() {

  const [chat, setChat] = useState<Chat | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const { chatId } = useParams<{ chatId: string }>();

  function handleBack() {
    navigate('/home');
  }

  if (loading) {
    return (
      <div className="chat-page">
        <div className="chat-header">
          <button className="back-button" onClick={handleBack}>
            ←
          </button>
          <span className="chat-title">Loading...</span>
        </div>
      </div>
    );
  }

  if (error || !chat) {
    return (
      <div className="chat-page">
        <div className="chat-header">
          <button className="back-button" onClick={handleBack}>
            ←
          </button>
          <span className="chat-title">Error</span>
        </div>
        <div className="error-message">{error || 'Chat not found'}</div>
      </div>
    );
  }

  return (
    <div>
      <div>Chat</div>
      <div>Chat ID: {chatId}</div>
    </div>
  )
}

export default Chat