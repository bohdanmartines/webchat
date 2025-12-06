const BASE = import.meta.env.VITE_WS_BASE || '/ws/chat'

export async function connect(chatId: number) {
  console.log('Connecting to WebSocket with base:', BASE);
  const webSocket = new WebSocket(BASE + '/' + chatId);

  webSocket.onopen = () => {
    console.log('WebSocket connected');

    const token = localStorage.getItem('token')
    webSocket.send(JSON.stringify({
      type: 'authenticate',
      token: token
    }));
    console.log('Sent authentication message');
  }

  return webSocket
}