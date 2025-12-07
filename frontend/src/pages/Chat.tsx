import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useRef, useState} from "react";

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
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const { chatId } = useParams<{ chatId: string }>();
  if (!chatId) {
    navigate('/home');
    return null;
  }
  const chatIdNumber = parseInt(chatId, 10);

  const currentUser = localStorage.getItem('username');

  useEffect(() => {
    loadChat();
    loadMessages();

    return () => {
      if (ws?.readyState === WebSocket.OPEN) {
        ws.close();
      }
    };
  }, [])

  useEffect(() => {
    scrollToBottom();

    // correction scroll after layout settling
    setTimeout(() => {
      scrollToBottom();
    }, 50);
  }, [messages]);

  async function loadChat() {
    try {
      setError(null);

      const chatData = await chatApi.getChat(chatIdNumber);
      setChat(chatData);

      const webSocket = await webSocketApi.connect(chatIdNumber);

      webSocket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        switch (message.type) {
          case 'newMessage':
            setMessages(prev => [...prev, message]);
            console.log('Message added to message list: ', message);
        }
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

  async function loadMessages() {
    const messageData = await chatApi.getMessages(chatIdNumber);
    setMessages(messageData);
    console.log('Messages loaded:', messageData);
  }

  function scrollToBottom() {
    console.log('scrolling to bottom');
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }

  function formatTime(timestamp?: string) {
    if (!timestamp) return '';
    const date = new Date(timestamp);

    const now = new Date();
    const isToday =
      date.getFullYear() === now.getFullYear() &&
      date.getMonth() === now.getMonth() &&
      date.getDate() === now.getDate();

    if (isToday) {
      return date.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit'
      });
    }

    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
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
        ) : (
          messages.map((message, index) => {
            const isOwnMessage = message.username === currentUser;
            return (
              <div
                key={message.id || index}
                className={`message ${isOwnMessage ? 'message-own' : 'message-other'}`}
              >
                <div className="message-bubble">
                  {!isOwnMessage && (
                    <div className="message-sender">{message.username}</div>
                  )}
                  <div className="message-content">{message.content}</div>
                  <div className="message-time">
                    {formatTime(message.createdAt)}
                  </div>
                </div>
              </div>
            )
          })
        )}
        <div ref={messagesEndRef}/>
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