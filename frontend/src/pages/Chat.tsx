import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";

import * as webSocketApi from '../api/webSocket'

import type {Chat} from "../types/Chat.ts";
import * as chatApi from "../api/chat.ts";

import '../css/Chat.css';
import type {Message} from "../types/Message.ts";

function Chat() {

  const [chat, setChat] = useState<Chat | null>(null);
  const [ws, setWs] = useState<WebSocket | null>(null);
  const [connected, setConnected] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const [messages, setMessages] = useState<Message[]>([]);
  const [messageInput, setMessageInput] = useState('');

  const { chatId } = useParams<{ chatId: string }>();
  if (!chatId) {
    navigate('/home');
    return null;
  }
  const chatIdNumber = parseInt(chatId, 10);

  useEffect(() => {
    loadChat();

    return () => {
      if (ws?.readyState === WebSocket.OPEN) {
        ws.close();
      }
    };
  }, [])

  async function loadChat() {
    try {
      setError(null);

      const chatData = await chatApi.getChat(chatIdNumber);
      setChat(chatData);

      const webSocket = await webSocketApi.connect(chatIdNumber);

      webSocket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        console.log('Message received: ', message);
      }

      webSocket.onerror = (event) => {
        const error = JSON.parse(event.data);
        setError(error)
        setConnected(false)
        console.log('WebSocket error: ', error);
      }

      webSocket.onclose = () => {
        setConnected(false);
        console.log('WebSocket closed');
      }

      setConnected(true);
      setWs(webSocket);
    } catch (err: any) {
      setError(err.message || 'Failed to load chat');
    }
  }

  function handleBack() {
    navigate('/home');
  }

  function getChatDisplayName() {
    if (!chat) return '';
    return chat.name ? chat.name : chat.participants.map(_ => _.username)
  }

  function handleSendMessage() {
    if (ws?.readyState !== WebSocket.OPEN) {
      setError('Cannot send message: WebSocket not connected');
    }
    ws.send(JSON.stringify({
      type: 'message',
      content: messageInput,
    }));
    setMessageInput('');
  }

  function loadMessages() {
    // TODO Implement me
  }

  if (!connected) {
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
        {messages.length === 0 ? (
          <div className="no-messages">
            Messages will appear here
          </div>
        ) : (messages.map((message, index) => <p key={index}>{message.content}</p>))}
          </div>
      <div className="input-area">
        <input
          type="text"
          className="message-input"
          placeholder="Type a message..."
          value={messageInput}
          onChange={(e) => setMessageInput(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleSendMessage()}
          disabled={!connected}
        />
        <button className="send-button"
                onClick={handleSendMessage}
                disabled={!connected || !messageInput.trim()}>
          Send
        </button>
      </div>
    </div>
  )
}

export default Chat