import React from "react";
import { X, Zap } from "lucide-react";
import { useSelector } from "react-redux";
import { ClipboardCopy } from "lucide-react";
import { toast } from "react-toastify";

const StepDetailSidebar = ({
  isOpen,
  index,
  chooseService,
  chooseEvent,
  onClose,
  updateConfig,
  openOAuthPopup,
}) => {
  const services = useSelector((state) => state.services.apps);
  const workflow = useSelector((state) => state.workflow);
  const step = workflow.steps[index];
  if (!isOpen || !step) return null;
  const selectedApp = services.find((app) => app.id === step.app);
  const selectedEvent = step.type
    ? step.type === "trigger"
      ? selectedApp.triggerDefinitions.find((event) => event.id === step.event)
      : selectedApp.actionDefinitions.find((event) => event.id === step.event)
    : null;
  let parsedConfig = null;

  try {
    parsedConfig = selectedEvent?.configJson
      ? JSON.parse(selectedEvent.configJson)
      : null;
  } catch (e) {
    console.error("Failed to parse configJson", e);
  }

  const copyToClipboard = async (webhookUrl) => {
    await navigator.clipboard.writeText(webhookUrl);
    toast.success("Webhook url saved successfully");
  };

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
        {selectedApp.authType !== "NONE" && (
          <div>
            <label className="block text-gray-600 font-medium mb-1">
              Account *
            </label>
            <div className="flex items-center justify-between border px-3 py-2 rounded-md bg-gray-50">
              <span className="truncate text-gray-800">
                {selectedApp?.connected
                  ? "Change Account"
                  : `Connect ${selectedApp.name}`}
              </span>
              <button
                className="text-indigo-600 text-xs font-medium hover:underline"
                onClick={() => openOAuthPopup(selectedApp.identifier)}
              >
                {selectedApp?.connected ? "Change" : "Connect"}
              </button>
            </div>
          </div>
        )}

        <p className="text-xs text-gray-500 leading-snug">
          Your credentials are stored securely.{" "}
          <a href="#" className="text-indigo-600 hover:underline">
            Manage accounts
          </a>
          .
        </p>

        {step?.webhookUrl && (
          <div className="relative bg-violet-50 border border-violet-200 p-4 rounded-xl space-y-2 w-full max-w-lg">
            <h3 className="font-semibold text-sm text-gray-800">
              Your webhook URL
            </h3>
            <p className="text-sm text-gray-500">
              You’ll need to configure your application with this webhook URL.
            </p>

            <div className="relative flex items-center border border-gray-200 rounded-lg bg-white pl-3 pr-16 py-2">
              <img
                src="/zapier-icon.svg"
                alt="zapier"
                className="w-5 h-5 mr-2"
              />
              <span className="text-sm text-gray-700 truncate">
                http://localhost:8080/catch/hooks/
                {step.webhookUrl}
              </span>

              <button
                className="absolute right-2 top-1/2 -translate-y-1/2 bg-gray-100 hover:bg-gray-200 text-sm text-gray-700 px-3 py-1 border border-gray-300 rounded-md shadow-sm"
                onClick={() =>
                  copyToClipboard(
                    `http://localhost:8080/catch/hooks/${step.webhookUrl}`
                  )
                }
              >
                Copy
              </button>
            </div>
          </div>
        )}
        {parsedConfig?.fields?.map((field) => (
          <div key={field.name}>
            <label className="block text-gray-600 font-medium mb-1">
              {field.label} {field.required && "*"}
            </label>
            {field.type === "textarea" ? (
              <textarea
                required={field.required}
                className="w-full border px-3 py-2 rounded-md bg-gray-50 mb-4"
                placeholder={field.label}
                value={step.inputConfig?.[field.name] || ""}
                onChange={(e) => updateConfig(step, field, e)}
              />
            ) : (
              <input
                type={field.type}
                required={field.required}
                className="w-full border px-3 py-2 rounded-md bg-gray-50 mb-4"
                placeholder={field.label}
                value={step.inputConfig?.[field.name] || ""}
                onChange={(e) => updateConfig(step, field, e)}
              />
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default StepDetailSidebar;
