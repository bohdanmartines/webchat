import {Link, useNavigate} from "react-router-dom";
import {useState} from "react";
import {useAuth} from "../api/AuthContext.tsx";

function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const auth = useAuth()
  const navigate = useNavigate()

  async function submit(e: React.FormEvent) {
    e.preventDefault()
    setLoading(true)
    setError(null)
    try {
      await auth.signIn(username, password)
      navigate('/')
    } catch (err: any) {
      setError(err?.response?.data?.message || err.message || 'Login failed')
    } finally {
      setLoading(false)
    }
  }

  return(
    <div className="flex items-center justify-center h-screen">
      <div className="w-full max-w-md bg-white p-8 rounded shadow">

        <h2 className="text-2xl mb-6">Sign in</h2>

        <form onSubmit={submit} className="space-y-4">
          <div>
            <label className="block text-sm">Username</label>
            <input value={username} onChange={e => setUsername(e.target.value)}
                   className="w-full border p-2 rounded" placeholder="your username" />
          </div>
          <div>
            <label className="block text-sm">Password</label>
            <input type="password" value={password} onChange={e => setPassword(e.target.value)}
                   className="w-full border p-2 rounded" placeholder="password" />
          </div>
          {error && <div className="text-red-600">{error}</div>}
          <div>
            <button className="w-full py-2 rounded bg-indigo-600 text-white" disabled={loading}>
              {loading ? 'Signing in...' : 'Sign in'}
            </button>
          </div>
        </form>

        <p className="mt-4 text-sm">
          Don't have an account? <Link to="/register" className="text-indigo-600">Sign up</Link>
        </p>
      </div>
    </div>
  )
}

export default Login