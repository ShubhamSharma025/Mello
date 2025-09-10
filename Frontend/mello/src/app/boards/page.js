'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';

export default function BoardsPage() {
  const [boards, setBoards] = useState([]);
  const [boardName, setBoardName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [token, setToken] = useState(null); // ✅ keep token in state

  // Load token safely after client-side render
  useEffect(() => {
    if (typeof window !== 'undefined') {
      const storedToken = localStorage.getItem('jwtToken');
      setToken(storedToken);
    }
  }, []);

  // Fetch all boards from backend
  const fetchBoards = () => {
    if (!token) return;

    setLoading(true);
    setError(null);

    fetch('http://localhost:8080/api/boards', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`, // ✅ JWT header
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch boards');
        return res.json();
      })
      .then((data) => setBoards(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  };

  // Trigger fetch once token is ready
  useEffect(() => {
    if (token) {
      fetchBoards();
    }
  }, [token]);

  // Create a new board
  const createBoard = (e) => {
    e.preventDefault();
    if (boardName.trim().length < 3 || !token) return;

    fetch('http://localhost:8080/api/boards', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`, // ✅ JWT header
      },
      body: JSON.stringify({ name: boardName }),
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to create board');
        return res.json();
      })
      .then(() => {
        setBoardName('');
        fetchBoards(); // refresh list
      })
      .catch((err) => alert('Error: ' + err.message));
  };

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Your Boards</h1>

      {/* ➕ Create Board Form */}
      <form onSubmit={createBoard} className="mb-6 flex gap-4">
        <input
          type="text"
          placeholder="Enter board name"
          value={boardName}
          onChange={(e) => setBoardName(e.target.value)}
          className="flex-1 border px-4 py-2 rounded-lg shadow-sm"
        />
        <button
          type="submit"
          className="bg-blue-600 text-white px-6 py-2 rounded-lg shadow hover:bg-blue-700 transition"
        >
          Create
        </button>
      </form>

      {/* 🔄 Loading / Error / Empty */}
      {loading && <p>Loading boards...</p>}
      {error && <p className="text-red-600">{error}</p>}
      {!loading && boards.length === 0 && !error && (
        <p className="text-gray-500">No boards yet. Create your first one above!</p>
      )}

      {/* 🧱 Display Boards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {boards.map((board) => (
          <Link key={board.id} href={`/boards/${board.id}`}>
            <div className="bg-white shadow p-4 rounded-lg hover:bg-gray-100 cursor-pointer transition">
              <h2 className="text-lg font-semibold">{board.name}</h2>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}
