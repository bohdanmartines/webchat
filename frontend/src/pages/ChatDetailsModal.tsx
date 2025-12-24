import '../css/ChatDetails.css';
import {getChatDisplayName} from "../types/Chat.ts";
import type {User} from "../types/User.ts";

function ChatDetailsModal({chat, isOpen, onClose}) {

  function handleClose() {
    onClose();
  }

  if (!isOpen) return null;

  return (
    <div className="modal-backdrop">
      <div className="modal-content">
        <div className="modal-header">
          <span className="chat-title">{getChatDisplayName(chat)}</span>
          <button className="close-btn" onClick={handleClose}>×</button>
        </div>
        <div className="modal-body">
          <p>{chat.participants.length} participant(s)</p>

          <p>Add a participant</p>
          <input type="text" placeholder="Participant username"/>

          {
            chat.participants.map((participant: User) =>
              <div
                key={participant.id}
                className="participant-item">
                <p>{participant.username}</p>
              </div>
            )
          }
        </div>
      </div>
    </div>
  )
}

export default ChatDetailsModal