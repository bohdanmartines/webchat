import http from './http'

export async function signUp(username: string, password: string) {
  let response = await http.post('/auth/register ', {username, password});
  return response.data;
}

export async function signIn(username: string, password: string) {
  let response = await http.post('/auth/login ', {username, password});
  return response.data;
}
