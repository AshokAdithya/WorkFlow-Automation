import React, { useState, useEffect, useRef } from "react";
import {
  Settings,
  PlusCircle,
  Trash2,
  Power,
  Zap,
  GripVertical,
} from "lucide-react";
import { api } from "../api/Api";

const StepCard = ({
  step,
  index,
  onDragStart,
  onDragOver,
  onDrop,
  onDeleteStep,
  onUpdateStep,
  isDraggable,
  supportedApplications,
}) => {
  const isTrigger = step.type === "trigger";
  const [isEditingApp, setIsEditingApp] = useState(false);
  const [isEditingEvent, setIsEditingEvent] = useState(false);

  const appInputRef = useRef(null);
  const eventInputRef = useRef(null);

  const selectedApp = supportedApplications.find(
    (app) => app.name === step.app
  );
  const availableEvents = isTrigger
    ? selectedApp?.triggers || []
    : selectedApp?.actions || [];

  const handleAppChange = (e) => {
    const newAppName = e.target.value;
    const newApp = supportedApplications.find((app) => app.name === newAppName);
    let newEventName = "";

    if (newApp) {
      if (isTrigger && newApp.triggers.length > 0) {
        newEventName = newApp.triggers[0].name;
      } else if (!isTrigger && newApp.actions.length > 0) {
        newEventName = newApp.actions[0].name;
      }
    }
    onUpdateStep(step.id, "app", newAppName);
    onUpdateStep(step.id, "event", newEventName);
    // Persist the actual identifier for backend
    onUpdateStep(step.id, "appIntegrationId", newApp ? newApp.id : null);
    onUpdateStep(
      step.id,
      isTrigger ? "triggerTypeIdentifier" : "actionTypeIdentifier",
      newEventName
        ? isTrigger
          ? newApp.triggers.find((t) => t.name === newEventName)
              ?.triggerTypeIdentifier
          : newApp.actions.find((a) => a.name === newEventName)
              ?.actionTypeIdentifier
        : null
    );
  };

  const handleEventChange = (e) => {
    const newEventName = e.target.value;
    onUpdateStep(step.id, "event", newEventName);
    // Persist the actual identifier for backend
    const eventIdentifier = newEventName
      ? isTrigger
        ? selectedApp.triggers.find((t) => t.name === newEventName)
            ?.triggerTypeIdentifier
        : selectedApp.actions.find((a) => a.name === newEventName)
            ?.actionTypeIdentifier
      : null;
    onUpdateStep(
      step.id,
      isTrigger ? "triggerTypeIdentifier" : "actionTypeIdentifier",
      eventIdentifier
    );
  };

  useEffect(() => {
    if (isEditingApp && appInputRef.current) {
      appInputRef.current.focus();
    }
  }, [isEditingApp]);

  useEffect(() => {
    if (isEditingEvent && eventInputRef.current) {
      eventInputRef.current.focus();
    }
  }, [isEditingEvent]);

  return (
    <div
      className={`bg-white rounded-xl shadow-sm p-5 mb-3 flex items-center justify-between transition-all duration-300
        ${
          isTrigger
            ? "border-l-8 border-blue-600"
            : "border-l-8 border-purple-400 ml-8"
        }
        ${
          isDraggable
            ? "cursor-grab hover:scale-[1.01] hover:shadow-md"
            : "cursor-default"
        }
      `}
      draggable={isDraggable}
      onDragStart={(e) => onDragStart(e, index)}
      onDragOver={onDragOver}
      onDrop={(e) => onDrop(e, index)}
    >
      <div className="flex items-center flex-grow">
        {!isTrigger && isDraggable && (
          <GripVertical
            className="text-gray-400 mr-3 cursor-grab flex-shrink-0"
            size={24}
          />
        )}
        <div>
          <p
            className={`text-xs font-semibold tracking-wide uppercase ${
              isTrigger ? "text-blue-700" : "text-purple-600"
            }`}
          >
            {isTrigger ? "Trigger" : "Action"}
          </p>
          <h4 className="text-base font-medium text-gray-800 mt-1 leading-tight">
            {isEditingApp ? (
              <select
                ref={appInputRef}
                value={step.app}
                onChange={handleAppChange}
                onBlur={() => setIsEditingApp(false)}
                className="font-semibold text-gray-900 border-b border-gray-300 focus:outline-none focus:border-blue-500 w-32 bg-white rounded-md p-1"
                autoFocus
              >
                {supportedApplications.map((app) => (
                  <option key={app.id} value={app.name}>
                    {app.name}
                  </option>
                ))}
              </select>
            ) : (
              <span
                className="font-semibold text-gray-900 cursor-text"
                onClick={() => setIsEditingApp(true)}
              >
                {step.app}
              </span>
            )}
            :{" "}
            {isEditingEvent ? (
              <select
                ref={eventInputRef}
                value={step.event}
                onChange={handleEventChange}
                onBlur={() => setIsEditingEvent(false)}
                className="font-normal text-gray-700 border-b border-gray-300 focus:outline-none focus:border-blue-500 w-48 bg-white rounded-md p-1"
                autoFocus
              >
                {availableEvents.length > 0 ? (
                  availableEvents.map((event) => (
                    <option key={event.id} value={event.name}>
                      {event.name}
                    </option>
                  ))
                ) : (
                  <option value="">No Events Available</option>
                )}
              </select>
            ) : (
              <span
                className="font-normal text-gray-700 cursor-text"
                onClick={() => setIsEditingEvent(true)}
              >
                {step.event}
              </span>
            )}
          </h4>
        </div>
      </div>
      {!isTrigger && (
        <button
          title="Delete Step"
          onClick={() => onDeleteStep(step.id)}
          className="p-2 rounded-full text-gray-400 hover:bg-red-50 hover:text-red-500 transition-colors duration-200 ml-4 flex-shrink-0"
        >
          <Trash2 size={20} />
        </button>
      )}
    </div>
  );
};

