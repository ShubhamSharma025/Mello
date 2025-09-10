'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { FiEye, FiEyeOff } from 'react-icons/fi'
import { FcGoogle } from 'react-icons/fc'

export default function SignupPage() {
  const router = useRouter()

  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const togglePasswordVisibility = () => setShowPassword(!showPassword)

  // 🔹 Handle Signup with Spring Security + JWT
  const handleSignup = async (e) => {
    e.preventDefault()
    setError(null)

    if (password !== confirmPassword) {
      return setError('Passwords do not match')
    }

    setLoading(true)
    try {
      const res = await fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password }),
      })

      if (!res.ok) {
        const errData = await res.json().catch(() => ({}))
        throw new Error(errData.message || 'Signup failed')
      }

      const data = await res.json()
      localStorage.setItem('jwtToken', data.token)

      router.push('/boards') // ✅ redirect to boards
    } catch (err) {
      console.error(err)
      setError(err.message || 'Failed to sign up. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const handleGoogleSignup = () => {
    setError('Google signup not yet connected to backend.')
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-purple-100 to-indigo-100 px-4">
      <div className="bg-white rounded-xl shadow-md p-8 w-full max-w-md">
        <h2 className="text-3xl font-bold text-center mb-6 text-indigo-700">
          Create a Mello Account
        </h2>

        {error && <p className="text-red-600 text-sm mb-4 text-center">{error}</p>}

        <form onSubmit={handleSignup} className="space-y-4">
          <InputField
            label="Username"
            type="text"
            value={username}
            onChange={setUsername}
            required
          />

          <InputField
            label="Email"
            type="email"
            value={email}
            onChange={setEmail}
            required
          />

          <PasswordField
            label="Password"
            value={password}
            onChange={setPassword}
            show={showPassword}
            toggleShow={togglePasswordVisibility}
          />

          <PasswordField
            label="Confirm Password"
            value={confirmPassword}
            onChange={setConfirmPassword}
            show={showPassword}
            toggleShow={togglePasswordVisibility}
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-indigo-600 text-white py-2 rounded-lg hover:bg-indigo-700 transition shadow"
          >
            {loading ? 'Signing up...' : 'Sign Up'}
          </button>
        </form>

        <div className="mt-6 flex items-center justify-center text-sm text-gray-500">Or</div>

        <button
          onClick={handleGoogleSignup}
          disabled={loading}
          className="mt-4 w-full flex items-center justify-center border border-gray-300 py-2 rounded-lg hover:bg-gray-100 transition"
        >
          <FcGoogle className="mr-2 text-xl" />
          {loading ? 'Loading...' : 'Sign up with Google'}
        </button>

        <p className="mt-4 text-sm text-center text-gray-600">
          Already have an account?{' '}
          <span
            onClick={() => router.push('/login')}
            className="text-indigo-600 cursor-pointer underline"
          >
            Log in
          </span>
        </p>
      </div>
    </div>
  )
}

// 🔹 Reusable Input Component
function InputField({ label, type, value, onChange, required }) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
      <input
        type={type}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required={required}
        className="w-full border border-gray-300 px-3 py-2 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-400"
      />
    </div>
  )
}

// 🔹 Reusable Password Field
function PasswordField({ label, value, onChange, show, toggleShow }) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
      <div className="relative">
        <input
          type={show ? 'text' : 'password'}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          required
          className="w-full border border-gray-300 px-3 py-2 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-400 pr-10"
        />
        <span
          onClick={toggleShow}
          className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 cursor-pointer"
        >
          {show ? <FiEyeOff size={20} /> : <FiEye size={20} />}
        </span>
      </div>
    </div>
  )
}
