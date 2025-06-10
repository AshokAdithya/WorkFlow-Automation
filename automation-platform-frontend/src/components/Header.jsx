import Button from "./Button";
import { Link, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { FaUserCircle } from "react-icons/fa";
import { api } from "../api/Api";
import { toast } from "react-toastify";

const Header = () => {
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const navigate = useNavigate();

  const HandleLogout = async () => {
    try {
      const res = await api.post("/auth/logout", {});
      toast.success(res.data);
      navigate("/", { replace: true });
    } catch (err) {
      toast.error("Error while logging out");
    }
  };
  return (
    <header className="flex justify-between items-center max-w-7xl mx-auto py-6 px-6">
      <h1 className="text-2xl font-bold text-blue-600 cursor-pointer">
        AutomateX
      </h1>
      <nav className="flex items-center gap-8">
        {!isAuthenticated ? (
          <>
            <a href="/auth/signin" className="text-gray-700 hover:underline">
              Sign In
            </a>
            <Button href="/auth/signup">Get Started</Button>
          </>
        ) : (
          <div className="flex items-center gap-6">
            <Link to="/app/profile">
              <FaUserCircle className="text-gray-600 text-4xl cursor-pointer hover:text-gray-800" />
            </Link>
            <Button variant="secondary" onClick={HandleLogout}>
              Logout
            </Button>
          </div>
        )}
      </nav>
    </header>
  );
};

export default Header;
