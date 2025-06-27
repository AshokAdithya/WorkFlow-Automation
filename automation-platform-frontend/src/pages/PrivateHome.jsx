// import {
//   FaPlus,
//   FaGlobe,
//   FaRedo,
//   FaFilter,
//   FaFolder,
//   FaBolt,
// } from "react-icons/fa";
// import Header from "../components/Header";
// import { useNavigate } from "react-router-dom";

// const PrivateHome = () => {
//   const navigate = useNavigate();

//   const createWorkflow = () => {
//     navigate("/app/workflow");
//   };
//   return (
//     <>
//       <Header />
//       <div className="bg-[#fdfcf9] min-h-screen w-screen flex justify-center px-4 md:px-6 py-10">
//         <main className="w-screen max-w-7xl">
//           {/* Top Bar */}
//           <div className="max-w-7xl w-full mx-auto mb-10">
//             <div className="flex flex-wrap justify-between items-center gap-4">
//               {/* Tabs */}
//               <div className="flex items-center gap-2">
//                 <button className="flex items-center gap-1 text-xl px-3 py-1.5 border rounded-full font-medium bg-white text-purple-600 border-purple-200 shadow-sm hover:bg-purple-50 focus:outline-none focus:ring-2 focus:ring-purple-200">
//                   <FaBolt className="text-purple-500" />
//                   Zyncs
//                 </button>
//               </div>

//               {/* Filters + Search */}
//               <div className="flex flex-wrap items-center gap-2">
//                 <select className="border text-xl px-3 py-1.5 rounded-md bg-white text-gray-700 focus:outline-none focus:ring-2 focus:ring-purple-200">
//                   <option>All</option>
//                   <option>Active</option>
//                   <option>Archived</option>
//                 </select>
//                 <button className="flex items-center gap-1 border px-3 py-1.5 text-xl rounded-md bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-200">
//                   <FaFilter />
//                   Filters
//                 </button>
//                 <button className="p-3 border rounded-md bg-white text-gray-600 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-200">
//                   <FaRedo />
//                 </button>
//                 <input
//                   type="text"
//                   placeholder="Search by name or webhook"
//                   className="border px-3 py-2 text-m rounded-md bg-white w-64 focus:outline-none focus:ring-2 focus:ring-purple-200"
//                 />
//               </div>

//               {/* Right Buttons */}
//               <div className="flex items-center gap-2">
//                 <button className="text-m px-4 py-2 rounded-md border bg-white text-gray-600 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-200">
//                   Trash
//                 </button>
//                 <button
//                   onClick={createWorkflow}
//                   className="text-m px-4 py-2 rounded-md bg-purple-600 text-white hover:bg-purple-700 flex items-center gap-1 focus:outline-none focus:ring-2 focus:ring-purple-300 hover:cursor-pointer"
//                 >
//                   <FaPlus />
//                   Create
//                 </button>
//               </div>
//             </div>
//           </div>

//           {/* Empty State */}
//           <div className="max-w-5xl mx-auto bg-[#f7f5ee] border rounded-xl p-12 text-center">
//             <div className="text-3xl mb-4 text-gray-800 font-semibold">
//               You haven’t created a Zync yet
//             </div>
//             <p className="text-gray-600 mb-8">
//               Build automated workflows by creating your first Zync.
//             </p>
//             <div className="flex justify-center gap-4">
//               <button className="flex items-center gap-2 px-4 py-2 border rounded-md bg-white hover:bg-gray-50 text-sm focus:outline-none focus:ring-2 focus:ring-gray-300">
//                 <FaGlobe className="text-gray-500" />
//                 Explore templates
//               </button>
//               <button
//                 onClick={createWorkflow}
//                 className="flex items-center gap-2 px-4 py-2 rounded-md bg-purple-600 hover:bg-purple-700 text-white text-sm focus:outline-none focus:ring-2 focus:ring-purple-400 hover:cursor-pointer"
//               >
//                 <FaPlus />
//                 Create Zync
//               </button>
//             </div>
//           </div>

//           {/* Footer */}
//           <div className="text-center text-sm text-gray-500 mt-20">
//             © {new Date().getFullYear()} AutomateX Inc. &nbsp; · &nbsp;
//             <a href="#" className="underline hover:text-gray-700">
//               Legal
//             </a>
//             &nbsp; · &nbsp;
//             <a href="#" className="underline hover:text-gray-700">
//               Privacy
//             </a>
//           </div>
//         </main>
//       </div>
//     </>
//   );
// };

// export default PrivateHome;

import { FaPlus, FaGlobe, FaRedo, FaFilter, FaBolt } from "react-icons/fa";
import Header from "../components/Header";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { api } from "../api/Api";
import { toast } from "react-toastify";

