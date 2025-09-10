// 'use client'

// import { useRouter } from 'next/navigation'

// export default function SignOutButton() {
//   const router = useRouter()

//   const handleSignOut = () => {
//     try {
//       // 🔹 Remove the JWT token from localStorage
//       localStorage.removeItem('jwtToken')
//       console.log('User signed out')

//       // 🔹 Redirect to login page
//       router.push('/api/auth/login')
//     } catch (error) {
//       console.error('Error signing out:', error)
//     }
//   }

//   return (
//     <button
//       onClick={handleSignOut}
//       className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition"
//     >
//       🚪 Sign Out
//     </button>
//   )
// }
