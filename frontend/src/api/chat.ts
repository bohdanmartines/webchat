import http from './http'

export async function getChats() {
  let response = await http.get('/chat');
  return response.data;
}
