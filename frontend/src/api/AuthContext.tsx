import {createContext, useContext, useState} from "react";

import * as authApi from '../api/auth'

type AuthContextType = {
  token: string | null
  username: string | null
  signUp: (username: string, password: string) => Promise<any>
}

const AuthContext = createContext<AuthContextType | null>(null)

export function AuthProvider({children}: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(() => 'MOCK_TOKEN')
  const [username, setUsername] = useState<string | null>(() => 'MOCK_USERNAME')

  async function signUp(username: string, password: string) {
    return await authApi.signUp(username, password)
  }

  return (
    <AuthContext value={{token, username, signUp}}>
      {children}
    </AuthContext>
  )
}

export function useAuth() {
  let context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider')
  return context
}