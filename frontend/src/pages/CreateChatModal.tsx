import { useState } from 'react';
import '../css/CreateChatModal.css';

function CreateChatModal({ isOpen, onClose, onSave }) {

  const [chatName, setChatName] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleClose = () => {
    setChatName('');
    setError('');
    onClose();
  };

  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      handleClose();
    }
  }

  async function handleSubmit (e: React.FormEvent){
    e.preventDefault();

    if (!chatName.trim()) {
      setError('Chat name cannot be empty');
      return;
    }

    try {
      setIsLoading(true);
      setError('');
      await onSave(chatName.trim());
      setChatName('');
      onClose();
    } catch (err: any) {
      setError(err.message || 'Failed to create chat');
    } finally {
      setIsLoading(false);
    }
  }

  if (!isOpen) return null;

  return(
    <div className="modal-backdrop" onClick={handleBackdropClick}>
      <div className="modal-content">
        <div className="modal-header">
          <h2>Create New Chat</h2>
          <button className="close-btn" onClick={handleClose}>×</button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="modal-body">
            <label htmlFor="chatName">Chat Name</label>
            <input
              id="chatName"
              type="text"
              value={chatName}
              onChange={(e) => setChatName(e.target.value)}
              placeholder="Enter chat name..."
              autoFocus
              disabled={isLoading}
              />
            {error && <div className="error-message">{error}</div>}
          </div>

          <div className="modal-footer">
            <button
              type="button"
              className="btn-secondary"
              onClick={handleClose}
              disabled={isLoading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn-primary"
              disabled={isLoading}
            >
              {isLoading ? 'Creating...' : 'Create'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default CreateChatModal