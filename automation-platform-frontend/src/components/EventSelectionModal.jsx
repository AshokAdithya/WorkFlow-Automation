import React, { useState } from "react";
import { X, Search } from "lucide-react";
import { useSelector } from "react-redux";

const EventSelectionModal = ({
  isOpen,
  onClose,
  onSelectEvent,
  dataFromSidebar,
}) => {
  const [searchTerm, setSearchTerm] = useState("");
  const services = useSelector((state) => state.services.apps);

  if (!isOpen || !dataFromSidebar) return null;

  const selectedApp = services.find((app) => app.id === dataFromSidebar.app);
  const events =
    dataFromSidebar.type === "trigger"
      ? selectedApp?.triggerDefinitions || []
      : selectedApp?.actionDefinitions || [];

  const filteredEvents = events.filter(
    (event) =>
      event.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      (event.description &&
        event.description.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  return (
    <div className="fixed inset-0 z-50 p-4 flex items-center justify-center bg-gray-100 bg-opacity-100">
      <div className="w-full max-w-4xl max-h-[90vh] bg-white rounded-xl shadow-2xl overflow-y-auto flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-2xl font-bold text-gray-800">
            Choose a {dataFromSidebar.type === "trigger" ? "Trigger" : "Action"}{" "}
            for {selectedApp?.name}
          </h2>
          <button
            onClick={onClose}
            className="p-2 rounded-full text-gray-500 hover:text-gray-700 hover:bg-gray-100 transition-colors"
          >
            <X size={24} />
          </button>
        </div>

        {/* Search */}
        <div className="p-6 border-b border-gray-200">
          <div className="relative">
            <Search
              size={20}
              className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
            />
            <input
              type="text"
              placeholder={`Search ${
                dataFromSidebar.type === "trigger" ? "triggers" : "actions"
              }...`}
              className="w-full pl-10 pr-4 py-3 text-lg border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {/* Event Grid */}
        <div className="p-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 overflow-y-auto">
          {filteredEvents.length > 0 ? (
            filteredEvents.map((event) => (
              <div
                key={event.id}
                onClick={() => onSelectEvent(dataFromSidebar, event)}
                className="bg-gray-50 border border-gray-200 rounded-lg p-4 text-center cursor-pointer hover:bg-green-50 hover:shadow-md transition-all duration-200"
              >
                <h3 className="font-semibold text-gray-800 text-base mb-1">
                  {event.name}
                </h3>
                <p className="text-sm text-gray-600 line-clamp-2">
                  {event.description}
                </p>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center text-gray-500 p-8">
              No {dataFromSidebar.type === "trigger" ? "triggers" : "actions"}{" "}
              found for {selectedApp?.name}.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default EventSelectionModal;
