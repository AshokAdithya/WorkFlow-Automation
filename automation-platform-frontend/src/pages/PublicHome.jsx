import Header from "../components/Header";
import Button from "../components/Button";
import FeatureCard from "../components/FeatureCard";
import React, { useEffect } from "react";
import { FaBolt, FaCogs, FaProjectDiagram } from "react-icons/fa";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import useAuth from "../hooks/TokenManagement";

const PublicHome = () => {
  const { loading } = useAuth();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const navigate = useNavigate();

  useEffect(() => {
    if (!loading && isAuthenticated) {
      navigate("/app/home", { replace: true });
    }
  }, [isAuthenticated, navigate, loading]);

  const GoogleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };
  return (
    <>
      <Header />

      {/* Hero */}
      <section className="bg-gradient-to-br from-blue-50 to-white py-24 px-6">
        <div className="w-screen text-center">
          <h1 className="text-5xl font-extrabold text-gray-900 mb-6">
            Automate Everything with{" "}
            <span className="text-blue-600">AutomateX</span>
          </h1>
          <p className="text-xl text-gray-600 mb-8">
            Connect your favorite apps and automate your workflows in minutes —
            no code needed.
          </p>
          <div className="flex flex-row gap-6 justify-center mx-auto ">
            <Button href="/auth/signup" variant="primary">
              Get Started Free
            </Button>
            <button
              onClick={GoogleLogin}
              type="button"
              className="inline-flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-1 focus:ring-indigo-500 transition"
            >
              <img
                src="https://lh3.googleusercontent.com/COxitqgJr1sJnIDe8-jiKhxDx1FrYbtRHKJ9z_hELisAlapwE9LUPh6fcXIfb5vwpbMl4xl9H9TRFPc5NOO8Sb3VSgIBrfRYvW6cUA"
                alt="Google logo"
                className="w-5 h-5 mr-2"
              />
              Sign up with Google
            </button>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="py-20 bg-white px-6">
        <div className="max-w-6xl mx-auto text-center mb-16">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">
            Why AutomateX?
          </h2>
          <p className="text-gray-600 text-lg">
            Simplify your work. Focus on what matters.
          </p>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
          <FeatureCard
            icon={<FaBolt />}
            title="Instant Automation"
            description="Create powerful workflows in just a few clicks. No coding required."
          />
          <FeatureCard
            icon={<FaCogs />}
            title="Flexible & Customizable"
            description="Connect any app, set custom triggers and conditions tailored to your needs."
          />
          <FeatureCard
            icon={<FaProjectDiagram />}
            title="Multi-Step Automation"
            description="Link multiple actions and apps into seamless, automated chains — all from a single trigger."
          />
        </div>
      </section>

      {/* Call to Action */}
      <section className="py-20 bg-blue-600 text-white px-6">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl font-bold mb-4">
            Ready to automate your life?
          </h2>
          <p className="mb-8 text-lg">
            Join thousands who are saving time with AutomateX.
          </p>
          <Button href="/auth/signup" variant="secondary">
            Start Free Today
          </Button>
        </div>
      </section>

      <footer className="py-8 text-center text-gray-400 text-sm">
        &copy; {new Date().getFullYear()} AutomateX. All rights reserved.
      </footer>
    </>
  );
};

export default PublicHome;
