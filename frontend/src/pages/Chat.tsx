import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";

import type {Chat} from "../types/Chat.ts";
import * as chatApi from "../api/chat.ts";

import '../css/Chat.css';

function Chat() {

  const [chat, setChat] = useState<Chat | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const { chatId } = useParams<{ chatId: string }>();
  if (!chatId) {
    navigate('/home');
    return null;
  }
  const chatIdNumber = parseInt(chatId, 10);

  useEffect(() => {
    loadChat();
  }, [])

  async function loadChat() {
    try {
      setLoading(true);
      setError(null);
      const chatData = await chatApi.getChat(chatIdNumber);
      setChat(chatData);
    } catch (err: any) {
      setError(err.message || 'Failed to load chat');
    } finally {
      setLoading(false);
    }
  }

  function handleBack() {
    navigate('/home');
  }

  function getChatDisplayName() {
    if (!chat) return '';
    return chat.name ? chat.name : chat.participants.map(_ => _.username)
  }

  if (loading) {
    return (
      <div className="chat-page page-container">
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
      <div className="chat-page page-container">
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
    <div className="chat-page page-container">
      <div className="chat-header">
        <button className="back-button" onClick={handleBack}>
          ←
        </button>
        <span className="chat-title">{getChatDisplayName()}</span>
      </div>
      <div className="messages-area">
        <div className="no-messages">
          Messages will appear here
        </div>
      </div>
      <div className="input-area">
        <input
          type="text"
          className="message-input"
          placeholder="Type a message..."
          disabled
        />
        <button className="send-button" disabled>
          Send
        </button>
      </div>
    </div>
  )
}

export default Chat