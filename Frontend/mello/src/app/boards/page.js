'use client';

import { useEffect, useState, useRef } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { MoreVertical, Trash } from 'lucide-react'; // icons for three-dot and delete

export default function BoardsPage() {
  const [boards, setBoards] = useState([]);
  const [boardName, setBoardName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [boardDropdownOpen, setBoardDropdownOpen] = useState(null); // track which board's dropdown is open

  const router = useRouter();
  const dropdownRef = useRef(null);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const storedToken = localStorage.getItem('jwtToken');
      setToken(storedToken);

      const storedUser = localStorage.getItem('user');
      if (storedUser) setUser(JSON.parse(storedUser));
    }

    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(false);
        setBoardDropdownOpen(null);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const fetchBoards = () => {
    if (!token) return;
    setLoading(true);
    setError(null);

    fetch('http://localhost:8080/api/boards', {
      headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch boards');
        return res.json();
      })
      .then((data) => setBoards(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    if (token) fetchBoards();
  }, [token]);

  const createBoard = (e) => {
    e.preventDefault();
    if (boardName.trim().length < 3 || !token) return;

    fetch('http://localhost:8080/api/boards', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
      body: JSON.stringify({ name: boardName }),
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to create board');
        return res.json();
      })
      .then(() => {
        setBoardName('');
        fetchBoards();
      })
      .catch((err) => alert('Error: ' + err.message));
  };

  const deleteBoard = async (boardId) => {
    if (!token) return;
    const confirmDelete = window.confirm('Are you sure you want to delete this board?');
    if (!confirmDelete) return;

    try {
      const res = await fetch(`http://localhost:8080/api/boards/${boardId}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) {
        alert('Failed to delete board. Check console for details.');
        console.error('Delete board error:', res.status, res.statusText);
        return;
      }

      alert('Board deleted successfully!');
      fetchBoards();
      setBoardDropdownOpen(null);
    } catch (err) {
      console.error('Error deleting board:', err);
      alert('Error deleting board. Check console.');
    }
  };

  const handleSignOut = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
    router.push('/');
  };

  return (
    <div className="min-h-screen w-full relative">
      {/* Background */}
      <div
        className="absolute inset-0 z-0"
        style={{
          backgroundImage: `
            linear-gradient(to right, rgba(229,231,235,0.8) 1px, transparent 1px),
            linear-gradient(to bottom, rgba(229,231,235,0.8) 1px, transparent 1px),
            radial-gradient(circle 500px at 20% 100%, rgba(139,92,246,0.3), transparent),
            radial-gradient(circle 500px at 100% 80%, rgba(59,130,246,0.3), transparent)
          `,
          backgroundSize: "48px 48px, 48px 48px, 100% 100%, 100% 100%",
        }}
      />

      {/* Navbar/Profile */}
      <div className="absolute top-6 right-6 z-20 flex items-center" ref={dropdownRef}>
        <div
          className="w-12 h-12 rounded-full bg-gray-300 flex items-center justify-center cursor-pointer hover:ring-2 hover:ring-blue-400 transition"
          onClick={() => setDropdownOpen(!dropdownOpen)}
        >
          <span className="text-black font-bold">{user ? (user.name?.charAt(0) || 'U') : 'S'}</span>
        </div>

        {dropdownOpen && (
          <div className="absolute right-0 mt-14 w-40 bg-white rounded-lg shadow-lg overflow-hidden z-30">
            {token ? (
              <button
                onClick={handleSignOut}
                className="w-full text-left px-4 py-2 hover:bg-gray-100 transition"
              >
                Sign Out
              </button>
            ) : (
              <Link href="/auth/signup">
                <span className="block px-4 py-2 hover:bg-gray-100 cursor-pointer transition">
                  Sign Up
                </span>
              </Link>
            )}
          </div>
        )}
      </div>

      {/* Page Content */}
      <div className="relative z-10 max-w-4xl mx-auto p-6">
        <h1 className="text-3xl font-bold mb-6 text-white">Your Boards</h1>

        <form onSubmit={createBoard} className="mb-6 flex gap-4">
          <input
            type="text"
            placeholder="Enter board name"
            value={boardName}
            onChange={(e) => setBoardName(e.target.value)}
            className="flex-1 border-2 border-gray-300 px-4 py-2 rounded-lg shadow focus:outline-none focus:ring-2 focus:ring-blue-400 bg-white text-black"
          />
          <button
            type="submit"
            className="bg-blue-600 text-white px-6 py-2 rounded-lg shadow hover:bg-blue-700 transition"
          >
            Create
          </button>
        </form>

        {loading && <p className="text-white">Loading boards...</p>}
        {error && <p className="text-red-600">{error}</p>}
        {!loading && boards.length === 0 && !error && (
          <p className="text-gray-200">No boards yet. Create your first one above!</p>
        )}

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {boards.map((board) => (
            <div key={board.id} className="relative group">
              <Link href={`/boards/${board.id}`}>
                <div className="bg-white shadow p-4 rounded-lg border-2 border-pink-200 drop-shadow-lg hover:bg-gray-100 cursor-pointer transition flex justify-between items-start">
                  <h2 className="text-lg font-semibold">{board.name}</h2>
                </div>
              </Link>

              {/* Three-dot menu */}
              <div className="absolute top-2 right-2">
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    setBoardDropdownOpen(boardDropdownOpen === board.id ? null : board.id);
                  }}
                  className="p-1 hover:bg-gray-200 rounded-full transition"
                >
                  <MoreVertical size={18} />
                </button>

                {boardDropdownOpen === board.id && (
                  <div className="absolute right-0 mt-2 w-40 bg-white rounded-lg shadow-lg z-20">
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        deleteBoard(board.id);
                      }}
                      className="flex items-center px-4 py-2 text-red-600 hover:bg-gray-100 w-full text-left"
                    >
                      <Trash size={16} className="mr-2" /> Delete Board
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
