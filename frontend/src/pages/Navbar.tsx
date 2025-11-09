import {useAuth} from "../api/AuthContext.tsx";

export default function Navbar() {

  const { user, signOut } = useAuth()

  return(
    <header className="bg-white shadow">
      <div className="max-w-4xl mx-auto flex items-center justify-between p-4">
        <div className="font-semibold">WebChat</div>
        <div className="flex items-center gap-4">
          <div className="text-sm">{user}</div>
          <button onClick={signOut} className="text-sm text-red-500">Sign out</button>
        </div>
      </div>
    </header>
  )
}