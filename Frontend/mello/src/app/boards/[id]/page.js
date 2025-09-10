"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { HTML5Backend } from "react-dnd-html5-backend";
import { DndProvider } from "react-dnd";
import { DragCard } from "./DragCard";
import CardDetailsSheet from "./CardDetailsSheet";
import { MoreVertical, Trash } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import {
  AlertDialog,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogCancel,
  AlertDialogAction,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";

export default function BoardPage() {
  const { id } = useParams();
  const [board, setBoard] = useState(null);
  const [loading, setLoading] = useState(true);
  const [newListName, setNewListName] = useState("");
  const [cardInputs, setCardInputs] = useState({});
  const [editCard, setEditCard] = useState(null);
  const [cardToDelete, setCardToDelete] = useState(null);
  const [listToDelete, setListToDelete] = useState(null);
  const [selectedCard, setSelectedCard] = useState(null);

  // ✅ Grab token once on mount
  const getToken = () =>
    typeof window !== "undefined" ? localStorage.getItem("jwtToken") : null;

  const fetchBoard = async () => {
    try {
      const token = getToken();
      const res = await fetch(`http://localhost:8080/api/boards/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
      const data = await res.json();
      setBoard(data);
    } catch (err) {
      console.error("Failed to fetch board:", err);
      setBoard(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBoard();
  }, [id]);

  const createList = async (e) => {
    e.preventDefault();
    if (!newListName.trim()) return;

    const token = getToken();
    await fetch("http://localhost:8080/api/lists", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ name: newListName, boardId: id }),
    });

    setNewListName("");
    fetchBoard();
  };

  const createCard = async (listId, e) => {
    e.preventDefault();
    const input = cardInputs[listId];
    const title = input?.title;
    const description = input?.description || "";

    if (!title || !title.trim()) return;

    const token = getToken();
    await fetch("http://localhost:8080/api/cards", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ title, description, listId }),
    });

    setCardInputs((prev) => ({
      ...prev,
      [listId]: { title: "", description: "" },
    }));
    fetchBoard();
  };

  const handleDeleteCard = async (cardId) => {
    try {
      const token = getToken();
      await fetch(`http://localhost:8080/api/cards/${cardId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      setCardToDelete(null);
      fetchBoard();
    } catch (err) {
      console.error("Failed to delete card:", err);
    }
  };

  const handleDeleteList = async (listId) => {
    try {
      const token = getToken();
      await fetch(`http://localhost:8080/api/lists/${listId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      setListToDelete(null);
      fetchBoard();
    } catch (err) {
      console.error("Failed to delete list:", err);
    }
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = getToken();
      await fetch(`http://localhost:8080/api/cards/${editCard.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          title: editCard.title,
          description: editCard.description,
        }),
      });
      setEditCard(null);
      fetchBoard();
    } catch (err) {
      console.error("Failed to update card:", err);
    }
  };

  // ...rest of your render logic remains unchanged


  // Local move (drag & drop, not persisted yet)
  const moveCard = (cardId, listId, toIndex) => {
    setBoard((prevBoard) => {
      const newBoard = { ...prevBoard };
      const list = newBoard.cardLists.find((l) => l.id === listId);
      if (!list) return prevBoard;

      const currentIndex = list.cards.findIndex((c) => c.id === cardId);
      if (currentIndex === -1) return prevBoard;

      const updatedCards = [...list.cards];
      const [movedCard] = updatedCards.splice(currentIndex, 1);
      updatedCards.splice(toIndex, 0, movedCard);

      list.cards = updatedCards;

      return newBoard;
    });
  };

  if (loading) return <div className="p-6 text-muted-foreground">Loading...</div>;
  if (!board) return <div className="p-6 text-destructive">Board not found.</div>;

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">{board.name}</h1>

      {/* Add list */}
      <form onSubmit={createList} className="mb-6 flex gap-4">
        <Input
          placeholder="New list name"
          value={newListName}
          onChange={(e) => setNewListName(e.target.value)}
          className="w-64"
        />
        <Button type="submit">Add List</Button>
      </form>

      {/* Lists and cards */}
      <DndProvider backend={HTML5Backend}>
        <div className="flex gap-4 items-start overflow-x-auto pb-4">
          {board.cardLists?.map((list) => (
            <div
              key={list.id}
              className="bg-muted p-4 rounded-2xl shadow w-64 flex-shrink-0 flex flex-col max-h-[80vh] overflow-y-auto"
            >
              {/* List header */}
              <div className="flex justify-between items-center mb-3">
                <h2 className="text-lg font-semibold">{list.name}</h2>
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" size="icon" className="h-6 w-6">
                      <MoreVertical size={16} />
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end">
                    <DropdownMenuItem
                      onClick={() => setListToDelete(list)}
                      className="text-red-600"
                    >
                      <Trash size={14} className="mr-2" /> Delete List
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </div>

              {/* Cards */}
              <div className="flex flex-col gap-2 mb-4">
                {list.cards?.map((card, index) => {
                  const cardLabel = card.labels?.[0]?.name?.toLowerCase();
                  const highlightClass =
                    cardLabel === "done"
                      ? "border-green-500"
                      : cardLabel === "urgent"
                      ? "border-red-500"
                      : "";

                  return (
                    <div key={card.id} className={`border-2 rounded-xl ${highlightClass}`}>
                      <DragCard
                        card={card}
                        index={index}
                        listId={list.id}
                        moveCard={moveCard}
                        onEdit={() => setEditCard(card)}
                        onDelete={() => setCardToDelete(card)}
                        fetchBoard={fetchBoard}
                        onOpenDetails={() => setSelectedCard(card)}
                      />
                    </div>
                  );
                })}
              </div>

              {/* Add card */}
              <form onSubmit={(e) => createCard(list.id, e)} className="flex flex-col gap-2">
                <Input
                  placeholder="Card title"
                  value={cardInputs[list.id]?.title || ""}
                  onChange={(e) => setCardInputs((prev) => ({
                    ...prev,
                    [list.id]: { ...prev[list.id], title: e.target.value }
                  }))}
                />
                <Textarea
                  placeholder="Card description (optional)"
                  value={cardInputs[list.id]?.description || ""}
                  onChange={(e) => setCardInputs((prev) => ({
                    ...prev,
                    [list.id]: { ...prev[list.id], description: e.target.value }
                  }))}
                  rows={2}
                />
                <Button type="submit" size="sm">Add Card</Button>
              </form>
            </div>
          ))}
        </div>
      </DndProvider>

      {/* Edit card dialog */}
      {editCard && (
        <Dialog open={true} onOpenChange={() => setEditCard(null)}>
          <DialogContent>
            <form onSubmit={handleEditSubmit} className="flex flex-col gap-4">
              <Input
                value={editCard.title}
                onChange={(e) => setEditCard({ ...editCard, title: e.target.value })}
                placeholder="Title"
              />
              <Textarea
                value={editCard.description}
                onChange={(e) => setEditCard({ ...editCard, description: e.target.value })}
                placeholder="Description"
                rows={3}
              />
              <div className="flex justify-end gap-2">
                <Button type="button" variant="outline" onClick={() => setEditCard(null)}>
                  Cancel
                </Button>
                <Button type="submit">Save</Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      )}

      {/* Delete card dialog */}
      <AlertDialog open={cardToDelete !== null} onOpenChange={() => setCardToDelete(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete this card?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete
              <strong> {cardToDelete?.title}</strong>.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={() => setCardToDelete(null)}>Cancel</AlertDialogCancel>
            <AlertDialogAction
              className="bg-red-600 hover:bg-red-700 text-white"
              onClick={() => handleDeleteCard(cardToDelete.id)}
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Delete list dialog */}
      <AlertDialog open={listToDelete !== null} onOpenChange={() => setListToDelete(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete this list?</AlertDialogTitle>
            <AlertDialogDescription>
              This will delete the entire list <strong>{listToDelete?.name}</strong> and all its cards.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={() => setListToDelete(null)}>Cancel</AlertDialogCancel>
            <AlertDialogAction
              className="bg-red-600 hover:bg-red-700 text-white"
              onClick={() => handleDeleteList(listToDelete.id)}
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Card details drawer */}
      {selectedCard && (
        <CardDetailsSheet card={selectedCard} onClose={() => setSelectedCard(null)} />
      )}
    </div>
  );
}
