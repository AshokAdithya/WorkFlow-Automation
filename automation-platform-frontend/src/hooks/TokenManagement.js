import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { setAuthenticated, setUnauthenticated } from "../redux/AuthSlice";
import { api } from "../api/Api";

const useAuth = () => {
  const [loading, setLoading] = useState(true);
  const dispatch = useDispatch();

  useEffect(() => {
    const refreshAccessToken = async () => {
      try {
        await api.post("/auth/refresh-token", {});
        dispatch(setAuthenticated());
      } catch (err) {
        dispatch(setUnauthenticated());
      } finally {
        setLoading(false);
      }
    };

    refreshAccessToken();
  }, [dispatch]);

  return { loading };
};

export default useAuth;
