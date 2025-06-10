import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "../hooks/TokenManagement";
import { api } from "../api/Api";
import { toast } from "react-toastify";

const Signin = () => {
  const navigate = useNavigate();
  const { isAuthenticated, loading } = useAuth();

  const [data, setData] = useState({
    email: "",
    password: "",
  });

  useEffect(() => {
    if (!loading && isAuthenticated) {
      navigate("/app/home", { replace: true });
    }
  }, [loading, isAuthenticated, navigate]);

  const handleChange = ({ currentTarget: input }) => {
    setData({ ...data, [input.name]: input.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/auth/signin", data);
      toast.success(res.data.message || "Signed in successfully");
      navigate("/app/home");
    } catch (error) {
      if (
        error.response &&
        error.response.status >= 400 &&
        error.response.status <= 500
      ) {
        toast.error(error.response.data.message || "Invalid credentials");
      } else {
        toast.error("Something went wrong. Please try again.");
      }
    }
  };

  const GoogleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  return (
    <div className="min-h-screen w-screen bg-white flex items-center justify-center px-16 py-12 sm:px-24">
      <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-16 items-center">
        {/* New Feature Highlights for Signin */}
        <div className="text-center md:text-left">
          <h2 className="text-4xl font-bold text-gray-900 mb-6">
            What You Get After Signing In
          </h2>
          <ul className="space-y-5 text-gray-700">
            <li className="flex items-start">
              <span className="text-indigo-600 font-bold mr-2">✓</span>{" "}
              Personalized Dashboard — Your productivity at a glance
            </li>
            <li className="flex items-start">
              <span className="text-indigo-600 font-bold mr-2">✓</span> Smart
              Notifications — Stay updated without distractions
            </li>
            <li className="flex items-start">
              <span className="text-indigo-600 font-bold mr-2">✓</span> Seamless
              Integrations — Connect with tools you already use
            </li>
            <li className="flex items-start">
              <span className="text-indigo-600 font-bold mr-2">✓</span> Secure
              Data Storage — We keep your data safe and private
            </li>
            <li className="flex items-start">
              <span className="text-indigo-600 font-bold mr-2">✓</span> 24/7
              Support — We're here whenever you need help
            </li>
          </ul>
        </div>

        {/* Signin Form */}
        <div className="flex items-center justify-center">
          <div className="w-full max-w-md bg-white rounded-lg shadow-md p-8 space-y-6">
            <h2 className="text-3xl font-extrabold text-gray-900 text-center">
              Sign in to your account
            </h2>

            <button
              onClick={GoogleLogin}
              type="button"
              className="w-full inline-flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-1 focus:ring-indigo-500 transition"
            >
              <img
                src="https://lh3.googleusercontent.com/COxitqgJr1sJnIDe8-jiKhxDx1FrYbtRHKJ9z_hELisAlapwE9LUPh6fcXIfb5vwpbMl4xl9H9TRFPc5NOO8Sb3VSgIBrfRYvW6cUA"
                alt="Google logo"
                className="w-5 h-5 mr-2"
              />
              Sign in with Google
            </button>

            <div className="relative flex items-center justify-center text-gray-400">
              <span className="bg-white px-2 text-sm z-10">or</span>
              <div className="absolute border-t border-gray-300 w-full top-1/2 transform -translate-y-1/2"></div>
            </div>

            <form className="space-y-5" onSubmit={handleSubmit}>
              <div>
                <label
                  htmlFor="email"
                  className="block text-sm font-medium text-gray-700"
                >
                  Email address
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  required
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  placeholder="you@example.com"
                  onChange={handleChange}
                  value={data.email}
                />
              </div>

              <div>
                <label
                  htmlFor="password"
                  className="block text-sm font-medium text-gray-700"
                >
                  Password
                </label>
                <input
                  type="password"
                  id="password"
                  name="password"
                  required
                  minLength={6}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  placeholder="••••••••"
                  onChange={handleChange}
                  value={data.password}
                />
              </div>

              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition font-semibold text-sm"
              >
                Sign in
              </button>
            </form>

            <p className="text-center text-sm text-gray-600">
              Don't have an account?{" "}
              <a
                href="/auth/signup"
                className="font-medium text-indigo-600 hover:text-indigo-500"
              >
                Sign up
              </a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Signin;
