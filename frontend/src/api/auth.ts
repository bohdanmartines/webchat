import http from './http'

export async function signUp(username: string, password: string, name: string) {
  let response = await http.post('/auth/register ', {username, password, name});
  return response.data;
}
