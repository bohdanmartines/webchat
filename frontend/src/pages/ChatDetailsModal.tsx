import '../css/ChatDetails.css';
import {getChatDisplayName} from "../types/Chat.ts";
import {useState} from "react";

import * as chatApi from "../api/chat.ts";

function ChatDetailsModal({chat, isOpen, onClose, onChatUpdated}) {

  const [newUsername, setNewUsername] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const isCreator = chat.ownerUsername === localStorage.getItem('username');

  const handleClose = () => {
    setNewUsername('');
    setError('');
    onClose();
  }

  const handleOverlayClick = (e: React.MouseEvent) => {
    if (e.target === e.currentTarget) {
      handleClose();
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !loading) {
      handleAddParticipant(newUsername.trim());
    }
  };

  const handleAddParticipant = async () => {
    if (!newUsername.trim()) {
      setError('Please enter a username');
      return;
    }
    console.log('Adding participant ' + newUsername + ' to chat ' + chat.id);
    setError(null);

    try {
      await chatApi.addParticipant(chat.id, newUsername);

      const chatData = await chatApi.getChat(chatIdNumber);
      onChatUpdated(chatData);

      setNewUsername('');
      setError(null);

    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to add participant');
    }
  }

  const handleRemoveParticipant = async (username: string) => {
  // TODO Implement me
    console.log('Remove participant ' + username);
  }

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal-content">
        <div className="modal-header">
          <span className="chat-title">{getChatDisplayName(chat)}</span>
          <button className="modal-close" onClick={handleClose} aria-label="Close">
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
            <label className="modal-label">
              Participants ({chat.participants.length})
            </label>

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

          {isCreator && (
            <div className="modal-section">
              <label className="modal-label">Add Participant</label>
              <div className="add-participant-container">
                <input
                  type="text"
                  className="add-participant-input"
                  placeholder="Enter username..."
                  value={newUsername}
                  onChange={(e) => setNewUsername(e.target.value)}
                  onKeyDown={handleKeyDown}
                  disabled={loading}
                />
                <button
                  className="add-participant-button"
                  onClick={() => handleAddParticipant()}
                  disabled={loading || !newUsername.trim()}
                >
                  {loading ? 'Adding...' : 'Add'}
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default ChatDetailsModal