const SingleWorkflowEditor = () => {
  const [workflow, setWorkflow] = useState({
    id: "workflow-1",
    name: "New Email to Slack Message",
    isEnabled: true,
    steps: [
      {
        id: "workflow-1-1",
        type: "trigger",
        app: "Gmail",
        event: "New Email",
        appIntegrationId: null,
        triggerTypeIdentifier: null,
      },
      {
        id: "workflow-1-2",
        type: "action",
        app: "Slack",
        event: "Send Channel Message",
        appIntegrationId: null,
        actionTypeIdentifier: null,
      },
      {
        id: "workflow-1-3",
        type: "action",
        app: "Google Sheets",
        event: "Add Row to Spreadsheet",
        appIntegrationId: null,
        actionTypeIdentifier: null,
      },
    ],
  });
  const [isEditingWorkflowName, setIsEditingWorkflowName] = useState(false);
  const [supportedApplications, setSupportedApplications] = useState([]);
  const [loadingApps, setLoadingApps] = useState(true);
  const [errorLoadingApps, setErrorLoadingApps] = useState(null);

  // Fetch supported applications from backend using Axios
  useEffect(() => {
    const fetchApplications = async () => {
      try {
        const response = await api.get("/app-integrations");
        let data = response.data; // Axios wraps the response in a 'data' property
        console.log(data.response);

        // IMPORTANT: Add a check to ensure 'data' is an array
        if (!Array.isArray(data)) {
          console.warn(
            "API response for app-integrations was not an array. Setting to empty array.",
            data
          );
          data = []; // Default to an empty array to prevent 'find' errors
        }

        setSupportedApplications(data);

        setWorkflow((prevWorkflow) => {
          const updatedSteps = prevWorkflow.steps.map((step) => {
            const foundApp = data.find((app) => app.name === step.app);
            if (foundApp) {
              const appIntegrationId = foundApp.id;
              const events =
                step.type === "trigger" ? foundApp.triggers : foundApp.actions;
              const foundEvent = events.find(
                (event) => event.name === step.event
              );

              let eventIdentifier = null;
              if (foundEvent) {
                eventIdentifier =
                  step.type === "trigger"
                    ? foundEvent.triggerTypeIdentifier
                    : foundEvent.actionTypeIdentifier;
              } else if (events.length > 0) {
                const defaultEvent = events[0];
                step.event = defaultEvent.name;
                eventIdentifier =
                  step.type === "trigger"
                    ? defaultEvent.triggerTypeIdentifier
                    : defaultEvent.actionTypeIdentifier;
              }

              return {
                ...step,
                appIntegrationId: appIntegrationId,
                [step.type === "trigger"
                  ? "triggerTypeIdentifier"
                  : "actionTypeIdentifier"]: eventIdentifier,
              };
            }
            return step;
          });
          return { ...prevWorkflow, steps: updatedSteps };
        });
      } catch (error) {
        console.error("Error fetching applications:", error);
        setErrorLoadingApps(error.message); // Axios errors have a 'message' property
      } finally {
        setLoadingApps(false);
      }
    };
    fetchApplications();
  }, []);

  const handleWorkflowNameChange = (e) => {
    setWorkflow((prevWorkflow) => ({
      ...prevWorkflow,
      name: e.target.value,
    }));
  };

  const [draggedItem, setDraggedItem] = useState(null);

  const handleToggleWorkflow = () => {
    setWorkflow((prevWorkflow) => ({
      ...prevWorkflow,
      isEnabled: !prevWorkflow.isEnabled,
    }));
  };

  const handleDragStart = (e, index) => {
    if (index === 0) {
      e.preventDefault();
      return;
    }
    setDraggedItem(workflow.steps[index]);
    e.dataTransfer.effectAllowed = "move";
    e.dataTransfer.setData("text/plain", index);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    e.dataTransfer.dropEffect = "move";
  };

  const handleDrop = (e, targetIndex) => {
    e.preventDefault();
    const sourceIndex = parseInt(e.dataTransfer.getData("text/plain"), 10);

    if (targetIndex === 0 || sourceIndex === 0) {
      return;
    }

    const newSteps = [...workflow.steps];
    const [removed] = newSteps.splice(sourceIndex, 1);
    newSteps.splice(targetIndex, 0, removed);

    setWorkflow((prevWorkflow) => ({
      ...prevWorkflow,
      steps: newSteps,
    }));
    setDraggedItem(null);
  };

  const handleAddAction = () => {
    let defaultApp = "Unknown App";
    let defaultEvent = "New Event";
    let defaultAppIntegrationId = null;
    let defaultActionTypeIdentifier = null;

    if (supportedApplications.length > 0) {
      const firstApp = supportedApplications[0];
      defaultApp = firstApp.name;
      defaultAppIntegrationId = firstApp.id;
      if (firstApp.actions && firstApp.actions.length > 0) {
        defaultEvent = firstApp.actions[0].name;
        defaultActionTypeIdentifier = firstApp.actions[0].actionTypeIdentifier;
      } else if (firstApp.triggers && firstApp.triggers.length > 0) {
        defaultEvent = firstApp.triggers[0].name;
        defaultActionTypeIdentifier =
          firstApp.triggers[0].triggerTypeIdentifier;
      }
    }

    setWorkflow((prevWorkflow) => ({
      ...prevWorkflow,
      steps: [
        ...prevWorkflow.steps,
        {
          id: `${prevWorkflow.id}-${prevWorkflow.steps.length + 1}`,
          type: "action",
          app: defaultApp,
          event: defaultEvent,
          appIntegrationId: defaultAppIntegrationId,
          actionTypeIdentifier: defaultActionTypeIdentifier,
        },
      ],
    }));
  };

  const handleDeleteStep = (stepIdToDelete) => {
    setWorkflow((prevWorkflow) => ({
      ...prevWorkflow,
      steps: prevWorkflow.steps.filter((step) => step.id !== stepIdToDelete),
    }));
  };

  const handleUpdateStep = (stepId, field, value) => {
    setWorkflow((prevWorkflow) => ({
      ...prevWorkflow,
      steps: prevWorkflow.steps.map((s) =>
        s.id === stepId ? { ...s, [field]: value } : s
      ),
    }));
  };

  return (
    <div className="bg-white rounded-3xl shadow-2xl p-8 mb-4 transition-all duration-300 transform hover:scale-[1.005]">
      <div className="flex items-center justify-between pb-6 border-b border-gray-100 mb-6">
        <div className="flex items-center">
          <Zap className="text-blue-600 mr-4 flex-shrink-0" size={32} />
          {isEditingWorkflowName ? (
            <input
              type="text"
              value={workflow.name}
              onChange={handleWorkflowNameChange}
              onBlur={() => setIsEditingWorkflowName(false)}
              onKeyDown={(e) => {
                if (e.key === "Enter") setIsEditingWorkflowName(false);
              }}
              className="text-2xl font-bold text-gray-900 leading-tight border-b border-gray-300 focus:outline-none focus:border-blue-500 w-80"
              autoFocus
            />
          ) : (
            <h3
              className="text-2xl font-bold text-gray-900 leading-tight cursor-text"
              onClick={() => setIsEditingWorkflowName(true)}
            >
              {workflow.name}
            </h3>
          )}
        </div>
        <div className="flex items-center space-x-4">
          <button
            onClick={handleToggleWorkflow}
            className={`relative inline-flex h-8 w-16 items-center rounded-full transition-colors duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2 ${
              workflow.isEnabled
                ? "bg-green-500 focus:ring-green-600"
                : "bg-gray-300 focus:ring-gray-400"
            }`}
            aria-checked={workflow.isEnabled}
            role="switch"
          >
            <span
              className={`inline-block h-6 w-6 transform rounded-full bg-white transition-transform duration-300 ${
                workflow.isEnabled ? "translate-x-9" : "translate-x-1"
              }`}
            />
            <span className="absolute inset-0 flex items-center justify-between px-1.5">
              {workflow.isEnabled ? (
                <Power className="text-white ml-0.5" size={16} />
              ) : (
                <Power className="text-gray-600 mr-0.5" size={16} />
              )}
            </span>
          </button>
          <button
            title="Workflow Settings"
            className="p-2 rounded-full text-gray-500 hover:bg-gray-100 hover:text-gray-700 transition-colors duration-200"
          >
            <Settings size={24} />
          </button>
        </div>
      </div>

      <div className="text-center mb-6 text-gray-600">
        {loadingApps ? (
          <p>Loading supported applications...</p>
        ) : errorLoadingApps ? (
          <p className="text-red-500">
            Error loading applications: {errorLoadingApps}. Please ensure your
            Spring Boot backend is running at http://localhost:8080.
          </p>
        ) : (
          <p className="text-sm font-medium">
            Currently supporting{" "}
            <span className="font-bold text-blue-600">
              {supportedApplications.length}
            </span>{" "}
            applications.
          </p>
        )}
      </div>

      <div className="bg-gray-50 rounded-2xl p-6 border border-gray-100">
        {workflow.steps.map((step, index) => (
          <StepCard
            key={step.id}
            step={step}
            index={index}
            onDragStart={handleDragStart}
            onDragOver={handleDragOver}
            onDrop={handleDrop}
            onDeleteStep={handleDeleteStep}
            onUpdateStep={handleUpdateStep}
            isDraggable={step.type === "action"}
            supportedApplications={supportedApplications}
          />
        ))}

        <button
          onClick={handleAddAction}
          className="mt-6 w-full flex items-center justify-center px-4 py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition-colors duration-200 text-base transform hover:scale-[1.01] focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          <PlusCircle size={20} className="mr-2" />
          Add Action
        </button>
      </div>
    </div>
  );
};

const App = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-100 to-purple-200 font-sans px-4 sm:px-6 md:px-12 lg:px-24 xl:px-48 py-8 sm:py-10 md:py-16">
      <script src="https://cdn.tailwindcss.com"></script>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap');
          body {
            font-family: 'Inter', sans-serif;
          }
        `}
      </style>

      <div className="w-full mx-auto max-w-4xl lg:max-w-6xl">
        <h1 className="text-4xl font-extrabold text-gray-900 mb-12 text-center drop-shadow-sm">
          My Workflow
        </h1>
        <SingleWorkflowEditor />
      </div>
    </div>
  );
};

export default App;
