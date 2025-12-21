const BASE = import.meta.env.VITE_WS_BASE || '/ws/chat'

export function connect(chatId: number) {
  console.log('Connecting to WebSocket with base:', BASE);
  return new WebSocket(BASE + '/' + chatId)
}