import React, { useState } from "react";
import { X, Search } from "lucide-react";

const ServiceSelectionModal = ({
  isOpen,
  onClose,
  getSidebar,
  supportedApplications,
  stepType,
  onEditFromSidebar,
  dataFromSidebar,
}) => {
  const [searchTerm, setSearchTerm] = useState("");
  if (!isOpen && !dataFromSidebar) return null;

  const filteredApps = supportedApplications.filter(
    (app) =>
      app.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      app.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="fixed inset-0 z-50 p-4 flex items-center justify-center bg-gray-100 bg-opacity-100 ">
      <div className="w-full max-w-4xl max-h-[90vh] bg-white rounded-xl shadow-2xl overflow-y-auto flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-2xl font-bold text-gray-800">
            Choose an App to{" "}
            {stepType === "trigger" ? "Start Your Workflow" : "Add an Action"}
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
              placeholder="Search apps..."
              className="w-full pl-10 pr-4 py-3 text-lg border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {/* App Grid */}
        <div className="p-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {filteredApps.length > 0 ? (
            filteredApps.map((app) => (
              <div
                key={app.id}
                onClick={() =>
                  dataFromSidebar
                    ? onEditFromSidebar(dataFromSidebar, app)
                    : getSidebar(app, stepType)
                }
                className="p-4 bg-gray-50 border border-gray-200 rounded-lg flex flex-col items-center text-center cursor-pointer hover:bg-blue-50 hover:shadow-md transition-all"
              >
                <img
                  src={
                    app.logoUrl ||
                    `https://placehold.co/40x40/cccccc/000000?text=${app.name.charAt(
                      0
                    )}`
                  }
                  alt={`${app.name} Logo`}
                  className="w-16 h-16 object-contain mb-3 rounded-full border border-gray-100"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = `https://placehold.co/40x40/cccccc/000000?text=${app.name.charAt(
                      0
                    )}`;
                  }}
                />
                <h3 className="text-base font-semibold text-gray-800 mb-1">
                  {app.name}
                </h3>
                <p className="text-sm text-gray-600 line-clamp-2">
                  {app.description}
                </p>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center text-gray-500 p-8">
              No applications found matching your search.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ServiceSelectionModal;
