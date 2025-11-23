import {Link, useNavigate} from "react-router-dom";
import {useState} from "react";
import {useAuth} from "../api/AuthContext.tsx";
import '../css/Auth.css';

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
      if (err.response?.status === 401) {
        setError('Invalid credentials')
      } else {
        setError(err?.response?.data?.message || err.message || 'Login failed')
      }
    } finally {
      setLoading(false)
    }
  }

  return(
    <div className="auth-page">
      <div className="auth-container">
        <h1 className="auth-title">Login</h1>

        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <form onSubmit={submit}>
          <div className="form-group">
            <label htmlFor="username" className="form-label">
              Username
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              disabled={loading}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={loading}
              className="form-input"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="btn-submit"
          >
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="auth-footer">
          Don't have an account?{' '}
          <Link to="/register" className="auth-link">
            Register
          </Link>
        </div>
      </div>
    </div>
  )
}

export default Login