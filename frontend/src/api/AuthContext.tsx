import {createContext, useContext, useEffect, useState} from "react";

import * as authApi from '../api/auth'

type AuthContextType = {
  token: string | null
  user: string | null
  signUp: (username: string, password: string) => Promise<any>
  signIn: (username: string, password: string) => Promise<any>
  signOut: () => Promise<any>
}

const AuthContext = createContext<AuthContextType | null>(null)

const TOKEN_KEY = 'token';
const USERNAME_KEY = 'username';

export function AuthProvider({children}: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem(TOKEN_KEY))
  const [user, setUser] = useState<string | null>(() => localStorage.getItem(USERNAME_KEY))

  useEffect(() => {
    if (token) {
      localStorage.setItem(TOKEN_KEY, token)
    } else {
      localStorage.removeItem(TOKEN_KEY)
    }
  }, [token])

  useEffect(() => {
    if (user) {
      localStorage.setItem(USERNAME_KEY, user)
    } else {
      localStorage.removeItem(USERNAME_KEY)
    }
  }, [user])

  async function signUp(username: string, password: string) {
    return await authApi.signUp(username, password)
  }

  async function signIn(username: string, password: string) {
    let response = await authApi.signIn(username, password);
    setToken(response.token)
    setUser(username)
    return response
  }

  async function signOut() {
    setToken(null)
    setUser(null)
  }

  return (
    <AuthContext value={{token, user, signUp, signIn, signOut}}>
      {children}
    </AuthContext>
  )
}

export function useAuth() {
  let context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider')
  return context
}