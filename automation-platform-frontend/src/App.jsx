import React, { Suspense, lazy } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import Loading from "./pages/Loading";
import PrivateHome from "./pages/PrivateHome";
import PrivateRoute from "./components/PrivateRoute";
import WorkflowPage from "./pages/WorkflowPage";
const Signup = lazy(() => import("./pages/Signup"));
const Signin = lazy(() => import("./pages/Signin"));
const PublicHome = lazy(() => import("./pages/PublicHome"));

function App() {
  return (
    <Router>
      <div className="App">
        <Suspense fallback={<Loading />}>
          <Routes>
            <Route path="/" exact element={<PublicHome />} />
            <Route path="/auth/signin" exact element={<Signin />} />
            <Route path="/auth/signup" exact element={<Signup />} />
            <Route
              path="/app/home"
              exact
              element={
                <PrivateRoute>
                  <PrivateHome />
                </PrivateRoute>
              }
            />
            <Route
              path="/app/workflow"
              exact
              element={
                <PrivateRoute>
                  <WorkflowPage />
                </PrivateRoute>
              }
            />
          </Routes>
          <ToastContainer
            position="top-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="colored"
          />
        </Suspense>
      </div>
    </Router>
  );
}

export default App;
