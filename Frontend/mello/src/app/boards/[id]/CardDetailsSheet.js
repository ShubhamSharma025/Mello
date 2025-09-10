"use client";

import { useEffect, useState, useMemo } from "react";
import { X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";

export default function CardDetailsSheet({ card, onClose }) {
  const [checklist, setChecklist] = useState([]);
  const [newCheckItem, setNewCheckItem] = useState("");
  const [labels, setLabels] = useState([]);
  const [newLabel, setNewLabel] = useState("");
  const [labelColor, setLabelColor] = useState("#3b82f6"); // Default blue
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");

  const getToken = () =>
    typeof window !== "undefined" ? localStorage.getItem("jwtToken") : null;

  const authHeaders = () => {
    const token = getToken();
    return token
      ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
      : { "Content-Type": "application/json" };
  };

  // ✅ Fetch card details (checklist, labels, comments)
  useEffect(() => {
    if (!card?.id) return;

    const fetchData = async () => {
      try {
        const [checkRes, labelRes, commentRes] = await Promise.all([
          fetch(`http://localhost:8080/api/checklist/card/${card.id}`, {
            headers: authHeaders(),
          }),
          fetch(`http://localhost:8080/api/labels/card/${card.id}`, {
            headers: authHeaders(),
          }),
          fetch(`http://localhost:8080/api/comments/card/${card.id}`, {
            headers: authHeaders(),
          }),
        ]);

        const checkData = checkRes.ok ? await checkRes.json() : [];
        const labelData = labelRes.ok ? await labelRes.json() : [];
        const commentData = commentRes.ok ? await commentRes.json() : [];

        setChecklist(checkData);
        setLabels(labelData);
        setComments(commentData);
      } catch (err) {
        console.error("Failed to fetch card data:", err);
      }
    };

    fetchData();
  }, [card?.id]);

  // ✅ Checklist toggle
  const handleToggle = async (item) => {
    try {
      await fetch(`http://localhost:8080/api/checklist/${item.id}`, {
        method: "PUT",
        headers: authHeaders(),
        body: JSON.stringify({
          content: item.content,
          checked: !item.checked,
        }),
      });
      setChecklist((prev) =>
        prev.map((i) => (i.id === item.id ? { ...i, checked: !i.checked } : i))
      );
    } catch (err) {
      console.error("Failed to toggle checkbox:", err);
    }
  };

  // ✅ Delete checklist
  const handleDeleteChecklist = async (id) => {
    try {
      await fetch(`http://localhost:8080/api/checklist/${id}`, {
        method: "DELETE",
        headers: authHeaders(),
      });
      setChecklist((prev) => prev.filter((item) => item.id !== id));
    } catch (err) {
      console.error("Failed to delete checklist item:", err);
    }
  };

  // ✅ Add checklist
  const handleAddChecklist = async () => {
    if (!newCheckItem.trim()) return;
    try {
      const res = await fetch("http://localhost:8080/api/checklist", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({
          cardId: card.id,
          content: newCheckItem,
        }),
      });
      if (!res.ok) throw new Error("Failed to add checklist");
      const newItem = await res.json();
      setChecklist([...checklist, newItem]);
      setNewCheckItem("");
    } catch (err) {
      console.error("Failed to add checklist item:", err);
    }
  };

  // ✅ Add label (replace existing one)
  const handleAddLabel = async () => {
    if (!newLabel.trim()) return;

    const labelName = newLabel.toLowerCase().trim();
    let colorToUse = labelColor;

    if (labelName === "done") {
      colorToUse = "#22c55e";
    } else if (labelName === "urgent") {
      colorToUse = "#ef4444";
    }

    try {
      if (labels.length > 0) {
        await fetch(`http://localhost:8080/api/labels/${labels[0].id}`, {
          method: "DELETE",
          headers: authHeaders(),
        });
      }

      const res = await fetch("http://localhost:8080/api/labels", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({
          cardId: card.id,
          name: newLabel,
          color: colorToUse,
        }),
      });
      if (!res.ok) throw new Error("Failed to add label");
      const newLbl = await res.json();
      setLabels([newLbl]);
      setNewLabel("");
    } catch (err) {
      console.error("Failed to add label:", err);
    }
  };

  // ✅ Add comment
  const handleAddComment = async () => {
    if (!newComment.trim()) return;
    try {
      const res = await fetch("http://localhost:8080/api/comments", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({
          cardId: card.id,
          content: newComment,
        }),
      });
      if (!res.ok) throw new Error("Failed to add comment");
      const comment = await res.json();
      setComments([...comments, comment]);
      setNewComment("");
    } catch (err) {
      console.error("Failed to add comment:", err);
    }
  };

  // ✅ Highlight color by label
  const cardHighlightColor = useMemo(() => {
    const labelName = labels[0]?.name?.toLowerCase();
    if (labelName === "urgent") return "border-red-500 ring-red-500";
    if (labelName === "done") return "border-green-500 ring-green-500";
    return "";
  }, [labels]);

  if (!card) return null;

  return (
    <div className="fixed inset-0 bg-black/30 z-50 flex justify-end">
      <div
        className={`bg-white dark:bg-zinc-900 w-full sm:w-[400px] h-full p-6 overflow-y-auto shadow-xl border-2 ${cardHighlightColor}`}
      >
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold">{card.title}</h2>
          <Button variant="ghost" size="icon" onClick={onClose}>
            <X />
          </Button>
        </div>

        {/* Description */}
        {card.description && (
          <div className="mb-4">
            <h3 className="text-sm font-medium text-muted-foreground mb-1">Description</h3>
            <p className="text-base">{card.description}</p>
          </div>
        )}

        {/* Due Date */}
        {card.dueDate && (
          <div className="mb-4">
            <h3 className="text-sm font-medium text-muted-foreground mb-1">Due Date</h3>
            <p className="text-sm text-green-600">{card.dueDate}</p>
          </div>
        )}

        {/* Checklist */}
        <div className="mb-6">
          <h3 className="text-sm font-medium text-muted-foreground mb-2">Checklist</h3>
          <div className="space-y-2">
            {checklist.map((item) => (
              <div key={item.id} className="flex items-center justify-between gap-2">
                <div className="flex items-center gap-2">
                  <Checkbox
                    checked={item.checked}
                    onCheckedChange={() => handleToggle(item)}
                  />
                  <span className={`text-sm ${item.checked ? "line-through text-muted-foreground" : ""}`}>
                    {item.content}
                  </span>
                </div>
                <Button
                  size="icon"
                  variant="ghost"
                  onClick={() => handleDeleteChecklist(item.id)}
                  title="Delete item"
                >
                  🗑️
                </Button>
              </div>
            ))}
            <div className="flex gap-2 mt-2">
              <Input
                value={newCheckItem}
                onChange={(e) => setNewCheckItem(e.target.value)}
                placeholder="New item"
              />
              <Button size="sm" onClick={handleAddChecklist}>
                Add
              </Button>
            </div>
          </div>
        </div>

        {/* Label */}
        <div className="mb-6">
          <h3 className="text-sm font-medium text-muted-foreground mb-1">Label</h3>
          {labels.length > 0 && (
            <div className="mb-2">
              <span
                className="px-2 py-1 text-white text-xs rounded"
                style={{ backgroundColor: labels[0].color || "#3b82f6" }}
              >
                {labels[0].name}
              </span>
            </div>
          )}
          <div className="flex gap-2 mb-1">
            <Input
              value={newLabel}
              onChange={(e) => setNewLabel(e.target.value)}
              placeholder="Label name"
            />
            <Input
              type="color"
              value={labelColor}
              onChange={(e) => setLabelColor(e.target.value)}
              className="w-10 p-1"
              title="Label color"
              disabled={
                newLabel.toLowerCase() === "done" ||
                newLabel.toLowerCase() === "urgent"
              }
            />
            <Button size="sm" onClick={handleAddLabel}>
              Add
            </Button>
          </div>
        </div>

        {/* Comments */}
        <div className="mb-6">
          <h3 className="text-sm font-medium text-muted-foreground mb-1">Comments</h3>
          <div className="space-y-2 mb-2">
            {comments.map((comment) => (
              <div key={comment.id} className="text-sm bg-muted p-2 rounded">
                {comment.content}
              </div>
            ))}
          </div>
          <Textarea
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder="Write a comment..."
            className="mb-2"
          />
          <Button size="sm" onClick={handleAddComment}>
            Post
          </Button>
        </div>
      </div>
    </div>
  );
}
