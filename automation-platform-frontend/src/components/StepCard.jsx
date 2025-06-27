import React from "react";
import { Trash2, GripVertical, SquarePen } from "lucide-react";
import { useSelector } from "react-redux";

const StepCard = ({
  step,
  index,
  onDragStart,
  onDragOver,
  onDrop,
  onDeleteStep,
  isDraggable,
  // onEdit,
  onClick,
}) => {
  const isTrigger = step.type === "trigger";
  const services = useSelector((state) => state.services.apps);
  const selectedApp = services.find((app) => step.app == app.id);
  const selectedEvent = step.type
    ? step.type === "trigger"
      ? selectedApp.triggerDefinitions.find((event) => event.id === step.event)
      : selectedApp.actionDefinitions.find((event) => event.id === step.event)
    : null;

  return (
    <div
      className={`flex items-center justify-between p-5 mb-3 rounded-xl bg-white shadow-sm transition-all hover:cursor-pointer duration-300 ${
        isTrigger
          ? "border-l-8 border-blue-600"
          : "border-l-8 border-purple-400 ml-8"
      } ${
        isDraggable
          ? "cursor-grab hover:scale-[1.01] hover:shadow-md"
          : "cursor-default"
      }`}
      draggable={isDraggable}
      onDragStart={(e) => onDragStart(e, index)}
      onDragOver={onDragOver}
      onDrop={(e) => onDrop(e, index)}
      onClick={() => onClick(index)}
    >
      <div className="flex items-center flex-grow">
        {!isTrigger && isDraggable && (
          <GripVertical
            className="mr-3 text-gray-400 flex-shrink-0 cursor-grab"
            size={24}
          />
        )}

        <div>
          <p
            className={`text-xs font-semibold uppercase tracking-wide ${
              isTrigger ? "text-blue-700" : "text-purple-600"
            }`}
          >
            {index + 1}
            {". "}
            {isTrigger ? "Trigger" : "Action"}
          </p>

          <h4 className="mt-1 text-base font-medium text-gray-800 leading-tight">
            <span className="font-semibold text-gray-900">
              {selectedApp.name}
            </span>
            :{" "}
            <span className="font-normal text-gray-700">
              {selectedEvent ? selectedEvent.name : "Choose an event"}
            </span>
          </h4>
        </div>
      </div>

      {!isTrigger && (
        <button
          title="Delete Step"
          onClick={() => onDeleteStep(step.id)}
          className="ml-4 p-2 flex-shrink-0 cursor-pointer rounded-full text-gray-400 hover:text-red-500 hover:bg-red-50 transition-colors duration-200"
        >
          <Trash2 size={20} />
        </button>
      )}
    </div>
  );
};

export default StepCard;
