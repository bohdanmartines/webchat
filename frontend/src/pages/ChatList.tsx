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

  return (<div>
    {chats.map((chat) => (
      <div
        key={chat.id}
        className="chat-item"
        onClick={() => onChatClick && onChatClick(chat.id)}
      >
      </div>
    ))}
  </div>)
}

export default ChatList