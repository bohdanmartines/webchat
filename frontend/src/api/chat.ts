import http from './http'

export async function createChat(name: string) {
  let response = await http.post('/chat', {name});
  return response.data;
}

export async function getChats() {
  let response = await http.get('/chat');
  return response.data;
}

export async function getChat(chatId: number) {
  let response = await http.get('/chat/' + chatId);
  return response.data;
}

export async function getMessages(chatId: number) {
  let response = await http.get('/chat/' + chatId + '/message');
  return response.data;
}

export async function addParticipant(chatId: number, username: string) {
  let response = await http.post('/chat/' + chatId + '/user', {username});
  return response.data;
}

export async function removeParticipant(chatId: number, username: string) {
  let response = await http.delete(`/chat/${chatId}/user`, {
    data: { username }
  });
  return response.data;
}
