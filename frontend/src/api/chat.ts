import http from './http'

export async function createChat(name: string) {
  let response = await http.post('/chat', {name});
  return response.data;
}

export async function getChats() {
  let response = await http.get('/chat');
  return response.data;
}
