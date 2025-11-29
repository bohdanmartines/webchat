import {useParams} from "react-router-dom";

function Chat() {

  const { chatId } = useParams<{ chatId: string }>();

  return (
    <div>
      <div>Chat</div>
      <div>Chat ID: {chatId}</div>
    </div>
  )
}

export default Chat