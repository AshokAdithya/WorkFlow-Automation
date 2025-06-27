import React, { useState, useEffect, useRef } from "react";
import {
  Settings,
  PlusCircle,
  Trash2,
  Power,
  Zap,
  GripVertical,
  X,
  Search,
} from "lucide-react";
import { fetchServices } from "../redux/servicesSlice";
import { v4 as uuidv4 } from "uuid";
import { toast } from "react-toastify";
import { api } from "../api/Api";
import { useLocation } from "react-router-dom";

// Import Redux hooks and Provider
import { Provider, useSelector, useDispatch } from "react-redux";
import {
  setWorkflow,
  toggleWorkflowEnabled,
  setWorkflowName,
  addStep,
  deleteStep,
  updateStep,
  reorderSteps,
} from "../redux/WorkflowSlice";
import ServiceSelectionModal from "../components/ServiceSelectionModal";
import StepCard from "../components/StepCard";
import EventSelectionModal from "../components/EventSelectionModal";
import StepDetailSidebar from "../components/StepDetailSidebar";
import Button from "../components/Button";
const Workflow = () => {
  const dispatch = useDispatch();
  const location = useLocation();
  const { id } = location.state || {};
  const workflow = useSelector((state) => state.workflow);
  const {
    apps: supportedApplications,
    loading: loadingApps,
    error: errorLoadingApps,
  } = useSelector((state) => state.services);

  const [isEditingWorkflowName, setIsEditingWorkflowName] = useState(false);
  const [showServiceSelectionModal, setShowServiceSelectionModal] =
    useState(false);
  const [showEventSelectionModal, setShowEventSelectionModal] = useState(false);
  const [isFromSidebarForService, setIsFromSidebarForService] = useState(null);
  const [isFromSidebarForEvent, setIsFromSidebarForEvent] = useState(null);
  const [addingStepType, setAddingStepType] = useState(null);

  // const [originalStepType, setOriginalStepType] = useState(null);
  const [selectedStep, setSelectedStep] = useState(null);

  useEffect(() => {
    dispatch(fetchServices());

    const fetchWorkflow = async () => {
      if (!id) {
        dispatch(
          setWorkflow({
            id: "workflow-new-temp-" + Date.now(),
            name: "My New Workflow",
            enabled: false,
            steps: [],
          })
        );
      } else {
        try {
          const res = await api.get(`/workflows/${id}`);
          const stepsWithIds = res.data.steps.map((step) => ({
            ...step,
            inputConfig:
              typeof step.inputConfig === "string"
                ? JSON.parse(step.inputConfig)
                : step.inputConfig,
            id: `step-${Date.now()}-${Math.random().toString(36).substr(2, 5)}`,
          }));
          dispatch(setWorkflow({ ...res.data, steps: stepsWithIds }));
        } catch (err) {
          dispatch(
            setWorkflow({
              id: "workflow-new-temp-" + Date.now(),
              name: "My New Workflow",
              enabled: false,
              steps: [],
            })
          );
        }
      }
    };
    fetchWorkflow();

    const unloadCallback = (event) => {
      event.preventDefault();
      event.returnValue = "";
      return "";
    };

    window.addEventListener("beforeunload", unloadCallback);
    return () => window.removeEventListener("beforeunload", unloadCallback);
  }, [id]);

  //Handling Name change of the workflow
  const handleWorkflowNameChange = (e) => {
    dispatch(setWorkflowName(e.target.value));
  };

  //Handling Drag and Drop action
  const handleDragStart = (e, index) => {
    if (index === 0) {
      e.preventDefault();
      return;
    }
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

    dispatch(reorderSteps(newSteps));
  };

  const handleSelectStep = (index) => {
    setSelectedStep(index);
  };

  const openAddActionModal = () => {
    setAddingStepType("action");
    setShowServiceSelectionModal(true);
  };

  const openAddTriggerModal = () => {
    const hasTrigger =
      workflow.steps.length > 0 && workflow.steps[0].type === "trigger";
    if (!hasTrigger) {
      setAddingStepType("trigger");
      setShowServiceSelectionModal(true);
    } else {
      console.log("Trigger already exists for this workflow.");
    }
  };

  const handleSelectedService = (selectedApp, type) => {
    setShowServiceSelectionModal(false);
    const index = workflow.steps.length;
    const newStep = {
      id: `step-${Date.now()}-${Math.random().toString(36).substr(2, 5)}`,
      type: type,
      app: selectedApp.id,
      isConnected: false,
    };

    dispatch(addStep({ newStep, type }));
    if (index >= 0) {
      setTimeout(() => {
        setSelectedStep(index);
      }, 100);
    }
  };

  const handleDeleteStep = (stepIdToDelete) => {
    dispatch(deleteStep(stepIdToDelete));
    setTimeout(() => {
      setSelectedStep(null);
    }, 100);
  };

  const selectedStepNull = () => {
    setSelectedStep(null);
  };

  const handleSelectEventFromSidebar = (step) => {
    setIsFromSidebarForEvent(step);
    setShowEventSelectionModal(true);
  };

  const handleSelectServiceFromSidebar = (step) => {
    setIsFromSidebarForService(step);
    setAddingStepType(step.type);
    setShowServiceSelectionModal(true);
  };

  const refreshSelectedStep = (stepId) => {
    const updatedStepIndex = workflow.steps.findIndex((s) => s.id === stepId);
    if (updatedStepIndex !== -1) {
      setSelectedStep(updatedStepIndex);
    }
  };

  const handleEventSelectedAndAddStep = (step, event) => {
    dispatch(
      updateStep({
        stepId: step.id,
        field: "event",
        value: event.id,
      })
    );
    refreshSelectedStep(step.id);
    setShowEventSelectionModal(false);
    setIsFromSidebarForEvent(null);
  };

  const handleChangeConfig = (step, field, e) => {
    dispatch(
      updateStep({
        stepId: step.id,
        field: "inputConfig",
        value: {
          ...step.inputConfig,
          [field.name]: e.target.value,
        },
      })
    );
  };

  const handleServiceSelectedAndAddStep = (step, service) => {
    dispatch(
      updateStep({
        stepId: step.id,
        field: "app",
        value: service.id,
      })
    );
    dispatch(
      updateStep({
        stepId: step.id,
        field: "event",
        value: null,
      })
    );
    refreshSelectedStep(step.id);
    setShowServiceSelectionModal(false);
    setIsFromSidebarForService(null);
  };

  const handleSaveWorkflow = async () => {
    const payload = {
      id:
        typeof workflow.id === "string" &&
        workflow.id.startsWith("workflow-new-temp")
          ? null
          : workflow.id,
      name: workflow.name,
      enabled: workflow.enabled,
      steps: workflow.steps.map((s, i) => ({
        stepOrder: i,
        type: s.type,
        app: s.app,
        event: s.event,
        inputConfig: JSON.stringify(s.inputConfig || {}),
      })),
    };

    try {
      const res = await api.post("/workflows/save", payload);
      const stepsWithIds = res.data.steps.map((step) => ({
        ...step,
        inputConfig:
          typeof step.inputConfig === "string"
            ? JSON.parse(step.inputConfig)
            : step.inputConfig,
        id: `step-${Date.now()}-${Math.random().toString(36).substr(2, 5)}`,
      }));
      dispatch(setWorkflow({ ...res.data, steps: stepsWithIds }));
      toast.success("Workflow saved successfully");
    } catch (e) {
      console.error(e);
      toast.warn("Save failed: " + e.response?.data?.message);
    }
  };

  const toggleWorkflow = async (workflowId) => {
    try {
      const res = await api.patch(`/workflows/toggle/${workflowId}`, {});
      const { success, message } = res.data;

      console.log(res.data);

      dispatch(toggleWorkflowEnabled(success));
      toast.success(message);
    } catch (err) {
      dispatch(toggleWorkflowEnabled(false));
      toast.error(err.response?.data?.message || "Failed to toggle workflow");
    }
  };

  const openAuthServices = async (serviceIdentifier) => {
    const width = 700;
    const height = 700;
    const left = window.innerWidth / 2 - width;
    const top = window.innerHeight / 2 - height / 2;

    try {
      const response = await api.get(`/oauth/${serviceIdentifier}/authorize`);
      const authUrl = response.data;

      const popup = window.open(
        authUrl,
        "OAuthPopup",
        `width=${width},height=${height},top=${top},left=${left}`
      );

      const interval = setInterval(() => {
        if (popup.closed) {
          clearInterval(interval);
          console.log("OAuth popup closed. Refresh account info.");
        }
      }, 1000);
      dispatch(fetchServices());
    } catch (err) {
      console.error("Failed to open OAuth popup:", err);
    }
  };

  const hasTrigger =
    workflow.steps &&
    workflow.steps.length > 0 &&
    workflow.steps[0].type === "trigger";

  return (
    <div className="fixed inset-0 z-50 bg-gray-100 bg-opacity-90 flex items-center justify-center">
      <div className="backdrop-blur-xl rounded-3xl p-10 w-full max-w-3xl ">
        <div className="flex items-center justify-between pb-6 border-b border-gray-100 mb-6">
          <div className="flex items-center">
            <Zap className="text-blue-600 mr-4 flex-shrink-0" size={32} />
            {isEditingWorkflowName ? (
              <input
                type="text"
                value={workflow.name} // Read from Redux state
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
                {workflow.name || "Unnamed Workflow"}{" "}
                {/* Display name from Redux, or default */}
              </h3>
            )}
          </div>
          <div className="flex items-center space-x-4">
            <button
              onClick={() => toggleWorkflow(workflow.id)}
              className={`relative inline-flex h-8 w-16 items-center rounded-full transition-colors duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2 ${
                workflow.enabled // Read from Redux state
                  ? "bg-green-500 focus:ring-green-600"
                  : "bg-gray-300 focus:ring-gray-400"
              }`}
              aria-checked={workflow.enabled}
              role="switch"
            >
              <span
                className={`inline-block h-6 w-6 transform rounded-full bg-white transition-transform duration-300 ${
                  workflow.enabled ? "translate-x-9" : "translate-x-1"
                }`}
              />
              <span className="absolute inset-0 flex items-center justify-between px-1.5">
                {workflow.enabled ? (
                  <Power className="text-white ml-0.5" size={16} />
                ) : (
                  <Power className="text-gray-600 mr-0.5" size={16} />
                )}
              </span>
            </button>
            <Button
              children={"Save"}
              onClick={handleSaveWorkflow}
              variant="primary"
            />
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

        <div className=" rounded-2xl p-6 ">
          {/* Trigger Section */}
          {!hasTrigger && (
            <button
              onClick={openAddTriggerModal}
              className="w-full flex items-center justify-center px-4 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700 transition-colors duration-200 text-base transform hover:scale-[1.01] focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 mb-4"
            >
              <PlusCircle size={20} className="mr-2" />
              Add Trigger
            </button>
          )}

          {workflow.steps &&
            workflow.steps.map((step, index) => (
              <StepCard
                key={step.id}
                step={step}
                index={index}
                onDragStart={handleDragStart}
                onDragOver={handleDragOver}
                onDrop={handleDrop}
                onDeleteStep={handleDeleteStep}
                isDraggable={step.type === "action"}
                onClick={handleSelectStep}
              />
            ))}

          <button
            onClick={openAddActionModal}
            className="mt-6 w-full flex items-center justify-center px-4 py-3 bg-purple-600 text-white font-semibold rounded-lg shadow-md hover:bg-purple-700 transition-colors duration-200 text-base transform hover:scale-[1.01] focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2"
          >
            <PlusCircle size={20} className="mr-2" />
            Add Action
          </button>
        </div>
      </div>
      <ServiceSelectionModal
        isOpen={showServiceSelectionModal}
        onClose={() => {
          setShowServiceSelectionModal(false);
          setIsFromSidebarForService(null);
        }}
        getSidebar={handleSelectedService}
        supportedApplications={supportedApplications}
        stepType={addingStepType}
        onEditFromSidebar={handleServiceSelectedAndAddStep}
        dataFromSidebar={isFromSidebarForService}
      />
      <EventSelectionModal
        isOpen={showEventSelectionModal}
        onClose={() => {
          setShowEventSelectionModal(false);
          setIsFromSidebarForEvent(null);
        }}
        onSelectEvent={handleEventSelectedAndAddStep}
        dataFromSidebar={isFromSidebarForEvent}
      />
      <StepDetailSidebar
        isOpen={selectedStep !== null}
        index={selectedStep}
        chooseService={handleSelectServiceFromSidebar}
        chooseEvent={handleSelectEventFromSidebar}
        onClose={selectedStepNull}
        updateConfig={handleChangeConfig}
        openOAuthPopup={openAuthServices}
      />
    </div>
  );
};

export default Workflow;
