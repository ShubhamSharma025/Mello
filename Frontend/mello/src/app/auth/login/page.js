'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { FiEye, FiEyeOff } from 'react-icons/fi'
import { FcGoogle } from 'react-icons/fc'

export default function LoginPage() {
  const router = useRouter()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  // 🔹 Login with Spring Security + JWT
  const loginWithEmail = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const res = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }), // ✅ match backend DTO
      })

      if (!res.ok) {
        throw new Error('Login failed')
      }

      const data = await res.json()

      // ✅ Store JWT & user info
      localStorage.setItem('jwtToken', data.jwt)
      localStorage.setItem('userId', data.userId)

      // Redirect to dashboard/boards
      router.push('/boards')
    } catch (err) {
      console.error(err)
      setError('Invalid username or password.')
    } finally {
      setLoading(false)
    }
  }

  // 🔹 Placeholder for Google login
  const loginWithGoogle = async () => {
    setError('Google login not yet connected to backend.')
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-100 to-purple-100 px-4">
      <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
        <h2 className="text-3xl font-bold text-center text-indigo-700 mb-6">
          Login to Mello
        </h2>

        {error && <p className="text-sm text-center text-red-600 mb-4">{error}</p>}

        {/* 🔹 Username/Password Form */}
        <form onSubmit={loginWithEmail} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1 text-gray-700">Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
              required
              className="w-full border border-gray-300 px-4 py-2 rounded-lg shadow-sm focus:ring-2 focus:ring-indigo-400 focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1 text-gray-700">Password</label>
            <div className="relative">
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                autoComplete="current-password"
                required
                className="w-full border border-gray-300 px-4 py-2 rounded-lg shadow-sm focus:ring-2 focus:ring-indigo-400 focus:outline-none pr-10"
              />
              <span
                onClick={() => setShowPassword((prev) => !prev)}
                className="absolute right-3 top-1/2 -translate-y-1/2 cursor-pointer text-gray-500"
              >
                {showPassword ? <FiEyeOff size={20} /> : <FiEye size={20} />}
              </span>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 rounded-lg transition"
          >
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        {/* 🔹 Google Button */}
        <div className="my-4 text-center text-gray-500">or</div>
        <button
          onClick={loginWithGoogle}
          disabled={loading}
          className="w-full flex items-center justify-center gap-3 border border-gray-300 py-2 rounded-lg hover:bg-gray-100 transition"
        >
          <FcGoogle size={20} />
          <span>Continue with Google</span>
        </button>

        {/* 🔹 Signup Link */}
        <p className="mt-6 text-center text-sm text-gray-600">
          Don’t have an account?{' '}
          <span
            onClick={() => router.push('/auth/signup')}
            className="text-indigo-600 underline cursor-pointer"
          >
            Sign up
          </span>
        </p>
      </div>
    </div>
  )
}
