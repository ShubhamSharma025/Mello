'use client'

import { useRouter } from 'next/navigation'

export default function HeroPage() {
  const router = useRouter()

  const handleCreateBoard = () => {
    router.push('/boards') // Redirect to boards
  }

  const handleLogin = () => {
    router.push('/auth/login') // ✅ corrected path
  }

  const handleSignup = () => {
    router.push('/auth/signup') // ✅ corrected path
  }

  return (
    <div className="min-h-screen w-full bg-white relative flex flex-col">
      {/* 🔹 Magenta Orb Grid Background */}
      <div
        className="absolute inset-0 z-0"
        style={{
          background: "white",
          backgroundImage: `
            linear-gradient(to right, rgba(71,85,105,0.15) 1px, transparent 1px),
            linear-gradient(to bottom, rgba(71,85,105,0.15) 1px, transparent 1px),
            radial-gradient(circle at 50% 60%, rgba(236,72,153,0.15) 0%, rgba(168,85,247,0.05) 40%, transparent 70%)
          `,
          backgroundSize: "40px 40px, 40px 40px, 100% 100%",
        }}
      />

      {/* 🔹 Navbar */}
      <header className="relative z-10 flex justify-between items-center px-10 py-6">
        <h2 className="text-2xl font-bold text-gray-800">Mello</h2>
        <div className="space-x-6">
          <button onClick={handleLogin} className="text-gray-700 hover:text-blue-600">
            Login
          </button>
          <button
            onClick={handleSignup}
            className="px-4 py-2 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition"
          >
            Sign Up
          </button>
        </div>
      </header>

      {/* 🔹 Hero Section */}
      <main className="relative z-10 flex flex-1 items-center justify-center">
        <div className="text-center space-y-6">
          <h1 className="text-5xl font-extrabold text-gray-800">Welcome to Mello</h1>
          <p className="text-lg text-gray-600">Organize your work visually, just like Trello</p>
          
          <div className="flex justify-center gap-4">
            <button
              onClick={handleCreateBoard}
              className="px-6 py-3 bg-blue-600 text-white rounded-2xl text-lg shadow hover:bg-blue-700 transition"
            >
              Create a Board
            </button>
            <button
              onClick={handleSignup}
              className="px-6 py-3 bg-pink-600 text-white rounded-2xl text-lg shadow hover:bg-pink-700 transition"
            >
              Get Started
            </button>
          </div>
        </div>
      </main>
    </div>
  )
}
