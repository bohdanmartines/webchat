import '../css/CreateChatModal.css';
import {getChatDisplayName} from "../types/Chat.ts";

function ChatDetailsModal({ chat, isOpen, onClose }) {

  function handleClose() {
    onClose();
  }

  if (!isOpen) return null;

  return(
    <div className="modal-backdrop">
      <div className="modal-content">
        <div className="modal-header">
          <span className="chat-title">{getChatDisplayName(chat)}</span>
          <button className="close-btn" onClick={handleClose}>×</button>
        </div>
      </div>
    </div>
  )
}

export default ChatDetailsModal