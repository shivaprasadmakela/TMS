import { useDispatch, useSelector } from "react-redux";
import { logout } from "../store/slices/authSlice";
import { RootState } from "../store";
import { useNavigate } from "react-router-dom";
import { AppDispatch } from "../store"; 

const Dashboard = () => {
  const dispatch = useDispatch<AppDispatch>(); 
  const auth = useSelector((state: RootState) => state.auth);
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    navigate("/signin");
  };

  return (
    <div>
      <h1>Dashboard</h1>
      <p>Welcome, {auth.user?.firstName || "User"}!</p>
      <button onClick={handleLogout}>Sign Out</button>
    </div>
  );
};

export default Dashboard;
