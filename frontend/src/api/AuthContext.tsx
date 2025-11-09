import {createContext, useContext, useState} from "react";

import * as authApi from '../api/auth'

type AuthContextType = {
  token: string | null
  user: string | null
  signUp: (username: string, password: string) => Promise<any>
  signIn: (username: string, password: string) => Promise<any>
}

const AuthContext = createContext<AuthContextType | null>(null)

export function AuthProvider({children}: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(null)
  const [user, setUser] = useState<string | null>(null)

  async function signUp(username: string, password: string) {
    return await authApi.signUp(username, password)
  }

  async function signIn(username: string, password: string) {
    let response = await authApi.signIn(username, password);
    setToken(response.token)
    setUser(username)
    return response
  }

  return (
    <AuthContext value={{token, user, signUp, signIn}}>
      {children}
    </AuthContext>
  )
}

export function useAuth() {
  let context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider')
  return context
}