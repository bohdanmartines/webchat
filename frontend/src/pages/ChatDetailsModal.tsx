import '../css/ChatDetails.css';
import {getChatDisplayName} from "../types/Chat.ts";
import {useState} from "react";

function ChatDetailsModal({chat, isOpen, onClose}) {

  const [error, setError] = useState<string | null>(null);

  const isCreator = chat.ownerUsername === localStorage.getItem('username');

  const handleOverlayClick = (e: React.MouseEvent) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  const handleRemoveParticipant = async (username: string) => {
  // TODO Implement me
    console.log('Remove participant ' + username);
  }

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal-content">
        <div className="modal-header">
          <h2>Chat Information</h2>
          <button className="modal-close" onClick={onClose} aria-label="Close">
            ✕
          </button>
        </div>

        <div className="modal-body">
          {error && (
            <div className="modal-error">
              {error}
            </div>
          )}

          <div className="modal-section">
            <label className="modal-label">Chat Name</label>
            <div className="chat-name-display">{getChatDisplayName(chat)}</div>
          </div>

          <div className="modal-section">
            <label className="modal-label">
              Participants ({chat.participants.length})
            </label>
          </div>

          <div className="participants-list">
            {chat.participants.map((participant) => {
              const isParticipantOwner = participant.id === chat.ownerId;
              const canRemove = isCreator && !isParticipantOwner;

              return (
                <div key={participant.username} className="participant-item">
                  <div className="participant-info">
                    <span className="participant-icon">👤</span>
                    <span className="participant-username">
                        {participant.username}
                      {isParticipantOwner && (
                        <span className="creator-badge"> (Owner)</span>
                      )}
                      </span>
                    {canRemove && (
                      <button
                        className="remove-button"
                        onClick={() => handleRemoveParticipant(participant.username)}
                        aria-label={`Remove ${participant.username}`}
                      >
                        ✕
                      </button>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  )
}

export default ChatDetailsModal