const PrivateHome = () => {
  const navigate = useNavigate();
  const [workflows, setWorkflows] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const createWorkflow = (id) => {
    navigate("/app/workflow", { state: { id: id } });
  };

  const fetchWorkflows = async () => {
    try {
      const res = await api.get("/workflows");
      const data = Array.isArray(res.data) ? res.data : [];
      setWorkflows(data);
    } catch (err) {
      console.error("Failed to fetch workflows", err);
      setWorkflows([]);
    }
  };

  const filteredWorkflows = workflows.filter((wf) =>
    (wf.name || "Untitled Zync")
      .toLowerCase()
      .includes(searchTerm.toLowerCase())
  );

  const toggleWorkflow = async (workflowId, currentStatus) => {
    try {
      await api.put(`/workflows/${workflowId}/toggle`, {
        enabled: !currentStatus,
      });
      fetchWorkflows(); // refresh
    } catch (err) {
      toast.error("Toggle failed", err);
    }
  };

  useEffect(() => {
    fetchWorkflows();
  }, []);

  return (
    <>
      <Header />
      <div className="bg-[#fdfcf9] min-h-screen w-screen flex justify-center px-4 md:px-6 py-10">
        <main className="w-screen max-w-7xl">
          {/* Top Bar */}
          <div className="max-w-7xl w-full mx-auto mb-10">
            <div className="flex flex-wrap justify-between items-center gap-4">
              {/* Tabs */}
              <div className="flex items-center gap-2">
                <button className="flex items-center gap-1 text-xl px-3 py-1.5 border rounded-full font-medium bg-white text-purple-600 border-purple-200 shadow-sm hover:bg-purple-50 focus:outline-none focus:ring-2 focus:ring-purple-200">
                  <FaBolt className="text-purple-500" />
                  Zyncs
                </button>
              </div>

              {/* Filters + Search */}
              <div className="flex flex-wrap items-center gap-2">
                <button
                  className="p-3 border rounded-md bg-white text-gray-600 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-200"
                  onClick={fetchWorkflows}
                >
                  <FaRedo />
                </button>
                <input
                  type="text"
                  placeholder="Search by name"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="border px-3 py-2 text-m rounded-md bg-white w-64 focus:outline-none focus:ring-2 focus:ring-purple-200"
                />
              </div>

              {/* Right Buttons */}
              <div className="flex items-center gap-2">
                <button className="text-m px-4 py-2 rounded-md border bg-white text-gray-600 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-200">
                  Trash
                </button>
                <button
                  onClick={() => {
                    createWorkflow(null);
                  }}
                  className="text-m px-4 py-2 rounded-md bg-purple-600 text-white hover:bg-purple-700 flex items-center gap-1 focus:outline-none focus:ring-2 focus:ring-purple-300 hover:cursor-pointer"
                >
                  <FaPlus />
                  Create
                </button>
              </div>
            </div>
          </div>

          <div className="max-w-5xl mx-auto bg-[#f7f5ee] border rounded-xl p-6">
            <table className="w-full text-left">
              <thead>
                <tr className="text-sm text-gray-600 border-b">
                  <th className="py-2 pr-4">Name</th>
                  <th className="py-2 pr-4">Apps</th>
                  <th className="py-2 pr-4">Last modified</th>
                  <th className="py-2 pr-2 text-right">Status</th>
                </tr>
              </thead>
              <tbody>
                {workflows.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="py-12 text-center text-gray-600">
                      You haven’t created a Zync yet
                    </td>
                  </tr>
                ) : (
                  filteredWorkflows.map((wf) => (
                    <tr
                      key={wf.id}
                      className="border-b hover:bg-[#f6f3e6] transition text-gray-800"
                    >
                      {/* Name with Icon */}
                      <td
                        className="py-3 pr-4 flex items-center gap-2 font-medium cursor-pointer"
                        onClick={() => {
                          createWorkflow(wf.id);
                        }}
                      >
                        <FaBolt className="text-orange-500" />
                        {wf.name || "Untitled Zync"}
                      </td>

                      {/* Apps */}
                      <td className="py-3 pr-4">
                        <div className="flex gap-2">
                          {[
                            wf.trigger?.appIntegration,
                            ...(wf.actions || []).map((a) => a.appIntegration),
                          ]
                            .filter(Boolean)
                            .map((app, idx) => (
                              <img
                                key={idx}
                                src={app.logoUrl || "/default-app.png"}
                                alt="app"
                                className="w-7 h-7 rounded-md border bg-white"
                              />
                            ))}
                        </div>
                      </td>

                      {/* Last modified */}
                      <td className="py-3 pr-4 text-sm text-gray-600 whitespace-nowrap">
                        {Math.abs(
                          Date.now() - new Date(wf.updatedAt).getTime()
                        ) <
                        1000 * 60 * 60 * 24
                          ? `${Math.floor(
                              (Date.now() - new Date(wf.updatedAt).getTime()) /
                                (1000 * 60)
                            )} minutes ago`
                          : new Date(wf.updatedAt).toLocaleDateString("en-US", {
                              year: "numeric",
                              month: "short",
                              day: "numeric",
                            })}
                      </td>

                      {/* Status toggle */}
                      <td className="py-3 pr-2 text-right">
                        <label className="inline-flex items-center cursor-pointer">
                          <input
                            type="checkbox"
                            className="sr-only"
                            checked={wf.enabled}
                            onChange={() => toggleWorkflow(wf.id, wf.enabled)}
                            readOnly
                          />
                          <div
                            className={`w-10 h-5 flex items-center rounded-full p-1 duration-300 ${
                              wf.enabled ? "bg-purple-500" : "bg-gray-300"
                            }`}
                          >
                            <div
                              className={`bg-white w-4 h-4 rounded-full shadow-md transform duration-300 ${
                                wf.enabled ? "translate-x-5" : "translate-x-0"
                              }`}
                            ></div>
                          </div>
                        </label>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          {/* Footer */}
          <div className="text-center text-sm text-gray-500 mt-20">
            © {new Date().getFullYear()} AutomateX Inc. &nbsp; · &nbsp;
            <a href="#" className="underline hover:text-gray-700">
              Legal
            </a>
            &nbsp; · &nbsp;
            <a href="#" className="underline hover:text-gray-700">
              Privacy
            </a>
          </div>
        </main>
      </div>
    </>
  );
};

export default PrivateHome;
