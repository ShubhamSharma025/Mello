"use client";

import { useEffect, useRef, useState, useMemo } from "react";
import { useDrag, useDrop } from "react-dnd";
import { Button } from "@/components/ui/button";
import { MoreVertical } from "lucide-react";
import { Checkbox } from "@/components/ui/checkbox";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";

const ItemType = "CARD";

export function DragCard({
  card,
  index,
  listId,
  moveCard,
  onEdit,
  onDelete,
  fetchBoard,
  onOpenDetails,
}) {
  const ref = useRef(null);
  const [checklist, setChecklist] = useState([]);
  const [labels, setLabels] = useState([]);

  const getToken = () =>
    typeof window !== "undefined" ? localStorage.getItem("jwtToken") : null;

  const authHeaders = () => {
    const token = getToken();
    return token
      ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
      : { "Content-Type": "application/json" };
  };

  // Drag and drop
  const [, drop] = useDrop({
    accept: ItemType,
    hover(item) {
      if (!ref.current) return;
      const dragIndex = item.index;
      const hoverIndex = index;
      const sourceListId = item.listId;
      if (dragIndex === hoverIndex && sourceListId === listId) return;

      moveCard(item.id, listId, hoverIndex);
      item.index = hoverIndex;
      item.listId = listId;
    },
    async drop(item) {
      try {
        await fetch("http://localhost:8080/api/cards/reorder", {
          method: "PUT",
          headers: authHeaders(),
          body: JSON.stringify({
            cardId: item.id,
            targetListId: listId,
            newPosition: index,
          }),
        });

        if (typeof fetchBoard === "function") fetchBoard();
      } catch (err) {
        console.error("Failed to persist card reorder:", err);
      }
    },
  });

  const [{ isDragging }, drag] = useDrag({
    type: ItemType,
    item: { id: card.id, index, listId },
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
    }),
  });

  drag(drop(ref));

  // Fetch checklist
  useEffect(() => {
    const fetchChecklist = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/api/checklist/card/${card.id}`,
          { headers: authHeaders() }
        );
        const data = await res.json();
        setChecklist(data);
      } catch (err) {
        console.error("Failed to fetch checklist:", err);
      }
    };
    fetchChecklist();
  }, [card.id]);

  // Fetch labels
  useEffect(() => {
    const fetchLabels = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/api/labels/card/${card.id}`,
          { headers: authHeaders() }
        );
        const data = await res.json();
        setLabels(data);
      } catch (err) {
        console.error("Failed to fetch labels:", err);
      }
    };
    fetchLabels();
  }, [card.id]);

  // Card color based on label
  const cardColor = useMemo(() => {
    const hasDone = labels.some((l) => l.name?.toLowerCase() === "done");
    const hasUrgent = labels.some((l) => l.name?.toLowerCase() === "urgent");
    if (hasUrgent) return "bg-red-100";
    if (hasDone) return "bg-green-100";
    return "bg-white";
  }, [labels]);

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
        prev.map((i) =>
          i.id === item.id ? { ...i, checked: !i.checked } : i
        )
      );
    } catch (err) {
      console.error("Failed to toggle checkbox:", err);
    }
  };

  return (
    <div
      ref={ref}
      className={`p-3 rounded-lg shadow group transition-all duration-200 relative ${cardColor}`}
      style={{
        opacity: isDragging ? 0.5 : 1,
        cursor: "grab",
      }}
    >
      {/* Card Header */}
      <div className="flex justify-between items-start">
        <div className="flex-1 overflow-hidden">
          <h3 className="font-medium truncate">{card.title}</h3>
          {card.description && (
            <p className="text-sm text-muted-foreground truncate">
              {card.description}
            </p>
          )}
          {card.dueDate && (
            <p className="text-xs text-green-700 mt-1">Due: {card.dueDate}</p>
          )}
        </div>

        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="icon" className="h-6 w-6">
              <MoreVertical size={16} />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuItem onClick={onEdit}>Edit</DropdownMenuItem>
            <DropdownMenuItem onClick={onDelete} className="text-red-600">
              Delete
            </DropdownMenuItem>
            <DropdownMenuItem onClick={() => onOpenDetails?.(card)}>
              Open Details
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>

      {/* Checklist Items */}
      {checklist.length > 0 && (
        <div className="mt-3 space-y-2 group-hover:translate-x-1 transition-all">
          {checklist.map((item) => (
            <div key={item.id} className="flex items-center gap-2">
              <Checkbox
                checked={item.checked}
                onCheckedChange={() => handleToggle(item)}
              />
              <span
                className={`text-sm ${
                  item.checked ? "line-through text-muted-foreground" : ""
                }`}
              >
                {item.content}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
