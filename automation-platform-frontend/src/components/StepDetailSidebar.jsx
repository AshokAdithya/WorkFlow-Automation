import React from "react";
import { X, Zap } from "lucide-react";
import { useSelector } from "react-redux";

const StepDetailSidebar = ({
  isOpen,
  index,
  chooseService,
  chooseEvent,
  onClose,
}) => {
  const services = useSelector((state) => state.services.apps);
  const workflow = useSelector((state) => state.workflow);
  if (!isOpen && !index) return null;
  const step = workflow.steps[index];
  const selectedApp = services.find((app) => app.id === step.app);
  const selectedEvent = step.event
    ? step.event === "trigger"
      ? selectedApp.triggers.find((event) => event.id === step.event)
      : selectedApp.actions.find((event) => event.id === step.event)
    : null;

  return (
    <div className="fixed top-6 right-6 z-20 w-[400px] max-h-[85vh] bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
      {/* Header */}
      <div className="flex items-center justify-between px-4 py-3 bg-gray-100 border-b border-gray-200">
        <div className="flex items-center gap-2">
          <div className="h-6 w-6 rounded bg-white shadow-inner flex items-center justify-center">
            <Zap size={16} className="text-indigo-600" />
          </div>
          <h3 className="text-sm font-semibold text-gray-800">
            {index + 1}
            {". "}
            {step.type === "trigger" ? "Trigger" : "Action"}
          </h3>
        </div>
        <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
          <X size={18} />
        </button>
      </div>

      {/* Body */}
      <div className="p-4 space-y-5 overflow-y-auto text-sm">
        <div>
          <label className="block text-gray-600 font-medium mb-1">App *</label>
          <div className="flex items-center justify-between border px-3 py-2 rounded-md bg-gray-50">
            <div className="flex items-center gap-2">
              <Zap size={14} className="text-indigo-600" />
              <span className="text-gray-800">{selectedApp.name}</span>
            </div>
            <button
              className="text-indigo-600 text-xs font-medium hover:cursor-pointer"
              onClick={() => chooseService(step)}
            >
              Change
            </button>
          </div>
        </div>
        <label className="block text-gray-600 font-medium mb-1">
          {step.type === "trigger" ? "Trigger" : "Action"} *
        </label>
        <div className="flex items-center justify-between border px-3 py-2 rounded-md bg-gray-50">
          <div className="flex items-center gap-2">
            <Zap size={14} className="text-indigo-600" />
            <span className="text-gray-800">
              {selectedEvent ? selectedEvent.name : "Choose an event"}
            </span>
          </div>
          <button
            className="text-indigo-600 text-xs font-medium  hover:underline"
            onClick={() => chooseEvent(step)}
          >
            {selectedEvent ? "Change" : "Choose"}
          </button>
        </div>

        <div>
          <label className="block text-gray-600 font-medium mb-1">
            Account *
          </label>
          <div className="flex items-center justify-between border px-3 py-2 rounded-md bg-gray-50">
            <span className="truncate text-gray-800">Connected Account</span>
            <button className="text-indigo-600 text-xs font-medium hover:underline">
              Change
            </button>
          </div>
        </div>

        <p className="text-xs text-gray-500 leading-snug">
          Your credentials are stored securely.{" "}
          <a href="#" className="text-indigo-600 hover:underline">
            Manage accounts
          </a>
          .
        </p>
      </div>
    </div>
  );
};

export default StepDetailSidebar;
