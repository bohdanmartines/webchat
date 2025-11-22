import '../css/ChatList.css';

function ChatList({ chats, onChatClick }) {

  if (chats.length === 0) {
    return (
      <div className="chat-list-empty">
        <div className="empty-state">
          <p>No chats yet</p>
          <p className="empty-state-subtitle">Create your first chat above!</p>
        </div>
      </div>
    );
  }

  return (
    <div className="chat-list">
      {chats.map((chat) => (
        <div
          key={chat.id}
          className="chat-item"
          onClick={() => onChatClick && onChatClick(chat.id)}
        >
          <div className="chat-icon">💬</div>
          <div className="chat-info">
            <div className="chat-name">{chat.name}</div>
          </div>
          <div className="chat-meta">
          <span className="participant-count">
                {' • '}{chat.participantCount} participant{chat.participantCount !== 1 ? 's' : ''}
              </span>
          </div>
        </div>
      ))}
    </div>
  )
}

export default ChatList