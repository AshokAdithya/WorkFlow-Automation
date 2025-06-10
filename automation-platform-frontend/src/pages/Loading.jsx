// src/components/Loading.jsx
import React from "react";

const Loading = () => {
  return (
    <div className="flex flex-col items-center justify-center w-screen h-screen bg-gray-50 text-gray-700">
      <div className="flex space-x-3 mb-4">
        <div className="w-6 h-6 bg-orange-500 rounded-full animate-bounce [animation-delay:-0.3s]"></div>
        <div className="w-6 h-6 bg-orange-500 rounded-full animate-bounce [animation-delay:-0.15s]"></div>
        <div className="w-6 h-6 bg-orange-500 rounded-full animate-bounce"></div>
      </div>
      <p className="text-2xl font-semibold text-gray-500 tracking-wide">
        Loading your workspace...
      </p>
    </div>
  );
};

export default Loading;
