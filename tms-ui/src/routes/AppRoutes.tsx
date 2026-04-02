import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import Signin from "../pages/SignIn";
import Signup from "../pages/SignUp";
import Dashboard from "../pages/Dashboard";
import MainLayout from "../components/layout/MainLayout";
import ProtectedRoute from "./ProtectedRoute";
import ProjectList from "../pages/projects/ProjectList";
import ProjectDetails from "../pages/projects/ProjectDetails";

const AppRoutes = () => (
  <Router>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/signin" element={<Signin />} />
      <Route path="/signup" element={<Signup />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/projects" element={<ProjectList />} />
          <Route path="/projects/:id" element={<ProjectDetails />} />
        </Route>
      </Route>
    </Routes>
  </Router>
);

export default AppRoutes;
